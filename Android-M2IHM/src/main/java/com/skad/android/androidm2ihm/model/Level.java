package com.skad.android.androidm2ihm.model;

import android.content.Context;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;

/**
 * Created by pschmitt on 1/9/14.
 */
public class Level extends Observable {

    public static final int LEVEL_COUNT = 3;
    private static final String TAG = "Level,";
    private static Level mInstance;
    // Business objects
    private int mLevelNumber;
    private Ball mBall;
    private Target mTarget;
    private List<Wall> mWallList = new ArrayList<Wall>();
    private List<Hole> mHoleList = new ArrayList<Hole>();
    private List<Gun> mGunList = new ArrayList<Gun>();
    private String mPath;

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

    public void setComponents(Ball ball, Target end, List<Wall> wallList, List<Hole> holeList, List<Gun> gunList) {
        mBall = ball;
        mTarget = end;
        mWallList = wallList;
        mHoleList = holeList;
        mGunList = gunList;
    }

    public void reset() {
        mWallList.clear();
        mGunList.clear();
        mHoleList.clear();
        mTarget = null;
        mBall = null;
    }

    public void remove(SpriteObject objectToRemove) {
        if (objectToRemove instanceof Ball) {
            mBall = null;
        } else if (objectToRemove instanceof Target) {
            mTarget = null;
        } else if (objectToRemove instanceof Wall) {
            mWallList.remove(objectToRemove);
        } else if (objectToRemove instanceof Gun) {
            mGunList.remove(objectToRemove);
        } else if (objectToRemove instanceof Hole) {
            mHoleList.remove(objectToRemove);
        }else if (objectToRemove instanceof WallArc) {
            mWallList.remove(objectToRemove);
        }
    }

    public void add(SpriteObject newObject) {
        if (newObject instanceof Ball) {
            mBall = (Ball) newObject;
        } else if (newObject instanceof Target) {
            mTarget = (Target) newObject;
        } else if (newObject instanceof Wall) {
            mWallList.add((Wall) newObject);
        } else if (newObject instanceof Gun) {
            mGunList.add((Gun) newObject);
        } else if (newObject instanceof Hole) {
            mHoleList.add((Hole) newObject);
        }else if (newObject instanceof WallArc) {
            mWallList.add((WallArc) newObject);
        }
    }

    public Ball getBall() {
        return mBall;
    }

    public void setBall(Ball ball) {
        mBall = ball;
    }

    public Target getTarget() {
        return mTarget;
    }

    public void setTarget(Target end) {
        mTarget = end;
    }

    public List<Wall> getWallList() {
        return mWallList;
    }

    public void setWallList(List<Wall> wallList) {
        mWallList = wallList;
    }

    public List<Hole> getHoleList() {
        return mHoleList;
    }

    public void setHoleList(List<Hole> holeList) {
        mHoleList = holeList;
    }

    public String getPath() {
        return mPath;
    }

    public void setPath(Context context, String mPath) {


        this.mPath = context.getExternalFilesDir(null)+ File.separator+mPath ;
    }

    public boolean containsGuns() {
        return mGunList.isEmpty();
    }

    public List<Gun> getGunList() {
        return mGunList;
    }

    public void setGunList(List<Gun> gunList) {
        mGunList = gunList;
    }

    public int getLevelNumber() {
        return mLevelNumber;
    }

    public void setLevelNumber(int levelNumber) {
        mLevelNumber = levelNumber;
    }

    public boolean playerReachedEnd() {
        return mTarget.intoHole(mBall);
    }

    public boolean playerFellIntoHole() {
        for (final Hole hole : mHoleList) {
            if (hole.intoHole(mBall)) {
                return true;
            }
        }
        return false;
    }

    public Vector2D playerHitWall() {
        for (final Wall wall : mWallList) {
            Vector2D pos = mBall.intersects(wall);
            if (pos != null) {
                return wall.getRebondVector(pos, mBall.getDir());
            }
        }
        return null;
    }

