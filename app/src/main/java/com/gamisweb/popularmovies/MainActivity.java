/*
 * Copyright (C) 2016 Corey T. Willinger - No Rights Reserved
 */

package com.gamisweb.popularmovies;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

/**
 * Proivdes starting point for application.  Must be identified in Manifest as such.
 */
public class MainActivity extends ActionBarActivity {
    private SharedPreferences sharedPref;

    /**
     * Called when Activity is created.  Loads {@link MainActivityFragment} on start and sets
     * shared preferences object.
     * @param savedInstanceState stores the app state during app life-cycle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new MainActivityFragment())
                    .commit();
        }
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
    }

    /**
     * Loads when optionsMenu is created by underlying system.
     * @param menu stores menu object to be inflated,
     * @return boolean value (default true)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Provides logic to be executed when menu item is selected.
     * @param item the user selected option from settings menu
     * @return the response from parent method
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

