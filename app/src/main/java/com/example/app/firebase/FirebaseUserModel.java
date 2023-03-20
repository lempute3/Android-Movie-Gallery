package com.example.app.firebase;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FirebaseUserModel {

    private String email, username;

    public FirebaseUserModel() {}
}
