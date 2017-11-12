package com.example.naormalka.naordating2;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapCircleThumbnail;
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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ImageView ivProfile;
    private TextView etProfile;
    private String userSex;
    private String name;
    private String photoUrl;
    private String oppisteUser;
    private DatabaseReference usersDb;
    private List<Cards> rowItems;

    private SwipeFlingAdapterView flingContainer;
    private ArrayAdapterCards arrayAdapterCards;

    private FirebaseAuth mAuth;
    private BootstrapCircleThumbnail like;
    private BootstrapCircleThumbnail dislike;
    FileOutputStream fos = null;
    FileOutputStream fosUSerNAme = null;
    ProgressBar progressBar;
    ProgressDialog progressDialog;
    DatabaseReference dataUserRef;
    String profileImageUrl12;
    UserChat userDb1;


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

    public void setLoadingDialog(boolean show) {
        progressDialog = new ProgressDialog(this);
        if (show == true) {
            progressDialog.show();
        } else {
            progressDialog.dismiss();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


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

        usersDb = FirebaseDatabase.getInstance().getReference().child("Users");

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

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View hView = navigationView.getHeaderView(0);

        ivProfile = (ImageView) hView.findViewById(R.id.profile);
        etProfile = (TextView) hView.findViewById(R.id.etProfile);

        String userName = readUserName();
        String gender = readGender();
        dataUserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(gender.toLowerCase()).child(currentUser.getUid());
        dataUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.GONE);
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if (map.get("name") != null) {
                        name = map.get("name").toString();
                        etProfile.setText(name);


                    }
                    if (map.get("phone") != null) {


                    }

                    if (map.get("profileImageUrl") != null) {

                        photoUrl = map.get("profileImageUrl").toString();
                        Glide.with(MainActivity.this).load(photoUrl).into(ivProfile);

                    }

                }

                userDb1 = new UserChat(name, photoUrl);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        SwipeCard();
        progressBar = (ProgressBar) findViewById(R.id.progressBar);


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
                usersDb.child(userId).child("connections").child("nope").child(currentUser.getUid()).setValue(true);


            }

            @Override
            public void onRightCardExit(Object dataObject) {
                RightCardExit(dataObject);
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {


            }

            @Override
            public void onScroll(float v) {

            }
        });

        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {

            }
        });
    }

    private void isConnectionMatch(final String userId, final String name1, final String imageUser) {
        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        final DatabaseReference userDb = FirebaseDatabase.getInstance().getReference("Users");

        final DatabaseReference currectuserConnectionDB = userDb.child(currentUser.getUid()).child("connections").child("yeps").child(userId);
        currectuserConnectionDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    String displayName = userDb1.getDisplayName();
                    String profileImage = userDb1.getProfileImage();

                    userDb.child(dataSnapshot.getKey()).child("connections").child("matches").child(currentUser.getUid()).setValue(true);
                    userDb.child(currentUser.getUid()).child("connections").child("matches").child(dataSnapshot.getKey()).setValue(true);
                    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    v.vibrate(500);
                    //  showChatList(displayName, profileImage);


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    private String readImageFromFireBase(String userGender, String userUid) {


        DatabaseReference users = FirebaseDatabase.getInstance().getReference().child("Users").child(userGender).child(userUid);
        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if (map.get("profileImageUrl") != null) {
                        profileImageUrl12 = map.get("profileImageUrl").toString();

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        if (profileImageUrl12 != null) {
            return profileImageUrl12;
        } else {
            return null;
        }

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
            if (LoginManager.getInstance() != null) {
                LoginManager.getInstance().logOut();
                Intent intent = new Intent(MainActivity.this, ChooseLoginRegistrationActivity.class);
                startActivity(intent);
            }


        }
        if (id == R.id.action_SettingUser) {

            Intent intent = new Intent(MainActivity.this, SettingActivity.class);
            startActivity(intent);

        }

        return true;
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
            getSupportFragmentManager().beginTransaction().replace(R.id.ee, new Chat_fragment()).commit();


        } else if (id == R.id.nav_setting) {
            Intent intent = new Intent(this, SettingActivity.class);
            intent.putExtra("userSex", readGender());
            startActivity(intent);
            finish();


        } else if (id == R.id.nav_invite) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void checkUserSex() {

        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        //if (currentUser == null) return;
        DatabaseReference UserDb = usersDb.child(currentUser.getUid());
        UserDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getKey().equals(currentUser.getUid())) {
                    if (dataSnapshot.exists()) {
                        if (dataSnapshot.child("sex") != null) {
                            userSex = dataSnapshot.child("sex").getValue().toString();
                            oppisteUser = "female";
                            switch (userSex) {
                                case "male":
                                    oppisteUser = "female";
                                    break;
                                case "female":
                                    oppisteUser = "male";
                                    break;
                            }
                            getOppositeSexUSer();
                        }
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public void getOppositeSexUSer() {

        usersDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (dataSnapshot.exists() && !dataSnapshot.child("connections").child("nope").hasChild(currentUser.getUid()) && !dataSnapshot.child("connections").child("yeps").hasChild(currentUser.getUid()) && dataSnapshot.child("sex").getValue().toString().equals(oppisteUser)) {
                    String imageUrl = "default";
                    if (!dataSnapshot.child("profileImageUrl").getValue().equals("default")) {
                        imageUrl = dataSnapshot.child("profileImageUrl").getValue().toString();
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
        String profileImageUrl = obj.getProfileImageUrl();
        usersDb.child(userId).child("connections").child("yeps").child(currentUser.getUid()).setValue(true);
        isConnectionMatch(userId, name, profileImageUrl);
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
