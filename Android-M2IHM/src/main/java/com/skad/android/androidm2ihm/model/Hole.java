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
    {   int tiersize = this.widht/3;
        Rect hole_rect = new Rect(getBoudingRectangle().centerX()-tiersize,getBoudingRectangle().centerY()-tiersize,getBoudingRectangle().centerX()+2*(tiersize/3),getBoudingRectangle().centerY()+2*(tiersize/3));

        return hole_rect.contains(balle.getBoudingRectangle().centerX(),balle.getBoudingRectangle().centerY());
    }
}
