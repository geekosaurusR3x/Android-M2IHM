package com.skad.android.androidm2ihm.utils;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.view.Surface;

/**
 * Created by pschmitt on 12/21/13.
 */
public class ScreenOrientation {
    private ScreenOrientation() {
    }

    /**
     * @param activity Context
     * @return ActivityInfo.$ScreenOrientation
     * @author: Uma sankar pradhan
     * Source: http://stackoverflow.com/a/15613855/1872036
     */
    public static int getOrientation(Activity activity) {
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int orientation = activity.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            if (rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_270) {
                return ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
            } else {
                return ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
            }
        }
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_90) {
                return ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
            } else {
                return ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
            }
        }
        return ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED;
    }
}
