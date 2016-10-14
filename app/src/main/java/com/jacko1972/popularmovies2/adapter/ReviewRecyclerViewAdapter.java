package com.jacko1972.popularmovies2.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jacko1972.popularmovies2.R;
import com.jacko1972.popularmovies2.model.ReviewsInfo;

public class ReviewRecyclerViewAdapter extends CursorRecyclerViewAdapter<ReviewRecyclerViewAdapter.ReviewViewHolder> {


    private final OnItemClickListener myOnItemClickListener;

    public ReviewRecyclerViewAdapter(Context context, Cursor cursor, OnItemClickListener onItemClickListener) {
        super(context, cursor);
        this.myOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_list_item, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ReviewViewHolder holder, Cursor cursor) {
        holder.reviewsInfo = ReviewsInfo.reviewFromFromCursor(cursor);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myOnItemClickListener.onItemClick(v, holder.getAdapterPosition());
            }
        });
        String author = holder.reviewsInfo.getAuthor();
        holder.author.setText(author);

        String content = holder.reviewsInfo.getContent();
        holder.reviewContent.setText(content);
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {
        final TextView author;
        final TextView reviewContent;
        final View mView;
        ReviewsInfo reviewsInfo;

        public ReviewViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            author = (TextView) itemView.findViewById(R.id.author_lbl);
            reviewContent = (TextView) itemView.findViewById(R.id.review_content);
        }
    }
}
