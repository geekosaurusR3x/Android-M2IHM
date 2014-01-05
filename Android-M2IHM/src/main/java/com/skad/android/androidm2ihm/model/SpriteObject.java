package com.skad.android.androidm2ihm.model;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;

/**
 * Created by skad on 19/12/13.
 */
abstract public class SpriteObject {
    protected Bitmap Sprite;
    protected int X;
    protected int Y;
    protected int widht;
    protected int height;

    public SpriteObject(){
        this.widht = 64;
        this.height = 64;
    }
    public int getX() {
        return X;
    }

    public void setX(int x) {
        X = x;
    }

    public int getY() {
        return Y;
    }

    public void setY(int y) {
        Y = y;
    }

    public void setWidht(int widht) {
        this.widht = widht;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setSprite(Bitmap sprite) {
        Sprite = Bitmap.createScaledBitmap(sprite, widht, height,false);

    }

    public Bitmap getSprite() {
        return Sprite;
    }

    public Rect getBoudingRectangle()
    {
        return new Rect(X,Y,X+widht,Y+height);
    }

    public boolean intersects(SpriteObject wall)
    {
        if(Rect.intersects(getBoudingRectangle(),wall.getBoudingRectangle()))
        {
            Rect collisionBounds = getCollisionBounds(getBoudingRectangle(), wall.getBoudingRectangle());
            for (int i = collisionBounds.left; i < collisionBounds.right; i++)
            {
                for (int j = collisionBounds.top; j < collisionBounds.bottom; j++)
                {
                    int bitmap1Pixel = Sprite.getPixel(i-X, j-Y);
                    int bitmap2Pixel = wall.getSprite().getPixel(i - wall.getX(), j - wall.getY());
                    if (isFilled(bitmap1Pixel) && isFilled(bitmap2Pixel))
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private Rect getCollisionBounds(Rect rect1, Rect rect2) {
        int left = (int) Math.max(rect1.left, rect2.left);
        int top = (int) Math.max(rect1.top, rect2.top);
        int right = (int) Math.min(rect1.right, rect2.right);
        int bottom = (int) Math.min(rect1.bottom, rect2.bottom);
        return new Rect(left, top, right, bottom);
    }

    private boolean isFilled(int pixel) {
        return pixel != Color.TRANSPARENT;
    }
}
