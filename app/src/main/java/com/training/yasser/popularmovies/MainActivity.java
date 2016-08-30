package com.training.yasser.popularmovies;


import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.training.yasser.popularmovies.data.Movie;
import com.training.yasser.popularmovies.utils.Connection;
import com.training.yasser.popularmovies.utils.DataLoader;
import com.training.yasser.popularmovies.utils.EndlessScrollListner;
import com.training.yasser.popularmovies.utils.GridAdapter;
import com.training.yasser.popularmovies.utils.MovieParser;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Movie>>, SortDialogFragment.NoticeDialogListener, GridAdapter.ClickListener {
    private final static String MOVIES_STATE = "MoviesState";
    private final static String[] SORT = {"popular", "top_rated"};
    private final static String[] BAR_TITLE = {"popular", "top rated"};
    private static final String LIST_STATE = "ListState";
    private RecyclerView mRecyclerView;
    private GridAdapter mAdapter;
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
        mAdapter = new GridAdapter(this, movies);
        mAdapter.setOnClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        boolean landscape = this.getResources().getBoolean(R.bool.is_landscape);
        if(landscape){
            mRecyclerView.setLayoutManager(new GridLayoutManager(this,4));
        }else{
            mRecyclerView.setLayoutManager(new GridLayoutManager(this,3));
        }

        mRecyclerView.addOnScrollListener(new EndlessScrollListner((GridLayoutManager)mRecyclerView.getLayoutManager()) {
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
        getSupportActionBar().setTitle(BAR_TITLE[sortOrder]);
        if (Connection.checkConnection(this)) {
            LoaderManager loaderManager = getSupportLoaderManager();
            loaderManager.restartLoader(0, null, this);
        }
    }


    @Override
    public void onClick(View view, int position) {
        Intent i = new Intent(this, DetailActivity.class);
        i.putExtra(DetailActivity.TAG, movies.get(position));
        startActivity(i);
    }

    @Override
    public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {
        Resources resources = getResources();
        DataLoader<Movie> loader = new DataLoader(this, resources.getString(R.string.base_url) + SORT[sortOrder] +
                resources.getString(R.string.page) + mPage + resources.getString(R.string.api_key));
        loader.setParser(new MovieParser());
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> data) {
        if(data != null && !data.isEmpty()){
            if(mOrderChanged){
                movies.clear();
                mOrderChanged = false;
            }
            movies.addAll(data);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {

    }
}
