package com.skad.android.androidm2ihm.thread;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import com.skad.android.androidm2ihm.R;
import com.skad.android.androidm2ihm.model.Level;
import com.skad.android.androidm2ihm.model.SpaceObject;
import com.skad.android.androidm2ihm.model.Vector2D;
import com.skad.android.androidm2ihm.utils.FileUtils;
import com.skad.android.androidm2ihm.utils.LevelParser;
import com.skad.android.androidm2ihm.utils.MathUtils;
import com.skad.android.androidm2ihm.view.BackgroundView;

import static java.lang.System.currentTimeMillis;

/**
 * Created by pschmitt on 1/13/14.
 */
public class SpaceShipThread extends Thread {
    private static final int SPACESHIP_FREQUENCY = 2500;
    private static final double mRatioWidth = 1;
    private static final double mRatioHeight = 1;
    private long mLastTimeSpaceObject;
    private boolean mRunning = true;
    private SurfaceHolder mSurfaceHolder;
    private BackgroundView mBackgroundView;
    private Canvas mCanvas;
    private Context mContext;
    private int mScreenWidth = 1;
    private int mScreenHeight = 1;

    public SpaceShipThread(SurfaceHolder surfaceHolder, Context context, BackgroundView levelView) {
        mSurfaceHolder = surfaceHolder;
        mRunning = false;
        mBackgroundView = levelView;
        mContext = context;
        DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
        mScreenWidth = metrics.widthPixels;
        mScreenHeight = metrics.heightPixels;
    }

    public void setRunning(boolean running) {
        mRunning = running;
    }

    @Override
    public void run() {
        synchronized (this) {
            while (mRunning) {
                mCanvas = mSurfaceHolder.lockCanvas();
                for (final SpaceObject mSpaceObject : mBackgroundView.getSpaceObjectList()) {
                    mSpaceObject.forward();
                }
                if (currentTimeMillis() - mLastTimeSpaceObject > SPACESHIP_FREQUENCY) {
                    Vector2D pos = MathUtils.randomVector(mScreenHeight, mScreenWidth);
                    Vector2D target = MathUtils.randomVector(mScreenHeight, mScreenWidth);
                    double scaleFactor = MathUtils.randomDouble(2);
                    SpaceObject spaceObject = new SpaceObject(pos, (int) (128 * mRatioWidth * scaleFactor), (int) (128 * mRatioHeight * scaleFactor));
                    spaceObject.setSprite(BitmapFactory.decodeFile(FileUtils.getfileordefault(mContext, Level.getInstance().getmPath(), "ship1.png")));
                    spaceObject.setDir(target);
                    spaceObject.setVelocity(10);
                    mBackgroundView.addSpaceShip(spaceObject);
                    mLastTimeSpaceObject = currentTimeMillis();
                }
                if (mCanvas != null) {
                    mBackgroundView.drawSpaceShips(mCanvas);
                    mSurfaceHolder.unlockCanvasAndPost(mCanvas);
                }
            }
        }
    }
}
