package com.training.yasser.popularmovies.utils;

import com.training.yasser.popularmovies.data.Movie;
import com.training.yasser.popularmovies.data.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yasser on 29/08/2016.
 */
public class TrailerParser extends Parser {
    @Override
    public List<Trailer> Parse(String data) {
        ArrayList<Trailer> trailers = new ArrayList<>();
        try {
            JSONObject root = new JSONObject(data);
            JSONArray trailerArray = root.getJSONArray("results");
            for (int i = 0; i < trailerArray.length(); i++){
                JSONObject trailer = trailerArray.getJSONObject(i);
                trailers.add(new Trailer(trailer.getString("id"),
                        trailer.getString("iso_639_1"),
                        trailer.getString("key")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return trailers;
    }
}
