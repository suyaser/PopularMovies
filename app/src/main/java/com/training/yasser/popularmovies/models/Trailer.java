package com.training.yasser.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yasser on 29/08/2016.
 */
public class Trailer implements Parcelable {
    private String mId;
    private String mLang;
    private String mKey;

    public Trailer(String id, String lang, String key) {
        mId = id;
        mLang = lang;
        mKey = key;
    }

    public Trailer(Parcel in) {
        mId = in.readString();
        mLang = in.readString();
        mKey = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeString(mLang);
        dest.writeString(mKey);
    }

    public String getmId() {
        return mId;
    }

    public String getmLang() {
        return mLang;
    }

    public String getmKey() {
        return mKey;
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
        return "http://img.youtube.com/vi/" + mKey + "/1.jpg";
    }
}
