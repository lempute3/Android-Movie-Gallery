package com.example.app.tmdb.request;

import com.example.tmdb.models.MovieModel;

import java.util.List;

public interface OnResponseCompletionListener {
    void onResponseSuccess(List<MovieModel> movies);
    void onResponseFailure(String message);
}
