package com.example.tmdb.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import lombok.Getter;

@Getter
public class MovieGenreModel implements Parcelable {

    private final String name;
    private final int id;

    public MovieGenreModel(String name, int id) {
        this.name = name;
        this.id = id;
    }

    protected MovieGenreModel(Parcel in) {
        this.name = in.readString();
        this.id = in.readInt();
    }

    public static final Creator<MovieGenreModel> CREATOR = new Creator<MovieGenreModel>() {
        @Override
        public MovieGenreModel createFromParcel(Parcel in) {
            return new MovieGenreModel(in);
        }

        @Override
        public MovieGenreModel[] newArray(int size) {
            return new MovieGenreModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeInt(id);
    }
}
