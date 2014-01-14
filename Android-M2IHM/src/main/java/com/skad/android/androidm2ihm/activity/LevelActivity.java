package com.skad.android.androidm2ihm.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.TextView;
import com.skad.android.androidm2ihm.R;
import com.skad.android.androidm2ihm.model.Level;
import com.skad.android.androidm2ihm.model.Score;
import com.skad.android.androidm2ihm.utils.LevelParser;
import com.skad.android.androidm2ihm.view.LevelView;

import java.io.File;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by pschmitt on 12/19/13.
 */
public class LevelActivity extends ActionBarActivity implements/* SensorEventListener,*/ DialogInterface.OnClickListener, DialogInterface.OnCancelListener, Observer/*, GameTask.OnGameEventListener*//*, LevelView.OnGameEventListener*/ {
    private static final String TAG = "LevelActivity";
    // private final Handler mHandler = new Handler();
    // Views
    private TextView mScoreView;
    private LevelView mLevelView;
    private boolean mPlayerFailed = false;
    private Score mScore;
    private int mLevelNumber;
    private String mLevelDir;
    private Level mLevel;
    private String mAudioPath;
    // Audio
    private boolean mMute;
    private MediaPlayer mBackgroundMusic;
    private SoundPool mSoundPool;
    private int mIdSoundWall;
    private int mIdSoundGameOver;
    private int mIdSoundWin;
    // Thread
    // private GameTask mGameTask;
/*
    private boolean mBound = false;
*/
    // Sensors
/*
    private SensorManager mSensorManager;
*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);

        // Determine which level should be loaded
        mLevelNumber = getIntent().getIntExtra(getString(R.string.extra_key_level), 1);
        mLevelDir = getIntent().getStringExtra(getString(R.string.extra_key_level_dir));
        mAudioPath = this.getExternalFilesDir(null)+ File.separator+"default"+File.separator;
        // Init score
        mScore = new Score(mLevelNumber);

        // Init task
        // mGameTask = new GameTask(this);

        // Show level view
        drawLevel();

        // Retain views
        mLevelView = (LevelView) findViewById(R.id.level_view);
        mScoreView = (TextView) findViewById(R.id.txt_score);
        mScoreView.setText(String.format(getString(R.string.score), mScore.getTotalScore()));

        // Setup sensors
/*
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
*/

        // Audio
        mSoundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        mIdSoundWall = mSoundPool.load(mAudioPath+"wall_hit.wav", 1);
        mIdSoundGameOver = mSoundPool.load(mAudioPath+"gameover.wav", 1);
        mIdSoundWin = mSoundPool.load(mAudioPath+"fins_level_completed.wav", 1);

