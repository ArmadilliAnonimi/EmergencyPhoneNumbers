package com.example.armadillianonimi.emergencyphonenumbers;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.os.Bundle;
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

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mEntries = getResources().getStringArray(R.array.countrynames);
        mEntryValues = getResources().getStringArray(R.array.countrycodes);
        mValue = prefs.getString("select_country", "en");
    }

    @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());


        dialog.setTitle("Select your Country:");
        // Add the buttons
        dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                mClickedDialogEntryIndex = currentElement;
                prefs.edit().putString("select_country", currentValue).commit();
            }
        });
        dialog.setPositiveButton("DONE", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked Done button and therefore it just closes and I don't have to do
                // anything here.
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
        return -1;
    }

    DialogInterface.OnClickListener selectItemListener = new DialogInterface.OnClickListener() {



        @Override public void onClick(DialogInterface dialog, int which) {
            currentElement = which;
            currentValue = prefs.getString("select_country", mValue);
            if (mClickedDialogEntryIndex != which) {
                mClickedDialogEntryIndex = which;
                mValue = mEntryValues[mClickedDialogEntryIndex].toString();
                prefs.edit().putString("select_country", mValue).commit();
            }
        }
    };


}
