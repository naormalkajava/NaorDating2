package com.example.naormalka.naordating2;


import android.*;
import android.Manifest;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapCircleThumbnail;
import com.bumptech.glide.Glide;
import com.bumptech.glide.disklrucache.DiskLruCache;
import com.facebook.login.LoginManager;
import com.facebook.places.internal.LocationPackageManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lorentzos.flingswipe.FlingCardListener;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.R.attr.name;
import static android.R.attr.value;
import static com.example.naormalka.naordating2.R.id.phone;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ImageView ivProfile;
  //  private ProgressBar circular_bar;
    private TextView etProfile;
    private String userSex;
    private String oppisteUser;
    private DatabaseReference userDb;
    private List<Cards> rowItems;
    private SwipeFlingAdapterView flingContainer;
    private ArrayAdapterCards arrayAdapterCards;
    private FirebaseAuth mAuth;
    private BootstrapCircleThumbnail like;
    private BootstrapCircleThumbnail dislike;
    FileOutputStream fos = null;
    FileOutputStream fosUSerNAme = null;
    DatabaseReference databaseReference;

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
    protected void onPause() {
        super.onPause();


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
     //  circular_bar = (ProgressBar) findViewById(R.id.circular_bar);

     //  if (rowItems != null) {
     //      if (!rowItems.isEmpty()) {
     //          circular_bar.setVisibility(View.VISIBLE);
     //      }
     //      else
     //      {
     //          circular_bar.setVisibility(View.GONE);
     //      }
     //  }





        like = (BootstrapCircleThumbnail) findViewById(R.id.like);
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (rowItems.isEmpty()) return;

                flingContainer.getTopCardListener().selectRight();
                flingContainer.getTopCardListener().setRotationDegrees(60f);
            }
        });
        dislike = (BootstrapCircleThumbnail) findViewById(R.id.dislike);
        dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rowItems.isEmpty()) return;
                flingContainer.getTopCardListener().selectLeft();
                flingContainer.getTopCardListener().setRotationDegrees(60f);

            }
        });

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

        requestStorgePermission();
        Intent intent = getIntent();
        Bundle intentBundle = intent.getExtras();
        if (intentBundle != null) {
            if (intentBundle.containsKey("gender")) {
                String gender = intent.getExtras().getString("gender");
                try {
                    fos = openFileOutput("gender", Context.MODE_PRIVATE);
                    fos.write(gender.getBytes());
                    fos.close();
                } catch (IOException e) {
                    e.getMessage();
                }
            }
            if (intentBundle.containsKey("userName")) {
                String userName = intent.getExtras().getString("userName");
                try {
                    fosUSerNAme = openFileOutput("userName", Context.MODE_PRIVATE);
                    fosUSerNAme.write(userName.getBytes());
                    fosUSerNAme.close();
                } catch (IOException e) {

                }
            }

        }

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
        String userName = readUserName();
        String gender = readGender();
        String s = currentUser.getProviders().get(0);
        if (s.contains("facebook.com")) {
            etProfile.setText(currentUser.getDisplayName());
        }
        else
        {
            etProfile.setText(userName);
        }
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
        SwipeCard();
    }

    private void requestStorgePermission() {
        try {
            if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, SettingActivity.PICK_FROM_GALLERY);
            }
        } catch (Exception e) {
            e.getMessage();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


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
                userDb.child(oppisteUser).child(userId).child("connections").child("nope").child(currentUser.getUid()).setValue(true);
                Toast.makeText(MainActivity.this, "Left!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                RightCardExit(dataObject);
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

        final DatabaseReference currectuserConnectionDB = userDb.child(userSex).child(currentUser.getUid()).child("connections").child("yeps").child(userId);
        currectuserConnectionDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Toast.makeText(MainActivity.this, "new Connection", Toast.LENGTH_LONG).show();
                    userDb.child(oppisteUser).child(dataSnapshot.getKey()).child("connections").child("matches").child(currentUser.getUid()).setValue(true);
                    userDb.child(userSex).child(currentUser.getUid()).child("connections").child("matches").child(dataSnapshot.getKey()).setValue(true);
                    FragmentManager fm = getSupportFragmentManager();
                    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    v.vibrate(500);
                    NewMatchFragment dialog = new NewMatchFragment();
                    dialog.show(fm, "dialog");

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
        if (id == R.id.action_logout) {
            mAuth.signOut();
            LoginManager.getInstance().logOut();
            Intent intent = new Intent(MainActivity.this, ChooseLoginRegistrationActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        if (id == R.id.action_SettingUser) {
            Intent intent = new Intent(MainActivity.this, SettingActivity.class);
            intent.putExtra("userSex", userSex);
            startActivity(intent);
            finish();
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
                if (dataSnapshot.exists() && !dataSnapshot.child("connections").child("nope").hasChild(currentUser.getUid()) && !dataSnapshot.child("connections").child("yeps").hasChild(currentUser.getUid())) {
                    String imageUrl = null;
                    if (dataSnapshot.child("profileImageUrl").exists()) {
                       imageUrl = dataSnapshot.child("profileImageUrl").toString();
                    }
                    Cards item = new Cards(dataSnapshot.getKey(), dataSnapshot.child("name").getValue().toString(), imageUrl);
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

    private void RightCardExit(Object dataObject) {
        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        final Cards obj = (Cards) dataObject;
        final String userId = obj.getUserId();
        String name = obj.getName();
        userDb.child(oppisteUser).child(userId).child("connections").child("yeps").child(currentUser.getUid()).setValue(true);
        isConnectionMatch(userId);
        Toast.makeText(MainActivity.this, "Right!", Toast.LENGTH_SHORT).show();
    }

    private String readGender() {
        FileInputStream in = null;
        StringBuilder sb = null;
        try {
            in = openFileInput("gender");
            InputStreamReader inputStreamReader = new InputStreamReader(in);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.getMessage();
        } finally {
            return sb.toString();
        }
    }

    private String readUserName() {
        FileInputStream in = null;
        StringBuilder sb = null;
        try {
            in = openFileInput("userName");
            InputStreamReader inputStreamReader = new InputStreamReader(in);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.getMessage();
        } finally {
            return sb.toString();
        }

    }


}


