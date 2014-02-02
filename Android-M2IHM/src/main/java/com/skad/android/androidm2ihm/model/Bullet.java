package com.skad.android.androidm2ihm.model;

/**
 * Bulet class with represent the bullet fired by the guns.
 * Extends SpriteObject
 * Created by skad on 27/12/13.
 */
public class Bullet extends SpriteObject {

    /**
     * Constructor
     *
     * @param x
     * @param y
     * @param width
     * @param height set the type of b
     */
    public Bullet(int x, int y, int width, int height) {
        super(x, y, width, height);
        this.mType = "b";
    }

    /**
     * decrease te velocity of the object. This is only needed by the bullet
     */
    public void decreaseVelocity() {
        if (mVelocity > 0) {
            mVelocity--;
        }
    }
}
