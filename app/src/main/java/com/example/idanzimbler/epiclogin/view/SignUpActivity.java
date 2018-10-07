package com.example.idanzimbler.epiclogin.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.idanzimbler.epiclogin.R;
import com.example.idanzimbler.epiclogin.modle.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    ProgressBar progressBar;
    EditText editTextEmail, editTextPassword, editTextAge;
    RadioGroup radioGroup;
    RadioButton radioButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        editTextEmail = findViewById(R.id.signemailtxt);
        editTextPassword = findViewById(R.id.signpasstxt);
        progressBar = findViewById(R.id.progressbar);
        editTextAge = findViewById(R.id.signagetxt);
        radioGroup = findViewById(R.id.radioGroup);

        mAuth = FirebaseAuth.getInstance();
        findViewById(R.id.haveaccbtn).setOnClickListener(this);
        findViewById(R.id.signupbtn).setOnClickListener(this);

    }


    private void registerUser() {
        final String email = editTextEmail.getText().toString().trim();
        String pass = editTextPassword.getText().toString().trim();
        final String age = editTextAge.getText().toString().trim();
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);


        if (email.isEmpty()) {
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return;
        }
        if (age.isEmpty()) {
            editTextAge.setError("Age is required");
            editTextAge.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Please enter a valid email");
            editTextEmail.requestFocus();
            return;
        }

        if (pass.isEmpty()) {
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();
            return;
        }
        if (pass.length() < 6) {
            editTextPassword.setError("Minimum length of password is 6");
            editTextPassword.requestFocus();
            return;
        }
        if (age.length() > 2) {
            editTextAge.setError("Maximum age is 99");
            editTextAge.requestFocus();
            return;
        }
        if (Integer.parseInt(age) < 12) {
            editTextAge.setError("Minimum age is 12");
            editTextAge.requestFocus();
            return;
        }
        if (radioButton == null) {
            Toast.makeText(getApplicationContext(), "Gender is required", Toast.LENGTH_SHORT).show();
            return;
        }

        final String sex = radioButton.getText().toString().trim();

        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);

                        if (task.isSuccessful()) {
                            User user = new User(
                                    email,
                                    age,
                                    sex,
                                    new ArrayList<String>(),
                                    new ArrayList<String>()
                            );
                            FirebaseDatabase.getInstance().getReference("Users").
                                    child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    progressBar.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {
                                        Intent intent = new Intent(SignUpActivity.this, GenresActivity.class);
                                        intent.putExtra("Email", editTextEmail.getText().toString());
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(getApplicationContext(), "User registeration failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });


                        } else {
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                Toast.makeText(getApplicationContext(), "User is already registered", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.signupbtn:
                registerUser();
                break;
            case R.id.haveaccbtn:
                startActivity(new Intent(this, MainActivity.class));
                break;

        }
    }

}
