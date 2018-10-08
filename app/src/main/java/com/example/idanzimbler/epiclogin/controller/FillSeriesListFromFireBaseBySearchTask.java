package com.example.idanzimbler.epiclogin.controller;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.widget.ExpandableListView;

import com.example.idanzimbler.epiclogin.modle.TvSeries;
import com.example.idanzimbler.epiclogin.view.HomeActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

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
        searchString = searchString.substring(0, 1).toUpperCase() + searchString.substring(1);
        Query query = seriesRef.orderByChild("name").startAt(searchString).endAt(Utils.nextWord(searchString));
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                TvSeriesHomeList.getInstance().clear();
                SeriesAdapter adapter = (SeriesAdapter) list.getExpandableListAdapter();
                if(adapter == null) {
                    adapter = new SeriesAdapter(context,TvSeriesHomeList.getInstance().getSeries());
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
                    TvSeriesHomeList.getInstance().getSeries().add(tvSeries);
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

