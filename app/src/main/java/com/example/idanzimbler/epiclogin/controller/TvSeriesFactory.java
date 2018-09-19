package com.example.idanzimbler.epiclogin.controller;

import com.example.idanzimbler.epiclogin.modle.TvSeries;

import org.json.JSONObject;

public class TvSeriesFactory {
    public TvSeries getTvSeries(JSONObject obj){
        TvSeries ts = new TvSeries();
        try {
            ts.setName(obj.getString("Title").toString());
 //           ts.setImdbid(obj.getString("imdbID").toString());
            ts.setNumOfSeasons(Integer.parseInt(obj.getString("totalSeasons").toString()));
 //           ts.setRating(Float.parseFloat(obj.getString("imdbRating").toString()));
            ts.setPoster(obj.getString("Poster").toString());

        } catch (Exception e) {
            return null;
        }
        return ts;

    }
}
