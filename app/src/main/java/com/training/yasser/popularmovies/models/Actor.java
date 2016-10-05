package com.training.yasser.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yasser on 08/09/2016.
 */
public class Actor implements Parcelable {
    private static final String BASE_URL = "http://image.tmdb.org/t/p/w185";
    private String id;
    private String character;
    private String name;
    private String profilePath;

    public Actor(String id, String character, String name, String profPic) {
        this.id = id;
        this.character = character;
        this.name = name;
        profilePath = profPic;
    }

    public Actor(Parcel in) {
        id = in.readString();
        character = in.readString();
        name = in.readString();
        profilePath = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(character);
        dest.writeString(name);
        dest.writeString(profilePath);
    }

    public String getId() {
        return id;
    }

    public String getCharacter() {
        return character;
    }

    public String getName() {
        return name;
    }

    public String getProfilePath() {
        return BASE_URL + profilePath;
    }

    public static final Creator<Actor> CREATOR = new Creator<Actor>() {
        @Override
        public Actor createFromParcel(Parcel in) {
            return new Actor(in);
        }

        @Override
        public Actor[] newArray(int size) {
            return new Actor[size];
        }
    };
}
