package com.skad.android.androidm2ihm.activity;

import android.app.AlertDialog;
import android.content.*;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.skad.android.androidm2ihm.R;
import com.skad.android.androidm2ihm.model.Level;
import com.skad.android.androidm2ihm.model.Score;
import com.skad.android.androidm2ihm.service.GameService;
import com.skad.android.androidm2ihm.utils.LevelParser;
import com.skad.android.androidm2ihm.view.LevelView;

/**
 * Created by pschmitt on 12/19/13.
 */
public class LevelActivity extends ActionBarActivity implements SensorEventListener, LevelView.onLevelEventListener, DialogInterface.OnClickListener, DialogInterface.OnCancelListener {
    private static final String TAG = "LevelActivity";

    // Views
    private TextView mScoreView;

    // Service
    private GameService mService;
    boolean mBound = false;

    private int mLevelNumber;
    private LevelView mLevelView;
    private SensorManager mSensorManager;
    private boolean mPlayerFailed = false;
    private Level mLevel;

    @Override
    protected void onStart() {
        super.onStart();
        // Bind to LocalService
        Intent intent = new Intent(this, GameService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);

        // Determine which level should be loaded
        mLevelNumber = getIntent().getIntExtra(getString(R.string.extra_key_level), 1);
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
        if (mLevelNumber < Level.LEVEL_COUNT) {
            mLevelNumber++;
            drawLevel();
        } else {
            // Player completed last level, exit
            finish();
        }
    }

    private void drawLevel() {
        // Load level
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        mLevel = LevelParser.getLevelFromFile(this, mLevelNumber, metrics.widthPixels, metrics.heightPixels);
        mLevelView = new LevelView(this, mLevel);
        // Attach its view
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
        // Unbind service
        if (mBound)
            unbindService(mConnection);
        mSensorManager.unregisterListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "destroy");
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
        if (mLevelNumber < Level.LEVEL_COUNT) {
            msg = String.format(getString(R.string.dialog_success_msg), mLevelNumber, mLevelView.getScore().getTotalScore(), mLevelView.getScore().getHighScore(this), (mLevelNumber + 1));
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

    /**
     * Defines callbacks for service binding, passed to bindService()
     */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to GameService, cast the IBinder and get GameService instance
            GameService.LocalBinder binder = (GameService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
            Toast.makeText(LevelActivity.this, "GameService started", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            Toast.makeText(LevelActivity.this, "GameService stopped", Toast.LENGTH_LONG).show();
            mBound = false;
        }
    };

}