package com.training.yasser.popularmovies.interfaces;


import com.training.yasser.popularmovies.models.Movie;

import java.util.List;

/**
 * Created by yasser on 04/08/2016.
 */
public abstract class DataHandler {
    public abstract List<Movie> handleData(String data);
}
