package com.example.armadillianonimi.emergencyphonenumbers;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.google.android.gms.nearby.connection.AppIdentifier;


/**
 * Created by val on 19/05/16.
 * This class opens up an dialogue and edits the selected country in the preferences if the
 * Done button is pressed.
 */
public class CountrySelectionDialog extends DialogFragment {
    private boolean choiceAvailable;
    private CharSequence[] mEntries;
    private CharSequence[] mEntryValues;
    private String mValue;
    SharedPreferences prefs;
    private int elementInSettings;
    private EmergencyPhoneNumbersAPI api = EmergencyPhoneNumbersAPI.getSharedInstance();
    private ListItem[] items;
    ArrayAdapter adapter;


    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mEntries = new CharSequence[api.getCountries().size()];
        mEntryValues = new CharSequence[mEntries.length];
        items = new ListItem[mEntries.length];
        mValue = prefs.getString("select_country", "CH");
        createCountryArray();
    }

    @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        if (choiceAvailable) {
            elementInSettings = getValueIndex();
            items[elementInSettings].isItemSelected = true;

            // Add the buttons
            dialog.setNeutralButton(R.string.CANCEL, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    //       Doesn't do anything except closing the dialogue.
                }
            });
            dialog.setPositiveButton(R.string.DONE, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    prefs.edit().putString("select_country", mValue).apply();
                }
            });
            dialog.setSingleChoiceItems(mEntries, elementInSettings, selectItemListener);
            adapter = new ArrayAdapter<ListItem>(
                    getActivity(),
                    android.R.layout.select_dialog_item,
                    android.R.id.text1,
                    items){
                public View getView(int position, View convertView, ViewGroup parent) {
                    //Use super class to create the View
                    View v = super.getView(position, convertView, parent);
                    ListItem currItem = items[position];

                    TextView tv = (TextView)v.findViewById(android.R.id.text1);
                    tv.setTextColor(getResources().getColor(R.color.colorEmergencyCardsText));

                    // Resize the flag pictures.
                    Drawable d = getResources().getDrawable(items[position].icon);
                    Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
                    int dp50 = (int) (50 * getResources().getDisplayMetrics().density + 0.5f);
                    int dp37 = (int) (37.5 * getResources().getDisplayMetrics().density + 0.5f);
                    Drawable d_resized = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, dp50, dp37, true));
                    Drawable dti = getResources().getDrawable(R.drawable.selected);
                    Bitmap bitmap2 = ((BitmapDrawable) dti).getBitmap();
                    Drawable dChecked = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap2, dp37, dp37, true));
                    dChecked.setTint(getResources().getColor(R.color.colorEmergencyCardsText));


                    if (currItem.isItemSelected) {
                        //Put the image on the TextView
                        tv.setCompoundDrawablesWithIntrinsicBounds(d_resized, null, dChecked, null);

                    } else {
                        //Put the image on the TextView
                        tv.setCompoundDrawablesWithIntrinsicBounds(d_resized, null, null, null);

                    }

                    //Add margin between image and text (support various screen densities)
                    int dp15 = (int) (15 * getResources().getDisplayMetrics().density + 0.5f);
                    tv.setCompoundDrawablePadding(dp15);

                    return v;
                }
            };
            dialog.setAdapter(adapter, selectItemListener);

//            Test how to include images,
//            dialog.setIcon(getResources().getIdentifier(mEntryValues[0].toString().toLowerCase(), "drawable", "com.example.armadillianonimi.emergencyphonenumbers"));

       //     System.out.println("CHECK SAVED COUNTRY CODE: "+ PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("select_country", "CH"));
        } else {
            dialog.setTitle(R.string.country_selection_dialog_title_no);
            // add buttons
            dialog.setPositiveButton(R.string.DISABLE, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    prefs.edit().putBoolean("pref_auto_location", false).apply();

                    System.out.println("DISABLED AUTO LOCATION FROM DIALOG");
                }
            });
            dialog.setNeutralButton(R.string.CLOSE, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //       Doesn't do anything except closing the dialogue.
                }
            });
            dialog.setMessage(R.string.country_selection_dialog_message_no);
            System.out.println("DISPLAYED NEGATION TO CHANGE COUNTRY MWAHAHAHAH - You suck.");
        }
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
            if (elementInSettings != which) {
                items[elementInSettings].isItemSelected = false;
                elementInSettings = which;
                mValue = mEntryValues[elementInSettings].toString();
            }
            items[elementInSettings].isItemSelected = true;
            adapter.notifyDataSetChanged();
        }
    };

    public void createCountryArray() {
        int i = 0;
        for (Country country : api.getCountries()) {
            mEntries[i] = country.getName();
            mEntryValues[i] = country.getCode();
            if (!(mEntryValues[i].equals("DO"))) {
                items[i] = new ListItem(country.getName(), getResources().getIdentifier(mEntryValues[i].toString().toLowerCase(), "drawable", "com.example.armadillianonimi.emergencyphonenumbers"));
            }
            else {
                items[i] = new ListItem(country.getName(), getResources().getIdentifier(mEntryValues[i].toString().toLowerCase() + "2", "drawable", "com.example.armadillianonimi.emergencyphonenumbers"));
            }
            i++;
        }
    }

    public void setChoiceAvailable(boolean canIChooseCountryPls) {
        this.choiceAvailable = canIChooseCountryPls;
    }
}


