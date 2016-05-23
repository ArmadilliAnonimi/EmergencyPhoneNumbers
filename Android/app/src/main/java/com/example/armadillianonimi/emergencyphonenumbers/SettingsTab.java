package com.example.armadillianonimi.emergencyphonenumbers;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.preference.DialogPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.util.AttributeSet;

/**
 * Created by Aron Fiechter on 2016-05-04.
 */
public class SettingsTab extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.preferences);

        // Listener for click on "Change country" preference
        Preference dialogPref = findPreference("dialog");
        dialogPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                android.app.FragmentManager fm = getActivity().getFragmentManager();
                CountrySelectionDialog Country_Selector = new CountrySelectionDialog();
                Country_Selector.show(fm, "Country_Selector");
                return true;
            }
        });

        // Listener for click on "FAQ" preference
        Preference faqDialog = findPreference("faq");
        faqDialog.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                android.app.FragmentManager fm = getActivity().getFragmentManager();
                AboutDialog dialog = new AboutDialog();
                dialog.show(fm, "FAQ");
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
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
}