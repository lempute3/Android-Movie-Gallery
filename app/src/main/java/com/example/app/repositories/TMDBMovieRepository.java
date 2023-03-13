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

    private String mQuery;
    private int mPageNumber;

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
    public void searchMovieApi(String query, int page) {
        mQuery = query;
        mPageNumber = page;
        mTMDBMovieApiClient.searchMoviesApi(query, page);
    }
    public void searchNextPage() {
        searchMovieApi(mQuery, mPageNumber+1);
    }

}
