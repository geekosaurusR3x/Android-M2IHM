package com.skad.android.androidm2ihm.model;

import com.skad.android.androidm2ihm.utils.MathUtils;

/**
 * This is the player class and this extends SpriteObject
 * Created by skad on 19/12/13.
 */
public class Ball extends SpriteObject {
    /**
     * Minimum value value of the angle to make an rotation
     */
    private static final int ROTATION_THRESHOLD = 5;
    /**
     * Number of frame were the player can't move
     */
    private int freeze;

    /**
     * Constructor
     *
     * @param x
     * @param y
     * @param width
     * @param height set the type of p
     */
    public Ball(int x, int y, int width, int height) {
        super(x, y, width, height);
        mVelocity = 2.0;
        this.mType = "p";
        this.freeze = 0;

    }

    /**
     * Set the number of frame for freeze
     *
     * @param freeze
     */
    public void setFreeze(int freeze) {
        this.freeze = freeze;
    }

    /**
     * Decrease the number of freezeing frame
     */
    public void decreaseFreeze() {
        if (freeze > 0) {
            freeze--;
        }
    }

    /**
     * Rotate the actual SpriteObject into the target direction
     * this one only compute the angle needed by the rotation AND apply the roation only if the angle is supperior of the THRESHOLD
     *
     * @param targetX
     * @param targetY
     * @see com.skad.android.androidm2ihm.model.SpriteObject#rotate(float) for the real rotation
     */
    @Override
    public void rotate(int targetX, int targetY) {
        float angle = MathUtils.angleFromTwoPoint(getBoundingRectangle().centerX(), getBoundingRectangle().centerY(), targetX, targetY);
        // Don't rotate too often (prevents flickering)
        if (Math.abs(mAngle - angle) > ROTATION_THRESHOLD) {
            rotate(angle);
        }
    }

    /**
     * Set the direction of the Player with the direction of the accelerometer and rotate it
     *
     * @param targetX
     * @param targetY
     */
    @Override
    public void setDir(double targetX, double targetY) {
        if (mDir != null) {
            if (freeze == 0) {
                mDir.setXandY(targetX, targetY);
                // mDir.normalize();
            }
        } else {
            mDir = new Vector2D(targetX, targetY);
        }

        rotate((int) (getXPos() + (100 * targetX)), (int) (getYPos() + (100 * targetY)));
    }
}
