package com.jacko1972.popularmovies2.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.jacko1972.popularmovies2.BuildConfig;
import com.jacko1972.popularmovies2.R;
import com.jacko1972.popularmovies2.model.MovieInfo;
import com.jacko1972.popularmovies2.provider.MovieContract;
import com.jacko1972.popularmovies2.service.FullMovieJsonResponse;
import com.jacko1972.popularmovies2.service.MovieDbInterface;
import com.jacko1972.popularmovies2.service.MovieDbService;
import com.jacko1972.popularmovies2.util.Utilities;

import java.util.List;
import java.util.Vector;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PopularMovies2SyncAdapter extends AbstractThreadedSyncAdapter {
    private final String TAG = PopularMovies2SyncAdapter.class.getSimpleName();
    private static final int SYNC_INTERVAL = 60 * 180;
    private static final int SYNC_FLEXTIME = SYNC_INTERVAL / 3;

    public PopularMovies2SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.d(TAG, "Starting sync");
        getContext().getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI, MovieContract.MovieEntry.COL_IS_FAVORITE + " = ?", new String[]{"no"});

        MovieDbInterface dbPopularInterface = MovieDbService.getClient().create(MovieDbInterface.class);
        final Call<FullMovieJsonResponse> makePopularCall = dbPopularInterface.getMovieByListType("popular", BuildConfig.MOVIES_API_KEY);
        makePopularCall.enqueue(new Callback<FullMovieJsonResponse>() {
            @Override
            public void onResponse(Call<FullMovieJsonResponse> call, Response<FullMovieJsonResponse> response) {
                if (response.isSuccessful()) {
                    List<MovieInfo> movieInfoList = response.body().getResults();
                    Vector<ContentValues> cVVector = new Vector<>(movieInfoList.size());
                    ContentValues cv = new ContentValues();
                    for (MovieInfo movieInfo : movieInfoList) {
                        cv = movieInfo.movieToContentValues();
                        cv.put("category", "popular");
                        if (Utilities.isMovieFavorite(getContext(), movieInfo.getId())) {
                            getContext().getContentResolver().delete(MovieContract.MovieEntry.buildMovieUri(movieInfo.getId()), null, null);
                            cv.put("is_favorite", "yes");
                        } else {
                            cv.put("is_favorite", "no");
                        }
                        cVVector.add(cv);
                    }
                    if (cVVector.size() > 0) {
                        ContentValues[] cvArray = new ContentValues[cVVector.size()];
                        cVVector.toArray(cvArray);
                        getContext().getContentResolver().bulkInsert(MovieContract.MovieEntry.CONTENT_URI, cvArray);
                    }
                }
            }

            @Override
            public void onFailure(Call<FullMovieJsonResponse> call, Throwable t) {
                Log.d(TAG, "call: " + call);
                Log.d(TAG, "throws: " + t.toString());
            }
        });

        MovieDbInterface dbTopRatedInterface = MovieDbService.getClient().create(MovieDbInterface.class);
        final Call<FullMovieJsonResponse> makeTopRatedCall = dbTopRatedInterface.getMovieByListType("top_rated", BuildConfig.MOVIES_API_KEY);
        makeTopRatedCall.enqueue(new Callback<FullMovieJsonResponse>() {
            @Override
            public void onResponse(Call<FullMovieJsonResponse> call, Response<FullMovieJsonResponse> response) {
                if (response.isSuccessful()) {
                    List<MovieInfo> movieInfoList = response.body().getResults();
                    Vector<ContentValues> cVVector = new Vector<>(movieInfoList.size());
                    ContentValues cv = new ContentValues();
                    for (MovieInfo movieInfo : movieInfoList) {
                        cv = movieInfo.movieToContentValues();
                        cv.put("category", "top_rated");
                        if (Utilities.isMovieFavorite(getContext(), movieInfo.getId())) {
                            getContext().getContentResolver().delete(MovieContract.MovieEntry.buildMovieUri(movieInfo.getId()), null, null);
                            cv.put("is_favorite", "yes");
                        } else {
                            cv.put("is_favorite", "no");
                        }
                        cVVector.add(cv);
                    }
                    if (cVVector.size() > 0) {
                        ContentValues[] cvArray = new ContentValues[cVVector.size()];
                        cVVector.toArray(cvArray);
                        getContext().getContentResolver().bulkInsert(MovieContract.MovieEntry.CONTENT_URI, cvArray);
                    }
                }
            }

            @Override
            public void onFailure(Call<FullMovieJsonResponse> call, Throwable t) {
                Log.d(TAG, "call: " + call);
                Log.d(TAG, "throws: " + t.toString());
            }
        });


    }

    private static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // we can enable inexact timers in our periodic sync
            SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic(syncInterval, flexTime).
                    setSyncAdapter(account, authority).
                    setExtras(new Bundle()).build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account,
                    authority, new Bundle(), syncInterval);
        }
    }

    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.content_authority), bundle);
    }

    private static Account getSyncAccount(Context context) {
        // Get an instance of the Android account manager
        AccountManager accountManager = (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        Account newAccount = new Account(context.getString(R.string.app_name), context.getString(R.string.sync_account_type));
        if (null == accountManager.getPassword(newAccount)) {
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            onAccountCreated(newAccount, context);
        }
        return newAccount;
    }

    private static void onAccountCreated(Account newAccount, Context context) {
        PopularMovies2SyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);
        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);
        syncImmediately(context);
    }

    public static void initializeSyncAdapter(Context context) {
        getSyncAccount(context);
    }
}