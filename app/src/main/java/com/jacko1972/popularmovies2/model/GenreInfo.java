package com.jacko1972.popularmovies2.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class GenreInfo implements Parcelable {

    @SerializedName("id")
    private long genre_id;
    @SerializedName("name")
    private String name;
    @SerializedName("movie_id")
    private String movie_id;

    public long getGenre_id() {
        return genre_id;
    }

    public void setGenre_id(long genre_id) {
        this.genre_id = genre_id;
    }

    public String getMovie_id() {
        return movie_id;
    }

    public void setMovie_id(String movie_id) {
        this.movie_id = movie_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private GenreInfo(Parcel in) {
        genre_id = in.readLong();
        name = in.readString();
        movie_id = in.readString();
    }

    public static final Creator<GenreInfo> CREATOR = new Creator<GenreInfo>() {
        @Override
        public GenreInfo createFromParcel(Parcel in) {
            return new GenreInfo(in);
        }

        @Override
        public GenreInfo[] newArray(int size) {
            return new GenreInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(genre_id);
        dest.writeString(name);
        dest.writeString(movie_id);
    }
}
