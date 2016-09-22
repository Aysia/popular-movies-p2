package com.linux_girl.popularmovies.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
                DatabaseContract.Favorites.FMOVIE_ID,
        };
        // Define 'where' part of query.
        String selection = DatabaseContract.Favorites.FMOVIE_ID + " LIKE ?";
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
    public Cursor getFavorites(String selection, String[] selectionArgs) {
        SQLiteDatabase db = getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                DatabaseContract.Favorites.FMOVIE_ID,
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
        String selection = DatabaseContract.Favorites.FMOVIE_ID + " LIKE ?";
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
