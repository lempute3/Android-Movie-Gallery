package com.example.tmdb;

import com.example.app.tmdb.response.MovieGenreResponse;
import com.example.app.tmdb.response.MovieResponse;
import com.example.app.tmdb.response.MovieSearchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TMDBApi {

    // Interface call for searching movies by query.
    // URL example: 'https://api.themoviedb.org/3/search/movie?api_key={API_KEY}&query={SEARCH_QUERY}&page={PAGE_NUM}'
    @GET("/3/search/movie?")
    Call<MovieSearchResponse> searchMovieByQuery (
            @Query("api_key")   String apiKey,
            @Query("query")     String searchQuery,
            @Query("page")      int    pageNumber
    );

    // Interface call for searching movie by id.
    // URL example: 'https://api.themoviedb.org/3/movie/{MOVIE_ID}?api_key={API_KEY}'
    @GET("/3/search/{movie_id}?")
    Call<MovieResponse> getMovieByID (
            @Path("movie_id") int movie_id,
            @Query("api_key") String apiKey
    );

    // Interface call for searching movies by genre.
    // URL example: 'https://api.themoviedb.org/3/discover/movie?api_key={API_KEY}&with_genres={GENRE_ID}&page={PAGE_NUM}'
    @GET("/3/discover/movie?")
    Call<MovieSearchResponse> searchMoviesByGenre (
            @Query("api_key")       String apiKey,
            @Query("with_genres")   int genre_id,
            @Query("page")          int pageNumber
    );

    // Interface call for getting list of official movie genres.
    // URL example: 'https://api.themoviedb.org/3/genre/movie/list?api_key={API_KEY}'
    @GET("/3/genre/movie/list?")
    Call<MovieGenreResponse> searchMovieGenres(
            @Query("api_key") String apiKey
    );

}
