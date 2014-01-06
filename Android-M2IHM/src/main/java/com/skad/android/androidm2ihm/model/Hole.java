package com.skad.android.androidm2ihm.model;

import android.graphics.Rect;

/**
 * Created by skad on 27/12/13.
 */
public class Hole extends SpriteObject {
    public Hole() {
        super();
    }

    public boolean intoHole(SpriteObject balle) {
        int tiersize = this.width / 3;
        Rect hole_rect = new Rect(getBoundingRectangle().centerX() - tiersize, getBoundingRectangle().centerY() - tiersize, getBoundingRectangle().centerX() + 2 * (tiersize / 3), getBoundingRectangle().centerY() + 2 * (tiersize / 3));
        return hole_rect.contains(balle.getBoundingRectangle().centerX(), balle.getBoundingRectangle().centerY());
    }
}
