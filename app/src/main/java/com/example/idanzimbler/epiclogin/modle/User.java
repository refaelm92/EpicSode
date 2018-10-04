package com.example.idanzimbler.epiclogin.modle;

import java.util.ArrayList;

public class User {
    public String email, age, sex;
    ArrayList<Boolean> checkedGenres;
    ArrayList<String> favorites;

    public User() {

    }

    public User(String email, String age, String sex,
                ArrayList<Boolean> checkedGenres,ArrayList<String> favorites) {
        this.email = email;
        this.age = age;
        this.sex = sex;
        this.checkedGenres = checkedGenres;
        this.favorites = favorites;
    }
}
