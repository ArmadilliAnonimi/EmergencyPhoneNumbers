package com.example.armadillianonimi.emergencyphonenumbers;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by Aron Fiechter on 2016-05-04.
 */
public class SettingsPreferenceFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
        }
    }