package com.example.app.firebase;

public interface OnUserDataFetchListener {
    void onFetchSuccess(User user);
    void onFetchFailure(String message);
}
