package org.ragecastle.movieprovider;

import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import org.ragecastle.movieprovider.adapter.Movie;
import org.ragecastle.movieprovider.adapter.MovieAdapter;
import org.ragecastle.movieprovider.database.Contract;

import java.util.Arrays;

/**
 * Created by jahall on 12/30/15.
 *
 */
public class MainFragment extends Fragment {

    private final String LOG_TAG = MainFragment.class.getSimpleName();
    private MovieAdapter movieAdapter;
    private GridView gridView;

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

        gridView = (GridView) rootView.findViewById(R.id.gridview_image);
        fillGrid(getImages());
        return rootView;
    }

    private Movie[] getImages(){
        Cursor cursor;

        String[] projection = {
                Contract.MovieEntry.COLUMN_IMAGE,
                Contract.MovieEntry.COLUMN_MOVIE_ID};

        cursor = getActivity().getContentResolver().query(
                Contract.MovieEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);

        if (cursor.moveToFirst()){
            Movie[] movieArray = new Movie[cursor.getCount()];
            do {
                String movieId = cursor.getString(cursor.getColumnIndex(Contract.MovieEntry.COLUMN_MOVIE_ID));
                String image = cursor.getString(cursor.getColumnIndex(Contract.MovieEntry.COLUMN_IMAGE));

                movieArray[cursor.getPosition()] =
                        new Movie(
                                movieId,
                                "title",
                                image,
                                "release_date",
                                "avg_Rating",
                                "plot");
            } while (cursor.moveToNext());

            cursor.close();
            return movieArray;
        }

        return new Movie[]{
               new Movie("id",
                       "title",
                       "/t90Y3G8UGQp0f0DrP60wRu9gfrH.jpg",
                       "release_date",
                       "average_rating",
                       "plot")
       };
    }

    private void fillGrid(final Movie[] movieArray){
        movieAdapter = new MovieAdapter(getActivity(), Arrays.asList(movieArray));
        // Populate grid view
        gridView.setAdapter(movieAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie movie = movieAdapter.getItem(position);
                //create new intent to launch the detail page
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("movie_id", movie.id);
                startActivity(intent);
            }
        });
    }
}
