package com.example.app.firebase;

import com.example.tmdb.models.MovieModel;

import java.util.List;

public interface OnMoviesDataFetchListener {
    void onFetchSuccess(List<MovieModel> movies);
    void onFetchFailure(String message);
}
