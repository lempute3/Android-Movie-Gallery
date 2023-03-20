package com.example.app.firebase;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FirebaseMovieModel {

    private String backdropPath, movieOverview, posterPath, releaseYear, title, voteAverageFloat;
    private List<String> genreIds;
    int movieId;

}
