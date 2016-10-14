package com.jacko1972.popularmovies2;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.jacko1972.popularmovies2.adapter.MovieRecyclerViewAdapter;
import com.jacko1972.popularmovies2.model.MovieInfo;
import com.jacko1972.popularmovies2.provider.MovieContract;
import com.jacko1972.popularmovies2.sync.PopularMovies2SyncAdapter;
import com.jacko1972.popularmovies2.util.RecyclerViewWithEmptyView;
import com.jacko1972.popularmovies2.util.Utilities;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "MainActivity";
    private static final int SETTINGS_REQUEST_CODE = 2;
    private static final int MSG_SHOW_NO_MOVIE_DIALOG = 3;
    private static final int MSG_RELOAD_DETAIL_FRAGMENT = 4;
    private static final int MSG_IMMEDIATE_SYNC = 5;
    private RecyclerViewWithEmptyView recyclerView;
    private MovieRecyclerViewAdapter movieRecyclerViewAdapter;
    private boolean mTwoPane;
    private static final int ACTIVITY_LIST_LOADER_ID = 1;
    private int lastSelectedMoviePosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        if (findViewById(R.id.movie_detail_container) != null) {
            mTwoPane = true;
        }
        if (savedInstanceState == null && mTwoPane) {
            MovieDetailFragment fragment = new MovieDetailFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment, getString(R.string.fragment_detail_tag))
                    .commit();
        } else if (savedInstanceState != null) {
            lastSelectedMoviePosition = savedInstanceState.getInt("lastPosition");
        }

        recyclerView = (RecyclerViewWithEmptyView) findViewById(R.id.movie_list);
        recyclerView.setEmptyView(findViewById(R.id.empty_movie_recycler_view));

        assert recyclerView != null;
        setupRecyclerView(recyclerView);

        if (!Utilities.internetAvailable(this)) {
            Toast.makeText(this, "No Internet Connection at this time. Links will not work and some images may not display.", Toast.LENGTH_LONG).show();
        }
        PopularMovies2SyncAdapter.initializeSyncAdapter(this);
        getSupportLoaderManager().initLoader(ACTIVITY_LIST_LOADER_ID, null, this);
    }

    private void setupRecyclerView(@NonNull RecyclerViewWithEmptyView recyclerView) {
        recyclerView.setLayoutManager(new GridLayoutManager(this, getResources().getInteger(R.integer.movie_image_column_count)));
        movieRecyclerViewAdapter = new MovieRecyclerViewAdapter(this, null, new MovieRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                lastSelectedMoviePosition = position;
                showMovieDetailFromClick();
            }
        });
        recyclerView.setAdapter(movieRecyclerViewAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivityForResult(intent, SETTINGS_REQUEST_CODE);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SETTINGS_REQUEST_CODE) {
            lastSelectedMoviePosition = 0;
            updateSyncedData();
            getSupportLoaderManager().restartLoader(ACTIVITY_LIST_LOADER_ID, null, this);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void updateSyncedData() {
        PopularMovies2SyncAdapter.syncImmediately(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("lastPosition", lastSelectedMoviePosition);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        getSupportLoaderManager().restartLoader(ACTIVITY_LIST_LOADER_ID, null, this);
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        movieRecyclerViewAdapter.swapCursor(null);
        String choice = Utilities.getPreferredDisplayMode(this);
        switch (choice) {
            case "favorite":
                return new CursorLoader(this, MovieContract.MovieEntry.CONTENT_URI, null, MovieContract.MovieEntry.COL_IS_FAVORITE + " = ?", new String[]{"yes"}, null);
            default:
                return new CursorLoader(this, MovieContract.MovieEntry.CONTENT_URI, null, MovieContract.MovieEntry.COL_CATEGORY + " = ?", new String[]{choice}, null);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        movieRecyclerViewAdapter.swapCursor(data);
        if (data.getCount() > 0) {
            handler.sendEmptyMessage(MSG_RELOAD_DETAIL_FRAGMENT);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        movieRecyclerViewAdapter.swapCursor(null);
    }

    private void showMovieDetailFromClick() {
        Cursor cursor = movieRecyclerViewAdapter.getCursor();
        if (cursor != null && cursor.getCount() > 0) {
            if (lastSelectedMoviePosition != -1) {
                cursor.moveToPosition(lastSelectedMoviePosition);
            } else {
                cursor.moveToPosition(0);
            }
            MovieInfo selectedMovie = MovieInfo.movieFromCursor(cursor);
            if (selectedMovie != null) {
                if (mTwoPane) {
                    setTitle(selectedMovie.getTitle());
                    MovieDetailFragment fragment = MovieDetailFragment.newInstance(selectedMovie, mTwoPane);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.movie_detail_container, fragment, getString(R.string.fragment_detail_tag))
                            .commit();
                } else {
                    Intent intent = new Intent(this, MovieDetailActivity.class);
                    intent.putExtra("parcelMovie", selectedMovie);
                    startActivity(intent);
                }
            } else {
                showNoMovieAlertDialog();
            }
        }
    }

    private void showMovieDetail() {
        Cursor cursor = movieRecyclerViewAdapter.getCursor();
        if (cursor != null && cursor.getCount() > 0) {
            if (lastSelectedMoviePosition != -1) {
                cursor.moveToPosition(lastSelectedMoviePosition);
            } else {
                cursor.moveToPosition(0);
            }
            MovieInfo selectedMovie = MovieInfo.movieFromCursor(cursor);
            if (selectedMovie != null) {
                setTitle(selectedMovie.getTitle());
                MovieDetailFragment fragment = MovieDetailFragment.newInstance(selectedMovie, mTwoPane);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container, fragment, getString(R.string.fragment_detail_tag))
                        .commit();
            } else {
                showNoMovieAlertDialog();
            }
        }
    }

    private void showNoMovieAlertDialog() {
        NoMovieAlertDialog noMovieAlertDialog = new NoMovieAlertDialog();
        noMovieAlertDialog.show(getSupportFragmentManager(), "noMovieDialog");
        MovieDetailFragment fragment = (MovieDetailFragment) getSupportFragmentManager().findFragmentByTag(getString(R.string.fragment_detail_tag));
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MSG_RELOAD_DETAIL_FRAGMENT) {
                if (mTwoPane) {
                    showMovieDetail();
                }
            }
        }
    };

    public static class NoMovieAlertDialog extends DialogFragment {

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            boolean internet = Utilities.internetAvailable(getActivity());
            String message = getActivity().getString(R.string.no_movie_alert);
            if (internet) {
                message = message.concat(getActivity().getString(R.string.alert_open_settings));
            } else {
                message = message.concat(getActivity().getString(R.string.alert_no_internet));
            }
            return new AlertDialog.Builder(getActivity())
                    .setTitle("No Movies!")
                    .setMessage(message)
                    .setNegativeButton("Settings", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(getActivity(), SettingsActivity.class);
                            startActivity(intent);
                        }
                    })
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .create();
        }
    }
}
