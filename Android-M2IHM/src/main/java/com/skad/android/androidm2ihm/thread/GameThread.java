package com.skad.android.androidm2ihm.thread;

import android.util.Log;
import com.skad.android.androidm2ihm.model.*;

import java.util.Iterator;

/**
 * Created by pschmitt on 1/12/14.
 */
public class GameThread extends Thread {
    private static final String TAG = "GameThread";
    private static final int FIRE_RATE = 1000;
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
                Ball ball = mLevel.getBall();
                for (Gun gun : mLevel.getGunList()) {
                    long currentTimeMs = System.currentTimeMillis();
                    gun.rotate((int) ball.getXPos(), (int) ball.getYPos());
                    if (currentTimeMs - gun.getLastTimeFired() > FIRE_RATE) {
                        gun.fire((int) ball.getXPos(), (int) ball.getYPos());
                        gun.setLastTimeFired(currentTimeMs);
                        Log.d(TAG, "Fired bullet #" + gun.getBulletList().size());
                    }
                    Iterator<Bullet> bulletIterator = gun.getBulletList().iterator();
                    while (bulletIterator.hasNext()) {
                        Bullet bullet = bulletIterator.next();
                        bullet.forward();
                        //bullet.decreaseVelocity();
                        for (final Wall wall : mLevel.getWallList()) {
                            if (bullet.intersects(wall)) {
                                bulletIterator.remove();
                            }
                        }
                    }
                }
            }
        }
    }
}
