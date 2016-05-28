package com.example.armadillianonimi.emergencyphonenumbers;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.CardView;
import android.text.Layout;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

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
    //private ArrayList<String[]> elements = new ArrayList<>();
    private HashMap<Integer,String[]> elements2 = new HashMap<>();
    private CardView c;
    private final int CONTACT_PICK_REQUEST = 1000;
    private final int RESULT_CODE_OK = -1;
    private static final int PERMISSION_REQUEST_CODE = 1;

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

        if (elements2.size() > 0){
            for (Integer i : elements2.keySet()){
                LinearLayout l = (LinearLayout) view.findViewById(R.id.main);
                createCard(elements2.get(i)[0],elements2.get(i)[1], false,i, l);

            }
        }

        System.out.println("EmergencyFragment: onCreateView");
        return view;
    }

    private int inDP(int num){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, num, getResources().getDisplayMetrics());
    }
    public Drawable getSelectedItemDrawable() {
        int[] attrs = new int[]{R.attr.selectableItemBackground};
        TypedArray ta = getActivity().obtainStyledAttributes(attrs);
        Drawable selectedItemDrawable = ta.getDrawable(0);
        ta.recycle();
        return selectedItemDrawable;
    }

    public void createCard(String name, String phone, boolean flag, int i,LinearLayout l){
            if (!(elements2.containsKey(phone.hashCode())) ) {
                c = new CardView(getContext());
                FrameLayout.LayoutParams v = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                v.gravity = Gravity.CENTER;
                c.setLayoutParams(v);
                l.addView(c);
                c.isFocusable();
                c.isClickable();
                c.setForeground(getSelectedItemDrawable());
                c.setPadding(40, 40, 40, 40);
                String[] person = new String[2];
                person[0] = name;
                person[1] = phone;
                if (flag) {
                        int n = person[1].hashCode();
                        elements2.put(n, person);
                        c.setId(n);
                } else {
                    c.setId(i);
                }
                ViewGroup.MarginLayoutParams m = (ViewGroup.MarginLayoutParams) c.getLayoutParams();
                m.setMargins(inDP(10), inDP(5), inDP(10), inDP(5));
                c.setContentPadding(inDP(10), inDP(10), inDP(10), inDP(10));
                c.setLayoutParams(m);
                c.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        call2(v);

                    }
                });


                RelativeLayout r = new RelativeLayout(getContext());
                r.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.MATCH_PARENT));
                c.addView(r);


                ImageView img = new ImageView(getContext());
                r.addView(img);
                img.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.phone_icon));
                RelativeLayout.LayoutParams d = (RelativeLayout.LayoutParams) img.getLayoutParams();
                d.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                d.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
                img.getLayoutParams().height = inDP(40);
                img.getLayoutParams().width = inDP(40);
                img.setLayoutParams(d);
                ViewGroup.MarginLayoutParams ma = (ViewGroup.MarginLayoutParams) img.getLayoutParams();
                ma.setMargins(0, 0, inDP(10), 0);
                img.setContentDescription("Phone icon");
                img.setColorFilter(Color.parseColor("#616161"));
                img.setLayoutParams(ma);
//            ImageView otherimg = (ImageView) getActivity().findViewById(R.id.img);
//            ViewGroup.MarginLayoutParams imgpar = (ViewGroup.MarginLayoutParams) otherimg.getLayoutParams();
//            img.setLayoutParams(imgpar);


                TextView number = new TextView(getContext());
                r.addView(number);
                number.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                RelativeLayout.LayoutParams da = (RelativeLayout.LayoutParams) number.getLayoutParams();
                da.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                da.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
                number.setLayoutParams(da);
                number.setText(phone);
                number.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                number.setTextColor(Color.parseColor("#616161"));
                number.setPadding(inDP(10), inDP(10), inDP(10), inDP(10));
                ViewGroup.MarginLayoutParams mar = (ViewGroup.MarginLayoutParams) number.getLayoutParams();
                mar.setMargins(0, 0, inDP(50), 0);
                number.setLayoutParams(mar);

//            TextView othertext = (TextView) getActivity().findViewById(R.id.policenum);
//            ViewGroup.MarginLayoutParams textparam = (ViewGroup.MarginLayoutParams) othertext.getLayoutParams();
//            number.setLayoutParams(textparam);

                TextView number2 = new TextView(getContext());
                r.addView(number2);
                number2.setText(name);
                number2.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                RelativeLayout.LayoutParams das = (RelativeLayout.LayoutParams) number2.getLayoutParams();
                das.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
                number2.setLayoutParams(das);
                number2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 35);
                number2.setTextColor(Color.parseColor("#616161"));
                number2.setPadding(inDP(10), inDP(10), inDP(10), inDP(10));
//            TextView othertext2 = (TextView) getActivity().findViewById(R.id.pol);
//            ViewGroup.MarginLayoutParams textparam2 = (ViewGroup.MarginLayoutParams) othertext2.getLayoutParams();
//            number2.setLayoutParams(textparam2);
            }
        }



    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CONTACT_PICK_REQUEST && resultCode == RESULT_CODE_OK) {
            final ArrayList<Contact> selectedContacts = data.getParcelableArrayListExtra("SelectedContacts");
            for (int i = 0; i < selectedContacts.size(); i++) {
                LinearLayout l = (LinearLayout) getView().findViewById(R.id.main);
                createCard(selectedContacts.get(i).name,selectedContacts.get(i).phone, true,i, l);

            }
        }
        System.out.println("EmergencyFragment: onActivityResult");
    }
    public boolean checkPermission(String permission){
        if (ContextCompat.checkSelfPermission(getContext(), permission)
                != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }
    public void request(String permission){
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{permission},
                PERMISSION_REQUEST_CODE);
    }
    public void call2(View view) {
        if (checkPermission(Manifest.permission.CALL_PHONE)) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + elements2.get(view.getId())[1]));
            startActivity(callIntent);
        } else {
            request(Manifest.permission.CALL_PHONE);
            Toast.makeText(getContext(), "Please, allow call permission.\nIt's good for you!", Toast.LENGTH_SHORT).show();
        }
    }


    public HashMap<Integer,String[]> getElements(){
        return elements2;
    }

    private void setupAddContactButton() {
        addContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission(Manifest.permission.READ_CONTACTS)) {
                    Intent intentContactPick = new Intent(getContext(), ContactsPickerActivity.class);
                    startActivityForResult(intentContactPick, CONTACT_PICK_REQUEST);
                }else {
                    request(Manifest.permission.READ_CONTACTS);
                    Toast.makeText(getContext(), "Please, if you want to add a contact, allow contact permission.", Toast.LENGTH_SHORT).show();
                }

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
        System.out.println("EmergencyFragment: onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        System.out.println("EmergencyFragment: onPause");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("EmergencyFragment: onDestroy");
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        System.out.println("EmergencyFragment: onLowMemory");
    }
}
