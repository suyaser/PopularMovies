package com.training.yasser.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.training.yasser.popularmovies.models.Movie;
import com.training.yasser.popularmovies.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yasser on 17/07/2016.
 */
public class GridAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_HEADER = 0;
    private static final int VIEW_ITEM = 1;
    private Context context;
    private List<Movie> movies;
    private LayoutInflater mInflater;
    private ClickListener onClickListener;
    private String headerTitle;

    public GridAdapter(Context context, ArrayList<Movie> movies) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
        this.movies = movies;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        View view;
        if (viewType == VIEW_HEADER) {
            view = mInflater.inflate(R.layout.grid_header, parent, false);
            holder = new HeaderHolder(view);
        } else if(viewType == VIEW_ITEM){
            view = mInflater.inflate(R.layout.grid_img_item, parent, false);
            holder = new ItemHolder(view);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemHolder) {
            Movie movie = movies.get(position - 1);
            Glide
                    .with(context)
                    .load(movie.getmImg())
                    .into(((ItemHolder)holder).imgView);
            ((ItemHolder)holder).Title.setText(movie.getmTitle());
        } else if (holder instanceof HeaderHolder) {
            ((HeaderHolder)holder).OrderState.setText(headerTitle);
        }
    }

    @Override
    public int getItemCount() {
        return movies.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position < 1) {
            return VIEW_HEADER;
        } else {
            return VIEW_ITEM;
        }
    }

    public void setOnClickListener(ClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setHeaderTitle(String headerTitle) {
        this.headerTitle = headerTitle;
    }

    class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imgView;
        CardView cardView;
        TextView Title;

        public ItemHolder(View itemView) {
            super(itemView);
            Title = (TextView) itemView.findViewById(R.id.MovieTitle);
            Title.setSelected(true);
            imgView = (ImageView) itemView.findViewById(R.id.GridImgItem);
            cardView = (CardView) itemView.findViewById(R.id.GridCardView);
            cardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onClickListener.onClick(v, getAdapterPosition() - 1);
        }
    }

    public interface ClickListener {
        void onClick(View view, int position);
    }

    class HeaderHolder extends RecyclerView.ViewHolder {
        TextView OrderState;

        public HeaderHolder(View itemView) {
            super(itemView);
            OrderState = (TextView) itemView.findViewById(R.id.OrderTitle);
        }

    }

}
