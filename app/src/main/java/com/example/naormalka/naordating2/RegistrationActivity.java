package com.example.naormalka.naordating2;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;


public class RegistrationActivity extends AppCompatActivity {

    public static final String TAG = "naor";

    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;
    FirebaseAuth mAuth;
    BootstrapButton btnRegister;
    EditText etName;
    EditText etemail;
    EditText etPassword;
    RadioGroup mRadioGroup;


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
                    Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                    intent.putExtra("gender", getGender());
                    intent.putExtra("userName", getName());
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        };
        btnRegister = (BootstrapButton) findViewById(R.id.register);
        mRadioGroup = (RadioGroup) findViewById(R.id.radioGroup);

        etemail = (EditText) findViewById(R.id.email);
        etName = (EditText) findViewById(R.id.name);
        etPassword = (EditText) findViewById(R.id.password);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerToapp();
            }
        });

    }

    private void registerToapp() {
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

                    String userId = mAuth.getCurrentUser().getUid();
                    DatabaseReference currentUserDb = FirebaseDatabase.getInstance().getReference().child("Users").child(radioButton.getText().toString().toLowerCase()).child(userId);
                    Map userInfo = new HashMap();
                    userInfo.put("name",name);
                    userInfo.put("profileImageUrl","defalut");
                    currentUserDb.updateChildren(userInfo);

                }
            }
        });

    }


    public String getGender() {
        int selectId = mRadioGroup.getCheckedRadioButtonId();
        final RadioButton radioButton = (RadioButton) findViewById(selectId);
        return radioButton.getText().toString();
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
        Intent i = new Intent(this, ChooseLoginRegistrationActivity.class);
        startActivity(i);
        finish();
        return;
    }
}
