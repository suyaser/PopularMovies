package com.training.yasser.popularmovies.utils;

import com.training.yasser.popularmovies.data.Review;
import com.training.yasser.popularmovies.data.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yasser on 29/08/2016.
 */
public class ReviewParser extends Parser {
    @Override
    public List<Review> Parse(String data) {
        ArrayList<Review> reviews = new ArrayList<>();
        try {
            JSONObject root = new JSONObject(data);
            JSONArray reviewArray = root.getJSONArray("results");
            for (int i = 0; i < reviewArray.length(); i++){
                JSONObject review = reviewArray.getJSONObject(i);
                reviews.add(new Review(review.getString("id"),
                        review.getString("author"),
                        review.getString("content"),
                        review.getString("url")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return reviews;
    }
}
