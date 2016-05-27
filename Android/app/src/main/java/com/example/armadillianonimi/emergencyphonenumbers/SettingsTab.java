package com.example.armadillianonimi.emergencyphonenumbers;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ShareCompat;
import android.support.v7.preference.DialogPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.util.AttributeSet;

/**
 * Created by Aron Fiechter on 2016-05-04.
 */
public class SettingsTab extends PreferenceFragmentCompat {

    SharedPreferences prefs;

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.preferences);
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

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

        // Listener for click on "Tutorial" preference
        Preference tutorialDialog = findPreference("tutorial");
        tutorialDialog.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                // TODO: show tutorial that is also TODO
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
    public void onResume() {
        super.onResume();
        addPreferencesFromResource(R.xml.preferences);
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