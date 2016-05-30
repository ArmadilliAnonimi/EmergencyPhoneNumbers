package com.example.armadillianonimi.emergencyphonenumbers;

import java.util.ArrayList;

public class ContactsList{

    public ArrayList<Contact> contactArrayList;

    public ContactsList(){
        contactArrayList = new ArrayList<Contact>();
    }

    public ContactsList(ArrayList<Contact> contacts) {
        contactArrayList = contacts;
    }

    public int getCount(){
        return contactArrayList.size();
    }

    public void addContact(Contact contact){
        contactArrayList.add(contact);
    }

    public  void removeContact(Contact contact){
        contactArrayList.remove(contact);
    }

    public Contact getContact(int id){

        for(int i=0;i<this.getCount();i++){
            if(Integer.parseInt(contactArrayList.get(i).id)==id)
                return contactArrayList.get(i);
        }

        return null;
    }
}