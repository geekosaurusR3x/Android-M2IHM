package com.skad.android.androidm2ihm.service;

import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.*;
import android.os.Process;
import com.skad.android.androidm2ihm.R;
import com.skad.android.androidm2ihm.model.Bullet;
import com.skad.android.androidm2ihm.model.Gun;
import com.skad.android.androidm2ihm.model.Level;

import static java.lang.System.currentTimeMillis;

/**
 * Created by pschmitt on 1/8/14.
 */
public class GameService extends Service {
    // Binder given to clients
    private final IBinder mBinder = new LocalBinder();
    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;
    // Music player
    private MediaPlayer mBackgroundMusic;
    private Level mLevel;
    private long mLastBulletFiredTime;

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        stopBackgroundMusicPlayback();
        return super.onUnbind(intent);
    }

    @Override
    public void onCreate() {
        HandlerThread thread = new HandlerThread("ServiceStartArguments",
                Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        // Get the HandlerThread's Looper and use it for our Handler
        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);
        mLevel = Level.getInstance();
        startBackgroundMusicPlayback();
        mServiceLooper.getThread().run();
        // fireBullets();
    }

    public void fireBullets() {
        synchronized (this) {
            for (; ; ) {
                for (final Gun mGun : mLevel.getGunList()) {
                    Bullet mBullet = mGun.fire(mLevel.getBall().getX(), mLevel.getBall().getY());
                    mBullet.setSprite(BitmapFactory.decodeResource(getResources(), R.drawable.bullet));
                    mLevel.getBulletList().add(mBullet);
                    // A new bullet was fired, request update
                    // invalidate();
                }
                try {
                    wait(currentTimeMillis() + 10 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void startBackgroundMusicPlayback() {
        mBackgroundMusic = MediaPlayer.create(this, R.raw.background_music);
        mBackgroundMusic.setLooping(true);
        mBackgroundMusic.start();
    }

    public void stopBackgroundMusicPlayback() {
        if (mBackgroundMusic != null) {
            mBackgroundMusic.release();
            mBackgroundMusic = null;
        }
    }

    // Handler that receives messages from the thread
    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            synchronized (this) {
                try {

                } catch (Exception e) {
                }
            }
            // Stop the service using the startId, so that we don't stop
            // the service in the middle of handling another job
            stopSelf(msg.arg1);
        }
    }

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        public GameService getService() {
            // Return this instance of GameService so clients can call public methods
            return GameService.this;
        }
    }
}
