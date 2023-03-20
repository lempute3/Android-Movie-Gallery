package com.example.tmdb.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.app.utils.AppCredentials;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MovieModel implements Parcelable {

    private String title, release_date, overview, poster_path, backdrop_path;
    private List<String> genre_ids;

    private float vote_average;
    private int id;

    public MovieModel() {}

    protected MovieModel(Parcel in) {
        this.title = in.readString();
        this.release_date = in.readString();
        this.overview = in.readString();
        this.poster_path = in.readString();
        this.backdrop_path = in.readString();
        this.genre_ids = in.createStringArrayList();
        this.vote_average = in.readFloat();
        this.id = in.readInt();
    }



    public String getRelease_date() {
        if (this.release_date == null || this.release_date.isEmpty()) return "";
        return this.release_date.substring(0, 4);
    }

    public String getTitle()            { return title; }
    public String getOverview()         { return overview; }
    public String getPoster_path()      { return String.format("%s%s", AppCredentials.TMDB_IMAGE_URL, this.poster_path); }
    public String getBackdrop_path()    { return backdrop_path; }
    public List<String> getGenre_ids()  { return genre_ids; }
    public float getVote_average()      { return vote_average; }
    public int getId()                  { return id; }


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
        parcel.writeStringList(genre_ids);
        parcel.writeFloat(vote_average);
        parcel.writeInt(id);
    }
}
