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
}
