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
import java.nio.charset.Charset;
import java.util.ArrayList;

public class ReviewTask extends AsyncTask<String, Void, JSONObject> {

    public interface TaskListener {
        public void onFinished(ArrayList<Reviews> reviews);
    }

    // This is the reference to the associated listener
    private final TaskListener taskListener;

    public ReviewTask(TaskListener listener) {
        // The listener reference is passed in through the constructor
        this.taskListener = listener;
    }

    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = null;

    final String BASE_RESULTS = "results";
    final String REVIEW_AUTHOR = "author";
    final String REVIEW_CONTENT = "content";

    String jsonResponse = "";
    String jsonString;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected JSONObject doInBackground(String... args) {
        final String BASE_URL =
                "http://api.themoviedb.org/3/movie/" + args[0] + "/reviews?";

        final String APPID_PARAM = "api_key";

        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(APPID_PARAM, BuildConfig.THE_MOVIE_DB_API_CODE)
                .build();

        String url = builtUri.toString();

        JSONObject json = getJSONFromUrl(url);

        return json;
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {

        ArrayList<Reviews> reviews = new ArrayList<>();
        try {
            JSONArray tArray = jsonObject.getJSONArray(BASE_RESULTS);
            String author;
            String content;

            for (int i = 0; i < tArray.length(); i++) {
                // Get the JSONObjects representing a trailer
                JSONObject theObject = tArray.getJSONObject(i);
                author = theObject.getString(REVIEW_AUTHOR);
                content = theObject.getString(REVIEW_CONTENT);

                // Add the objects to the @link String
                reviews.add(new Reviews(author, content));
            }

            taskListener.onFinished(reviews);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public JSONObject getJSONFromUrl(String uri) {
        // Making HTTP request
        try {
            URL url = new URL(uri);

            // Perform HTTP request to the URL and receive a JSON response back
            HttpURLConnection urlConnection = null;
            InputStream inputStream = null;

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.i("JSONParse", "No Data Found at the URL");
                return null;
            }

        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }

        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(jsonResponse);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

        // return JSON Object
        return jObj;
    }


    private String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
}
