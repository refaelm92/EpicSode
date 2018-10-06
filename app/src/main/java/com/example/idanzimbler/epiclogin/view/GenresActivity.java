package com.example.idanzimbler.epiclogin.view;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.example.idanzimbler.epiclogin.R;
import com.example.idanzimbler.epiclogin.controller.Constants;
import com.example.idanzimbler.epiclogin.controller.Genres;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;

public class GenresActivity extends AppCompatActivity implements View.OnClickListener {
    ProgressBar progressBar;
    private FirebaseAuth mAuth;
    String extraEmail;
    HashSet<String> genresSet = new HashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genres);
        progressBar = findViewById(R.id.progressbar);
        mAuth = FirebaseAuth.getInstance();
        findViewById(R.id.finishbtn).setOnClickListener(this);
        findViewById(R.id.skipbtn).setOnClickListener(this);
        extraEmail = mAuth.getCurrentUser().getEmail();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.finishbtn:
                finishRegister();
                break;
            case R.id.skipbtn:
                Intent intent = new Intent(GenresActivity.this, MainActivity.class);
                intent.putExtra("Email", extraEmail);
                startActivity(intent);
                break;
        }
    }

    private void finishRegister() {
        progressBar.setVisibility(View.VISIBLE);
        FirebaseDatabase.getInstance().getReference("Users").
                child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("Genres")
                .setValue(new ArrayList<String>(genresSet)).addOnCompleteListener(new OnCompleteListener<Void>(){
        @Override
        public void onComplete(@NonNull Task<Void> task) {
            progressBar.setVisibility(View.GONE);
            if (task.isSuccessful()) {
                //Toast.makeText(getApplicationContext(), "User registered successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(GenresActivity.this, MainActivity.class);
                intent.putExtra("Email", extraEmail);
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), "User registeration failed", Toast.LENGTH_SHORT).show();
            }
        }
    });
    }

    public void checkBoxClick(View view) {
        boolean isChecked = ((CheckBox) view).isChecked();
        String checkBoxName = getResources().getResourceEntryName(view.getId()); // action_adventure
        String constName = checkBoxName.replaceAll("genres_","").replaceAll("_cb","").toUpperCase();
        Class  genresClass = Genres.class;
        try {
            Field field = genresClass.getField(constName);
            String genre = (String)field.get(null);
            if(isChecked){
                genresSet.add(genre);
            }else{
                genresSet.remove(genre);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}