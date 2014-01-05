package com.skad.android.androidm2ihm.view;

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
    private Ball mBall;
    private long mLastTime = 0;
    private int mNumLevel = 0;
    private boolean mPaused = false;

    private ArrayList mListWall = new ArrayList();
    private ArrayList mListHole = new ArrayList();
    private ArrayList mListBullet = new ArrayList();
    private ArrayList mListGun = new ArrayList();

    private onLevelEventListener mParentActivity;

    public Level(Context context, int numlevel) {
        super(context);
        try {
            mParentActivity = (onLevelEventListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement onLevelEventListener");
        }
        this.mNumLevel = numlevel;
        this.loadLevel();
    }



    protected void loadLevel() {
        mBall = new Ball();
        InputStream filelevelstream = getResources().openRawResource(this.mNumLevel);
        BufferedReader reader = new BufferedReader(new InputStreamReader(filelevelstream));
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                if (!line.substring(0, 1).matches("#")) {
                    String[] temp = line.split("/");
                    switch (temp[0]) {
                        case "p":
                            mBall.setX(Integer.parseInt(temp[1]));
                            mBall.setY(Integer.parseInt(temp[2]));
                            mBall.setWidht(Integer.parseInt(temp[3]));
                            mBall.setHeight(Integer.parseInt(temp[4]));
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

        mBall.setSprite(BitmapFactory.decodeResource(getResources(), R.drawable.balle));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mPaused) {
            return;
        }

        update();
        for (final Object wall : mListWall) {
            canvas.drawBitmap(((SpriteObject) (wall)).getSprite(), ((SpriteObject) (wall)).getX(), ((SpriteObject) (wall)).getY(), null);
        }
        for (final Object hole : mListHole) {
            canvas.drawBitmap(((SpriteObject) (hole)).getSprite(), ((SpriteObject) (hole)).getX(), ((SpriteObject) (hole)).getY(), null);
        }
        for (final Object gun : mListGun) {
            canvas.drawBitmap(((SpriteObject) (gun)).getSprite(), ((SpriteObject) (gun)).getX(), ((SpriteObject) (gun)).getY(), null);
        }
        for (final Object bullet : mListBullet) {
            canvas.drawBitmap(((SpriteObject) (bullet)).getSprite(), ((SpriteObject) (bullet)).getX(), ((SpriteObject) (bullet)).getY(), null);
        }

        canvas.drawBitmap(mBall.getSprite(), mBall.getX(), mBall.getY(), null);
        invalidate();
    }

    private void update() {
        for (final Object mHole : mListHole) {
            if (((Hole) (mHole)).intoHole(mBall)) {
                mParentActivity.onLevelCompleted();
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
        // TODO Handle player failures (wall/bullet collision)
    }

    public void setForceX(float forceX) {
        int lastx = mBall.getX();
        mBall.applyForceX(forceX);
        if (collision()) {
            mBall.setX(lastx);
        }
    }

    public void setForceY(float forceY) {
        int lasty = mBall.getY();
        mBall.applyForceY(forceY);
        if (collision()) {
            mBall.setY(lasty);
        }
    }

    protected boolean collision() {
        for (final Object wall : mListWall) {
            if (mBall.intersects((SpriteObject) (wall))) {
                return true;
            }
        }
        return false;
    }

    public void pause() {
        mPaused = true;
    }

    public void resume() {
        mPaused = false;
    }

    public interface onLevelEventListener {
        public void onLevelCompleted();
        public void onLevelFailed();
    }
}
