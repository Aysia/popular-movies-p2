package com.linux_girl.popularmovies;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;

import static android.view.View.GONE;
import static com.linux_girl.popularmovies.DetailActivity.TRAILERFRAMGMENT_TAG;
import static com.linux_girl.popularmovies.MainFragment.adapter;

public class TrailerFragment extends Fragment implements TrailerTask.TaskListener {

    String LOG_TAG = TrailerFragment.class.getSimpleName();

    final static String KEY_POSITION = "position";
    int mCurrentPosition = -1;

    static String MOVIE_ID = "extra";

    View rootView;
    View mRootView;
    ListView listView;
    TrailerAdapter trailerAdapter;

    TrailerTask task = new TrailerTask(this);
    String mMovieId;

    public TrailerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onFinished(ArrayList<Trailers> trailers) {

        if(MainActivity.isOnline(getContext())) {
            insertTrailers(trailers);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_trailer, container, false);
        listView = (ListView) rootView.findViewById(R.id.trailer_list_view);

        Bundle arguments = getArguments();

        if (arguments != null) {
            mMovieId = arguments.getString(MOVIE_ID);
            if(MainActivity.isOnline(getContext()) && mMovieId != null) {
                task.execute(mMovieId);
            }

        }

        return rootView;
    }

    public void insertTrailers(final ArrayList<Trailers> trailers) {

        trailerAdapter = new TrailerAdapter(getContext(), trailers);

        listView.setAdapter(trailerAdapter);
        Utility.setListViewHeightBasedOnChildren(listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Trailers currentTrailer = trailerAdapter.getItem(position);
                watchYoutubeVideo(currentTrailer != null ? currentTrailer.mKey : null);
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save the current trailer selection in case we need to recreate the fragment
        outState.putInt(KEY_POSITION, mCurrentPosition);
    }

    public void watchYoutubeVideo(String id){

        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + id));
        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            startActivity(webIntent);
        }
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
