package com.skad.android.androidm2ihm.fragment;


import android.os.Bundle;
import android.preference.PreferenceFragment;
import com.skad.android.androidm2ihm.R;

/**
 * Settings Fragment, to be displayed by PreferenceActivity
 * Created by pschmitt on 11/17/13.
 */
public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
