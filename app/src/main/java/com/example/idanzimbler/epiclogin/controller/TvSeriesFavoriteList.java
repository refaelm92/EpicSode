package com.example.idanzimbler.epiclogin.controller;

import android.support.annotation.NonNull;

import com.example.idanzimbler.epiclogin.modle.TvSeries;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TvSeriesFavoriteList {
    static TvSeriesFavoriteList instance;
    ArrayList<TvSeries> series;
    DatabaseReference favoritesRef;
    DatabaseReference seriesRef;

    private FirebaseAuth mAuth;


    public static TvSeriesFavoriteList getInstance() {
        if (instance == null) {
            instance = new TvSeriesFavoriteList();
        }
        return instance;
    }

    private TvSeriesFavoriteList() {
        mAuth = FirebaseAuth.getInstance();
        favoritesRef = FirebaseDatabase.getInstance().getReference("Users").
                child(mAuth.getCurrentUser().getUid()).child("favorites");
        seriesRef = FirebaseDatabase.getInstance().getReference("TvSeries");
        initializeSeriesList();
    }

    private void initializeSeriesList() {
        series = new ArrayList<>();
        final ArrayList<String> seriesIdList = new ArrayList<>();
        favoritesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    seriesIdList.add(data.getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        seriesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot tvSeriesData : dataSnapshot.getChildren()) {
                    String id = tvSeriesData.getKey();
                    if(!seriesIdList.contains(id)) continue;
                    if(contains(id)) continue;
                    String name = tvSeriesData.child("name").getValue(String.class);
                    Integer numOfSeasons = tvSeriesData.child("numOfSeasons").getValue(Integer.class);
                    String poster = tvSeriesData.child("poster").getValue(String.class);
                    Float popularity = tvSeriesData.child("popularity").getValue(Float.class);
                    TvSeries tvSeries = new TvSeries(id,name,poster,numOfSeasons,popularity);
                    series.add(tvSeries);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public ArrayList<TvSeries> getSeries() {
        return series;
    }

    public void setSeries(ArrayList<TvSeries> series) {
        this.series = series;
    }

    public void clear() {
        series.clear();
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

    public void remove(TvSeries seriesToRemove) {
        if (series.contains(seriesToRemove)) {
            series.remove(seriesToRemove);
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
