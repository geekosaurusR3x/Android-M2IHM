package com.skad.android.androidm2ihm.utils;

import android.graphics.Point;
import com.skad.android.androidm2ihm.model.Vector2D;

/**
 * Created by pschmitt on 1/9/14.
 */
public class MathUtils {
    static public final float PI = 3.1415927f;
    static public final float radiansToDegrees = 180f / PI;

    private MathUtils() {
    }

    /**
     * This method returns zero if input is negative
     * or max - 1 if input > max
     *
     * @param number   Input number
     * @param maxValue Maximum value
     * @return Either 0, max - 1 or input
     */
    public static int maxOrZero(int number, int maxValue) {
        if (number < 0) {
            return 0;
        }
        if (number >= maxValue) {
            return maxValue - 1;
        }
        return number;
    }

    public static Vector2D randomVector(int xMax, int yMax) {
        int lower = 0; // retourne 1 point de point entre : [lower , xMax]
        return new Vector2D((int) (Math.random() * (xMax - lower)) + lower, (int) (Math.random() * (yMax - lower)) + lower);
    }

    public static double randomDouble(int xMax) {
        int lower = 0;
        return (Math.random() * (xMax - lower)) + lower;
    }

    public static double randomInt(int xMax) {
        int lower = 0;
        return (int) ((Math.random() * (xMax - lower)) + lower);
    }

    public static Vector2D vectorFromPoint(double X1, double Y1, double X2, double Y2) {
        Vector2D vector = new Vector2D(X2 - X1, Y2 - Y1);
        vector.normalize();
        return vector;
    }

    public static final Point getCenterOfCircle() {
        return null;
    }

    public static final Point closestPointOnLine(float lx1, float ly1, float lx2, float ly2, float x0, float y0) {
        float A1 = ly2 - ly1;
        float B1 = lx1 - lx2;
        double C1 = (ly2 - ly1) * lx1 + (lx1 - lx2) * ly1;
        double C2 = -B1 * x0 + A1 * y0;
        double det = A1 * A1 - -B1 * B1;
        double cx;
        double cy;
        if (det != 0) {
            cx = (float) ((A1 * C1 - B1 * C2) / det);
            cy = (float) ((A1 * C2 - -B1 * C1) / det);
        } else {
            cx = x0;
            cy = y0;
        }
        return new Point((int) cx, (int) cy);
    }

    /**
     * Compute the angle between two point.
     *
     * @param x
     * @param y
     * @param targetX
     * @param targetY
     * @return Return the angle
     */
    public static final float angleFromTwoPoint(int x, int y, int targetX, int targetY) {
        float angle = 0;
        double dx = targetX - x;
        double dy = targetY - y;

        angle = (float) Math.toDegrees(Math.atan2(dy, dx));

        if (angle < 0) {
            angle += 360;
        }

        return angle;
    }

}
