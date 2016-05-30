package com.example.armadillianonimi.emergencyphonenumbers;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.preference.SwitchPreferenceCompat;

public class SettingsTab extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    SharedPreferences prefs;
    SwitchPreferenceCompat autoLocationSwitch;
    Preference.OnPreferenceChangeListener autoLocationListener;

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.preferences);
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        autoLocationSwitch = (SwitchPreferenceCompat) findPreference("pref_auto_location");
        autoLocationListener = new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newVal) {
                System.out.println("AUTO LOCATION CHANGED TO " + newVal.toString());
                final boolean value = (Boolean) newVal;
                autoLocationSwitch.setChecked(value);
                return true;
            }
        };
        autoLocationSwitch.setOnPreferenceChangeListener(autoLocationListener);

        // Register shared preferences to update the switch when it's changed from the dialog
        prefs.registerOnSharedPreferenceChangeListener(this);

        // Listener for click on "Change country" preference
        Preference dialogPref = findPreference("dialog");
        dialogPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                android.app.FragmentManager fm = getActivity().getFragmentManager();
                CountrySelectionDialog countrySelector = new CountrySelectionDialog();
                countrySelector.setChoiceAvailable(!(prefs.getBoolean("pref_auto_location", false)));
                countrySelector.show(fm, "countrySelector");
                return true;
            }
        });

        // Listener for click on "About" preference
        Preference aboutDialog = findPreference("about_dialog");
        aboutDialog.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                android.app.FragmentManager fm = getActivity().getFragmentManager();
                AboutDialog dialog = new AboutDialog();
                dialog.show(fm, "About");
                return true;
            }
        });
        System.out.println("SettingsTab: onCreatePreferences");
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
        if (key.equals("pref_auto_location")) {
            SwitchPreferenceCompat switchPreference = (SwitchPreferenceCompat) findPreference(key);
            switchPreference.setChecked(prefs.getBoolean(key, false));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("SettingsTab: onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        System.out.println("SettingsTab: onPause");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("SettingsTab: onDestroy");
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        System.out.println("SettingsTab: onLowMemory");
    }
}