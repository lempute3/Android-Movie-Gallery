package com.example.app.repositories.firebase;

public interface OnTaskCompletionListener {
    void onSuccess();
    void onFailure(String message);
}
