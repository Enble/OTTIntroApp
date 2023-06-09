package com.example.ottintroapplication.common;

public class SimpleMovieItem {

    String rank;
    String title;
    String movieId;
    String runTime;
    String overview;
    int resId;

    public SimpleMovieItem(String rank, String title, String movieId, String runTime, String overview, int resId) {
        this.rank = rank;
        this.title = title;
        this.movieId = movieId;
        this.runTime = runTime;
        this.overview = overview;
        this.resId = resId;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getRunTime() {
        return runTime;
    }

    public void setRunTime(String runTime) {
        this.runTime = runTime;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }
}
