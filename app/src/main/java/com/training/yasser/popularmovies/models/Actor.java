package com.training.yasser.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yasser on 08/09/2016.
 */
public class Actor implements Parcelable {
    private static final String BASE_URL = "http://image.tmdb.org/t/p/w185";
    private String mId;
    private String mCharacter;
    private String mName;
    private String mProfPic;

    public Actor(String id, String character, String name, String profPic) {
        mId = id;
        mCharacter = character;
        mName = name;
        mProfPic = profPic;
    }

    public Actor(Parcel in) {
        mId = in.readString();
        mCharacter = in.readString();
        mName = in.readString();
        mProfPic = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeString(mCharacter);
        dest.writeString(mName);
        dest.writeString(mProfPic);
    }

    public String getmId() {
        return mId;
    }

    public String getmCharacter() {
        return mCharacter;
    }

    public String getmName() {
        return mName;
    }

    public String getmProfPic() {
        return BASE_URL + mProfPic;
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
