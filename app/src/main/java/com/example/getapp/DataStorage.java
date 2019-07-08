package com.example.getapp;

import java.io.Serializable;
import java.util.ArrayList;

public class DataStorage implements Serializable {
    public ArrayList<String> movies = new ArrayList<String>();
    public ArrayList<String> moviesAll = new ArrayList<String>();
    public ArrayList<String> moviestart = new ArrayList<String>();
    public ArrayList<String> movieends = new ArrayList<String>();
    public ArrayList<String> theatre = new ArrayList<String>();
    public ArrayList<String> theatreID = new ArrayList<String>();
    public ArrayList<String> theatreName = new ArrayList<String>();
    public ArrayList<String> movieimage = new ArrayList<String>();
    public ArrayList<String> movieinfo = new ArrayList<String>();
    public String currentdate;
    public String movietitle;
    int cityposition = 0;

    public void clearContent() {
        movies.clear();
        moviesAll.clear();
        moviestart.clear();
        movieends.clear();
        theatre.clear();
        movieimage.clear();
    }

}
