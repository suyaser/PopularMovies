package com.training.yasser.popularmovies.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.training.yasser.popularmovies.R;
import com.training.yasser.popularmovies.fragments.DetailFragment;
import com.training.yasser.popularmovies.models.Movie;

public class DetailActivity extends AppCompatActivity{
    private DetailFragment detailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Movie movie = (Movie) getIntent().getParcelableExtra(DetailFragment.MOVIE_TAG);
        if (savedInstanceState == null) {
            detailFragment = DetailFragment.newInstance(movie, false);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.DetailFragment, detailFragment);
            ft.commit();
        }
    }
}