package com.example.armadillianonimi.emergencyphonenumbers;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import com.google.android.gms.maps.model.LatLng;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationFinder {

    private LocationManager locationManager;
    private LocationManagerListener locationManagerListener;
    private FragmentActivity activity;

    public LocationFinder(FragmentActivity fragmentActivity) {
        activity = fragmentActivity;
    }

    public void setLocationManagerListener(LocationManagerListener listener) {
        locationManagerListener = listener;
    }

    // Request the user location and wait for the Network to return it
    public void requestLocation() {
        locationManager = (LocationManager) activity.getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
    }

    private LocationListener locationListener = new LocationListener() {
        @Override
        // Method for finding the location.
        public void onLocationChanged(Location location) {

            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            LatLng coordinates = new LatLng(latitude, longitude);

            // Create a new object UserLocation with the coordinates we have found.
            UserLocation userLocation = new UserLocation();
            userLocation.position = coordinates;

            // From the coordinates, we find the complete address and the country and we use it to set the UserLocation object created before.
            Geocoder geocoder = new Geocoder(activity.getApplicationContext(), Locale.getDefault());
            try {
                List<Address> listAddresses = geocoder.getFromLocation(latitude, longitude, 1);
                if (listAddresses != null && listAddresses.size() > 0) {
                    // Street & Number, CAP City, Country,
                    android.location.Address myAddress = listAddresses.get(0);
                    String locationString = myAddress.getThoroughfare() + " " + myAddress.getFeatureName() + ", " + myAddress.getPostalCode() + " " + myAddress.getLocality() + ", " + myAddress.getCountryName();
                    userLocation.completeAddress = locationString + "\n" + latitude + ", " + longitude;
                    userLocation.country = myAddress.getCountryName();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            locationManagerListener.locationReceived(userLocation);

            // If we have found the location, we stop updating the map.
            if (locationManager != null && checkLocationPermission()) {
                locationManager.removeUpdates(locationListener);
            }
        }


        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
            //
        }

        // If the provider is enabled, do nothing.
        @Override
        public void onProviderEnabled(String s) {
            //
        }

        // If the provider is disabled, set the text in the addressText in the LocationTab class as "Location disabled ...".
        @Override
        public void onProviderDisabled(String s) {
            if (activity instanceof MainActivity) {
                MainActivity main = (MainActivity)activity;
                LocationTab locationTab = (LocationTab)main.adapter.getItem(1);
                locationTab.setProviderDisabled();
            }
        }
    };

    // If the access to the location was not yet granted, ask for it.
    public boolean hasPermissionToAccessLocation() {
        if (!checkLocationPermission()) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            return false;
        } else {
            return true;
        }
    }

    // Method by default in Android to deal with location permissions.
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (activity instanceof MainActivity) {
                        MainActivity main = (MainActivity)activity;
                        LocationTab locationTab = (LocationTab)main.adapter.getItem(1);
                        locationTab.loadMap();
                    }
                }
            }
        }
    }

    public boolean checkLocationPermission() {
        String permission = "android.permission.ACCESS_FINE_LOCATION";
        String permissionCoarse = "android.permission.ACCESS_COARSE_LOCATION";
        int res = activity.checkCallingOrSelfPermission(permission);
        int resCoarse = activity.checkCallingOrSelfPermission(permissionCoarse);
        return res == PackageManager.PERMISSION_GRANTED && resCoarse == PackageManager.PERMISSION_GRANTED;
    }
}
