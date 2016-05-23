package com.example.armadillianonimi.emergencyphonenumbers;

/**
 * Created by Emanuele on 23/05/16.
 */
import android.app.Activity;
import android.content.Context;
import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;


import java.util.ArrayList;


public class ContactsListAdapter extends BaseAdapter {

    Context context;
    ContactsList contactsList,filteredContactsList,selectedContactsList;
    String filterContactName;

    ContactsListAdapter(Context context, ContactsList contactsList){

        super();
        this.context = context;
        this.contactsList = contactsList;
        this.filteredContactsList=new ContactsList();
        this.selectedContactsList = new ContactsList();
        this.filterContactName = "";
    }

    public void filter(String filterContactName){



        filteredContactsList.contactArrayList.clear();

        if(filterContactName.isEmpty() || filterContactName.length()<1){
            filteredContactsList.contactArrayList.addAll(contactsList.contactArrayList);
            this.filterContactName = "";

        }
        else {
            this.filterContactName = filterContactName.toLowerCase().trim();
            for (int i = 0; i < contactsList.contactArrayList.size(); i++) {

                if (contactsList.contactArrayList.get(i).name.toLowerCase().contains(filterContactName))
                    filteredContactsList.addContact(contactsList.contactArrayList.get(i));
            }
        }
        notifyDataSetChanged();

    }

    public void addContacts(ArrayList<Contact> contacts){
        this.contactsList.contactArrayList.addAll(contacts);
        this.filter(this.filterContactName);

    }

    @Override
    public int getCount() {
        return filteredContactsList.getCount();
    }

    @Override
    public Contact getItem(int position) {
        return filteredContactsList.contactArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(this.getItem(position).id);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if(convertView==null){
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();

            convertView = inflater.inflate(R.layout.contactitem, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.chkContact = (CheckBox) convertView.findViewById(R.id.chk_contact);
            convertView.setTag(viewHolder);

        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.chkContact.setText(this.filteredContactsList.contactArrayList.get(position).toString());
        viewHolder.chkContact.setId(Integer.parseInt(this.filteredContactsList.contactArrayList.get(position).id));
        viewHolder.chkContact.setChecked(alreadySelected(filteredContactsList.contactArrayList.get(position)));

        viewHolder.chkContact.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Contact contact = filteredContactsList.getContact(buttonView.getId());

                if(contact!=null && isChecked && !alreadySelected(contact)){
                    selectedContactsList.addContact(contact);
                }
                else if(contact!=null && !isChecked){
                    selectedContactsList.removeContact(contact);
                }
            }
        });

        return convertView;
    }

    public boolean alreadySelected(Contact contact)
    {
        if(this.selectedContactsList.getContact(Integer.parseInt(contact.id))!=null)
            return true;

        return false;
    }

    public static class ViewHolder{

        CheckBox chkContact;
    }
}