package com.skad.android.androidm2ihm.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import com.skad.android.androidm2ihm.R;
import com.skad.android.androidm2ihm.utils.ScreenOrientation;
import com.skad.android.androidm2ihm.view.Level;

import static android.os.SystemClock.sleep;

/**
 * Created by pschmitt on 12/19/13.
 */
public class LevelActivity extends Activity   implements SensorEventListener {
    private static final String TAG = "LevelActivity";

    private Level mLevel;
    private SensorManager mSensorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLevel = new Level(this);
        setContentView(R.layout.activity_level);
        View container = findViewById(R.id.container);
        ((ViewGroup)container).addView(mLevel);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        // Action bar
        ActionBar actionBar = getActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        if (sharedPref.getBoolean(getString(R.string.pref_key_hide_actionbar), false) && actionBar != null) {
            actionBar.hide();
        }
        if (sharedPref.getBoolean(getString(R.string.pref_key_force_landscape), false)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float xValue = 0.0f;
        float yValue = 0.0f;
        int orientation = ScreenOrientation.getOrientation(this);
        switch (orientation) {
            case ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE:
                xValue = event.values[1];
                yValue = event.values[0];
                break;
            case ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE:
                xValue = -event.values[1];
                yValue = -event.values[0];
                break;
            case ActivityInfo.SCREEN_ORIENTATION_PORTRAIT:
                xValue = -event.values[0];
                yValue = event.values[1];
                break;
            case ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT:
                xValue = event.values[0];
                yValue = -event.values[1];
                break;
        }
        mLevel.setForceX(xValue);
        mLevel.setForceY(yValue);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }
}
