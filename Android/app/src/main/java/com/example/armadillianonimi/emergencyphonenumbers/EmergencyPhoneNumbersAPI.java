package com.example.armadillianonimi.emergencyphonenumbers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * This class takes care of connection to the backend and to download the JSON file.
 * We then parse it to extract the version number and all the countries. A Country object containing all its data
 * is instantiated and stored in an ArrayList for later use.
 */

public class EmergencyPhoneNumbersAPI {

    // Singleton instance for this class
    private static EmergencyPhoneNumbersAPI sharedInstance = null;

    // Listener used to communicate changes through a callback with other objects
    private EmergencyAPIListener listener;

    // URL of the backend
    private static String emergencyPhoneNumbersURL = "https://emergency-phone-numbers.herokuapp.com";

    // Name of the file stored in the internal storage
    private static String fileName = "countries.json";

    // Stores the context used to write and read from the internal storage
    private Context context;

    // Object that takes care of doing HTTP requests
    private OkHttpClient client;

    // Contains all the Country objects to be displayed in a list to the user
    private ArrayList<Country> countries;
    private HashMap<String, Country> countryHashMap;

    /**
     * Callback for the URL connection to the server
     */
    private Callback callback = new Callback() {

        /**
         * Handle error of the URL connection (maybe we are offline or an error occured)
         * @param call
         * @param e
         */
        @Override
        public void onFailure(Call call, IOException e) {
            System.out.println("Failed with exception: " + e.getLocalizedMessage());
        }

        /**
         * The Server returned a response so we have to handle it.
         * @param call
         * @param response
         * @throws IOException
         */
        @Override
        public void onResponse(Call call, Response response) throws IOException {
            if (response.code() == 200) {
                System.out.println("Successfully received response with code: 200");
                String JSONString = response.body().string();
                generateCountriesFromString(JSONString);
            } else {
                System.out.println("Received response but with code " + response.code());
                throw new IOException();
            }
        }
    };

    /**
     * Constructor of this class that enqueues the URL request to the Server.
     */
    private EmergencyPhoneNumbersAPI() {
        client = new OkHttpClient();
        countries = new ArrayList<>();
        countryHashMap = new HashMap<>();
    }

    public static EmergencyPhoneNumbersAPI getSharedInstance() {
        if (sharedInstance == null) {
            sharedInstance = new EmergencyPhoneNumbersAPI();
        }
        return sharedInstance;
    }

    public void requestCountries(Context context) {
        this.context = context;
        if (areCountriesCached(context)) {
            // Read countries and communicate to listener to reload UI
            readCountries(context);
            // Check if there is internet connection.
            if (haveNetworkConnection()) {
                Request request = new Request.Builder().url(emergencyPhoneNumbersURL).build();
                client.newCall(request).enqueue(callback);
            }
        } else {
            Request request = new Request.Builder().url(emergencyPhoneNumbersURL).build();
            client.newCall(request).enqueue(callback);
        }
    }

    private boolean areCountriesCached(Context context) {
        File file = context.getFileStreamPath(fileName);
        return file.exists();
    }

    private void readCountries(Context context) {
        try {
            FileInputStream fileInput = context.openFileInput(fileName);
            int c;
            String countries = "";
            while ((c = fileInput.read()) != -1) {
                countries += Character.toString((char)c);
            }
            fileInput.close();
            generateCountriesFromString(countries);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeCountries(Context context, String json) {
        try {
            FileOutputStream fileOut = context.openFileOutput(fileName, Context.MODE_WORLD_READABLE);
            System.out.println(json);
            fileOut.write(json.getBytes());
            fileOut.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void generateCountriesFromString(String JSONString) {
        JSONObject object;
        try {
            countries = new ArrayList<Country>();
            countryHashMap = new HashMap<String, Country>();
            object = new JSONObject(JSONString);
            JSONArray countriesArray = object.getJSONArray("content");
            for (int i = 0; i < countriesArray.length(); i++) {
                Country country = new Country(countriesArray.getJSONObject(i));
                String code = country.getCode();
                countries.add(country);
                countryHashMap.put(code, country);
            }
            writeCountries(context, JSONString);
            // Tell interface that we loaded all the countries
            listener.countriesAvailable(countryHashMap);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    public ArrayList<Country> getCountries() {
        return countries;
    }

    public HashMap<String, Country> getCountryHashMap() {
        return countryHashMap;
    }

    public void setEmergencyAPIListener(EmergencyAPIListener listener) {
        this.listener = listener;
    }

}