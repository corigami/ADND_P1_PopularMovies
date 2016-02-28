/*
 * Copyright (C) 2016 Corey T. Willinger - Some Rights Reserved
 */
package com.gamisweb.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * The main fragment to be loaded by {@link MainActivity}, loaded on app start.
 */
public class MainActivityFragment extends Fragment {
    static final String API_KEY = "";
    private SharedPreferences sharedPref;
    private MovieAdapter movieAdapter;

    public MainActivityFragment() {
    }

    /**
     * Called when Activity is created.
     * @param savedInstanceState the state of class throughout the application life-cycle
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //let main activity know that this fragment has menu items
        setHasOptionsMenu(true);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
    }


    /**
     * Provides logic to be executed when menu item is selected.
     * @param item the user selected option from settings menu
     * @return the response from parent method
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    /**
     * Loads the fragments view.  Also sets the gridView adapter to custom {@link MovieAdapter}.
     * @param inflater the inflater object used to expand the view from xml
     * @param container the object to attach the view to.
     * @param savedInstanceState stores the app state during app life-cycle
     * @return the root view of the fragment
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        movieAdapter = new MovieAdapter(getActivity(), R.id.gridview, new ArrayList<Movie>());

        GridView gridview = (GridView) rootView.findViewById(R.id.gridview);
        gridview.setAdapter(movieAdapter);

        //adds click listener that loads the detail activity for the selected movie
        gridview.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent detailIntent = new Intent(getActivity(), MovieDetailActivity.class);
                detailIntent.putExtra("Movie", movieAdapter.getItem(position));
                startActivity(detailIntent);
            }
        });
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        getMovieData();
    }


    /**
     * Starts the background request for movie data based on sort settings.
     */
    public void getMovieData() {
        FetchMovieData movieTask = new FetchMovieData();
        String sort = sharedPref.getString("sort", "popularity.desc");
        movieTask.execute(sort);

    }

    /**
     * Requests data from the theMovieDataBase using the site's APIs.
     */
    public class FetchMovieData extends AsyncTask<String, Void, ArrayList<Movie>> {
        private final String LOG_TAG = FetchMovieData.class.getSimpleName();

        @Override
        protected ArrayList<Movie> doInBackground(String... params) {
            Log.v(LOG_TAG, "do'n in background");
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String movieJsonStr = null;
            //Fields required for the request URL
            final String MOVIE_BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
            final String SORT_PARAM = "sort_by";
            final String COUNT_PARAM = "vote_count.gte";
            final String APIKEY_PARAM = "api_key";

            try {
                //build the uri using set parameters.
                Uri builtUri = Uri.parse(MOVIE_BASE_URL)
                        .buildUpon()
                        .appendQueryParameter(SORT_PARAM, params[0])
                        .appendQueryParameter(COUNT_PARAM, "100")//only return if count >= 100
                        .appendQueryParameter(APIKEY_PARAM, getContext().getString(R.string.API_KEY))
                        .build();

                URL url = new URL(builtUri.toString());
                // Create the request to TheMovieDb, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                movieJsonStr = buffer.toString();

            } catch (IOException e) {
                Log.e("LOG_TAG", "Error ", e);
                // If the code didn't successfully get the movie data, there's no point in attempting
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("LOG_TAG", "Error closing stream", e);
                    }
                }
            }

            ArrayList<Movie> results = new ArrayList<>();
            try {
                results = getMovieDataFromJson(movieJsonStr);
            } catch (JSONException e) {
                Log.e("LOG_TAG", e.getMessage(), e);
                e.printStackTrace();
            }
            return results;
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> results) {
            try {

                if (results != null) {
                    //only update the UI if the results don't match the array
                    if (movieAdapter.getCount() == 0 || movieAdapter.getItem(0).getId() != results.get(0).getId()) {
                        movieAdapter.clear();
                        movieAdapter.add(results);
                        ((GridView) getView().findViewById(R.id.gridview)).setAdapter(movieAdapter);
                        movieAdapter.notifyDataSetChanged();
                    }
                }

            } catch (final Exception e) {
                Log.e(LOG_TAG, "Error updating display", e);
            }
        }

        public ArrayList<Movie> getMovieDataFromJson(String movieJsonStr)
                throws JSONException {

            // These are the names of the JSON objects that need to be extracted.
            final String TMDB_ID = "id";
            final String TMDB_TITLE = "original_title";
            final String TMDB_POPULARITY = "popularity";
            final String TMDB_VOTE_COUNT = "vote_count";
            final String TMDB_VOTE_AVG = "vote_average";
            final String TMDB_POSTER_PATH = "poster_path";
            final String TMDB_OVERVIEW = "overview";
            final String TMDB_RESULTS = "results";
            final String TMDB_RELEASE = "release_date";

            JSONObject movieJson = new JSONObject(movieJsonStr);
            JSONArray movieJsonArray = movieJson.getJSONArray(TMDB_RESULTS);

            ArrayList<Movie> tempArrayList = new ArrayList<Movie>();

            for (int i = 0; i < movieJsonArray.length(); i++) {
                JSONObject tempJsonObj = movieJsonArray.getJSONObject(i);
                String tempId = tempJsonObj.getString(TMDB_ID);

                //only create a movie object if an id string is returned.
                if (tempId != null && !tempId.isEmpty()) {
                    final Movie tempMovie = new Movie(Integer.parseInt(tempId), tempJsonObj.getString(TMDB_TITLE));
                    float popularity = Float.parseFloat(tempJsonObj.getString(TMDB_POPULARITY));
                    tempMovie.setPopularity(Math.round(popularity));
                    float voteAvg = Float.parseFloat(tempJsonObj.getString(TMDB_VOTE_AVG));
                    tempMovie.setVoteCount(Integer.parseInt(tempJsonObj.getString(TMDB_VOTE_COUNT)));
                    tempMovie.setVoteAvg(Math.round(voteAvg));
                    tempMovie.setOverview(tempJsonObj.getString(TMDB_OVERVIEW));
                    //build the path to use with Picasso
                    String urlString = "http://image.tmdb.org/t/p/w185/";
                    urlString += tempJsonObj.getString(TMDB_POSTER_PATH);
                    tempMovie.setPosterPath(urlString);
                    tempMovie.setReleaseDate(tempJsonObj.getString(TMDB_RELEASE));
                    tempArrayList.add(tempMovie);

                }
            }
            return tempArrayList;
        }
    }
}

