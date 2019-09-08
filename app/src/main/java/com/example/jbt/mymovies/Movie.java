package com.example.jbt.mymovies;

import java.io.Serializable;

public class Movie implements Serializable {
// movie entity with 2 constructors with an option to transfer a movie object thanks to the implementation of "serializable"
    private long id;
    private String title, overview, imagepath;
    private float rating;

    public Movie(long id, String title, String overview, String imagepath, float rating) {
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.imagepath = imagepath;
        this.rating = rating;
    }

    public Movie(String title, String overview, String imagepath, float rating) {
        this.title = title;
        this.overview = overview;
        this.imagepath = imagepath;
        this.rating = rating;
    }
   // getters & setters
    public long getId() {
        return id;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getImagepath() {
        return imagepath;
    }

    public void setImagepath(String imagepath) {
        this.imagepath = imagepath;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
    // method to show the movie's details on the listview via the adapter
    @Override
    public String toString() {
        return "Movie: "+title;
    }
}
