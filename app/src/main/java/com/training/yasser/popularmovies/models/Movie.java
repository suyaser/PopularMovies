package com.training.yasser.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yasser on 17/07/2016.
 */
public class Movie implements Parcelable{
    private static final String BASE_URL = "http://image.tmdb.org/t/p/w185";
    private static final String BASE_URL_HIGH = "http://image.tmdb.org/t/p/w500";
    private String posterPath;
    private String overview;
    private String releaseDate;
    private List<Integer> genreIds = new ArrayList<Integer>();
    private Integer id;
    private String title;
    private String backdropPath;
    private Double voteAverage;


    public Movie(String mImg, String mTitle, String mPlot, double mRating, String mRelDate, int mId, String mBackDrop, ArrayList<Integer> mGenre) {
        this.posterPath = mImg;
        this.title = mTitle;
        this.overview = mPlot;
        this.voteAverage = mRating;
        this.releaseDate = mRelDate;
        this.id = mId;
        this.backdropPath = mBackDrop;
        this.genreIds = mGenre;
    }

    protected Movie(Parcel in) {
        posterPath = in.readString();
        title = in.readString();
        overview = in.readString();
        voteAverage = in.readDouble();
        releaseDate = in.readString();
        id = in.readInt();
        backdropPath = in.readString();
        genreIds = (ArrayList<Integer>)in.readSerializable();
    }

    public String getPosterPath() {
        return BASE_URL + posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public List<Integer> getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(List<Integer> genreIds) {
        this.genreIds = genreIds;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBackdropPath() {
        return BASE_URL_HIGH + backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(posterPath);
        dest.writeString(title);
        dest.writeString(overview);
        dest.writeDouble(voteAverage);
        dest.writeString(releaseDate);
        dest.writeInt(id);
        dest.writeString(backdropPath);
        dest.writeSerializable((Serializable) genreIds);
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
