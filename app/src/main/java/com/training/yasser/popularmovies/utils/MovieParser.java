package com.training.yasser.popularmovies.utils;

import com.training.yasser.popularmovies.models.Movie;
import com.training.yasser.popularmovies.interfaces.Parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yasser on 16/08/2016.
 */
public class MovieParser extends Parser {
    @Override
    public List<Movie> Parse(String data) {
        ArrayList<Movie> movies = new ArrayList<>();
        try {
            JSONObject root = new JSONObject(data);
            JSONArray moviesArray = root.getJSONArray("results");
            for (int i = 0; i < moviesArray.length(); i++){
                JSONObject movie = moviesArray.getJSONObject(i);
                JSONArray genres = movie.getJSONArray("genre_ids");
                ArrayList<Integer> list = new ArrayList<>();

                for (int j=0; j<genres.length(); j++) {
                    list.add(genres.getInt(j));
                }

                movies.add(new Movie(movie.getString("poster_path"),
                        movie.getString("original_title"),
                        movie.getString("overview"),
                        movie.getDouble("vote_average"),
                        movie.getString("release_date"),
                        movie.getInt("id"),
                        movie.getString("backdrop_path"),
                        list));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movies;
    }
}
