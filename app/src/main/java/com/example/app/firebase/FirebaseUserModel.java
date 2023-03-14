package com.example.app.firebase;

public class FirebaseUserModel {

    private String email, username;

    public FirebaseUserModel() {}

    public FirebaseUserModel(String email, String username) {
        this.email = email;
        this.username = username;
    }

    public String getEmail()    { return email;    }
    public String getUsername() { return username; }

}
