package com.training.yasser.popularmovies.fragments;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.training.yasser.popularmovies.R;
import com.training.yasser.popularmovies.adapters.MovieListAdapter;
import com.training.yasser.popularmovies.interfaces.ClickListener;
import com.training.yasser.popularmovies.interfaces.EndlessScrollListner;
import com.training.yasser.popularmovies.models.ActorResponse;
import com.training.yasser.popularmovies.models.Movie;
import com.training.yasser.popularmovies.models.MovieResponse;
import com.training.yasser.popularmovies.network.ApiClient;
import com.training.yasser.popularmovies.network.Connection;
import com.training.yasser.popularmovies.utils.MovieProvider;

import java.util.ArrayList;
import java.util.Arrays;
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
public class ListFragment extends Fragment implements ClickListener {

    private final static String TAG = ListFragment.class.getSimpleName();
    private final static String MOVIES_STATE = "MoviesState";
    private final static String LIST_STATE = "ListState";
    private final static String SORT_STATE = "sortState";
    private final static String PAGE_STATE = "PageState";
    private final static String[] SORT = {"popular", "top_rated"};

    private final static String[] BAR_TITLE = {"Most Popular Movies", "Top Rated Movies", "Favorites"};

    @BindView(R.id.gridView) RecyclerView mRecyclerView;

    Unbinder unbinder;
    private MovieListAdapter mAdapter;
    private ArrayList<Movie> movies;
    private int sortOrder;
    private Parcelable state = null;
    private int mPage;
    private boolean mOrderChanged = false;
    private OnListItemSelectedListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main,
                container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);

        if (getActivity() instanceof OnListItemSelectedListener) {
            listener = (OnListItemSelectedListener) getActivity();
        } else {
            throw new ClassCastException(
                    getActivity().toString()
                            + " must implement ItemsListFragment.OnListItemSelectedListener");
        }

        if (savedInstanceState == null) {
            movies = new ArrayList<Movie>();
            mPage = 1;
            sortOrder = 0;
            updateMovies();
        } else {
            state = savedInstanceState.getParcelable(LIST_STATE);
            movies = savedInstanceState.getParcelableArrayList(MOVIES_STATE);
            sortOrder = savedInstanceState.getInt(SORT_STATE);
            mPage = savedInstanceState.getInt(PAGE_STATE);
        }

        mAdapter = new MovieListAdapter(this, movies);
        mAdapter.setHeaderTitle(BAR_TITLE[sortOrder]);
        mRecyclerView.setAdapter(mAdapter);
        final GridLayoutManager manager = new GridLayoutManager(getContext(), getResources().getInteger(R.integer.main_grid_columbs));
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return position == 0 ? manager.getSpanCount() : 1;
            }
        });
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.addOnScrollListener(new EndlessScrollListner((GridLayoutManager) mRecyclerView.getLayoutManager()) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                mPage++;
                updateMovies();
            }
        });

        if (state != null) {
            mRecyclerView.getLayoutManager().onRestoreInstanceState(state);
            state = null;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        state = mRecyclerView.getLayoutManager().onSaveInstanceState();
        outState.putParcelableArrayList(MOVIES_STATE, movies);
        outState.putParcelable(LIST_STATE, state);
        outState.putInt(PAGE_STATE, mPage);
        outState.putInt(SORT_STATE, sortOrder);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        unbinder.unbind();
        listener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (sortOrder == 2) {
            updateFavDB();
        }
    }

    private void updateMovies() {
        if (sortOrder == 2) {
            updateFavDB();
            return;
        }

        if (Connection.checkConnection(getContext())) {
            LoadData();
        }
    }

    private void LoadData() {
        ApiClient.MovieDBApi movieDBApi = ApiClient.getMovieDBApi();
        Call<MovieResponse> call = movieDBApi.getMovieList(SORT[sortOrder], mPage);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                Log.d(TAG, "Load movie list : " + String.valueOf(response.isSuccessful()));
                loadFinished(response.body().getResults());
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Log.d(TAG, "load Movie list failed : "+ t.getStackTrace().toString());
            }
        });
    }

    private void loadFinished(List<Movie> data) {
        if (data != null && !data.isEmpty()) {
            if (mOrderChanged) {
                movies.clear();
                mOrderChanged = false;
                mAdapter.setHeaderTitle(BAR_TITLE[sortOrder]);
            }
            movies.addAll(data);
            mAdapter.notifyDataSetChanged();
        }
    }

    private void updateFavDB() {
        ContentResolver resolver = getContext().getContentResolver();
        Cursor cursor =
                resolver.query(MovieProvider.MOVIE_URI,
                        null,
                        null,
                        null,
                        null);
        ArrayList<Movie> data = new ArrayList<>();
        while (cursor.moveToNext()) {
            data.add(new Movie(cursor.getString(cursor.getColumnIndex(MovieProvider.KEY_COLUMN_IMG)),
                    cursor.getString(cursor.getColumnIndex(MovieProvider.KEY_COLUMN_TITLE)),
                    cursor.getString(cursor.getColumnIndex(MovieProvider.KEY_COLUMN_PLOT)),
                    cursor.getDouble(cursor.getColumnIndex(MovieProvider.KEY_COLUMN_RATING)),
                    cursor.getString(cursor.getColumnIndex(MovieProvider.KEY_COLUMN_RELDATE)),
                    cursor.getInt(cursor.getColumnIndex(MovieProvider.KEY_COLUMN_MOVIE_ID)),
                    cursor.getString(cursor.getColumnIndex(MovieProvider.KEY_COLUMN_BACKDROP)),
                    stringToList(cursor.getString(cursor.getColumnIndex(MovieProvider.KEY_COLUMN_GENRE)))));
        }
        cursor.close();
        movies.clear();
        mAdapter.setHeaderTitle(BAR_TITLE[sortOrder]);
        movies.addAll(data);
        mAdapter.notifyDataSetChanged();
    }

    private ArrayList<Integer> stringToList(String data) {
        String[] array = data.split(",");
        List<Integer> genreIds = new ArrayList<>();
        for(String genre : array){
            genreIds.add(Integer.valueOf(genre));
        }
        return (ArrayList<Integer>) genreIds;
    }

    @Override
    public void onClick(View view, int position, int type) {
        listener.onItemSelected(movies.get(position));
    }

    public void Sort(int item) {
        if (sortOrder != item) {
            sortOrder = item;
            mOrderChanged = true;
            mPage = 1;
            updateMovies();
        }
    }

    public interface OnListItemSelectedListener {
        public void onItemSelected(Movie movie);
    }


}
