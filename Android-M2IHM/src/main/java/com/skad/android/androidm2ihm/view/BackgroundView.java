package com.skad.android.androidm2ihm.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import com.skad.android.androidm2ihm.model.SpaceObject;
import com.skad.android.androidm2ihm.thread.SpaceShipThread;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by skad on 09/01/14.
 */
public class BackgroundView extends SurfaceView implements SurfaceHolder.Callback {

    private List<SpaceObject> mSpaceObjectList = new ArrayList<SpaceObject>();
    private long mLastTimeSpaceObject;
    private SpaceShipThread mSpaceShipThread;

    public BackgroundView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        SurfaceHolder holder = getHolder();
        if (holder != null) {
            holder.addCallback(this);
        }
    }

    public void drawSpaceShips(Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        for (final SpaceObject spaceObject : mSpaceObjectList) {
            canvas.drawBitmap(spaceObject.getScaledSprite(), (int) spaceObject.getXPos(), (int) spaceObject.getYPos(), null);
        }
    }

    public List<SpaceObject> getSpaceObjectList() {
        return mSpaceObjectList;
    }

    public void addSpaceShip(SpaceObject spaceObject) {
        mSpaceObjectList.add(spaceObject);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mSpaceShipThread = new SpaceShipThread(holder, getContext(), this);
        mSpaceShipThread.setRunning(true);
        mSpaceShipThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mSpaceShipThread.setRunning(false);
        mSpaceShipThread.interrupt();
    }
}
