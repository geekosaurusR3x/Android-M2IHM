package com.skad.android.androidm2ihm;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class MainActivity extends Activity  implements SensorEventListener {

    private static final String TAG = "MainActivity";

    private SensorManager mSensorManager;

    private Level level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        level = new Level(this);
        setContentView(level);

        //DummyFragment dummyFragment = new DummyFragment();
        //if (savedInstanceState == null) {
        //    getFragmentManager().beginTransaction()
        //            .add(R.id.container, dummyFragment)
        //            .commit();
        //}
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE), SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // Carefull! This ouputs a lot of data as it gets called reaaaaally ofte
        Log.d(TAG, "Event[0]: " + Float.toString(event.values[0]));
        Log.d(TAG, "Event[1]" + Float.toString(event.values[1]));
        Log.d(TAG, "Event[2]" + Float.toString(event.values[2]));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    /**
     * A dummy fragment containing a simple view.
     */
    public static class DummyFragment extends Fragment {

        public DummyFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

}
