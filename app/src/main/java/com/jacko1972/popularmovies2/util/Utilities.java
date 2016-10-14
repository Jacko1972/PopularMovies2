package com.jacko1972.popularmovies2.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.preference.PreferenceManager;

import com.jacko1972.popularmovies2.R;
import com.jacko1972.popularmovies2.provider.MovieContract;

import java.util.Calendar;
import java.util.Locale;

public class Utilities {

    //private static final String TAG = "Utilities";

    public static final String imageAddress500w = "http://image.tmdb.org/t/p/w500";

    public static boolean internetAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    public static String getPreferredDisplayMode(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(context.getString(R.string.prefs_movie_list), context.getString(R.string.prefs_movie_list_default_value));
    }

    public static boolean isMovieFavorite(Context context, long id) {
        Cursor cursor = context.getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI,
                null,
                MovieContract.MovieEntry._ID + " = ? AND " + MovieContract.MovieEntry.COL_IS_FAVORITE + " = ?",
                new String[]{String.valueOf(id), "yes"},
                null
        );
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.close();
                return true;
            } else {
                cursor.close();
                return false;
            }
        } else {
            return false;
        }
    }

    public static String getReleaseDate(String release) {
        try {
            String[] dateSplit = release.split("-");
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.MONTH, Integer.parseInt(dateSplit[1]));
            return calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.ENGLISH) + " " + dateSplit[0];
        } catch (Exception e) {
            return "";
        }
    }
}
