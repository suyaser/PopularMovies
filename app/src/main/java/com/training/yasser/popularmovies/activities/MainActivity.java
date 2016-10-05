package com.training.yasser.popularmovies.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.training.yasser.popularmovies.R;
import com.training.yasser.popularmovies.fragments.DetailFragment;
import com.training.yasser.popularmovies.fragments.ListFragment;
import com.training.yasser.popularmovies.fragments.SortDialogFragment;
import com.training.yasser.popularmovies.models.Movie;

public class MainActivity extends AppCompatActivity implements ListFragment.OnListItemSelectedListener, SortDialogFragment.NoticeDialogListener {

    private boolean isTwoPane = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.mainBar);
        setSupportActionBar(toolbar);

        isTwoPane();
    }

    @Override
    public void onItemSelected(Movie movie) {
        if (isTwoPane) {
            selectMovieTwoPane(movie);
        } else {
            selectMovieOnePane(movie);
        }
    }

    private void selectMovieOnePane(Movie movie) {
        Intent i = new Intent(this, DetailActivity.class);
        i.putExtra(DetailFragment.MOVIE_TAG, movie);
        startActivity(i);
    }

    private void selectMovieTwoPane(Movie movie) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(DetailFragment.MOVIE_TAG, movie);
        args.putBoolean(DetailFragment.Twopane_TAG, isTwoPane);
        fragment.setArguments(args);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.DetailFragment, fragment);
        ft.commit();
    }


    private void isTwoPane() {
        //check if detail fragment exist
        FrameLayout fragmentItemDetail = (FrameLayout) findViewById(R.id.DetailFragment);
        if (fragmentItemDetail != null) {
            isTwoPane = true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sort:
                //open sort options dialog
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
        //change movie sort order inside movie fragment
        ((ListFragment)getSupportFragmentManager().findFragmentById(R.id.fragmentItemsList)).Sort(item);
    }
}
