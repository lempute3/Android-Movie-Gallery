package com.example.tmdb.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.app.utils.AppCredentials;

public class MovieModel implements Parcelable {

    private final String title, release_date, overview, poster_path, backdrop_path;
    private final String [] genre_ids;

    private final float vote_average;
    private final int id;

    public MovieModel (
            String title,
            String releaseDate,
            String movieOverview,
            String posterPath,
            String backdrop_path,
            String[] genreIds,
            float voteAverage,
            int movieId
    ) {
        this.title = title;
        this.release_date = releaseDate;
        this.overview = movieOverview;
        this.poster_path = posterPath;
        this.backdrop_path = backdrop_path;
        this.genre_ids = genreIds;
        this.id = movieId;
        this.vote_average = voteAverage;
    }

    protected MovieModel(Parcel in) {
        this.title = in.readString();
        this.release_date = in.readString();
        this.overview = in.readString();
        this.poster_path = in.readString();
        this.backdrop_path = in.readString();
        this.genre_ids = in.createStringArray();
        this.vote_average = in.readFloat();
        this.id = in.readInt();
    }

    public String   getTitle()           { return this.title; }
    public String   getReleaseDate()     { return this.release_date; }
    public String   getMovieOverview()   { return this.overview; }
    public String   getBackdropPath()    { return this.backdrop_path; }
    public String[] getGenreIds()        { return this.genre_ids; }
    public float    getVoteAverage()     { return this.vote_average; }
    public int      getMovieId()         { return this.id; }
    public String   getPosterPath()      { return String.format("%s%s", AppCredentials.TMDB_IMAGE_URL, this.poster_path); }

    public static final Creator<MovieModel> CREATOR = new Creator<MovieModel>() {
        @Override
        public MovieModel createFromParcel(Parcel in) {
            return new MovieModel(in);
        }

        @Override
        public MovieModel[] newArray(int size) {
            return new MovieModel[size];
        }
    };
    
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(this.title);
        parcel.writeString(release_date);
        parcel.writeString(overview);
        parcel.writeString(poster_path);
        parcel.writeStringArray(genre_ids);
        parcel.writeFloat(vote_average);
        parcel.writeInt(id);
    }
}
