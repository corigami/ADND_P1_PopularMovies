package com.gamisweb.popularmovies;

/**
 * Movie object to store data
 * Created by corey on 2/18/2016.
 */
public class Movie {
    private int id;
    private String title;
    private int popularity;
    private int voteCount;
    private float voteAvg;
    private String posterPath;

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

    /**
     * Returns string to uniquely identify Movie object.
     * @return string composition of id, title, and posterPath
     */
    public String toString(){
        return id + ", " + title + ", " + posterPath;
    }
}
