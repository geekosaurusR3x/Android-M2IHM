package com.skad.android.androidm2ihm.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.skad.android.androidm2ihm.R;

/**
 * Created by pschmitt on 1/6/14.
 */
public class Score {

    private int mCollisions;
    private int mLevelId;

    public Score(int levelId) {
        mCollisions = 0;
        mLevelId = levelId;
    }

    public void collided() {
        mCollisions++;
    }

    public int getTotalScore() {
        int score = (100 * mLevelId) - mCollisions;
        return score > 0 ? score : 0;
    }

    public static int getHighScore(Context context) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPref.getInt(context.getString(R.string.pref_key_highscore), -1);
    }

    public static void saveHighScore(Context context, int score) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPref.edit().putInt(context.getString(R.string.pref_key_highscore), score).commit();
    }
}
