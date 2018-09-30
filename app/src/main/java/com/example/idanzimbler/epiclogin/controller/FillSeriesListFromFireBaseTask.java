package com.example.idanzimbler.epiclogin.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.example.idanzimbler.epiclogin.modle.TvSeries;
import com.example.idanzimbler.epiclogin.view.HomeActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

public class FillSeriesListFromFireBaseTask extends AsyncTask<Void, Void, Void> {
    static final int NUM_OF_ITEMS_IN_PAGE = 15;
    ExpandableListView list;
    HomeActivity context;
    int numOfResults;
    int beginingPostion;
    FirebaseDatabase database;
    DatabaseReference seriesRef;

    public FillSeriesListFromFireBaseTask(HomeActivity context, ExpandableListView list) {
        numOfResults = TvSeriesList.getInstance().getPage()*NUM_OF_ITEMS_IN_PAGE;
        database = FirebaseDatabase.getInstance();
        seriesRef = database.getReference("TvSeries");
        this.list = list;
        this.context = context;
        this.beginingPostion = TvSeriesList.getInstance().getSeries().size();
        Log.e("refaelTest","numOfResults "+numOfResults);

    }

    @Override

    protected Void doInBackground(Void... voids) {
        seriesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.e("refaelTest","numOfChild "+dataSnapshot.getChildrenCount());
                Log.e("refaelTest","beginPos "+beginingPostion);
                CustomAdapter adapter = (CustomAdapter) list.getExpandableListAdapter();
                if(adapter == null) {
                    adapter = new CustomAdapter(context);
                    list.setAdapter(adapter);
                }
                adapter.notifyDataSetChanged();
                Iterator<DataSnapshot> itr = dataSnapshot.getChildren().iterator();
                int counter = 0;
                while(itr.hasNext()){
                    if(counter < beginingPostion){
                        itr.next();
                        counter++;
                        continue;
                    }
                    if(counter < numOfResults){
                        try {
                            DataSnapshot tvSeriesData = itr.next();
                            String id = tvSeriesData.getKey();
                            String name = tvSeriesData.child("name").getValue(String.class);
                            Integer numOfSeasons = tvSeriesData.child("numOfSeasons").getValue(Integer.class);
                            String poster = tvSeriesData.child("poster").getValue(String.class);
                            Float popularity = tvSeriesData.child("popularity").getValue(Float.class);
                            TvSeries tvSeries = new TvSeries(id,name,poster,numOfSeasons,popularity);
                            TvSeriesList.getInstance().getSeries().add(tvSeries);
                            adapter.notifyDataSetChanged();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        counter++;
                    }else{
                        break;
                    }
                }
                context.getProgressBar().setVisibility(View.INVISIBLE);

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

