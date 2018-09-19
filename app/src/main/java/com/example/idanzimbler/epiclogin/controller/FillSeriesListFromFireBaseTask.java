package com.example.idanzimbler.epiclogin.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ExpandableListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.idanzimbler.epiclogin.modle.TvSeries;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class FillSeriesListFromFireBaseTask extends AsyncTask<Void, Void, Void> {
    static final int NUM_OF_ITEMS_IN_PAGE = 20;
    ExpandableListView list;
    TvSeriesFactory seriesFactory;
    ArrayList<TvSeries> series;
    Context context;
    int page;
    int numOfResults;
    int beginingPostion;
    FirebaseDatabase database;
    DatabaseReference seriesRef;

    public FillSeriesListFromFireBaseTask(Context context, ExpandableListView list, int page, ArrayList<TvSeries> series) {
        numOfResults = page*NUM_OF_ITEMS_IN_PAGE;
        database = FirebaseDatabase.getInstance();
        seriesRef = database.getReference("TvSeries").limitToFirst(numOfResults).getRef();
        this.list = list;
        this.seriesFactory = new TvSeriesFactory();
        this.series = series;
        this.context = context;
        this.page = page;
        this.beginingPostion = series.size();
    }

    @Override

    protected Void doInBackground(Void... voids) {
        seriesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> itr = dataSnapshot.getChildren().iterator();
                int counter = 0;
                while(itr.hasNext()){
                    if(counter > beginingPostion && counter < numOfResults){
                        try {
                            TvSeries tvSeries = itr.next().getValue(TvSeries.class);
                            series.add(tvSeries);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        counter++;
                    }else{
                        itr.next();
                        counter++;
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
        CustomAdapter adapter = new CustomAdapter(context, series);
        list.setAdapter(adapter);
        list.setSelection(beginingPostion - 1);
    }
}

