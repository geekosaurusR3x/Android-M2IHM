package com.skad.android.androidm2ihm.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.skad.android.androidm2ihm.R;
import com.skad.android.androidm2ihm.model.Score;
import com.skad.android.androidm2ihm.view.LevelView;

/**
 * Created by pschmitt on 12/19/13.
 */
public class LevelActivity extends ActionBarActivity implements SensorEventListener, LevelView.onLevelEventListener, DialogInterface.OnClickListener, DialogInterface.OnCancelListener {
    private static final String TAG = "LevelActivity";

    // Views
    private TextView mScoreView;

    private MediaPlayer mBackgroundMusic;
    private int mLevelId;
    private LevelView mLevelView;
    private SensorManager mSensorManager;
    private boolean mPlayerFailed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);

        // Determine which level should be loaded
        mLevelId = getIntent().getIntExtra(getString(R.string.extra_key_level), 1);
        drawLevel();

        // Retain views
        mScoreView = (TextView) findViewById(R.id.txt_score);
        mScoreView.setText(String.format(getString(R.string.score), mLevelView.getScore().getTotalScore()));

        // Setup sensors
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        // Hide ActionBar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    private void restartLevel() {
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
        mLevelView.resume();
        drawLevel();
    }

    private void nextLevel() {
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
        if (mLevelId < 3) {
            mLevelId++;
            drawLevel();
        } else {
            // Player completed last level, exit
            finish();
        }
    }

    private void drawLevel() {
        this.mBackgroundMusic = MediaPlayer.create(this,  R.raw.background_music);
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
        mLevelView = new LevelView(this, levelResId, mLevelId);
        View container = findViewById(R.id.container);
        ((ViewGroup) container).addView(mLevelView);

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
        if (mBackgroundMusic != null) {
            mBackgroundMusic.release();
            mBackgroundMusic = null;
        }
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
        float xValue = event.values[1];
        float yValue = event.values[0];
        mLevelView.setForceX(xValue);
        mLevelView.setForceY(yValue);
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
        mLevelView.pause();
    }

    private void saveHighScore() {
        Score score = mLevelView.getScore();
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
            msg = String.format(getString(R.string.dialog_success_msg), mLevelId, mLevelView.getScore().getTotalScore(), mLevelView.getScore().getHighScore(this), (mLevelId + 1));
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
        successDialogBuilder.setOnCancelListener(this);
        successDialogBuilder.create().show();
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int btnId) {
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

    @Override
    public void onCancel(DialogInterface dialog) {
        restartLevel();
    }

    @Override
    public void onScoreUpdated() {
        mScoreView.setText(String.format(getString(R.string.score), mLevelView.getScore().getTotalScore()));
    }
}