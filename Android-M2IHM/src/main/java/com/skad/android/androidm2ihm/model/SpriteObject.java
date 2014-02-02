package com.skad.android.androidm2ihm.model;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import com.skad.android.androidm2ihm.utils.MathUtils;

/**
 * Represent a generique object that can be drawing on the screen
 * Created by skad on 19/12/13.
 */
abstract public class SpriteObject {
    /**
     * Position of the SpriteObject into screen repert
     */
    protected Vector2D mPosition;
    /**
     * Direction of the SpriteObject movement
     */
    protected Vector2D mDir;
    /**
     * With of the SpriteObject
     */
    protected int mWidth;
    /**
     * Height of the SpriteObject
     */
    protected int mHeight;
    /**
     * Width Ratio between the original screen size were the lvl was design and the actual screen size
     */
    protected double mRatioWidth;
    /**
     * Height Ratio between the original screen size were the lvl was design and the actual screen size
     */
    protected double mRatioHeight;
    /**
     * Velocity off the player
     */
    protected double mVelocity;
    /**
     * Scaled Bitmap with is the normal picture of the sprite
     */
    protected Bitmap mScaledSprite;
    /**
     * Scaled Bitmap with is the alternate picture of the sprite when the player was hit
     */
    protected Bitmap mAlternateSprite;
    /**
     * Original Bitmap with is the normal picture of the sprite (needed for the rotation)
     */
    protected Bitmap mOriginalSprite;
    /**
     * Original Bitmap with is the alternate picture of the sprite when the player was hit (needed for the rotation)
     */
    protected Bitmap mOriginalAlternateSprite;
    /**
     * Switch for the sprite
     */
    protected boolean mShowAlternateSprite = false;
    /**
     * Actual rotation of the sprite
     */
    protected float mAngle;
    /**
     * Type of the SpriteObject
     */
    protected String mType;
    /**
     * Id of the SpriteObject
     */
    private int mId;

    /**
     * Constructor of the SpriteObject
     *
     * @param x
     * @param y
     * @param width
     * @param height
     */
    protected SpriteObject(int x, int y, int width, int height) {
        mPosition = new Vector2D(x, y);
        mWidth = width;
        mHeight = height;
    }

    /**
     * Constructor of the SpriteObject but take an Vector2D instead of X and Y
     *
     * @param pos
     * @param width
     * @param height
     */
    protected SpriteObject(Vector2D pos, int width, int height) {
        mPosition = pos;
        mWidth = width;
        mHeight = height;
    }

    /**
     * Return the actual position of the object
     *
     * @return Return the actual position of the object
     * @see com.skad.android.androidm2ihm.model.Vector2D
     */
    public Vector2D getPosition() {
        return mPosition;
    }

    /**
     * Set the actual position of the SpriteObject with the param
     *
     * @param position
     * @see com.skad.android.androidm2ihm.model.Vector2D
     */
    public void setPosition(Vector2D position) {
        mPosition = position;
    }

    /**
     * Return the X position of the SpriteObject
     *
     * @return Return the X position of the SpriteObject
     */
    public double getXPos() {
        return mPosition.getX();
    }

    /**
     * Set the X position with the param
     *
     * @param xPos Set the X position with the param
     */
    public void setXPos(int xPos) {
        mPosition.setX(xPos);
    }

    /**
     * Return the Y position of the SpriteObject
     *
     * @return Return the Y position of the SpriteObject
     */
    public double getYPos() {
        return mPosition.getY();
    }

    /**
     * Set the Y position with the param
     *
     * @param yPos Set the Y position with the param
     */
    public void setYPos(int yPos) {
        mPosition.setY(yPos);
    }

    /**
     * Return the Id of the SpriteObject
     *
     * @return Return the Id of the SpriteObject
     */
    public int getId() {
        return mId;
    }

    /**
     * Set the Id of the SpriteObject
     *
     * @param mId Set the Id of the SpriteObject
     */
    public void setId(int mId) {
        this.mId = mId;
    }

