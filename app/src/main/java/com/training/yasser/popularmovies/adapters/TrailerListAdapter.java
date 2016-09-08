package com.training.yasser.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.training.yasser.popularmovies.R;
import com.training.yasser.popularmovies.interfaces.ClickListener;
import com.training.yasser.popularmovies.models.Trailer;
import com.training.yasser.popularmovies.utils.LoaderCallbacks;

import java.util.ArrayList;

/**
 * Created by yasser on 07/09/2016.
 */
public class TrailerListAdapter extends RecyclerView.Adapter<TrailerListAdapter.TrailerViewHolder> {
    private final LayoutInflater mInflater;
    private final Context context;
    private ArrayList<Trailer> mTrailers;
    private ClickListener onClickListener;

    public TrailerListAdapter(Context context, ArrayList<Trailer> trailers) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mTrailers = trailers;
        onClickListener = (ClickListener)context;
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
                .with(context)
                .load(trailer.getTumbnailUrl())
                .into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return mTrailers.size();
    }

    class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView thumbnail;

        public TrailerViewHolder(View MovieView) {
            super(MovieView);
            thumbnail = (ImageView) MovieView.findViewById(R.id.TrailerTumb);
            MovieView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onClickListener.onClick(view, getAdapterPosition(), LoaderCallbacks.TRAILER_LOADER_ID);
        }
    }
}
