package com.skad.android.androidm2ihm.model;

/**
 * Created by skad on 19/12/13.
 */
public class Ball extends SpriteObject {
    private int mSpeed = 2;

    public Ball(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public void applyForceX(float X) {
        super.x += mSpeed * X;
    }

    public void applyForceY(float Y) {
        super.y += mSpeed * Y;
    }

    public void removeForceX(float X) {
        super.x -= X;
    }

    public void removeForceY(float Y) {
        super.y -= Y;
    }

    public int getSpeed() {
        return mSpeed;
    }

    public void setSpeed(int speed) {
        this.mSpeed = speed;
    }

}
