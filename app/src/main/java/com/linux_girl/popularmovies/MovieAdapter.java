package com.linux_girl.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieAdapter extends ArrayAdapter<Movies> {
    private boolean mCurrentMovie = true;

    /**
     * Create a new {@link MovieAdapter} object.
     */
    public MovieAdapter(Context context, ArrayList<Movies> movies) {
        super(context, 0, movies);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if an existing view is being reused, otherwise inflate the view
        View listMoviesView = convertView;
        if (listMoviesView == null) {
            listMoviesView = LayoutInflater.from(getContext()).inflate(
                    R.layout.movie_item, parent, false);
        }

        // Get the {@link Movies} object located at this position in the list
        Movies currentMovie = getItem(position);

        // Find the ImageView
        ImageView imageView = (ImageView) listMoviesView.findViewById(R.id.imageView);

        String imageUri = "http://image.tmdb.org/t/p/w185/" + currentMovie.getImageUrl();
        Picasso.with(getContext())
                .load(imageUri)
                .into(imageView);

        // Return the whole book item layout (containing 2 TextViews) so that it can be shown in
        // the ListView.
        return listMoviesView;
    }
    public void setCurrentMovie(boolean useCurrentMovie) {
        mCurrentMovie = useCurrentMovie;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }
}

