package com.example.tmdb;

import com.example.app.tmdb.response.MovieSearchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TMDBApi {

    // Interface call for query movie search.
    // URL example: 'https://api.themoviedb.org/3/search/movie?api_key={API_KEY}&query={SEARCH_QUERY}&&page={PAGE_NUM}
    @GET("/3/search/movie?")
    Call<MovieSearchResponse> searchMovieByQuery (
            @Query("api_key")   String apiKey,
            @Query("query")     String searchQuery,
            @Query("page")      int    pageNumber
    );

    // Interface call for id movie search.
    // URL example: 'https://api.themoviedb.org/3/movie/{MOVIE_ID}?api_key={API_KEY}
    @GET("/3/search/{movie_id}?")
    Call<MovieSearchResponse> getMovieByID (
            @Path("movie_id") int movie_id,
            @Query("api_key") String apiKey
    );

}
