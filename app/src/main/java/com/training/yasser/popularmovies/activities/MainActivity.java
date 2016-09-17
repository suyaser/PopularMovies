package com.training.yasser.popularmovies.activities;


import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

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

public class MainActivity extends AppCompatActivity implements ListFragment.OnListItemSelectedListener, SortDialogFragment.NoticeDialogListener {

    private static final String FRAGMENT_TAG = "fragment tag";
    private boolean isTwoPane = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.mainBar);
        setSupportActionBar(toolbar);

        determinePaneLayout();
    }

    @Override
    public void onItemSelected(Movie movie) {
        if (isTwoPane) {
            DetailFragment fragmentItem = DetailFragment.newInstance(movie, isTwoPane);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.DetailFragment, fragmentItem);
            ft.commit();
        } else {
            Intent i = new Intent(this, DetailActivity.class);
            i.putExtra(DetailFragment.MOVIE_TAG, movie);
            startActivity(i);
        }
    }


    private void determinePaneLayout() {
        FrameLayout fragmentItemDetail = (FrameLayout) findViewById(R.id.DetailFragment);
        if (fragmentItemDetail != null) {
            isTwoPane = true;
        }
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
        ((ListFragment)getSupportFragmentManager().findFragmentById(R.id.fragmentItemsList)).Sort(item);
    }
}
