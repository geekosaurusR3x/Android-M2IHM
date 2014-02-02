package com.skad.android.androidm2ihm.model;


import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

/**
 * Represent the gun witch fire bullet
 * Extend SpriteObject
 * Created by skad on 27/12/13.
 */
public class Gun extends SpriteObject {
    /**
     * Las time were the gun fire
     */
    private long mLastTimeFired = 0;
    /**
     * list of the fired bullet
     */
    private List<Bullet> mBulletList;
    /**
     * sprite for the bullet
     */
    private Bitmap mBulletSprite;
    /**
     * Time between two fire
     */
    private int mFireRate = 1000;

    /**
     * Constructor.
     * Generate the Fired Bullet List
     *
     * @param x
     * @param y
     * @param width
     * @param height
     * @param fireRate
     */
    public Gun(int x, int y, int width, int height, int fireRate) {
        super(x, y, width, height);
        mBulletList = new ArrayList<Bullet>();
        mFireRate = fireRate;
        this.mType = "g";
    }

    public int getFireRate() {
        return mFireRate;
    }

    public void setFireRate(int fireRate) {
        this.mFireRate = fireRate;
    }

    public Bitmap getBulletSprite() {
        return mBulletSprite;
    }

    public void setBulletSprite(Bitmap sprite) {
        mBulletSprite = sprite;
    }

    public List<Bullet> getBulletList() {
        return mBulletList;
    }

    public void setBulletList(List<Bullet> bulletList) {
        this.mBulletList = bulletList;
    }

    public long getLastTimeFired() {
        return mLastTimeFired;
    }

    public void setLastTimeFired(long lastTimeFired) {
        this.mLastTimeFired = lastTimeFired;
    }

    /**
     * Generate a new bullet and adding it into the BulletList
     *
     * @param targetX
     * @param targetY
     */
    public void fire(int targetX, int targetY) {
        Bullet bullet = new Bullet(getBoundingRectangle().centerX(), getBoundingRectangle().centerY(), (int) (32 * mRatioWidth), (int) (32 * mRatioHeight));
        bullet.setDir(targetX, targetY);
        bullet.setSprite(mBulletSprite);
        bullet.setVelocity(50);
        mBulletList.add(bullet);
    }

    /**
     * Remove the bullet from the Bullet List
     *
     * @param bullet
     */
    public void removeBullet(Bullet bullet) {
        mBulletList.remove(bullet);
    }

    /**
     * Take the super.toString and add the firerate.
     *
     * @return
     */
    @Override
    public String toString() {
        return super.toString() + "/" + getFireRate();
    }
}
