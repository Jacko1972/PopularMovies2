package com.jacko1972.popularmovies2.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.jacko1972.popularmovies2.provider.MovieContract;

import java.util.ArrayList;

public class TrailersInfo implements Parcelable {

    @SerializedName("id")
    private String id;
    @SerializedName("iso_639_1")
    private String iso_639_1;
    @SerializedName("iso_3166_1")
    private String iso_3166_1;
    @SerializedName("key")
    private String key;
    @SerializedName("name")
    private String name;
    @SerializedName("site")
    private String site;
    @SerializedName("size")
    private int size;
    @SerializedName("type")
    private String type;
    @SerializedName("movie_id")
    private String movie_id;

    private TrailersInfo() {

    }

    private TrailersInfo(Parcel in) {
        id = in.readString();
        iso_639_1 = in.readString();
        iso_3166_1 = in.readString();
        key = in.readString();
        name = in.readString();
        site = in.readString();
        size = in.readInt();
        type = in.readString();
        movie_id = in.readString();
    }

    public static final Creator<TrailersInfo> CREATOR = new Creator<TrailersInfo>() {
        @Override
        public TrailersInfo createFromParcel(Parcel in) {
            return new TrailersInfo(in);
        }

        @Override
        public TrailersInfo[] newArray(int size) {
            return new TrailersInfo[size];
        }
    };

    public String getMovie_id() {
        return movie_id;
    }

    private void setMovie_id(String movie_id) {
        this.movie_id = movie_id;
    }

    public String getId() {
        return id;
    }

    private void setId(String id) {
        this.id = id;
    }

    public String getIso_639_1() {
        return iso_639_1;
    }

    private void setIso_639_1(String iso_639_1) {
        this.iso_639_1 = iso_639_1;
    }

    public String getKey() {
        return key;
    }

    private void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    public String getSite() {
        return site;
    }

    private void setSite(String site) {
        this.site = site;
    }

    public int getSize() {
        return size;
    }

    private void setSize(int size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    private void setType(String type) {
        this.type = type;
    }

    public String getIso_3166_1() {
        return iso_3166_1;
    }

    private void setIso_3166_1(String iso_3166_1) {
        this.iso_3166_1 = iso_3166_1;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(iso_639_1);
        dest.writeString(iso_3166_1);
        dest.writeString(key);
        dest.writeString(name);
        dest.writeString(site);
        dest.writeInt(size);
        dest.writeString(type);
        dest.writeString(movie_id);
    }

    public ContentValues trailerToContentValues() {
        ContentValues values = new ContentValues();
        values.put(MovieContract.TrailerEntry.COL_TRAILER_ID, id);
        values.put(MovieContract.TrailerEntry.COL_TRAILER_ISO_639_1, iso_639_1);
        values.put(MovieContract.TrailerEntry.COL_TRAILER_ISO_3166_1, iso_3166_1);
        values.put(MovieContract.TrailerEntry.COL_TRAILER_KEY, key);
        values.put(MovieContract.TrailerEntry.COL_TRAILER_MOVIE_ID, movie_id);
        values.put(MovieContract.TrailerEntry.COL_TRAILER_NAME, name);
        values.put(MovieContract.TrailerEntry.COL_TRAILER_SITE, site);
        values.put(MovieContract.TrailerEntry.COL_TRAILER_SIZE, size);
        values.put(MovieContract.TrailerEntry.COL_TRAILER_TYPE, type);
        return values;
    }

    public static ArrayList<TrailersInfo> trailersFromFromCursor(Cursor cursor) {
        ArrayList<TrailersInfo> trailers = new ArrayList<>();
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                TrailersInfo trailer = new TrailersInfo();
                trailer.setId(cursor.getString(cursor.getColumnIndex(MovieContract.TrailerEntry.COL_TRAILER_ID)));
                trailer.setIso_639_1(cursor.getString(cursor.getColumnIndex(MovieContract.TrailerEntry.COL_TRAILER_ISO_639_1)));
                trailer.setIso_3166_1(cursor.getString(cursor.getColumnIndex(MovieContract.TrailerEntry.COL_TRAILER_ISO_3166_1)));
                trailer.setKey(cursor.getString(cursor.getColumnIndex(MovieContract.TrailerEntry.COL_TRAILER_KEY)));
                trailer.setMovie_id(cursor.getString(cursor.getColumnIndex(MovieContract.TrailerEntry.COL_TRAILER_MOVIE_ID)));
                trailer.setName(cursor.getString(cursor.getColumnIndex(MovieContract.TrailerEntry.COL_TRAILER_NAME)));
                trailer.setSite(cursor.getString(cursor.getColumnIndex(MovieContract.TrailerEntry.COL_TRAILER_SITE)));
                trailer.setSize(cursor.getInt(cursor.getColumnIndex(MovieContract.TrailerEntry.COL_TRAILER_SIZE)));
                trailer.setType(cursor.getString(cursor.getColumnIndex(MovieContract.TrailerEntry.COL_TRAILER_TYPE)));
                trailers.add(trailer);
            } while (cursor.moveToNext());
        }
        return trailers;
    }

    public static TrailersInfo trailerFromFromCursor(Cursor cursor) {
        if (cursor != null && cursor.getCount() > 0) {
            TrailersInfo trailer = new TrailersInfo();
            trailer.setId(cursor.getString(cursor.getColumnIndex(MovieContract.TrailerEntry.COL_TRAILER_ID)));
            trailer.setIso_639_1(cursor.getString(cursor.getColumnIndex(MovieContract.TrailerEntry.COL_TRAILER_ISO_639_1)));
            trailer.setIso_3166_1(cursor.getString(cursor.getColumnIndex(MovieContract.TrailerEntry.COL_TRAILER_ISO_3166_1)));
            trailer.setKey(cursor.getString(cursor.getColumnIndex(MovieContract.TrailerEntry.COL_TRAILER_KEY)));
            trailer.setMovie_id(cursor.getString(cursor.getColumnIndex(MovieContract.TrailerEntry.COL_TRAILER_MOVIE_ID)));
            trailer.setName(cursor.getString(cursor.getColumnIndex(MovieContract.TrailerEntry.COL_TRAILER_NAME)));
            trailer.setSite(cursor.getString(cursor.getColumnIndex(MovieContract.TrailerEntry.COL_TRAILER_SITE)));
            trailer.setSize(cursor.getInt(cursor.getColumnIndex(MovieContract.TrailerEntry.COL_TRAILER_SIZE)));
            trailer.setType(cursor.getString(cursor.getColumnIndex(MovieContract.TrailerEntry.COL_TRAILER_TYPE)));
            return trailer;
        }
        return null;
    }
}
