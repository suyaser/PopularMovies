package com.training.yasser.popularmovies.adapters;

import android.support.v4.app.Fragment;
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

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yasser on 08/09/2016.
 */
public class ActorListAdapter extends RecyclerView.Adapter<ActorListAdapter.ActorViewHolder> {
    private final LayoutInflater mInflater;
    private final Fragment fragment;
    private ArrayList<Actor> mActors;
    public ActorListAdapter(Fragment fragment, ArrayList<Actor> actors) {
        mInflater = LayoutInflater.from(fragment.getContext());
        this.fragment = fragment;
        this.mActors = actors;

    }

    @Override
    public ActorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.actor_list_item, parent, false);
        return new ActorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ActorViewHolder holder, int position) {
        Actor actor = mActors.get(position);
        holder.mName.setText(actor.getName());
        holder.mRole.setText(actor.getCharacter());
        Glide
                .with(fragment)
                .load(actor.getProfilePath())
                .into(holder.mProfPic);

    }

    @Override
    public int getItemCount() {
        return mActors.size();
    }

    class ActorViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ActorPic) ImageView mProfPic;
        @BindView(R.id.ActorName) TextView mName;
        @BindView(R.id.ActorRole) TextView mRole;

        public ActorViewHolder(View MovieView) {
            super(MovieView);
            ButterKnife.bind(this, MovieView);
        }

    }
}