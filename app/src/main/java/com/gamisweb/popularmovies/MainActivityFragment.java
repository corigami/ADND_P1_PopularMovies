package com.gamisweb.popularmovies;

import android.content.SharedPreferences;
import android.graphics.Path;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    SharedPreferences sharedPref;
    MovieAdapter movieAdapter;
    ArrayList<Movie> myMovies;

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //let main activity know that this fragment has menu items
        setHasOptionsMenu(true);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        movieAdapter = new MovieAdapter(getActivity(), myMovies);
        GridView gridview = (GridView) rootView.findViewById(R.id.gridview);
        gridview.setAdapter(movieAdapter);
        return rootView;
    }

    @Override
    public void onStart(){
        super.onStart();
        getMovieData();
    }

    public void getMovieData(){
        FetchMovieData movieTask = new FetchMovieData();
        movieTask.execute();

    }

    public class FetchMovieData extends AsyncTask<Void, Void, ArrayList<Movie>> {
        private final String LOG_TAG = FetchMovieData.class.getSimpleName();

        @Override
        protected ArrayList<Movie> doInBackground(Void... params) {
            Log.v(LOG_TAG, "do'n in background");
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String movieJsonStr = null;
            //Build our URL
            final String MOVIE_BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
            final String SORT_PARAM = "sort_by";
            final String APIKEY_PARAM = "api_key";

            //build URI and fetch data from themoviedb
            try
            {
                Uri builtUri = Uri.parse(MOVIE_BASE_URL)
                        .buildUpon()
                        .appendQueryParameter(SORT_PARAM, "popularity.desc")
                        .appendQueryParameter(APIKEY_PARAM, getString(R.string.API_KEY))
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
            try{
                results = getMovieDataFromJson(movieJsonStr);
            }catch(JSONException e){
                Log.e("LOG_TAG", e.getMessage(), e);
                e.printStackTrace();
            }
            return results;
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> results) {
            try {
                if(results != null) {
                    //update our movie adapter with the new collection of movies
                    movieAdapter.setMovies(results);
                }

            } catch (final Exception e) {
                Log.e(LOG_TAG, "Couldn't convert JSON object");
            }

        }

        /**
         * Take the String representing the complete forecast in JSON Format and
         * pull out the data we need to construct the Strings needed for the wireframes.
         * <p/>
         * Fortunately parsing is easy:  constructor takes the JSON string and converts it
         * into an Object hierarchy for us.
         */
        private ArrayList<Movie> getMovieDataFromJson(String movieJsonStr)
                throws JSONException {

            // These are the names of the JSON objects that need to be extracted.
            final String TMDB_ID = "id";
            final String TMDB_TITLE = "title";
            final String TMDB_POPULARITY = "popularity";
            final String TMDB_VOTE_COUNT = "vote_count";
            final String TMDB_VOTE_AVG = "vote_average";
            final String TMDB_POSTER_PATH = "poster_path";
            final String TMDB_RESULTS = "results";

            JSONObject movieJson = new JSONObject(movieJsonStr);
            JSONArray movieJsonArray = movieJson.getJSONArray(TMDB_RESULTS);

            ArrayList<Movie> tempArrayList = new ArrayList<Movie>();
            for(int i = 0; i < movieJsonArray.length(); i++){
                JSONObject tempJsonObj = movieJsonArray.getJSONObject(i);
                String tempId = tempJsonObj.getString(TMDB_ID);

                //only create a movie object if an id string is returned.
                if(tempId != null && !tempId.isEmpty()){
                    Movie tempMovie = new Movie(Integer.parseInt(tempId),tempJsonObj.getString(TMDB_TITLE));
                    float popularity = Float.parseFloat(tempJsonObj.getString(TMDB_POPULARITY));
                    tempMovie.setPopularity(Math.round(popularity));
                    tempMovie.setVoteCount(Integer.parseInt(tempJsonObj.getString(TMDB_VOTE_COUNT)));
                    float voteAvg = Float.parseFloat(tempJsonObj.getString(TMDB_VOTE_AVG));
                    tempMovie.setVoteAvg(Math.round(voteAvg));
                    tempMovie.setPosterPath(tempJsonObj.getString(TMDB_POSTER_PATH));

                    tempArrayList.add(tempMovie);
                }
            }
            return tempArrayList;


            /*
            for (int i = 0; i < weatherArray.length(); i++) {
                // For now, using the format "Day, description, hi/low"
                String day;
                String description;
                String highAndLow;

                // Get the JSON object representing the day
                JSONObject dayForecast = weatherArray.getJSONObject(i);

                // The date/time is returned as a long.  We need to convert that
                // into something human-readable, since most people won't read "1400356800" as
                // "this saturday".
                long dateTime;
                // Cheating to convert this to UTC time, which is what we want anyhow
                dateTime = dayTime.setJulianDay(julianStartDay + i);
                day = getReadableDateString(dateTime);

                // description is in a child array called "weather", which is 1 element long.
                JSONObject weatherObject = dayForecast.getJSONArray(OWM_WEATHER).getJSONObject(0);
                description = weatherObject.getString(OWM_DESCRIPTION);

                // Temperatures are in a child object called "temp".  Try not to name variables
                // "temp" when working with temperature.  It confuses everybody.
                JSONObject temperatureObject = dayForecast.getJSONObject(OWM_TEMPERATURE);
                double high = temperatureObject.getDouble(OWM_MAX);
                double low = temperatureObject.getDouble(OWM_MIN);

                highAndLow = formatHighLows(high, low);
                resultStrs[i] = day + " - " + description + " - " + highAndLow;
            }
            */

        }
    }


}
