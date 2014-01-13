package com.skad.android.androidm2ihm.view;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import com.skad.android.androidm2ihm.R;
import com.skad.android.androidm2ihm.model.Ball;
import com.skad.android.androidm2ihm.model.SpriteObject;

import java.util.ArrayList;

/**
 * Created by skad on 19/12/13.
 */
public class EditeurView extends View {

    // Bitmap background;
    private boolean mPaused = false;
    private double mRatioWidth = 1;
    private double mRatioHeight = 1;
    private int mScreenWidth = 1;
    private int mScreenHeight = 1;
    private ArrayList<SpriteObject> mListObject = new ArrayList<SpriteObject>();

    public EditeurView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        mScreenWidth = metrics.widthPixels;
        mScreenHeight = metrics.heightPixels;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mPaused) {
            return;
        }

        for (final SpriteObject mObject : mListObject) {
            canvas.drawBitmap(mObject.getSprite(), mObject.getX(), mObject.getY(), null);
        }
        invalidate();
    }


    public void addElement(int tag,float x, float y)
    {
        switch(tag)
        {
            case R.id.editeur_start:
                Ball mBall = new Ball((int)x,(int)y,128,128);
                mBall.setSprite(BitmapFactory.decodeResource(getResources(), R.drawable.playership));
                mBall.setId(tag);
                mListObject.add(mBall);
                break;
        }

        invalidate();

    }

    public void removeElement(int id) {
        mListObject.remove(mListObject.get(id));
    }

    public int getIdElement(float x, float y)
    {
        for (final SpriteObject mSpriteObject : mListObject)
        {
            if(mSpriteObject.isInto((int)x,(int)y))
            {
                return mListObject.indexOf(mSpriteObject);
            }
        }
        return -1;
    }

    public void moveLeft(int id)
    {
        mListObject.get(id).setX(mListObject.get(id).getX()-1);
    }

    public void moveRight(int id)
    {
        mListObject.get(id).setX(mListObject.get(id).getX() + 1);
    }

    public void moveUp(int id)
    {
        mListObject.get(id).setY(mListObject.get(id).getY() - 1);
    }

    public void moveDown(int id)
    {
        mListObject.get(id).setY(mListObject.get(id).getY() + 1);
    }

    public void widthPlus(int id)
    {
        mListObject.get(id).setWidth(mListObject.get(id).getWidth() + 1);
        mListObject.get(id).reSize();
    }
    public void widthMinus(int id)
    {
        SpriteObject object = mListObject.get(id);
        if (object.getWidth() - 1 > 0) {
            object.setWidth(object.getWidth() - 1);
            object.reSize();
        }
    }
    public void heightPlus(int id)
    {
        mListObject.get(id).setHeight(mListObject.get(id).getHeight()+1);
        mListObject.get(id).reSize();
    }
    public void heightMinus(int id)
    {
        SpriteObject object = mListObject.get(id);
        if (object.getHeight() - 1 > 0) {
            object.setHeight(object.getHeight() - 1);
            object.reSize();
        }
    }
    public void moveElementById(int id,float x, float y)
    {   int X =(int)x-(mListObject.get(id).getWidth()/2);
        int Y =(int)y-(mListObject.get(id).getHeight()/2);
        mListObject.get(id).setX(X);
        mListObject.get(id).setY(Y);
    }

    public void pause() {
        mPaused = true;
    }

    public void resume() {
        mPaused = false;
    }
}
