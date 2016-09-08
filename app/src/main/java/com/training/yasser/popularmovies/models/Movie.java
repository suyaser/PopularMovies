package com.training.yasser.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by yasser on 17/07/2016.
 */
public class Movie implements Parcelable{
    private static final String BASE_URL = "http://image.tmdb.org/t/p/w185";
    private static final String BASE_URL_HIGH = "http://image.tmdb.org/t/p/w500";
    private String mImg;
    private String mTitle;
    private String mPlot;
    private double mRating;
    private String mRelDate;
    private int mId;
    private String mBackDrop;
    private ArrayList<Integer> mGenre;

    public Movie(String mImg, String mTitle, String mPlot, double mRating, String mRelDate, int mId, String mBackDrop, ArrayList<Integer> mGenre) {
        this.mRelDate = mRelDate;
        this.mImg = mImg;
        this.mTitle = mTitle;
        this.mPlot = mPlot;
        this.mRating = mRating;
        this.mId = mId;
        this.mBackDrop = mBackDrop;
        this.mGenre = mGenre;
    }

    protected Movie(Parcel in) {
        mImg = in.readString();
        mTitle = in.readString();
        mPlot = in.readString();
        mRating = in.readDouble();
        mRelDate = in.readString();
        mId = in.readInt();
        mBackDrop = in.readString();
        mGenre = (ArrayList<Integer>)in.readSerializable();
    }

    public String getmImg() {
        return BASE_URL + mImg;
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmPlot() {
        return mPlot;
    }

    public double getmRating() {
        return mRating;
    }

    public String getmRelDate() {
        return mRelDate;
    }

    public int getmId() {
        return mId;
    }

    public String getmBackDrop() {
        return BASE_URL_HIGH + mBackDrop;
    }

    public ArrayList<Integer> getmGenre() {
        return mGenre;
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
        dest.writeDouble(mRating);
        dest.writeString(mRelDate);
        dest.writeInt(mId);
        dest.writeString(mBackDrop);
        dest.writeSerializable(mGenre);
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
