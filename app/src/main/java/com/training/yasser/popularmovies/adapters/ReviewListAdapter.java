package com.training.yasser.popularmovies.adapters;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.training.yasser.popularmovies.R;
import com.training.yasser.popularmovies.interfaces.ClickListener;
import com.training.yasser.popularmovies.models.Review;
import com.training.yasser.popularmovies.utils.LoaderCallbacks;

import java.util.ArrayList;

/**
 * Created by yasser on 08/09/2016.
 */
public class ReviewListAdapter extends RecyclerView.Adapter<ReviewListAdapter.ReviewViewHolder> {
    private final LayoutInflater mInflater;
    private final Fragment fragment;
    private ArrayList<Review> mReviews;
    private ClickListener onClickListener;
    public ReviewListAdapter(Fragment fragment, ArrayList<Review> reviews) {
        mInflater = LayoutInflater.from(fragment.getContext());
        this.fragment = fragment;
        this.mReviews = reviews;
        onClickListener = (ClickListener)fragment;

    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.review_list_item, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        Review review = mReviews.get(position);
        holder.mAuthor.setText(review.getmAuthor());
        holder.mContent.setText(review.getmContent());

    }

    @Override
    public int getItemCount() {
        return mReviews.size();
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mAuthor;
        TextView mContent;

        public ReviewViewHolder(View MovieView) {
            super(MovieView);
            mAuthor = (TextView) MovieView.findViewById(R.id.ReviewAuthor);
            mContent = (TextView) MovieView.findViewById(R.id.ReviewContent);
            MovieView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onClickListener.onClick(view, getAdapterPosition(), LoaderCallbacks.REVIEW_LOADER_ID);
        }
    }
}