    public Bullet playerWasHitByBullet(Gun gun) {
        for (final Bullet bullet : gun.getBulletList()) {
            Vector2D intersection = mBall.intersects(bullet);
            if (intersection != null) {
                return bullet;
            }
        }
        return null;
    }

    public void updatePlayerPosition(float forceX, float forceY) {
        if (mBall != null) {
            mBall.setDir(forceX, forceY);
        }
    }

    public void updateBullets() {
        long currentTimeMs = System.currentTimeMillis();
        //update bullet


        for (Gun gun : mGunList) {
            gun.rotate((int) mBall.getXPos(), (int) mBall.getYPos());
            if (currentTimeMs - gun.getLastTimeFired() > gun.getFireRate()) {
                gun.fire((int) mBall.getXPos(), (int) mBall.getYPos());
                gun.setLastTimeFired(currentTimeMs);
                // Log.d(TAG, "Fired bullet #" + gun.getBulletList().size());
            }
            Iterator<Bullet> bulletIterator = gun.getBulletList().iterator();
            while (bulletIterator.hasNext()) {
                Bullet bullet = bulletIterator.next();
                bullet.forward();
                //bullet.decreaseVelocity();
                for (final Wall wall : mWallList) {
                    Vector2D intersection = bullet.intersects(wall);
                    if (intersection != null) {
                        bulletIterator.remove();
                    }
                }
            }
        }
        double lastx = mBall.getXPos();
        double lasty = mBall.getYPos();
        mBall.forward();
        mBall.decreseFreeze();
        Vector2D newdirection = playerHitWall();
        if (newdirection != null) { // Player hit a wall
            //mBall.setDir(newdirection);
            //mBall.setFreeze(10);
            mBall.setXPos((int) lastx);
            mBall.setYPos((int) lasty);
            mBall.setShowAlternateSprite(true);
            // TODO bounce
            // ball.setDir(-ball.getDirX(), ball.getDirY());
            setChanged();
            notifyObservers(EVENT.COLLISION_WALL);
        } else {
            mBall.setShowAlternateSprite(false);
        }
        for (final Gun gun : mGunList) {
            Bullet bullet = playerWasHitByBullet(gun);
            if (bullet != null) { // Player got hit by a bullet
                // TODO bounce
                mBall.setShowAlternateSprite(true);
                gun.removeBullet(bullet);
                setChanged();
                notifyObservers(EVENT.COLLISION_BULLET);
            } else {
                mBall.setShowAlternateSprite(false);
            }
        }
        if (playerReachedEnd()) { // Epic win
            //mLevelListener.onLevelCompleted();
            setChanged();
            notifyObservers(EVENT.GAME_SUCCESS);
        }
        if (playerFellIntoHole()) { // Game over
            //mLevelListener.onLevelFailed();
            setChanged();
            notifyObservers(EVENT.GAME_OVER);
        }
    }

    /**
     * Retrieve all the sprites!
     * Note: The order matters as the first object is drawn first (followers will be "above")
     *
     * @return An (Array)List containing all sprites
     */
    public List<SpriteObject> getAllSprites() {
        List<SpriteObject> spriteList = new ArrayList<SpriteObject>();

        if (!mHoleList.isEmpty()) {
            spriteList.addAll(mHoleList);
        }
        if (!mGunList.isEmpty()) {
            for (final Gun gun : mGunList) {
                spriteList.addAll(gun.getBulletList());
            }
            spriteList.addAll(mGunList);
        }
        if (!mWallList.isEmpty()) {
            spriteList.addAll(mWallList);
        }
        if (mTarget != null) {
            spriteList.add(mTarget);
        }
        if (mBall != null) {
            spriteList.add(mBall);
        }

        return spriteList;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (final SpriteObject mSpriteObject : getAllSprites()) {
            sb.append(mSpriteObject + "\n");
        }

        return sb.toString();
    }

    public static enum EVENT {GAME_OVER, GAME_SUCCESS, COLLISION_WALL, COLLISION_BULLET}
}
