package com.gamisweb.popularmovies;

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

import java.util.ArrayList;
import java.util.List;


/**
 * Created by corey on 2/22/2016.
 */
public class MovieAdapter extends ArrayAdapter {
    private final String LOG_TAG = MovieAdapter.class.getSimpleName();
    private Context mContext;
    private ArrayList<Movie> movies;

    public MovieAdapter(Context c, int resource, ArrayList<Movie> _movies) {
        super(c, resource, _movies);
        mContext = c;
        movies = _movies;
    }

    @Override
    public int getCount() {
        if (movies != null)
            return movies.size();
        else return 0;
    }

    @Override
    public Movie getItem(int position) {
        return movies.get(position);
    }

    public void add(Movie newMovie) {
        movies.add(newMovie);
        notifyDataSetChanged();
    }

    public void add(ArrayList<Movie> newMovies) {
        for (Movie m : newMovies) {
            movies.add(m);
        }
        notifyDataSetChanged();
    }

    public void setMovies(ArrayList<Movie> _movies) {
        movies = _movies;
        notifyDataSetChanged();
        Log.v(LOG_TAG, "Data was just changed");
    }

    @Override
    public void clear() {
        movies.clear();
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View gridView;

        if (convertView == null) {

            gridView = new View(mContext);

            // get layout from mobile.xml
            gridView = inflater.inflate(R.layout.movie_tile, null);

            //Initialize ImageView
            ImageView imageView = (ImageView) gridView.findViewById(R.id.imageView);

            //Loading image from below url into imageView
            WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x;
            width = (int) width / 2;

            Picasso.with(mContext)
                    .load(movies.get(position).getPosterPath())
                    .resize(width, (int) (width * 1.5))
                    .into(imageView);

        } else {
            gridView = (View) convertView;
        }

        return gridView;
    }
}
