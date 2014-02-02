package com.skad.android.androidm2ihm.model;

/**
 * Class for the object for the background
 * Extend SpriteObject
 * Created by skad on 09/01/14.
 */
public class SpaceObject extends SpriteObject {
    /**
     * Constructor
     *
     * @param x
     * @param y
     * @param width
     * @param height
     */
    public SpaceObject(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    /**
     * Constructor
     *
     * @param pos
     * @param width
     * @param height
     */
    public SpaceObject(Vector2D pos, int width, int height) {
        super(pos, width, height);
    }
}
