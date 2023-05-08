package com.example.test.google_maps;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.test.R;
import com.example.test.databinding.FragmentMapBinding;
import com.example.test.helper_classes.Global;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapFragment extends Fragment {

    FragmentMapBinding binding;
    LatLng lodhaMallLatLng;
    LocationManager locationManager;
    double userLatitude;
    double userLongitude;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_map, container, false);
        check_If_LocationTurnedOn();

        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapFragment);

        assert supportMapFragment != null;
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {

                // Create a Geocoder object to handle geocoding
                Geocoder geocoder = new Geocoder(getContext());

// Define the address string for Lodha Xperia Mall
                String addressString = "Lodha Xperia Mall, Kalyan Shil Road, Dombivli East, Maharashtra, India";

// Declare a LatLng variable to hold the coordinates
                lodhaMallLatLng = null;

                try {
                    // Use the Geocoder to get the address information
                    List<Address> addresses = geocoder.getFromLocationName(addressString, 1);

                    // If the Geocoder found a result, extract the latitude and longitude
                    if (addresses != null && addresses.size() > 0) {
                        Address address = addresses.get(0);
                        lodhaMallLatLng = new LatLng(address.getLatitude(), address.getLongitude());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

// If we were able to obtain the coordinates, add the marker to the map and animate the camera
                if (lodhaMallLatLng != null) {
                    // Create a MarkerOptions object and set its position and title
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(lodhaMallLatLng);
                    markerOptions.title("Lodha Xperia Mall");

                    // Add the marker to the map and animate the camera to center on the marker
                    googleMap.addMarker(markerOptions);
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lodhaMallLatLng, 15));
                } else {
                    // If we were not able to obtain the coordinates, log an error
                    Log.e("MapsActivity", "Unable to geocode address: " + addressString);
                    System.out.println("Unable to geocode address: " + addressString);
                }


                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {

                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(latLng);
                        markerOptions.title("Here LatLong:" + latLng.latitude + "," + latLng.longitude);
                        googleMap.clear();
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20));
                        googleMap.addMarker(markerOptions);
                    }
                });
            }
        });

          getCurrentLocationAndDistance();
        return binding.getRoot();
    }


    private void check_If_LocationTurnedOn() {


        Global.showToast(getContext(), getString(R.string.location_access_needed));
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {


            if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
            ) {
                // Request the location permission if it is not granted
                ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            }

            // Location services are disabled, prompt/navigate the user to turn them on in Settings Screen UI
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }

    }

    // For Test purpose Using  Xperia Mall as Destination
    // Calculating Distance between  Xperia Mall and User's Device Location
    private void getCurrentLocationAndDistance() {


        if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            // Request the location permission if it is not granted
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        } else {

            if (locationManager == null) {
                locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            }

            // Get the device location
            locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {
                    // Do something with the location
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();

                    userLatitude = latitude;
                    userLongitude = longitude;


                    // Create a LatLng object for the user's location
                    LatLng userLatLng = new LatLng(userLatitude, userLongitude);

                    // Calculate the distance between the user's location and the marker using Location.distanceBetween()
                    float[] distance = new float[1];
                    Location.distanceBetween(userLatLng.latitude, userLatLng.longitude, lodhaMallLatLng.latitude, lodhaMallLatLng.longitude, distance);

                    // Convert the distance to kilometers
                    float distanceInKm = distance[0] / 1000;
                    String formattedDistanceInKm = String.format("%.2f", distanceInKm);

                    TextView txtDistance = getActivity().findViewById(R.id.txtDistance);
                    txtDistance.setText(formattedDistanceInKm);
                    System.out.println("Distance in Km:" + distanceInKm);
                    // Log the distance
                    Log.d("MapsActivity", "Distance from user to Lodha Xperia Mall: " + distanceInKm + " km");


                }

            }, null);
        }


    }
}