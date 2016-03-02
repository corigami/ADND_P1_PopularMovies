/*
 * Copyright (C) 2016 Corey T. Willinger - No Rights Reserved
 */
package com.gamisweb.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
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

            gridView = new View(getContext());

            // get layout from mobile.xml
            gridView = inflater.inflate(R.layout.movie_tile, null);

            //Initialize ImageView
            ImageView imageView = (ImageView) gridView.findViewById(R.id.imageView);

            //Loading image from below url into imageView
            WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x;
            width = (int) width / 2;

            if(movies!= null) {
                Picasso.with(getContext())
                        .load(movies.get(position).getPosterPath())
                        .resize(width, (int) (width * 1.5))
                        .into(imageView);
            }

        } else {
            gridView = (View) convertView;
        }

        return gridView;
    }
}
