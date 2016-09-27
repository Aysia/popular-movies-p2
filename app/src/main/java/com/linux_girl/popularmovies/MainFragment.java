package com.linux_girl.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.linux_girl.popularmovies.data.DatabaseContract;
import com.linux_girl.popularmovies.data.DatabaseHelper;

import java.util.ArrayList;


public class MainFragment extends Fragment implements LoaderManager.LoaderCallbacks<Movies>{

    private final String LOG_TAG = MainFragment.class.getSimpleName();
    static MovieAdapter adapter;
    public static String MOVIE_EXTRA = "extra";
    GridView gridView;
    int mPosition = gridView.INVALID_POSITION;
    private static final String SELECTED_KEY = "selected_position";
    private static final int MOVIE_LOADER = 0;
    public Movies currentMovie;

    public MainFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        adapter = new MovieAdapter(getActivity(), new ArrayList<Movies>());
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // Get a reference to the GridView, and attach this adapter to it.
        gridView = (GridView) rootView.findViewById(R.id.maingrid);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                currentMovie = adapter.getItem(position);
                if (currentMovie != null) {
                    ((Callback) getActivity())
                            .onItemSelected(MainActivity.getMovieObject(currentMovie)
                            );
                }
                mPosition = position;
            }
        });

        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
            mPosition = savedInstanceState.getInt(SELECTED_KEY);
        }

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(MOVIE_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    public static void setMovieAdapter(ArrayList<Movies> movie) {
        adapter.clear();
        adapter.addAll(movie);
        adapter.notifyDataSetChanged();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivityForResult(new Intent(getContext(), SettingsActivity.class), 0);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            updateMovies();
        }
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        // When tablets rotate, the currently selected list item needs to be saved.
        // When no item is selected, mPosition will be set to Listview.INVALID_POSITION,
        // so check for that before storing.
        if (mPosition != GridView.INVALID_POSITION) {
            outState.putInt(SELECTED_KEY, mPosition);
        }
        super.onSaveInstanceState(outState);
    }

    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
        updateMovies();
    }

    @Override
    public Loader<Movies> onCreateLoader(int position, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Movies> loader, Movies data) {
        return;
    }

    @Override
    public void onLoaderReset(Loader<Movies> loader) {
        return;
    }

    public interface Callback {
        public void onItemSelected(MovieObject object);
    }

    public void updateMovies() {
        //First get Preference Key
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortOrder = prefs.getString(getString(R.string.pref_sort_key),
                getString(R.string.pref_sort_default));

        // If sorting == favorites always display
        if(sortOrder.equals("favorites")) {
            DatabaseHelper dbHelper = new DatabaseHelper(getContext());
            Cursor cursor = dbHelper.getFavorites(null, null);
            cursorToList(cursor);
        } else {
            // only if there is connection display other sorting
            if (MainActivity.isOnline(getContext())) {
                GetMovieTask getMovie = new GetMovieTask();
                getMovie.execute(sortOrder);
            } else {
                Toast.makeText(getContext(), "No Internet Connection Available", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void cursorToList(Cursor cursor) {
        ArrayList<Movies> movies = new ArrayList<>();
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            String movieId = cursor.getString(cursor.getColumnIndex(DatabaseContract.Favorites.MOVIE_ID));
            String movieTitle = cursor.getString(cursor.getColumnIndex(DatabaseContract.Favorites.MOVIE_TITLE));
            String moviePlot = cursor.getString(cursor.getColumnIndex(DatabaseContract.Favorites.MOVIE_PLOT));
            String userRating = cursor.getString(cursor.getColumnIndex(DatabaseContract.Favorites.MOVIE_RATING));
            String releaseDate = cursor.getString(cursor.getColumnIndex(DatabaseContract.Favorites.RELEASE_DATE));

            movies.add(new Movies(movieId,movieTitle, moviePlot, userRating, releaseDate,"null","null")); //add the item
            cursor.moveToNext();
        }
        setMovieAdapter(movies);
    }
}
