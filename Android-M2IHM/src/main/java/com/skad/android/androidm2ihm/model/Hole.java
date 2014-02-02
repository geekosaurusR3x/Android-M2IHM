package com.skad.android.androidm2ihm.model;

import android.graphics.Rect;

/**
 * Hole class
 * Extend SpriteObject
 * Created by skad on 27/12/13.
 */
public class Hole extends SpriteObject {
    /**
     * Constructor
     *
     * @param x
     * @param y
     * @param width
     * @param height set the type of h
     */
    public Hole(int x, int y, int width, int height) {
        super(x, y, width, height);
        this.mType = "h";
    }

    /**
     * Return if the ball is into the hole
     *
     * @param balle
     * @return bool if the param is into the hole
     * @see com.skad.android.androidm2ihm.model.Ball
     */
    public boolean intoHole(SpriteObject balle) {
        int tierSize = this.mWidth / 3;
        Rect holeRect = new Rect(getBoundingRectangle().centerX() - tierSize, getBoundingRectangle().centerY() - tierSize, getBoundingRectangle().centerX() + tierSize, getBoundingRectangle().centerY() + tierSize);
        return holeRect.contains(balle.getBoundingRectangle().centerX(), balle.getBoundingRectangle().centerY());
    }

}
