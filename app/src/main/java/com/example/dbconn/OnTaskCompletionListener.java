package com.example.dbconn;

public interface OnTaskCompletionListener {
    void onSuccess();
    void onFailure(String message);
}
