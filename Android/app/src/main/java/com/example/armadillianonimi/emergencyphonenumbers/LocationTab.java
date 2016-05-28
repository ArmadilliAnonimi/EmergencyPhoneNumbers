package com.example.armadillianonimi.emergencyphonenumbers;

import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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

    private LocationFinder locationFinder;
    private MapView mapView;
    private TextView addressTextView;
    private GoogleMap googleMap;
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
            marker = googleMap.addMarker(new MarkerOptions().position(coordinates).title(getString(R.string.you_are_here)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

            // Adding marker on the map.
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(coordinates).zoom(16).build();
            googleMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));

            Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
            try {
                List<android.location.Address> listAddresses = geocoder.getFromLocation(latitude, longitude, 1);
                if (listAddresses != null && listAddresses.size() > 0) {
                    // Street & Number, CAP City, Country,
                    android.location.Address myAddress = listAddresses.get(0);
                    String locationString = myAddress.getThoroughfare() + " " + myAddress.getFeatureName() + ", " + myAddress.getPostalCode() + " " + myAddress.getLocality() + ", " + myAddress.getCountryName();
                    addressTextView.setText(locationString + "\n" + latitude + ", " + longitude);
                    addressTextView.setContentDescription(locationString + "\n" + latitude + ", " + longitude);

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
            if (addressTextView != null) {
                addressTextView.setText("Location disabled.\nPlease check your settings.");
                addressTextView.setContentDescription("Location disabled.\nPlease check your settings.");
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate and return the layout.
        View view = inflater.inflate(R.layout.location_tab, container, false);
        mapView = (MapView) view.findViewById(R.id.mapView);
        addressTextView = (TextView) view.findViewById(R.id.addresstextview);
        addressTextView.setText(R.string.finding_location);
        addressTextView.setContentDescription(getString(R.string.finding_location));
        mapView.onCreate(savedInstanceState);
        mapView.onResume();

        locationFinder = new LocationFinder(getActivity());
        locationFinder.setLocationManagerListener(new LocationManagerListener() {
            @Override
            public void locationReceived(UserLocation location) {
                addressTextView.setText(location.completeAddress);
                addressTextView.setContentDescription(location.completeAddress);

                if (mapView != null) {
                    googleMap = mapView.getMap();

                    if (marker != null) {
                        marker.remove();
                    }

                    // Adding marker on the map.
                    marker = googleMap.addMarker(new MarkerOptions().position(location.position).title(getString(R.string.you_are_here)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

                    // Moving position of the camera in the map depending on the position of the marker.
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(location.position).zoom(16).build();
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }
            }
        });
        return view;
    }

    // If we have permissions to access location, load the map.
    @Override
    public void onStart() {
        super.onStart();

        if (locationFinder.hasPermissionToAccessLocation()) {
            loadMap();
        }
    }

    // Method that creates the map.
    public void loadMap() {
        locationFinder.requestLocation();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // If the provider is disabled, set the addressTextView as:
    public void setProviderDisabled() {
        if (addressTextView != null) {
        addressTextView.setText("Location disabled.\nPlease check your settings.");
        addressTextView.setContentDescription("Location disabled.\nPlease check your settings.");
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
}