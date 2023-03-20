package com.example.app.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.app.firebase.FirebaseRepository;
import com.example.app.repositories.TMDBMovieRepository;
import com.example.tmdb.models.MovieGenreModel;
import com.example.tmdb.models.MovieModel;

import java.util.List;

public class MovieListViewModel extends ViewModel
/* This class is used for View-Model */
{
    TMDBMovieRepository mTMDBMovieRepository;
    FirebaseRepository mFirebaseRepository;

    public MovieListViewModel() {
        mTMDBMovieRepository = TMDBMovieRepository.getInstance();
        mFirebaseRepository = FirebaseRepository.getInstance();
    }

    public LiveData<List<MovieModel>> getMovies()            { return mTMDBMovieRepository.getMovies(); }
    public LiveData<List<MovieGenreModel>> getMoviesGenres() { return mTMDBMovieRepository.getGenres(); }

    public void searchMovieGenresApi()                                { mTMDBMovieRepository.searchMovieGenresApi(); }
    public void searchMovieApi(String query, int genre_id, int page)  { mTMDBMovieRepository.searchMovieApi(query, genre_id, page); }
    public void searchNextPage()                                      { mTMDBMovieRepository.searchNextPage(); }
    public void sortMoviesByVote(float rating)                        { mTMDBMovieRepository.sortMoviesByVote(rating); }

    public LiveData<List<MovieModel>> getUserWatchlist()              { return mFirebaseRepository.getUserWatchlist(); }
    public void searchUserWatchlist()                                 { mFirebaseRepository.searchUserWatchlist(); }
    public void searchUserWatchlist(String query)                     { mFirebaseRepository.searchUserWatchlist(query);}
}
