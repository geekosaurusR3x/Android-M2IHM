package com.skad.android.androidm2ihm.model;

/**
 * Created by skad on 19/12/13.
 */
public class Ball extends SpriteObject {
    public Ball() {
        super();
        this.width = 32;
        this.height = 32;
    }

    public Ball(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public void applyForceX(float X) {
        super.x += X;
    }

    public void applyForceY(float Y) {
        super.y += Y;
    }

    public void removeForceX(float X) {
        super.x -= X;
    }

    public void removeForceY(float Y) {
        super.y -= Y;
    }
}
