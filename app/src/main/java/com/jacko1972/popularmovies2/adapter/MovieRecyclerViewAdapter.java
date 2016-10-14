package com.jacko1972.popularmovies2.adapter;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jacko1972.popularmovies2.R;
import com.jacko1972.popularmovies2.model.MovieInfo;
import com.jacko1972.popularmovies2.util.Utilities;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MovieRecyclerViewAdapter extends CursorRecyclerViewAdapter<MovieRecyclerViewAdapter.MovieImageViewHolder> {


    private Context context;
    private final OnItemClickListener myOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public MovieRecyclerViewAdapter(Context context, Cursor cursor, OnItemClickListener onItemClickListener) {
        super(context, cursor);
        this.myOnItemClickListener = onItemClickListener;
        this.context = context;
    }

    @Override
    public MovieImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_image, parent, false);
        return new MovieImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MovieImageViewHolder viewHolder, Cursor cursor) {
        viewHolder.movieInfo = MovieInfo.movieFromCursor(cursor);
        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myOnItemClickListener.onItemClick(v, viewHolder.getAdapterPosition());
            }
        });
        final Transformation transformation = new RoundedCornersTransformation(context, 12, 6);

        if (!viewHolder.movieInfo.getBackdrop_path().equals("No Path")) {
            final Uri uri = Uri.parse(Utilities.imageAddress500w + viewHolder.movieInfo.getPoster_path());
            Glide.with(context).load(uri)
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .bitmapTransform(transformation)
                    .placeholder(R.drawable.spinner_animation)
                    .error(R.mipmap.ic_launcher)
                    .into(viewHolder.imageView);
            //}
        } else {
            Glide.with(context).load(R.mipmap.ic_launcher).transform(transformation).into(viewHolder.imageView);
        }

        if (Utilities.isMovieFavorite(context, viewHolder.movieInfo.getId())) {
            viewHolder.starImageView.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), android.R.drawable.star_on, null));
        } else {
            viewHolder.starImageView.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), android.R.drawable.star_off, null));
        }

        String userRatingText = viewHolder.movieInfo.getVote_average() + context.getResources().getString(R.string.out_of_10);
        viewHolder.voteCount.setText(userRatingText);
    }

    public class MovieImageViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView imageView;
        public final ImageView starImageView;
        public final TextView voteCount;
        public MovieInfo movieInfo;

        public MovieImageViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            voteCount = (TextView) itemView.findViewById(R.id.rating_out_of_10);
            imageView = (ImageView) itemView.findViewById(R.id.movie_image_view);
            starImageView = (ImageView) itemView.findViewById(R.id.favorite_star);
        }
    }
}
