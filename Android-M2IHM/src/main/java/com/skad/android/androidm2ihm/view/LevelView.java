package com.skad.android.androidm2ihm.view;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;
import com.skad.android.androidm2ihm.model.Level;
import com.skad.android.androidm2ihm.model.SpriteObject;

/**
 * Created by skad on 19/12/13.
 */
public class LevelView extends View {

    private static final String TAG = "LevelView";

    // Bitmap background;
    private Level mLevel;

    public LevelView(Context context, Level level) {
        super(context);
        mLevel = level;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (final SpriteObject sprite : mLevel.getAllSprites()) {
            canvas.drawBitmap(sprite.getSprite(), sprite.getX(), sprite.getY(), null);
        }
        invalidate();
    }
}
