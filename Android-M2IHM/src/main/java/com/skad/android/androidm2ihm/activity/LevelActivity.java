package com.skad.android.androidm2ihm.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import com.skad.android.androidm2ihm.R;
import com.skad.android.androidm2ihm.model.Score;
import com.skad.android.androidm2ihm.utils.ScreenOrientation;
import com.skad.android.androidm2ihm.view.Level;

/**
 * Created by pschmitt on 12/19/13.
 */
public class LevelActivity extends Activity implements SensorEventListener, Level.onLevelEventListener, DialogInterface.OnClickListener {
    private static final String TAG = "LevelActivity";

    private MediaPlayer mBackgroundMusic;
    private int mLevelId;
    private Level mLevel;
    private SensorManager mSensorManager;
    private boolean mPlayerFailed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);

        // Determine which level should be loaded
        mLevelId = getIntent().getIntExtra(getString(R.string.extra_key_level), 1);
        drawLevel();

        // Setup sensors
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        // Action bar
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            updateActionBarTitle();
        }
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        if (sharedPref.getBoolean(getString(R.string.pref_key_hide_actionbar), false) && actionBar != null) {
            actionBar.hide();
        }
        if (sharedPref.getBoolean(getString(R.string.pref_key_force_landscape), false)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    private void restartLevel() {
        drawLevel();
        updateActionBarTitle();
    }

    private void nextLevel() {
        if (mLevelId < 3) {
            mLevelId++;
            drawLevel();
            updateActionBarTitle();
        } else {
            // Player completed last level, exit
            finish();
        }
    }

    private void updateActionBarTitle() {

    }

    private void drawLevel() {
        this.mBackgroundMusic = MediaPlayer.create(this,  R.raw.background);
        this.mBackgroundMusic.setLooping(true);
        this.mBackgroundMusic.start();
        int levelResId = R.raw.lvl1;
        switch (mLevelId) {
            case 1:
                levelResId = R.raw.lvl1;
                break;
            case 2:
                levelResId = R.raw.lvl2;
                break;
            case 3:
                levelResId = R.raw.lvl3;
                break;
        }
        mLevel = new Level(this, levelResId, mLevelId);
        View container = findViewById(R.id.container);
        ((ViewGroup) container).addView(mLevel);
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
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "destroy");
        if (mBackgroundMusic != null) {
            mBackgroundMusic.release();
            mBackgroundMusic = null;
        }
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

    private void pauseGame() {
        mBackgroundMusic.stop();
        mSensorManager.unregisterListener(this);
        mLevel.pause();
    }

    private void saveHighScore() {
        Score score = mLevel.getScore();
        int highscore = score.getHighScore(this);
        if (score.getTotalScore() > highscore) {
            score.saveHighScore(this);
        }
    }

    @Override
    public void onLevelCompleted() {
        pauseGame();
        saveHighScore();
        mPlayerFailed = false;
        AlertDialog.Builder successDialogBuilder = new AlertDialog.Builder(this);
        successDialogBuilder.setTitle(getString(R.string.dialog_success_title));
        String msg = "";
        if (mLevelId < 3) {
            msg = String.format(getString(R.string.dialog_success_msg), mLevelId, mLevel.getScore().getTotalScore(), mLevel.getScore().getHighScore(this), (mLevelId + 1));
        } else {
            msg = getString(R.string.dialog_success_msg_alt);
        }
        successDialogBuilder.setMessage(msg);
        successDialogBuilder.setPositiveButton(android.R.string.ok, this);
        successDialogBuilder.setNeutralButton(R.string.dialog_success_restart, this);
        successDialogBuilder.create().show();
    }

    @Override
    public void onLevelFailed() {
        pauseGame();
        mPlayerFailed = true;
        AlertDialog.Builder successDialogBuilder = new AlertDialog.Builder(this);
        successDialogBuilder.setTitle(getString(R.string.dialog_failure_title));
        successDialogBuilder.setMessage(getString(R.string.dialog_failure_msg));
        successDialogBuilder.setPositiveButton(getString(android.R.string.ok), this);
        successDialogBuilder.setCancelable(true);
        successDialogBuilder.create().show();
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int btnId) {
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
        mLevel.resume();
        switch (btnId) {
            case DialogInterface.BUTTON_POSITIVE:
                if (!mPlayerFailed) {
                    nextLevel();
                    break;
                }
            case DialogInterface.BUTTON_NEUTRAL:
                restartLevel();
                break;
        }
    }
}