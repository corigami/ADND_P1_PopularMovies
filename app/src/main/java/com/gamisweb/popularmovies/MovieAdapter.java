/*
 * Copyright (C) 2016 Corey T. Willinger - No Rights Reserved
 */
package com.gamisweb.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

/**
 * Extends the ArrayAdapter class to paint {@link Movie} details.
 * Created by corey on 2/22/2016.
 */
public class MovieAdapter extends ArrayAdapter<Movie> {
    private final String LOG_TAG = MovieAdapter.class.getSimpleName();
    private List<Movie> movies;

    /**
     * Constructs the MovieAdapter object.
     * @param c the application context
     * @param resource the default view to be populated by the Adapter
     * @param _movies {@link ArrayList} of {@code Movie}s
     */
    public MovieAdapter(Context c, int resource, ArrayList<Movie> _movies) {
        super(c, resource, _movies);
        movies = _movies;
    }

        /**
     * Returns {@link View} to be displayed
     * @param position the index of the {@link Movie} to be rendered
     * @param convertView {@code View} to be used instead of generating new view (if not @code null)
     * @param parent the parent ViewGroup of the view that will be created.
     * @return
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) ((Activity)getContext()).getLayoutInflater();
       // LayoutInflater inflater = (LayoutInflater) getAc.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View gridView;

        if (convertView == null) {

            // get layout from mobile.xml
            gridView = inflater.inflate(R.layout.movie_tile, null);

            //Initialize ImageView
            ImageView imageView = (ImageView) gridView.findViewById(R.id.imageView);

            //Loading image from below url into imageView
            DisplayUtil dispUtil = new DisplayUtil((getContext()));
            int imgWidth;
            int imgHeight;
            if(dispUtil.getOrientation() == 1){
                imgWidth = (int)dispUtil.getDisplaySize().x /2;
            }else{
               imgWidth = (int)(dispUtil.getDisplaySize().x /3);
                 }
            imgHeight = (int)(imgWidth * 1.5);

            if(movies!= null) {
               Picasso.with(getContext())
                        .load(movies.get(position).getPosterPath())
                        .resize(imgWidth, imgHeight)
                        .error(R.drawable.error)
                        .into(imageView);
            }

        } else {
            gridView = convertView;
        }
        return gridView;
    }
}
