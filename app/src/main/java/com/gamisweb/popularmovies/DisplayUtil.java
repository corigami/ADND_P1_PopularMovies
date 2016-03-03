package com.gamisweb.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

public class DisplayUtil{
    Context context;
    Display myDisplay;
    Point size = new Point();
    int orientation;

    DisplayUtil(Context _context){
        context = _context;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        myDisplay = wm.getDefaultDisplay();
        myDisplay.getSize(size);
        orientation = getScreenOrientation();

    }

    DisplayUtil(Display _display){
        myDisplay = _display;
        myDisplay.getSize(size);
        orientation = getScreenOrientation();
    }

    public int getOrientation() {
        return orientation;
    }

    public int getScreenOrientation()
    {
        if(size.x < size.y){
            orientation = Configuration.ORIENTATION_PORTRAIT;
        }else {
            orientation = Configuration.ORIENTATION_LANDSCAPE;
        }
        return orientation;
    }

    public Point getDisplaySize() {
        return size;
    }
}
