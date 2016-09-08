package com.training.yasser.popularmovies.utils;

import com.training.yasser.popularmovies.interfaces.Parser;
import com.training.yasser.popularmovies.models.Actor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yasser on 08/09/2016.
 */
public class ActorParser extends Parser {
    @Override
    public List<Actor> Parse(String data) {
        ArrayList<Actor> actors = new ArrayList<>();
        try {
            JSONObject root = new JSONObject(data);
            JSONArray actorArray = root.getJSONArray("cast");
            for (int i = 0; i < actorArray.length(); i++){
                JSONObject actor = actorArray.getJSONObject(i);
                actors.add(new Actor(actor.getString("id"),
                        actor.getString("character"),
                        actor.getString("name"),
                        actor.getString("profile_path")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return actors;
    }
}