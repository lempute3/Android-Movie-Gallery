package com.example.app.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.app.repositories.TMDBMovieRepository;
import com.example.tmdb.models.MovieModel;

import java.util.List;

public class MovieListViewModel extends ViewModel
/* This class is used for View-Model */
{
    TMDBMovieRepository mTMDBMovieRepository;

    public MovieListViewModel() {
        mTMDBMovieRepository = TMDBMovieRepository.getInstance();
    }

    public LiveData<List<MovieModel>> getMovies() { return mTMDBMovieRepository.getMovies(); }
    public void searchMovieApi(String query, int page) { mTMDBMovieRepository.searchMovieApi(query, page); }

}
