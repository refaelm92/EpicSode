package com.example.idanzimbler.epiclogin.view;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.idanzimbler.epiclogin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    FirebaseAuth mAuth;
    EditText editTextEmail,editTextPassword;
    ProgressBar progressBar;
    //String Email,Password,Age,Gender;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        editTextEmail = (EditText)findViewById(R.id.emailtxt);
        editTextPassword = (EditText)findViewById(R.id.passtxt);
        progressBar = (ProgressBar)findViewById(R.id.progressbar);
        if(getIntent().getExtras() != null){
            editTextEmail.setText(getIntent().getExtras().getString("Email"));
        }


        findViewById(R.id.noaccbtn).setOnClickListener(this);
        findViewById(R.id.loginbtn).setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser() != null){

        }
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.noaccbtn:
                startActivity(new Intent(this,SignUpActivity.class));

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

        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if(task.isSuccessful()){
                    Intent intent = new Intent (MainActivity.this,HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
