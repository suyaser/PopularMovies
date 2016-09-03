package com.training.yasser.popularmovies.utils;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;

import com.training.yasser.popularmovies.interfaces.Parser;
import com.training.yasser.popularmovies.network.Connection;

import java.util.List;

/**
 * Created by yasser on 16/08/2016.
 */
public class DataLoader<T> extends AsyncTaskLoader<List<T>> {
    private Connection mConnection;
    private Parser parser;
    public DataLoader(Context context, String requestUrl) {
        super(context);
        mConnection = new Connection(context, requestUrl);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<T> loadInBackground() {
        String jsonResponse = mConnection.Download();
        if(jsonResponse == null || jsonResponse.isEmpty()){
            return null;
        }
        return parser.Parse(jsonResponse);
    }

    public void setParser(Parser parser) {
        this.parser = parser;
    }
}

