package com.example.dbconn;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseHelper {

    private static FirebaseHelper instance;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;

    private FirebaseHelper() {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance(" https://flickframe-c767b-default-rtdb.europe-west1.firebasedatabase.app");
    }

    public static FirebaseHelper getInstance() {
        if (instance == null) {
            instance = new FirebaseHelper();
        }
        return instance;
    }

    public void loginUser(String email, String password, final OnTaskCompletionListener listener)
    /* This method is used to log in a user to a system using their email and password. */
    {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        listener.onSuccess();
                    } else {
                        listener.onFailure(task.getException().getMessage());
                    }
                });
    }

    public void registerUser(String reference, String email, String username, String password, final OnTaskCompletionListener listener)
    /* This method is used to register a new user in a Firebase database
       by creating a new user in Firebase authentication and storing the user's information in the database. */
    {
        DatabaseReference dbReference = mDatabase.getReference(reference);
        User user = new User(email, username);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    dbReference.child(mAuth.getCurrentUser().getUid()).setValue(user)
                            .addOnSuccessListener(aVoid -> listener.onSuccess())
                            .addOnFailureListener(e -> listener.onFailure(e.getMessage()));
                });
    }

    public void resetPassword(String email, final OnTaskCompletionListener listener) {
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        listener.onSuccess();
                    } else {
                        listener.onFailure(task.getException().getMessage());
                    }
                });
    }

}
