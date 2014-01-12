package com.skad.android.androidm2ihm.model;

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
