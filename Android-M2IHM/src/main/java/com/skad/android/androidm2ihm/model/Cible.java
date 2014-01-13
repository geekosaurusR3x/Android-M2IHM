package com.skad.android.androidm2ihm.model;

import android.graphics.Rect;

/**
 * Created by skad on 27/12/13.
 */
public class Cible extends Hole {
    public Cible(int x, int y, int width, int height) {
        super(x, y, width, height);
        this.mType = "e";
    }
}
