package com.skad.android.androidm2ihm.task;

import android.content.Context;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Looper;
import android.util.Log;
import com.skad.android.androidm2ihm.model.Gun;
import com.skad.android.androidm2ihm.model.Level;

/**
 * Created by pschmitt on 1/9/14.
 */
public class GameTask extends AsyncTask<Void, Void, Void> /*implements SensorEventListener*/ {
    private static final String TAG = "GameTask";
    private Context mContext;
    private long mBulletFiredLastTime;
    private boolean mPaused = false;
    private Level mLevel;
    private OnGameEventListener mListener;
    // Sensors
    private SensorManager mSensorManager;

    public GameTask(Context context) {
        mContext = context;
        try {
            mListener = (OnGameEventListener) context;
        } catch (ClassCastException e) {
            Log.e("Gametask", "Activity must implement OnGameEventListener");
        }
        mLevel = Level.getInstance();
        // Setup sensors
        mSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
    }

    public void pause() {
        mPaused = true;
    }

    public void resume() {
        mPaused = false;
    }

    @Override
    protected Void doInBackground(Void... params) {
        Looper.prepare();
        synchronized (this) {
/*
            mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
*/
            while (!mPaused) {
                checkCollisions();
                fireBullets();
            }
        }
        return null;
    }

    private void checkCollisions() {
        /*if (mLevel.playerWasHitByBullet()) { // Player got hit by a bullet
            // mListener.onCollisionDetected(Level.EVENT.COLLISION_BULLET);
        }
        if (mLevel.playerHitWall()) { // Player hit wall
            // mListener.onCollisionDetected(Level.EVENT.COLLISION_WALL);
        }
        if (mLevel.playerReachedEnd()) { // Epic win
            mPaused = true;
            // mListener.onLevelCompleted();
        }
        if (mLevel.playerFellIntoHole()) { // Game over
            mPaused = true;
            // mListener.onLevelFailed();
        }*/
    }

    private void fireBullets() {
        if (!mLevel.containsGuns()) {
            return;
        }
        if (System.currentTimeMillis() - mBulletFiredLastTime > 1000) {
            for (Gun gun : mLevel.getGunList()) {
                gun.fire((int) mLevel.getBall().getXPos(), (int) mLevel.getBall().getYPos());
                mBulletFiredLastTime = System.currentTimeMillis();
                // A new bullet was fired, request update
                // mLevelView.invalidate();
            }
        }
    }

    /*@Override
    public void onSensorChanged(SensorEvent event) {
        float xValue = event.values[1];
        float yValue = event.values[0];
        //mLevelView.setForce(xValue, yValue);
        mLevel.updatePlayerPosition(xValue, yValue);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }*/

    public interface OnGameEventListener {
        void onCollisionDetected(Level.EVENT eventType);

        void onLevelCompleted();

        void onLevelFailed();
    }
}
