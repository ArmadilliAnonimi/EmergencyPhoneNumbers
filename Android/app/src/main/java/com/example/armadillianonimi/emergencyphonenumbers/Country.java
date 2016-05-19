package com.example.armadillianonimi.emergencyphonenumbers;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by patrickbalestra on 4/30/16.
 * Object that stored all the information for a country.
 */
public class Country {

    // JSON keys returned from the Server
    private final String nameKey = "name";
    private final String codeKey = "code";
    private final String policeKey = "police";
    private final String medicalKey = "medical";
    private final String fireKey = "fire";

    // Fields
    private String name;
    private String code;
    private String police;
    private String medical;
    private String fire;

    /**
     * Constructor of this class.
     *
     * @param name    of the country
     * @param code    (2-digit format) of the country
     * @param police  phone number of the country
     * @param medical phone number of the country
     * @param fire    phone number of the country
     */
    private void construct(String name, String code, String police, String medical, String fire) {
        this.name = name;
        this.code = code;
        this.police = police;
        this.medical = medical;
        this.fire = fire;
    }

    /**
     * Convenience constructor to build a a Country object directly from the JSONObject.
     *
     * @param object JSONObject returned found in the JSON file
     */
    public Country(JSONObject object) {
        try {
            this.construct(object.getString(nameKey), object.getString(codeKey), object.getString(policeKey), object.getString(medicalKey), object.getString(fireKey));
        } catch (JSONException e) {
            System.out.println("Failed to deserialize Country JSONObject");
            e.printStackTrace();
        }
    }

    public String getFire() {
        return fire;
    }

    public String getPolice() {
        return police;
    }
    public String getMedical() {
        return medical;
    }
}
