package com.example.armadillianonimi.emergencyphonenumbers;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Aron Fiechter on 2016-05-04.
 */
public class SettingsTab extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings_tab, container, false);
        System.out.println("SettingsTab has been opened.");

        getFragmentManager().beginTransaction().replace(R.id.settings_container_tab, new SettingsPreferenceFragment()).commit();
        System.out.println("Replaced view. Returning...");
        return view;
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