package com.training.yasser.popularmovies.utils;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;

import com.training.yasser.popularmovies.data.Movie;

import java.util.List;

/**
 * Created by yasser on 16/08/2016.
 */
public class MovieLoader extends AsyncTaskLoader<List<Movie>> {
    private Connection mConnection;
    public MovieLoader(Context context, String requestUrl) {
        super(context);
        mConnection = new Connection(context, requestUrl);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Movie> loadInBackground() {
        String jsonResponse = mConnection.Download();
        if(jsonResponse == null || jsonResponse.isEmpty()){
            return null;
        }
        JsonParser parser = new JsonParser();
        return parser.handleData(jsonResponse);
    }

}

