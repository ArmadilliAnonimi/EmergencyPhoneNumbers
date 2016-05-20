package com.example.armadillianonimi.emergencyphonenumbers;

import java.util.ArrayList;

/**
 * Created by Emanuele on 20/05/16.
 */
public interface EmergencyAPIListener {
    void countriesAvailable(ArrayList<Country> countries);
}
