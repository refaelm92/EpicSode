package com.example.idanzimbler.epiclogin.modle;

import java.util.ArrayList;

public class TvSeries {
    private String name, poster;
    private float popularity;
    private int numOfSeasons;
    private ArrayList<Integer> seasons;
 //   private float rating;

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

    public ArrayList<Integer> getSeasons() {
        return seasons;
    }

    public void setSeasons(ArrayList<Integer> seasons) {
        this.seasons = seasons;
    }
//
//    public String getImdbid() {
//        return imdbid;
//    }
//
//    public void setImdbid(String imdbid) {
//        this.imdbid = imdbid;
//    }
//
//    public float getRating() {
//        return rating;
//    }
//
//    public void setRating(float rating) {
//        this.rating = rating;
//    }

    public TvSeries() {

    }

    public TvSeries(String name, float popularity, String poster, ArrayList<Integer> seasons, int numOfSeasons) {
        this.name = name;
        this.popularity = popularity;
        this.poster = poster;
        this.seasons = seasons;
        this.numOfSeasons = numOfSeasons;
    }
}
