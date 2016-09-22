package com.linux_girl.popularmovies;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Lani on 8/25/2016.
 * Class created to utilize the power of AsyncTask to open stream to the
 * TMDB API and get the json file to read and parse through.
 */
public class GetMovieTask extends AsyncTask<String, Void, String> {

    public ArrayList<Movies> movie;

    public ArrayList<Movies> getMovieDataFromJson(String movieJsonStr)
            throws JSONException {

        ArrayList<Movies> movie = new ArrayList<>();
        // These are the JSONObject constants that need to be extracted
        final String MOVIE_ID = "id";
        final String MOVIE_RESULTS = "results";
        final String MOVIE_POSTER_PATH = "poster_path";
        final String MOVIE_TITLE = "original_title";
        final String MOVIE_PLOT = "overview";
        final String MOVIE_USER_RATINGS = "vote_average";
        final String MOVIE_RELEASE_DATE = "release_date";
        final String MOVIE_BACK_DROP = "backdrop_path";

        JSONObject moviesJson = new JSONObject(movieJsonStr);
        JSONArray moviesArray = moviesJson.getJSONArray(MOVIE_RESULTS);

        for(int i = 0; i < moviesArray.length(); i++) {

            String id;
            String title;
            String plot;
            String rating;
            String date;
            String imageUrl;
            String backDrop;

            // Get the JSONObjects representing a movie
            JSONObject movieObject = moviesArray.getJSONObject(i);
            id = movieObject.getString(MOVIE_ID);
            title = movieObject.getString(MOVIE_TITLE);
            plot = movieObject.getString(MOVIE_PLOT);
            rating = movieObject.getString(MOVIE_USER_RATINGS);
            date = movieObject.getString(MOVIE_RELEASE_DATE);
            imageUrl = movieObject.getString(MOVIE_POSTER_PATH);
            backDrop = movieObject.getString(MOVIE_BACK_DROP);

            // Add the objects to the @link Movies
            movie.add(new Movies(id, title, plot, rating, date, imageUrl, backDrop));
        }
        return movie;
    }

    @Override
    protected String doInBackground(String... params) {
        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String movieJsonStr = null;

        try {

            /**
             * Start the build process for the URL we need to obtain the
             * JSON response. Utilize params[0] which holds the user
             * selection "sort" in Settings
             */

            final String MOVIES_BASE_URL =
                    "http://api.themoviedb.org/3/movie/" + params[0];

            /**
             * Assign the key for the builder to apply my API Key
             */
            final String APPID_PARAM = "api_key";

            /**
             * Build the Uri based on the above constants and
             * grab my API key from gradle build
             */


            Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                    .appendQueryParameter(APPID_PARAM, BuildConfig.THE_MOVIE_DB_API_CODE)
                    .build();

            URL url = new URL(builtUri.toString());

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                movieJsonStr = null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // new line to make debugging easier
                buffer.append(line + "\n");
            }
            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            movieJsonStr = buffer.toString();

        } catch (IOException e) {
            Log.e("MainFragment", "Error ", e);

            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("MainFragment", "Error closing stream", e);
                }
            }
        }
        return movieJsonStr;
    }

    @Override
    protected void onPostExecute(String movieJsonStr ){
        try {
            ArrayList<Movies> movie = new ArrayList<>();
            movie = getMovieDataFromJson(movieJsonStr);
            if(movie != null) {
                MainFragment.setMovieAdapter(movie);
            }
        } catch(JSONException e) {
            e.printStackTrace();
        }
    }
}
