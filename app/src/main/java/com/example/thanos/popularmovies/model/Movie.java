package com.example.thanos.popularmovies.model;

/**
 * Created by Thanos on 3/20/2018.
 */

public class Movie {
    private String movieTitle;
    private String description;
    private String language;
    private String image;
    private String releaseDate;
    private boolean isAdult;

    public Movie(){
    }

    public Movie(String movieTitle, String description, String language, String image, String releaseDate, boolean isAdult){
        this.movieTitle = movieTitle;
        this.description = description;
        this.language = language;
        this.image = image;
        this.releaseDate = releaseDate;
        this.isAdult = isAdult;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public boolean isAdult() {
        return isAdult;
    }

    public void setAdult(boolean adult) {
        isAdult = adult;
    }
}
