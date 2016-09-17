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
import com.training.yasser.popularmovies.fragments.DetailFragment;
import com.training.yasser.popularmovies.fragments.ListFragment;
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

public class MainActivity extends AppCompatActivity implements ListFragment.OnListItemSelectedListener{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onItemSelected(Movie movie) {
        Intent i = new Intent(this, DetailActivity.class);
        i.putExtra(DetailFragment.MOVIE_TAG, movie);
        startActivity(i);
    }
}
