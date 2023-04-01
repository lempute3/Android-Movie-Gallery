package com.example.app.repositories.firebase;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.app.utils.AppCredentials;
import com.example.tmdb.models.MovieModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FirebaseRepository {

    private static FirebaseRepository instance;
    private FirebaseAuth mAuth;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mUsersCollection, mUsersWatchlistCollection, mTempUsersCollection, mUsersFavouritesCollection;

    private String mUserId;
    private MutableLiveData<List<MovieModel>> mUserWatchlist;

    private FirebaseRepository() {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance(AppCredentials.FIREBASE_DB_URL);

        mUserWatchlist = new MutableLiveData<>();

        mUsersCollection = mDatabase.getReference("Users");
        mUsersWatchlistCollection = mDatabase.getReference("Watchlist");
        mTempUsersCollection = mDatabase.getReference("TempUsers");
        mUsersFavouritesCollection = mDatabase.getReference("Favourite");
    }

    public static FirebaseRepository getInstance() {
        if (instance == null) {
            instance = new FirebaseRepository();
        }
        return instance;
    }

    public FirebaseUser getCurrentUser() {
        return mAuth.getCurrentUser();
    }
    public void signOutUser() { mAuth.signOut(); }

    private void getCollectionData(DatabaseReference collection, Class<?> modelClass, final OnDataFetchListener listener) {
        collection.child(mUserId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Object data = snapshot.getValue(modelClass);
                        if (data != null) listener.onFetchSuccess(data);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        listener.onFetchFailure(error.getMessage());
                    }
                });
    }

    private void getMoviesFromCollection(DatabaseReference movieCollection, final OnMoviesDataFetchListener listener) {
        movieCollection.child(mUserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<MovieModel> movies = new ArrayList<>();
                for (DataSnapshot movieSnapshot : snapshot.getChildren()) {
                    MovieModel movie = movieSnapshot.getValue(MovieModel.class);
                    if (movie != null) {
                        movies.add(movie);
                    }
                }
                listener.onFetchSuccess(movies);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onFetchFailure(error.getMessage());
            }
        });
    }

    private void checkMovieInCollection(DatabaseReference movieCollection, int movieId, final OnMovieCheckListener listener) {
        movieCollection.child(mUserId)
                .orderByChild("id").equalTo(movieId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        listener.onMovieExist(snapshot.exists());
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        listener.onMovieError(error.getMessage());
                    }
                });
    }

    private void addMovieToCollection(DatabaseReference movieCollection, MovieModel movieModel, final OnTaskCompletionListener listener) {
        checkMovieInCollection(movieCollection, movieModel.getId(), new OnMovieCheckListener() {
            @Override
            public void onMovieExist(boolean exist) {
                if (exist) {
                    listener.onFailure("Movie already exists in watchlist");
                } else {
                    movieCollection.child(mUserId).push().setValue(movieModel, (error, ref) -> {
                        if (error == null) listener.onSuccess();
                        else listener.onFailure(error.getMessage());
                    });
                }
            }

            @Override
            public void onMovieError(String message) {
                listener.onFailure(message);
            }
        });
    }

    private void removeMovieFromCollection(DatabaseReference movieCollection, int movieId, final OnTaskCompletionListener listener) {
        movieCollection.child(mUserId)
                .orderByChild("id").equalTo(movieId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            DataSnapshot movieSnapshot = snapshot.getChildren().iterator().next();
                            movieSnapshot.getRef().removeValue((error, ref) -> {
                                if (error == null) listener.onSuccess();
                                else listener.onFailure(error.getMessage());
                            });
                        } else {
                            listener.onFailure("Movie not found in watchlist");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        listener.onFailure(error.getMessage());
                    }
                });
    }

    public void addMovieToWatchlist(MovieModel movieModel, final OnTaskCompletionListener listener) {
        addMovieToCollection(mUsersWatchlistCollection, movieModel, new OnTaskCompletionListener() {
            @Override
            public void onSuccess() {
                listener.onSuccess();
            }

            @Override
            public void onFailure(String message) {
                listener.onFailure(message);
            }
        });
    }

    public void addMovieToFavourites(MovieModel movieModel, final OnTaskCompletionListener listener) {
        addMovieToCollection(mUsersFavouritesCollection, movieModel, new OnTaskCompletionListener() {
            @Override
            public void onSuccess() {
                listener.onSuccess();
            }

            @Override
            public void onFailure(String message) {
                listener.onFailure(message);
            }
        });
    }

    public void removeMovieFromWatchlist(int movieId, final OnTaskCompletionListener listener) {
        removeMovieFromCollection(mUsersWatchlistCollection, movieId, new OnTaskCompletionListener() {
            @Override
            public void onSuccess() {
                listener.onSuccess();
            }

            @Override
            public void onFailure(String message) {
                listener.onFailure(message);
            }
        });
    }

    public void removeMovieFromFavourites(int movieId, final OnTaskCompletionListener listener) {
        removeMovieFromCollection(mUsersFavouritesCollection, movieId, new OnTaskCompletionListener() {
            @Override
            public void onSuccess() {
                listener.onSuccess();
            }

            @Override
            public void onFailure(String message) {
                listener.onFailure(message);
            }
        });
    }

    public void checkMovieInWatchlist(int movieId, final OnMovieCheckListener listener) {
        checkMovieInCollection(mUsersWatchlistCollection, movieId, new OnMovieCheckListener() {
            @Override
            public void onMovieExist(boolean exist) {
                listener.onMovieExist(exist ? true : false);
            }

            @Override
            public void onMovieError(String message) {
                listener.onMovieError(message);
            }
        });
    }

    public void checkMovieInFavourites(int movieId, final OnMovieCheckListener listener) {
        checkMovieInCollection(mUsersFavouritesCollection, movieId, new OnMovieCheckListener() {
            @Override
            public void onMovieExist(boolean exist) {
                listener.onMovieExist(exist ? true : false);
            }

            @Override
            public void onMovieError(String message) {
                listener.onMovieError(message);
            }
        });
    }

    public void searchUserWatchlist() {
        getMoviesFromCollection(mUsersWatchlistCollection, new OnMoviesDataFetchListener() {
            @Override
            public void onFetchSuccess(List<MovieModel> movies) {
                mUserWatchlist.postValue(movies);
            }

            @Override
            public void onFetchFailure(String message) {
                mUserWatchlist.postValue(null);
                Log.e("FIREBASE_MOVIE_FETCH_ERROR", message);
            }
        });
    }

    public void searchUserWatchlist(String query) {
        getMoviesFromCollection(mUsersWatchlistCollection, new OnMoviesDataFetchListener() {
            @Override
            public void onFetchSuccess(List<MovieModel> movies) {
                List<MovieModel> searchResults = new ArrayList<>();
                for (MovieModel movie : movies) {
                    if (movie.getTitle().toLowerCase().contains(query.toLowerCase())) {
                        searchResults.add(movie);
                    }
                }
                mUserWatchlist.postValue(searchResults);
            }

            @Override
            public void onFetchFailure(String message) {
                mUserWatchlist.postValue(null);
                Log.e("FIREBASE_MOVIE_FETCH_ERROR", message);
            }
        });
    }

    public void getCurrentUserData(final OnDataFetchListener listener)
    /*  This method is used retrieve the current user's data from a
        firebase realtime database collection. */
    {
        getCollectionData(mUsersCollection, FirebaseUserModel.class, new OnDataFetchListener() {
            @Override
            public void onFetchSuccess(Object obj) {
                listener.onFetchSuccess(obj);
            }

            @Override
            public void onFetchFailure(String message) {
                listener.onFetchFailure(message);
            }
        });
    }

    public void loginUser(String email, String password, final OnTaskCompletionListener listener)
    /* This method is used to log in a user to a system using their email and password. */
    {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        mUserId = mAuth.getCurrentUser().getUid();
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
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String accessToken = mAuth.getCurrentUser().getIdToken(false).getResult().getToken();
                        FirebaseUserModel user = new FirebaseUserModel(accessToken, email, username);
                        mUsersCollection.child(mAuth.getCurrentUser().getUid()).setValue(user)
                                .addOnSuccessListener(aVoid -> listener.onSuccess())
                                .addOnFailureListener(e -> listener.onFailure(e.getMessage()));
                    } else {
                        listener.onFailure(task.getException().getMessage());
                    }
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

    public void generateTempAccessKey(final OnDataFetchListener listener) {
        String accessKey = UUID.randomUUID().toString();
        long timestamp = System.currentTimeMillis();
        FirebaseTempUserModel tempUser = new FirebaseTempUserModel(mUserId, accessKey, timestamp);

        mTempUsersCollection.child(mUserId).setValue(tempUser)
                .addOnSuccessListener(aVoid -> listener.onFetchSuccess(tempUser))
                .addOnFailureListener(e -> listener.onFetchFailure(e.getMessage()));
    }

    public MutableLiveData<List<MovieModel>> getUserWatchlist() {
        return mUserWatchlist;
    }
}
