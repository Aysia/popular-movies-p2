package com.linux_girl.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Lani on 9/15/2016.
 */
public class TrailerObject implements Parcelable {
    String mName;
    String mKey;

    public TrailerObject() {
    }

    public TrailerObject(String name, String key) {
        this.mName = name;
        this.mKey = key;
    }

    public TrailerObject(Parcel in) {
        mName = in.readString();
        mKey = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mName);
        parcel.writeString(mKey);
    }

    public final Parcelable.Creator<TrailerObject> CREATOR
            = new Parcelable.Creator<TrailerObject>() {
        public TrailerObject createFromParcel(Parcel in) {
            return new TrailerObject(in);
        }

        public TrailerObject[] newArray(int size) {
            return new TrailerObject[size];
        }
    };
}