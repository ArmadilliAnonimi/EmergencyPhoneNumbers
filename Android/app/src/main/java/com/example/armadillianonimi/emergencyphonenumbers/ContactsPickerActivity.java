package com.example.armadillianonimi.emergencyphonenumbers;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import java.util.HashMap;

public class ContactsPickerActivity extends AppCompatActivity {

    public static String SELECTED = "selected";

    ListView contactsChooser;
    Button btnDone;
    Button btnCancel;
    EditText txtFilter;
    TextView txtLoadInfo;
    ContactsListAdapter contactsListAdapter;
    ContactsLoader contactsLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact);

        contactsChooser = (ListView) findViewById(R.id.lst_contacts_chooser);
        btnDone = (Button) findViewById(R.id.btn_done);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        txtFilter = (EditText) findViewById(R.id.txt_filter);
        txtLoadInfo = (TextView) findViewById(R.id.txt_load_progress);

        contactsListAdapter = new ContactsListAdapter(this, new ContactsList());

        contactsChooser.setAdapter(contactsListAdapter);

        loadContacts("");

        Bundle extras = getIntent().getExtras();
        final HashMap<String, Contact> alreadyAdded = (HashMap<String, Contact>) extras.get(EmergencyTab.ALREADY_ADDED);
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
                resultIntent.putExtra(ContactsPickerActivity.SELECTED, contactsListAdapter.selectedContactsList);
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
