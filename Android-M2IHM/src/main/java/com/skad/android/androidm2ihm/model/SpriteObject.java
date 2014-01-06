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
    protected int width;
    protected int height;

    public SpriteObject() {
        this.width = 64;
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

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Bitmap getSprite() {
        return Sprite;
    }

    public void setSprite(Bitmap sprite) {
        Sprite = Bitmap.createScaledBitmap(sprite, width, height, false);
    }

    public Rect getBoundingRectangle() {
        return new Rect(X, Y, X + width, Y + height);
    }

    public boolean intersects(SpriteObject wall) {
        if (Rect.intersects(getBoundingRectangle(), wall.getBoundingRectangle())) {
            Rect collisionBounds = getCollisionBounds(getBoundingRectangle(), wall.getBoundingRectangle());
            for (int i = collisionBounds.left; i < collisionBounds.right; i++) {
                for (int j = collisionBounds.top; j < collisionBounds.bottom; j++) {
                    int bitmap1Pixel = Sprite.getPixel(i - X, j - Y);
                    int bitmap2Pixel = wall.getSprite().getPixel(i - wall.getX(), j - wall.getY());
                    if (isFilled(bitmap1Pixel) && isFilled(bitmap2Pixel)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private Rect getCollisionBounds(Rect rect1, Rect rect2) {
        int left = Math.max(rect1.left, rect2.left);
        int top = Math.max(rect1.top, rect2.top);
        int right = Math.min(rect1.right, rect2.right);
        int bottom = Math.min(rect1.bottom, rect2.bottom);
        return new Rect(left, top, right, bottom);
    }

    private boolean isFilled(int pixel) {
        return pixel != Color.TRANSPARENT;
    }
}
