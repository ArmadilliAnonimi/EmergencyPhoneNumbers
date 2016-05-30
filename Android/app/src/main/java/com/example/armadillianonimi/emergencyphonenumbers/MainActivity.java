package com.example.armadillianonimi.emergencyphonenumbers;

import android.Manifest;

import android.content.DialogInterface;
import android.net.Uri;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.view.Window;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.HashMap;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    // Tabs and toolbar
    private Toolbar toolbar;
    SectionsPagerAdapter mSectionsPagerAdapter;
    private TabLayout tabs;
    final EmergencyTab emergencyTab = new EmergencyTab();
    final LocationTab locationTab = new LocationTab();
    final SettingsTab settingsTab = new SettingsTab();

    // Preferences
    private SharedPreferences prefs;
    private SharedPreferences.OnSharedPreferenceChangeListener prefsListener;
    private Boolean COMING_THROUGH_GEOLOCATION = false;
    private LocationFinder locationFinder;
    private static final int PERMISSION_REQUEST_CODE = 1;

    // Countries
    private EmergencyPhoneNumbersAPI api = EmergencyPhoneNumbersAPI.getSharedInstance();
    private TextView country;
    private Country selectedCountry;
    private ImageView flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);

        // Initializes the shared preferences
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        // IntroActivity showed just the first time the app is opened
        boolean previouslyStarted = prefs.getBoolean(getString(R.string.pref_previously_started), false);
        if(!previouslyStarted) {
            SharedPreferences.Editor edit = prefs.edit();
            edit.putBoolean(getString(R.string.pref_previously_started), Boolean.TRUE);
            edit.commit();
            startActivityForResult(new Intent(this, IntroActivity.class), 0);
        }

        locationFinder = new LocationFinder(this);
        locationFinder.setLocationManagerListener(new LocationManagerListener() {
            @Override
            public void locationReceived(UserLocation location) {
                updateCountry(location);
            }
        });

        if (prefs.getBoolean("pref_auto_location", false)) {
            System.out.println("REQUESTING LOCATION");
            locationFinder.requestLocation();
        }

        // Creating The Toolbar and setting it as the Toolbar for the activity.
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        country = (TextView) findViewById(R.id.country);
        flag = (ImageView) findViewById(R.id.flag);

        // Creating The SectionsPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Assigning ViewPager View and setting the adapter
        ViewPager pager;
        pager = (ViewPager)findViewById(R.id.pager);
        pager.setAdapter(mSectionsPagerAdapter);

        // Assigning the TabLayout View
        tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.setTabGravity(TabLayout.GRAVITY_FILL);

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

        // Loads the numbers and country in the emergency tab
        manageEmergencyAPI();

        setupFlagButton();

        // Changes the country when it's changed in the preferences
        prefsListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

                if (key.equals("select_country")) {
                    changeCountry(api.getCountryHashMap());
                }

                if (key.equals("pref_auto_location")) {
                    System.out.println("REQUESTING LOCATION BECAUSE SETTING AUTO PREF CHANGED");
                    locationFinder.requestLocation();
                }
            }
        };
        prefs.registerOnSharedPreferenceChangeListener(prefsListener);

        System.out.println("### - MainActivity created");
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Permission requests
        if (resultCode == RESULT_OK) {
            if (!(checkPermission(Manifest.permission.CALL_PHONE))) {
                requestPermissions();
            }
        }
    }

    public void call(View view) {
        if (checkPermission(Manifest.permission.CALL_PHONE)) {
            int id = view.getId();
            final Intent callIntent = new Intent(Intent.ACTION_CALL);
            String num  = null;
            String name = null;
            switch(id){
                case(R.id.fire):
                    num = selectedCountry.getFire();
                    name = "Fire";
                    callIntent.setData(Uri.parse("tel: "+ num));
                    break;
                case(R.id.police):
                    num = selectedCountry.getPolice();
                    name = "Police";
                    callIntent.setData(Uri.parse("tel:"+ num));
                    break;
                case(R.id.medical):
                    name = "Medical";
                    num = selectedCountry.getMedical();
                    callIntent.setData(Uri.parse("tel:" + num));
                    break;
            }
            if (prefs.getBoolean("pref_call_confirmation", true)) {

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Call " + name + " (" + num +")?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(callIntent);
                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            } else{
                startActivity(callIntent);
            }
        } else {
            requestPermissions();
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

    public void requestPermissions(){
        String[] permissions = new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE);
    }

    public void selectAppBarColour(int position) {
        switch (position) {
            case 0: setColorsForTab(getResources().getColor(R.color.colorPrimary),
                        getResources().getColor(R.color.colorPrimaryDark)); break;
            case 1: setColorsForTab(getResources().getColor(R.color.colorPrimaryLocation),
                        getResources().getColor(R.color.colorPrimaryLocationDark)); break;
            case 2: setColorsForTab(getResources().getColor(R.color.colorPrimarySettings),
                        getResources().getColor(R.color.colorPrimarySettingsDark)); break;
        }
    }
    private void setColorsForTab(int primaryColour, int primaryColourDark) {
        // if version is above API 21 -> can change status bar colour
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(primaryColourDark);
        }
        // change appbar, toolbar and tabs colour
        toolbar.setBackgroundColor(primaryColour);
        tabs.setBackgroundColor(primaryColour);
        findViewById(R.id.appbar).setBackgroundColor(primaryColour);
    }

    public void updateCountry(UserLocation country) {
        COMING_THROUGH_GEOLOCATION = true;
        selectedCountry = api.getCountryHashMap().get(country.countryCode);
        prefs.edit().putString("select_country", country.countryCode).apply();
    }

    public void setupFlagButton() {
        LinearLayout flagButton = (LinearLayout) findViewById(R.id.set_country);
        flagButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
    }

    private void showDialog() {
        android.app.FragmentManager fm = getFragmentManager();
        CountrySelectionDialog countrySelector = new CountrySelectionDialog();
        countrySelector.setChoiceAvailable(!(prefs.getBoolean("pref_auto_location", false)));
        countrySelector.show(fm, "Country_Selector");
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
            case 3: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    EmergencyTab emergency = (EmergencyTab) mSectionsPagerAdapter.getItem(0);
                    emergency.selectContacts(this);
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
            // other 'case' lines to check for other
            // permissions this app might request
    }


    private void manageEmergencyAPI() {
        EmergencyPhoneNumbersAPI api = EmergencyPhoneNumbersAPI.getSharedInstance();
        api.setEmergencyAPIListener(new EmergencyAPIListener() {
            @Override
            public void countriesAvailable(HashMap<String, Country> countryHashMap) {
            changeCountry(countryHashMap);
            }
        });
        api.requestCountries(getApplicationContext());
    }

    public void changeCountry(HashMap<String, Country> countryHashMap) {

        String defaultCountryCode;

        // Check if user comes from location button
        if (COMING_THROUGH_GEOLOCATION) {
            defaultCountryCode = selectedCountry.getCode();
        } else {
            defaultCountryCode = getDefaultCountry().getCode();
        }

        COMING_THROUGH_GEOLOCATION = false;

        final String currentCode = prefs.getString("select_country", defaultCountryCode);
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
                    String flagID = currentCode.toLowerCase();
                    if (flagID.equals("do")) {
                        flagID += "2";
                    }
                    flag.setImageResource((getResources().getIdentifier(flagID, "drawable", "com.example.armadillianonimi.emergencyphonenumbers")));
                }
            }
        });
    }

    public Country getDefaultCountry() {
        String countryCode = Locale.getDefault().getCountry();
        if (countryCode == "") {
            // GEOLOCATION THINGY, for now CH
            return api.getCountryHashMap().get("CH");
        } else {
            return api.getCountryHashMap().get(countryCode);
        }
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
        public String getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.emergency_title);
                case 1:
                    return getString(R.string.location_title);
                case 2:
                    return getString(R.string.settings_title);
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

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState);
    }
}