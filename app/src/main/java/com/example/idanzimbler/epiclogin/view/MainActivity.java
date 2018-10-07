package com.example.idanzimbler.epiclogin.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.idanzimbler.epiclogin.R;
import com.example.idanzimbler.epiclogin.controller.TvSeriesFavoriteList;
import com.example.idanzimbler.epiclogin.controller.TvSeriesHomeList;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    FirebaseAuth mAuth;
    EditText editTextEmail, editTextPassword;
    ProgressBar progressBar;
    FirebaseDatabase database;
    DatabaseReference seriesRef;
    CheckBox mCheckBoxRemember;
    SharedPreferences mPrefs;
    private static final String PREFS_NAME = "PrefsFile";

    //String Email,Password,Age,Gender;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPrefs = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        seriesRef = database.getReference("TvSeries");
        editTextEmail = (EditText) findViewById(R.id.emailtxt);
        editTextPassword = (EditText) findViewById(R.id.passtxt);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        mCheckBoxRemember = (CheckBox) findViewById(R.id.checkBoxRememberMe);

        if (getIntent().getExtras() != null) {
            editTextEmail.setText(getIntent().getExtras().getString("Email"));
        }


        findViewById(R.id.noaccbtn).setOnClickListener(this);
        findViewById(R.id.loginbtn).setOnClickListener(this);
        getPreferencesData();

    }

    private void getPreferencesData() {
        SharedPreferences sp = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        if(sp.contains("pref_name")){
            String u = sp.getString("pref_name","not found");
            editTextEmail.setText(u.toString());
        }
        if(sp.contains("pref_pass")){
            String p = sp.getString("pref_pass","not found");
            editTextPassword.setText(p.toString());
        }
        if(sp.contains("pref_check")){
            Boolean b = sp.getBoolean("pref_check",false);
            mCheckBoxRemember.setChecked(b);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null) {

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.noaccbtn:
                startActivity(new Intent(this, SignUpActivity.class));

                break;
            case R.id.loginbtn:
                userLogin();
                break;

        }


    }

    private void userLogin() {


        String email = editTextEmail.getText().toString().trim();
        String pass = editTextPassword.getText().toString().trim();

        if (email.isEmpty()) {
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
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
        if(mCheckBoxRemember.isChecked()){
            Boolean boolIsChecked = mCheckBoxRemember.isChecked();
            SharedPreferences.Editor editor = mPrefs.edit();
            editor.putString("pref_name", editTextEmail.getText().toString());
            editor.putString("pref_pass",editTextPassword.getText().toString());
            editor.putBoolean("pref_check",boolIsChecked);
            editor.apply();
        }
        else{
            mPrefs.edit().clear().apply();
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    Bundle b = new Bundle();
                    b.putInt(HomeActivity.INTENT_FLAG, HomeActivity.FIRST_ENTER);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtras(b);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
