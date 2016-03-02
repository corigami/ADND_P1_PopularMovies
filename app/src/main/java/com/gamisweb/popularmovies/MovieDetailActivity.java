/*
 * Copyright (C) 2016 Corey T. Willinger - No Rights Reserved
 */

package com.gamisweb.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;

/**
 * Activity that maintains the {@link MovieDetailActivityFragment} that displays detailed information
 * about movie selected from the main screen. It loads {@code MovieDetailActivityFragment} when created.
 */
public class MovieDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movie_detail_container, new MovieDetailActivityFragment())
                    .commit();
        }
    }

    /**
     * Called when the option menu is created.
     * @param menu the menu object to be inflated to.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_frag_menu, menu);
        return true;
    }
}
