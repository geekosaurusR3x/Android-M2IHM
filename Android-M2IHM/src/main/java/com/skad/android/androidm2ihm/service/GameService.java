package com.skad.android.androidm2ihm.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.*;
import android.os.Process;
import android.util.DisplayMetrics;
import com.skad.android.androidm2ihm.R;
import com.skad.android.androidm2ihm.model.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

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
    // Business objects
    private Ball mBall;
    private Hole mEnd;
    private ArrayList<Wall> mListWall = new ArrayList<Wall>();
    private ArrayList<Hole> mListHole = new ArrayList<Hole>();
    private ArrayList<Bullet> mListBullet = new ArrayList<Bullet>();
    private ArrayList<Gun> mListGun = new ArrayList<Gun>();

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

    public void parseLevelFile(Context context, int level) {
        int levelResId = R.raw.lvl1;
        switch (level) {
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
        // TODO Externalize! Views shouldn't load files
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int screenWidth = metrics.widthPixels;
        int screenHeight = metrics.heightPixels;
        double ratioWidth = 1;
        double ratioHeight = 1;
        InputStream fileLevelStream = getResources().openRawResource(levelResId);
        BufferedReader reader = new BufferedReader(new InputStreamReader(fileLevelStream));
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                if (!line.substring(0, 1).matches("#")) {
                    String[] temp = line.split("/");
                    String objectType = temp[0];
                    int xPos = Integer.parseInt(temp[1]);
                    int yPos = Integer.parseInt(temp[2]);
                    int width = Integer.parseInt(temp[3]);
                    int height = Integer.parseInt(temp[4]);

                    // Ajusting for the screen size
                    xPos = (int) (xPos * ratioHeight);
                    yPos = (int) (yPos * ratioWidth);
                    width = (int) (width * ratioHeight);
                    height = (int) (height * ratioWidth);

                    if (objectType.equals("screen")) { // screensize and ratio
                        ratioWidth = (double) screenWidth / width;
                        ratioHeight = (double) screenHeight / height;
                    }
                    if (objectType.equals("p")) { // player (ball)
                        mBall = new Ball(xPos, yPos, width, height);
                        mBall.setSprite(BitmapFactory.decodeResource(getResources(), R.drawable.ball));
                    }
                    if (objectType.equals("h")) { // hole
                        Hole hole = new Hole(xPos, yPos, width, height);
                        hole.setSprite(BitmapFactory.decodeResource(getResources(), R.drawable.hole));
                        mListHole.add(hole);
                    }
                    if (objectType.equals("w")) { // wall (straight)
                        Wall wall = new Wall(xPos, yPos, width, height);
                        wall.setSprite(BitmapFactory.decodeResource(getResources(), R.drawable.wall_grey_texture));
                        mListWall.add(wall);
                    }
                    if (objectType.equals("abl")) { // wall (curved - bottom left)
                        Wall wall1 = new Wall(xPos, yPos, width, height);
                        wall1.setSprite(BitmapFactory.decodeResource(getResources(), R.drawable.arcwall_bottom_left));
                        mListWall.add(wall1);
                    }
                    if (objectType.equals("abr")) { // wall (curved - bottom right)
                        Wall wall2 = new Wall(xPos, yPos, width, height);
                        wall2.setSprite(BitmapFactory.decodeResource(getResources(), R.drawable.arcwall_bottom_right));
                        mListWall.add(wall2);
                    }
                    if (objectType.equals("atl")) { // wall (curved - top left)
                        Wall wall3 = new Wall(xPos, yPos, width, height);
                        wall3.setSprite(BitmapFactory.decodeResource(getResources(), R.drawable.arcwall_top_left));
                        mListWall.add(wall3);
                    }
                    if (objectType.equals("atr")) { // wall (curved - top right)
                        Wall wall4 = new Wall(xPos, yPos, width, height);
                        wall4.setSprite(BitmapFactory.decodeResource(getResources(), R.drawable.arcwall_top_right));
                        mListWall.add(wall4);
                    }
                    if (objectType.equals("g")) { // gun
                        Gun gun = new Gun(xPos, yPos, width, height);
                        gun.setSprite(BitmapFactory.decodeResource(getResources(), R.drawable.gun));
                        gun.setRatioHeight(ratioHeight);
                        gun.setRatioWidth(ratioWidth);
                        mListGun.add(gun);
                    }
                    if (objectType.equals("e")) {
                        mEnd = new Hole(xPos, yPos, width, height);
                        mEnd.setSprite(BitmapFactory.decodeResource(getResources(), R.drawable.cible));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fileLevelStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
            while (true) {
                synchronized (this) {
                    try {

                    } catch (Exception e) {
                    }
                }
            }
            // Stop the service using the startId, so that we don't stop
            // the service in the middle of handling another job
            // stopSelf(msg.arg1);
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
