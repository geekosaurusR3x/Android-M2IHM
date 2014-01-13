package com.skad.android.androidm2ihm.model;

/**
 * Created by skad on 27/12/13.
 */
public class Wall extends SpriteObject {
    public Wall(int x, int y, int width, int height) {
        super(x, y, width, height);
        this.mType = "w";
    }
}
