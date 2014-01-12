package com.skad.android.androidm2ihm.model;

/**
 * Created by skad on 27/12/13.
 */
public class Bullet extends SpriteObject {
    private double mDirX;
    private double mDirY;
    private int mVelocity = 50;

    public Bullet(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public void setDirX(double mDirX) {
        this.mDirX = mDirX;
    }

    public void setDirY(double mDirY) {
        this.mDirY = mDirY;
    }

    public void setVelocity(int velocity) {
        this.mVelocity = velocity;
    }

    public void decreaseVelocity() {
        if (mVelocity > 0) {
            mVelocity--;
        }
    }
}
