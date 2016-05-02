package com.example.armadillianonimi.emergencyphonenumbers;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.example.armadillianonimi.emergencyphonenumbers.EmergencyTab;
import com.example.armadillianonimi.emergencyphonenumbers.LocationTab;
import com.example.armadillianonimi.emergencyphonenumbers.SettingsTab;

/**
 * Created by patrickbalestra on 5/2/16.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
    int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created


    // Build a Constructor and assign the passed Values to appropriate values in the class
    public ViewPagerAdapter(FragmentManager fm,CharSequence mTitles[], int mNumbOfTabsumb) {
        super(fm);

        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;

    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {

        if(position == 0) {
            EmergencyTab tab1 = new EmergencyTab();
            return tab1;
        } else if(position == 1) {
            LocationTab tab2 = new LocationTab();
            return tab2;
        } else {
            SettingsTab tab3 = new SettingsTab();
            return tab3;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }

    @Override
    public int getCount() {
        return NumbOfTabs;
    }

}
