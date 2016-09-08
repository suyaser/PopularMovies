package com.training.yasser.popularmovies.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.training.yasser.popularmovies.R;
import com.training.yasser.popularmovies.adapters.ActorListAdapter;
import com.training.yasser.popularmovies.adapters.ReviewListAdapter;
import com.training.yasser.popularmovies.adapters.TrailerListAdapter;
import com.training.yasser.popularmovies.interfaces.ClickListener;
import com.training.yasser.popularmovies.models.Actor;
import com.training.yasser.popularmovies.models.Movie;
import com.training.yasser.popularmovies.models.Review;
import com.training.yasser.popularmovies.models.Trailer;
import com.training.yasser.popularmovies.network.Connection;
import com.training.yasser.popularmovies.utils.LoaderCallbacks;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity implements LoaderCallbacks.LoaderListener, ClickListener{

    public static final String TAG = "DETAIL_ACTIVITY";
    private static final String TRAILERS_STATE = "trailers state";
    private static final String REVIEWS_STATE = "reviews state";
    private static final String MOVIE_STATE = "movie state";
    private static final String ACTORS_STATE = "actor state";
    private Movie mMovie;
    private ImageView mPoster;
    private ImageView mBackDrop;
    private TextView mTitle;
    private TextView mPlot;
    private TextView mRating;
    private TextView mDate;
    private ArrayList<Trailer> trailers;
    private ArrayList<Review> reviews;
    private RecyclerView mTrailerRecyclerView;
    private TrailerListAdapter mTrailerAdapter;
    private RecyclerView mReviewRecyclerView;
    private ReviewListAdapter mReviewAdapter;
    private RecyclerView mActorRecyclerView;
    private ActorListAdapter mActorAdapter;
    private ArrayList<Actor> actors;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.detailBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        if (savedInstanceState == null) {
            trailers = new ArrayList();
            reviews = new ArrayList();
            actors = new ArrayList();
            mMovie = getIntent().getParcelableExtra(TAG);
        } else{
            trailers = savedInstanceState.getParcelableArrayList(TRAILERS_STATE);
            reviews = savedInstanceState.getParcelableArrayList(REVIEWS_STATE);
            actors = savedInstanceState.getParcelableArrayList(ACTORS_STATE);
            mMovie = savedInstanceState.getParcelable(MOVIE_STATE);
        }

        mBackDrop = (ImageView) findViewById(R.id.backDrop);
        mPoster = (ImageView) findViewById(R.id.poster);
        mTitle = (TextView) findViewById(R.id.title);
        mPlot = (TextView) findViewById(R.id.plot);
        mRating = (TextView) findViewById(R.id.rating);
        mDate = (TextView) findViewById(R.id.date);

        if (Connection.checkConnection(this)) {
            loadReviews();
            loadTrailer();
            loadActor();
        }

        mTrailerRecyclerView = (RecyclerView) findViewById(R.id.TrailerList);
        mTrailerAdapter = new TrailerListAdapter(this, trailers);
        mTrailerRecyclerView.setAdapter(mTrailerAdapter);
        mTrailerRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        mActorRecyclerView = (RecyclerView) findViewById(R.id.ActorList);
        mActorAdapter = new ActorListAdapter(this, actors);
        mActorRecyclerView.setAdapter(mActorAdapter);
        mActorRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        mReviewRecyclerView = (RecyclerView) findViewById(R.id.ReviewList);
        mReviewAdapter = new ReviewListAdapter(this, reviews);
        mReviewRecyclerView.setAdapter(mReviewAdapter);
        mReviewRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        populateView();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(TRAILERS_STATE, trailers);
        outState.putParcelableArrayList(REVIEWS_STATE, reviews);
        outState.putParcelableArrayList(ACTORS_STATE, actors);
        outState.putParcelable(MOVIE_STATE, mMovie);
        super.onSaveInstanceState(outState);
    }

    private void loadActor() {
            LoaderCallbacks<Actor> loaderCallbacks = new LoaderCallbacks(this);
            LoaderManager loaderManager = getSupportLoaderManager();
            Bundle bundle = new Bundle();
            bundle.putInt(LoaderCallbacks.ID_KEY, mMovie.getmId());
            loaderManager.restartLoader(LoaderCallbacks.ACTOR_LOADER_ID, bundle, loaderCallbacks);
    }


    private void loadReviews() {
            LoaderCallbacks<Review> loaderCallbacks = new LoaderCallbacks(this);
            LoaderManager loaderManager = getSupportLoaderManager();
            Bundle bundle = new Bundle();
            bundle.putInt(LoaderCallbacks.ID_KEY, mMovie.getmId());
            loaderManager.restartLoader(LoaderCallbacks.REVIEW_LOADER_ID, bundle, loaderCallbacks);
    }

    private void loadTrailer() {
            LoaderCallbacks<Trailer> loaderCallbacks = new LoaderCallbacks(this);
            LoaderManager loaderManager = getSupportLoaderManager();
            Bundle bundle = new Bundle();
            bundle.putInt(LoaderCallbacks.ID_KEY, mMovie.getmId());
            loaderManager.restartLoader(LoaderCallbacks.TRAILER_LOADER_ID, bundle, loaderCallbacks);
    }

    private void populateView() {
        Glide
                .with(this)
                .load(mMovie.getmImg())
                .placeholder(R.drawable.placeholder)
                .fitCenter()
                .dontAnimate()
                .into(mPoster);
        Glide
                .with(this)
                .load(mMovie.getmBackDrop())
                .into(mBackDrop);
        mTitle.setText(mMovie.getmTitle());
        mPlot.setText(mMovie.getmPlot());
        mRating.setText(Double.toString(mMovie.getmRating()));
        mDate.setText(mMovie.getmRelDate());
    }

    public void openVideo(View v) {
        openVideo(0);
    }

    private void openVideo(int index) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + trailers.get(index).getmKey()));
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://www.youtube.com/watch?v=" + trailers.get(index).getmKey()));
            startActivity(intent);
        }
    }

    private void openReview(String s) {
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(s));
        startActivity(i);
    }

    @Override
    public void loadFinished(int id, List data) {
        if (data == null || data.isEmpty()) { return;}
        switch (id){
            case LoaderCallbacks.REVIEW_LOADER_ID:
                if(!reviews.isEmpty()){
                    reviews.clear();
                }
                reviews.addAll(data);
                mReviewAdapter.notifyDataSetChanged();
                break;
            case LoaderCallbacks.TRAILER_LOADER_ID:
                if(!trailers.isEmpty()){
                    trailers.clear();
                }
                trailers.addAll(data);
                mTrailerAdapter.notifyDataSetChanged();
                break;
            case LoaderCallbacks.ACTOR_LOADER_ID:
                if(!actors.isEmpty()){
                    actors.clear();
                }
                actors.addAll(data);
                mActorAdapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public void onClick(View view, int position, int type) {
        switch(type){
            case LoaderCallbacks.TRAILER_LOADER_ID:
                openVideo(position);
                break;
            case LoaderCallbacks.REVIEW_LOADER_ID:
                openReview(reviews.get(position).getmUrl());
                break;
        }
    }
}
