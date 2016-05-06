package com.example.armadillianonimi.emergencyphonenumbers;

import android.Manifest;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v4.view.ViewPager;

import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import android.view.View;
import android.content.Intent;
import android.widget.ImageButton;
import android.content.Context;

import com.example.armadillianonimi.emergencyphonenumbers.ScrollingFlags;
import com.example.armadillianonimi.emergencyphonenumbers.ViewPagerAdapter;
import com.example.armadillianonimi.emergencyphonenumbers.SlidingTabLayout;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    ViewPager pager;
    ViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    CharSequence Titles[] = {"EMERGENCY", "LOCATION", "SETTINGS"};
    int numberOfTabs = 3;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Creating The Toolbar and setting it as the Toolbar for the activity
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        TextView country = (TextView) findViewById(R.id.country);
        country.setText("Brasil");
        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), Titles, numberOfTabs);

        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager)findViewById(R.id.pager);
        pager.setAdapter(adapter);

        // Assiging the Sliding Tab Layout View
        tabs = (SlidingTabLayout)findViewById(R.id.tabs);
        // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width
        tabs.setDistributeEvenly(true);

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);

        // EmergencyPhoneNumbersAPI api = new EmergencyPhoneNumbersAPI();
        openFlags();

    }
    public void openFlags() {
        ImageButton button = (ImageButton) findViewById(R.id.flagButton);
        final Context context = this;

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent flags = new Intent(context, ScrollingFlags.class);
                startActivity(flags);

            }

        });
    }

    // Callback method called when the user allows or denies the access to the location. We reload the map if we have the permission to do so.
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        System.out.println("GOT CALLBACK");
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                System.out.println("Results: " + grantResults.toString());
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    System.out.println("Perission granted!");
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

}