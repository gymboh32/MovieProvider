package org.ragecastle.movieprovider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ragecastle.movieprovider.adapter.Movie;

/**
 * Created by jahall on 12/30/15.
 *
 * Parse the JSON movie Data
 *
 */
public class DataParser {

    public static Movie[] getMovieInfo(String movieData) throws JSONException {

        // String array of poster location to be returned
        Movie[] results;
        // Make the movieData parameter a JSONObject
        JSONObject jsonMovieData = new JSONObject(movieData);

        // Extract the list of results from movieData
        JSONArray movieInfoArray = jsonMovieData.getJSONArray("results");

        // Set the size of the results array based on the number of movies returned
        results = new Movie [movieInfoArray.length()];

        // Loop through the JSONArray and extract the poster location information
        for(int i=0;i<movieInfoArray.length();i++){

            // Pull the movieInfo from the Array
            JSONObject movieInfo = movieInfoArray.getJSONObject(i);
            String id = getMovieData(movieInfo, "id");
            String title = getMovieData(movieInfo, "title");
            String image = getMovieData(movieInfo, "poster_path");
            String releaseDate = getMovieData(movieInfo, "release_date");
            String avgRating = getMovieData(movieInfo, "vote_average");
            String plot = getMovieData(movieInfo, "overview");

            // Add the movie to the array
            results[i] = new Movie(id, title, image, releaseDate, avgRating, plot);
        }

        // Return String array of poster locations
        return results;
    }

    public static String getMovieData(JSONObject movieInfo, String category) throws JSONException {
        // Return the String of movie data
        return movieInfo.getString(category);
    }
}
