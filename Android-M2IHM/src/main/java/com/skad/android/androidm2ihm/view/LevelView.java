package com.skad.android.androidm2ihm.view;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.media.AudioManager;
import android.media.SoundPool;
import android.view.View;
import com.skad.android.androidm2ihm.R;
import com.skad.android.androidm2ihm.model.*;

import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;

import static java.lang.System.currentTimeMillis;

/**
 * Created by skad on 19/12/13.
 */
public class LevelView extends View implements Observer {

    private static final String TAG = "LevelView";

    private Score mScore;
    // Bitmap background;
    private SoundPool mSoundPool;
    private long mLastTime;
    private int mIdSoundWall;
    private int mIdSoundGameOver;
    private int mIdSoundWin;
    private boolean mPaused = false;
    private Level mLevel;

    private onLevelEventListener mParentActivity;

    public LevelView(Context context, Level level) {
        super(context);
        try {
            mParentActivity = (onLevelEventListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement onLevelEventListener");
        }
        mLevel = level;
        mScore = new Score(level.getLevelNumber());
        mScore.addObserver(this);
        mSoundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        mIdSoundWall = mSoundPool.load(context, R.raw.wall_hit, 1);
        mIdSoundGameOver = mSoundPool.load(context, R.raw.gameover, 1);
        mIdSoundWin = mSoundPool.load(context, R.raw.fins_level_completed, 1);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mPaused) {
            return;
        }

        update();
        for (final Wall wall : mLevel.getWallList()) {
            canvas.drawBitmap(wall.getSprite(), wall.getX(), wall.getY(), null);
        }
        for (final Hole hole : mLevel.getHoleList()) {
            canvas.drawBitmap(hole.getSprite(), hole.getX(), hole.getY(), null);
        }
        for (final Gun gun : mLevel.getGunList()) {
            canvas.drawBitmap(gun.getSprite(), gun.getX(), gun.getY(), null);
        }
        for (final Bullet bullet : mLevel.getBulletList()) {
            canvas.drawBitmap(bullet.getSprite(), bullet.getX(), bullet.getY(), null);
        }

        canvas.drawBitmap(mLevel.getBall().getSprite(), mLevel.getBall().getX(), mLevel.getBall().getY(), null);
        canvas.drawBitmap(mLevel.getEnd().getSprite(), mLevel.getEnd().getX(), mLevel.getEnd().getY(), null);
        invalidate();
    }

    private void update() {
        for (final Hole mHole : mLevel.getHoleList()) { // gameover
            if (mHole.intoHole(mLevel.getBall())) {
                mSoundPool.play(mIdSoundGameOver, 1, 1, 0, 0, 1);
                mParentActivity.onLevelFailed();
            }
        }
        if (mLevel.getEnd().intoHole(mLevel.getBall())) {
            // win
            mSoundPool.play(mIdSoundWin, 1, 1, 0, 0, 1);
            mParentActivity.onLevelCompleted();
        }
        Bullet bullet;
        for (Iterator<Bullet> itBullet = mLevel.getBulletList().iterator(); itBullet.hasNext(); ) {
            bullet = itBullet.next();
            if (bullet.getTimeToLive() <= 0) {
                mLevel.getBulletList().remove(bullet);
            }
            bullet.forward();
            bullet.decreaseTimeToLive();
            bullet.decreseVelocity();
        }
        for (final Gun mGun : mLevel.getGunList()) {
            mGun.rotate(mLevel.getBall().getX(), mLevel.getBall().getY());
        }
        if (currentTimeMillis() - mLastTime > 1000) {
            for (final Gun mGun : mLevel.getGunList()) {
                Bullet mBullet = mGun.fire(mLevel.getBall().getX(), mLevel.getBall().getY());
                mBullet.setSprite(BitmapFactory.decodeResource(getResources(), R.drawable.bullet));
                mLevel.getBulletList().add(mBullet);
            }
            mLastTime = currentTimeMillis();
        }
        // TODO Handle player failures (wall/bullet collision)
    }

    public void setForceX(float forceX) {
        int screenWidth = getWidth();

        int lastX = mLevel.getBall().getX();
        mLevel.getBall().applyForceX(forceX);
        if (collision() || mLevel.getBall().getX() < 0 || mLevel.getBall().getX() > screenWidth - mLevel.getBall().getSprite().getWidth()) {
            mLevel.getBall().setX(lastX);
        }
    }

    public void setForceY(float forceY) {
        int screenHeight = getHeight();

        int lastY = mLevel.getBall().getY();
        mLevel.getBall().applyForceY(forceY);
        if (collision() || mLevel.getBall().getY() < 0 || mLevel.getBall().getY() > screenHeight - mLevel.getBall().getSprite().getHeight()) {
            mLevel.getBall().setY(lastY);
        }
    }

    private boolean collision() {
        for (final Wall wall : mLevel.getWallList()) {
            if (mLevel.getBall().intersects(wall)) {
                mSoundPool.play(mIdSoundWall, 1, 1, 0, 0, 1);
                mScore.collided();
                return true;
            }
        }
        for (final Bullet bullet : mLevel.getBulletList()) {
            if (mLevel.getBall().intersects(bullet)) {
                mScore.collided();
                // Log.d(TAG, "X:" + mLevel.getBall().getX() + " Y: " + mLevel.getBall().getY());
                // setForceX(-10);
                // setForceY(-10);
                return true;
            }
        }
        return false;
    }

    public void pause() {
        mPaused = true;
    }

    public void resume() {
        mPaused = false;
    }

    public Score getScore() {
        return mScore;
    }

    @Override
    public void update(Observable observable, Object data) {
        mParentActivity.onScoreUpdated();
    }

    public interface onLevelEventListener {
        public void onLevelCompleted();

        public void onLevelFailed();

        public void onScoreUpdated();
    }
}
