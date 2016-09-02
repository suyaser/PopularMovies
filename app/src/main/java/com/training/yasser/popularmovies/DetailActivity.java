package com.training.yasser.popularmovies;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.training.yasser.popularmovies.data.Movie;
import com.training.yasser.popularmovies.data.Review;
import com.training.yasser.popularmovies.data.Trailer;
import com.training.yasser.popularmovies.utils.Connection;
import com.training.yasser.popularmovies.utils.LoaderCallbacks;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity implements LoaderCallbacks.LoaderListener{

    public static final String TAG = "DETAIL_ACTIVITY";
    private static final String TRAILERS_STATE = "trailers state";
    private static final String REVIEWS_STATE = "reviews state";
    private static final String MOVIE_STATE = "movie state";
    private Movie mMovie;
    private ImageView mPoster;
    private ImageView mBackDrop;
    private TextView mTitle;
    private TextView mPlot;
    private TextView mRating;
    private TextView mDate;
    private ArrayList<Trailer> trailers;
    private ArrayList<Review> reviews;


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
            mMovie = getIntent().getParcelableExtra(TAG);
        } else{
            trailers = savedInstanceState.getParcelableArrayList(TRAILERS_STATE);
            reviews = savedInstanceState.getParcelableArrayList(REVIEWS_STATE);
            mMovie = savedInstanceState.getParcelable(MOVIE_STATE);
        }

        mBackDrop = (ImageView) findViewById(R.id.backDrop);
        mPoster = (ImageView) findViewById(R.id.poster);
        mTitle = (TextView) findViewById(R.id.title);
        mPlot = (TextView) findViewById(R.id.plot);
        mRating = (TextView) findViewById(R.id.rating);
        mDate = (TextView) findViewById(R.id.date);


        loadReviews();
        loadTrailer();
        populateView();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(TRAILERS_STATE, trailers);
        outState.putParcelableArrayList(REVIEWS_STATE, reviews);
        outState.putParcelable(MOVIE_STATE, mMovie);
        super.onSaveInstanceState(outState);
    }

    private void loadReviews() {
        if (Connection.checkConnection(this)) {
            LoaderCallbacks<Review> loaderCallbacks = new LoaderCallbacks(this);
            LoaderManager loaderManager = getSupportLoaderManager();
            Bundle bundle = new Bundle();
            Log.d("ddd", String.valueOf(mMovie.getmId()));
            bundle.putInt(LoaderCallbacks.ID_KEY, mMovie.getmId());
            loaderManager.restartLoader(LoaderCallbacks.REVIEW_LOADER_ID, bundle, loaderCallbacks);
        }
    }

    private void loadTrailer() {
        if (Connection.checkConnection(this)) {
            LoaderCallbacks<Trailer> loaderCallbacks = new LoaderCallbacks(this);
            LoaderManager loaderManager = getSupportLoaderManager();
            Bundle bundle = new Bundle();
            bundle.putInt(LoaderCallbacks.ID_KEY, mMovie.getmId());
            loaderManager.restartLoader(LoaderCallbacks.TRAILER_LOADER_ID, bundle, loaderCallbacks);
        }
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
        mRating.setText(Integer.toString(mMovie.getmRating()));
        mDate.setText(mMovie.getmRelDate());
    }

    public void openVideo(View v) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + trailers.get(0).getmKey()));
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://www.youtube.com/watch?v=" + trailers.get(0).getmKey()));
            startActivity(intent);
        }
    }

    @Override
    public void loadFinished(int id, List data) {
        if (data == null || data.isEmpty()) { return;}
        switch (id){
            case LoaderCallbacks.REVIEW_LOADER_ID:
                reviews.addAll(data);
                break;
            case LoaderCallbacks.TRAILER_LOADER_ID:
                trailers.addAll(data);
                break;
        }
    }
}
