package com.linux_girl.popularmovies.data;

import android.provider.BaseColumns;

public class DatabaseContract {
    /**
     * When a change is made to the database, increment the DATABASE_VERSION
     */
    public static final int DATABASE_VERSION = 5;
    // Name of the Database
    public static final String DATABASE_NAME = "favorites.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ", ";

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

        /**
         * create the products table
         */
        public static final String CREATE_FAVORITES_TABLE = "CREATE TABLE " +
                TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY, " +
                FMOVIE_ID + TEXT_TYPE +  " )";

        /**
         * Constant to delete the tables
         */
        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

        // Delete database
        public static final String DELETE_DATABASE = "DROP DATABASE IF EXISTS " + DATABASE_NAME;
    }

}
