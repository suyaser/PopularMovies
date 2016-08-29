package com.training.yasser.popularmovies.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.training.yasser.popularmovies.data.Movie;
import com.training.yasser.popularmovies.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yasser on 17/07/2016.
 */
public class GridAdapter extends RecyclerView.Adapter<GridAdapter.ViewHolder>{
    private Context context;
    private List<Movie> movies;
    private LayoutInflater mInflater;
    private ClickListener onClickListener;

    public GridAdapter(Context context, ArrayList<Movie> movies) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
        this.movies = movies;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.grid_img_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Movie movie = movies.get(position);
        Glide
                .with(context)
                .load(movie.getmImg())
                .into(holder.imgView);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public void setOnClickListener(ClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView imgView;

        public ViewHolder(View itemView) {
            super(itemView);
            imgView = (ImageView) itemView.findViewById(R.id.GridImgItem);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.d("ViewHolder", "OnCLick");
            onClickListener.onClick(v, getAdapterPosition());
        }
    }
    public interface ClickListener{
        void onClick(View view, int position);
    }

}
