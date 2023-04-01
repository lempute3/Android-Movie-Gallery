package com.example.app.repositories.firebase;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FirebaseUserModel {

    private String access_token, email, username;

    public FirebaseUserModel() {}
}
