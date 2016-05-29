package com.example.armadillianonimi.emergencyphonenumbers;

/**
 * Created by Emanuele on 23/05/16.
 */
import android.os.Parcel;
import android.os.Parcelable;


public class Contact implements Parcelable {

    public String id,name,phone,label;

    Contact(String id, String name,String phone,String label){
        this.id=id;
        this.name=name;
        this.phone=phone;
        this.label=label;
    }

    protected Contact(Parcel in) {
        id = in.readString();
        name = in.readString();
        phone = in.readString();
        label = in.readString();
    }

    public static final Creator<Contact> CREATOR = new Creator<Contact>() {
        @Override
        public Contact createFromParcel(Parcel in) {
            return new Contact(in);
        }

        @Override
        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };

    @Override
    public String toString()
    {
        return name+" | "+label+" : "+phone+" --- id "+id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(phone);
        dest.writeString(label);
    }
}