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
import com.training.yasser.popularmovies.adapters.ActorListAdapter;
import com.training.yasser.popularmovies.adapters.ReviewListAdapter;
import com.training.yasser.popularmovies.adapters.TrailerListAdapter;
import com.training.yasser.popularmovies.interfaces.ClickListener;
import com.training.yasser.popularmovies.models.Actor;
import com.training.yasser.popularmovies.models.ActorResponse;
import com.training.yasser.popularmovies.models.Movie;
import com.training.yasser.popularmovies.models.Review;
import com.training.yasser.popularmovies.models.ReviewResponse;
import com.training.yasser.popularmovies.models.Trailer;
import com.training.yasser.popularmovies.models.TrailerResponse;
import com.training.yasser.popularmovies.network.ApiClient;
import com.training.yasser.popularmovies.network.Connection;
import com.training.yasser.popularmovies.utils.MovieProvider;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by yasser on 17/09/2016.
 */
public class DetailFragment extends Fragment implements ClickListener {

    private static final String TAG = DetailFragment.class.getSimpleName();

    public static final int MOVIE_LOADER_ID = 0;
    public static final int REVIEW_LOADER_ID = 1;
    public static final int TRAILER_LOADER_ID = 2;
    public static final int ACTOR_LOADER_ID = 3;

    public static final String MOVIE_TAG = "movie tag";
    public static final String Twopane_TAG = "twopane tag";

    private static final String TRAILERS_STATE = "trailers state";
    private static final String REVIEWS_STATE = "reviews state";
    private static final String MOVIE_STATE = "movie state";
    private static final String ACTORS_STATE = "actor state";
    private static final String FAVOURITE_STATE = "favourite state";

    private Movie mMovie;

