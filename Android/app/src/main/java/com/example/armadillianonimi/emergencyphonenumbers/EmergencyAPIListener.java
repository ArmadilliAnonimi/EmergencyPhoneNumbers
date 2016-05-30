package com.example.armadillianonimi.emergencyphonenumbers;

import java.util.HashMap;

public interface EmergencyAPIListener {
    void countriesAvailable(HashMap<String, Country> countryHashMap);
}
