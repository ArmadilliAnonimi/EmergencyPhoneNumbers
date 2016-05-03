package com.example.armadillianonimi.emergencyphonenumbers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v4.view.ViewPager;

import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.example.armadillianonimi.emergencyphonenumbers.ViewPagerAdapter;
import com.example.armadillianonimi.emergencyphonenumbers.SlidingTabLayout;
import com.example.armadillianonimi.emergencyphonenumbers.ScrollFlags;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageButton;
import android.view.View;
import android.view.View.OnClickListener;
import android.content.Intent;


public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    ViewPager pager;
    ViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    CharSequence Titles[] = {"EMERGENCY", "LOCATION", "SETTINGS"};
    int numberOfTabs = 3;
    ImageButton imgButton;
    OnClickListener clickListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Creating The Toolbar and setting it as the Toolbar for the activity
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        TextView country = (TextView) findViewById(R.id.country);
        country.setText("ciao");
        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), Titles, numberOfTabs);

        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        // Assigning the Sliding Tab Layout View
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
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
        ImageButton button = (ImageButton)findViewById(R.id.flagButton);
        button.setOnClickListener(new OnClickListener() {
        });
        addButtonListener();

    }

    public void addButtonListener() {
        imgButton = (ImageButton) findViewById(R.id.flagButton);
        imgButton.setOnClickListener(clickListener);
        startActivity(ScrollFlags.onCreate());
    }


}

