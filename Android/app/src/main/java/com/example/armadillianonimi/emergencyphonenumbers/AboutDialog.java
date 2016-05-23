package com.example.armadillianonimi.emergencyphonenumbers;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.preference.PreferenceManager;

/**
 * Created by patrickbalestra on 5/23/16.
 */
public class AboutDialog extends DialogFragment {

    private CharSequence[] names;
    private CharSequence[] websites;

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        names = new CharSequence[5];
        websites = new CharSequence[5];

        // Our names
        names[0] = "Patrick Balestra";
        names[1] = "Valerie Burgener";
        names[2] = "Emanuele Esposito";
        names[3] = "Aron Fiechter";
        names[4] = "Susanna Riccardi";
        // Our websites
        websites[0] = "http://atelier.inf.unisi.ch/~balesp/";
        websites[1] = "http://atelier.inf.unisi.ch/~burgev";
        websites[2] = "http://atelier.inf.unisi.ch/~esposem/";
        websites[3] = "http://atelier.inf.unisi.ch/~fiecha/";
        websites[4] = "http://atelier.inf.unisi.ch/~riccas/";
    }

    @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle("This app was developed @ USI by the following students:");

        dialog.setNeutralButton("CLOSE", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) { }
        });

        dialog.setItems(names, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(websites[which].toString()));
                startActivity(browserIntent);
            }
        });
        return dialog.create();
    }

}
