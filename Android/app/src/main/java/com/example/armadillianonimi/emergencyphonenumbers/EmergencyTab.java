package com.example.armadillianonimi.emergencyphonenumbers;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.armadillianonimi.emergencyphonenumbers.R;

/**
 * Created by patrickbalestra on 14/03/2015.
 */
public class EmergencyTab extends Fragment {
    String firenum;
    private TextView textview_fire;
    String policenum;
    private TextView textview_police;
    String medicnum;
    private TextView textview_medic;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.emergency_tab, container, false);
        textview_fire = (TextView) view.findViewById(R.id.firenum);
        if (firenum != null) {
            textview_fire.setText(firenum);
        }
        textview_police = (TextView) view.findViewById(R.id.policenum);
        if (policenum != null) {
            textview_police.setText(policenum);
        }
        textview_medic = (TextView) view.findViewById(R.id.medicalnum);
        if (medicnum != null) {
            textview_medic.setText(medicnum);
        }
        return  view;
    }
    public void setFire(String firetext){
    System.out.println("fire woks, Mr Patrick");
        textview_fire.setText(firetext);
        firenum = firetext;
    }

    public void setPolice(String policetext){
        System.out.println("police woks, Mr Patrick");
        textview_police.setText(policetext);
        policenum = policetext;
    }

    public void call() {

        System.out.println("Should start a call");
    }

    public void setMedical(String medictext){
        System.out.println("medical woks, Mr Patrick");
        textview_medic.setText(medictext);
        medicnum = medictext;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
