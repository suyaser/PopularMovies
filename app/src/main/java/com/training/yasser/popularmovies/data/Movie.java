package com.training.yasser.popularmovies.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yasser on 17/07/2016.
 */
public class Movie implements Parcelable{
    private static final String BASE_URL = "http://image.tmdb.org/t/p/w185";
    private String mImg;
    private String mTitle;
    private String mPlot;
    private int mRating;
    private String mRelDate;

    public Movie(String mImg, String mTitle, String mPlot, int mRating, String mRelDate) {
        this.mRelDate = mRelDate;
        this.mImg = mImg;
        this.mTitle = mTitle;
        this.mPlot = mPlot;
        this.mRating = mRating;
    }

    protected Movie(Parcel in) {
        mImg = in.readString();
        mTitle = in.readString();
        mPlot = in.readString();
        mRating = in.readInt();
        mRelDate = in.readString();
    }

    public String getmImg() {
        return BASE_URL + mImg;
    }

    public void setmImg(String mImg) {
        this.mImg = mImg;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmPlot() {
        return mPlot;
    }

    public void setmPlot(String mPlot) {
        this.mPlot = mPlot;
    }

    public int getmRating() {
        return mRating;
    }

    public void setmRating(int mRating) {
        this.mRating = mRating;
    }

    public String getmRelDate() {
        return mRelDate;
    }

    public void setmRelDate(String mRelDate) {
        this.mRelDate = mRelDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mImg);
        dest.writeString(mTitle);
        dest.writeString(mPlot);
        dest.writeInt(mRating);
        dest.writeString(mRelDate);
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
