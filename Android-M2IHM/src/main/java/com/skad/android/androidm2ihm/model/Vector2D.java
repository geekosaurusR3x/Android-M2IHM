package com.skad.android.androidm2ihm.model;

import com.skad.android.androidm2ihm.utils.MathUtils;

/**
 * Created by pschmitt on 1/11/14.
 */
public class Vector2D {
    private double mX;
    private double mY;

    public Vector2D(double x, double y) {
        mX = x;
        mY = y;
    }

    public double length() {
        return Math.sqrt((mX * mX) + (mY * mY));
    }

    public Vector2D normalize() {
        double normX = 0.0;
        double normY = 0.0;
        double len = length();
        if (Math.abs(len) > 0.0) {
            normX = mX / len;
            normY = mY / len;
        }
        return new Vector2D(normX, normY);
    }

    public void add(Vector2D vector) {
        mX += vector.getX();
        mY += vector.getY();
    }

    public void sub(Vector2D vector) {
        mX -= vector.getX();
        mY -= vector.getY();
    }

    public void mult(Vector2D vector) {
        mX *= vector.getX();
        mY *= vector.getY();
    }

    public void div(Vector2D vector) {
        mX /= vector.getX();
        mY /= vector.getY();
    }

    public double scalaire(Vector2D vector) {

        return (mX * vector.getX()) + (mY * vector.getY());
    }

    public Vector2D getNormalVector() {
        return new Vector2D(-mY, mX);
    }

    public Vector2D invert() {
        return new Vector2D(-mX, -mY);
    }

    public float angle() {
        float angle = (float) Math.atan2(mY, mX) * MathUtils.radiansToDegrees;
        if (angle < 0) angle += 360;
        return angle;
    }

    public double getX() {
        return mX;
    }

    public void setX(double x) {
        mX = x;
    }

    public double getY() {
        return mY;
    }

    public void setY(double y) {
        mY = y;
    }

    public void setXandY(double x, double y) {
        mX = x;
        mY = y;
    }
}
