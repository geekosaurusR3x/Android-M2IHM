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
}
