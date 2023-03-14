package com.example.app.tmdb.request;

import com.example.tmdb.TMDBApi;
import com.example.app.utils.AppCredentials;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TMDBRetrofitHelper {

    private static TMDBRetrofitHelper instance;

    private Retrofit.Builder mRetrofitBuilder;
    private Retrofit mRetrofit;
    private TMDBApi mTMDbApi;

    private TMDBRetrofitHelper() {
        mRetrofitBuilder = new Retrofit.Builder()
                .baseUrl(AppCredentials.TMDB_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());

        mRetrofit = mRetrofitBuilder.build();
        mTMDbApi = mRetrofit.create(TMDBApi.class);
    }

    public static TMDBRetrofitHelper getInstance() {
        if (instance == null) {
            instance = new TMDBRetrofitHelper();
        }
        return instance;
    }

    public TMDBApi getTMDBApi() { return mTMDbApi; }

    /*public void getMovieList(String query, int page, final OnResponseCompletionListener listener) {
        TMDBApi tmdbApi = mTMDbApi;
        Call<MovieSearchResponse> responseCall = tmdbApi
                .searchMovieByQuery(TMDBCredentials.TMDB_API_KEY, query, page);

        responseCall.enqueue(new Callback<MovieSearchResponse>() {
            @Override
            public void onResponse(Call<MovieSearchResponse> call, Response<MovieSearchResponse> response) {
                if (response.isSuccessful()) {
                    List<MovieModel> movies = new ArrayList<>(response.body().getMovieList());
                    listener.onResponseSuccess(movies);
                }
            }

            @Override
            public void onFailure(Call<MovieSearchResponse> call, Throwable t) {
                listener.onResponseFailure(t.getMessage());
            }
        });
    }*/

}
