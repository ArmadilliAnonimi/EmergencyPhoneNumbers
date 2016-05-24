package com.example.armadillianonimi.emergencyphonenumbers;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.view.View;
import android.content.Context;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private TextView country;
    final CharSequence Titles[] = {"EMERGENCY", "LOCATION", "SETTINGS"};
    LocationFinder locationFinder;
    SectionsPagerAdapter mSectionsPagerAdapter;
    Toolbar toolbar;
    TabLayout tabs;
    final EmergencyTab emergencyTab = new EmergencyTab();
    final LocationTab locationTab = new LocationTab();
    final SettingsTab settingsTab = new SettingsTab();
    SharedPreferences.OnSharedPreferenceChangeListener prefsListener;
    private static final int PERMISSION_REQUEST_CODE = 1;

    Country selectedCountry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);

        locationFinder = new LocationFinder(this);
        locationFinder.setLocationManagerListener(new LocationManagerListener() {
            @Override
            public void locationReceived(UserLocation location) {
                updateCountry(location.country);
            }
        });

        // TODO: request location only if the user preference is set to true, otherwise just load the last location set from the preferences.
        if (true) {
            locationFinder.requestLocation();
        }

        // Creating The Toolbar and setting it as the Toolbar for the activity.
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        country = (TextView) findViewById(R.id.country);

        // Creating The SectionsPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Assigning ViewPager View and setting the adapter
        ViewPager pager;
        pager = (ViewPager)findViewById(R.id.pager);
        pager.setAdapter(mSectionsPagerAdapter);

        // Assigning the TabLayout View
        tabs = (TabLayout) findViewById(R.id.tabs);
        // Setting Custom Color for the Scroll bar indicator of the TabLayout View
        tabs.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorAccent));

        // Setting the ViewPager For the TabLayout
        tabs.setupWithViewPager(pager);

        // Tabs Listener
        tabs.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(pager) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                super.onTabSelected(tab);
                selectAppBarColour(tabs.getSelectedTabPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        manageEmergencyAPI();

        setupFlagButton();
        setupLocationButton();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefsListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                if (key.equals("select_country")) {
                    String currentCountryCode = sharedPreferences.getString(key, "DE");
                    HashMap<String, Country> countryHashMap = EmergencyPhoneNumbersAPI.getSharedInstance().getCountryHashMap();
                    selectedCountry = countryHashMap.get(currentCountryCode);
                    final EmergencyTab emergencyTab = (EmergencyTab) mSectionsPagerAdapter.getItem(0);
                    final String fire =  selectedCountry.getFire();
                    final String police =  selectedCountry.getPolice();
                    final String medical =  selectedCountry.getMedical();
                    final String name = selectedCountry.getName();
                    MainActivity.this.runOnUiThread(new Runnable(){
                        @Override
                        public void run() {
                            if ((fire != null) || (police != null) || (medical != null)) {
                                emergencyTab.fireNumber = fire;
                                emergencyTab.policeNumber = police;
                                emergencyTab.medicalNumber = medical;
                                emergencyTab.updateUI();
                                country.setText(name);
                            }
                        }
                    });
                }
            }
        };
        prefs.registerOnSharedPreferenceChangeListener(prefsListener);

        if (!(checkPermission(Manifest.permission.CALL_PHONE))) {
               request(Manifest.permission.CALL_PHONE);
        }
        if (!(checkPermission(Manifest.permission.READ_CONTACTS))) {
            request(Manifest.permission.READ_CONTACTS);
        }
