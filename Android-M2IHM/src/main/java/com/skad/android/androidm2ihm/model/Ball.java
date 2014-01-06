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

    public void applyForceX(float X) {
        super.X += X;
    }

    public void applyForceY(float Y) {
        super.Y += Y;
    }

    public void removeForceX(float X) {
        super.X -= X;
    }

    public void removeForceY(float Y) {
        super.Y -= Y;
    }
}
