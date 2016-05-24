package com.example.armadillianonimi.emergencyphonenumbers;

import java.util.HashMap;

/**
 * Created by Emanuele on 20/05/16.
 */
public interface EmergencyAPIListener {
    void countriesAvailable(HashMap<String, Country> countryHashMap);
}
