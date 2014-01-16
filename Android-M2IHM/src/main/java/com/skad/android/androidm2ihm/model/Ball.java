package com.skad.android.androidm2ihm.model;

/**
 * Created by skad on 19/12/13.
 */
public class Ball extends SpriteObject {

    public Ball() {
        super();
        mWidth = 32;
        mHeight = 32;
        mVelocity = 2.0;
    }

    public Ball(int x, int y, int width, int height) {
        super(x, y, width, height);
        mVelocity = 2.0;
        this.mType = "p";

    }

    @Override
    public void setDir(double targetX, double targetY) {
        if (mDir != null) {
            mDir.setXandY(targetX, targetY);
        } else {
            mDir = new Vector2D(targetX, targetY);
        }
        rotate((int) (getXPos() + (100 * targetX)), (int) (getYPos() + (100 * targetY)));
    }
}
