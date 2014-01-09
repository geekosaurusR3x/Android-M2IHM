package com.skad.android.androidm2ihm.view;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.DisplayMetrics;
import android.view.View;
import com.skad.android.androidm2ihm.R;
import com.skad.android.androidm2ihm.model.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
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
    private Ball mBall;
    private Hole mEnd;
    private SoundPool mSoundPool;
    private long mLastTime;
    private int mLevelResId;
    private int mIdSoundWall;
    private int mIdSoundGameOver;
    private int mIdSoundWin;
    private boolean mPaused = false;
    private double mRatioWidth = 1;
    private double mRatioHeight = 1;

    private ArrayList<Wall> mListWall = new ArrayList<Wall>();
    private ArrayList<Hole> mListHole = new ArrayList<Hole>();
    private ArrayList<Bullet> mListBullet = new ArrayList<Bullet>();
    private ArrayList<Gun> mListGun = new ArrayList<Gun>();

    private onLevelEventListener mParentActivity;

    public LevelView(Context context, int levelResId, int levelId) {
        super(context);
        try {
            mParentActivity = (onLevelEventListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement onLevelEventListener");
        }
        mLevelResId = levelResId;
        mScore = new Score(levelId);
        mScore.addObserver(this);
        mSoundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        mIdSoundWall = mSoundPool.load(context, R.raw.wall_hit, 1);
        mIdSoundGameOver = mSoundPool.load(context, R.raw.gameover, 1);
        mIdSoundWin = mSoundPool.load(context, R.raw.fins_level_completed, 1);
        loadLevel();
    }

    private void loadLevel() {
        // TODO Externalize! Views shouldn't load files
        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
        int screenWidth = metrics.widthPixels;
        int screenHeight = metrics.heightPixels;
        InputStream fileLevelStream = getResources().openRawResource(this.mLevelResId);
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
                    xPos = (int) (xPos * mRatioHeight);
                    yPos = (int) (yPos * mRatioWidth);
                    width = (int) (width * mRatioHeight);
                    height = (int) (height * mRatioWidth);

                    if (objectType.equals("screen")) { // screensize and ratio
                        mRatioWidth = (double) screenWidth / width;
                        mRatioHeight = (double) screenHeight / height;
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
                        gun.setRatioHeight(mRatioHeight);
                        gun.setRatioWidth(mRatioWidth);
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

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mPaused) {
            return;
        }

        update();
        for (final Wall wall : mListWall) {
            canvas.drawBitmap(wall.getSprite(), wall.getX(), wall.getY(), null);
        }
        for (final Hole hole : mListHole) {
            canvas.drawBitmap(hole.getSprite(), hole.getX(), hole.getY(), null);
        }
        for (final Gun gun : mListGun) {
            canvas.drawBitmap(gun.getSprite(), gun.getX(), gun.getY(), null);
        }
        for (final Bullet bullet : mListBullet) {
            canvas.drawBitmap(bullet.getSprite(), bullet.getX(), bullet.getY(), null);
        }

        canvas.drawBitmap(mEnd.getSprite(), mEnd.getX(), mEnd.getY(), null);
        canvas.drawBitmap(mBall.getSprite(), mBall.getX(), mBall.getY(), null);
        invalidate();
    }

    private void update() {
        for (final Hole mHole : mListHole) { // gameover
            if (mHole.intoHole(mBall)) {
                mSoundPool.play(mIdSoundGameOver, 1, 1, 0, 0, 1);
                mParentActivity.onLevelFailed();
            }
        }
        if (mEnd.intoHole(mBall)) {
            // win
            mSoundPool.play(mIdSoundWin, 1, 1, 0, 0, 1);
            mParentActivity.onLevelCompleted();
        }
        Bullet bullet;
        for (Iterator<Bullet> itBullet = mListBullet.iterator(); itBullet.hasNext(); ) {
            bullet = itBullet.next();
            if (bullet.getTimeToLive() <= 0) {
                mListBullet.remove(bullet);
            }
            bullet.forward();
            bullet.decreaseTimeToLive();
            bullet.decreseVelocity();
        }
        for (final Gun mGun : mListGun) {
            mGun.rotate(mBall.getX(), mBall.getY());
        }
        if (currentTimeMillis() - mLastTime > 1000) {
            for (final Gun mGun : mListGun) {
                Bullet mBullet = mGun.fire(mBall.getX(), mBall.getY());
                mBullet.setSprite(BitmapFactory.decodeResource(getResources(), R.drawable.bullet));
                mListBullet.add(mBullet);
            }
            mLastTime = currentTimeMillis();
        }
        // TODO Handle player failures (wall/bullet collision)
    }

    public void setForceX(float forceX) {
        int screenWidth = getWidth();

        int lastX = mBall.getX();
        mBall.applyForceX(forceX);
        if (collision() || mBall.getX() < 0 || mBall.getX() > screenWidth - mBall.getSprite().getWidth()) {
            mBall.setX(lastX);
        }
    }

    public void setForceY(float forceY) {
        int screenHeight = getHeight();

        int lastY = mBall.getY();
        mBall.applyForceY(forceY);
        if (collision() || mBall.getY() < 0 || mBall.getY() > screenHeight - mBall.getSprite().getHeight()) {
            mBall.setY(lastY);
        }
    }

    private boolean collision() {
        for (final Wall wall : mListWall) {
            if (mBall.intersects(wall)) {
                mSoundPool.play(mIdSoundWall, 1, 1, 0, 0, 1);
                mScore.collided();
                return true;
            }
        }
        for (final Bullet bullet : mListBullet) {
            if (mBall.intersects(bullet)) {
                mScore.collided();
                // Log.d(TAG, "X:" + mBall.getX() + " Y: " + mBall.getY());
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
