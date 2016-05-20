package com.example.armadillianonimi.emergencyphonenumbers;

import android.content.pm.PackageManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.content.Intent;
import android.content.Context;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TextView country;
    CharSequence Titles[] = {"EMERGENCY", "LOCATION", "SETTINGS"};
    LocationFinder locationFinder;
    SectionsPagerAdapter mSectionsPagerAdapter;
    EmergencyTab emergencyTab = new EmergencyTab();
    LocationTab locationTab = new LocationTab();
    SettingsTab settingsTab = new SettingsTab();

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
        Toolbar toolbar;
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        country = (TextView) findViewById(R.id.country);

        // Creating The SectionsPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Assigning ViewPager View and setting the adapter
        ViewPager pager;
        pager = (ViewPager)findViewById(R.id.pager);
        pager.setAdapter(mSectionsPagerAdapter);

        // Assiging the Sliding Tab Layout View
        // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width
        TabLayout tabs;
        tabs = (TabLayout) findViewById(R.id.tabs);

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setSelectedTabIndicatorColor(getResources().getColor(R.color.tabsScrollColor));


        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setupWithViewPager(pager);

        manageEmergencyAPI();

        setupFlagButton();
        setupLocationButton();

    }

//    public void selectAppBarColour(int position) {
//        switch (position) {
//            case 0:
//                int emergencyColour = getResources().getColor(R.color.colorPrimary);
//                toolbar.setBackgroundColor(emergencyColour);
//                tabs.setBackgroundColor(emergencyColour);
//                break;
//            case 1:
//                int locationColour = getResources().getColor(R.color.colorPrimaryLocation);
//                toolbar.setBackgroundColor(locationColour);
//                tabs.setBackgroundColor(locationColour);
//                break;
//            case 2:
//                int settingsColour = getResources().getColor(R.color.colorPrimarySettings);
//                toolbar.setBackgroundColor(settingsColour);
//                tabs.setBackgroundColor(settingsColour);
//                break;
//        }
//    }

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
        final Context context = this;

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
            public void countriesAvailable(ArrayList<Country> countries) {
                System.out.println("We just reiceved the countries: " + countries);

                Country selectedCountry = countries.get(24);
                final EmergencyTab emergencyTab = (EmergencyTab) mSectionsPagerAdapter.getItem(0);
                final String fire =  selectedCountry.getFire();
                final String police =  selectedCountry.getPolice();
                final String medical =  selectedCountry.getMedical();
                MainActivity.this.runOnUiThread(new Runnable(){
                    @Override
                    public void run() {
                        if ((fire != null) || (police != null) || (medical != null)) {
                            emergencyTab.setFire(fire);
                            emergencyTab.setPolice(police);
                            emergencyTab.setMedical(medical);
                            System.out.println("Ci siamo");
                        } else {
                            System.out.println("NOOOOOOOOOOOOO");
                        }
                    }
                });
            }
        });
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
}