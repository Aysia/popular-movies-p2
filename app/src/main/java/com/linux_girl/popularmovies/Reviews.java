package com.linux_girl.popularmovies;

/**
 * Created by Lani on 9/20/2016.
 */
public class Reviews {

    public final String mAuthor;
    public final String mContent;

    /**
     * Construct a new @link Trailer
     */

    public Reviews(String author, String content) {
        this.mAuthor = author;
        this.mContent = content;
    }

    public String getAuthor() {
        return mAuthor;
    }
    public String getReviewContent() {
        return mContent;
    }
}
