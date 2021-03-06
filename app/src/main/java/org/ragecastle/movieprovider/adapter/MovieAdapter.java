package org.ragecastle.movieprovider.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.ragecastle.movieprovider.R;

import java.util.List;

/**
 * Created by jahall on 12/30/15.
 *
 */
public class MovieAdapter extends ArrayAdapter<Movie> {
    // private static final String LOG_TAG = MoviePosterAdapter.class.getSimpleName();
    private ImageView view;
    private final String BASE_URL = "http://image.tmdb.org/t/p/w185";

    public MovieAdapter(Activity context, List<Movie> movie){
        super(context, 0, movie);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Movie movie = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.list_item, parent, false);
        }

        view = (ImageView) convertView.findViewById(R.id.list_item_movie);

        String url = BASE_URL.concat(movie.image);

        // TODO: add error to display default image
        Picasso.with(getContext()).load(url).fit().centerCrop().into(view);

        return convertView;
    }

    @Override
    public void add(Movie movie) {
        String url = BASE_URL.concat(movie.image);
        // TODO: add error to display default image
        Picasso.with(getContext()).load(url).fit().into(view);
    }
}
