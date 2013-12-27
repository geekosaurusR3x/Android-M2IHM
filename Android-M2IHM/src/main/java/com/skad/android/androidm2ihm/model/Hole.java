package com.skad.android.androidm2ihm.model;

import android.graphics.Rect;

/**
 * Created by skad on 27/12/13.
 */
public class Hole extends SpriteObject {
       public Hole()
       {
           super();
       }

    public boolean intoHole(SpriteObject balle)
    {
        return getBoudingRectangle().contains(balle.getBoudingRectangle());
    }
}
