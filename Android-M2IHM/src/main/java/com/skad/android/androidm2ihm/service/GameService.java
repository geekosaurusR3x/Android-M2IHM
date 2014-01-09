package com.skad.android.androidm2ihm.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.*;
import android.os.Process;
import com.skad.android.androidm2ihm.R;

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

    @Override
    public IBinder onBind(Intent intent) {
        mBackgroundMusic = MediaPlayer.create(this, R.raw.background_music);
        mBackgroundMusic.setLooping(true);
        mBackgroundMusic.start();
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        mBackgroundMusic.release();
        mBackgroundMusic = null;
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
    }

    // Handler that receives messages from the thread
    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            // Normally we would do some work here, like download a file.
            // For our sample, we just sleep for 5 seconds.
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
            // Return this instance of LocalService so clients can call public methods
            return GameService.this;
        }
    }
}
