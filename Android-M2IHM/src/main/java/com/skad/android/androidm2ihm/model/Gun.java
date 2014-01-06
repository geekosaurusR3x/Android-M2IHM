package com.skad.android.androidm2ihm.model;

/**
 * Created by skad on 27/12/13.
 */
public class Gun extends SpriteObject {
    private int mVelocity = 1;

    public Gun(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public Bullet fire() {
        Bullet temp = new Bullet();
        temp.setX(x);
        temp.setY(y);
        int x = 10 - getX();
        int y = 10 - getY();
        temp.setDirX(x / 100);
        temp.setDirY(y / 100);
        temp.setVelocity(mVelocity);
        return temp;
    }

}
