package com.example.armadillianonimi.emergencyphonenumbers;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.identity.intents.Address;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationTab extends Fragment {

    private MapView mapView;
    private TextView addressTextView;
    private GoogleMap googleMap;
    private LocationManager locationManager;
    private Marker marker;
    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {

            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            LatLng coordinates = new LatLng(latitude, longitude);

            googleMap = mapView.getMap();
            System.out.println("Latitude:" + latitude + ", Longitude:" + longitude);

            if (marker != null) {
                marker.remove();
            }
            marker = googleMap.addMarker(new MarkerOptions().position(coordinates).title("You are here").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

            // Adding marker on the map.
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(coordinates).zoom(16).build();
            googleMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));

            Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
            try {
                List<android.location.Address> listAddresses = geocoder.getFromLocation(latitude, longitude, 1);
                if(listAddresses != null && listAddresses.size() > 0){
                    // Street & Number, CAP City, Country,
                    android.location.Address myAddress = listAddresses.get(0);
                    String locationString = myAddress.getThoroughfare() + " " + myAddress.getFeatureName() + ", " + myAddress.getPostalCode() + " " + myAddress.getLocality() + ", " + myAddress.getCountryName();
                    addressTextView.setText(locationString + "\n" + latitude + ", " + longitude);

                    System.out.println(locationString);
                    System.out.println("Found location: " + listAddresses.toString());
                }
            } catch (IOException e) {
                System.out.println("ERROR");
                e.printStackTrace();
            }
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
            System.out.println("Provider status changed");
        }

        @Override
        public void onProviderEnabled(String s) {
            System.out.println("Provider enabled");
        }

        @Override
        public void onProviderDisabled(String s) {
            System.out.println("Provider disabled");
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflat and return the layout.
        View view = inflater.inflate(R.layout.location_tab, container, false);
        mapView = (MapView) view.findViewById(R.id.mapView);
        addressTextView = (TextView) view.findViewById(R.id.addresstextview);
        addressTextView.setText("Finding your location...");
        mapView.onCreate(savedInstanceState);
        mapView.onResume();

        // Initialize the location manager
        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        requestPermissionToAccessLocation();
    }

    public void loadMap() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // Map callbacks.
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
        //locationManager.removeUpdates(locationListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    private void requestPermissionToAccessLocation() {
        // If the access to the location was not yet granted, ask for it.
        if (!checkLocationPermission()) {
            System.out.println("Requesting permission");
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        } else {
            System.out.println("load map");
            loadMap();
        }
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    loadMap();
                }
            }
            // Check other permissions in the future
        }
    }

    public boolean checkLocationPermission() {
        String permission = "android.permission.ACCESS_FINE_LOCATION";
        String permissionCoarse = "android.permission.ACCESS_COARSE_LOCATION";
        int res = getActivity().checkCallingOrSelfPermission(permission);
        int resCoarse = getActivity().checkCallingOrSelfPermission(permissionCoarse);
        return res == PackageManager.PERMISSION_GRANTED && resCoarse == PackageManager.PERMISSION_GRANTED;
    }
}