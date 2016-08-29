package com.training.yasser.popularmovies.utils;


import com.training.yasser.popularmovies.data.Movie;

import java.util.List;

/**
 * Created by yasser on 04/08/2016.
 */
public abstract class Parser<T> {
    public abstract List<T> Parse(String data);
}
