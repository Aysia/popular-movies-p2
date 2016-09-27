package com.linux_girl.popularmovies.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.facebook.stetho.common.Util;
import com.linux_girl.popularmovies.Utility;

import java.sql.Blob;
import java.text.SimpleDateFormat;
import java.util.Date;


public class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(Context context) {
        super(context, DatabaseContract.DATABASE_NAME, null,
                DatabaseContract.DATABASE_VERSION);
    }

    // Method is called during creation of the database
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DatabaseContract.Favorites.CREATE_FAVORITES_TABLE);
    }

    // Method is called during an upgrade of the database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DatabaseContract.Favorites.DELETE_TABLE);
        onCreate(db);
    }

    public void deleteDatabase() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(DatabaseContract.Favorites.DELETE_DATABASE);
    }

    public long addFavorite(ContentValues values) {
        SQLiteDatabase db = getWritableDatabase();
        // Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                DatabaseContract.Favorites.TABLE_NAME, null, values);

        return newRowId;
    }
    public boolean isFavorite(String movie_id) {
        SQLiteDatabase db = getReadableDatabase();

        String[] projection = {
                DatabaseContract.Favorites.MOVIE_ID,
        };
        // Define 'where' part of query.
        String selection = DatabaseContract.Favorites.MOVIE_ID + " LIKE ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = {movie_id};

        // Issue SQL statement.
        Cursor cursor = db.query(
                DatabaseContract.Favorites.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }
    public byte[] getPosterCover(String movieId) {
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = { DatabaseContract.Favorites.MOVIE_POSTER };
        String selection = DatabaseContract.Favorites.MOVIE_ID + "=?";
        String[] selectionArgs = { movieId };

        try {
            Cursor cursor = db.query(
                    DatabaseContract.Favorites.TABLE_NAME, columns, selection, selectionArgs, null, null, null
            );
            cursor.moveToFirst();
            byte[] blob = cursor.getBlob(cursor.getColumnIndex(DatabaseContract.Favorites.MOVIE_POSTER));
            cursor.close();
            return blob ;
        } catch (NullPointerException e) {
            return null;
        }

    }
    public Cursor getFavorites(String selection, String[] selectionArgs) {
        SQLiteDatabase db = getReadableDatabase();

        String[] projection = {
                DatabaseContract.Favorites.MOVIE_ID,
                DatabaseContract.Favorites.MOVIE_TITLE,
                DatabaseContract.Favorites.MOVIE_POSTER,
                DatabaseContract.Favorites.MOVIE_RATING,
                DatabaseContract.Favorites.RELEASE_DATE,
                DatabaseContract.Favorites.MOVIE_PLOT
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder = DatabaseContract.Favorites._ID + " ASC";


        Cursor cursor = db.query(
                DatabaseContract.Favorites.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder
        );
        return cursor;

    }

    // Delete row based on MOVIE_ID
    public void unFavorite(String movie_id) {
        SQLiteDatabase db = getWritableDatabase();
        // Define 'where' part of query.
        String selection = DatabaseContract.Favorites.MOVIE_ID + " LIKE ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = { movie_id};
        // Issue SQL statement.
        db.delete(DatabaseContract.Favorites.TABLE_NAME, selection, selectionArgs);
    }

    // Delete all data in the database
    public void deleteTablesData() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + DatabaseContract.Favorites.TABLE_NAME);
    }

    public String getCurrentTimestamp() {
        String currentTimeStamp =
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        return currentTimeStamp;
    }
}
