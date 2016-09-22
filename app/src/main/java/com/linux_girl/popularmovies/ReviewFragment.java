package com.linux_girl.popularmovies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by Lani on 9/20/2016.
 */
public class ReviewFragment extends Fragment implements ReviewTask.TaskListener {

    String LOG_TAG = ReviewFragment.class.getSimpleName();

    final static String KEY_POSITION = "position";
    int mCurrentPosition = -1;

    static String MOVIE_ID = "extra";

    View rootView;
    ListView listView;
    ReviewAdapter adapter;

    ReviewTask task = new ReviewTask(this);
    String mMovieId;

    public ReviewFragment() {
    }

    @Override
    public void onFinished(ArrayList<Reviews> reviews) {
        insertReviews(reviews);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_reviews, container, false);
        listView = (ListView) rootView.findViewById(R.id.reviews_listview);

        Bundle arguments = getArguments();

        if (arguments != null) {
            // Update Details

            mMovieId = arguments.getString(MOVIE_ID);
            task.execute(mMovieId);

        } else if (mCurrentPosition != -1) {
            // Set description based on savedInstanceState defined during onCreateView()
            task.execute(mMovieId);
        }

        if (savedInstanceState != null) {
            mCurrentPosition = savedInstanceState.getInt(KEY_POSITION);
        }

        return rootView;
    }

    public void insertReviews(ArrayList<Reviews> reviews) {

        /**
         * Create an {@link TrailerAdapter}, whose data source is a list of
         * {@link Trailers}. The adapter knows how to create list items for each
         * item in the list.
         */
        adapter = new ReviewAdapter(getContext(), reviews);

        /** Set the adapter on the {@link ListView}
         so the list can be populated in the user interface
         */
        listView.setAdapter(adapter);
        Utility.setListViewHeightBasedOnChildren(listView);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save the current trailer selection in case we need to recreate the fragment
        outState.putInt(KEY_POSITION, mCurrentPosition);
    }
}
