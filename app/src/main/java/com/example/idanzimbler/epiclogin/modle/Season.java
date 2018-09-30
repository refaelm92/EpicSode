package com.example.idanzimbler.epiclogin.modle;


import java.util.ArrayList;

public class Season {
    private ArrayList<Episode> episodesList;
    private String seasonPoster;

    public Season() {
        episodesList = new ArrayList<>();
        seasonPoster = "";
    }

    public ArrayList<Episode> getEpisodesList() {
        return episodesList;
    }

    public void setEpisodesList(ArrayList<Episode> episodesList) {
        this.episodesList = episodesList;
    }

    public String getSeasonPoster() {
        return seasonPoster;
    }

    public void setSeasonPoster(String seasonPoster) {
        this.seasonPoster = seasonPoster;
    }
}
