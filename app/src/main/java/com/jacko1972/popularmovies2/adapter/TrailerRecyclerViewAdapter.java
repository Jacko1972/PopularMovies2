package com.jacko1972.popularmovies2.adapter;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jacko1972.popularmovies2.R;
import com.jacko1972.popularmovies2.model.TrailersInfo;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class TrailerRecyclerViewAdapter extends CursorRecyclerViewAdapter<TrailerRecyclerViewAdapter.TrailerViewHolder> {


    private final OnItemClickListener myOnItemClickListener;
    private Context context;

    public TrailerRecyclerViewAdapter(Context context, Cursor cursor, OnItemClickListener onItemClickListener) {
        super(context, cursor);
        this.context = context;
        this.myOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_item, parent, false);
        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TrailerViewHolder holder, Cursor cursor) {
        holder.trailerInfo = TrailersInfo.trailerFromFromCursor(cursor);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myOnItemClickListener.onItemClick(v, holder.getAdapterPosition());
            }
        });
        final Transformation transformation = new RoundedCornersTransformation(context, 12, 6);

        Uri uri = Uri.parse("https://img.youtube.com/vi/" + holder.trailerInfo.getKey() + "/mqdefault.jpg");
        Glide.with(context).load(uri)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .bitmapTransform(transformation)
                .override(90, 160)
                .into(holder.trailerImageView);
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder {
        final ImageView trailerImageView;
        final View mView;
        TrailersInfo trailerInfo;

        public TrailerViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            trailerImageView = (ImageView) itemView.findViewById(R.id.trailer_item_image_view);
        }
    }
}
