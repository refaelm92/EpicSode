package com.example.idanzimbler.epiclogin.controller;

import com.example.idanzimbler.epiclogin.modle.TvSeries;

import java.util.ArrayList;
import java.util.Collections;

public class TvSeriesHomeList {
    static TvSeriesHomeList instance;
    ArrayList<TvSeries> series;
    int preLast;
    int page;

    public static TvSeriesHomeList getInstance(){
        if(instance == null){
            instance = new TvSeriesHomeList();
        }
        return instance;
    }

    private TvSeriesHomeList(){

        series = new ArrayList<>();
        preLast = 0;
        page = 1;
    }

    public ArrayList<TvSeries> getSeries() {
        return series;
    }

    public void setSeries(ArrayList<TvSeries> series) {
        this.series = series;
    }

    public int getPreLast() {
        return preLast;
    }

    public void setPreLast(int preLast) {
        this.preLast = preLast;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void incrementPage(){
        page++;
    }

    public void clear(){
        series.clear();
        preLast = 0;
        page = 1;
    }

    public boolean contains(String id){
        for (TvSeries tvSeries : series) {
            if(tvSeries.getId().equals(id)) return true;
        }
        return false;
    }
}
