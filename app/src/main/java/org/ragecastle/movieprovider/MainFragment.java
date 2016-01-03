package org.ragecastle.movieprovider;

import android.app.Fragment;
import android.content.ContentValues;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import org.json.JSONException;
import org.ragecastle.movieprovider.adapter.Movie;
import org.ragecastle.movieprovider.adapter.MovieAdapter;
import org.ragecastle.movieprovider.database.Contract;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

/**
 * Created by jahall on 12/30/15.
 *
 */
public class MainFragment extends Fragment {

    private MovieAdapter movieAdapter;
    private GridView gridView;

    public MainFragment () {}

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FetchDataTask fetchDataTask = new FetchDataTask();
        fetchDataTask.execute("");
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        // Create the rootView of the Fragment
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        Movie[] movieArray = {
                new Movie("id",
                        "title",
                        "t90Y3G8UGQp0f0DrP60wRu9gfrH.jpg",
                        "release_date",
                        "average_rating",
                        "plot")
        };

        gridView = (GridView) rootView.findViewById(R.id.gridview_image);
        movieAdapter = new MovieAdapter(getActivity(), Arrays.asList(movieArray));
        // Populate grid view
        gridView.setAdapter(movieAdapter);

        return rootView;
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
                final String SORT_BY_PARAM = "sort_by";
                final String APIKEY = "";

                // Build the URI to pass in for movie information
                Uri builder = Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter(API_KEY_PARAM, APIKEY)
                        .appendQueryParameter(SORT_BY_PARAM, params[0])
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

            ContentValues[] movieValuesArray = new ContentValues[moviesArray.length];
            // Loop through static array of Flavors, add each to an instance of ContentValues
            // in the array of ContentValues
            for (int i = 0; i < moviesArray.length; i++) {
                movieValuesArray[i] = new ContentValues();
                movieValuesArray[i].put(Contract.MovieEntry.COLUMN_TITLE, moviesArray[i].title);
                movieValuesArray[i].put(Contract.MovieEntry.COLUMN_IMAGE, moviesArray[i].image);
                movieValuesArray[i].put(Contract.MovieEntry.COLUMN_IMAGE, moviesArray[i].releaseDate);
                movieValuesArray[i].put(Contract.MovieEntry.COLUMN_IMAGE, moviesArray[i].avgRating);
                movieValuesArray[i].put(Contract.MovieEntry.COLUMN_IMAGE, moviesArray[i].plot);
            }
            try {
                // bulkInsert our ContentValues array
                getActivity().getContentResolver().bulkInsert(Contract.MovieEntry.CONTENT_URI,
                        movieValuesArray);
            } catch (Exception e){
                Log.e(LOG_TAG, "database broke");

            }
        }
    }

}
