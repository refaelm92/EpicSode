package com.example.idanzimbler.epiclogin.controller;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;

import com.example.idanzimbler.epiclogin.modle.TvSeries;
import com.example.idanzimbler.epiclogin.view.HomeActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

public class FillSeriesListFromFireBaseBySearchTask extends AsyncTask<Void, Void, Void> {
    ExpandableListView list;
    HomeActivity context;
    FirebaseDatabase database;
    DatabaseReference seriesRef;
    String searchString;
    public FillSeriesListFromFireBaseBySearchTask(HomeActivity context, ExpandableListView list,String searchString) {
        database = FirebaseDatabase.getInstance();
        seriesRef = database.getReference("TvSeries");
        this.list = list;
        this.context = context;
        this.searchString = searchString;

    }

    @Override

    protected Void doInBackground(Void... voids) {
        Query query = seriesRef.orderByChild("name").startAt(searchString).endAt(Utils.nextWord(searchString));
        Log.e("refaelTest","quert init");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.e("refaelTest","quert num of child "+dataSnapshot.getChildrenCount());
                TvSeriesList.getInstance().clear();
                CustomAdapter adapter = (CustomAdapter) list.getExpandableListAdapter();
                if(adapter == null) {
                    adapter = new CustomAdapter(context);
                    list.setAdapter(adapter);
                }
                adapter.notifyDataSetChanged();
                for (DataSnapshot tvSeriesData : dataSnapshot.getChildren()) {
                    String id = tvSeriesData.getKey();
                    String name = tvSeriesData.child("name").getValue(String.class);
                    Integer numOfSeasons = tvSeriesData.child("numOfSeasons").getValue(Integer.class);
                    String poster = tvSeriesData.child("poster").getValue(String.class);
                    Float popularity = tvSeriesData.child("popularity").getValue(Float.class);
                    TvSeries tvSeries = new TvSeries(id,name,poster,numOfSeasons,popularity);
                    TvSeriesList.getInstance().getSeries().add(tvSeries);
                    adapter.notifyDataSetChanged();
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
}

