package com.training.yasser.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yasser on 29/08/2016.
 */
public class Trailer implements Parcelable {
    private String source;

    public Trailer(String key) {
        source = key;
    }

    public Trailer(Parcel in) {
        source = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(source);
    }

    public String getSource() {
        return source;
    }

    public static final Creator<Trailer> CREATOR = new Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel in) {
            return new Trailer(in);
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };

    public String getTumbnailUrl() {
        return "http://img.youtube.com/vi/" + source + "/1.jpg";
    }
}
