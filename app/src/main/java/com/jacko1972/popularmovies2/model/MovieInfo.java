package com.jacko1972.popularmovies2.model;


import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.jacko1972.popularmovies2.provider.MovieContract;

import java.util.ArrayList;

public class MovieInfo implements Parcelable {

    //private static final String TAG = "MovieInfo";

    @SerializedName("adult")
    private String adult;
    @SerializedName("backdrop_path")
    private String backdrop_path;
    @SerializedName("id")
    private long id;
    @SerializedName("original_language")
    private String original_language;
    @SerializedName("original_title")
    private String original_title;
    @SerializedName("overview")
    private String overview;
    @SerializedName("release_date")
    private String release_date;
    @SerializedName("poster_path")
    private String poster_path;
    @SerializedName("popularity")
    private String popularity;
    @SerializedName("title")
    private String title;
    @SerializedName("video")
    private String video;
    @SerializedName("vote_average")
    private double vote_average;
    @SerializedName("vote_count")
    private long vote_count;
    @SerializedName("category")
    private String category;
    @SerializedName("is_favorite")
    private String is_favorite;

    private MovieInfo(Parcel in) {
        adult = in.readString();
        backdrop_path = in.readString();
        id = in.readLong();
        original_language = in.readString();
        original_title = in.readString();
        overview = in.readString();
        release_date = in.readString();
        poster_path = in.readString();
        popularity = in.readString();
        title = in.readString();
        video = in.readString();
        vote_average = in.readDouble();
        vote_count = in.readLong();
        category = in.readString();
        is_favorite = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(adult);
        dest.writeString(backdrop_path);
        dest.writeLong(id);
        dest.writeString(original_language);
        dest.writeString(original_title);
        dest.writeString(overview);
        dest.writeString(release_date);
        dest.writeString(poster_path);
        dest.writeString(popularity);
        dest.writeString(title);
        dest.writeString(video);
        dest.writeDouble(vote_average);
        dest.writeLong(vote_count);
        //dest.writeList(genre_ids);
        dest.writeString(category);
        dest.writeString(is_favorite);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MovieInfo> CREATOR = new Creator<MovieInfo>() {
        @Override
        public MovieInfo createFromParcel(Parcel in) {
            return new MovieInfo(in);
        }

        @Override
        public MovieInfo[] newArray(int size) {
            return new MovieInfo[size];
        }
    };

    public String getAdult() {
        return adult;
    }

    public void setAdult(String adult) {
        this.adult = adult;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public void setOriginal_language(String original_language) {
        this.original_language = original_language;
    }

    public String getPopularity() {
        return popularity;
    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }

    public String getTitle() {
        return title;
    }

    private void setTitle(String title) {
        this.title = title;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public long getVote_count() {
        return vote_count;
    }

    private void setVote_count(long vote_count) {
        this.vote_count = vote_count;
    }

    private MovieInfo() {
    }

    public long getId() {
        return id;
    }

    private void setId(long id) {
        this.id = id;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    private void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getOverview() {
        return overview;
    }

    private void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPoster_path() {
        return poster_path;
    }

    private void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getRelease_date() {
        return release_date;
    }

    private void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public double getVote_average() {
        return vote_average;
    }

    private void setVote_average(double vote_average) {
        this.vote_average = vote_average;
    }

    public String getCategory() {
        return category;
    }

    private void setCategory(String category) {
        this.category = category;
    }

    public String getIs_favorite() {
        return is_favorite;
    }

    private void setIs_favorite(String is_favorite) {
        this.is_favorite = is_favorite;
    }

    public ContentValues movieToContentValues() {
        ContentValues values = new ContentValues();
        values.put(MovieContract.MovieEntry._ID, id);
        values.put(MovieContract.MovieEntry.COL_OVERVIEW, overview);
        values.put(MovieContract.MovieEntry.COL_RELEASE_DATE, release_date);
        values.put(MovieContract.MovieEntry.COL_POSTER_PATH, poster_path);
        values.put(MovieContract.MovieEntry.COL_TITLE, title);
        values.put(MovieContract.MovieEntry.COL_VOTE_AVERAGE, vote_average);
        values.put(MovieContract.MovieEntry.COL_VOTE_COUNT, vote_count);
        values.put(MovieContract.MovieEntry.COL_BACKDROP_PATH, backdrop_path);
        values.put(MovieContract.MovieEntry.COL_CATEGORY, category);
        values.put(MovieContract.MovieEntry.COL_IS_FAVORITE, is_favorite);
        return values;
    }

    public static MovieInfo movieFromCursor(Cursor cursor) {
        if (cursor != null && cursor.getCount() > 0) {
            MovieInfo movie = new MovieInfo();
            movie.setId(cursor.getLong(cursor.getColumnIndex(MovieContract.MovieEntry._ID)));
            movie.setTitle(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COL_TITLE)));
            movie.setOverview(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COL_OVERVIEW)));
            movie.setRelease_date(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COL_RELEASE_DATE)));
            movie.setPoster_path(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COL_POSTER_PATH)));
            movie.setVote_average(cursor.getDouble(cursor.getColumnIndex(MovieContract.MovieEntry.COL_VOTE_AVERAGE)));
            movie.setVote_count(cursor.getLong(cursor.getColumnIndex(MovieContract.MovieEntry.COL_VOTE_COUNT)));
            movie.setBackdrop_path(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COL_BACKDROP_PATH)));
            movie.setIs_favorite(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COL_IS_FAVORITE)));
            movie.setCategory(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COL_CATEGORY)));
            return movie;
        } else {
            return null;
        }
    }
}