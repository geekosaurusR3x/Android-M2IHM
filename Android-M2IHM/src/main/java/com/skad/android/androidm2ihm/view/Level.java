package com.skad.android.androidm2ihm.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;
import android.view.View;
import com.skad.android.androidm2ihm.R;
import com.skad.android.androidm2ihm.model.*;

import java.util.ArrayList;
import java.util.List;

import static java.lang.System.currentTimeMillis;

/**
 * Created by skad on 19/12/13.
 */
public class Level  extends View {

    // Bitmap background;
    private Ball mBalle;
    private long mLastTime = 0;
    private ArrayList mListWall = new ArrayList();
    private ArrayList mListHole = new ArrayList();
    private ArrayList mListBullet = new ArrayList();
    private ArrayList mListGun = new ArrayList();

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

        //Hole mHole = new Hole();
        //mHole.setSprite(BitmapFactory.decodeResource(getResources(),R.drawable.hole_texture));
        //mHole.setX(600);
        //mHole.setY(600);
        //mListHole.add(mHole);

        Gun mGun = new Gun();
        mGun.setSprite(BitmapFactory.decodeResource(getResources(),R.drawable.cannon));
        mGun.setX(500);
        mGun.setY(600);
        mListGun.add(mGun);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        update();
        for(final Object mMur: mListWall)
        {
            canvas.drawBitmap(((SpriteObject)(mMur)).getSprite(), ((SpriteObject)(mMur)).getX(), ((SpriteObject)(mMur)).getY(), null);
        }
        for(final Object mHole: mListHole)
        {
            canvas.drawBitmap(((SpriteObject)(mHole)).getSprite(), ((SpriteObject)(mHole)).getX(), ((SpriteObject)(mHole)).getY(), null);
        }
        for(final Object mGun: mListGun)
        {
            canvas.drawBitmap(((SpriteObject)(mGun)).getSprite(), ((SpriteObject)(mGun)).getX(), ((SpriteObject)(mGun)).getY(), null);
        }
        for(final Object mBullet: mListBullet)
        {
            canvas.drawBitmap(((SpriteObject)(mBullet)).getSprite(), ((SpriteObject)(mBullet)).getX(), ((SpriteObject)(mBullet)).getY(), null);
        }

        canvas.drawBitmap(mBalle.getSprite(), mBalle.getX(), mBalle.getY(), null);
        invalidate();
    }

    private void update() {
        for(final Object mHole: mListHole)
        {
            if(((Hole)(mHole)).intoHole(mBalle)){
                ((Activity)getContext()).finish();
            }
        }
        for(final Object mBullet: mListBullet)
        {
            ((Bullet)(mBullet)).forward();
        }
        if(currentTimeMillis()-mLastTime>10000)
        {
            for(final Object mGun: mListGun)
            {
                Bullet mBullet = ((Gun)(mGun)).fire();
                mBullet.setSprite(BitmapFactory.decodeResource(getResources(), R.drawable.boulet));
                mListBullet.add(mBullet);
            }
        }
        mLastTime = currentTimeMillis();
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
