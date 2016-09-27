package com.linux_girl.popularmovies;

import android.content.ContentValues;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.linux_girl.popularmovies.data.DatabaseContract;
import com.linux_girl.popularmovies.data.DatabaseHelper;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.linux_girl.popularmovies.R.id.imageView;

public class DetailFragment extends Fragment {

    String LOG_TAG = DetailFragment.class.getSimpleName();

    private TextView mTitleView;
    private ImageView mImageView;
    private TextView mPlotView;
    private TextView mDateView;
    private TextView mRatingView;
    private FloatingActionButton mActionButton;
    public MovieObject object;

    final static String KEY_POSITION = "position";
    int mCurrentPosition = -1;
    static String MOVIE_EXTRA = "extra";

    final String date_prefix = "Release Date: ";
    final String rating_suffix = "/10";
    final String rating_prefix = "User Ratings: ";

    public DetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        mTitleView = (TextView) rootView.findViewById(R.id.movie_title);
        mImageView = (ImageView) rootView.findViewById(imageView);
        mPlotView = (TextView) rootView.findViewById(R.id.movie_plot);
        mDateView = (TextView) rootView.findViewById(R.id.release_date);
        mRatingView = (TextView) rootView.findViewById(R.id.user_ratings);

        mActionButton = (FloatingActionButton) rootView.findViewById(R.id.fave_button);
        //mBackDropLayout = (CustomLayout) rootView.findViewById(R.id.movie_backdrop);

        Bundle arguments = getArguments();

        if (arguments != null) {
            // Update Details
            object = arguments.getParcelable(MOVIE_EXTRA);
            updateDetails(object);

        } else if (mCurrentPosition != -1) {
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
                if (dbHelper.isFavorite(movie.movieId)) {
                    dbHelper.unFavorite(movie.movieId);
                    EventBus.getDefault().post(new MessageEvent("Removed from Favorites"));
                } else {
                    String uri = "http://image.tmdb.org/t/p/w185/" + movie.imageUrl;
                    byte[] moviePoster = Utility.getBytes(uri);

                    values.put(DatabaseContract.Favorites.MOVIE_ID, movie.movieId);
                    values.put(DatabaseContract.Favorites.MOVIE_TITLE, movie.movieTitle);
                    values.put(DatabaseContract.Favorites.MOVIE_PLOT, movie.moviePlot);
                    values.put(DatabaseContract.Favorites.MOVIE_RATING, movie.userRating);
                    values.put(DatabaseContract.Favorites.RELEASE_DATE, movie.releaseDate);
                    values.put(DatabaseContract.Favorites.MOVIE_POSTER, moviePoster);

                    dbHelper.addFavorite(values);
                    EventBus.getDefault().post(new MessageEvent("Saved to Favorites"));
                }
                refreshFragment();
            }
        });

        if (dbHelper.isFavorite(movie.movieId)) {
            mActionButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.googleGold)));
        }

        mTitleView.setText(movie.movieTitle);

        if (movie.imageUrl == "null") {
            String movieId = movie.movieId;
            try {
                byte[] blob = dbHelper.getPosterCover(movieId);
                mImageView.setImageBitmap(Utility.getImage(blob));
            } catch (IndexOutOfBoundsException e) {
                mImageView.setImageResource(R.drawable.placeholder);
            }

        } else {

            String imageUri = "http://image.tmdb.org/t/p/w185/" + movie.imageUrl;
            Picasso.with(getContext())
                    .load(imageUri)
                    .into(mImageView);
        }

        mPlotView.setText(movie.moviePlot);
        String date = movie.releaseDate;
        try {
            SimpleDateFormat oldFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            Date oldDate = oldFormat.parse(date);
            SimpleDateFormat newFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
            date = newFormat.format(oldDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String release_date = date_prefix + date;
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


    // Setup for EventBus to display Toast on Favorite Status
    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        Toast.makeText(getActivity(), event.message, Toast.LENGTH_SHORT).show();
    }
}