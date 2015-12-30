package org.ragecastle.movieprovider;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import org.ragecastle.movieprovider.adapter.Movie;
import org.ragecastle.movieprovider.adapter.MovieAdapter;

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

}
