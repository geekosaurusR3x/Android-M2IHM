package com.skad.android.androidm2ihm.view;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
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
public class BackgroundView extends View {
    private ArrayList<SpaceObject> mListSpaceObject = new ArrayList<SpaceObject>();
    private long mLastTimeSpaceObject;
    private double mRatioWidth = 1;
    private double mRatioHeight = 1;
    private int mScreenWidth = 1;
    private int mScreenHeight = 1;

    public BackgroundView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        mScreenWidth = metrics.widthPixels;
        mScreenHeight = metrics.heightPixels;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        update();

        for (final SpaceObject spaceObject : mListSpaceObject) {
            canvas.drawBitmap(spaceObject.getScaledSprite(), (int) spaceObject.getXPos(), (int) spaceObject.getYPos(), null);
        }

        invalidate();
    }

    protected void update() {
        for (final SpaceObject mSpaceObject : mListSpaceObject) {
            mSpaceObject.forward();
        }
        if (currentTimeMillis() - mLastTimeSpaceObject > 10000) {
            int[] pos = Functions.GenerateAleaXY(mScreenHeight, mScreenWidth);
            int[] target = Functions.GenerateAleaXY(mScreenHeight, mScreenWidth);
            double scaleFactor = Functions.randomDouble(2);
            SpaceObject spaceObject = new SpaceObject(pos[0], pos[1], (int) (128 * mRatioWidth * scaleFactor), (int) (128 * mRatioHeight * scaleFactor));
            spaceObject.setSprite(BitmapFactory.decodeResource(getResources(), R.drawable.ship1));
            spaceObject.setDir(target[0], target[1]);
            spaceObject.setVelocity(10);
            mListSpaceObject.add(spaceObject);
            mLastTimeSpaceObject = currentTimeMillis();
        }
    }

}
