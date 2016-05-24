package com.example.armadillianonimi.emergencyphonenumbers;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by patrickbalestra on 14/03/2015.
 */
public class EmergencyTab extends Fragment {

    public String fireNumber;
    public String policeNumber;
    public String medicalNumber;

    private TextView fireTextView;
    private TextView policeTextView;
    private TextView medicalTextView;
    private FloatingActionButton addContactButton;
    private ArrayList<String[]> elements = new ArrayList<>();
    private CardView c;
    private final int CONTACT_PICK_REQUEST = 1000;
    private final int RESULT_CODE_OK = -1;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.emergency_tab, container, false);
        System.out.println("Initializing fireTextView");
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

        if (requestCode == CONTACT_PICK_REQUEST && resultCode == RESULT_CODE_OK) {
            final ArrayList<Contact> selectedContacts = data.getParcelableArrayListExtra("SelectedContacts");
            for (int i = 0; i < selectedContacts.size(); i++){
               // if (!(elements.containsKey(selectedContacts.get(i).name))) {
                String[] person = new String[2];
                person[0]= selectedContacts.get(i).name;
                person[1] = selectedContacts.get(i).phone;
                    elements.add( person);
                    LinearLayout l = (LinearLayout) getView().findViewById(R.id.main);
                    c = new CardView(getContext());
                    c.isFocusable();
                    c.isClickable();
                    c.setId(i);
                    RelativeLayout r = new RelativeLayout(getContext());
                    TextView t = new TextView(getContext());
                    l.addView(c);
                    c.addView(r);
                    r.addView(t);
                    t.setText(selectedContacts.get(i).name);
               // }
            }
            c.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    call2(v);

                }
            });
        }
    }
    public void call2(View view) {
        //if (checkPermission(Manifest.permission.CALL_PHONE)) {
            int id = view.getId();
            Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + elements.get(view.getId())[0]));

                //emergencyTab.getElements()
//                    default:
//                        callIntent.setData(Uri.parse("tel:" +elements.get(id)));

            startActivity(callIntent);
        //} else {
            //request(Manifest.permission.CALL_PHONE);
            //Toast.makeText(getApplicationContext(), "Please, allow call permission.\nIt's good for you!", Toast.LENGTH_SHORT).show();
        }
    //}


    public ArrayList<String[]> getElements(){
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

    public void updateUI() {
        if (fireTextView != null) {
            fireTextView.setText(fireNumber);
        }

        if (policeTextView != null) {
            policeTextView.setText(policeNumber);
        }

        if (medicalTextView != null) {
            medicalTextView.setText(medicalNumber);
        }
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
