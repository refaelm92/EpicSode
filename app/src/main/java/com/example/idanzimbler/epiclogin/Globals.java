package com.example.idanzimbler.epiclogin;

import java.util.ArrayList;

public class Globals {


    private static Globals instance = new Globals();

    // Getter-Setters
    public static Globals getInstance() {
        return instance;
    }

    public static void setInstance(Globals instance) {
        Globals.instance = instance;
    }

    public ArrayList<String> seasons;


    private Globals() {

    }


    public ArrayList<String> getValue() {
        return seasons;
    }


    public void setValue(ArrayList<String> seasons) {
        this.seasons = seasons;
    }


}