package com.linux_girl.popularmovies;

/**
 * Created by Lani on 9/8/2016.
 */
public class Trailers {

    public final String mName;
    public final String mKey;

    /**
     * Construct a new @link Trailer
     */

    public Trailers(String name, String key) {
        this.mName = name;
        this.mKey = key;
    }

    public String getTrailerName() {
        return mName;
    }
    public String getTrailerKey() {
        return mKey;
    }

}
