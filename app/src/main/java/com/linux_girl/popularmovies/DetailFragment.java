package com.linux_girl.popularmovies;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.linux_girl.popularmovies.data.DatabaseContract;
import com.linux_girl.popularmovies.data.DatabaseHelper;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Field;

public class DetailFragment extends Fragment {

    // Tag for Logs
    String LOG_TAG = DetailFragment.class.getSimpleName();

    private TextView mTitleView;
    private ImageView mImageView;
    private TextView mPlotView;
    private TextView mDateView;
    private TextView mRatingView;
    private FloatingActionButton mActionButton;
    public MovieObject object;
    private CustomLayout mBackDropLayout;

    final static String KEY_POSITION = "position";
    int mCurrentPosition = -1;
    static String MOVIE_EXTRA = "extra";
    static String MOVIE_PREF_ID = "movie_id";

    final String date_prefix = "Release Date: ";
    final String rating_suffix = "/10";
    final String rating_prefix = "User Ratings: ";
    DetailActivity detailActivity = new DetailActivity();

    public DetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        mTitleView = (TextView) rootView.findViewById(R.id.movie_title);
        mImageView = (ImageView) rootView.findViewById(R.id.imageView);
        mPlotView = (TextView) rootView.findViewById(R.id.movie_plot);
        mDateView = (TextView) rootView.findViewById(R.id.release_date);
        mRatingView = (TextView) rootView.findViewById(R.id.user_ratings);

        mActionButton = (FloatingActionButton) rootView.findViewById(R.id.fave_button);
        mBackDropLayout = (CustomLayout) rootView.findViewById(R.id.movie_backdrop);

        Bundle arguments = getArguments();

        if (arguments != null) {
            // Update Details
            object = arguments.getParcelable(MOVIE_EXTRA);
            updateDetails(object);

        } else if (mCurrentPosition != -1) {
            // Set description based on savedInstanceState defined during onCreateView()
            updateDetails(object);
        }

        if (savedInstanceState != null) {
            mCurrentPosition = savedInstanceState.getInt(KEY_POSITION);
        }

        return rootView;
    }


    public void updateDetails(final MovieObject movie) {
        final DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        final ContentValues values = new ContentValues();

        mActionButton.setOnClickListener(new AdapterView.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(dbHelper.isFavorite(movie.movieId)) {
                    dbHelper.unFavorite(movie.movieId);
                } else {
                    //TODO: Add Movie Poster Blob bitmap and backdrop
                    values.put(DatabaseContract.Favorites.FMOVIE_ID, movie.movieId);
                    values.put(DatabaseContract.Favorites.MOVIE_TITLE, movie.movieTitle);
                    values.put(DatabaseContract.Favorites.MOVIE_PLOT, movie.moviePlot);
                    values.put(DatabaseContract.Favorites.MOVIE_RATING, movie.userRating);
                    values.put(DatabaseContract.Favorites.RELEASE_DATE, movie.releaseDate);
                    dbHelper.addFavorite(values);
                }
                refreshFragment();
            }
        });

        if (dbHelper.isFavorite(movie.movieId)) {
            mActionButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.googleGold)));

        }

        String backDropUrl = "http://image.tmdb.org/t/p/w1920/" + movie.mBackDrop;

        Picasso.with(getContext())
                .load(backDropUrl)
                .into(mBackDropLayout);

        mTitleView.setText(movie.movieTitle);

        String imageUri = "http://image.tmdb.org/t/p/w185/" + movie.imageUrl;
        Picasso.with(getContext())
                .load(imageUri)
                .into(mImageView);

        mPlotView.setText(movie.moviePlot);
        String release_date = date_prefix + movie.releaseDate;
        mDateView.setText(release_date);
        String formatted_ratings = rating_prefix + movie.userRating + rating_suffix;
        mRatingView.setText(formatted_ratings);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save the current description selection in case we need to recreate the fragment
        outState.putInt(KEY_POSITION, mCurrentPosition);
    }

    public void refreshFragment() {
        super.onResume();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }

    @Override
    public void onDetach() {
        super.onDetach();

        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}