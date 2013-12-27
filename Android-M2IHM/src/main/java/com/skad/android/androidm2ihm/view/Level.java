package com.skad.android.androidm2ihm.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.View;
import com.skad.android.androidm2ihm.R;
import com.skad.android.androidm2ihm.model.Ball;
import com.skad.android.androidm2ihm.model.Hole;
import com.skad.android.androidm2ihm.model.SpriteObject;
import com.skad.android.androidm2ihm.model.Wall;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by skad on 19/12/13.
 */
public class Level  extends View {

    // Bitmap background;
    private Ball mBalle;
    private Hole mHole;
    private ArrayList mListWall = new ArrayList();

    public Level(Context context) {
        super(context);
        mBalle = new Ball();
        mBalle.setSprite(BitmapFactory.decodeResource(getResources(), R.drawable.balle));
        mBalle.setX(50);
        mBalle.setY(50);

        Wall mMur = new Wall();
        mMur.setSprite(BitmapFactory.decodeResource(getResources(), R.drawable.arcwall_bottom_left));
        mMur.setX(300);
        mMur.setY(300);
        mListWall.add(mMur);

        mHole = new Hole();
        mHole.setSprite(BitmapFactory.decodeResource(getResources(),R.drawable.hole_texture));
        mHole.setX(600);
        mHole.setY(600);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        update();
        for(final Object mMur: mListWall)
        {
            canvas.drawBitmap(((SpriteObject)(mMur)).getSprite(), ((SpriteObject)(mMur)).getX(), ((SpriteObject)(mMur)).getY(), null);
        }
        canvas.drawBitmap(mHole.getSprite(), mHole.getX(), mHole.getY(), null);
        canvas.drawBitmap(mBalle.getSprite(), mBalle.getX(), mBalle.getY(), null);
        invalidate();
    }

    private void update() {
        if(mHole.intoHole(mBalle)){
            ((Activity)getContext()).finish();
        }
    }

    public void setForceX(float forceX) {
        int lastx = mBalle.getX();
        mBalle.ApplyForceX(forceX);
        if(Colision()) {
            mBalle.setX(lastx);
        }
    }

    public void setForceY(float forceY) {
        int lasty = mBalle.getY();
        mBalle.ApplyForceY(forceY);
        if(Colision()) {
            mBalle.setY(lasty);
        }
    }

    protected boolean Colision()
    {   for(final Object mMur: mListWall)
        {
            if (mBalle.intersect((SpriteObject)(mMur)))
            {
                return true;
            }
        }
        return false;
    }
}
