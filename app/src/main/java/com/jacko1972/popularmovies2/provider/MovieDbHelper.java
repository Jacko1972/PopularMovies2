package com.jacko1972.popularmovies2.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.jacko1972.popularmovies2.provider.MovieContract.MovieEntry;
import com.jacko1972.popularmovies2.provider.MovieContract.ReviewEntry;
import com.jacko1972.popularmovies2.provider.MovieContract.TrailerEntry;


class MovieDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "movies.db";

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_POPULAR_MOVIE_TABLE = "CREATE TABLE " + MovieEntry.TABLE_NAME + " (" +
                MovieEntry._ID + " INTEGER NOT NULL, " +
                MovieEntry.COL_BACKDROP_PATH + " TEXT NOT NULL, " +
                MovieEntry.COL_IS_FAVORITE + " TEXT NOT NULL DEFAULT 'no', " +
                MovieEntry.COL_OVERVIEW + " TEXT NOT NULL, " +
                MovieEntry.COL_RELEASE_DATE + " TEXT NOT NULL, " +
                MovieEntry.COL_POSTER_PATH + " TEXT NOT NULL, " +
                MovieEntry.COL_TITLE + " TEXT NOT NULL, " +
                MovieEntry.COL_VOTE_AVERAGE + " REAL NOT NULL, " +
                MovieEntry.COL_VOTE_COUNT + " INTEGER NOT NULL, " +
                MovieEntry.COL_CATEGORY + " TEXT NOT NULL);";
        db.execSQL(SQL_CREATE_POPULAR_MOVIE_TABLE);


        final String SQL_CREATE_TRAILER_TABLE = "CREATE TABLE " + TrailerEntry.TABLE_NAME + " (" +
                TrailerEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TrailerEntry.COL_TRAILER_ID + " INTEGER NOT NULL, " +
                TrailerEntry.COL_TRAILER_ISO_639_1 + " TEXT NOT NULL, " +
                TrailerEntry.COL_TRAILER_ISO_3166_1 + " TEXT NOT NULL, " +
                TrailerEntry.COL_TRAILER_KEY + " TEXT NOT NULL, " +
                TrailerEntry.COL_TRAILER_NAME + " TEXT NOT NULL, " +
                TrailerEntry.COL_TRAILER_SITE + " TEXT NOT NULL, " +
                TrailerEntry.COL_TRAILER_SIZE + " INTEGER NOT NULL, " +
                TrailerEntry.COL_TRAILER_TYPE + " TEXT NOT NULL, " +
                TrailerEntry.COL_TRAILER_MOVIE_ID + " TEXT NOT NULL);";
        db.execSQL(SQL_CREATE_TRAILER_TABLE);

        final String SQL_CREATE_REVIEW_TABLE = "CREATE TABLE " + ReviewEntry.TABLE_NAME + " (" +
                ReviewEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ReviewEntry.COL_REVIEW_ID + " INTEGER NOT NULL, " +
                ReviewEntry.COL_REVIEW_AUTHOR + " TEXT NOT NULL, " +
                ReviewEntry.COL_REVIEW_CONTENT + " TEXT NOT NULL, " +
                ReviewEntry.COL_REVIEW_URL + " TEXT NOT NULL, " +
                ReviewEntry.COL_REVIEW_MOVIE_ID + " TEXT NOT NULL);";
        db.execSQL(SQL_CREATE_REVIEW_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TrailerEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ReviewEntry.TABLE_NAME);
        onCreate(db);
    }
}
