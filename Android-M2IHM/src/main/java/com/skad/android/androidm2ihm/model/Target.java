package com.skad.android.androidm2ihm.model;

/**
 * Represent the end of the level
 * Extend Hole
 * Created by pschmitt on 1/13/14.
 */
public class Target extends Hole {
    /**
     * Constructor
     *
     * @param x
     * @param y
     * @param width
     * @param height Set the type for "e"
     */
    public Target(int x, int y, int width, int height) {
        super(x, y, width, height);
        this.mType = "e";
    }
}
