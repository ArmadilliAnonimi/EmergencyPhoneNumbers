package com.example.armadillianonimi.emergencyphonenumbers;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class CountrySelector extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //showAlertDialog();
    }

//    private void showAlertDialog() {
//        FragmentManager fm = getSupportFragmentManager();
//        CountrySelectionDialog alertDialog = new CountrySelectionDialog();
//        alertDialog.show(fm, "fragment_alert");
//    }
}
