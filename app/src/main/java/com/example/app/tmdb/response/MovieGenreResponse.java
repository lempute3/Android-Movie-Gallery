package com.example.app.tmdb.response;

import com.example.tmdb.models.MovieGenreModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieGenreResponse
/* This class if used for getting list of official movie genres. */
{
    @SerializedName("genres")
    @Expose()
    private List<MovieGenreModel> mMovieGenreList;

    public List<MovieGenreModel> getMovieGenreList()  { return mMovieGenreList; }

    @Override
    public String toString() {
        return "MovieGenreResponse{" +
                "mMovieGenreList=" + mMovieGenreList +
                '}';
    }
}
