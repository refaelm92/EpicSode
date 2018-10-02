package com.example.idanzimbler.epiclogin.view;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.idanzimbler.epiclogin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.idanzimbler.epiclogin.controller.Constants.*;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    DatabaseReference databaseCheckedGenres;
    Button doneBtn;
    ProgressBar progressBar;
    private FirebaseAuth mAuth;
    final ArrayList<Boolean> checkedGenres = new ArrayList<>();
    CheckBox actioncb, animationcb, thrillercb, horrorcb, romancecb,
            documentarycb, comedycb, adventurecb, westerncb, familycb,
            historycb, musiccb, warcb, crimecb, dramacb, mysterycb,
            scificb, tvmoviecb, fantasycb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        for (int i = 0; i < NUMOFGENRES; i++) {
            checkedGenres.add(false);
        }

        progressBar = findViewById(R.id.progressbar);
        mAuth = FirebaseAuth.getInstance();
        databaseCheckedGenres = FirebaseDatabase.getInstance().getReference("Users").
                child(mAuth.getCurrentUser().getUid()).child("checkedGenres");

        doneBtn = findViewById(R.id.donebtn);
        actioncb = findViewById(R.id.actioncb);
        animationcb = findViewById(R.id.animationcb);
        thrillercb = findViewById(R.id.thrillercb);
        horrorcb = findViewById(R.id.horrorcb);
        romancecb = findViewById(R.id.romancecb);
        documentarycb = findViewById(R.id.documentarycb);
        comedycb = findViewById(R.id.comedycb);
        adventurecb = findViewById(R.id.adventurecb);
        westerncb = findViewById(R.id.westerncb);
        familycb = findViewById(R.id.familycb);
        historycb = findViewById(R.id.historycb);
        musiccb = findViewById(R.id.musiccb);
        warcb = findViewById(R.id.warcb);
        crimecb = findViewById(R.id.crimecb);
        dramacb = findViewById(R.id.dramacb);
        scificb = findViewById(R.id.scificb);
        tvmoviecb = findViewById(R.id.tvmoviecb);
        fantasycb = findViewById(R.id.fantasycb);
        mysterycb = findViewById(R.id.mysterycb);
        findViewById(R.id.donebtn).setOnClickListener(this);
        findViewById(R.id.backbtn).setOnClickListener(this);

        progressBar.setVisibility(View.VISIBLE);
        databaseCheckedGenres.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int i = 0;
                for (DataSnapshot bool : dataSnapshot.getChildren()) {
                    boolean b = bool.getValue(Boolean.class);
                    switch (i) {
                        case ACTION:
                            actioncb.setChecked(b);
                            checkedGenres.set(ACTION, b);
                            break;
                        case ADVENTURE:
                            adventurecb.setChecked(b);
                            checkedGenres.set(ADVENTURE, b);
                            break;
                        case ANIMATION:
                            animationcb.setChecked(b);
                            checkedGenres.set(ANIMATION, b);
                            break;
                        case WESTERN:
                            westerncb.setChecked(b);
                            checkedGenres.set(WESTERN, b);
                            break;
                        case COMEDY:
                            comedycb.setChecked(b);
                            checkedGenres.set(COMEDY, b);
                            break;
                        case CRIME:
                            crimecb.setChecked(b);
                            checkedGenres.set(CRIME, b);
                            break;
                        case THRILLER:
                            thrillercb.setChecked(b);
                            checkedGenres.set(THRILLER, b);
                            break;
                        case DRAMA:
                            dramacb.setChecked(b);
                            checkedGenres.set(DRAMA, b);
                            break;
                        case FANTASY:
                            fantasycb.setChecked(b);
                            checkedGenres.set(FANTASY, b);
                            break;
                        case HORROR:
                            horrorcb.setChecked(b);
                            checkedGenres.set(HORROR, b);
                            break;
                        case ROMANCE:
                            romancecb.setChecked(b);
                            checkedGenres.set(ROMANCE, b);
                            break;
                        case DOCUMENTARY:
                            documentarycb.setChecked(b);
                            checkedGenres.set(DOCUMENTARY, b);
                            break;
                        case FAMILY:
                            familycb.setChecked(b);
                            checkedGenres.set(FAMILY, b);
                            break;
                        case HISTORY:
                            historycb.setChecked(b);
                            checkedGenres.set(HISTORY, b);
                            break;
                        case MUSIC:
                            musiccb.setChecked(b);
                            checkedGenres.set(MUSIC, b);
                            break;
                        case MYSTERY:
                            mysterycb.setChecked(b);
                            checkedGenres.set(MYSTERY, b);
                            break;
                        case SCIFI:
                            scificb.setChecked(b);
                            checkedGenres.set(SCIFI, b);
                            break;
                        case TVMOVIE:
                            tvmoviecb.setChecked(b);
                            checkedGenres.set(TVMOVIE, b);
                            break;
                        case WAR:
                            warcb.setChecked(b);
                            checkedGenres.set(WAR, b);
                            break;
                    }
                    i++;

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        progressBar.setVisibility(View.GONE);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.donebtn:
                doneBtnClick();
                break;
            case R.id.backbtn:
                startActivity(new Intent(this,HomeActivity.class));
        }
    }

    private void doneBtnClick() {
        progressBar.setVisibility(View.VISIBLE);
        FirebaseDatabase.getInstance().getReference("Users").
                child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("checkedGenres")
                .setValue(checkedGenres).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Genres updated", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Failed to update genres", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
