package com.skad.android.androidm2ihm.model;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import com.skad.android.androidm2ihm.utils.MathUtils;

/**
 * Created by skad on 19/12/13.
 */
abstract public class SpriteObject {
    protected Vector2D mPosition;
    protected Vector2D mDir;
    protected int mWidth;
    protected int mHeight;
    protected double mRatioWidth;
    protected double mRatioHeight;
    protected double mVelocity;
    private Bitmap mScaledSprite;
    private Bitmap mAlternateSprite;
    private Bitmap mOriginalSprite;
    private Bitmap mOriginalAlternateSprite;
    private boolean mShowAlternateSprite = false;

    public SpriteObject() {
        this(0, 0, 64, 64);
    }

    protected SpriteObject(int x, int y, int width, int height) {
        mPosition = new Vector2D(x, y);
        mWidth = width;
        mHeight = height;
    }

    protected SpriteObject(Vector2D pos, int width, int height) {
        mPosition = pos;
        mWidth = width;
        mHeight = height;
    }

    public Vector2D getPosition() {
        return mPosition;
    }

    public void setPosition(Vector2D position) {
        mPosition = position;
    }

    public double getXPos() {
        return mPosition.getX();
    }

    public void setXPos(int xPos) {
        mPosition.setX(xPos);
    }

    public double getYPos() {
        return mPosition.getY();
    }

    public void setYPos(int yPos) {
        mPosition.setY(yPos);
    }

    public void setWidth(int width) {
        mWidth = width;
    }

    public void setHeight(int height) {
        mHeight = height;
    }

    public Bitmap getScaledSprite() {
        if (mAlternateSprite != null) {
            return mShowAlternateSprite ? mAlternateSprite : mScaledSprite;
        }
        return mScaledSprite;
    }

    public void setSprite(Bitmap sprite) {
        mScaledSprite = Bitmap.createScaledBitmap(sprite, mWidth, mHeight, false);
        mOriginalSprite = sprite;
    }

    public void setAlternateSprite(Bitmap sprite) {
        mAlternateSprite = Bitmap.createScaledBitmap(sprite, mWidth, mHeight, false);
        mOriginalAlternateSprite = sprite;
    }

    public void setShowAlternateSprite(boolean alternativeSprite) {
        mShowAlternateSprite = alternativeSprite;
    }

    public void setRatioWidth(double ratioWidth) {
        mRatioWidth = ratioWidth;
    }

    public void setRatioHeight(double ratioHeight) {
        mRatioHeight = ratioHeight;
    }

    public Rect getBoundingRectangle() {
        return new Rect((int) mPosition.getX(), (int) mPosition.getY(), (int) mPosition.getX() + mWidth, (int) mPosition.getY() + mHeight);
    }

    public boolean intersects(SpriteObject object) {
        if (Rect.intersects(getBoundingRectangle(), object.getBoundingRectangle())) {
            Rect collisionBounds = getCollisionBounds(getBoundingRectangle(), object.getBoundingRectangle());
            for (int i = collisionBounds.left; i < collisionBounds.right; i++) {
                for (int j = collisionBounds.top; j < collisionBounds.bottom; j++) {
                    int deltaX1 = i - (int) mPosition.getX(), deltaY1 = j - (int) mPosition.getY();
                    deltaX1 = MathUtils.maxOrZero(deltaX1, mScaledSprite.getWidth());
                    deltaY1 = MathUtils.maxOrZero(deltaY1, mScaledSprite.getHeight());
                    int bitmap1Pixel = mScaledSprite.getPixel(deltaX1, deltaY1);
                    int deltaX2 = i - (int) object.getXPos(), deltaY2 = j - (int) object.getYPos();
                    deltaX2 = MathUtils.maxOrZero(deltaX2, object.getScaledSprite().getWidth());
                    deltaY2 = MathUtils.maxOrZero(deltaY2, object.getScaledSprite().getHeight());
                    int bitmap2Pixel = object.getScaledSprite().getPixel(deltaX2, deltaY2);
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

    public void setDir(Vector2D vector) {
        setDir(vector.getX(), vector.getY());
    }

    public void setDir(double targetX, double targetY) {
        Vector2D temp = MathUtils.vectorFromPoint(getXPos(), getYPos(), targetX, targetY);
        mDir = temp;
        rotate((int) targetX, (int) targetY);
    }

    public double getVelocity() {
        return mVelocity;
    }

    public void setVelocity(int velocity) {
        mVelocity = velocity;
    }

    public void forward() {
        // TODO Better collision handling
        mPosition.setX(mPosition.getX() + mVelocity * mDir.getX());
        mPosition.setY(mPosition.getY() + mVelocity * mDir.getY());
    }

    public void rotate(int targetX, int targetY) {
        if (mOriginalSprite == null) {
            return;
        }
        double dx = targetX - getBoundingRectangle().centerX();
        double dy = targetY - getBoundingRectangle().centerY();
        float angle = (float) Math.toDegrees(Math.atan2(dy, dx));
        if (angle < 0) {
            angle += 360;
        }

        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(mOriginalSprite, mWidth, mHeight, true);
        mScaledSprite = Bitmap.createBitmap(scaledBitmap, 0, 0, mWidth, mHeight, matrix, true);
    }

    public double getDirX() {
        return mDir.getX();
    }

    public double getDirY() {
        return mDir.getY();
    }
}
