package org.ragecastle.movieprovider;

import android.content.ContentValues;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONException;
import org.ragecastle.movieprovider.adapter.Movie;
import org.ragecastle.movieprovider.database.Contract;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FetchDataTask fetchDataTask = new FetchDataTask();
        fetchDataTask.execute("");

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.main_container, new MainFragment())
                    .commit();
        }
    }

    public class FetchDataTask extends AsyncTask<String, Void, Movie[]> {

        private final String LOG_TAG = FetchDataTask.class.getSimpleName();

        @Override
        protected Movie[] doInBackground(String... params) {

            HttpURLConnection connection;
            BufferedReader reader = null;
            InputStream inputStream;
            StringBuffer buffer;
            String result = null;

            try {
                // constants of api parameters
                final String BASE_URL = "https://api.themoviedb.org/3/discover/movie";
                final String API_KEY_PARAM = "api_key";
                final String APIKEY = "";

                // Build the URI to pass in for movie information
                Uri builder = Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter(API_KEY_PARAM, APIKEY)
                        .build();

                // Create URL to pass in for movie information
                URL url = new URL(builder.toString());

                // Open the connection for the HTTP request
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                // Get the input stream from the URL request
                inputStream = connection.getInputStream();

                // Create buffer to write the input stream to
                buffer = new StringBuffer();
                // If stream is empty return null
                if (inputStream == null) {
                    return null;
                }

                // Read the input stream
                reader = new BufferedReader(new InputStreamReader(inputStream));

                // Write the reader to the buffer as long as there is something to write
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line).append("\n");
                }

                // Convert the buffer to String to be sent to the Parser
                result = buffer.toString();

            } catch (IOException e) {
                Log.e(LOG_TAG, "Check the API Key");
            } finally {
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Couldn't close reader");
                }
            }

            try {
                return DataParser.getMovieInfo(result);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Movie[] moviesArray) {

            ContentValues movieValues;
            // Loop through static array of Flavors, add each to an instance of ContentValues
            // in the array of ContentValues
            for (int i = 0; i < moviesArray.length; i++) {
                movieValues = new ContentValues();
                movieValues.put(Contract.MovieEntry.COLUMN_MOVIE_ID, moviesArray[i].id);
                movieValues.put(Contract.MovieEntry.COLUMN_TITLE, moviesArray[i].title);
                movieValues.put(Contract.MovieEntry.COLUMN_IMAGE, moviesArray[i].image);
                movieValues.put(Contract.MovieEntry.COLUMN_RELEASE_DATE, moviesArray[i].releaseDate);
                movieValues.put(Contract.MovieEntry.COLUMN_AVG_RATING, moviesArray[i].avgRating);
                movieValues.put(Contract.MovieEntry.COLUMN_PLOT, moviesArray[i].plot);

                // array to filter columns being queried
                String[] projection = {Contract.MovieEntry.COLUMN_MOVIE_ID};
                // Check if the value is already in the database before adding it
                if (getContentResolver().query(
                        Contract.MovieEntry.CONTENT_URI.buildUpon().appendPath(moviesArray[i].id).build(),
                        projection,
                        null,
                        null,
                        null).getCount() == 0){
                    Log.i(LOG_TAG, "Added " + moviesArray[i].title);
                    // add the movie to the database
                    getContentResolver().insert(Contract.MovieEntry.CONTENT_URI, movieValues);
                }
            }
        }
    }
}
