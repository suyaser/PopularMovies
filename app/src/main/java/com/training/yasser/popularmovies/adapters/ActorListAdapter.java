package com.training.yasser.popularmovies.adapters;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.training.yasser.popularmovies.R;
import com.training.yasser.popularmovies.interfaces.ClickListener;
import com.training.yasser.popularmovies.models.Actor;
import com.training.yasser.popularmovies.utils.LoaderCallbacks;

import java.util.ArrayList;

/**
 * Created by yasser on 08/09/2016.
 */
public class ActorListAdapter extends RecyclerView.Adapter<ActorListAdapter.ActorViewHolder> {
    private final LayoutInflater mInflater;
    private final Context context;
    private ArrayList<Actor> mActors;
    private ClickListener onClickListener;
    public ActorListAdapter(Context context, ArrayList<Actor> actors) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mActors = actors;
        onClickListener = (ClickListener)context;

    }

    @Override
    public ActorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.actor_list_item, parent, false);
        return new ActorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ActorViewHolder holder, int position) {
        Actor actor = mActors.get(position);
        holder.mName.setText(actor.getmName());
        holder.mRole.setText(actor.getmCharacter());
        Glide
                .with(context)
                .load(actor.getmProfPic())
                .into(holder.mProfPic);

    }

    @Override
    public int getItemCount() {
        return mActors.size();
    }

    class ActorViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView mProfPic;
        TextView mName;
        TextView mRole;

        public ActorViewHolder(View MovieView) {
            super(MovieView);
            mName = (TextView) MovieView.findViewById(R.id.ActorName);
            mRole = (TextView) MovieView.findViewById(R.id.ActorRole);
            mProfPic = (ImageView) MovieView.findViewById(R.id.ActorPic);
            MovieView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onClickListener.onClick(view, getAdapterPosition(), LoaderCallbacks.ACTOR_LOADER_ID);
        }
    }
}