package com.example.app.repositories.firebase;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FirebaseTempUserModel {

    private String user_id, access_key;
    private long created_at;

    public FirebaseTempUserModel() {}
}
