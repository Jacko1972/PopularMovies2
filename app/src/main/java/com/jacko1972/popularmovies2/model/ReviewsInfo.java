package com.jacko1972.popularmovies2.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.jacko1972.popularmovies2.provider.MovieContract;

import java.util.ArrayList;

public class ReviewsInfo implements Parcelable {

    @SerializedName("id")
    private String id;
    @SerializedName("author")
    private String author;
    @SerializedName("content")
    private String content;
    @SerializedName("url")
    private String url;
    @SerializedName("movie_id")
    private String movie_id;

    private ReviewsInfo() {
    }

    private ReviewsInfo(Parcel in) {
        id = in.readString();
        author = in.readString();
        content = in.readString();
        url = in.readString();
        movie_id = in.readString();
    }

    public static final Creator<ReviewsInfo> CREATOR = new Creator<ReviewsInfo>() {
        @Override
        public ReviewsInfo createFromParcel(Parcel in) {
            return new ReviewsInfo(in);
        }

        @Override
        public ReviewsInfo[] newArray(int size) {
            return new ReviewsInfo[size];
        }
    };

    public String getMovie_id() {
        return movie_id;
    }

    private void setMovie_id(String movie_id) {
        this.movie_id = movie_id;
    }

    public String getAuthor() {
        return author;
    }

    private void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    private void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    private void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    private void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(author);
        dest.writeString(content);
        dest.writeString(url);
        dest.writeString(movie_id);
    }

    public static ReviewsInfo reviewFromFromCursor(Cursor cursor) {
        if (cursor != null && cursor.getCount() > 0) {
            ReviewsInfo review = new ReviewsInfo();
            review.setId(cursor.getString(cursor.getColumnIndex(MovieContract.ReviewEntry.COL_REVIEW_ID)));
            review.setAuthor(cursor.getString(cursor.getColumnIndex(MovieContract.ReviewEntry.COL_REVIEW_AUTHOR)));
            review.setContent(cursor.getString(cursor.getColumnIndex(MovieContract.ReviewEntry.COL_REVIEW_CONTENT)));
            review.setUrl(cursor.getString(cursor.getColumnIndex(MovieContract.ReviewEntry.COL_REVIEW_URL)));
            review.setMovie_id(cursor.getString(cursor.getColumnIndex(MovieContract.ReviewEntry.COL_REVIEW_MOVIE_ID)));
            return review;
        } else {
            return null;
        }
    }

    public static ArrayList<ReviewsInfo> reviewsFromFromCursor(Cursor cursor) {
        ArrayList<ReviewsInfo> reviews = new ArrayList<>();
        if (cursor != null && cursor.getCount() > 0) {
            do {
                ReviewsInfo review = new ReviewsInfo();
                review.setId(cursor.getString(cursor.getColumnIndex(MovieContract.ReviewEntry.COL_REVIEW_ID)));
                review.setAuthor(cursor.getString(cursor.getColumnIndex(MovieContract.ReviewEntry.COL_REVIEW_AUTHOR)));
                review.setContent(cursor.getString(cursor.getColumnIndex(MovieContract.ReviewEntry.COL_REVIEW_CONTENT)));
                review.setUrl(cursor.getString(cursor.getColumnIndex(MovieContract.ReviewEntry.COL_REVIEW_URL)));
                review.setMovie_id(cursor.getString(cursor.getColumnIndex(MovieContract.ReviewEntry.COL_REVIEW_MOVIE_ID)));
                reviews.add(review);
            } while (cursor.moveToNext());
        }
        return reviews;
    }
    public ContentValues reviewToContentValues() {
        ContentValues values = new ContentValues();
        values.put(MovieContract.ReviewEntry.COL_REVIEW_ID, id);
        values.put(MovieContract.ReviewEntry.COL_REVIEW_AUTHOR, author);
        values.put(MovieContract.ReviewEntry.COL_REVIEW_CONTENT, content);
        values.put(MovieContract.ReviewEntry.COL_REVIEW_MOVIE_ID, movie_id);
        values.put(MovieContract.ReviewEntry.COL_REVIEW_URL, url);
        return values;
    }
}
