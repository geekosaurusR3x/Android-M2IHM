package com.skad.android.androidm2ihm.model;

import android.graphics.Rect;

/**
 * Created by skad on 27/12/13.
 */
public class Hole extends SpriteObject {
    public Hole(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public boolean intoHole(SpriteObject balle) {
        int tierSize = this.width / 3;
        Rect hole_rect = new Rect(getBoundingRectangle().centerX() - tierSize, getBoundingRectangle().centerY() - tierSize, getBoundingRectangle().centerX() + 2 * (tierSize / 3), getBoundingRectangle().centerY() + 2 * (tierSize / 3));
        return hole_rect.contains(balle.getBoundingRectangle().centerX(), balle.getBoundingRectangle().centerY());
    }
}
