package com.skad.android.androidm2ihm.model;

import java.util.Vector;

/**
 * Created by skad on 27/12/13.
 */
public class Gun extends SpriteObject{
    private int mVelocity = 10;
    public Gun()
    {
        super();
    }

    public Bullet fire()
    {

        Bullet temp = new Bullet();
        temp.setX(X);
        temp.setY(Y);
        temp.setmDirX(1);
        temp.setmDirY(1);
        temp.setmVelocity(mVelocity);
        return temp;
    }

}
