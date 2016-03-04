/*
 * Copyright (C) 2016 Corey T. Willinger - No Rights Reserved
 */

package com.gamisweb.popularmovies;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

/**
 * Helper class for display attributes
 */
public class DisplayUtil{
    Context context;
    Display myDisplay;
    Point size = new Point();
    int orientation;

    /**
     * Constructs a DisplayUtil object
     * @param _context the current views context
     */
    DisplayUtil(Context _context){
        context = _context;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        myDisplay = wm.getDefaultDisplay();
        myDisplay.getSize(size);
        orientation = getScreenOrientation();

    }

    /**
     * Constructs a DisplayUtil object
     * @param _display display object to get size and orientation from
     */
    DisplayUtil(Display _display){
        myDisplay = _display;
        myDisplay.getSize(size);
        orientation = getScreenOrientation();
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setMyDisplay(Display myDisplay) {
        this.myDisplay = myDisplay;
    }

    public int getOrientation() { return orientation; }

    public Point getDisplaySize() {
        return size;
    }

    /**
     * Returns the current screen's orientation.  1 if portrait, 2 if landscape
     * @return the current screen's rotation
     */
    public int getScreenOrientation()
    {
        if(size.x < size.y){
            orientation = Configuration.ORIENTATION_PORTRAIT;
        }else {
            orientation = Configuration.ORIENTATION_LANDSCAPE;
        }
        return orientation;
    }
}
