/*
 * Copyright (C) 2016 Corey T. Willinger - No Rights Reserved
 */

package com.gamisweb.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * The fragment displays detailed information about movie selected from the main screen.
 * Load when {@link MovieDetailActivity} is created.
 */
public class MovieDetailActivityFragment extends Fragment {
    private static final String LOG_TAG = MovieDetailActivityFragment.class.getSimpleName();
    Context mContext = getContext();
    private Movie currentMovie;

    public MovieDetailActivityFragment() {
        // Required empty public constructor
    }

    /**
     * Default constructor.
     * @param savedInstanceState the state of class throughout the application life-cycle
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    /**
     * Creates the view for the fragment
     * @param inflater the inflater from the owning activity
     * @param container the parent container
     * @param savedInstanceState the state of class throughout the application life-cycle
     * @return the view that was created
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Intent intent = getActivity().getIntent();
        currentMovie = (Movie)intent.getSerializableExtra("Movie");

        //get all of our activity objects
        View rootView = inflater.inflate(R.layout.fragment_movie_detail_activity, container, false);
        ImageView imageView = (ImageView) rootView.findViewById(R.id.movie_detail_poster);

        //Duplicate code from main activity, not sure how to cache images from initial call...
        //TODO Optimize this code to use cached images from Picasso
        //Calculate window size for setting
        DisplayUtil dispUtil = new DisplayUtil((getContext()));
        int width = dispUtil.getDisplaySize().x;
        width = (int) width/2;
        Picasso.with(mContext)
                .load(currentMovie.getPosterPath())
                .resize(width, (int) (width * 1.5))
                .error(R.drawable.error)
                .into(imageView);

        TextView detailTitleTextView = (TextView)rootView.findViewById(R.id.movie_detail_title);
        if(detailTitleTextView != null)
            detailTitleTextView.setText(currentMovie.getTitle());
        RatingBar ratingBar = (RatingBar)rootView.findViewById(R.id.movie_detail_ratingBar);
        if(ratingBar != null) {
            ratingBar.setNumStars(5);
            ratingBar.setRating(currentMovie.getVoteAvg()/2);
        }
        TextView releaseDateTextView = (TextView)rootView.findViewById(R.id.movie_detail_release);
        if(releaseDateTextView != null)
            releaseDateTextView.setText("Released: " + currentMovie.getReleaseDate());

        TextView overviewTextView = (TextView)rootView.findViewById(R.id.movie_detail_overview);
        if(overviewTextView != null)
            overviewTextView.setText(currentMovie.getOverview());
        return rootView;
    }
}
