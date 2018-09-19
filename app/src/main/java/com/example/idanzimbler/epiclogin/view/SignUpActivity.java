package com.example.idanzimbler.epiclogin.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.CheckBox;
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
import java.util.Collections;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    ProgressBar progressBar;
    EditText editTextEmail, editTextPassword, editTextAge;
    RadioGroup radioGroup;
    RadioButton radioButton;
    private FirebaseAuth mAuth;
    ArrayList<Boolean> checkedGenres = new ArrayList<>(NUMOFGENRES);
    //#defines
    public static final int NUMOFGENRES = 12;
    public static final int ACTION = 0;
    public static final int ADVENTURE = 1;
    public static final int ANIMATION = 2;
    public static final int BIOGRAPHY = 3;
    public static final int COMEDY = 4;
    public static final int CRIME = 5;
    public static final int THRILLER = 6;
    public static final int DRAMA = 7;
    public static final int FANTASY = 8;
    public static final int HORROR = 9;
    public static final int ROMANCE = 10;
    public static final int DOCUMENTARY = 11;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        checkedGenres.addAll(Collections.nCopies(NUMOFGENRES, Boolean.FALSE));
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
                                    checkedGenres
                            );
                            FirebaseDatabase.getInstance().getReference("Users").
                                    child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    progressBar.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {
                                        //Toast.makeText(getApplicationContext(), "User registered successfully", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                        intent.putExtra("Email", editTextEmail.getText().toString());
                                        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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


    public void checkBoxClick(View view) {
        boolean isChecked = ((CheckBox) view).isChecked();

        switch (view.getId()) {
            case R.id.actioncb:
                checkedGenres.add(ACTION, isChecked);
                break;
            case R.id.adventurecb:
                checkedGenres.add(ADVENTURE, isChecked);
                break;
            case R.id.animationcb:
                checkedGenres.add(ANIMATION, isChecked);
                break;
            case R.id.biographycb:
                checkedGenres.add(BIOGRAPHY, isChecked);
                break;
            case R.id.comedycb:
                checkedGenres.add(COMEDY, isChecked);
                break;
            case R.id.crimecb:
                checkedGenres.add(CRIME, isChecked);
                break;
            case R.id.thrillercb:
                checkedGenres.add(THRILLER, isChecked);
                break;
            case R.id.dramacb:
                checkedGenres.add(DRAMA, isChecked);
                break;
            case R.id.fantasycb:
                checkedGenres.add(FANTASY, isChecked);
                break;
            case R.id.horrorcb:
                checkedGenres.add(HORROR, isChecked);
                break;
            case R.id.romancecb:
                checkedGenres.add(ROMANCE, isChecked);
                break;
            case R.id.documentarycb:
                checkedGenres.add(DOCUMENTARY, isChecked);
                break;
        }

    }
}
