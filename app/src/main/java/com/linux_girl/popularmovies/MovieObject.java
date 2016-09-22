package com.linux_girl.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Lani on 8/26/2016.
 */
public class MovieObject implements Parcelable {
    String movieId;
    String movieTitle;
    String moviePlot;
    String releaseDate;
    String imageUrl;
    String userRating;
    String mBackDrop;

    public MovieObject() {
    }

    public MovieObject(String mId, String mTitle, String mPlot,
                       String rDate, String uRatings, String mPath, String backdrop) {
        this.movieId = mId;
        this.movieTitle = mTitle;
        this.moviePlot = mPlot;
        this.releaseDate = rDate;
        this.userRating = uRatings;
        this.imageUrl = mPath;
        this.mBackDrop = backdrop;
    }

    public MovieObject(Parcel in) {
        movieId = in.readString();
        movieTitle = in.readString();
        moviePlot = in.readString();
        releaseDate = in.readString();
        userRating = in.readString();
        imageUrl = in.readString();
        mBackDrop = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(movieId);
        parcel.writeString(movieTitle);
        parcel.writeString(moviePlot);
        parcel.writeString(releaseDate);
        parcel.writeString(userRating);
        parcel.writeString(imageUrl);
        parcel.writeString(mBackDrop);
    }

    public static final Parcelable.Creator<MovieObject> CREATOR
            = new Parcelable.Creator<MovieObject>() {
                public MovieObject createFromParcel(Parcel in) {
                    return new MovieObject(in);
                }

                public MovieObject[] newArray(int size) {
                    return new MovieObject[size];
                }
            };
}
