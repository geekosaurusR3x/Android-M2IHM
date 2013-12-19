package com.skad.android.androidm2ihm;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.View;

/**
 * Created by skad on 19/12/13.
 */
public class Level  extends View{

    Bitmap background;
    Ball balle;

    public Level(Context context) {
        super(context);
        background = BitmapFactory.decodeResource(getResources(),R.drawable.wood_texture);
        balle = new Ball();
        balle.setSprite(BitmapFactory.decodeResource(getResources(),R.drawable.balle));
        balle.setX(50);
        balle.setY(50);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(background,0,0,null);
        canvas.drawBitmap(balle.getSprite(),balle.getX(),balle.getY(),null);
        invalidate();
    }


    public void setForceX(float forceX) {
        balle.ApplyForceX(forceX);
    }

    public void setForceY(float forceY) {
        balle.ApplyForceY(forceY);
    }
}
