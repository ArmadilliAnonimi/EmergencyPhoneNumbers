package com.example.armadillianonimi.emergencyphonenumbers;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class ContactsPickerActivity extends AppCompatActivity {

    public static String SELECTED = "selected";

    ListView contactsChooser;
    Button btnDone;
    Button btnCancel;
    Button btnRemoveAll;
    EditText txtFilter;
    TextView txtLoadInfo;
    ContactsListAdapter contactsListAdapter;
    ContactsLoader contactsLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setNavigationBarColor(getResources().getColor(R.color.colorPrimaryDark));
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        contactsChooser = (ListView) findViewById(R.id.lst_contacts_chooser);
        btnDone = (Button) findViewById(R.id.btn_done);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnRemoveAll = (Button) findViewById(R.id.btn_remove_all);
        txtFilter = (EditText) findViewById(R.id.txt_filter);
        txtLoadInfo = (TextView) findViewById(R.id.txt_load_progress);

        contactsListAdapter = new ContactsListAdapter(this, new ContactsList());

        contactsChooser.setAdapter(contactsListAdapter);

        loadContacts("");

        String addedContactsList =  getIntent().getStringExtra(EmergencyTab.ALREADY_ADDED);
        Gson gson = new Gson();
        java.lang.reflect.Type type = new TypeToken<LinkedHashMap<String, Contact>>(){}.getType();
        final LinkedHashMap<String, Contact> alreadyAdded = gson.fromJson(addedContactsList, type);
        contactsListAdapter.setSelectedContactsList(alreadyAdded);

        txtFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                contactsListAdapter.filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                Gson gson = new Gson();
                String addedContactsList = gson.toJson(contactsListAdapter.selectedContactsList);
                resultIntent.putExtra(ContactsPickerActivity.SELECTED, addedContactsList);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                setResult(RESULT_CANCELED, resultIntent);
                finish();
            }
        });

        btnRemoveAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactsListAdapter.selectedContactsList = new LinkedHashMap<>();
                contactsListAdapter.notifyDataSetChanged();
                CharSequence message = getResources().getString(R.string.contacts_toast);
                Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER,0,-100);
                toast.show();
            }
        });
    }

    private void loadContacts(String filter) {
        if (contactsLoader!=null && contactsLoader.getStatus()!= AsyncTask.Status.FINISHED) {
            try {
                contactsLoader.cancel(true);
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        if (filter==null) filter="";

        try {
            //Running AsyncLoader with adapter and  filter
            contactsLoader = new ContactsLoader(this,contactsListAdapter);
            contactsLoader.txtProgress = txtLoadInfo;
            contactsLoader.execute(filter);
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
