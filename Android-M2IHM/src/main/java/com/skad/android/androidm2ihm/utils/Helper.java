package com.skad.android.androidm2ihm.utils;

/**
 * Created by pschmitt on 1/9/14.
 */
public class Helper {
    private Helper() {
    }

    /**
     * This method returns zero if input is negative
     * or max - 1 if input > max
     *
     * @param number   Input number
     * @param maxValue Maximum value
     * @return
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
}
