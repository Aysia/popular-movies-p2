package com.linux_girl.popularmovies.data;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.linux_girl.popularmovies.R;
import com.linux_girl.popularmovies.Utility;


public class DatabaseAdapter extends CursorAdapter {
    // CursorAdapter will handle all the moveToFirst(), getCount() logic for you :)

    public DatabaseAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, 0);
    }

    // The newView method is used to inflate a new view and return it,
    // you don't bind any data to the view at this point.
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.movie_item, parent, false);
    }

    // The bindView method is used to bind all data to a given view
    // such as setting the text on a TextView.
    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        View listMoviesView = view;

        // Find the ImageView
        ImageView imageView = (ImageView) listMoviesView.findViewById(R.id.imageView);

        byte[] posterPath = cursor.getBlob(cursor.getColumnIndex(DatabaseContract.Favorites.MOVIE_POSTER));
        imageView.setImageBitmap(Utility.getImage(posterPath));

    }

    public static final int getCursorPosition(Cursor cursor) {
        return cursor.getPosition();
    }
}
