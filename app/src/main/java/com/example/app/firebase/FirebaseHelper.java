package com.example.app.firebase;

import androidx.annotation.NonNull;

import com.example.app.utils.AppCredentials;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.CompletableFuture;

public class FirebaseHelper {

    private static FirebaseHelper instance;
    private FirebaseAuth mAuth;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mUsersCollection;


    private FirebaseHelper() {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance(AppCredentials.FIREBASE_DB_URL);

        mUsersCollection = mDatabase.getReference("Users");
    }

    public static FirebaseHelper getInstance() {
        if (instance == null) {
            instance = new FirebaseHelper();
        }
        return instance;
    }

    public FirebaseUser getCurrentUser() {
        return mAuth.getCurrentUser();
    }
    public void signOutUser() { mAuth.signOut(); }


    public void getCurrentUserData(final OnUserDataFetchListener listener)
    /*  This method is used retrieve the current user's data from a
        firebase realtime database collection. */
    {
        String userID = mAuth.getCurrentUser().getUid();
        mUsersCollection.child(userID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);
                        if (user != null) listener.onFetchSuccess(user);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        listener.onFetchFailure(error.getMessage());
                    }
                });
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

    public void registerUser(String email, String username, String password, final OnTaskCompletionListener listener)
    /* This method is used to register a new user in a Firebase database
       by creating a new user in Firebase authentication and storing the user's information in the database. */
    {
        User user = new User(email, username);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    mUsersCollection.child(mAuth.getCurrentUser().getUid()).setValue(user)
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

    public CompletableFuture<Boolean> userEmailVerify() {
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        mAuth.getCurrentUser().sendEmailVerification()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        future.complete(true);
                    } else {
                        future.complete(false);
                    }
                });

        return future;
    }
}
