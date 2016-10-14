package com.jacko1972.popularmovies2;


import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jacko1972.popularmovies2.adapter.ReviewRecyclerViewAdapter;
import com.jacko1972.popularmovies2.adapter.TrailerRecyclerViewAdapter;
import com.jacko1972.popularmovies2.model.MovieInfo;
import com.jacko1972.popularmovies2.model.ReviewsInfo;
import com.jacko1972.popularmovies2.model.TrailersInfo;
import com.jacko1972.popularmovies2.provider.MovieContract;
import com.jacko1972.popularmovies2.service.MovieDbInterface;
import com.jacko1972.popularmovies2.service.MovieDbService;
import com.jacko1972.popularmovies2.service.ReviewsJsonResponse;
import com.jacko1972.popularmovies2.service.TrailersJsonResponse;
import com.jacko1972.popularmovies2.util.AsyncQueryHandler;
import com.jacko1972.popularmovies2.util.RecyclerViewWithEmptyView;
import com.jacko1972.popularmovies2.util.Utilities;

import java.util.List;
import java.util.Vector;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailFragment extends Fragment implements View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    private static final int TRAILER_LOADER = 12;
    private static final int REVIEW_LOADER = 56;

    private boolean trailerFirstRun = true;
    private boolean reviewFirstRun = true;
    private Activity mActivity;
    private MovieInfo movieInfo;
    private static final String TAG = "MovieDetailFragment";
    private RecyclerViewWithEmptyView reviewRecyclerView;
    private RecyclerViewWithEmptyView trailerRecyclerView;
    private ReviewRecyclerViewAdapter reviewAdapter;
    private TrailerRecyclerViewAdapter trailerAdapter;
    private FloatingActionButton fab;
    private AsyncQueryHandler asyncQueryHandler;
    private ImageView movieImageView;
    private ImageView posterImageView;
    private ShareActionProvider mShareActionProvider;
    private String shareFirstTrailerLink;

    public MovieDetailFragment() {
        // Required empty public constructor
    }

    public static MovieDetailFragment newInstance(MovieInfo param1, boolean param2) {
        MovieDetailFragment fragment = new MovieDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable("movieInfo", param1);
        args.putBoolean("mTwoPane", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            movieInfo = getArguments().getParcelable("movieInfo");
        } else if (savedInstanceState != null) {
            movieInfo = savedInstanceState.getParcelable("parcelMovie");
        }

        setHasOptionsMenu(true);

        asyncQueryHandler = new AsyncQueryHandler(mActivity.getContentResolver()) {
            @Override
            protected void onUpdateComplete(int token, Object cookie, int result) {
                if (token == 5678) {
                    Toast.makeText(mActivity, movieInfo.getTitle() + " Successfully Removed From Favourites", Toast.LENGTH_LONG).show();
                }
                if (token == 1234) {
                    Toast.makeText(mActivity, movieInfo.getTitle() + " Successfully Added To Favourites", Toast.LENGTH_LONG).show();
                }
                super.onUpdateComplete(token, cookie, result);
            }
        };
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        if (movieInfo != null) {
            getLoaderManager().initLoader(TRAILER_LOADER, null, this);
            getLoaderManager().initLoader(REVIEW_LOADER, null, this);
        }
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (movieInfo == null) {
            return inflater.inflate(R.layout.fragment_no_movie_detail, container, false);
        } else {
            View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

            fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
            fab.setOnClickListener(this);
            if (Utilities.isMovieFavorite(mActivity, movieInfo.getId())) {
                fab.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_favorite_white_24dp, null));
            } else {
                fab.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_favorite_border_white_24dp, null));
            }

            reviewRecyclerView = (RecyclerViewWithEmptyView) rootView.findViewById(R.id.review_list);
            reviewRecyclerView.setEmptyView(rootView.findViewById(R.id.review_empty_view));
            reviewAdapter = new ReviewRecyclerViewAdapter(mActivity, null, new ReviewRecyclerViewAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    if (Utilities.internetAvailable(mActivity)) {
                        Cursor cursor = reviewAdapter.getCursor();
                        cursor.moveToPosition(position);
                        ReviewsInfo reviewsInfo = ReviewsInfo.reviewFromFromCursor(cursor);
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(reviewsInfo.getUrl()));
                        if (browserIntent.resolveActivity(mActivity.getPackageManager()) != null) {
                            startActivity(browserIntent);
                        } else {
                            Toast.makeText(mActivity, "Could not resolve activity to show Review!", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(mActivity, "No Internet Connection! Unable to show Review!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            reviewRecyclerView.setAdapter(reviewAdapter);
            reviewRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
            trailerRecyclerView = (RecyclerViewWithEmptyView) rootView.findViewById(R.id.trailer_recycler_view);
            trailerRecyclerView.setEmptyView(rootView.findViewById(R.id.trailer_empty_view));
            trailerRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false));
            trailerAdapter = new TrailerRecyclerViewAdapter(mActivity, null, new TrailerRecyclerViewAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    if (Utilities.internetAvailable(mActivity)) {
                        Cursor cursor = trailerAdapter.getCursor();
                        cursor.moveToPosition(position);
                        TrailersInfo trailersInfo = TrailersInfo.trailerFromFromCursor(cursor);
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + trailersInfo.getKey()));
                        if (intent.resolveActivity(mActivity.getPackageManager()) != null) {
                            startActivity(intent);
                        } else {
                            Toast.makeText(mActivity, "Could not resolve activity to show Trailer!", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(mActivity, "No Internet Connection! Unable to show trailer!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            trailerRecyclerView.setAdapter(trailerAdapter);

            if (movieInfo != null) {
                //Set Image
                movieImageView = (ImageView) rootView.findViewById(R.id.movie_detail_image_view);
                if (movieImageView != null) {
                    if (!movieInfo.getPoster_path().equals("No Path")) {
                        final Transformation transformation = new RoundedCornersTransformation(mActivity, 12, 6);
                        final Uri uri = Uri.parse(Utilities.imageAddress500w + movieInfo.getPoster_path());
                        Glide.with(mActivity).load(uri)
                                .fitCenter().diskCacheStrategy(DiskCacheStrategy.ALL)
                                .bitmapTransform(transformation).placeholder(R.drawable.spinner_animation)
                                .error(R.mipmap.ic_launcher).into(movieImageView);
                        //}
                    }
                }
                //Set Poster Image
                posterImageView = (ImageView) rootView.findViewById(R.id.poster_image_view);
                if (posterImageView != null) {
                    if (!movieInfo.getBackdrop_path().equals("No Path")) {
                        final Uri uri = Uri.parse(Utilities.imageAddress500w + movieInfo.getBackdrop_path());
                        Glide.with(mActivity).load(uri)
                                .centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL)
                                .placeholder(R.drawable.spinner_animation).error(R.mipmap.ic_launcher)
                                .into(posterImageView);
                    }
                }

                //Set Synopsis
                TextView synopsis = (TextView) rootView.findViewById(R.id.synopsis);
                synopsis.setText(movieInfo.getOverview());

                //Set Vote Count
                TextView voteCount = (TextView) rootView.findViewById(R.id.vote_count);
                voteCount.setText(String.valueOf(movieInfo.getVote_count()));

                //Set Release Date
                TextView releaseDate = (TextView) rootView.findViewById(R.id.release_date);
                String release = movieInfo.getRelease_date();
                releaseDate.setText(Utilities.getReleaseDate(release));

                //Set User Rating
                TextView userRating = (TextView) rootView.findViewById(R.id.user_rating);
                String userRatingText = movieInfo.getVote_average() + getString(R.string.out_of_10);
                userRating.setText(userRatingText);
            }
            return rootView;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            mActivity = (Activity) context;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (movieInfo != null) {
            outState.putParcelable("parcelMovie", movieInfo);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_movie_detail, menu);
        MenuItem menuItem = menu.findItem(R.id.action_share);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
        if (shareFirstTrailerLink != null) {
            mShareActionProvider.setShareIntent(createShareTrailerIntent());
        }
    }

    private Intent createShareTrailerIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareFirstTrailerLink);
        return shareIntent;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fab) {
            setFavoriteMovie();
            //} else {

        }
    }

    private void setFavoriteMovie() {
        Toast.makeText(mActivity, "Processing...", Toast.LENGTH_SHORT).show();
        Uri itemUri = ContentUris.withAppendedId(MovieContract.MovieEntry.CONTENT_URI, movieInfo.getId());
        ContentValues cv = movieInfo.movieToContentValues();
        if (Utilities.isMovieFavorite(mActivity, movieInfo.getId())) {
            fab.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_favorite_border_white_24dp, null));
            cv.put("is_favorite", "no");
            asyncQueryHandler.startUpdate(5678, null, itemUri, cv, null, null);
        } else {
            fab.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_favorite_white_24dp, null));
            cv.put("is_favorite", "yes");
            asyncQueryHandler.startUpdate(1234, null, itemUri, cv, null, null);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case TRAILER_LOADER:
                return new CursorLoader(mActivity, MovieContract.TrailerEntry.buildTrailerUriWithMovieId(Long.toString(movieInfo.getId())), null, null, null, null);
            case REVIEW_LOADER:
                return new CursorLoader(mActivity, MovieContract.ReviewEntry.buildReviewUriWithMovieId(Long.toString(movieInfo.getId())), null, null, null, null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            case TRAILER_LOADER:
                if (data.getCount() == 0 && trailerFirstRun && Utilities.internetAvailable(mActivity)) {
                    downloadTrailers();
                }
                trailerAdapter.swapCursor(data);

                if (data.getCount() > 0) {
                    data.moveToFirst();
                    TrailersInfo trailersInfo = TrailersInfo.trailerFromFromCursor(data);
                    shareFirstTrailerLink = "http://www.youtube.com/watch?v=" + trailersInfo.getKey();
                    if (mShareActionProvider != null) {
                        mShareActionProvider.setShareIntent(createShareTrailerIntent());
                    }
                }

                break;
            case REVIEW_LOADER:
                if (data.getCount() == 0 && reviewFirstRun && Utilities.internetAvailable(mActivity)) {
                    downloadReviews();
                }
                reviewAdapter.swapCursor(data);
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        reviewAdapter.swapCursor(null);
    }

    private void downloadTrailers() {
        trailerFirstRun = false;
        MovieDbInterface dbInterface = MovieDbService.getClient().create(MovieDbInterface.class);
        Call<TrailersJsonResponse> makeCall = dbInterface.getTrailersForMovie(movieInfo.getId(), BuildConfig.MOVIES_API_KEY);
        makeCall.enqueue(new Callback<TrailersJsonResponse>() {
            @Override
            public void onResponse(Call<TrailersJsonResponse> call, Response<TrailersJsonResponse> response) {
                if (response.isSuccessful()) {
                    List<TrailersInfo> trailersInfo = response.body().getResults();
                    Vector<ContentValues> cVVector = new Vector<>(trailersInfo.size());
                    ContentValues cv = new ContentValues();

                    for (TrailersInfo trailer : trailersInfo) {
                        cv = trailer.trailerToContentValues();
                        cv.put("movie_id", movieInfo.getId());
                        cVVector.add(cv);
                    }
                    if (cVVector.size() > 0) {
                        ContentValues[] cvArray = new ContentValues[cVVector.size()];
                        cVVector.toArray(cvArray);
                        asyncQueryHandler.startBulkInsert(111, null, MovieContract.TrailerEntry.CONTENT_URI, cvArray);
                    }
                }
            }

            @Override
            public void onFailure(Call<TrailersJsonResponse> call, Throwable t) {
                Log.d(TAG, "call: " + call);
                Log.d(TAG, "throws: " + t.toString());
            }
        });
    }

    private void downloadReviews() {
        reviewFirstRun = false;
        MovieDbInterface dbInterface = MovieDbService.getClient().create(MovieDbInterface.class);
        Call<ReviewsJsonResponse> reviewCall = dbInterface.getReviewsForMovie(movieInfo.getId(), BuildConfig.MOVIES_API_KEY);
        reviewCall.enqueue(new Callback<ReviewsJsonResponse>() {
            @Override
            public void onResponse(Call<ReviewsJsonResponse> call, Response<ReviewsJsonResponse> response) {
                if (response.isSuccessful()) {
                    List<ReviewsInfo> reviewsInfo = response.body().getResults();
                    Vector<ContentValues> cVVector = new Vector<>(reviewsInfo.size());
                    ContentValues cv = new ContentValues();

                    for (ReviewsInfo review : reviewsInfo) {
                        cv = review.reviewToContentValues();
                        cv.put("movie_id", movieInfo.getId());
                        cVVector.add(cv);
                    }
                    if (cVVector.size() > 0) {
                        ContentValues[] cvArray = new ContentValues[cVVector.size()];
                        cVVector.toArray(cvArray);
                        asyncQueryHandler.startBulkInsert(222, null, MovieContract.ReviewEntry.CONTENT_URI, cvArray);
                    }
                }
            }

            @Override
            public void onFailure(Call<ReviewsJsonResponse> call, Throwable t) {
                Log.d(TAG, "call: " + call);
                Log.d(TAG, "throws: " + t.toString());
            }
        });
    }
}
