package com.skad.android.androidm2ihm.model;

/**
 * Created by skad on 27/12/13.
 */
public class Bullet extends SpriteObject {
    protected int mDirX;
    protected int mDirY;
    protected int mVelocity;

    public Bullet() {
        super();
    }

    public void setDirX(int mDirX) {
        this.mDirX = mDirX;
    }

    public void setDirY(int mDirY) {
        this.mDirY = mDirY;
    }

    public void setVelocity(int velocity) {
        this.mVelocity = velocity;
    }

    public void forward() {
        x += mVelocity * mDirX;
        y += mVelocity * mDirY;
    }

    public void decreseVelocity() {
        mVelocity--;
    }
}
