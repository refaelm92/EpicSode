package com.example.idanzimbler.epiclogin.modle;

import java.util.ArrayList;

public class Episode {
    private ArrayList<EpisodeImage> episodesImages;
    private String overview;

    public Episode (){
        this.episodesImages = new ArrayList<>();
        this.overview = "";
    }


    public ArrayList<EpisodeImage> getEpisodesImages() {
        return episodesImages;
    }

    public void setEpisodesImages(ArrayList<EpisodeImage> episodesImages) {
        this.episodesImages = episodesImages;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }
}
