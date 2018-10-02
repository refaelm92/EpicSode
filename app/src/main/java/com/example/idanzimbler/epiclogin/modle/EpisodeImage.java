package com.example.idanzimbler.epiclogin.modle;

public class EpisodeImage {
    private int likes;
    private String url;


    public EpisodeImage() {
    }

    public EpisodeImage(int likes, String url) {
        this.likes = likes;
        this.url = url;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
