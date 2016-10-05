package com.training.yasser.popularmovies.adapters;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.training.yasser.popularmovies.R;
import com.training.yasser.popularmovies.fragments.DetailFragment;
import com.training.yasser.popularmovies.interfaces.ClickListener;
import com.training.yasser.popularmovies.models.Trailer;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yasser on 07/09/2016.
 */
public class TrailerListAdapter extends RecyclerView.Adapter<TrailerListAdapter.TrailerViewHolder> {
    private final LayoutInflater mInflater;
    private final Fragment fragment;
    private ArrayList<Trailer> mTrailers;
    private ClickListener onClickListener;

    public TrailerListAdapter(Fragment fragment, ArrayList<Trailer> trailers) {
        mInflater = LayoutInflater.from(fragment.getContext());
        this.fragment = fragment;
        this.mTrailers = trailers;
        onClickListener = (ClickListener)fragment;
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.trailer_list_item, parent, false);
        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {
        Trailer trailer = mTrailers.get(position);
        Glide
                .with(fragment)
                .load(trailer.getTumbnailUrl())
                .into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return mTrailers.size();
    }

    class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.TrailerTumb) ImageView thumbnail;

        public TrailerViewHolder(View MovieView) {
            super(MovieView);
            ButterKnife.bind(this, MovieView);
            MovieView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onClickListener.onClick(view, getAdapterPosition(), DetailFragment.TRAILER_LOADER_ID);
        }
    }
}
