package com.example.idanzimbler.epiclogin.modle;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

public class TvSeries implements Serializable,Comparable<TvSeries>{
    private String name, poster;
    private int numOfSeasons;
    private float popularity;
    private String id;
    private ArrayList<String> genres;
    private ArrayList<Season> seasonsList;

    public TvSeries()  {
    }

    public TvSeries(String id ,String name, String poster, int numOfSeasons, float popularity) {
        this.id = id;
        this.name = name;
        this.poster = poster;
        this.numOfSeasons = numOfSeasons;
        this.popularity = popularity;
        genres = new ArrayList<>();
        seasonsList = new ArrayList<>();
    }

    public TvSeries(String id,String name, float popularity, String poster, int numOfSeasons
            , ArrayList<Season> seasonsList, ArrayList<String> genres) {
        this.id = id;
        this.name = name;
        this.popularity = popularity;
        this.poster = poster;
        this.numOfSeasons = numOfSeasons;
        this.seasonsList = seasonsList;
        this.genres = genres;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getNumOfSeasons() {
        return numOfSeasons;
    }

    public void setNumOfSeasons(int numOfSeasons) {
        this.numOfSeasons = numOfSeasons;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public float getPopularity() {
        return popularity;
    }

    public void setPopularity(float popularity) {
        this.popularity = popularity;
    }

    public ArrayList<Season> getSeasonsList() {
        return seasonsList;
    }

    public void setSeasonsList(ArrayList<Season> seasonsList) {
        this.seasonsList = seasonsList;
    }


    public ArrayList<String> getGenres() {
        return genres;
    }

    public void setGenres(ArrayList<String> genres) {
        this.genres = genres;
    }

    @Override
    public String toString() {
        return "TvSeries{" +
                "name='" + name + '\'' +
                ", poster='" + poster + '\'' +
                ", numOfSeasons=" + numOfSeasons +
                ", popularity=" + popularity +
                ", seasonsList=" + seasonsList +
                '}';
    }

    @Override
    public int compareTo(@NonNull TvSeries o) {
        if(this.popularity > o.popularity) return -1;
        if(o.popularity > this.popularity) return 1;
        return 0;
    }
}
