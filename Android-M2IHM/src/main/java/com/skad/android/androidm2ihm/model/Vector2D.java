package com.skad.android.androidm2ihm.model;

import com.skad.android.androidm2ihm.utils.MathUtils;

/**
 * Created by pschmitt on 1/11/14.
 */
public class Vector2D {
    private double mX;
    private double mY;

    /**
     * Constructor of the Vector3D class
     *
     * @param x
     * @param y
     */
    public Vector2D(double x, double y) {
        mX = x;
        mY = y;
    }

    /**
     *
     * @return Return the length (norme) of the vector
     */
    public double length() {
        return Math.sqrt((mX * mX) + (mY * mY));
    }

    /**
     * Normalize the Vector2D.
     * It modifying the actual instance of the vector
     */
    public void normalize() {
        double normX = 0.0;
        double normY = 0.0;
        double len = length();
        if (Math.abs(len) > 0.0) {
            normX = mX / len;
            normY = mY / len;
        }
        mX = normX;
        mY = normY;
    }

    /**
     * Addition the actual instance of the vector with the param Vector2D
     * @param vector
     */
    public void add(Vector2D vector) {
        mX += vector.getX();
        mY += vector.getY();
    }

    /**
     * Remove the actual instance of the vector with the param Vector2D
     * @param vector
     */
    public void sub(Vector2D vector) {
        mX -= vector.getX();
        mY -= vector.getY();
    }

    /**
     * Multiply the actual instance of the vector with the param Vector2D
     * @param vector
     */
    public void mult(Vector2D vector) {
        mX *= vector.getX();
        mY *= vector.getY();
    }

    /**
     * Divise the actual instance of the vector with the param Vector2D
     * @param vector
     */
    public void div(Vector2D vector) {
        mX /= vector.getX();
        mY /= vector.getY();
    }

    /**
     * Return a new vector2D witch is the scalar of the acual instance of the vector and the param
     * @param vector
     * @return new Vector2D
     */
    public double scalaire(Vector2D vector) {

        return (mX * vector.getX()) + (mY * vector.getY());
    }

    /**
     * Return a new vector witch is the normal vector of the actual instance of vector
     * @return new Vector2D
     */
    public Vector2D getNormalVector() {
        return new Vector2D(mY, -mX);
    }

    /**
     * Return a new vector witch is the invert of the actual instance of the vector
     * @return new Vector2D
     */
    public Vector2D invert() {
        return new Vector2D(-mX, -mY);
    }

    /**
     * Return the angle with the actual instance of the vector and the I vector of the repert
     * @return float
     */
    public float angle() {
        float angle = (float) Math.atan2(mY, mX) * MathUtils.radiansToDegrees;
        if (angle < 0) angle += 360;
        return angle;
    }

    /**
     * Return the X value of the actual instance of the vector
     * @return int
     */
    public double getX() {
        return mX;
    }

    /**
     * Set the X value of the actual instance of the vector
     * @param x
     */
    public void setX(double x) {
        mX = x;
    }

    /**
     * Return the Y value of the actual instance of the vector
     * @return int
     */
    public double getY() {
        return mY;
    }

    /**
     * Set the Y value of the actual instance of the vector
     * @param y
     */
    public void setY(double y) {
        mY = y;
    }

    /**
     * Set the X and Y value of the actual instance of the vector
     * @param x
     * @param y
     */
    public void setXandY(double x, double y) {
        mX = x;
        mY = y;
    }

    /**
     * Return a string of the actual instance of the vector
     * @return String
     */
    public String toString() {
        return "X " + mX + " Y " + mY;
    }
}
