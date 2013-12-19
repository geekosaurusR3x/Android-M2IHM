package com.skad.android.androidm2ihm.activity;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import com.skad.android.androidm2ihm.fragment.SettingsFragment;

/**
 * Settings screen
 * Created by pschmitt on 11/17/13.
 */
public class SettingsActivity extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }
}
