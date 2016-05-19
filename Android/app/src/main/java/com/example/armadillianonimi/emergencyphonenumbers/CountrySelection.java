package com.example.armadillianonimi.emergencyphonenumbers;


import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.app.Activity;
// Fragment Manager for APIs before 11, otherwise useless
import android.support.v4.app.FragmentActivity;

public class CountrySelection extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction().replace(android.R.id.content,
                new PrefsFragment()).commit();
    }


    public static class PrefsFragment extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.country_selector);
        }
    }

}