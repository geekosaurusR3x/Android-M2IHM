package com.skad.android.androidm2ihm.model;

import android.graphics.Bitmap;

/**
 * Created by skad on 19/12/13.
 */
abstract public class SpriteObject {
    protected Bitmap Sprite;
    protected int X;
    protected int Y;

    public int getX() {
        return X;
    }

    public void setX(int x) {
        X = x;
    }

    public int getY() {
        return Y;
    }

    public void setY(int y) {
        Y = y;
    }

    public void setSprite(Bitmap sprite) {
        Sprite = sprite;
    }

    public Bitmap getSprite() {
        return Sprite;
    }
}
