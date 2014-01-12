package com.skad.android.androidm2ihm.model;


import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by skad on 27/12/13.
 */
public class Gun extends SpriteObject {
    private long mLastTimeFired = 0;
    private List<Bullet> mBulletList;
    private Bitmap mBulletSprite;

    public Gun(int x, int y, int width, int height) {
        super(x, y, width, height);
        mBulletList = new ArrayList<Bullet>();
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

    public void fire(int targetX, int targetY) {
        Bullet bullet = new Bullet(getBoundingRectangle().centerX(), getBoundingRectangle().centerY(), (int) (32 * mRatioWidth), (int) (32 * mRatioHeight));
        bullet.setDir(targetX, targetY);
        bullet.setSprite(mBulletSprite);
        mBulletList.add(bullet);
    }

    public void removeBullet(Bullet bullet) {
        mBulletList.remove(bullet);
    }
}
