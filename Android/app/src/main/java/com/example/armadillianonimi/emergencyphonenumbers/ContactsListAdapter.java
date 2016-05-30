package com.example.armadillianonimi.emergencyphonenumbers;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;


import java.util.ArrayList;
import java.util.HashMap;

public class ContactsListAdapter extends BaseAdapter {

    Context context;
    ContactsList contactsList,filteredContactsList;
    HashMap<String, Contact> selectedContactsList;
    String filterContactName;

    ContactsListAdapter(Context context, ContactsList contactsList){
        super();
        this.context = context;
        this.contactsList = contactsList;
        this.filteredContactsList = new ContactsList();
        this.selectedContactsList = new HashMap<>();
        this.filterContactName = "";
    }

    public void setSelectedContactsList(HashMap<String, Contact> alreadyAddedContacts) {
        selectedContactsList = alreadyAddedContacts;
    }

    public void filter(String filterContactName) {

        filteredContactsList.contactArrayList.clear();

        if (filterContactName.isEmpty() || filterContactName.length()<1) {
            filteredContactsList.contactArrayList.addAll(contactsList.contactArrayList);
            this.filterContactName = "";
        } else {
            this.filterContactName = filterContactName.toLowerCase().trim();
            for (int i = 0; i < contactsList.contactArrayList.size(); i++) {
                if (contactsList.contactArrayList.get(i).name.toLowerCase().contains(filterContactName))
                    filteredContactsList.addContact(contactsList.contactArrayList.get(i));
            }
        }

        notifyDataSetChanged();

    }

    public void addContacts(ArrayList<Contact> contacts) {
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

        } else {
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
                    selectedContactsList.put(contact.id, contact);
                    System.out.println("======+ Added: "+contact.name+" --- id "+contact.id);
                    System.out.println("\t\t\t Selected: "+selectedContactsList.size());
                }
                else if(contact!=null && !isChecked){
                    selectedContactsList.remove(contact.id);
                    System.out.println("======x Removed: "+contact.name+" --- id "+contact.id);
                    System.out.println("\t\t\t Selected: "+selectedContactsList.size());
                }
            }
        });

        return convertView;
    }

    public boolean alreadySelected(Contact contact) {
        return (this.selectedContactsList.containsKey(contact.id));
    }

    public static class ViewHolder {
        CheckBox chkContact;
    }
}