package com.example.idanzimbler.epiclogin.controller;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ExpandableListView;

import com.example.idanzimbler.epiclogin.modle.TvSeries;
import com.example.idanzimbler.epiclogin.modle.User;
import com.example.idanzimbler.epiclogin.view.HomeActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;

public class FillSeriesListFromFireBaseBySuggestionsTask extends AsyncTask<Void, Void, Void> {
    private static final int TASK_STARTED = 0;
    private static final int USER_TASK_FINISHED = 1;
    private static final int SERIES_IDS_TASK_FINISHED = 2;
    private static final int NUM_OF_ITEMS_IN_PAGE = 15;

    ExpandableListView list;
    HomeActivity context;
    private FirebaseDatabase database;
    private DatabaseReference seriesRef;
    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;
    private User signedUser;
    private int taskStatus;
    private HashSet<String> seriesIdsSet;
    public FillSeriesListFromFireBaseBySuggestionsTask(HomeActivity context, ExpandableListView list) {
        taskStatus = TASK_STARTED;
        database = FirebaseDatabase.getInstance();
        seriesRef = database.getReference("TvSeries");
        this.list = list;
        this.context = context;
        usersRef = database.getReference("Users");
        mAuth = FirebaseAuth.getInstance();
        usersRef.child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                signedUser = dataSnapshot.getValue(User.class);
                taskStatus = USER_TASK_FINISHED;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        seriesIdsSet = new HashSet<>();
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    if (isProfileMatch(signedUser, user)) {
                        if (user.getFavorites() != null) {
                            for (String id : user.getFavorites()) {
                                seriesIdsSet.add(id);
                            }
                        }
                    }
                }
                taskStatus = SERIES_IDS_TASK_FINISHED;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override

    protected Void doInBackground(Void... voids) {
        while (taskStatus != SERIES_IDS_TASK_FINISHED) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        seriesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                context.getProgressBar().setVisibility(View.INVISIBLE);
                TvSeriesHomeList.getInstance().clear();
                SeriesAdapter adapter = (SeriesAdapter) list.getExpandableListAdapter();
                if(adapter == null) {
                    adapter = new SeriesAdapter(context,TvSeriesHomeList.getInstance().getSeries());
                    list.setAdapter(adapter);
                }
                adapter.notifyDataSetChanged();
                for (DataSnapshot tvSeriesData : dataSnapshot.getChildren()) {
                    String id = tvSeriesData.getKey();
                    if (seriesIdsSet.contains(id) && !TvSeriesFavoriteList.getInstance().contains(id)) {
                        String name = tvSeriesData.child("name").getValue(String.class);
                        Integer numOfSeasons = tvSeriesData.child("numOfSeasons").getValue(Integer.class);
                        String poster = tvSeriesData.child("poster").getValue(String.class);
                        Float popularity = tvSeriesData.child("popularity").getValue(Float.class);
                        TvSeries tvSeries = new TvSeries(id, name, poster, numOfSeasons, popularity);
                        TvSeriesHomeList.getInstance().getSeries().add(tvSeries);
                        adapter.notifyDataSetChanged();
                    }
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
    }


    private boolean isProfileMatch(User signedUser, User user) {
        try {
            int ageDifference = Math.abs(Integer.parseInt(signedUser.getAge()) - Integer.parseInt(user.getAge()));
            if (!isIntersectionEqualsNull(signedUser.getGenres(), user.getGenres())
                    && signedUser.getSex().equals(user.getSex())
                    && ageDifference <= 10) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }


    private boolean isIntersectionEqualsNull(ArrayList<String> A, ArrayList<String> B) {
        if(A == null || B == null) return false;
        for (String item : A) {
            if (B.contains(item)) return false;
        }
        return true;
    }
}

