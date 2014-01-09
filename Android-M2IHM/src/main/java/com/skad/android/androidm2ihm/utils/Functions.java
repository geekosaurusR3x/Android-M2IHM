package com.skad.android.androidm2ihm.utils;

/**
 * Created by skad on 09/01/14.
 */
public class Functions {
    private Functions() {
    }

    public static Object[] GenerateAleaXY(int Xmax, int Ymax)
    {
        int lower = 0;

        Object[] temp = {(int)(Math.random() * (Xmax-lower)) + lower,(int)(Math.random() * (Ymax-lower)) + lower};

        return temp;
    }

    public static double GenerateAleaDouble(int Xmax)
    {
        int lower = 0;

        return (Math.random() * (Xmax-lower)) + lower;
    }

    public static double GenerateAleaInt(int Xmax)
    {
        int lower = 0;

        return (int)((Math.random() * (Xmax-lower)) + lower);
    }
    public static Object[] GetVectorFromPoint(double X1, double Y1, double X2, double Y2)
    {
        double x = X2 - X1;
        double y = Y2 - Y1;

        return GetVectorNormeFromVector(x,y);
    }
    public static Object[] GetVectorNormeFromVector(double X,double Y)
    {
        double r = Math.sqrt((X * X) + (Y * Y));
        Object[] temp = {X/r,Y/r};
        return  temp;
    }

}
