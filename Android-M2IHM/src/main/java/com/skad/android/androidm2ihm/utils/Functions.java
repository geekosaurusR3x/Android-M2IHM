package com.skad.android.androidm2ihm.utils;

/**
 * Created by skad on 09/01/14.
 */
public class Functions {
    private Functions() {
    }

    public static int[] GenerateAleaXY(int xMax, int yMax) {
        int lower = 0;
        int[] temp = {(int) (Math.random() * (xMax - lower)) + lower, (int) (Math.random() * (yMax - lower)) + lower};
        return temp;
    }

    public static double randomDouble(int xMax) {
        int lower = 0;
        return (Math.random() * (xMax - lower)) + lower;
    }

    public static double randomInt(int xMax) {
        int lower = 0;
        return (int) ((Math.random() * (xMax - lower)) + lower);
    }

    public static double[] vectorFromPoint(double X1, double Y1, double X2, double Y2) {
        double x = X2 - X1;
        double y = Y2 - Y1;
        return normalVectorFromVector(x, y);
    }

    public static double[] normalVectorFromVector(double X, double Y) {
        double r = Math.sqrt((X * X) + (Y * Y));
        double[] temp = {X / r, Y / r};
        return temp;
    }

}
