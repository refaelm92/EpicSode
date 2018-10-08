package com.example.idanzimbler.epiclogin.controller;

import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class UsersBookmarks {
    private static UsersBookmarks instance;
    private Map<String, String> bookmarks;
    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;

    public static UsersBookmarks getInstance() {
        if (instance == null) {
            instance = new UsersBookmarks();
        }
        return instance;
    }

    private UsersBookmarks() {
        mAuth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference("Users");
    }

    public void initializeBookmarks() {
        bookmarks = new HashMap<>();
        usersRef.child(mAuth.getCurrentUser().getUid()).child("bookmarks").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    bookmarks.put(snapshot.getKey(),snapshot.getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public boolean isBookmark(String seriesId, int season, int episode) {
        if (!bookmarks.containsKey(seriesId)) return false;
        String[] seasonEpisodePair = bookmarks.get(seriesId).split(",");
        return Integer.parseInt(seasonEpisodePair[0]) == season
                && Integer.parseInt(seasonEpisodePair[1]) == episode;
    }

    public boolean isSeasonBookmark(String seriesId, int season) {
        if (!bookmarks.containsKey(seriesId)) return false;
        String[] seasonEpisodePair = bookmarks.get(seriesId).split(",");
        return Integer.parseInt(seasonEpisodePair[0]) == season;
    }

    public void addBookmark(String seriesId, int season, int episode) {
        String value = season + "," + episode;
        bookmarks.put(seriesId, value);
        usersRef.child(mAuth.getCurrentUser().getUid()).child("bookmarks").child(seriesId).setValue(value);
    }

    public void removeBookmark(String seriesId, int season) {
        bookmarks.remove(seriesId);
        usersRef.child(mAuth.getCurrentUser().getUid()).child("bookmarks").child(seriesId).removeValue();
    }


}
