package com.jacko1972.popularmovies2.util;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

public class RecyclerViewWithEmptyView extends RecyclerView {

    private View mEmptyView;

    private AdapterDataObserver mDataObserver = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            updateEmptyView();
        }
    };


    public RecyclerViewWithEmptyView(Context context) {
        super(context);
    }


    public RecyclerViewWithEmptyView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public RecyclerViewWithEmptyView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setEmptyView(View emptyView) {
        mEmptyView = emptyView;
    }


    @Override
    public void setAdapter(Adapter adapter) {
        if (getAdapter() != null) {
            getAdapter().unregisterAdapterDataObserver(mDataObserver);
        }
        if (adapter != null) {
            adapter.registerAdapterDataObserver(mDataObserver);
        }
        super.setAdapter(adapter);
        updateEmptyView();
    }


    private void updateEmptyView() {
        if (mEmptyView != null && getAdapter() != null) {
            boolean showEmptyView = getAdapter().getItemCount() == 0;
            mEmptyView.setVisibility(showEmptyView ? VISIBLE : GONE);
            setVisibility(showEmptyView ? GONE : VISIBLE);
        }
    }

}
