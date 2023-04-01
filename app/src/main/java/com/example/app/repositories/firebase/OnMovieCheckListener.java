package com.example.app.repositories.firebase;

public interface OnMovieCheckListener {
    void onMovieExist(boolean exist);
    void onMovieError(String message);
}