    /**
     * Return the ScaledSprite Bitmap or if(mShowAlternateSprite) return the mAlternateSprite
     *
     * @return Return the ScaledSprite Bitmap or if(mShowAlternateSprite) return the mAlternateSprite
     */
    public Bitmap getScaledSprite() {
        if (mAlternateSprite != null) {
            return mShowAlternateSprite ? mAlternateSprite : mScaledSprite;
        }
        return mScaledSprite;
    }

    /**
     * Calculate if a point is into the bounding rectangle of the sprite
     *
     * @param x
     * @param y
     * @return Return true if X and Y is into the bounding rectangle of the sprite else false
     */
    public boolean isInto(int x, int y) {
        return getBoundingRectangle().contains(x, y);
    }

    /**
     * Return the Width of the SpriteObject
     *
     * @return Return the Width of the SpriteObject
     */
    public int getWidth() {
        return mWidth;
    }

    /**
     * Set the Width of the SpriteObject
     *
     * @param width
     */
    public void setWidth(int width) {
        mWidth = width;
    }

    /**
     * Return the Height of the SpriteObject
     *
     * @return Return the Height of the SpriteObject
     */
    public int getHeight() {
        return mHeight;
    }

    /**
     * Set the Height of the SpriteObject
     *
     * @param height
     */
    public void setHeight(int height) {
        mHeight = height;
    }

    public float getAngle() {
        return mAngle;
    }

    public void setAngle(float mAngle) {
        this.mAngle = mAngle;
    }

    public void setSprite(Bitmap sprite) {
        mOriginalSprite = sprite;
        resize();
    }

    /**
     * Set the AlternateSprite with the param
     *
     * @param sprite an alternate Bitmap
     */
    public void setAlternateSprite(Bitmap sprite) {
        mAlternateSprite = Bitmap.createScaledBitmap(sprite, mWidth, mHeight, false);
        mOriginalAlternateSprite = sprite;
    }

    /**
     * Switch the display sprite with the bool
     *
     * @param alternativeSprite
     */
    public void setShowAlternateSprite(boolean alternativeSprite) {
        mShowAlternateSprite = alternativeSprite;
    }

    public void setRatioWidth(double ratioWidth) {
        mRatioWidth = ratioWidth;
    }

    public void setRatioHeight(double ratioHeight) {
        mRatioHeight = ratioHeight;
    }

    /**
     * Resize the OriginalSprites with the with and the height of the SpriteObject
     */
    public void resize() {
        mScaledSprite = Bitmap.createScaledBitmap(mOriginalSprite, getWidth(), getHeight(), false);
        if (mAlternateSprite != null) {
            mAlternateSprite = Bitmap.createScaledBitmap(mOriginalAlternateSprite, getWidth(), getHeight(), false);
        }
    }

    /**
     * Return the bounding rectangle (aka the rectangle where the bitmap is on the screen) of the SpriteObject.
     *
     * @return new bounding rectangle
     */
    public Rect getBoundingRectangle() {
        return new Rect((int) mPosition.getX(), (int) mPosition.getY(), (int) mPosition.getX() + mWidth, (int) mPosition.getY() + mHeight);
    }

    /**
     * Check whether two SpriteObjects are intersecting
     *
     * @param object The other SpriteObject we may be intersecting with
     * @return A Vector2D representing the intersection between the two SpriteObjects, null if there's no intersection
     * @see com.skad.android.androidm2ihm.model.Vector2D
     */
    public Vector2D intersects(SpriteObject object) {
        if (Rect.intersects(getBoundingRectangle(), object.getBoundingRectangle())) {
            Rect collisionBounds = getCollisionBounds(getBoundingRectangle(), object.getBoundingRectangle());
            for (int i = collisionBounds.left; i < collisionBounds.right; i++) {
                for (int j = collisionBounds.top; j < collisionBounds.bottom; j++) {
                    // Deltas
                    int deltaX1 = MathUtils.maxOrZero(i - (int) mPosition.getX(), mScaledSprite.getWidth());
                    int deltaY1 = MathUtils.maxOrZero(j - (int) mPosition.getY(), mScaledSprite.getHeight());
                    int deltaX2 = MathUtils.maxOrZero(i - (int) object.getXPos(), object.getScaledSprite().getWidth());
                    int deltaY2 = MathUtils.maxOrZero(j - (int) object.getYPos(), object.getScaledSprite().getHeight());
                    // Pixel content
                    int bitmap1Pixel = mScaledSprite.getPixel(deltaX1, deltaY1);
                    int bitmap2Pixel = object.getScaledSprite().getPixel(deltaX2, deltaY2);
                    // If both pixels are filled (ie. non-transparent) the 2 bitmaps are intersecting
                    if (isFilled(bitmap1Pixel) && isFilled(bitmap2Pixel)) {
                        return new Vector2D(deltaX1, deltaY1);
                    }
                }
            }
        }
        return null;
    }

