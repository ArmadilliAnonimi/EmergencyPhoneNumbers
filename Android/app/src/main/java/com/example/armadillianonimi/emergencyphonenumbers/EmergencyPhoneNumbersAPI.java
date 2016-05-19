package com.example.armadillianonimi.emergencyphonenumbers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by patrickbalestra on 4/30/16.
 * This class takes care of connection to the backend and to download the JSON file.
 * We then parse it to extract the version number and all the countries. A Country object containing all its data
 * is instantiated and stored in an ArrayList for later use.
 */

public class EmergencyPhoneNumbersAPI {

    // URL of the backend hsoted on Heroku
    private static String url = "https://emergency-phone-numbers.herokuapp.com";

    // TODO: to be stored on disk and compared with the JSON version
    private double APIVersion = 0.1;

    // Contains all the Country objects to be displayed in a list to the user
    private ArrayList<Country> countries;
    private MainActivity activity;

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
                JSONObject object = null;
                try {
                    object = new JSONObject(JSONString);
                    double contentVersion = object.getDouble("version");
                    APIVersion = contentVersion;

                    JSONArray countriesArray = object.getJSONArray("content");
                    for (int i = 0; i < countriesArray.length(); i++) {
                        Country country = new Country(countriesArray.getJSONObject(i));
                        countries.add(country);
                    }
                    activity.setNumber(countries);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                System.out.println("Received response but with code " + response.code());
                throw new IOException();
            }
        }
    };

    /**
     * Constructor of this class that enqueues the URL request to the Server.
     */
    public EmergencyPhoneNumbersAPI(MainActivity activity) {
        this.activity = activity;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(callback);
        countries = new ArrayList<>();
    }
}
