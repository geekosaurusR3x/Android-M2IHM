package com.skad.android.androidm2ihm.utils;

import android.graphics.Point;
import com.skad.android.androidm2ihm.model.Vector2D;

/**
 * Created by pschmitt on 1/9/14.
 */
public class MathUtils {
    /**
     * Pi Const
     */
    static public final float PI = 3.1415927f;
    /**
     * RadiansToDegrees ration const
     */
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

    /**
     * Genertate a Vector2D from 0 to Max
     *
     * @param xMax
     * @param yMax
     * @return a new vector2D
     */
    public static Vector2D randomVector(int xMax, int yMax) {
        int lower = 0; // retourne 1 point de point entre : [lower , xMax]
        return new Vector2D((int) (Math.random() * (xMax - lower)) + lower, (int) (Math.random() * (yMax - lower)) + lower);
    }

    /**
     * Generate a double into [O - xMax]
     * @param xMax
     * @return double
     */
    public static double randomDouble(int xMax) {
        int lower = 0;
        return (Math.random() * (xMax - lower)) + lower;
    }

    /**
     * Generate a int into [O - xMax]
     * @param xMax
     * @return
     */
    public static double randomInt(int xMax) {
        int lower = 0;
        return (int) ((Math.random() * (xMax - lower)) + lower);
    }

    /**
     * Generate a new Vector2D from X1/Y1 to X2/Y2
     * @param X1
     * @param Y1
     * @param X2
     * @param Y2
     * @return new Vector2D
     */
    public static Vector2D vectorFromPoint(double X1, double Y1, double X2, double Y2) {
        Vector2D vector = new Vector2D(X2 - X1, Y2 - Y1);
        vector.normalize();
        return vector;
    }

    /**
     * Do Noting
     * @return null
     */
    public static final Point getCenterOfCircle() {
        return null;
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
