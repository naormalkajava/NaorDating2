package com.example.naormalka.naordating2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;


public class RegistrationActivity extends AppCompatActivity {
    public static final String TAG = "naor";


    CallbackManager callbackManager;
    LoginButton faceButton;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;

    FirebaseAuth mAuth;
    String birthday = "";
    String gender = "";
    Button btnRegister;
    EditText etName;
    EditText etemail;
    EditText etPassword;
    RadioGroup mRadioGroup;
    ProgressDialog progress;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrationactivity);
        mAuth = FirebaseAuth.getInstance();


        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    progress = new ProgressDialog(RegistrationActivity.this);
                    progress.setTitle("Please Wait");
                    progress.setMessage("Wait");
                    progress.setCancelable(true);
                    progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progress.show();
                    Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        };
        btnRegister = (Button) findViewById(R.id.register);
        mRadioGroup = (RadioGroup) findViewById(R.id.radioGroup);

        etemail = (EditText) findViewById(R.id.email);
        etName = (EditText) findViewById(R.id.name);
        etPassword = (EditText) findViewById(R.id.password);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int selectId = mRadioGroup.getCheckedRadioButtonId();

                final RadioButton radioButton = (RadioButton) findViewById(selectId);


                final String email = etemail.getText().toString();
                final String password = etPassword.getText().toString();
                final String name = etName.getText().toString();

                if (!isEmailValid() | !isNameValid() | !isPasswordValid() | !isGenderValid())
                    return;


                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(RegistrationActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        } else {

                            //  FirebaseUser currentUser = mAuth.getCurrentUser();
                            //  DatabaseReference currentUserDb = FirebaseDatabase.getInstance().getReference().child("Users")
                            //          .child(radioButton.getText().toString().toLowerCase()).child(currentUser.getUid()).child(name);
                            //  AppUserByEmail user = new AppUserByEmail(name, currentUser.getUid(), null, radioButton.getText().toString().toLowerCase());
                            //  currentUserDb.setValue(user);
                            String userId = mAuth.getCurrentUser().getUid();
                            DatabaseReference currentUserDb = FirebaseDatabase.getInstance().getReference().child("Users").child(radioButton.getText().toString().toLowerCase()).child(userId).child("name");
                            currentUserDb.setValue(name);
                        }
                    }
                });
            }
        });

        //
        callbackManager = CallbackManager.Factory.create();
        faceButton = (LoginButton) findViewById(R.id.faceButton);
        faceButton.setReadPermissions(Arrays.asList(
                "public_profile", "email", "user_birthday", "user_friends"));
        faceButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {

                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
                AccessToken accessToken = loginResult.getAccessToken();
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.v("RegistrationActivity", response.toString());


                                try {

                                    gender = object.getString("gender");
                                    birthday = object.getString("birthday");

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,birthday,gender");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");

            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);

            }
        });

    }

    private String getEmail() {
        return etemail.getText().toString();
    }

    private String getPassword() {
        return etPassword.getText().toString();
    }


    private String getName() {
        return etName.getText().toString();
    }

    private boolean isGenderValid() {
        boolean isvalid;
        if (mRadioGroup.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Invalid Gender", Toast.LENGTH_SHORT).show();
            isvalid = false;
        } else {
            isvalid = true;
        }

        return isvalid;
    }

    private boolean isEmailValid() {
        boolean iscurrectMail;

        if (getEmail().length() > 6 && getEmail().contains("@") && (getEmail().contains("co.il") || getEmail().contains("com")))//...
        {
            iscurrectMail = true;

        } else {
            iscurrectMail = false;
            etemail.setError("Invalid email address.");

        }

        return iscurrectMail;
    }

    private boolean isPasswordValid() {
        boolean iscurrectMail;

        if (getPassword().length() < 5) {
            etPassword.setError("You Must At Least 6 charcters");
            iscurrectMail = false;
        } else {
            iscurrectMail = true;
        }
        return iscurrectMail;
    }

    private boolean isNameValid() {

        if (getName().length() <= 3) {
            etName.setError("You Must At Least 4 charcters");
            return false;
        } else {
            return true;
        }
    }


    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            Toast.makeText(RegistrationActivity.this, "succed to firebase", Toast.LENGTH_SHORT).show();
                            registerToFireBase();

                            gotoMainActivity();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(RegistrationActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }

                    }
                });


    }

    private void registerToFireBase() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference userref = FirebaseDatabase.getInstance().getReference().child("Users").child(gender.toLowerCase()).child(currentUser.getUid()).child("name");
        userref.setValue(currentUser.getDisplayName());



    }


    private void gotoMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthStateListener);
    }

}