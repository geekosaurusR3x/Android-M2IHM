package com.skad.android.androidm2ihm;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.View;

/**
 * Created by skad on 19/12/13.
 */
public class Level  extends View{

    Bitmap background;

    public Level(Context context) {
        super(context);
        background = BitmapFactory.decodeResource(getResources(),R.drawable.wood_texture);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(background,0,0,null);
        invalidate();
    }
}
