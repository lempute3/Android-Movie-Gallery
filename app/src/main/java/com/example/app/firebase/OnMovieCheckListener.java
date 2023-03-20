package com.example.app.firebase;

public interface OnMovieCheckListener {
    void onMovieExist(boolean exist);
    void onMovieError(String message);
}
