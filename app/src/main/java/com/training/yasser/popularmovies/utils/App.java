package com.training.yasser.popularmovies.utils;

import android.app.Application;

import com.bumptech.glide.request.target.ViewTarget;
import com.training.yasser.popularmovies.R;

/**
 * Created by yasser on 18/07/2016.
 */
public class App extends Application {
        @Override public void onCreate() {
            super.onCreate();
            ViewTarget.setTagId(R.id.glide_tag);
        }
    }
