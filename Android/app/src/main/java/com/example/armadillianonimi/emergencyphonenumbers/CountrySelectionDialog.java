package com.example.armadillianonimi.emergencyphonenumbers;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;


/**
 * Created by val on 19/05/16.
 * This class does exactly the same as a list preference in a xml, but it is needed to open up
 * directly the dialogue.
 */
public class CountrySelectionDialog extends DialogFragment {
    private CharSequence[] mEntries;
    private CharSequence[] mEntryValues;
    private String mValue;
    private boolean mValueSet;
    SharedPreferences prefs;
    private int mClickedDialogEntryIndex;
    public int currentElement;
    public String currentValue;
    private EmergencyPhoneNumbersAPI api = EmergencyPhoneNumbersAPI.getSharedInstance();

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mEntries = new CharSequence[api.getCountries().size()];
        mEntryValues = new CharSequence[api.getCountries().size()];
        mValue = prefs.getString("select_country", "CH");
        createCountryArray();
    }

    @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());

        dialog.setTitle("Select your Country:");
        // Add the buttons
        dialog.setNeutralButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
        //       Doesn't do anything except closing the dialogue.
            }
        });
        dialog.setPositiveButton("DONE", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                mValue = mEntryValues[mClickedDialogEntryIndex].toString();
                prefs.edit().putString("select_country", mValue).commit();
            }
        });
        mClickedDialogEntryIndex = getValueIndex();
        dialog.setSingleChoiceItems(mEntries, mClickedDialogEntryIndex, selectItemListener);
        return dialog.create();
    }

    private int getValueIndex() {
        return findIndexOfValue(mValue);
    }

    public int findIndexOfValue(String value) {
        if (value != null && mEntryValues != null) {
            for (int i = mEntryValues.length - 1; i >= 0; i--) {
                if (mEntryValues[i].equals(value)) {
                    return i;
                }
            }
        }
        return 0;
    }

    DialogInterface.OnClickListener selectItemListener = new DialogInterface.OnClickListener() {



        @Override public void onClick(DialogInterface dialog, int which) {
            currentElement = which;
            currentValue = prefs.getString("select_country", mValue);
            if (mClickedDialogEntryIndex != which) {
                mClickedDialogEntryIndex = which;
            }
        }
    };

    public void createCountryArray() {
        int i = 0;
        for (Country country : api.getCountries()) {
            mEntries[i] = country.getName();
            mEntryValues[i] = country.getCode();
            i++;
        }
    }
}



