package com.skad.android.androidm2ihm.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import com.skad.android.androidm2ihm.model.Level;
import com.skad.android.androidm2ihm.model.SpriteObject;
import com.skad.android.androidm2ihm.thread.GameThread;

/**
 * Created by skad on 19/12/13.
 */
public class LevelView extends SurfaceView implements SurfaceHolder.Callback/*implements Observer*/ {

    private static final String TAG = "LevelView";
    private Level mLevel;
    private GameThread mGameThread;
    private SurfaceHolder mHolder;

    public LevelView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        mLevel = Level.getInstance();
        SurfaceHolder holder = getHolder();
        if (holder != null) {
            holder.addCallback(this);
        }
        // Make transparent so that we can see our background
        setZOrderOnTop(true);
        holder.setFormat(PixelFormat.TRANSPARENT);
    }

    public void drawGameElements(Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        for (final SpriteObject sprite : Level.getInstance().getAllSprites()) {
            canvas.drawBitmap(sprite.getScaledSprite(), (int) sprite.getXPos(), (int) sprite.getYPos(), null);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mHolder = holder;
        mGameThread = new GameThread(holder, getContext(), this);
        mGameThread.setRunning(true);
        mGameThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // TODO ?
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mGameThread.setRunning(false);
        mGameThread.interrupt();
    }

    public void restart() {
        mGameThread = new GameThread(mHolder, getContext(), this);
        mGameThread.setRunning(true);
        mGameThread.start();
    }
}
