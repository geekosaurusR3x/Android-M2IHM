package com.skad.android.androidm2ihm.model;


/**
 * Created by skad on 27/12/13.
 */
public class Gun extends SpriteObject {
    private int mVelocity = 50;

    public Gun(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public Bullet fire(int CibleX, int CibleY) {
        Bullet temp = new Bullet(this.getBoundingRectangle().centerX(),this.getBoundingRectangle().centerY(),(int)(32*this.ratioWidth),(int)(32*this.ratioHeight));
        temp.setDir(CibleX,CibleY);
        temp.setVelocity(mVelocity);
        return temp;
}

}
