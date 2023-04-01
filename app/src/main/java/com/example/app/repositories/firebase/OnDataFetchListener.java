package com.example.app.repositories.firebase;

public interface OnDataFetchListener {
    void onFetchSuccess(Object obj);
    void onFetchFailure(String message);
}
