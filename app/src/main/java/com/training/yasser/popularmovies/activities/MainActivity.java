package com.training.yasser.popularmovies.activities;


import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.training.yasser.popularmovies.R;
import com.training.yasser.popularmovies.fragments.SortDialogFragment;
import com.training.yasser.popularmovies.interfaces.ClickListener;
import com.training.yasser.popularmovies.models.Movie;
import com.training.yasser.popularmovies.network.Connection;
import com.training.yasser.popularmovies.interfaces.EndlessScrollListner;
import com.training.yasser.popularmovies.adapters.MovieListAdapter;
import com.training.yasser.popularmovies.utils.LoaderCallbacks;
import com.training.yasser.popularmovies.utils.MovieProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SortDialogFragment.NoticeDialogListener, ClickListener, LoaderCallbacks.LoaderListener {
    private final static String MOVIES_STATE = "MoviesState";
    private final static String[] BAR_TITLE = {"Most Popular Movies", "Top Rated Movies", "Favorites"};
    private static final String LIST_STATE = "ListState";
    private RecyclerView mRecyclerView;
    private MovieListAdapter mAdapter;
    private ArrayList<Movie> movies;
    private int sortOrder;
    private Parcelable state = null;
    private int mPage = 1;
    private boolean mOrderChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.mainBar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            movies = new ArrayList<Movie>();
            updateMovies();
        } else {
            state = savedInstanceState.getParcelable(LIST_STATE);
            movies = savedInstanceState.getParcelableArrayList(MOVIES_STATE);
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.gridView);
        mAdapter = new MovieListAdapter(this, movies);
        mAdapter.setHeaderTitle(BAR_TITLE[sortOrder]);
        mRecyclerView.setAdapter(mAdapter);
        final GridLayoutManager manager = new GridLayoutManager(this, getResources().getInteger(R.integer.main_grid_columbs));
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
    protected void onResume() {
        super.onResume();
        updateFavDB();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        state = mRecyclerView.getLayoutManager().onSaveInstanceState();
        outState.putParcelableArrayList(MOVIES_STATE, movies);
        outState.putParcelable(LIST_STATE, state);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sort:
                SortDialogFragment dialog = new SortDialogFragment();
                dialog.show(getSupportFragmentManager(), "Dialog");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onDialogPositiveClick(int item) {
        if (sortOrder != item) {
            sortOrder = item;
            mOrderChanged = true;
            mPage = 1;
            updateMovies();
        }
    }

    private void updateMovies() {
        if(sortOrder == 2){
            updateFavDB();
            return;
        }

        if (Connection.checkConnection(this)) {
            LoaderManager loaderManager = getSupportLoaderManager();
            LoaderCallbacks<Movie> loaderCallbacks = new LoaderCallbacks<>(this);
            Bundle bundle = new Bundle();
            bundle.putInt(LoaderCallbacks.SORT_ORDER_KEY, sortOrder);
            bundle.putInt(LoaderCallbacks.PAGE_KEY, mPage);
            loaderManager.restartLoader(LoaderCallbacks.MOVIE_LOADER_ID, bundle, loaderCallbacks);
        }
    }

    private void updateFavDB() {
        ContentResolver resolver = getContentResolver();
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

    private ArrayList<String> stringToList(String data) {
        String[] array = data.split(",");
        return new ArrayList<String>(Arrays.asList(array));
    }


    @Override
    public void onClick(View view, int position, int type) {
        Intent i = new Intent(this, DetailActivity.class);
        i.putExtra(DetailActivity.TAG, movies.get(position));
        startActivity(i);
    }

    @Override
    public void loadFinished(int id, List data) {
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
}
