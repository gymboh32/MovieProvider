package org.ragecastle.movieprovider.adapter;

/**
 * Created by jahall on 12/30/15.
 */
public class Movie {

    public String id;
    public String title;
    public String image;
    public String releaseDate;
    public String avgRating;
    public String plot;

    public Movie( String id,
                  String title,
                  String image,
                  String releaseDate,
                  String rating,
                  String plot)
    {
        this.id = id;
        this.title = title;
        this.image = image;
        this.releaseDate = releaseDate;
        this.avgRating = rating;
        this.plot = plot;
    }
}
