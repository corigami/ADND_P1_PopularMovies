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


public class MovieDetailActivityFragment extends Fragment {
    private static final String LOG_TAG = MovieDetailActivityFragment.class.getSimpleName();
    Context mContext = getContext();
    private Movie currentMovie;

    public MovieDetailActivityFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.movie_detail_frag_menu, menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Intent intent = getActivity().getIntent();
        currentMovie = (Movie)intent.getSerializableExtra("Movie");

        //get all of our activity objects
        View rootView = inflater.inflate(R.layout.fragment_movie_detail_activity, container, false);
        ImageView imageView = (ImageView) rootView.findViewById(R.id.movie_detail_poster);
        //get screen information to set size of viewImage


        //Duplicate code from main activity, not sure how to cache images from inital call...
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        width = (int) width/2;

        Picasso.with(mContext)
                .load(currentMovie.getPosterPath())
                .resize(width,(int)(width*1.5))
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
