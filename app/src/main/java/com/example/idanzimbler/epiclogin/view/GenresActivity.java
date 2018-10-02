package com.example.idanzimbler.epiclogin.view;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.example.idanzimbler.epiclogin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import static com.example.idanzimbler.epiclogin.controller.Constants.*;

public class GenresActivity extends AppCompatActivity implements View.OnClickListener {
    ProgressBar progressBar;
    private FirebaseAuth mAuth;
    String extraEmail;


    final ArrayList<Boolean> checkedGenres = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genres);
        for (int i = 0; i < NUMOFGENRES; i++) {
            checkedGenres.add(false);
        }
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
                .child("checkedGenres")
                .setValue(checkedGenres).addOnCompleteListener(new OnCompleteListener<Void>(){
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

        switch (view.getId()) {
            case R.id.actioncb:
                checkedGenres.set(ACTION, isChecked);
                break;
            case R.id.adventurecb:
                checkedGenres.set(ADVENTURE, isChecked);
                break;
            case R.id.animationcb:
                checkedGenres.set(ANIMATION, isChecked);
                break;
            case R.id.westerncb:
                checkedGenres.set(WESTERN, isChecked);
                break;
            case R.id.comedycb:
                checkedGenres.set(COMEDY, isChecked);
                break;
            case R.id.crimecb:
                checkedGenres.set(CRIME, isChecked);
                break;
            case R.id.thrillercb:
                checkedGenres.set(THRILLER, isChecked);
                break;
            case R.id.dramacb:
                checkedGenres.set(DRAMA, isChecked);
                break;
            case R.id.fantasycb:
                checkedGenres.set(FANTASY, isChecked);
                break;
            case R.id.horrorcb:
                checkedGenres.set(HORROR, isChecked);
                break;
            case R.id.romancecb:
                checkedGenres.set(ROMANCE, isChecked);
                break;
            case R.id.familycb:
                checkedGenres.set(FAMILY, isChecked);
                break;
            case R.id.historycb:
                checkedGenres.set(HISTORY, isChecked);
                break;
            case R.id.musiccb:
                checkedGenres.set(MUSIC, isChecked);
                break;
            case R.id.mysterycb:
                checkedGenres.set(MYSTERY, isChecked);
                break;
            case R.id.scificb:
                checkedGenres.set(SCIFI, isChecked);
                break;
            case R.id.tvmoviecb:
                checkedGenres.set(TVMOVIE, isChecked);
                break;
            case R.id.warcb:
                checkedGenres.set(WAR, isChecked);
                break;
            case R.id.documentarycb:
                checkedGenres.set(DOCUMENTARY, isChecked);
                break;
        }

    }
}