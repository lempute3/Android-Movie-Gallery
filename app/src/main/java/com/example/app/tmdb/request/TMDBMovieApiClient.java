package com.example.app.tmdb.request;


import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.app.tmdb.response.MovieSearchResponse;
import com.example.app.viewmodels.AppExecutors;
import com.example.tmdb.TMDBApi;
import com.example.app.utils.AppCredentials;
import com.example.tmdb.models.MovieModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TMDBMovieApiClient {

    private static TMDBMovieApiClient instance;

    private AppExecutors mAppExecutors;
    private RetrieveMoviesRunnable mRetrieveMoviesRunnable;

    // LiveData.
    private MutableLiveData<List<MovieModel>> mMovies;

    private TMDBMovieApiClient() {
        mMovies = new MutableLiveData<>();
        mAppExecutors = AppExecutors.getInstance();
    }

    public static TMDBMovieApiClient getInstance() {
        if (instance == null) {
            instance = new TMDBMovieApiClient();
        }
        return instance;
    }

    public void searchMoviesApi(String query, int page)
    /* Retrieves query search movie data from tmdb movie api */
    {
        final int CANCEL_TIMEOUT_MILLIS = 3000;

        if (mRetrieveMoviesRunnable != null) { mRetrieveMoviesRunnable = null; }
        mRetrieveMoviesRunnable = new RetrieveMoviesRunnable(query, page);

        // Executor for.
        final Future taskHandler = mAppExecutors.getNetworkIO().submit(mRetrieveMoviesRunnable);

        // Scheduler for cancelling api call.
        // Interrupts and cancels task handler.
        mAppExecutors.getNetworkIO().schedule(() -> {
            taskHandler.cancel(true);
        }, CANCEL_TIMEOUT_MILLIS, TimeUnit.MICROSECONDS);
    }

    public MutableLiveData<List<MovieModel>> getMovies() {
        return mMovies;
    }

    private class RetrieveMoviesRunnable implements Runnable {

        private RetrofitHelper mRetrofitHelper;
        private TMDBApi mTMDBApi;

        private String mQuery;
        private int mPageNumber;
        private boolean isRequestCancel;

        RetrieveMoviesRunnable(String query, int page) {
            mRetrofitHelper = RetrofitHelper.getInstance();
            mTMDBApi = mRetrofitHelper.getTMDBApi();

            mQuery = query;
            mPageNumber = page;
            isRequestCancel = false;
        }

        @Override
        public void run() {

            getMovieList(mQuery, mPageNumber).enqueue(new Callback<MovieSearchResponse>() {
                @Override
                public void onResponse(Call<MovieSearchResponse> call, Response<MovieSearchResponse> response) {

                    try {
                        if (isRequestCancel)
                            throw new IOException("Response error: request was canceled");

                        if (!response.isSuccessful())
                            throw new IOException("Response error: " + response.errorBody().string());

                        // Gets movie list from a response.
                        // Removes any movie that has no rating.
                        List<MovieModel> movieList = new ArrayList<>( ( (MovieSearchResponse) response.body()).getMovieList() );
                        movieList.removeIf(movie -> movie.getVoteAverage() <= 0.0f);

                        if (movieList.isEmpty() || movieList == null)
                            throw new IOException("Response error: request empty");

                        // Sends requested data to LiveData.
                        if (mPageNumber == 1) {
                            Log.e("TAG", movieList.get(1).getTitle());
                            mMovies.postValue(movieList);
                        } else {
                            List<MovieModel> currentMovies = mMovies.getValue();
                            currentMovies.addAll(movieList);
                            mMovies.postValue(currentMovies);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("REQUEST_ERROR", e.getMessage());
                        mMovies.postValue(null);
                    }
                }

                @Override
                public void onFailure(Call<MovieSearchResponse> call, Throwable t) {
                    Log.e("TAG", t.getMessage());
                    mMovies.postValue(null);
                }
            });
        }


        private Call<MovieSearchResponse> getMovieList(String query, int page) {
            Call<MovieSearchResponse> responseCall = mTMDBApi.searchMovieByQuery(
                    AppCredentials.TMDB_API_KEY,
                    query,
                    page
            );
            return responseCall;
        }

        private void cancelRequest() {
            Log.i("TAG: ", "Cancelling search request");
            isRequestCancel = true;
        }

    }

}
