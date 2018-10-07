package com.example.idanzimbler.epiclogin.modle;

import java.util.ArrayList;

public class User {
    public String email, age, sex;
    ArrayList<String> genres;
    ArrayList<String> favorites;

    public User() {

    }

    public User(String email, String age, String sex,
                ArrayList<String> genres,ArrayList<String> favorites) {
        this.email = email;
        this.age = age;
        this.sex = sex;
        this.genres = genres;
        this.favorites = favorites;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public ArrayList<String> getGenres() {
        return genres;
    }

    public void setGenres(ArrayList<String> genres) {
        this.genres = genres;
    }

    public ArrayList<String> getFavorites() {
        return favorites;
    }

    public void setFavorites(ArrayList<String> favorites) {
        this.favorites = favorites;
    }
}
