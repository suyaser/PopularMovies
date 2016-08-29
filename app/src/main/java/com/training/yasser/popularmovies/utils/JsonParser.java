package com.training.yasser.popularmovies.utils;

import com.training.yasser.popularmovies.data.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yasser on 16/08/2016.
 */
public class JsonParser extends DataHandler {
    @Override
    public List<Movie> handleData(String s) {
        ArrayList<Movie> movies = new ArrayList<>();
        try {
            JSONObject root = new JSONObject(s);
            JSONArray moviesArray = root.getJSONArray("results");
            for (int i = 0; i < moviesArray.length(); i++){
                movies.add(new Movie(moviesArray.getJSONObject(i).getString("poster_path"),
                        moviesArray.getJSONObject(i).getString("original_title"),
                        moviesArray.getJSONObject(i).getString("overview"),
                        moviesArray.getJSONObject(i).getInt("popularity"),
                        moviesArray.getJSONObject(i).getString("release_date")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movies;
    }
}
