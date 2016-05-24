package com.example.armadillianonimi.emergencyphonenumbers;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by patrickbalestra on 14/03/2015.
 */
public class EmergencyTab extends Fragment {

    private String fireNumber;
    private TextView fireTextView;
    private String policeNumber;
    private TextView policeTextView;
    private String medicalNumber;
    private TextView medicalTextView;
    private FloatingActionButton addContactButton;
    HashMap<String,String> elements = new HashMap<>();
    final int CONTACT_PICK_REQUEST = 1000;
    final int RESULT_CODE_OK = -1;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.emergency_tab, container, false);

        fireTextView = (TextView) view.findViewById(R.id.firenum);
        if (fireNumber != null) {
            fireTextView.setText(fireNumber);
        }
        policeTextView = (TextView) view.findViewById(R.id.policenum);
        if (policeNumber != null) {
            policeTextView.setText(policeNumber);
        }
        medicalTextView = (TextView) view.findViewById(R.id.medicalnum);
        if (medicalNumber != null) {
            medicalTextView.setText(medicalNumber);
        }

        addContactButton = (FloatingActionButton)view.findViewById(R.id.btn);
        setupAddContactButton();

        for(String el : elements.keySet()){
            LinearLayout l = (LinearLayout) view.findViewById(R.id.main);
            CardView c = new CardView(getContext());
            RelativeLayout r = new RelativeLayout(getContext());
            TextView t = new TextView(getContext());
            l.addView(c);
            c.addView(r);
            r.addView(t);
            t.setText(el);
        }
        return view;
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == CONTACT_PICK_REQUEST && resultCode == RESULT_CODE_OK) {

            ArrayList<Contact> selectedContacts = data.getParcelableArrayListExtra("SelectedContacts");


            for (int i = 0; i < selectedContacts.size(); i++){
                if (!(elements.containsKey(selectedContacts.get(i).name))) {
                    elements.put(selectedContacts.get(i).name, selectedContacts.get(i).phone);
                }
            }

        }
    }

    public HashMap<String,String> getElements(){
        return elements;
    }
    private void setupAddContactButton() {
        addContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentContactPick = new Intent(getContext(),ContactsPickerActivity.class);
                startActivityForResult(intentContactPick, CONTACT_PICK_REQUEST);
            }
        });
    }

    public void setFire(String firetext){
        fireTextView.setText(firetext);
        fireNumber = firetext;
    }

    public void setPolice(String policetext){
        policeTextView.setText(policetext);
        policeNumber = policetext;
    }

    public void setMedical(String medictext){
        medicalTextView.setText(medictext);
        medicalNumber = medictext;
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
