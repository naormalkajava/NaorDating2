package com.example.naormalka.naordating2;


import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {


    ImageView ivProfile;
    TextView etProfile;


    private String userSex;
    private String oppisteUser;
    private  DatabaseReference userDb;
    private String currentUId;

    ListView listView;
    List<Cards> rowItems;


    SharedPreferences.Editor editor;


    Button btnLogOut;
    SwipeFlingAdapterView flingContainer;
    private Cards cards_data[];
    ArrayAdapterCards arrayAdapterCards;
    FirebaseAuth mAuth;
    SharedPreferences pref;

    void sha1() {
        try {


            PackageInfo info = getPackageManager().getPackageInfo("com.example.naormalka.naordating2", PackageManager.GET_SIGNATURES);
            for (android.content.pm.Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        sha1();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userDb = FirebaseDatabase.getInstance().getReference().child("Users");


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mAuth = FirebaseAuth.getInstance();


//btn click register
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            GoToLoginActivity();
            finish();
            return;
        }


        btnLogOut = (Button) findViewById(R.id.btnlogOut);
        btnLogOut.setOnClickListener(this);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View hView = navigationView.getHeaderView(0);
        ivProfile = (ImageView) hView.findViewById(R.id.profile);
        etProfile = (TextView) hView.findViewById(R.id.etProfile);

        etProfile.setText(currentUser.getDisplayName());
        if (currentUser.getPhotoUrl() != null) {
            Glide.with(this).load(currentUser.getPhotoUrl().toString()).into(ivProfile);
        }

        SwipeCard();
    }
    // private String getAge(int year, int month, int day){
    //     Calendar dob = Calendar.getInstance();
    //     Calendar today = Calendar.getInstance();
//
    //     dob.set(year, month, day);
//
    //     int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
//
    //     if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
    //         age--;
    //     }
//
    //     Integer ageInt = new Integer(age);
    //     String ageS = ageInt.toString();
//
    //     return ageS;
    // }


    private void SwipeCard() {

        rowItems = new ArrayList<Cards>();
        checkUserSex();


        //choose your favorite adapter
        arrayAdapterCards = new ArrayAdapterCards(this, R.layout.item, rowItems);


        flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);

        //set the listener and the adapter
        flingContainer.setAdapter(arrayAdapterCards);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");
                rowItems.remove(0);
                arrayAdapterCards.notifyDataSetChanged();
            }


            @Override
            public void onLeftCardExit(Object dataObject) {
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                Cards obj = (Cards) dataObject;
                String userId = obj.getUserId();
                String name = obj.getName();
               // userDb.child(oppisteUser).child(userId).child("connections").child("nope").child(currentUser.getUid()).setValue(true);
                userDb.child(oppisteUser).child(userId).child("connections").child("nope").child(currentUser.getUid()).setValue(true);
                Toast.makeText(MainActivity.this, "Left!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                Cards obj = (Cards) dataObject;
                String userId = obj.getUserId();
                String name = obj.getName();
             //   userDb.child(oppisteUser).child(userId).child("connections").child("yeps").child(currentUser.getUid()).setValue(true);
                userDb.child(oppisteUser).child(userId).child("connections").child("yeps").child(currentUser.getUid()).setValue(true);
              isConnectionMatch(userId);
                Toast.makeText(MainActivity.this, "Right!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                // Ask for more data here

            }

            @Override
            public void onScroll(float v) {

            }
        });

        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                Toast.makeText(MainActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void isConnectionMatch(String userId) {
        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        final DatabaseReference userDb = FirebaseDatabase.getInstance().getReference("Users");

        DatabaseReference currectuserConnectionDB = userDb.child(userSex).child(currentUser.getUid()).child("connections").child("yeps").child(userId);
            currectuserConnectionDB.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){

                        Toast.makeText(MainActivity.this, "new Connection", Toast.LENGTH_LONG).show();
                        userDb.child(oppisteUser).child(dataSnapshot.getKey()).child("connections").child("matches").child(currentUser.getUid()).setValue(true);
                        userDb.child(userSex).child(currentUser.getUid()).child("connections").child("matches").child(dataSnapshot.getKey()).setValue(true);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

    }


    private void GoToLoginActivity() {
        Intent intent = new Intent(this, ChooseLoginRegistrationActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_chat) {

        } else if (id == R.id.nav_location) {

        } else if (id == R.id.nav_setting) {

        } else if (id == R.id.nav_invite) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void checkUserSex() {

        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) return;
        DatabaseReference maleDB = FirebaseDatabase.getInstance().getReference().child("Users").child("male");
        maleDB.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getKey().equals(currentUser.getUid())) {
                    userSex = "male";
                    oppisteUser = "female";
                    getOppositeSexUSer();

                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        // female DataBase


        DatabaseReference femaleDB = FirebaseDatabase.getInstance().getReference().child("Users").child("female");
        femaleDB.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getKey().equals(currentUser.getUid())) {

                    userSex = "female";
                    oppisteUser = "male";
                    getOppositeSexUSer();
                }


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    public void getOppositeSexUSer() {

        final DatabaseReference oppositeDB = FirebaseDatabase.getInstance().getReference().child("Users").child(oppisteUser);

        oppositeDB.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (dataSnapshot.exists() && !dataSnapshot.child("connections").child("nope").hasChild(currentUser.getUid()) && !dataSnapshot.child("connections").child("yeps").hasChild(currentUser.getUid()))  {
                    Cards item = new Cards(dataSnapshot.getKey(),dataSnapshot.child("name").getValue().toString(),null);
                    rowItems.add(item);
                    arrayAdapterCards.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    private String getNameFromDataBase(DataSnapshot dataSnapshot) {
        Object value = dataSnapshot.getValue();
        String s1 = value.toString();
        String s2 = s1.toString();
        String replace = s2.replace("{", "");
        String data = s1.replaceAll("\\=.*?\\}", "");
        String name = data.replace("{", "").replace("}", "").replace("connections," , "");
        return name;
    }

    private String getImageFromDataBase(DataSnapshot dataSnapshot) {
        String s1 = dataSnapshot.getValue().toString();
        String s2 = s1.replaceAll("\\=.*?\\,", "");
        String s3 = s2.replaceAll("\\{.*?\\=", "");
        String replace = s3.replace("}}", "");
        return replace;
    }

    @Override
    public void onClick(View view) {
        mAuth.signOut();
        LoginManager.getInstance().logOut();

        Intent intent = new Intent(MainActivity.this, ChooseLoginRegistrationActivity.class);
        startActivity(intent);
        finish();
        return;
    }


//swipeView
}


