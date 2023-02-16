package com.example.dbconn;

public class User {

    private String mEmail, mUsername;

    public User(String email, String username) {
        mUsername = username;
        mEmail = email;
    }

    public String getEmail()    { return mEmail;    }
    public String getUsername() { return mUsername; }

}