    @BindView(R.id.poster)
    ImageView mPoster;
    @BindView(R.id.backDrop)
    ImageView mBackDrop;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.plot)
    TextView mPlot;
    @BindView(R.id.rating)
    TextView mRating;
    @BindView(R.id.date)
    TextView mDate;
    @BindView(R.id.FavoriteButton)
    FloatingActionButton mFavouriteButton;

    private ArrayList<Trailer> trailers;
    private ArrayList<Review> reviews;
    private ArrayList<Actor> actors;

    @BindView(R.id.TrailerList)
    RecyclerView mTrailerRecyclerView;
    private TrailerListAdapter mTrailerAdapter;

    @BindView(R.id.ReviewList)
    RecyclerView mReviewRecyclerView;
    private ReviewListAdapter mReviewAdapter;

    @BindView(R.id.ActorList)
    RecyclerView mActorRecyclerView;
    private ActorListAdapter mActorAdapter;

    private boolean favorite = false;
    private boolean isTwopane = false;
    private Unbinder unbinder;

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
        unbinder = ButterKnife.bind(this, view);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.detailBar);
        if (isTwopane) {
            toolbar.inflateMenu(R.menu.menu_detail);
            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getItemId() == R.id.action_share) {
                        shareTrailer();
                        return true;
                    }
                    return false;
                }
            });
        } else {
            setHasOptionsMenu(true);
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        }


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

        mTrailerAdapter = new TrailerListAdapter(this, trailers);
        mTrailerRecyclerView.setAdapter(mTrailerAdapter);
        mTrailerRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        mActorAdapter = new ActorListAdapter(this, actors);
        mActorRecyclerView.setAdapter(mActorAdapter);
        mActorRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        mReviewAdapter = new ReviewListAdapter(this, reviews);
        mReviewRecyclerView.setAdapter(mReviewAdapter);
        mReviewRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        populateView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
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
        ApiClient.MovieDBApi movieDBApi = ApiClient.getMovieDBApi();
        Call<ActorResponse> call = movieDBApi.getActorList(mMovie.getId());
        call.enqueue(new Callback<ActorResponse>() {
            @Override
            public void onResponse(Call<ActorResponse> call, Response<ActorResponse> response) {
                loadFinished(ACTOR_LOADER_ID, response.body().getActors());
                Log.d(TAG, "load actor list : " + response.isSuccessful());
            }

            @Override
            public void onFailure(Call<ActorResponse> call, Throwable t) {
                Log.d(TAG, "load Actor list failed : " + t.getMessage());
            }
        });
    }

    private void loadReviews() {
        ApiClient.MovieDBApi movieDBApi = ApiClient.getMovieDBApi();
        Call<ReviewResponse> call = movieDBApi.getReviewList(mMovie.getId());
        call.enqueue(new Callback<ReviewResponse>() {
            @Override
            public void onResponse(Call<ReviewResponse> call, Response<ReviewResponse> response) {
                loadFinished(REVIEW_LOADER_ID, response.body().getReviews());
                Log.d(TAG, "load actor list : " + response.isSuccessful());
            }

            @Override
            public void onFailure(Call<ReviewResponse> call, Throwable t) {
                Log.d(TAG, "load Review list failed : " + t.getMessage());
            }
        });
    }

    private void loadTrailer() {
        ApiClient.MovieDBApi movieDBApi = ApiClient.getMovieDBApi();
        Call<TrailerResponse> call = movieDBApi.getTrailerList(mMovie.getId());
        call.enqueue(new Callback<TrailerResponse>() {
            @Override
            public void onResponse(Call<TrailerResponse> call, Response<TrailerResponse> response) {
                loadFinished(TRAILER_LOADER_ID, response.body().getTrailers());
                Log.d(TAG, "load Review list : " + response.isSuccessful());
            }

            @Override
            public void onFailure(Call<TrailerResponse> call, Throwable t) {
                Log.d(TAG, "load Review list failed : " + t.getMessage());
            }
        });
    }

    private void populateView() {
        Glide
                .with(this)
                .load(mMovie.getPosterPath())
                .placeholder(R.drawable.placeholder)
                .fitCenter()
                .dontAnimate()
                .into(mPoster);
        Glide
                .with(this)
                .load(mMovie.getBackdropPath())
                .into(mBackDrop);
        mTitle.setText(mMovie.getTitle());
        mPlot.setText(mMovie.getOverview());
        mRating.setText(Double.toString(mMovie.getVoteAverage()) + "/10");
        mDate.setText(mMovie.getReleaseDate());

        ContentResolver resolver = getContext().getContentResolver();
        String selection = MovieProvider.KEY_COLUMN_MOVIE_ID + " = ?";
        String[] selectionArgs = {"" + mMovie.getId()};
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
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + trailers.get(index).getSource()));
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://www.youtube.com/watch?v=" + trailers.get(index).getSource()));
            startActivity(intent);
        }
    }

    private void openReview(String s) {
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(s));
        startActivity(i);
    }

    private void loadFinished(int id, List data) {
        if (data == null || data.isEmpty()) {
            return;
        }
        switch (id) {
            case REVIEW_LOADER_ID:
                if (!reviews.isEmpty()) {
                    reviews.clear();
                }
                reviews.addAll(data);
                mReviewAdapter.notifyDataSetChanged();
                break;
            case TRAILER_LOADER_ID:
                if (!trailers.isEmpty()) {
                    trailers.clear();
                }
                trailers.addAll(data);
                mTrailerAdapter.notifyDataSetChanged();
                break;
            case ACTOR_LOADER_ID:
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
            case TRAILER_LOADER_ID:
                openVideo(position);
                break;
            case REVIEW_LOADER_ID:
                openReview(reviews.get(position).getUrl());
                break;
        }
    }

    public void FavButton(View view) {
        ContentResolver resolver = getContext().getContentResolver();
        if (favorite) {
            String selection = MovieProvider.KEY_COLUMN_MOVIE_ID + " = ?";
            String[] selectionArgs = {"" + mMovie.getId()};
            resolver.delete(MovieProvider.MOVIE_URI, selection, selectionArgs);
            favorite = false;
        } else {
            ContentValues values = new ContentValues();
            values.put(MovieProvider.KEY_COLUMN_IMG, mMovie.getPosterPath());
            values.put(MovieProvider.KEY_COLUMN_TITLE, mMovie.getTitle());
            values.put(MovieProvider.KEY_COLUMN_PLOT, mMovie.getOverview());
            values.put(MovieProvider.KEY_COLUMN_RATING, mMovie.getVoteAverage());
            values.put(MovieProvider.KEY_COLUMN_RELDATE, mMovie.getReleaseDate());
            values.put(MovieProvider.KEY_COLUMN_MOVIE_ID, mMovie.getId());
            values.put(MovieProvider.KEY_COLUMN_BACKDROP, mMovie.getBackdropPath());
            values.put(MovieProvider.KEY_COLUMN_GENRE, convertToString(mMovie.getGenreIds()));
            resolver.insert(MovieProvider.MOVIE_URI, values);
            favorite = true;
        }
        updateFloatingButton();
    }

    private String convertToString(List<Integer> list) {
        StringBuilder builder = new StringBuilder();
        for (int item : list) {
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
        }
        return super.onOptionsItemSelected(item);
    }

    private void shareTrailer() {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT,
                "http://www.youtube.com/watch?v=" + trailers.get(0).getSource());
        shareIntent.setType("text/plain");
        startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.send_to)));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_detail, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

}

