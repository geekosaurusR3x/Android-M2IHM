package com.skad.android.androidm2ihm.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.skad.android.androidm2ihm.R;
import com.skad.android.androidm2ihm.model.Bullet;
import com.skad.android.androidm2ihm.model.Level;
import com.skad.android.androidm2ihm.model.Score;
import com.skad.android.androidm2ihm.model.Wall;
import com.skad.android.androidm2ihm.task.GameTask;
import com.skad.android.androidm2ihm.utils.LevelParser;
import com.skad.android.androidm2ihm.view.LevelView;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by pschmitt on 12/19/13.
 */
public class LevelActivity extends ActionBarActivity implements SensorEventListener, DialogInterface.OnClickListener, DialogInterface.OnCancelListener, Observer, GameTask.OnGameEventListener {
    private static final String TAG = "LevelActivity";
    boolean mBound = false;
    // Views
    private TextView mScoreView;
    // Service
    private int mLevelNumber;
    private LevelView mLevelView;
    private SensorManager mSensorManager;
    private boolean mPlayerFailed = false;
    private Level mLevel;
    private GameTask mGameTask;
    private SoundPool mSoundPool;
    private int mIdSoundWall;
    private int mIdSoundGameOver;
    private int mIdSoundWin;
    // Score
    private Score mScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);

        // Determine which level should be loaded
        mLevelNumber = getIntent().getIntExtra(getString(R.string.extra_key_level), 1);
        drawLevel();

        // Init score
        mScore = new Score(mLevelNumber);
        mScore.addObserver(this);

        // Retain views
        mScoreView = (TextView) findViewById(R.id.txt_score);
        mScoreView.setText(String.format(getString(R.string.score), mScore.getTotalScore()));

        // Setup sensors
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        // Audio
        mSoundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        mIdSoundWall = mSoundPool.load(this, R.raw.wall_hit, 1);
        mIdSoundGameOver = mSoundPool.load(this, R.raw.gameover, 1);
        mIdSoundWin = mSoundPool.load(this, R.raw.fins_level_completed, 1);

        // Hide ActionBar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    private void restartLevel() {
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
        mScore.reset();
        mGameTask.resume();
        drawLevel();
    }

    private void nextLevel() {
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
        if (mLevelNumber < Level.LEVEL_COUNT) {
            mLevelNumber++;
            mScore.nextLevel();
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
        // Start gamethread
        startGameThread();
    }

    private void startGameThread() {
        mGameTask = new GameTask(this, this);
        mGameTask.execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
        if (mGameTask.isCancelled()) {
            mGameTask.execute();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
        mGameTask.cancel(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "destroy");
    }

    public void setForceX(float forceX) {
        int screenWidth = mLevelView.getWidth();

        int lastX = mLevel.getBall().getX();
        mLevel.getBall().applyForceX(forceX);
        if (collision() || mLevel.getBall().getX() < 0 || mLevel.getBall().getX() > screenWidth - mLevel.getBall().getSprite().getWidth()) {
            mLevel.getBall().setX(lastX);
        }
    }

    public void setForceY(float forceY) {
        int screenHeight = mLevelView.getHeight();

        int lastY = mLevel.getBall().getY();
        mLevel.getBall().applyForceY(forceY);
        if (collision() || mLevel.getBall().getY() < 0 || mLevel.getBall().getY() > screenHeight - mLevel.getBall().getSprite().getHeight()) {
            mLevel.getBall().setY(lastY);
        }
    }

    private boolean collision() {
        for (final Wall wall : mLevel.getWallList()) {
            if (mLevel.getBall().intersects(wall)) {
                return true;
            }
        }
        for (final Bullet bullet : mLevel.getBulletList()) {
            if (mLevel.getBall().intersects(bullet)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float xValue = event.values[1];
        float yValue = event.values[0];
        setForceX(xValue);
        setForceY(yValue);
        // Request view refresh
        mLevelView.invalidate();
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
        mGameTask.pause();
        mSensorManager.unregisterListener(this);
    }

    private void saveHighScore() {
        int highscore = mScore.getHighScore(this);
        if (mScore.getTotalScore() > highscore) {
            mScore.saveHighScore(this);
        }
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
        // User cancelled dialog, restart
        restartLevel();
    }

    @Override
    public void update(Observable observable, Object data) {
        LevelActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mScoreView.setText(String.format(getString(R.string.score), mScore.getTotalScore()));
            }
        });
    }

    @Override
    public void onCollisionDetected(Level.COLLISION collisionType) {
        mScore.collided();
        Log.i(TAG, "COLLISION!");
        switch (collisionType) {
            case COLLISION_WALL:
                mSoundPool.play(mIdSoundWall, 1, 1, 0, 0, 1);
                break;
            case COLLISION_BULLET:
                // TODO
                break;
        }
    }

    private void showFailureDialog() {
        AlertDialog.Builder successDialogBuilder = new AlertDialog.Builder(this);
        successDialogBuilder.setTitle(getString(R.string.dialog_failure_title));
        successDialogBuilder.setMessage(getString(R.string.dialog_failure_msg));
        successDialogBuilder.setPositiveButton(getString(android.R.string.ok), this);
        successDialogBuilder.setCancelable(true);
        successDialogBuilder.setOnCancelListener(this);
        successDialogBuilder.create().show();
    }

    private void showSuccessDialog() {
        mPlayerFailed = false;
        AlertDialog.Builder successDialogBuilder = new AlertDialog.Builder(this);
        successDialogBuilder.setTitle(getString(R.string.dialog_success_title));
        String msg = "";
        if (mLevelNumber < Level.LEVEL_COUNT) {
            msg = String.format(getString(R.string.dialog_success_msg), mLevelNumber, mScore.getTotalScore(), mScore.getHighScore(this), (mLevelNumber + 1));
        } else {
            msg = getString(R.string.dialog_success_msg_alt);
        }
        successDialogBuilder.setMessage(msg);
        successDialogBuilder.setPositiveButton(android.R.string.ok, this);
        successDialogBuilder.setNeutralButton(R.string.dialog_success_restart, this);
        successDialogBuilder.create().show();
    }

    @Override
    public void onLevelCompleted() {
        // Play sound
        mSoundPool.play(mIdSoundWin, 1, 1, 0, 0, 1);
        // Pause
        pauseGame();
        saveHighScore();
        LevelActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showSuccessDialog();
            }
        });
    }

    @Override
    public void onLevelFailed() {
        // Play sound
        mSoundPool.play(mIdSoundGameOver, 1, 1, 0, 0, 1);
        // Pause
        pauseGame();
        mPlayerFailed = true;
        LevelActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showFailureDialog();
            }
        });
    }
}