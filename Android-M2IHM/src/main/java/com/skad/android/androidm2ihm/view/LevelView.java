package com.skad.android.androidm2ihm.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import com.skad.android.androidm2ihm.model.Level;
import com.skad.android.androidm2ihm.model.SpriteObject;

/**
 * Created by skad on 19/12/13.
 */
public class LevelView extends View /*implements Observer*/ {

    private static final String TAG = "LevelView";
    private Level mLevel;
/*
    private OnGameEventListener mLevelListener;
*/

    public LevelView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
       /* try {
            mLevelListener = (OnGameEventListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement OnLevelEventListener");
        }*/
        mLevel = Level.getInstance();
/*
        mLevel.addObserver(this);
*/
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (final SpriteObject sprite : Level.getInstance().getAllSprites()) {
            canvas.drawBitmap(sprite.getScaledSprite(), (int) sprite.getXPos(), (int) sprite.getYPos(), null);
        }
        // Immediately request update
        invalidate();
    }

    /*@Override
    public void update(Observable observable, Object data) {
        if (data instanceof Level.EVENT) {
            switch ((Level.EVENT) data) {
                case GAME_OVER:
                    mLevelListener.onLevelFailed();
                    break;
                case GAME_SUCCESS:
                    mLevelListener.onLevelCompleted();
                    break;
                case COLLISION_BULLET:
                    mLevelListener.onCollisionDetected(Level.EVENT.COLLISION_BULLET);
                case COLLISION_WALL:
                    mLevelListener.onCollisionDetected(Level.EVENT.COLLISION_WALL);
                    break;
            }
        }
    }*/

   /*public interface OnGameEventListener {
        void onCollisionDetected(Level.EVENT eventType);

        void onLevelCompleted();

        void onLevelFailed();
    }*/
}
