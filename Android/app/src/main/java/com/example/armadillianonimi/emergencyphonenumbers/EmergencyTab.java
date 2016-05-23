package com.example.armadillianonimi.emergencyphonenumbers;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

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
        return view;
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("RESULT");

        if (requestCode == CONTACT_PICK_REQUEST && resultCode == RESULT_CODE_OK) {

            ArrayList<Contact> selectedContacts = data.getParcelableArrayListExtra("SelectedContacts");

            String display = "";
            for (int i = 0; i < selectedContacts.size(); i++){

                display += (i+1) + ". " + selectedContacts.get(i).toString() + "\n";

            }
//            contactsDisplay.setText("Selected Contacts : \n\n"+display);
        }
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
