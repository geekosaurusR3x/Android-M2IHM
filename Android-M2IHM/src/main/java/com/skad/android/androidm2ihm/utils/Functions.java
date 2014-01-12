package com.skad.android.androidm2ihm.utils;

import com.skad.android.androidm2ihm.model.Vector2D;

/**
 * Created by skad on 09/01/14.
 */
public class Functions {
    private Functions() {
    }

    public static Vector2D GenerateAleaXY(int xMax, int yMax) {
        int lower = 0; // ??
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
        return vector.normalize();
    }
}
