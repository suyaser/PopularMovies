package com.training.yasser.popularmovies.fragments;


import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.training.yasser.popularmovies.R;
import com.training.yasser.popularmovies.activities.DetailActivity;
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
import com.training.yasser.popularmovies.utils.MovieProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yasser on 17/09/2016.
 */
public class DetailFragment extends Fragment implements LoaderCallbacks.LoaderListener, ClickListener {

    public static final String MOVIE_TAG = "movie tag";
    private static final String Twopane_TAG = "twopane tag";
    private static final String TRAILERS_STATE = "trailers state";
    private static final String REVIEWS_STATE = "reviews state";
    private static final String MOVIE_STATE = "movie state";
    private static final String ACTORS_STATE = "actor state";
    private static final String FAVOURITE_STATE = "favourite state";

    private Movie mMovie;
    private ImageView mPoster;
    private ImageView mBackDrop;
    private TextView mTitle;
    private TextView mPlot;
    private TextView mRating;
    private TextView mDate;
    private FloatingActionButton mFavouriteButton;
    private ArrayList<Trailer> trailers;
    private ArrayList<Review> reviews;
    private RecyclerView mTrailerRecyclerView;
    private TrailerListAdapter mTrailerAdapter;
    private RecyclerView mReviewRecyclerView;
    private ReviewListAdapter mReviewAdapter;
    private RecyclerView mActorRecyclerView;
    private ActorListAdapter mActorAdapter;
    private ArrayList<Actor> actors;
    private boolean favorite = false;
    private boolean isTwopane = false;


    public static DetailFragment newInstance(Movie movie, boolean isTwoBane) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(MOVIE_TAG, movie);
        args.putBoolean(Twopane_TAG, isTwoBane);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (savedInstanceState == null) {
            trailers = new ArrayList();
            reviews = new ArrayList();
            actors = new ArrayList();
            mMovie = (Movie) getArguments().getParcelable(MOVIE_TAG);
            isTwopane = getArguments().getBoolean(Twopane_TAG);
        } else {
            trailers = savedInstanceState.getParcelableArrayList(TRAILERS_STATE);
            reviews = savedInstanceState.getParcelableArrayList(REVIEWS_STATE);
            actors = savedInstanceState.getParcelableArrayList(ACTORS_STATE);
            mMovie = savedInstanceState.getParcelable(MOVIE_STATE);
            favorite = savedInstanceState.getBoolean(FAVOURITE_STATE);
            isTwopane = savedInstanceState.getBoolean(Twopane_TAG);
        }

