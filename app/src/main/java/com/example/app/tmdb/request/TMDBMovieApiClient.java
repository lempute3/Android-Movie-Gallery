package com.example.app.tmdb.request;


import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.app.tmdb.response.MovieGenreResponse;
import com.example.app.tmdb.response.MovieSearchResponse;
import com.example.app.viewmodels.AppExecutors;
import com.example.tmdb.TMDBApi;
import com.example.app.utils.AppCredentials;
import com.example.tmdb.models.MovieGenreModel;
import com.example.tmdb.models.MovieModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TMDBMovieApiClient {

    private static TMDBMovieApiClient instance;

    /*TASK RUNNABLE*/
    private AppExecutors mAppExecutors;
    private RetrieveMoviesRunnable mRetrieveMoviesRunnable;
    private RetrieveMovieGenresRunnable mRetrieveMovieGenresRunnable;

    // LiveData.
    private MutableLiveData<List<MovieModel>> mMovies;
    private MutableLiveData<List<MovieGenreModel>> mMovieGenres;

    private TMDBMovieApiClient() {
        mMovies = new MutableLiveData<>();
        mMovieGenres = new MutableLiveData<>();
        mAppExecutors = AppExecutors.getInstance();
    }

    public static TMDBMovieApiClient getInstance() {
        if (instance == null) {
            instance = new TMDBMovieApiClient();
        }
        return instance;
    }

    private void apiTaskScheduler(Future task)
    /* This method is used for schedule apu call. */
    {
        mAppExecutors.getNetworkIO().schedule(() -> {
            task.cancel(true);
        }, mAppExecutors.CANCEL_TIMEOUT_MILLIS, TimeUnit.MICROSECONDS);
    }

    public void searchMoviesApi(String query, int genre_id, int page)
    /* Retrieves query search movie data from tmdb movie api. */
    {
        if (mRetrieveMoviesRunnable != null) { mRetrieveMoviesRunnable = null; }
        mRetrieveMoviesRunnable = new RetrieveMoviesRunnable(query, genre_id, page);

        final Future task = mAppExecutors.getNetworkIO().submit(mRetrieveMoviesRunnable);
        apiTaskScheduler(task);
    }

    public void searchMovieGenresApi()
    /* Retrieves a list of official movie genres. */
    {
        if (mRetrieveMovieGenresRunnable != null) { mRetrieveMovieGenresRunnable = null; }
        mRetrieveMovieGenresRunnable = new RetrieveMovieGenresRunnable();

        // Executor for.
        final Future task = mAppExecutors.getNetworkIO().submit(mRetrieveMovieGenresRunnable);
        apiTaskScheduler(task);
    }

    public MutableLiveData<List<MovieModel>> getMovies()           { return mMovies; }
    public MutableLiveData<List<MovieGenreModel>> getMovieGenres() { return mMovieGenres; }

    public void sortMoviesByVote(float rating)
    /* This method sorts out movie list based on the given rating. */
    {
        List<MovieModel> movieModels = mMovies.getValue();
        if (movieModels == null || movieModels.isEmpty()) return;

        float newRating = (rating / 5.0f) * 10.0f;
        Log.e("RA", Float.toString(newRating));

        List<MovieModel> sortedMovies = movieModels.stream()
                .filter(movie -> movie.getVote_average() <= newRating)
                .collect(Collectors.toList());
        mMovies.setValue(sortedMovies);
    }

    private class RetrieveMovieGenresRunnable implements Runnable {
        private TMDBRetrofitHelper mRetrofitHelper;
        private TMDBApi mTMDBApi;
        private boolean isRequestCancel;

        private RetrieveMovieGenresRunnable() {
            mRetrofitHelper = TMDBRetrofitHelper.getInstance();
            mTMDBApi = mRetrofitHelper.getTMDBApi();
            isRequestCancel = false;
        }

        @Override
        public void run() {
            getMovieGenreList().enqueue(new Callback<MovieGenreResponse>() {
                @Override
                public void onResponse(Call<MovieGenreResponse> call, Response<MovieGenreResponse> response) {
                    try {
                        if (!response.isSuccessful())
                            throw new IOException("Genre response error: " + response.errorBody().string());

                        // Gets genre list from a response.
                        List<MovieGenreModel> movieGenreList = new ArrayList<>( ( (MovieGenreResponse) response.body()).getMovieGenreList());

                        if (movieGenreList.isEmpty() || movieGenreList == null)
                            throw new IOException("Genre response error: request empty");

                        mMovieGenres.postValue(movieGenreList);

                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("GENRE_REQUEST_ERROR", e.getMessage());
                        mMovieGenres.postValue(null);
                    }
                }

                @Override
                public void onFailure(Call<MovieGenreResponse> call, Throwable t) {
                    Log.e("GENRE_REQUEST_ERROR", t.getMessage());
                    mMovieGenres.postValue(null);
                }
            });
        }

        private Call<MovieGenreResponse> getMovieGenreList() {
            Call<MovieGenreResponse> responseCall = mTMDBApi.searchMovieGenres(AppCredentials.TMDB_API_KEY);
            return responseCall;
        }

        private void cancelRequest() {
            Log.i("GENRE_REQUEST", "Cancelling search request");
            isRequestCancel = true;
        }
    }

    private class RetrieveMoviesRunnable implements Runnable {

        public static final String QUERY_TYPE_GENRE = "GENRE";
        public static final String QUERY_TYPE_SEARCH = "SEARCH";

        private TMDBRetrofitHelper mRetrofitHelper;
        private TMDBApi mTMDBApi;

        private String mQuery;
        private int mGenreId, mPageNumber;
        private boolean isRequestCancel;

        private RetrieveMoviesRunnable(String query, int genre_id, int page) {
            mRetrofitHelper = TMDBRetrofitHelper.getInstance();
            mTMDBApi = mRetrofitHelper.getTMDBApi();
            mQuery = query;
            mGenreId = genre_id;
            mPageNumber = page;
            isRequestCancel = false;
        }

        @Override
        public void run() {

            getMovieList(mQuery, mGenreId, mPageNumber).enqueue(new Callback<MovieSearchResponse>() {
                @Override
                public void onResponse(Call<MovieSearchResponse> call, Response<MovieSearchResponse> response) {

                    try {
                        if (!response.isSuccessful())
                            throw new IOException("Movie response error: " + response.errorBody().string());

                        // Gets movie list from a response.
                        // Removes any movie that has no rating.
                        List<MovieModel> movieList = new ArrayList<>( ( (MovieSearchResponse) response.body()).getMovieList() );
                        movieList.removeIf(movie -> movie.getVote_average() <= 0.0f);

                        if (movieList.isEmpty() || movieList == null)
                            throw new IOException("Movie response error: request empty");

                        if (mPageNumber == 1) {
                            mMovies.postValue(movieList);
                        } else {
                            List<MovieModel> currentMovies = mMovies.getValue();
                            currentMovies.addAll(movieList);
                            mMovies.postValue(currentMovies);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("MOVIE_REQUEST_ERROR", e.getMessage());
                        mMovies.postValue(null);
                    }
                }

                @Override
                public void onFailure(Call<MovieSearchResponse> call, Throwable t) {
                    Log.e("MOVIE_REQUEST_ERROR", t.getMessage());
                    mMovies.postValue(null);
                }
            });
        }


        private Call<MovieSearchResponse> getMovieList(String query, int genre_id, int page) {
            Call<MovieSearchResponse> responseCall;

            if (genre_id <= 0) {
                responseCall = mTMDBApi.searchMovieByQuery(
                        AppCredentials.TMDB_API_KEY,
                        query,
                        page
                );
            } else {
                responseCall = mTMDBApi.searchMoviesByGenre(
                        AppCredentials.TMDB_API_KEY,
                        genre_id,
                        page
                );
            }
            return responseCall;
        }

        private void cancelRequest() {
            Log.i("MOVIE_REQUEST", "Cancelling search request");
            isRequestCancel = true;
        }

    }

}
