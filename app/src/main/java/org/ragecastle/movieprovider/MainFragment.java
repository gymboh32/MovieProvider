package org.ragecastle.movieprovider;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import org.ragecastle.movieprovider.adapter.Movie;
import org.ragecastle.movieprovider.adapter.MovieAdapter;
import org.ragecastle.movieprovider.database.DBHelper;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by jahall on 12/30/15.
 *
 */
public class MainFragment extends Fragment {

    private final String LOG_TAG = MainFragment.class.getSimpleName();
    private MovieAdapter movieAdapter;
    private GridView gridView;
    private ArrayList<Movie> list = new ArrayList<Movie>();
    private DBHelper dbHelper;

    public MainFragment () {}

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

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
                        "http://image.tmdb.org/t/p/w500/t90Y3G8UGQp0f0DrP60wRu9gfrH.jpg",
                        "release_date",
                        "average_rating",
                        "plot")
        };

        gridView = (GridView) rootView.findViewById(R.id.gridview_image);
        fillGrid(movieArray);
//        readData(dbHelper.readAll());
        return rootView;
    }

    private void fillGrid(Movie[] movieArray){
        movieAdapter = new MovieAdapter(getActivity(), Arrays.asList(movieArray));
        // Populate grid view
        gridView.setAdapter(movieAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //get the info for the array list item being clicked
                Movie movie = movieAdapter.getItem(position);

                //create new intent to launch the detail page
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("id", true);
                intent.putExtra("title", movie.title);
                intent.putExtra("posterPath", movie.image);
                intent.putExtra("releaseDate", movie.releaseDate);
                intent.putExtra("avgRating", movie.avgRating);
                intent.putExtra("plot", movie.plot);
                startActivity(intent);
            }
        });
    }


}
