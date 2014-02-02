package com.skad.android.androidm2ihm.model;

import android.content.Context;
import android.util.Log;

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

    /**
     * This class is a singleton, instead of calling its (private) constructor, use this method
     *
     * @return The one and only instance of this class
     */
    public static Level getInstance() {
        if (mInstance == null) {
            synchronized (Level.class) {
                mInstance = new Level();
            }
        }
        return mInstance;
    }

    /**
     * Set all componenets at once
     *
     * @param ball     The ball
     * @param end      The target
     * @param wallList A list holding all wall items
     * @param holeList A list holding all holes
     * @param gunList  A list holding all guns
     */
    public void setComponents(Ball ball, Target end, List<Wall> wallList, List<Hole> holeList, List<Gun> gunList) {
        mBall = ball;
        mTarget = end;
        mWallList = wallList;
        mHoleList = holeList;
        mGunList = gunList;
    }

    /**
     * Clear all data
     */
    public void reset() {
        mWallList.clear();
        mGunList.clear();
        mHoleList.clear();
        mTarget = null;
        mBall = null;
        setChanged();
        notifyObservers();
    }

    /**
     * Remove an item from the level
     *
     * @param objectToRemove The SpriteObject we want to remove
     */
    public void remove(SpriteObject objectToRemove) {
        List<SpriteObject> spriteObjectList = getAllSprites();
        if (spriteObjectList == null || spriteObjectList.isEmpty() || !spriteObjectList.contains(objectToRemove)) {
            return;
        }
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
        } else if (objectToRemove instanceof WallArc) {
            mWallList.remove(objectToRemove);
        }
        setChanged();
        notifyObservers();
    }

    /**
     * Add an item to the level
     *
     * @param newObject The SpriteObject to add to our level
     */
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
        } else if (newObject instanceof WallArc) {
            mWallList.add((WallArc) newObject);
        }
        setChanged();
        notifyObservers();
    }

    /**
     * Get the index of an item in our SpriteObject list
     *
     * @param object The object whose index we want to retrieve
     * @return The index of the SpriteObject
     */
    public int getId(SpriteObject object) {
        return getAllSprites().indexOf(object);
    }

    /**
     * Retrieve a SpriteObject from its identifier (index in list)
     *
     * @param id Index in the list (confer getAllSprites())
     * @return The desired SpriteObject, or the last one if index > size of the SpriteObject list, the first one if index < 0
     */
    public SpriteObject get(int id) {
        // TODO Try catch block for try ArrayIndexOutOfBoundsException
        List<SpriteObject> spriteObjects = getAllSprites();
        if (id < 0) { // Negative id, return first object
            id = 0;
        } else if (id >= spriteObjects.size()) {
            id = spriteObjects.size() - 1; // Index too big, return last object
        }
        return spriteObjects.get(id);
    }

    /**
     * Retrieve the player's SpriteObject aka the Ball
     *
     * @return Ball
     */
    public Ball getBall() {
        return mBall;
    }

    /**
     * Set the player's SpriteObject aka the Ball
     *
     * @param ball Ball
     */
    public void setBall(Ball ball) {
        mBall = ball;
    }

    /**
     * Retrieve this level's target object
     *
     * @return The target
     */
    public Target getTarget() {
        return mTarget;
    }

    /**
     * Set this level's target object
     *
     * @param end The new target
     */
    public void setTarget(Target end) {
        mTarget = end;
    }

    /**
     * Retrieve the wall list
     *
     * @return A list holding all wall items
     */
    public List<Wall> getWallList() {
        return mWallList;
    }

    /**
     * Set the wall list
     *
     * @param wallList The new list holding all wall items
     */
    public void setWallList(List<Wall> wallList) {
        mWallList = wallList;
    }

    /**
     * Retrieve the hole list
     *
     * @return A list holding all hole items
     */
    public List<Hole> getHoleList() {
        return mHoleList;
    }

    /**
     * Set the hole list
     *
     * @param holeList The new list holding all hole items
     */
    public void setHoleList(List<Hole> holeList) {
        mHoleList = holeList;
    }

    /**
     * Retrieve the path where our level is stored
     *
     * @return The path to our resource file
     */
    public String getPath() {
        return mPath;
    }

    /**
     * Set the path where our level is stored
     * <p/>
     * TODO Remove the context parameter
     *
     * @param context Current context
     * @param mPath   The actual file name
     */
    public void setPath(Context context, String mPath) {
        this.mPath = context.getExternalFilesDir(null) + File.separator + mPath;
    }

    /**
     * Check if this level contains any guns
     *
     * @return True if there is at least one gun, false otherwise
     */
    public boolean containsGuns() {
        return mGunList.isEmpty();
    }

    /**
     * Retrieve the list of guns
     *
     * @return A list holding all guns present in this level
     */
    public List<Gun> getGunList() {
        return mGunList;
    }

    /**
     * Set our gun list
     *
     * @param gunList A list holding all guns
     */
    public void setGunList(List<Gun> gunList) {
        mGunList = gunList;
    }

    /**
     * Retrieve this level's number (As in level 1, level 2 etc.)
     *
     * @return The level number
     * @deprecated
     */
    public int getLevelNumber() {
        return mLevelNumber;
    }

    /**
     * Set this level's number (As in level 1, level 2 etc.)
     *
     * @param levelNumber The new level number
     */
    public void setLevelNumber(int levelNumber) {
        mLevelNumber = levelNumber;
    }

    /**
     * Check whether the player has reached the target
     *
     * @return true if level is completed, false otherwise
     */
    public boolean playerReachedEnd() {
        if (mTarget != null) { // This level may have no end at all
            return mTarget.intoHole(mBall);
        }
        return false;
    }

    /**
     * Check whether the player fell into a hole and the game should be considered as lost
     *
     * @return true if the player is currentyl in collision with a hole object, false otherwise
     */
    public boolean playerFellIntoHole() {
        for (final Hole hole : mHoleList) {
            if (hole.intoHole(mBall)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check whether the player hit a wall
     *
     * @return true if the player is currently in collision with a wall object, false otherwise
     */
    public Vector2D playerHitWall() {
        for (final Wall wall : mWallList) {
            Vector2D pos = mBall.intersects(wall);
            if (pos != null) {
                return wall.getReboundVector(pos, mBall.getDir());
            }
        }
        return null;
    }

    /**
     * Check whether the player was hit by a bullet
     *
     * @return true if the player is currently in collision with a bullet object, false otherwise
     */
    public Bullet playerWasHitByBullet(Gun gun) {
        for (final Bullet bullet : gun.getBulletList()) {
            Vector2D intersection = mBall.intersects(bullet);
            if (intersection != null) {
                return bullet;
            }
        }
        return null;
    }

    /**
     * Update a player position (move)
     *
     * @param forceX Where the player is heading for (X axis)
     * @param forceY Where the player is heading for (Y axis)
     */
    public void updatePlayerPosition(float forceX, float forceY) {
        if (mBall != null) {
            mBall.setDir(forceX, forceY);
        }
    }

    /**
     * Update all objects in this level
     */
    public void update() {
        updatePlayer();
        updateBullets();
        checkForCollisions();
    }

    /**
     * Make a player move and check for collisions
     */
    private void updatePlayer() {
        double lastX = mBall.getXPos();
        double lastY = mBall.getYPos();
        mBall.forward();
        // mBall.decreaseFreeze();
        Vector2D newDirection = playerHitWall();
        if (newDirection != null) { // Player hit a wall
            //mBall.setDir(newDirection);
            //mBall.setFreeze(10);
            // Uncomment the two following line to disable bouncing
            mBall.setXPos((int) lastX);
            mBall.setYPos((int) lastY);
            mBall.setShowAlternateSprite(true);
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
    }

    /**
     * Wrapper function that automagically check for any current collisions
     */
    private void checkForCollisions() {
        if (playerReachedEnd()) { // Epic win
            setChanged();
            notifyObservers(EVENT.GAME_SUCCESS);
        }
        if (playerFellIntoHole()) { // Game over
            setChanged();
            notifyObservers(EVENT.GAME_OVER);
        }
    }

    /**
     * Update the position of all bullets and make them disappear if they hit a wall or the player
     */
    private void updateBullets() {
        long currentTimeMs = System.currentTimeMillis();
        // update bullet positions
        for (Gun gun : mGunList) {
            // Rotate gun so that they aim at the player's ship
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
                bullet.decreaseVelocity();
                for (final Wall wall : mWallList) {
                    Vector2D intersection = bullet.intersects(wall);
                    if (intersection != null) {
                        try {
                            bulletIterator.remove();
                        } catch (IllegalStateException e) {
                            Log.wtf(TAG, "Error when trying to remove bullet: " + e.getCause());
                        }
                    }
                }
                /*Vector2D currentPosition = bullet.getPosition();
                Vector2D nextPosition = bullet.getNextPosition();

                Vector2D movement = currentPosition;
                movement.sub(nextPosition);
                boolean removeBullet = false;
                boolean xCurrentBigger = currentPosition.getX() > nextPosition.getX();
                boolean yCurrentBigger = currentPosition.getY() > nextPosition.getY();
                // Check if there's a bullet in the way
                for (int xMov = (int) currentPosition.getX(); xCurrentBigger ? xMov < nextPosition.getX() : xMov > nextPosition.getX(); xMov = xCurrentBigger ? xMov - 1 : xMov + 1) {
                    for (int yMov = (int) currentPosition.getY(); yCurrentBigger ? yMov < nextPosition.getY() : yMov > nextPosition.getY(); yMov = yCurrentBigger ? yMov - 1 : yMov + 1) {
                        for (final Wall wall : mWallList) {
                            if (wall.isInto(xMov, yMov)) {
                                removeBullet = true;
                            }
                        }
                    }
                }
                if (removeBullet) {
                    bulletIterator.remove();
                } else {
                    bullet.forward();
                }*/


            }
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

    /**
     * Events to be reported to observers
     */
    public static enum EVENT {
        GAME_OVER, GAME_SUCCESS, COLLISION_WALL, COLLISION_BULLET
    }
}