        // Hide ActionBar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    private void restartLevel() {
/*
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
*/
        mScore.reset();
        mLevelView.startNewThread();
        drawLevel();
    }

    private void nextLevel() {
/*
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
*/
        if (mLevelNumber < Level.LEVEL_COUNT) {
            mLevelNumber++;
            mScore.setLevel(mLevelNumber);
            mScore.reset();
            mLevelView.startNewThread();
            drawLevel();
        } else {
            // Player completed last level, exit
            finish();
        }
    }

    private void pauseGame() {
        // mGameThread.setRunning(false);
        stopBackgroundMusicPlayback();
/*
        mSensorManager.unregisterListener(this);
*/
    }

    private void startBackgroundMusicPlayback() {
        mBackgroundMusic = MediaPlayer.create(this, Uri.fromFile(new File(mAudioPath+"background_music.wav")));
        mBackgroundMusic.setLooping(true);
        mBackgroundMusic.start();
    }

    private void stopBackgroundMusicPlayback() {
        if (mBackgroundMusic != null) {
            mBackgroundMusic.release();
            mBackgroundMusic = null;
        }
    }

    private void drawLevel() {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        mLevel = LevelParser.getLevelFromFile(this,mLevelDir ,mLevelNumber, metrics.widthPixels, metrics.heightPixels);
        if (!mMute) {
            startBackgroundMusicPlayback();
        }
    }

    private void unregisterObserver() {
        mLevel.deleteObserver(this);
        mScore.deleteObserver(this);
    }

    private void registerObserver() {
        mLevel.addObserver(this);
        mScore.addObserver(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
/*
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
*/
        // Sound preferences
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        mMute = sharedPrefs.getBoolean(getString(R.string.pref_key_mute), false);
        registerObserver();
    }

    @Override
    protected void onPause() {
        super.onPause();
        /*mSensorManager.unregisterListener(this);*/
        stopBackgroundMusicPlayback();
        unregisterObserver();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "destroy");
        stopBackgroundMusicPlayback();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    /*@Override
    public void onSensorChanged(SensorEvent event) {
        float xValue = event.values[1];
        float yValue = event.values[0];
        //mLevelView.setForce(xValue, yValue);
        mLevel.updatePlayerPosition(xValue, yValue);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }*/

    private void saveHighScore() {
        int highscore = mScore.getHighScore(this);
        if (mScore.getTotalScore() > highscore) {
            mScore.saveHighScore(this);
        }
    }

    // @Override
    public void onCollisionDetected(Level.EVENT collisionType) {
        mScore.collided();
        switch (collisionType) {
            case COLLISION_BULLET:
                Log.d(TAG, "Player got hit by a bullet!");
                // TODO
                break;
            case COLLISION_WALL:
                mScore.collided();
                if (!mMute) {
                    mSoundPool.play(mIdSoundWall, 1, 1, 0, 0, 1);
                }
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
        mPlayerFailed = true;
    }

    private void showSuccessDialog() {
        AlertDialog.Builder successDialogBuilder = new AlertDialog.Builder(this);
        successDialogBuilder.setTitle(getString(R.string.dialog_success_title));
        String msg;
        if (mLevelNumber < Level.LEVEL_COUNT) {
            msg = String.format(getString(R.string.dialog_success_msg), mLevelNumber, mScore.getTotalScore(), mScore.getHighScore(this), (mLevelNumber + 1));
        } else {
            msg = getString(R.string.dialog_success_msg_alt);
        }
        successDialogBuilder.setMessage(msg);
        successDialogBuilder.setPositiveButton(android.R.string.ok, this);
        successDialogBuilder.setNeutralButton(R.string.dialog_success_restart, this);
        successDialogBuilder.create().show();
        mPlayerFailed = false;
    }

    // @Override
    public void onLevelCompleted() {
        // Play sound
        if (!mMute) {
            mSoundPool.play(mIdSoundWin, 1, 1, 0, 0, 1);
        }
        // Pause
        pauseGame();
        saveHighScore();
        showSuccessDialog();
    }

    // @Override
    public void onLevelFailed() {
        // Play sound
        if (!mMute) {
            mSoundPool.play(mIdSoundGameOver, 1, 1, 0, 0, 1);
        }
        // Pause
        pauseGame();
        mPlayerFailed = true;
        showFailureDialog();
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
        // User cancelled dialog, restart game
        restartLevel();
    }

    @Override
    public void update(Observable observable, Object data) {
        /*Runnable action = null;*/
        if (observable instanceof Score) {
           /* action = new Runnable() {
                @Override
                public void run() {*/
            mScoreView.setText(String.format(getString(R.string.score), mScore.getTotalScore()));
                /*}
            };*/
        } else if (observable instanceof Level) {
            if (data instanceof Level.EVENT) {
                switch ((Level.EVENT) data) {
                    case GAME_OVER:
                        /*action = new Runnable() {
                            @Override
                            public void run() {*/
                        onLevelFailed();
                            /*}
                        };*/
                        break;
                    case GAME_SUCCESS:
                        /*action = new Runnable() {
                            @Override
                            public void run() {*/
                        onLevelCompleted();
                            /*}
                        };*/
                        break;
                    case COLLISION_BULLET:
                        /*action = new Runnable() {
                            @Override
                            public void run() {*/
                        onCollisionDetected(Level.EVENT.COLLISION_BULLET);
                            /*}
                        };*/
                    case COLLISION_WALL:
                        /*action = new Runnable() {
                            @Override
                            public void run() {*/
                        onCollisionDetected(Level.EVENT.COLLISION_WALL);
                            /*}
                        };*/
                        break;
                }
            }
        }
        /*mHandler.post(action);*/
    }
}