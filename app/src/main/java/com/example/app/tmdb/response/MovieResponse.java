package com.example.app.tmdb.response;

import com.example.tmdb.models.MovieModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MovieResponse
/* This class is for single movie request. */
{

    @SerializedName("results")
    @Expose()
    private MovieModel mMovie;

    public MovieModel getMovie() { return mMovie; }

    @Override
    public String toString() {
        return "MovieResponse{" +
                "mMovie=" + mMovie +
                '}';
    }
}
