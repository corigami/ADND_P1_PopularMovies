/*
 * Copyright (C) 2016 Corey T. Willinger - No Rights Reserved
 */
package com.gamisweb.popularmovies;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.Date;

/**
 * Movie object to store movie data
 * Created by corey on 2/18/2016.
 */
public class Movie implements Serializable {
    private int id;
    private String title;
    private int popularity;
    private int voteCount;
    private float voteAvg;
    private String posterPath;
    private String overview;
    private String releaseDate;

    private Bitmap image;

    public Movie() {
    }
    public Movie(int _id){
        this.id = _id;
    }
    public Movie(int _id, String _title){
        this(_id);
        this.title = _title;
    }

    public int getPopularity() {
        return popularity;
    }

    public String getTitle(){return title;}

    public int getId(){return id;}

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public float getVoteAvg() {
        return voteAvg;
    }

    public void setVoteAvg(float voteAvg) {
        this.voteAvg = voteAvg;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }



    public void setTitle(String title) {
        this.title = title;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns string to uniquely identify Movie object.
     * @return string the composition of id and title
     */
    public String toString(){
        return id + ", " + title;
    }
}
