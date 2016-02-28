/*
 * Copyright (C) 2016 Corey T. Willinger - Some Rights Reserved
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
     * Called when Activity is created.
     * @param savedInstanceState the state of class throughout the application life-cycle
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
     * @param item the user selected option from menu
     * @return  a boolean value
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
