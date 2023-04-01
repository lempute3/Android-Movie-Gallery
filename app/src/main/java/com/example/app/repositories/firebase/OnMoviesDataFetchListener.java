package com.example.app.repositories.firebase;

import com.example.tmdb.models.MovieModel;

import java.util.List;

public interface OnMoviesDataFetchListener {
    void onFetchSuccess(List<MovieModel> movies);
    void onFetchFailure(String message);
}
