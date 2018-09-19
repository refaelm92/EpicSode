package com.example.idanzimbler.epiclogin.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ExpandableListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.idanzimbler.epiclogin.modle.TvSeries;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GetListOfSeriesTask extends AsyncTask<Void,Void,Void> {
    int NO_RESULTS_YET = -1;
    ExpandableListView list;
    TvSeriesFactory seriesFactory;
    ArrayList<TvSeries> series;
    RequestQueue rq;
    Context context;
    int page;
    int progress;
    boolean finished;
    String DISCOVER_URL = "https://api.themoviedb.org/3/discover/tv?" +
            "api_key=6d323285045793484dd7e2c60ef89365&language=en-US&" +
            "sort_by=popularity.desc&" +
            "include_null_first_air_dates=false&page=";
    String FIND_SERIES_URL = "http://www.omdbapi.com/?apikey=e89ae9dc&t=";
    int resultsCount;
    int resultsProgressed;
    int beginingPostion;

    public GetListOfSeriesTask(Context context, ExpandableListView list, int page,ArrayList<TvSeries> series) {
        this.list = list;
        this.seriesFactory = new TvSeriesFactory();
        this.series = series;
        this.context = context;
        this.page = page;
        this.progress = 0;
        this.finished = false;
        resultsCount = NO_RESULTS_YET;
        resultsProgressed = 0;
        this.beginingPostion = series.size()-1;
    }

    @Override

    protected Void doInBackground(Void... voids) {
        rq = Volley.newRequestQueue(context);
        JsonObjectRequest or = new JsonObjectRequest(Request.Method.GET, DISCOVER_URL + page,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray results = response.getJSONArray("results");
                    resultsCount = results.length();
                    for (int i = 0; i < resultsCount; i++) {
                        JSONObject ob = results.getJSONObject(i);
                        String seriesName = ob.getString("original_name").toString().replace(" ", "+");
                        JsonObjectRequest fjor = new JsonObjectRequest(Request.Method.GET,
                                FIND_SERIES_URL +
                                        seriesName,
                                null, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject res) {
                                resultsProgressed++;
                                TvSeries ts = seriesFactory.getTvSeries(res);
                                if(ts != null && ts.getName() != null && ts.getPoster() != null)
                                    series.add(ts);
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        });
                        rq.add(fjor);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }
        );
        rq.add(or);

        while (resultsCount == NO_RESULTS_YET || resultsCount!=resultsProgressed) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        CustomAdapter adapter = new CustomAdapter(context, series);
        list.setAdapter(adapter);
        list.setSelection(beginingPostion);
    }
}

