package com.linux_girl.popularmovies.data;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import static java.sql.Types.BLOB;

public class DatabaseContract {
    /**
     * When a change is made to the database, increment the DATABASE_VERSION
     */
    public static final int DATABASE_VERSION = 6;
    // Name of the Database
    public static final String DATABASE_NAME = "favorites.db";

    private static final String BLOB_TYPE = " BLOB";
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ", ";
    private static final String DOUBLE_TYPE = " DOUBLE";


    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor. Not sure that I would keep this, as I
    // utilize these constants everywhere and would prefer to instantiate it on each activity
    private DatabaseContract() {
    }

    public static abstract class Favorites implements BaseColumns {
        // Table Name
        public static final String TABLE_NAME = "FavoritesTable";

        // Table constants for Favorites
        public static final String FMOVIE_ID = "movieId";
        public static final String MOVIE_TITLE = "movie_title";
        public static final String MOVIE_POSTER = "movie_poster";
        public static final String MOVIE_PLOT = "synopsis";
        public static final String MOVIE_RATING = "user_rating";
        public static final String RELEASE_DATE = "release_date";
        public static final String MOVIE_BACKDROP = "backdrop";

        public static final String CREATE_FAVORITES_TABLE = "CREATE TABLE " +
                TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY, " +
                FMOVIE_ID + TEXT_TYPE +  COMMA_SEP +
                MOVIE_TITLE + TEXT_TYPE + COMMA_SEP +
                MOVIE_POSTER + BLOB_TYPE + COMMA_SEP +
                MOVIE_PLOT + TEXT_TYPE + COMMA_SEP +
                MOVIE_RATING + DOUBLE_TYPE + COMMA_SEP +
                MOVIE_BACKDROP + BLOB + COMMA_SEP +
                RELEASE_DATE + TEXT_TYPE + " )";

        /**
         * Constant to delete the tables
         */
        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

        // Delete database
        public static final String DELETE_DATABASE = "DROP DATABASE IF EXISTS " + DATABASE_NAME;
    }

}
