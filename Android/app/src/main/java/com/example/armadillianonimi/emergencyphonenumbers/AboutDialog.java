package com.example.armadillianonimi.emergencyphonenumbers;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
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
        Resources res = getResources();
        names = res.getStringArray(R.array.names_array);
        websites = res.getStringArray(R.array.websites_url_array);
    }

    @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle(R.string.about_dialog_title);

        dialog.setNeutralButton(R.string.CLOSE, new DialogInterface.OnClickListener() {
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
