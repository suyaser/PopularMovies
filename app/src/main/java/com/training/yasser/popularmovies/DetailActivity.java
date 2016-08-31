package com.training.yasser.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.training.yasser.popularmovies.data.Movie;

public class DetailActivity extends AppCompatActivity {

    public static final String TAG = "DETAIL_ACTIVITY";
    private Movie mMovie;
    private ImageView mPoster;
    private TextView mTitle;
    private TextView mPlot;
    private TextView mRating;
    private TextView mDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.detailBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        mPoster = (ImageView)findViewById(R.id.poster);
        mTitle = (TextView)findViewById(R.id.title);
        mPlot = (TextView)findViewById(R.id.plot);
        mRating = (TextView)findViewById(R.id.rating);
        mDate = (TextView)findViewById(R.id.date);

        mMovie = getIntent().getParcelableExtra(TAG);
        populateView();
    }

    private void populateView() {
        Glide
                .with(this)
                .load(mMovie.getmImg())
                .placeholder(R.drawable.placeholder)
                .fitCenter()
                .dontAnimate()
                .into(mPoster);
        mTitle.setText(mMovie.getmTitle());
        mPlot.setText(mMovie.getmPlot());
        mRating.setText(Integer.toString(mMovie.getmRating()));
        mDate.setText(mMovie.getmRelDate());
    }

}