        return inflater.inflate(R.layout.fragment_detail,
                container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.detailBar);
        if(!isTwopane) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        }else{
            toolbar.inflateMenu(R.menu.menu_detail);
            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if(item.getItemId() == R.id.action_share){
                        shareTrailer();
                        return true;
                    }
                    return false;
                }
            });
        }

        mBackDrop = (ImageView) view.findViewById(R.id.backDrop);
        mPoster = (ImageView) view.findViewById(R.id.poster);
        mTitle = (TextView) view.findViewById(R.id.title);
        mPlot = (TextView) view.findViewById(R.id.plot);
        mRating = (TextView) view.findViewById(R.id.rating);
        mDate = (TextView) view.findViewById(R.id.date);
        mFavouriteButton = (FloatingActionButton) view.findViewById(R.id.FavoriteButton);
        mFavouriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FavButton(view);
            }
        });
        mBackDrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openVideo(view);
            }
        });

        if (Connection.checkConnection(getContext())) {
            loadReviews();
            loadTrailer();
            loadActor();
        }

        mTrailerRecyclerView = (RecyclerView) view.findViewById(R.id.TrailerList);
        mTrailerAdapter = new TrailerListAdapter(this, trailers);
        mTrailerRecyclerView.setAdapter(mTrailerAdapter);
        mTrailerRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        mActorRecyclerView = (RecyclerView) view.findViewById(R.id.ActorList);
        mActorAdapter = new ActorListAdapter(this, actors);
        mActorRecyclerView.setAdapter(mActorAdapter);
        mActorRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        mReviewRecyclerView = (RecyclerView) view.findViewById(R.id.ReviewList);
        mReviewAdapter = new ReviewListAdapter(this, reviews);
        mReviewRecyclerView.setAdapter(mReviewAdapter);
        mReviewRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        populateView();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(TRAILERS_STATE, trailers);
        outState.putParcelableArrayList(REVIEWS_STATE, reviews);
        outState.putParcelableArrayList(ACTORS_STATE, actors);
        outState.putParcelable(MOVIE_STATE, mMovie);
        outState.putBoolean(FAVOURITE_STATE, favorite);
        outState.putBoolean(Twopane_TAG, isTwopane);
        super.onSaveInstanceState(outState);
    }

    private void loadActor() {
        LoaderCallbacks<Actor> loaderCallbacks = new LoaderCallbacks(this);
        LoaderManager loaderManager = getLoaderManager();
        Bundle bundle = new Bundle();
        bundle.putInt(LoaderCallbacks.ID_KEY, mMovie.getmId());
        loaderManager.restartLoader(LoaderCallbacks.ACTOR_LOADER_ID, bundle, loaderCallbacks);
    }

    private void loadReviews() {
        LoaderCallbacks<Review> loaderCallbacks = new LoaderCallbacks(this);
        LoaderManager loaderManager = getLoaderManager();
        Bundle bundle = new Bundle();
        bundle.putInt(LoaderCallbacks.ID_KEY, mMovie.getmId());
        loaderManager.restartLoader(LoaderCallbacks.REVIEW_LOADER_ID, bundle, loaderCallbacks);
    }

    private void loadTrailer() {
        LoaderCallbacks<Trailer> loaderCallbacks = new LoaderCallbacks(this);
        LoaderManager loaderManager = getLoaderManager();
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

        ContentResolver resolver = getContext().getContentResolver();
        String selection = MovieProvider.KEY_COLUMN_MOVIE_ID + " = ?";
        String[] selectionArgs = {"" + mMovie.getmId()};
        Cursor cursor =
                resolver.query(MovieProvider.MOVIE_URI,
                        null,
                        selection,
                        selectionArgs,
                        null);
        if (cursor.getCount() == 0) {
            favorite = false;
        } else {
            favorite = true;
        }
        updateFloatingButton();
    }

    private void updateFloatingButton() {
        if (favorite) {
            mFavouriteButton.setImageResource(R.drawable.ic_favorite_white_24dp);
        } else {
            mFavouriteButton.setImageResource(R.drawable.ic_favorite_border_white_24dp);
        }
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
        if (data == null || data.isEmpty()) {
            return;
        }
        switch (id) {
            case LoaderCallbacks.REVIEW_LOADER_ID:
                if (!reviews.isEmpty()) {
                    reviews.clear();
                }
                reviews.addAll(data);
                mReviewAdapter.notifyDataSetChanged();
                break;
            case LoaderCallbacks.TRAILER_LOADER_ID:
                if (!trailers.isEmpty()) {
                    trailers.clear();
                }
                trailers.addAll(data);
                mTrailerAdapter.notifyDataSetChanged();
                break;
            case LoaderCallbacks.ACTOR_LOADER_ID:
                if (!actors.isEmpty()) {
                    actors.clear();
                }
                actors.addAll(data);
                mActorAdapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public void onClick(View view, int position, int type) {
        switch (type) {
            case LoaderCallbacks.TRAILER_LOADER_ID:
                openVideo(position);
                break;
            case LoaderCallbacks.REVIEW_LOADER_ID:
                openReview(reviews.get(position).getmUrl());
                break;
        }
    }

    public void FavButton(View view) {
        ContentResolver resolver = getContext().getContentResolver();
        if (favorite) {
            String selection = MovieProvider.KEY_COLUMN_MOVIE_ID + " = ?";
            String[] selectionArgs = {"" + mMovie.getmId()};
            resolver.delete(MovieProvider.MOVIE_URI, selection, selectionArgs);
            favorite = false;
        } else {
            ContentValues values = new ContentValues();
            values.put(MovieProvider.KEY_COLUMN_IMG, mMovie.getmImg());
            values.put(MovieProvider.KEY_COLUMN_TITLE, mMovie.getmTitle());
            values.put(MovieProvider.KEY_COLUMN_PLOT, mMovie.getmPlot());
            values.put(MovieProvider.KEY_COLUMN_RATING, mMovie.getmRating());
            values.put(MovieProvider.KEY_COLUMN_RELDATE, mMovie.getmRelDate());
            values.put(MovieProvider.KEY_COLUMN_MOVIE_ID, mMovie.getmId());
            values.put(MovieProvider.KEY_COLUMN_BACKDROP, mMovie.getmBackDrop());
            values.put(MovieProvider.KEY_COLUMN_GENRE, convertToString(mMovie.getmGenre()));
            resolver.insert(MovieProvider.MOVIE_URI, values);
            favorite = true;
        }
        updateFloatingButton();
    }

    private String convertToString(ArrayList<String> list) {
        StringBuilder builder = new StringBuilder();
        for (String item : list) {
            builder.append(item);
            builder.append(",");
        }
        builder.setLength(builder.length() - 1);
        return builder.toString();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                shareTrailer();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void shareTrailer() {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT,
                Uri.parse("http://www.youtube.com/watch?v=" + trailers.get(0).getmKey()));
        shareIntent.setType("text/plain");
        startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.send_to)));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_detail, menu);
    }

}

