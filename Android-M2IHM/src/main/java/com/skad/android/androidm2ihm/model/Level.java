package com.skad.android.androidm2ihm.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pschmitt on 1/9/14.
 */
public class Level {
    public static final int LEVEL_COUNT = 3;

    public static enum COLLISION {COLLISION_WALL, COLLISION_BULLET}

    ;
    // Business objects
    private int mLevelNumber;
    private Ball mBall;
    private Hole mEnd;
    private List<Wall> mWallList = new ArrayList<Wall>();
    private List<Hole> mHoleList = new ArrayList<Hole>();
    private List<Bullet> mBulletList = new ArrayList<Bullet>();
    private List<Gun> mGunList = new ArrayList<Gun>();
    private static Level mInstance;

    private Level() {
    }

    public static Level getInstance() {
        if (mInstance == null) {
            synchronized (Level.class) {
                mInstance = new Level();
            }
        }
        return mInstance;
    }

    public void setComponents(Ball ball, Hole end, List<Wall> wallList, List<Hole> holeList, List<Gun> gunList) {
        mBall = ball;
        mEnd = end;
        mWallList = wallList;
        mHoleList = holeList;
        mGunList = gunList;
    }

    /*public Level(int levelNumber, Ball ball, Hole end, List<Wall> wallList, List<Gun> gunList) {
        mLevelNumber = levelNumber;
        mBall = ball;
        mEnd = end;
        mWallList = wallList;
        mGunList = gunList;
    }*/

    public void reset() {
        mWallList.clear();
        mGunList.clear();
        mBulletList.clear();
        mHoleList.clear();
        mEnd = null;
        mBall = null;
    }

    public Ball getBall() {
        return mBall;
    }

    public void setBall(Ball ball) {
        this.mBall = mBall;
    }

    public Hole getEnd() {
        return mEnd;
    }

    public void setEnd(Hole end) {
        this.mEnd = end;
    }

    public List<Wall> getWallList() {
        return mWallList;
    }

    public void setWallList(List<Wall> wallList) {
        this.mWallList = wallList;
    }

    public List<Hole> getHoleList() {
        return mHoleList;
    }

    public void setHoleList(List<Hole> holeList) {
        this.mHoleList = holeList;
    }

    public List<Bullet> getBulletList() {
        return mBulletList;
    }

    public void setBulletList(List<Bullet> bulletList) {
        this.mBulletList = bulletList;
    }

    public boolean containsGuns() {
        return mGunList.isEmpty();
    }

    public List<Gun> getGunList() {
        return mGunList;
    }

    public void setGunList(List<Gun> gunList) {
        this.mGunList = gunList;
    }

    public int getLevelNumber() {
        return mLevelNumber;
    }

    public void setLevelNumber(int levelNumber) {
        this.mLevelNumber = levelNumber;
    }

    /**
     * Retrieve all the sprites!
     * Note: The order matters as the first object is drawn first (followers will be "above")
     *
     * @return An (Array)List containing all sprites
     */
    public List<SpriteObject> getAllSprites() {
        List<SpriteObject> spriteList = new ArrayList<SpriteObject>();

        spriteList.addAll(mBulletList);
        spriteList.addAll(mHoleList);
        spriteList.addAll(mGunList);
        spriteList.addAll(mWallList);
        spriteList.add(mEnd);
        spriteList.add(mBall);

        return spriteList;
    }
}
