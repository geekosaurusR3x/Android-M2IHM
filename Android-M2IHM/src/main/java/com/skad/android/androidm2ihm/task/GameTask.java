package com.skad.android.androidm2ihm.task;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import com.skad.android.androidm2ihm.R;
import com.skad.android.androidm2ihm.model.*;

/**
 * Created by pschmitt on 1/9/14.
 */
public class GameTask extends AsyncTask<Void, Void, Void> {
    private static final String TAG = "GameTask";
    private Context mContext;
    private long mBulletFiredLastTime;
    private boolean mPaused = false;
    private Level mLevel;
    private OnGameEventListener mListener;

    public GameTask(Context context, OnGameEventListener listener) {
        mContext = context;
        mListener = listener;
        mLevel = Level.getInstance();
    }

    public void pause() {
        mPaused = true;
    }

    public void resume() {
        mPaused = false;
    }

    @Override
    protected Void doInBackground(Void... params) {
        synchronized (this) {
            while (true) {
                if (!mPaused) {
                    checkCollisions();
                    for (final Gun gun : mLevel.getGunList()) {
                        fireBullet(gun);
                    }
                }
            }
        }
    }

    /*public void setForceX(float forceX) {
        int screenWidth = mContext.getResources().getDisplayMetrics().widthPixels;
        int lastX = mLevel.getBall().getX();
        mLevel.getBall().applyForceX(forceX);
        if (collision() || mLevel.getBall().getX() < 0 || mLevel.getBall().getX() > screenWidth - mLevel.getBall().getSprite().getWidth()) {
            mLevel.getBall().setX(lastX);
        }
    }

    public void setForceY(float forceY) {
        int screenHeight = mContext.getResources().getDisplayMetrics().heightPixels;
        int lastY = mLevel.getBall().getY();
        mLevel.getBall().applyForceY(forceY);
        if (collision() || mLevel.getBall().getY() < 0 || mLevel.getBall().getY() > screenHeight - mLevel.getBall().getSprite().getHeight()) {
            mLevel.getBall().setY(lastY);
        }
    }*/

    private boolean checkWallCollision() {
        for (final Wall wall : mLevel.getWallList()) {
            if (mLevel.getBall().intersects(wall)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkBulletCollision() {
        for (final Bullet bullet : mLevel.getBulletList()) {
            if (mLevel.getBall().intersects(bullet)) {
                // Log.d(TAG, "X:" + mLevel.getBall().getX() + " Y: " + mLevel.getBall().getY());
                return true;
            }
        }
        return false;
    }

    private boolean checkHoleCollision() {
        for (final Hole mHole : mLevel.getHoleList()) { //gameover
            if (mHole.intoHole(mLevel.getBall())) {
                return true;
            }
        }
        return false;
    }

    private boolean checkEndCollision() {
        return mLevel.getEnd().intoHole(mLevel.getBall());
    }

    private void checkCollisions() {
        if (checkWallCollision()) {
            mListener.onCollisionDetected(Level.COLLISION.COLLISION_WALL);
        }
        if (checkBulletCollision()) {
            mListener.onCollisionDetected(Level.COLLISION.COLLISION_BULLET);
        }
        if (checkHoleCollision()) {
            mListener.onLevelFailed();
        }
        if (checkEndCollision()) {
            mListener.onLevelCompleted();
        }
        Log.d(TAG, "Collision check...");
    }

    private void fireBullet(Gun gun) {
        if (System.currentTimeMillis() - mBulletFiredLastTime > 1000) {
            Bullet mBullet = gun.fire(mLevel.getBall().getX(), mLevel.getBall().getY());
            mBullet.setSprite(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.bullet));
            mLevel.getBulletList().add(mBullet);
            mBulletFiredLastTime = System.currentTimeMillis();
            // A new bullet was fired, request update
            // mLevelView.invalidate();
        }
    }

    public interface OnGameEventListener {
        void onCollisionDetected(Level.COLLISION collisionType);

        void onLevelCompleted();

        void onLevelFailed();
    }
}
