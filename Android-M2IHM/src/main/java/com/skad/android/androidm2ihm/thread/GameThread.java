package com.skad.android.androidm2ihm.thread;

import com.skad.android.androidm2ihm.model.Level;

/**
 * Created by pschmitt on 1/12/14.
 */
public class GameThread extends Thread {
    private static final String TAG = "GameThread";
    private boolean mRunning = true;
    private Level mLevel;

    public void pause() {
        mRunning = false;
    }

    @Override
    public void run() {
        mLevel = Level.getInstance();
        synchronized (this) {
            while (mRunning) {
                mLevel.updateBullets();
            }
        }
    }
}
