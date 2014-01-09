package com.skad.android.androidm2ihm.view;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.DisplayMetrics;
import android.view.View;
import com.skad.android.androidm2ihm.R;
import com.skad.android.androidm2ihm.model.SpaceObject;
import com.skad.android.androidm2ihm.utils.Functions;

import java.util.ArrayList;

import static java.lang.System.currentTimeMillis;

/**
 * Created by skad on 09/01/14.
 */
public class BackgroundView extends View{
    private ArrayList<SpaceObject> mListSpaceObject = new ArrayList<SpaceObject>();
    private long mLastTimeSpaceObject;
    private double mRatioWidth = 1;
    private double mRatioHeight = 1;
    private int mScreenWidth = 1;
    private int mScreenHeight = 1;

    public BackgroundView(Context context) {
        super(context);
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        mScreenWidth = metrics.widthPixels;
        mScreenHeight = metrics.heightPixels;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.update();

        for (final SpaceObject spaceObject : mListSpaceObject) {
            canvas.drawBitmap(spaceObject.getSprite(), spaceObject.getX(), spaceObject.getY(), null);

        }

        invalidate();
    }

    protected void update()
    {
        for (final SpaceObject mSpaceObject : mListSpaceObject) {
            mSpaceObject.forward();
        }
        if (currentTimeMillis() - mLastTimeSpaceObject > 10000) {
            Object [] pos = Functions.GenerateAleaXY(mScreenHeight, mScreenWidth);
            Object [] cible = Functions.GenerateAleaXY(mScreenHeight,mScreenWidth);
            double  scalefactor = Functions.GenerateAleaDouble(2);
            SpaceObject mSpaceObect = new SpaceObject((Integer)(pos[0]),(Integer)(pos[1]),(int)(128*mRatioWidth*scalefactor),(int)(128*mRatioHeight*scalefactor));
            mSpaceObect.setSprite(BitmapFactory.decodeResource(getResources(), R.drawable.ship1));
            mSpaceObect.setDir((Integer)(cible[0]),(Integer)(cible[1]));
            mSpaceObect.setVelocity(10);
            mListSpaceObject.add(mSpaceObect);
            mLastTimeSpaceObject = currentTimeMillis();
        }
    }

}
