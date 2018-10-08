package com.example.idanzimbler.epiclogin.controller;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.idanzimbler.epiclogin.modle.TvSeries;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TvSeriesFavoriteList {
    private static TvSeriesFavoriteList instance;
    private ArrayList<TvSeries> series;
    private DatabaseReference usersRef;
    private DatabaseReference seriesRef;
    private DatabaseReference favoritesRef;
    private ArrayList<String> seriesIdList;
    private FirebaseAuth mAuth;
    private ValueEventListener idListener;
    private ValueEventListener seriesListener;

    public static TvSeriesFavoriteList getInstance() {
        if (instance == null) {
            instance = new TvSeriesFavoriteList();
        }
        return instance;
    }

    public ArrayList<String> getSeriesIdList() {
        return seriesIdList;
    }

    private TvSeriesFavoriteList() {
        Log.e("refaelTest", "constructing favorites");
        seriesRef = FirebaseDatabase.getInstance().getReference("TvSeries");
        mAuth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference("Users");
    }

    public void initializeSeriesList() {
        Log.e("refaelTest", "initializing favorites");
        favoritesRef = usersRef.child(mAuth.getCurrentUser().getUid()).child("favorites");
        idListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    seriesIdList.add(data.getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        favoritesRef.addListenerForSingleValueEvent(idListener);
        seriesListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot tvSeriesData : dataSnapshot.getChildren()) {
                    String id = tvSeriesData.getKey();
                    if (!seriesIdList.contains(id)) continue;
                    if (contains(id)) continue;
                    String name = tvSeriesData.child("name").getValue(String.class);
                    Log.e("refaelTest", "adding " + name);
                    Integer numOfSeasons = tvSeriesData.child("numOfSeasons").getValue(Integer.class);
                    String poster = tvSeriesData.child("poster").getValue(String.class);
                    Float popularity = tvSeriesData.child("popularity").getValue(Float.class);
                    TvSeries tvSeries = new TvSeries(id, name, poster, numOfSeasons, popularity);
                    series.add(tvSeries);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        seriesRef.addValueEventListener(seriesListener);
    }


    public ArrayList<TvSeries> getSeries() {
        return series;
    }

    public void setSeries(ArrayList<TvSeries> series) {
        this.series = series;
    }

    public void clear() {
        try {
            series = new ArrayList<>();
            seriesIdList = new ArrayList<>();
            if (favoritesRef != null && idListener != null)
                favoritesRef.removeEventListener(idListener);
            if (seriesRef != null && seriesListener != null)
                seriesRef.removeEventListener(seriesListener);
            mAuth = FirebaseAuth.getInstance();
            seriesRef = FirebaseDatabase.getInstance().getReference("TvSeries");
            usersRef = FirebaseDatabase.getInstance().getReference("Users");
        } catch (Exception e) {
            Log.e("refaelTest", "failed to clear " + e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean canAdd(TvSeries seriesToAdd) {
        return series.size() < 5;
    }

    public void add(TvSeries seriesToAdd) {
        if (!contains(seriesToAdd)) {
            series.add(seriesToAdd);
            favoritesRef.setValue(compressSeriesList());
        }
    }

    public void remove(String seriesIdToRemove) {
        if (contains(seriesIdToRemove)) {
            TvSeries seriesToRemove = null;
            for (TvSeries tvSeries : series) {
                if(tvSeries.getId().equals(seriesIdToRemove))
                    seriesToRemove = tvSeries;
            }
            if(seriesToRemove != null) series.remove(seriesToRemove);
            favoritesRef.setValue(compressSeriesList());
        }

    }

    public boolean contains(TvSeries seriesToCheck) {
        for (TvSeries tvSeries : series) {
            if (tvSeries.getId().equals(seriesToCheck.getId())) return true;
        }
        return false;
    }

    private ArrayList<String> compressSeriesList() {
        ArrayList<String> compressed = new ArrayList<>();
        for (TvSeries tvSeries : series) {
            compressed.add(tvSeries.getId());
        }
        return compressed;
    }

    public boolean contains(String seriesIdToCheck) {
        for (TvSeries tvSeries : series) {
            if (tvSeries.getId().equals(seriesIdToCheck)) return true;
        }
        return false;
    }

}
