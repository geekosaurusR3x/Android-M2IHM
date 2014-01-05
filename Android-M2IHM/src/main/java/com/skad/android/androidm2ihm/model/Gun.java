package com.skad.android.androidm2ihm.model;

import android.text.GetChars;

import java.util.Vector;

/**
 * Created by skad on 27/12/13.
 */
public class Gun extends SpriteObject{
    private int mVelocity = 1;
    public Gun()
    {
        super();
    }

    public Bullet fire()
    {

        Bullet temp = new Bullet();
        temp.setX(X);
        temp.setY(Y);
        int x = 10 - getX();
        int y = 10 - getY();
        temp.setmDirX(x/100);
        temp.setmDirY(y/100);
        temp.setmVelocity(mVelocity);
        return temp;
    }

}
