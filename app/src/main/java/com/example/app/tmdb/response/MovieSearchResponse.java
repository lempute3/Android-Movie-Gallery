package com.example.app.tmdb.response;

import com.example.tmdb.models.MovieModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieSearchResponse
/* This class if for getting multiple movies (Movie list) */
{
    @SerializedName("total_results")
    @Expose()
    private int mTotalCount;

    @SerializedName("results")
    @Expose()
    private List<MovieModel> mMovieList;

    public int              getTotalCount() { return mTotalCount; }
    public List<MovieModel> getMovieList()  { return mMovieList; }

    @Override
    public String toString() {
        return "MovieSearchResponse{" +
                "mTotalCount=" + mTotalCount +
                ", mMovieList=" + mMovieList +
                '}';
    }
}
