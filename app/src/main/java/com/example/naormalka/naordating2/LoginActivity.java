package com.example.naormalka.naordating2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ActionProvider;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.TypefaceProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private BootstrapButton mLogin;
    private ProgressBar progreesb;
    private EditText mEmail, mPassword;
    ProgressDialog progress;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        mAuth = FirebaseAuth.getInstance();
        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user !=null){
                    progress = new ProgressDialog(LoginActivity.this);
                    progress.setTitle("Please Wait");
                    progress.setMessage("Wait");
                    progress.setCancelable(true);
                    progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progress.show();

                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);

                    startActivity(intent);
                    finish();
                    return;
                }
            }
        };


        mLogin = (BootstrapButton) findViewById(R.id.login);

        mEmail = (EditText) findViewById(R.id.email);
        mPassword = (EditText) findViewById(R.id.password);

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!isEmailValid() | ! isPasswordValid()) return;

                final String email = mEmail.getText().toString();
                final String password = mPassword.getText().toString();
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            Toast.makeText(LoginActivity.this, "sign in error", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }

        });
    }

    private String getEmail () {
        return mEmail.getText().toString();
    }
    private String getPassword () {
        return mPassword.getText().toString();
    }

    private boolean isEmailValid() {
        boolean iscurrectMail;

        if (getEmail().length() > 6 && getEmail().contains("@") && (getEmail().contains("co.il") || getEmail().contains("com")))//...
        {
            iscurrectMail = true;

        } else {
            iscurrectMail = false;
            mEmail.setError("Invalid email address.");

        }

        return iscurrectMail;
    }
    private boolean isPasswordValid() {
        boolean iscurrectMail;

        if (getPassword().length() < 5) {
            mPassword.setError("You Must At Least 6 charcters");
            iscurrectMail = false;
        } else {
            iscurrectMail = true;
        }
        return iscurrectMail;
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
    @Override
    public void onBackPressed() {
        Intent i = new Intent(this,ChooseLoginRegistrationActivity.class);
        startActivity(i);
        finish();
        return;
    }
}
