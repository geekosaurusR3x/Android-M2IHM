package com.skad.android.androidm2ihm.model;


import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.Log;

/**
 * Created by skad on 27/12/13.
 */
public class Gun extends SpriteObject {
    private int mVelocity = 20;

    public Gun(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public Bullet fire(int CibleX, int CibleY) {
        Bullet temp = new Bullet(this.getBoundingRectangle().centerX(),this.getBoundingRectangle().centerY(),32,32);
        int x = CibleX - getX();
        int y = CibleY - getY();
        double r = Math.sqrt((x * x) + (y * y));
        temp.setDirX(x/r);
        temp.setDirY(y/r);
        temp.setVelocity(mVelocity);
        return temp;
}

}
