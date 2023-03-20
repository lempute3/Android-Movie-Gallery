package com.example.app.repositories;

import androidx.lifecycle.MutableLiveData;

import com.example.app.tmdb.request.TMDBMovieApiClient;
import com.example.tmdb.models.MovieGenreModel;
import com.example.tmdb.models.MovieModel;

import java.util.List;

public class TMDBMovieRepository
/* This class acts as an repository for movie api. */
{
    private static TMDBMovieRepository instance;

    private TMDBMovieApiClient mTMDBMovieApiClient;

    private String mQuery;
    private int mGenreId, mPageNumber;

    private TMDBMovieRepository() {
        mTMDBMovieApiClient = TMDBMovieApiClient.getInstance();
    }

    public static TMDBMovieRepository getInstance() {
        if (instance == null) {
            instance = new TMDBMovieRepository();
        }
        return instance;
    }

    public MutableLiveData<List<MovieModel>> getMovies()      { return  mTMDBMovieApiClient.getMovies(); }
    public MutableLiveData<List<MovieGenreModel>> getGenres() { return mTMDBMovieApiClient.getMovieGenres(); }
    public void searchMovieGenresApi()                        { mTMDBMovieApiClient.searchMovieGenresApi(); }
    public void searchMovieApi(String query, int genre_id, int page) {
        mQuery = query;
        mGenreId = genre_id;
        mPageNumber = page;
        mTMDBMovieApiClient.searchMoviesApi(query, genre_id, page);
    }
    public void searchNextPage() {
        searchMovieApi(mQuery, mGenreId, mPageNumber+1);
    }
    public void sortMoviesByVote(float rating) {mTMDBMovieApiClient.sortMoviesByVote(rating);}

}
