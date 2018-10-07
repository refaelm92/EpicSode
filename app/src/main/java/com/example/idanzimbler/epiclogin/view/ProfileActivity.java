package com.example.idanzimbler.epiclogin.view;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.idanzimbler.epiclogin.R;
import com.example.idanzimbler.epiclogin.controller.Constants;
import com.example.idanzimbler.epiclogin.controller.Genres;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;

import static com.example.idanzimbler.epiclogin.controller.Constants.*;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    DatabaseReference databaseCheckedGenres;
    DatabaseReference databaseAge;
    EditText ageEditText;
    Button doneBtn;
    private FirebaseAuth mAuth;
    HashSet<String> genresSet = new HashSet<>();
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        progressBar = findViewById(R.id.profile_progressbar);
        progressBar.setVisibility(View.VISIBLE);
        mAuth = FirebaseAuth.getInstance();
        databaseAge = FirebaseDatabase.getInstance().getReference("Users").
                child(mAuth.getCurrentUser().getUid()).child("age");
        databaseCheckedGenres = FirebaseDatabase.getInstance().getReference("Users").
                child(mAuth.getCurrentUser().getUid()).child("genres");

        doneBtn = findViewById(R.id.donebtn);
        ageEditText = findViewById(R.id.ageet);
        findViewById(R.id.donebtn).setOnClickListener(this);
        findViewById(R.id.backbtn).setOnClickListener(this);

        databaseCheckedGenres.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot genreData : dataSnapshot.getChildren()) {
                    String genre = genreData.getValue(String.class);
                    genresSet.add(genre);
                }
                try {
                    Class genresClass = Genres.class;
                    Field[] fields = genresClass.getFields();
                    for(int i = 0; i < fields.length; i++){
                        String value = (String) fields[i].get(null);
                        if (genresSet.contains(value)) {
                            String checkBoxName = "profile_" + fields[i].getName().toLowerCase() + "_cb";
                            CheckBox cb = findViewById(getResources().getIdentifier(checkBoxName, "id", getPackageName()));
                            cb.setChecked(true);
                        }
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        databaseAge.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String age = String.valueOf(dataSnapshot.getValue());
                ageEditText.setText(age);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        ageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    public void checkBoxClick(View view) {
        boolean isChecked = ((CheckBox) view).isChecked();
        String checkBoxName = getResources().getResourceEntryName(view.getId()); // action_adventure
        String constName = checkBoxName.replaceAll("profile_","").replaceAll("_cb","").toUpperCase();
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
        int age = Integer.parseInt(ageEditText.getText().toString());
        String strAge = ageEditText.getText().toString().trim();
        if (strAge.isEmpty()) {
            ageEditText.setError("Age is required");
            ageEditText.requestFocus();
            return;
        }
        if (strAge.length() > 2) {
            ageEditText.setError("Maximum age is 99");
            ageEditText.requestFocus();
            return;
        }
        if (age < 12) {
            ageEditText.setError("Minimum age is 12");
            ageEditText.requestFocus();
            return;
        }
        FirebaseDatabase.getInstance().getReference("Users").
                child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("age")
                .setValue(age);
        FirebaseDatabase.getInstance().getReference("Users").
                child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("genres")
                .setValue(new ArrayList<String>(genresSet)).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Profile updated", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Failed to update profile", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
