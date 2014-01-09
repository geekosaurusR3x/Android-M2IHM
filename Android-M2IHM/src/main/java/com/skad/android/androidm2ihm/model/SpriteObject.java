package com.skad.android.androidm2ihm.model;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.Log;

/**
 * Created by skad on 19/12/13.
 */
abstract public class SpriteObject {
    private Bitmap Sprite;
    private Bitmap OriginalSprite;
    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected double ratioWidth;
    protected double ratioHeight;
    private double mDirX;
    private double mDirY;
    private int mVelocity;

    public SpriteObject() {
        this.width = 64;
        this.height = 64;
    }

    protected SpriteObject(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
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

    public void setRatioWidth(double ratioWidth) {
        this.ratioWidth = ratioWidth;
    }

    public void setRatioHeight(double ratioHeight) {
        this.ratioHeight = ratioHeight;
    }

    public void setSprite(Bitmap sprite) {
        Sprite = Bitmap.createScaledBitmap(sprite, width, height, false);
        OriginalSprite = Sprite;
    }

    public Rect getBoundingRectangle() {
        return new Rect(x, y, x + width, y + height);
    }

    public boolean intersects(SpriteObject wall) {
        if (Rect.intersects(getBoundingRectangle(), wall.getBoundingRectangle())) {
            Rect collisionBounds = getCollisionBounds(getBoundingRectangle(), wall.getBoundingRectangle());
            for (int i = collisionBounds.left; i < collisionBounds.right; i++) {
                for (int j = collisionBounds.top; j < collisionBounds.bottom; j++) {
                    int bitmap1Pixel = Sprite.getPixel(i - x, j - y);
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

    public void rotate(int CibleX, int CibleY)
    {
        double dx =     CibleX - this.getBoundingRectangle().centerX();
        double dy =     CibleY - this.getBoundingRectangle().centerY();
        float angle = (float) Math.toDegrees(Math.atan2( dy,dx));
        if(angle < 0){
            angle += 360;
        }

        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(this.OriginalSprite,this.OriginalSprite.getWidth(),this.OriginalSprite.getHeight(), true);
        this.Sprite = Bitmap.createBitmap(scaledBitmap, 0, 0, width, height, matrix, true);
    }

    public void setDir(double CibleX, double CibleY) {
        double x = CibleX - getX();
        double y = CibleY - getY();
        double r = Math.sqrt((x * x) + (y * y));
        this.mDirX = x/r;
        this.mDirY = y/r;
    }

    public void setVelocity(int velocity) {
        this.mVelocity = velocity;
    }

    public void forward() {
        x += mVelocity * mDirX;
        y += mVelocity * mDirY;
    }

    public void decreseVelocity() {
        if(mVelocity>0) { mVelocity--;}
    }
}
