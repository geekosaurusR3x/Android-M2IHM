package com.skad.android.androidm2ihm.model;

/**
 * WallArc class
 * Extend Wall class
 * Created by skad on 27/12/13.
 */
public class WallArc extends Wall {
    /**
     * Constructor
     * Set the type of "wa"
     */
    public WallArc(int x, int y, int width, int height) {
        super(x, y, width, height);
        this.mType = "wa";
    }
}
