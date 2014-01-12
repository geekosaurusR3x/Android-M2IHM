package com.skad.android.androidm2ihm.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;

/**
 * Created by pschmitt on 1/9/14.
 */
public class Level extends Observable {
    public static final int LEVEL_COUNT = 3;
    private static Level mInstance;
    ;
    // Business objects
    private int mLevelNumber;
    private Ball mBall;
    private Hole mEnd;
    private List<Wall> mWallList = new ArrayList<Wall>();
    private List<Hole> mHoleList = new ArrayList<Hole>();
    private List<Gun> mGunList = new ArrayList<Gun>();

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

    public void reset() {
        mWallList.clear();
        mGunList.clear();
        mHoleList.clear();
        mEnd = null;
        mBall = null;
    }

    public Ball getBall() {
        return mBall;
    }

    public void setBall(Ball ball) {
        mBall = ball;
    }

    public Hole getEnd() {
        return mEnd;
    }

    public void setEnd(Hole end) {
        mEnd = end;
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
        return mEnd.intoHole(mBall);
    }

    public boolean playerFellIntoHole() {
        for (final Hole hole : mHoleList) {
            if (hole.intoHole(mBall)) {
                return true;
            }
        }
        return false;
    }

    public boolean playerHitWall() {
        for (final Wall wall : mWallList) {
            if (mBall.intersects(wall)) {
                // mLevelListener.onCollisionDetected(Level.COLLISION.COLLISION_WALL);
                return true;
            }
        }
        return false;
    }

    public Bullet playerWasHitByBullet(Gun gun) {
        for (final Bullet bullet : gun.getBulletList()) {
            if (mBall.intersects(bullet)) {
                // mLevelListener.onCollisionDetected(Level.COLLISION.COLLISION_BULLET);
                return bullet;
            }
        }
        return null;
    }

    public void updatePlayerPosition(float forceX, float forceY) {
        if (mBall == null) {
            return;
        }
        int lastX = (int) mBall.getXPos();
        int lastY = (int) mBall.getYPos();

        mBall.setDir(forceX, forceY);
        mBall.forward();
        //mBall.setPosition(new Vector2D(mBall.getXPos() + mBall.getVelocity() * forceX, mBall.getYPos() + mBall.getVelocity() * forceY));

        if (playerHitWall()) { // Player hit a wall
            mBall.setXPos(lastX);
            mBall.setYPos(lastY);
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

    public void updateBullets() {
        for (Gun gun : mGunList) {
            long currentTimeMs = System.currentTimeMillis();
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
                    if (bullet.intersects(wall)) {
                        bulletIterator.remove();
                    }
                }
            }
        }
    }

    public void update() {
        /*if (playerHitWall()) { // Player hit a wall
            mBall.setXPos(lastX);
            mBall.setYPos(lastY);
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
        }*/
    }

    /**
     * Retrieve all the sprites!
     * Note: The order matters as the first object is drawn first (followers will be "above")
     *
     * @return An (Array)List containing all sprites
     */
    public List<SpriteObject> getAllSprites() {
        List<SpriteObject> spriteList = new ArrayList<SpriteObject>();

        spriteList.addAll(mHoleList);
        for (final Gun gun : mGunList) {
            spriteList.addAll(gun.getBulletList());
        }
        spriteList.addAll(mGunList);
        spriteList.addAll(mWallList);
        spriteList.add(mEnd);
        spriteList.add(mBall);

        return spriteList;
    }

    public static enum EVENT {GAME_OVER, GAME_SUCCESS, COLLISION_WALL, COLLISION_BULLET}
}
