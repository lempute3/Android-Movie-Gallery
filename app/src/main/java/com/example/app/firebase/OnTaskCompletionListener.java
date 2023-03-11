package com.example.app.firebase;

public interface OnTaskCompletionListener {
    void onSuccess();
    void onFailure(String message);
}
