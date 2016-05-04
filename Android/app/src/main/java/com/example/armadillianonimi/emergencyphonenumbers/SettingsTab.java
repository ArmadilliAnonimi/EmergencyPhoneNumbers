package com.example.armadillianonimi.emergencyphonenumbers;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

        getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsPreferenceFragment()).commit();
        return view;
    }
}