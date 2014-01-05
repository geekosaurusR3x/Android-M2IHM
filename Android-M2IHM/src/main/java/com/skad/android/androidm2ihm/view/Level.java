package com.skad.android.androidm2ihm.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.View;
import com.skad.android.androidm2ihm.R;
import com.skad.android.androidm2ihm.model.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static java.lang.System.currentTimeMillis;

/**
 * Created by skad on 19/12/13.
 */
public class Level extends View {

    // Bitmap background;
    private Ball mBalle;
    private long mLastTime = 0;
    private int mNumLevel = 0;

    private ArrayList mListWall = new ArrayList();
    private ArrayList mListHole = new ArrayList();
    private ArrayList mListBullet = new ArrayList();
    private ArrayList mListGun = new ArrayList();

    public Level(Context context, int numlevel) {
        super(context);
        this.mNumLevel = numlevel;
        this.LoadLevel();
    }

    protected void LoadLevel() {
        mBalle = new Ball();
        InputStream filelevelstream = getResources().openRawResource(this.mNumLevel);
        BufferedReader reader = new BufferedReader(new InputStreamReader(filelevelstream));
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                if (!line.substring(0, 1).matches("#")) {
                    String[] temp = line.split("/");
                    switch (temp[0]) {
                        case "p":
                            mBalle.setX(Integer.parseInt(temp[1]));
                            mBalle.setY(Integer.parseInt(temp[2]));
                            mBalle.setWidht(Integer.parseInt(temp[3]));
                            mBalle.setHeight(Integer.parseInt(temp[4]));
                            break;
                        case "h":
                            Hole hole = new Hole();
                            hole.setX(Integer.parseInt(temp[1]));
                            hole.setY(Integer.parseInt(temp[2]));
                            hole.setWidht(Integer.parseInt(temp[3]));
                            hole.setHeight(Integer.parseInt(temp[4]));
                            hole.setSprite(BitmapFactory.decodeResource(getResources(), R.drawable.hole_texture));
                            mListHole.add(hole);
                            break;
                        case "w":
                            Wall wall = new Wall();
                            wall.setX(Integer.parseInt(temp[1]));
                            wall.setY(Integer.parseInt(temp[2]));
                            wall.setWidht(Integer.parseInt(temp[3]));
                            wall.setHeight(Integer.parseInt(temp[4]));
                            wall.setSprite(BitmapFactory.decodeResource(getResources(), R.drawable.wall_grey_texture));
                            mListWall.add(wall);
                            break;
                        case "abl":
                            Wall wall1 = new Wall();
                            wall1.setX(Integer.parseInt(temp[1]));
                            wall1.setY(Integer.parseInt(temp[2]));
                            wall1.setWidht(Integer.parseInt(temp[3]));
                            wall1.setHeight(Integer.parseInt(temp[4]));
                            wall1.setSprite(BitmapFactory.decodeResource(getResources(), R.drawable.arcwall_bottom_left));
                            mListWall.add(wall1);
                            break;
                        case "abr":
                            Wall wall2 = new Wall();
                            wall2.setX(Integer.parseInt(temp[1]));
                            wall2.setY(Integer.parseInt(temp[2]));
                            wall2.setWidht(Integer.parseInt(temp[3]));
                            wall2.setHeight(Integer.parseInt(temp[4]));
                            wall2.setSprite(BitmapFactory.decodeResource(getResources(), R.drawable.arcwall_bottom_right));
                            mListWall.add(wall2);
                            break;
                        case "atl":
                            Wall wall3 = new Wall();
                            wall3.setX(Integer.parseInt(temp[1]));
                            wall3.setY(Integer.parseInt(temp[2]));
                            wall3.setWidht(Integer.parseInt(temp[3]));
                            wall3.setHeight(Integer.parseInt(temp[4]));
                            wall3.setSprite(BitmapFactory.decodeResource(getResources(), R.drawable.arcwall_top_left));
                            mListWall.add(wall3);
                            break;
                        case "atr":
                            Wall wall4 = new Wall();
                            wall4.setX(Integer.parseInt(temp[1]));
                            wall4.setY(Integer.parseInt(temp[2]));
                            wall4.setWidht(Integer.parseInt(temp[3]));
                            wall4.setHeight(Integer.parseInt(temp[4]));
                            wall4.setSprite(BitmapFactory.decodeResource(getResources(), R.drawable.arcwall_top_right));
                            mListWall.add(wall4);
                            break;
                        case "g":
                            Gun gun = new Gun();
                            gun.setX(Integer.parseInt(temp[1]));
                            gun.setY(Integer.parseInt(temp[2]));
                            gun.setWidht(Integer.parseInt(temp[3]));
                            gun.setHeight(Integer.parseInt(temp[4]));
                            gun.setSprite(BitmapFactory.decodeResource(getResources(), R.drawable.cannon));
                            mListGun.add(gun);
                            break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
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
        for (final Object mMur : mListWall) {
            canvas.drawBitmap(((SpriteObject) (mMur)).getSprite(), ((SpriteObject) (mMur)).getX(), ((SpriteObject) (mMur)).getY(), null);
        }
        for (final Object mHole : mListHole) {
            canvas.drawBitmap(((SpriteObject) (mHole)).getSprite(), ((SpriteObject) (mHole)).getX(), ((SpriteObject) (mHole)).getY(), null);
        }
        for (final Object mGun : mListGun) {
            canvas.drawBitmap(((SpriteObject) (mGun)).getSprite(), ((SpriteObject) (mGun)).getX(), ((SpriteObject) (mGun)).getY(), null);
        }
        for (final Object mBullet : mListBullet) {
            canvas.drawBitmap(((SpriteObject) (mBullet)).getSprite(), ((SpriteObject) (mBullet)).getX(), ((SpriteObject) (mBullet)).getY(), null);
        }

        canvas.drawBitmap(mBalle.getSprite(), mBalle.getX(), mBalle.getY(), null);
        invalidate();
    }

    private void update() {
        for (final Object mHole : mListHole) {
            if (((Hole) (mHole)).intoHole(mBalle)) {
                ((Activity) getContext()).finish();
            }
        }
        for (final Object mBullet : mListBullet) {
            ((Bullet) (mBullet)).forward();
        }
        if (currentTimeMillis() - mLastTime > 10000) {
            for (final Object mGun : mListGun) {
                Bullet mBullet = ((Gun) (mGun)).fire();
                mBullet.setSprite(BitmapFactory.decodeResource(getResources(), R.drawable.boulet));
                mListBullet.add(mBullet);
            }
        }
        mLastTime = currentTimeMillis();
    }

    public void setForceX(float forceX) {
        int lastx = mBalle.getX();
        mBalle.ApplyForceX(forceX);
        if (Colision()) {
            mBalle.setX(lastx);
        }
    }

    public void setForceY(float forceY) {
        int lasty = mBalle.getY();
        mBalle.ApplyForceY(forceY);
        if (Colision()) {
            mBalle.setY(lasty);
        }
    }

    protected boolean Colision() {
        for (final Object mMur : mListWall) {
            if (mBalle.intersect((SpriteObject) (mMur))) {
                return true;
            }
        }
        return false;
    }
}
