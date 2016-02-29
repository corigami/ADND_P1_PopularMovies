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
 * Extends the ArrayAdapter class to paint {@link Movie} details.
 * Created by corey on 2/22/2016.
 */
public class MovieAdapter extends ArrayAdapter {
    private final String LOG_TAG = MovieAdapter.class.getSimpleName();
    private Context mContext;
    private ArrayList<Movie> movies;

    /**
     * Constructs the MovieAdapter object.
     * @param c the application context
     * @param resource the default view to be populated by the Adapter
     * @param _movies {@link ArrayList} of {@code Movie}s
     */
    public MovieAdapter(Context c, int resource, ArrayList<Movie> _movies) {
        super(c, resource, _movies);
        mContext = c;
        movies = _movies;
    }

    /**
     * Returns the number of movies in the underlying container
     * @return an {@link Integer} of the number of objects
     */
    @Override
    public int getCount() {
        if (movies != null)
            return movies.size();
        else return 0;
    }

    /**
     * Returns the {@link Movie} at the given position
     * @param position the index to retrieve from the arrayList
     * @return a {@code Movie} object
     * @throws IndexOutOfBoundsException
     */
    @Override
    public Movie getItem(int position) throws IndexOutOfBoundsException{
        return movies.get(position);
    }

    public void add(Movie newMovie) {
        movies.add(newMovie);
        notifyDataSetChanged();
    }


    /**
     * Adds an ArrayList of Movies to the current container
     * @param newMovies the {@link ArrayList} of {@link Movie}s to add
     */
    public void add(ArrayList<Movie> newMovies) {
        for (Movie m : newMovies) {
            movies.add(m);
        }
        notifyDataSetChanged();
    }

    /**
     * Replaces the current list of movies with a new set
     * @param _movies the new {@link ArrayList} of {@link Movie}s to use in the adpater
     */
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

    /**
     * Returns {@link View} to be displayed
     * @param position the index of the {@link Movie} to be rendered
     * @param convertView {@code View} to be used instead of generating new view (if not @code null)
     * @param parent the parent ViewGroup of the view that will be created.
     * @return
     */
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
