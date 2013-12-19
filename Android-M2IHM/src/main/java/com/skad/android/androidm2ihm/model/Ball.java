package com.skad.android.androidm2ihm.model;

/**
 * Created by skad on 19/12/13.
 */
public class Ball extends SpriteObject {
    public Ball() {
        super();
    }

    public void ApplyForceX(float X)
    {
         super.X+=X;
    }

    public void ApplyForceY(float Y)
    {
        super.Y+=Y;
    }
}
