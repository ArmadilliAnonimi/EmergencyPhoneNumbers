package com.example.armadillianonimi.emergencyphonenumbers;

/**
 * Created by Emanuele on 23/05/16.
 */
import java.util.ArrayList;


public class ContactsList{

    public ArrayList<Contact> contactArrayList;

    ContactsList(){

        contactArrayList = new ArrayList<Contact>();
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