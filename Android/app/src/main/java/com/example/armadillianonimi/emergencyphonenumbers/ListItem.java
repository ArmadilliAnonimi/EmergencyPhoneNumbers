package com.example.armadillianonimi.emergencyphonenumbers;

import android.widget.ListAdapter;

/**
 * Created by val on 26/05/16.
 */
public class ListItem {
    public final String text;
    public final int icon;
    public boolean isItemSelected;

    public ListItem(String text, Integer icon) {
        this.text = text;
        this.icon = icon;
        this.isItemSelected = false;
    }
    @Override
    public String toString() {
        return text;
    }
}