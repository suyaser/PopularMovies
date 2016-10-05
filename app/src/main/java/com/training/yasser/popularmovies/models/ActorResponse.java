package com.training.yasser.popularmovies.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by yasser on 05/10/2016.
 */

public class ActorResponse {
    @SerializedName("cast")
    private List<Actor> actors;

    public List<Actor> getActors() {
        return actors;
    }

    public void setActors(List<Actor> actors) {
        this.actors = actors;
    }
}
