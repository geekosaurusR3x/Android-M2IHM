package com.skad.android.androidm2ihm.view;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.View;
import com.skad.android.androidm2ihm.R;
import com.skad.android.androidm2ihm.model.Ball;
import com.skad.android.androidm2ihm.model.Wall;

/**
 * Created by skad on 19/12/13.
 */
public class Level  extends View {

    // Bitmap background;
    private Ball balle;
    private Wall mur;

    public Level(Context context) {
        super(context);
        balle = new Ball();
        balle.setSprite(BitmapFactory.decodeResource(getResources(), R.drawable.balle));
        balle.setX(50);
        balle.setY(50);

        mur = new Wall();
        mur.setSprite(BitmapFactory.decodeResource(getResources(),R.drawable.wall_grey_texture));
        mur.setX(300);
        mur.setY(300);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(balle.getSprite(),balle.getX(),balle.getY(),null);
        canvas.drawBitmap(mur.getSprite(),mur.getX(),mur.getY(),null);
        invalidate();
    }

    public void setForceX(float forceX) {
        int lastx = balle.getX();
        balle.ApplyForceX(forceX);
        if(Colision()) {
            balle.setX(lastx);
        }
    }

    public void setForceY(float forceY) {
        int lasty = balle.getY();
        balle.ApplyForceY(forceY);
        if(Colision()) {
            balle.setY(lasty);
        }
    }

    protected boolean Colision()
    {
        return balle.intersect(mur);
    }
}
