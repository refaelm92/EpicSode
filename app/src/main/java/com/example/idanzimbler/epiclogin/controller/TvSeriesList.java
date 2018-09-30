package com.example.idanzimbler.epiclogin.controller;

import com.example.idanzimbler.epiclogin.modle.TvSeries;

import java.util.ArrayList;
import java.util.Collections;

public class TvSeriesList {
    static TvSeriesList instance;
    ArrayList<TvSeries> series;
    int preLast;
    int page;

    public static TvSeriesList getInstance(){
        if(instance == null){
            instance = new TvSeriesList();
        }
        return instance;
    }

    private TvSeriesList(){
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

    public void sort(){
        Collections.sort(series);
    }
}
