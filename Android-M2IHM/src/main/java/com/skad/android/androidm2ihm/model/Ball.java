package com.skad.android.androidm2ihm.model;

import com.skad.android.androidm2ihm.utils.Functions;

import java.util.Vector;

/**
 * Created by skad on 19/12/13.
 */
public class Ball extends SpriteObject {
    public Ball() {
        super();
    }

    public Ball(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void setDir(double CibleX, double CibleY)
    {
        this.mDirX = CibleX;
        this.mDirY = CibleY;
        if(Math.abs(CibleX) > 0.2 && Math.abs(CibleY) > 0.2 )
        {
            this.rotate((int)(getX()+(100*CibleX)),(int)(getY()+(100*CibleY)));
        }
    }

}
