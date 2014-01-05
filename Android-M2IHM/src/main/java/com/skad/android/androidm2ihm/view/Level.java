package com.skad.android.androidm2ihm.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import com.skad.android.androidm2ihm.R;
import com.skad.android.androidm2ihm.model.*;

import java.io.*;
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
    private int mNumLevel = 0;
    private ArrayList mListWall = new ArrayList();
    private ArrayList mListHole = new ArrayList();
    private ArrayList mListBullet = new ArrayList();
    private ArrayList mListGun = new ArrayList();

    public Level(Context context,int numlevel) {
        super(context);
        this.mNumLevel = numlevel;
        this.LoadLevel();
    }

    protected void LoadLevel()
    {
        mBalle = new Ball();
        InputStream filelevelstream = getResources().openRawResource(this.mNumLevel);
        BufferedReader reader = new BufferedReader(new InputStreamReader(filelevelstream));
        String line;
        try {
            while ((line = reader.readLine()) != null)
            {
                if(!line.substring(0, 1).matches("#"))
                {
                    String[] temp  = line.split("/");
                    if(temp[0] == "p")
                    {
                        mBalle.setX(Integer.parseInt(temp[1]));
                        mBalle.setY(Integer.parseInt(temp[2]));
                        mBalle.setWidht(Integer.parseInt(temp[3]));
                        mBalle.setHeight(Integer.parseInt(temp[4]));
                    }

                    if(temp[0] == "h")
                    {
                        Hole mHole = new Hole();
                        mHole.setX(Integer.parseInt(temp[1]));
                        mHole.setY(Integer.parseInt(temp[2]));
                        mHole.setWidht(Integer.parseInt(temp[3]));
                        mHole.setHeight(Integer.parseInt(temp[4]));
                        mHole.setSprite(BitmapFactory.decodeResource(getResources(),R.drawable.hole_texture));
                        mListHole.add(mHole);
                    }
                    if(temp[0] == "w")
                    {
                        Wall mWall = new Wall();
                        mWall.setX(Integer.parseInt(temp[1]));
                        mWall.setY(Integer.parseInt(temp[2]));
                        mWall.setWidht(Integer.parseInt(temp[3]));
                        mWall.setHeight(Integer.parseInt(temp[4]));
                        mWall.setSprite(BitmapFactory.decodeResource(getResources(),R.drawable.wall_grey_texture));
                        mListWall.add(mWall);
                    }
                    if(temp[0] == "c")
                    {
                        Wall mWall = new Wall();
                        mWall.setX(Integer.parseInt(temp[1]));
                        mWall.setY(Integer.parseInt(temp[2]));
                        mWall.setWidht(Integer.parseInt(temp[3]));
                        mWall.setHeight(Integer.parseInt(temp[4]));
                        mWall.setSprite(BitmapFactory.decodeResource(getResources(),R.drawable.arcwall_bottom_left));
                        mListWall.add(mWall);
                    }
                    if(temp[0] == "g")
                    {
                        Gun mGun = new Gun();
                        mGun.setX(Integer.parseInt(temp[1]));
                        mGun.setY(Integer.parseInt(temp[2]));
                        mGun.setWidht(Integer.parseInt(temp[3]));
                        mGun.setHeight(Integer.parseInt(temp[4]));
                        mGun.setSprite(BitmapFactory.decodeResource(getResources(),R.drawable.cannon));
                        mListGun.add(mGun);
                    }
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try{
                filelevelstream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        mBalle.setSprite(BitmapFactory.decodeResource(getResources(), R.drawable.balle));
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
