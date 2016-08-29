package com.training.yasser.popularmovies;


import android.content.Intent;
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
import com.training.yasser.popularmovies.utils.GridAdapter;
import com.training.yasser.popularmovies.utils.MovieLoader;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Movie>>, SortDialogFragment.NoticeDialogListener, GridAdapter.ClickListener {
    private final static String MOVIES_STATE = "MoviesState";
    private final static String[] SORT = {"popular", "top_rated"};
    private final static String[] BAR_TITLE = {"popular", "top rated"};
    private static final String LIST_STATE = "ListState";
    private static final String BASE_URL = "http://api.themoviedb.org/3/movie/";
    private static final String API_KEY = "?api_key=336043898c0bea0b096943d0349c541c";
    private RecyclerView mRecyclerView;
    GridAdapter mAdapter;
    ArrayList<Movie> movies;
    int sortOrder;
    Parcelable state = null;

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
        boolean landscape = this.getResources().getBoolean(R.bool.is_landscape);
        if(landscape){
            mRecyclerView.setLayoutManager(new GridLayoutManager(this,3));
        }else{
            mRecyclerView.setLayoutManager(new GridLayoutManager(this,2));
        }

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
            updateMovies();
        }
    }

    private void updateMovies() {
        getSupportActionBar().setTitle(BAR_TITLE[sortOrder]);
        if (Connection.checkConnection(this)) {
            LoaderManager loaderManager = getSupportLoaderManager();
            loaderManager.initLoader(0, null, this);
        }
    }


    @Override
    public void onClick(View view, int position) {
        Log.d("MainACtivity", "OnCLick");
        Intent i = new Intent(this, DetailActivity.class);
        i.putExtra(DetailActivity.TAG, movies.get(position));
        startActivity(i);
    }

    @Override
    public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {
        return new MovieLoader(this, BASE_URL+SORT[sortOrder]+API_KEY);
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> data) {
        if(data != null && !data.isEmpty()){
            mAdapter.swap(data);
    }

    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {

    }
}
