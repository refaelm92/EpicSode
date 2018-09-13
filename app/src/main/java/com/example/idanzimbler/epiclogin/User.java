package com.example.idanzimbler.epiclogin;

import java.util.ArrayList;

public class User {
    public String email, age, sex;
    ArrayList<Boolean> checkedGenres;


    public User() {

    }

    public User(String email, String age, String sex, ArrayList<Boolean> checkedGenres) {
        this.email = email;
        this.age = age;
        this.sex = sex;
        this.checkedGenres = checkedGenres;
    }
}
