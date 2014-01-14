package com.skad.android.androidm2ihm.view;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import com.skad.android.androidm2ihm.R;
import com.skad.android.androidm2ihm.model.*;

/**
 * Created by skad on 19/12/13.
 */
public class EditorView extends View {

    // Bitmap background;
    private double mRatioWidth = 1;
    private double mRatioHeight = 1;
    private int mScreenWidth = 1;
    private int mScreenHeight = 1;
    private Level mLevel;

    public EditorView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        mScreenWidth = metrics.widthPixels;
        mScreenHeight = metrics.heightPixels;
        mLevel = Level.getInstance();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (final SpriteObject mObject : mLevel.getAllSprites()) {
            canvas.drawBitmap(mObject.getScaledSprite(), (int) (mObject.getXPos()), (int) (mObject.getYPos()), null);
        }
        invalidate();
    }

    public void addElement(int tag, float x, float y) {
        switch (tag) {
            case R.id.editeur_start:
                Ball ball = new Ball((int) x, (int) y, 92, 92);
                ball.setSprite(BitmapFactory.decodeResource(getResources(), R.drawable.playership));
                ball.setId(tag);
                mLevel.add(ball);
                break;
            case R.id.editeur_end:
                Target target = new Target((int) x, (int) y, 128, 128);
                target.setSprite(BitmapFactory.decodeResource(getResources(), R.drawable.cible));
                target.setId(tag);
                mLevel.add(target);
                break;
            case R.id.editeur_hole:
                Hole hole = new Hole((int) x, (int) y, 92, 92);
                hole.setSprite(BitmapFactory.decodeResource(getResources(), R.drawable.hole));
                hole.setId(tag);
                mLevel.add(hole);
                break;
            case R.id.editeur_wall:
                Wall wall = new Wall((int) x, (int) y, 64, 32);
                wall.setSprite(BitmapFactory.decodeResource(getResources(), R.drawable.wall_grey_texture));
                wall.setId(tag);
                mLevel.add(wall);
                break;
        }

        invalidate();

    }

    public void removeElement(int id) {
        mLevel.remove(mLevel.getAllSprites().get(id));
    }

    public int getElementId(float x, float y) {
        for (final SpriteObject mSpriteObject : mLevel.getAllSprites()) {
            if (mSpriteObject.isInto((int) x, (int) y)) {
                return mLevel.getAllSprites().indexOf(mSpriteObject);
            }
        }
        return -1;
    }

    public void moveLeft(int id) {
        mLevel.getAllSprites().get(id).setXPos((int) (mLevel.getAllSprites().get(id).getXPos() - 1));
    }

    public void moveRight(int id) {
        mLevel.getAllSprites().get(id).setXPos((int) (mLevel.getAllSprites().get(id).getXPos() + 1));
    }

    public void moveUp(int id) {
        mLevel.getAllSprites().get(id).setYPos((int) (mLevel.getAllSprites().get(id).getYPos() - 1));
    }

    public void moveDown(int id) {
        mLevel.getAllSprites().get(id).setYPos((int) (mLevel.getAllSprites().get(id).getYPos() + 1));
    }

    public void widthPlus(int id) {
        mLevel.getAllSprites().get(id).setWidth(mLevel.getAllSprites().get(id).getWidth() + 1);
        mLevel.getAllSprites().get(id).reSize();
    }

    public void widthMinus(int id) {
        SpriteObject object = mLevel.getAllSprites().get(id);
        if (object.getWidth() - 1 > 0) {
            object.setWidth(object.getWidth() - 1);
            object.reSize();
        }
    }

    public void heightPlus(int id) {
        mLevel.getAllSprites().get(id).setHeight(mLevel.getAllSprites().get(id).getHeight() + 1);
        mLevel.getAllSprites().get(id).reSize();
    }

    public void heightMinus(int id) {
        SpriteObject object = mLevel.getAllSprites().get(id);
        if (object.getHeight() - 1 > 0) {
            object.setHeight(object.getHeight() - 1);
            object.reSize();
        }
    }

    public void moveElementById(int id, float x, float y) {
        int X = (int) x - (mLevel.getAllSprites().get(id).getWidth() / 2);
        int Y = (int) y - (mLevel.getAllSprites().get(id).getHeight() / 2);
        mLevel.getAllSprites().get(id).setXPos(X);
        mLevel.getAllSprites().get(id).setYPos(Y);
    }

    @Override
    public String toString() {
        String temp = getContext().getString(R.string.editeur_file_first_line);
        temp += "screen/0/0/" + mScreenWidth + "/" + mScreenHeight + "\n";
        for (final SpriteObject mSpriteObject : mLevel.getAllSprites()) {
            temp += mSpriteObject + "\n";
        }

        return temp;
    }
}