//        HashMap<String,String> elements = emergencyTab.getElements();
//        for(String el : elements.keySet()){
//            LinearLayout l = (LinearLayout) findViewById(R.id.main);
//            CardView c = new CardView(this);
//            RelativeLayout r = new RelativeLayout(this);
//            TextView t = new TextView(this);
//            if (l != null) {
//                l.addView(c);
//                c.addView(r);
//                r.addView(t);
//                t.setText(el);
//                System.out.println("SIIIIIIII");
//            }
//            else { System.out.println("NOOOOOOOOOOOOOOO");
//            }
//        }

        System.out.println("### - MainActivity created");
    }

    public void call(View view) {
        if (checkPermission(Manifest.permission.CALL_PHONE)) {
            int id = view.getId();
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            switch(id){
                case(R.id.fire):
                    System.out.println(selectedCountry.getFire());
                    callIntent.setData(Uri.parse("tel: "+ selectedCountry.getFire()));
                    break;
                case(R.id.police):
                    callIntent.setData(Uri.parse("tel:"+ selectedCountry.getPolice()));
                    break;
                case(R.id.medical):
                    callIntent.setData(Uri.parse("tel:" + selectedCountry.getMedical()));
                    break;
                //emergencyTab.getElements()
//                    default:
//                        callIntent.setData(Uri.parse("tel:" +elements.get(id)));
               }
            startActivity(callIntent);
        } else {
            request(Manifest.permission.CALL_PHONE);
            Toast.makeText(getApplicationContext(), "Please, allow call permission.\nIt's good for you!", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean checkPermission(String permission){
        if (ContextCompat.checkSelfPermission(this, permission)
                != PackageManager.PERMISSION_GRANTED) {
        return false;
        }
        return true;
    }

    public void request(String permission){
        ActivityCompat.requestPermissions(this,
                new String[]{permission},
                PERMISSION_REQUEST_CODE);
    }
    public void selectAppBarColour(int position) {
        switch (position) {
            case 0:
                int emergencyColour = getResources().getColor(R.color.colorPrimary);
                int emergencyColourDark = getResources().getColor(R.color.colorPrimaryDark);
                toolbar.setBackgroundColor(emergencyColour);
                tabs.setBackgroundColor(emergencyColour);
                findViewById(R.id.appbar).setBackgroundColor(emergencyColour);
                Window emergencyWindow = getWindow();
                emergencyWindow.setStatusBarColor(emergencyColourDark);
                break;
            case 1:
                int locationColour = getResources().getColor(R.color.colorPrimaryLocation);
                int locationColourDark = getResources().getColor(R.color.colorPrimaryLocationDark);
                toolbar.setBackgroundColor(locationColour);
                tabs.setBackgroundColor(locationColour);
                findViewById(R.id.appbar).setBackgroundColor(locationColour);
                Window locationWindow = getWindow();
                locationWindow.setStatusBarColor(locationColourDark);
                break;
            case 2:
                int settingsColour = getResources().getColor(R.color.colorPrimarySettings);
                int settingsColourDark = getResources().getColor(R.color.colorPrimarySettingsDark);
                toolbar.setBackgroundColor(settingsColour);
                tabs.setBackgroundColor(settingsColour);
                findViewById(R.id.appbar).setBackgroundColor(settingsColour);
                Window settingsWindow = getWindow();
                settingsWindow.setStatusBarColor(settingsColourDark);
                break;
        }
    }

    public void setupLocationButton() {
        ImageButton locationButton = (ImageButton) findViewById(R.id.location_button);
        final Context context = this;
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationFinder.requestLocation();
            }
        });
    }

    public void updateCountry(String country) {
        this.country.setText(country);
    }

    public void setupFlagButton() {
        LinearLayout flagButton = (LinearLayout) findViewById(R.id.set_country);


        flagButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showDialog();
                //   Intent flags = new Intent(context, CountrySelection.class);
                // startActivity(flags);
            }

        });
    }

    private void showDialog() {
        android.app.FragmentManager fm = getFragmentManager();
        CountrySelectionDialog Country_Selector = new CountrySelectionDialog();
        Country_Selector.show(fm, "Country_Selector");
    }

    // Callback method called when the user allows or denies the access to the location. We reload the map if we have the permission to do so.
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    LocationTab locationTab = (LocationTab) mSectionsPagerAdapter.getItem(1);
                    locationTab.loadMap();
                    System.out.println("got location access");
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void manageEmergencyAPI() {
        EmergencyPhoneNumbersAPI api = EmergencyPhoneNumbersAPI.getSharedInstance();
        api.setEmergencyAPIListener(new EmergencyAPIListener() {
            @Override
            public void countriesAvailable(HashMap<String, Country> countryHashMap) {
                String currentCode = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("select_country", "CH");
                selectedCountry = countryHashMap.get(currentCode);

                final EmergencyTab emergencyTab = (EmergencyTab) mSectionsPagerAdapter.getItem(0);
                final String fire =  selectedCountry.getFire();
                final String police =  selectedCountry.getPolice();
                final String medical =  selectedCountry.getMedical();
                final String name = selectedCountry.getName();
                MainActivity.this.runOnUiThread(new Runnable(){
                    @Override
                    public void run() {
                        if ((fire != null) || (police != null) || (medical != null)) {
                            emergencyTab.fireNumber = fire;
                            emergencyTab.policeNumber = police;
                            emergencyTab.medicalNumber = medical;
                            emergencyTab.updateUI();
                            country.setText(name);
                        }
                    }
                });
            }
        });
        api.requestCountries(getApplicationContext());
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case 0: return emergencyTab;
                case 1: return locationTab;
                case 2: return settingsTab;
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return Titles[0];
                case 1:
                    return Titles[1];
                case 2:
                    return Titles[2];
            }
            return null;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("### - MainActivity started");
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("### - MainActivity resumed");
        System.out.println("APP IS RESUMED");
    }

    @Override
    protected void onPause() {
        super.onPause();
        System.out.println("### - MainActivity paused");
        System.out.println("APP IS PAUSED");
    }

    @Override
    protected void onStop() {
        super.onStop();
        System.out.println("### - MainActivity stopped");
        System.out.println("APP IS STOPPED");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        System.out.println("### - MainActivity restarted");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("### - MainActivity destroyed");
        System.out.println("APP IS QUIT");
    }
}