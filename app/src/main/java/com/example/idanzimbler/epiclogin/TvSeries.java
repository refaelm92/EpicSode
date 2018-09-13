package com.example.idanzimbler.epiclogin;

import java.util.ArrayList;

public class TvSeries {
    public String name, poster, popularity;
    public int numOfSeasons;
    public ArrayList<String> seasons;

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

    public String getPopularity() {
        return popularity;
    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }

    public ArrayList<String> getSeasons() {
        return seasons;
    }

    public void setSeasons(ArrayList<String> seasons) {
        this.seasons = seasons;
    }

    public TvSeries() {

    }

    public TvSeries(String name, String popularity, String poster, ArrayList<String> seasons, int numOfSeasons) {
        this.name = name;
        this.popularity = popularity;
        this.poster = poster;
        this.seasons = seasons;
        this.numOfSeasons = numOfSeasons;
    }
}
