package com.skad.android.androidm2ihm.model;

/**
 * Created by skad on 27/12/13.
 */
public class Bullet extends SpriteObject{
    protected int mDirX;
    protected int mDirY;
    protected int mVelocity;

    public Bullet()
    {
        super();
    }

    public void setmDirX(int mDirX) {
        this.mDirX = mDirX;
    }

    public void setmDirY(int mDirY) {
        this.mDirY = mDirY;
    }

    public void setmVelocity(int mVelocity) {
        this.mVelocity = mVelocity;
    }

    public void forward()
    {
        X += mVelocity*mDirX;
        Y += mVelocity*mDirY;
    }

    public void decreseVelocity()
    {
        mVelocity--;
    }
}
