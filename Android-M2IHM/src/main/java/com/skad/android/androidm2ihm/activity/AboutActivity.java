package com.skad.android.androidm2ihm.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import com.skad.android.androidm2ihm.R;

/**
 * Created by pschmitt on 1/6/14.
 */
public class AboutActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        // Action bar
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
}