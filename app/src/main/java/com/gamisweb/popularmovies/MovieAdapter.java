package com.gamisweb.popularmovies;

import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by corey on 2/22/2016.
 */
public class MovieAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Movie> movies;

    public MovieAdapter(Context c, ArrayList<Movie> _movies){
        mContext = c;
        movies = _movies;
    }
    @Override
    public int getCount() {
        if(movies != null)
         return movies.size();
        else return 0;
    }

    public void add(Movie newMovie){
        movies.add(newMovie);
    }

    public void clear(){
        movies = null;
    }

    public void setMovies(ArrayList<Movie> _movies){
        movies = _movies;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

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
            width = (int) width/2;


            String urlString = "http://image.tmdb.org/t/p/w185/";
            urlString += movies.get(position).getPosterPath();
            Picasso.with(mContext)
                    .load(urlString)
                    .resize(width,(int)(width*1.5))
                    .into(imageView);


            /*
            // set value into textview
            TextView textView = (TextView) gridView
                    .findViewById(R.id.titleTextView);
            textView.setText(movies[position].getTitle());


            TextView idView = (TextView) gridView
                    .findViewById(R.id.idTextView);
            idView.setText(Integer.toString(movies[position].getId()));

            */
            Log.v("In Movie Adapter", movies.get(position).getTitle());
            Log.v("In Movie Adapter", Integer.toString(movies.get(position).getId()));
        } else {
            gridView = (View) convertView;
        }

        return gridView;
    }
}
