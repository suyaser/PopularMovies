package com.training.yasser.popularmovies.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import java.util.List;

/**
 * Created by yasser on 01/09/2016.
 */
public class LoaderCallbacks<T> implements LoaderManager.LoaderCallbacks<List<T>> {
    public static final int MOVIE_LOADER_ID = 0;
    public static final int REVIEW_LOADER_ID = 1;
    public static final int TRAILER_LOADER_ID = 2;
    public static final String SORT_ORDER_KEY = "sort order";
    public static final String PAGE_KEY = "page";
    public static final String ID_KEY = "id";
    private static final String BASE_URL = "http://api.themoviedb.org/3/movie/";
    private static final String PAGE = "&page=";
    private static final String API_KEY = "?api_key=336043898c0bea0b096943d0349c541c";
    private static final String TRAILER = "/videos";
    private static final String REVIEW = "/reviews";
    private final LoaderListener<T> listener;


    public LoaderCallbacks(Activity activity) {
        listener = (LoaderListener<T>)activity;
    }

    @Override
    public Loader<List<T>> onCreateLoader(int id, Bundle args) {
        DataLoader<T> loader = null;
        switch (id){
            case MOVIE_LOADER_ID:
                loader = new DataLoader((Context)listener, BASE_URL + args.getString(SORT_ORDER_KEY) + API_KEY +
                        PAGE + args.getInt(PAGE_KEY) );
                loader.setParser(new MovieParser());
                break;
            case REVIEW_LOADER_ID:
                loader = new DataLoader((Context)listener, BASE_URL + args.getInt(ID_KEY) + REVIEW + API_KEY);
                loader.setParser(new ReviewParser());
                break;
            case TRAILER_LOADER_ID:
                loader = new DataLoader((Context)listener, BASE_URL +  args.getInt(ID_KEY) + TRAILER + API_KEY);
                loader.setParser(new TrailerParser());
                break;
        }
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<List<T>> loader, List<T> data) {
                listener.loadFinished(loader.getId(), data);
    }

    @Override
    public void onLoaderReset(Loader<List<T>> loader) {

    }

    public interface LoaderListener<T>{
        void loadFinished(int id, List<T> data);
    }
}
