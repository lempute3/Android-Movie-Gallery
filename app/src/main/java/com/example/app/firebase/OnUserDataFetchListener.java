package com.example.app.firebase;

public interface OnUserDataFetchListener {
    void onFetchSuccess(FirebaseUserModel user);
    void onFetchFailure(String message);
}