    /**
     * Calcultate the collision rectangle between two rect and return it
     *
     * @param rect1
     * @param rect2
     * @return collision rectangle
     */
    private Rect getCollisionBounds(Rect rect1, Rect rect2) {
        int left = Math.max(rect1.left, rect2.left);
        int top = Math.max(rect1.top, rect2.top);
        int right = Math.min(rect1.right, rect2.right);
        int bottom = Math.min(rect1.bottom, rect2.bottom);
        return new Rect(left, top, right, bottom);
    }

    /**
     * Calculate if the param pixel contain a color
     *
     * @param pixel
     * @return True if transparente else false
     */
    private boolean isFilled(int pixel) {
        return pixel != Color.TRANSPARENT;
    }

    /**
     * Set the direction of the movement of the SpriteObject with a point of direction and rotate it
     *
     * @param targetX
     * @param targetY
     */
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

    /**
     * Make the SpriteObject moving from the direction and the velocity and update the X and Y of this
     */
    public void forward() {
        // TODO Better collision handling
        mPosition.setX(mPosition.getX() + mVelocity * mDir.getX());
        mPosition.setY(mPosition.getY() + mVelocity * mDir.getY());
    }

    /**
     * Retrieve the position our object would take if we were to call the forward() method right now
     *
     * @return A Vector2D holding the next position
     * @see com.skad.android.androidm2ihm.model.Vector2D
     */
    public Vector2D getNextPosition() {
        return new Vector2D(mPosition.getX() + mVelocity * mDir.getX(), mPosition.getY() + mVelocity * mDir.getY());
    }

    /**
     * Rotate the actual SpriteObject into the target direction
     * this one only compute the angle needed by the rotation.
     *
     * @param targetX
     * @param targetY
     * @see com.skad.android.androidm2ihm.model.SpriteObject#rotate(float) for the real rotation
     */
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
        rotate(angle);
    }

    /**
     * Make the ScaledSprite and the AlternateSprite rotate
     * Need an angle for the rotation
     *
     * @param angle
     * @see com.skad.android.androidm2ihm.model.SpriteObject#rotate(int, int)  for the compute of this angle
     */
    public void rotate(float angle) {
        Matrix matrix = new Matrix();
        mAngle = angle;
        matrix.postRotate(mAngle);
        resize();
        mScaledSprite = Bitmap.createBitmap(mScaledSprite, 0, 0, mWidth, mHeight, matrix, true);
        if (mAlternateSprite != null) {
            mAlternateSprite = Bitmap.createBitmap(mAlternateSprite, 0, 0, mWidth, mHeight, matrix, true);
        }
    }

    public double getDirX() {
        return mDir.getX();
    }

    public double getDirY() {
        return mDir.getY();
    }

    public Vector2D getDir() {
        return mDir;
    }

    public void setDir(Vector2D vector) {
        setDir(vector.getX(), vector.getY());
    }

    /**
     * Return the String of the SpriteObject like that : type/x/y/width/height/angle
     *
     * @return Return the String of the SpriteObject
     * @see Level#toString() for the utiliti
     */
    @Override
    public String toString() {
        return mType + "/" + (int) getXPos() + "/" + (int) getYPos() + "/" + getWidth() + "/" + getHeight() + "/" + getAngle();
    }
}
