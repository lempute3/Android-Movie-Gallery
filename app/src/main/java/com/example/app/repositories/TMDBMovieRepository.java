package com.example.app.repositories;

import androidx.lifecycle.MutableLiveData;

import com.example.app.firebase.FirebaseHelper;
import com.example.app.tmdb.request.TMDBMovieApiClient;
import com.example.tmdb.models.MovieModel;

import java.util.List;

public class TMDBMovieRepository
/* This class acts as an repository for movie api. */
{
    private static TMDBMovieRepository instance;

    private TMDBMovieApiClient mTMDBMovieApiClient;

    private TMDBMovieRepository() {
        mTMDBMovieApiClient = TMDBMovieApiClient.getInstance();
    }

    public static TMDBMovieRepository getInstance() {
        if (instance == null) {
            instance = new TMDBMovieRepository();
        }
        return instance;
    }

    public MutableLiveData<List<MovieModel>> getMovies() { return  mTMDBMovieApiClient.getMovies(); }
    public void searchMovieApi(String query, int page) { mTMDBMovieApiClient.searchMoviesApi(query, page); }

}
