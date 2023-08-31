package com.example.test.google_maps;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.test.R;
import com.example.test.databinding.FragmentMapBinding;
import com.example.test.helper_classes.Global;
import com.example.test.npa_flow.details_of_customer.DetailsOfCustomerActivity;
import com.example.test.npa_flow.details_of_customer.adapter.DetailsOfCustomerAdapter;
import com.example.test.npa_flow.save_location.SaveLocationOfCustomerViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.android.PolyUtil;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.TravelMode;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapFragment extends Fragment {

    FragmentMapBinding binding;
    LocationManager locationManager;
    double userLatitude;
    double userLongitude;
   public static  double userMarkerLatitude; //for when user clicks on new locations
   public static double userMarkerLongitude; //for when user clicks on new locations
    String formattedDistanceInKm ="0";
    String dataSetId;
    double distanceInKm;

    DistanceViewModel distanceViewModel ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_map, container, false);
        check_If_LocationTurnedOn();
        distanceViewModel = new ViewModelProvider(getActivity()).get(DistanceViewModel.class);

        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapFragment);

        assert supportMapFragment != null;
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {

                try{
                    userLatitude = Global.getDeviceLocation(getActivity()).getLatitude();
                    userLongitude = Global.getDeviceLocation(getActivity()).getLongitude();
                   // googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID); //to set mapType
                }
                catch (Exception e){
                    System.out.println("Device Location Exception:"+e);
                    e.printStackTrace();
                }


                //coming from LoanCollectionAdapter red ivMap - Marker on User's Current Location
                if(GoogleMapsActivity.isFromLoanCollectionAdapter!=null){

                    try{
                       double userLatitude = Global.getDeviceLocation(getActivity()).getLatitude();
                       double  userLongitude = Global.getDeviceLocation(getActivity()).getLongitude();

                        MarkerOptions markerOptions = new MarkerOptions();
                        LatLng latLngFromLoanCollectionAdapter = new LatLng(userLatitude,userLongitude);
                        markerOptions.position(latLngFromLoanCollectionAdapter);
                        googleMap.addMarker(markerOptions);
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLngFromLoanCollectionAdapter, 15));

                        //if User does not Marks on GoogleMaps For New Marker
                        //then User's Current Device Location will become Member's Location

                        getActivity().findViewById(R.id.progressBarDistance).setVisibility(View.VISIBLE);
                       // getDistanceBetweenMarkerAndUser(latitude,longitude);
                        callGetDistanceUsingApi(userLatitude,userLongitude,userLatitude,userLongitude);
                        initObserverGetDistanceUsingApi();

                        // For Navigate Button in DetailsOFCustomerAdapter
                        Global.saveStringInSharedPref(getActivity().getApplicationContext(),"latitudeFromLoanCollectionAdapter",String.valueOf(userLatitude));
                        Global.saveStringInSharedPref(getActivity().getApplicationContext(),"longitudeFromLoanCollectionAdapter",String.valueOf(userLongitude));

                    }catch (Exception e){
                        System.out.println("LoanCollectionAdapter red ivMap Exception"+e);
                    }


                }



                //coming from DetailsOfCustomerAdapter Capture Button
                if(GoogleMapsActivity.isFromDetailsOfCustomerAdapter_CaptureButton!=null){

                    // by Default Capture button will  Red Mark User's Device Location

                    try{
                        double userLatitude = Global.getDeviceLocation(getActivity()).getLatitude();
                        double userLongitude = Global.getDeviceLocation(getActivity()).getLongitude();

                        LatLng userDeviceLatLng = new LatLng(userLatitude,userLongitude);
                        System.out.println("Capture Button userLatitude:"+userLatitude+" userLongitude"+userLongitude);

                        if(userDeviceLatLng!=null){

                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.position(userDeviceLatLng);
                            markerOptions.title("User's Location");

                            googleMap.addMarker(markerOptions);
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userDeviceLatLng, 15));

                            //if User does not Marks on GoogleMaps For New Marker
                            //then User's Current Device Location will become Member's Location

                            getActivity().findViewById(R.id.progressBarDistance).setVisibility(View.VISIBLE);
                           // getDistanceBetweenMarkerAndUser(userLatitude,userLongitude);
                            callGetDistanceUsingApi(userLatitude,userLongitude,userLatitude,userLongitude);
                            initObserverGetDistanceUsingApi();

                            // For Navigate Button in DetailsOFCustomerAdapter
                            Global.saveStringInSharedPref(getActivity().getApplicationContext(),"latitudeFromLoanCollectionAdapter",String.valueOf(userLatitude));
                            Global.saveStringInSharedPref(getActivity().getApplicationContext(),"longitudeFromLoanCollectionAdapter",String.valueOf(userLongitude));

                        }

                        else{
                            getCurrentLocation(googleMap); //User Marker
                        }
                    }catch (Exception e){
                        System.out.println("Capture Button Exception"+e);
                    }

                }

                //coming from VisitsForTheDayAdapter ivMap - Marker on User's Current Location
                if(GoogleMapsActivity.isFromVisitsForTheDayAdapter!=null){

                    try{

                        double userLatitude = Global.getDeviceLocation(getActivity()).getLatitude();
                        double userLongitude = Global.getDeviceLocation(getActivity()).getLongitude();

                        LatLng userDeviceLatLng = new LatLng(userLatitude,userLongitude);
                        System.out.println("VisitForTheDay ivMap userLatitude:"+userLatitude+" userLongitude"+userLongitude);

                        if(userDeviceLatLng!=null){

                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.position(userDeviceLatLng);
                            markerOptions.title("User's Location");

                            googleMap.addMarker(markerOptions);
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userDeviceLatLng, 15));

                            //if User does not Marks on GoogleMaps For New Marker
                            //then User's Current Device Location will become Member's Location

                            getActivity().findViewById(R.id.progressBarDistance).setVisibility(View.VISIBLE);
                           // getDistanceBetweenMarkerAndUser(userLatitude,userLongitude);
                            callGetDistanceUsingApi(userLatitude,userLongitude,userLatitude,userLongitude);
                            initObserverGetDistanceUsingApi();

                            // For Navigate Button in DetailsOFCustomerAdapter
                            Global.saveStringInSharedPref(getActivity().getApplicationContext(),"latitudeFromLoanCollectionAdapter",String.valueOf(userLatitude));
                            Global.saveStringInSharedPref(getActivity().getApplicationContext(),"longitudeFromLoanCollectionAdapter",String.valueOf(userLongitude));

                        }

                        else{
                            getCurrentLocation(googleMap); //User Marker
                        }


                    }catch (Exception e){
                        System.out.println("VisitsForTheDayAdapter ivMap Exception:"+e);
                    }


                }

                //coming from CallsForTheDayAdapter ivMap - Marker on User's Current Location
                if(GoogleMapsActivity.isFromCallsForTheDayAdapter!=null){

                    try{

                        double userLatitude = Global.getDeviceLocation(getActivity()).getLatitude();
                        double userLongitude = Global.getDeviceLocation(getActivity()).getLongitude();

                        LatLng userDeviceLatLng = new LatLng(userLatitude,userLongitude);
                        System.out.println("CallsForTheDayAdapter ivMap userLatitude:"+userLatitude+" userLongitude"+userLongitude);

                        if(userDeviceLatLng!=null){

                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.position(userDeviceLatLng);
                            markerOptions.title("User's Location");

                            googleMap.addMarker(markerOptions);
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userDeviceLatLng, 15));

                            //if User does not Marks on GoogleMaps For New Marker
                            //then User's Current Device Location will become Member's Location

                            getActivity().findViewById(R.id.progressBarDistance).setVisibility(View.VISIBLE);
                            // getDistanceBetweenMarkerAndUser(userLatitude,userLongitude);
                            callGetDistanceUsingApi(userLatitude,userLongitude,userLatitude,userLongitude);
                            initObserverGetDistanceUsingApi();

                            // For Navigate Button in DetailsOFCustomerAdapter
                            Global.saveStringInSharedPref(getActivity().getApplicationContext(),"latitudeFromLoanCollectionAdapter",String.valueOf(userLatitude));
                            Global.saveStringInSharedPref(getActivity().getApplicationContext(),"longitudeFromLoanCollectionAdapter",String.valueOf(userLongitude));

                        }

                        else{
                            getCurrentLocation(googleMap); //User Marker
                        }


                    }catch (Exception e){
                        System.out.println("CallsForTheDayAdapter ivMap Exception:"+e);
                    }
                }



                //When User Clicks on Maps for New Marker Location coming From Capture Button(DetailsOfCustomerAdapter) OR ivMap(LoanCollectionAdapter)

                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {

                        //for setting Message in showAlertDialogToSaveDistance() in GoogleMapsActivity
                        GoogleMapsActivity.isMapMarkerClicked = true; // if User clicks on Map for New Marker Position

                        getActivity().findViewById(R.id.progressBarDistance).setVisibility(View.VISIBLE); //Show Progress bar
                        getActivity().findViewById(R.id.txtDistance).setVisibility(View.INVISIBLE);

                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(latLng);
                        markerOptions.title("Here LatLong:" + latLng.latitude + "," + latLng.longitude);
                        googleMap.clear();
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20));
                        googleMap.addMarker(markerOptions);

                        // Assign New Marker position's Latitude and Longitude to calculate Distance of Marker And User's Device
                        userMarkerLatitude = latLng.latitude;
                        userMarkerLongitude = latLng.longitude;

                        //Save userMarker LatLong ,
                        // if User comes back to Details Page (DetailsOfCustomerAdapter) & clicks Navigate Button - Navigate To GoogleMaps for Direction
                        Global.saveStringInSharedPref(getActivity().getApplicationContext(),"latitudeFromLoanCollectionAdapter",String.valueOf(userMarkerLatitude));
                        Global.saveStringInSharedPref(getActivity().getApplicationContext(),"longitudeFromLoanCollectionAdapter",String.valueOf(userMarkerLongitude));

                        System.out.println("Here Capture Button userMarkerLatitude:"+userMarkerLatitude+" &userMarkerLongitude:"+userMarkerLongitude);

                        //Calculate Distance between New Marker position and User's Device And UpdateLocation Api is called
                        // getDistanceBetweenMarkerAndUser(userMarkerLatitude,userMarkerLongitude);

                        userLatitude = Global.getDeviceLocation(getActivity()).getLatitude();
                        userLongitude = Global.getDeviceLocation(getActivity()).getLongitude();

                        callGetDistanceUsingApi(userLatitude,userLongitude,userMarkerLatitude,userMarkerLongitude);
                        initObserverGetDistanceUsingApi();


                        /*// Draw a polyline to show the direction from the User to the New  User Marker
                        LatLng origin = new LatLng(userLatitude, userLongitude);
                        LatLng destination = new LatLng( userMarkerLatitude,  userMarkerLongitude);
                        PolylineOptions polylineOptions = new PolylineOptions()
                                .add(origin, destination)
                                .color(Color.BLUE)
                                .width(5f);
                        googleMap.addPolyline(polylineOptions);*/

                    }
                });
            }
        });

         // getCurrentLocationAndDistance();
        return binding.getRoot();
    }


    private void check_If_LocationTurnedOn() {


       // Global.showToast(getContext(), getString(R.string.location_access_needed));
         locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) || !locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {


            if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
            ) {
                // Request the location permission if it is not granted
                ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            }

            // Location services are disabled, prompt/navigate the user to turn them on in Settings Screen UI
            Global.showToast(getActivity().getApplicationContext(), "Please Turn Location On");

           /* Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);*/
        }

    }

    //for Navigate Button & for New Marker Position
    private void getCurrentLocation(GoogleMap googleMap){

        if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            // Request the location permission if it is not granted
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }

        //for Android Version 11 & Higher
        if(!Global.isBackgroundLocationAccessEnabled(getActivity())){
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, Global.REQUEST_BACKGROUND_LOCATION);
        }


        else {

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

                     // for User(Origin) Marker
                    LatLng userLatLng = new LatLng(userLatitude, userLongitude);
                    MarkerOptions userMarkerOptions = new MarkerOptions()
                            .position(userLatLng)
                            .title("User");
                    googleMap.addMarker(userMarkerOptions);

                }

            }, null);
        }

    }



    public  void getDistanceBetweenMarkerAndUser(double userMarkerLatitude, double userMarkerLongitude){


        if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            // Request the location permission if it is not granted
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }

        //for Android Version 11 & Higher
        if(!Global.isBackgroundLocationAccessEnabled(getActivity())){
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, Global.REQUEST_BACKGROUND_LOCATION);
        }

        else {

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

                    System.out.println("Here userLatitude & userLongitude: "+userLatitude+" "+userLongitude);
                    System.out.println("Here MarkerLatitude & MarkerLongitude: "+userMarkerLatitude+" "+userMarkerLongitude);

                    // Calculate the distance between the user's location and the marker using Location.distanceBetween() in Meters
                  /*  float[] distance = new float[1];
                    Location.distanceBetween(userLatLng.latitude, userLatLng.longitude, userMarkerLatitude, userMarkerLongitude, distance);

                    // Convert the distance from Meters To Kilometers
                    float distanceInKm = distance[0] / 1000;
                    String formattedDistanceInKm = String.format("%.2f", distanceInKm);
*/

                    //Calculate Distance between user's LatLong and userMarker's LatLong using Google's Directions Api
                    com.google.maps.model.LatLng originLatLng = new com.google.maps.model.LatLng(userLatitude, userLongitude);
                    com.google.maps.model.LatLng destinationLatLng = new com.google.maps.model.LatLng(userMarkerLatitude, userMarkerLongitude);

                    GeoApiContext geoApiContext = new GeoApiContext.Builder()
                            .apiKey("AIzaSyDsqxiDX4Pqfn7NUYzKFS2Nn2H4W5ywtaQ")
                            .build();

                    com.google.maps.DirectionsApi.newRequest(geoApiContext)
                            .origin(originLatLng)
                            .destination(destinationLatLng)
                            .mode(TravelMode.DRIVING) //Travel Mode is Driving (can use Walking)
                           // .avoid(DirectionsApi.RouteRestriction.HIGHWAYS)
                            .setCallback(new com.google.maps.PendingResult.Callback<com.google.maps.model.DirectionsResult>() {
                                @Override
                                public void onResult(com.google.maps.model.DirectionsResult result) {
                                    if (result.routes != null && result.routes.length > 0) {
                                        long distanceInMeters = result.routes[0].legs[0].distance.inMeters;

                                        //convert Meters to Kilometers
                                         distanceInKm = distanceInMeters / 1000.0;
                                       //  formattedDistanceInKm = String.format("%.2f", distanceInKm); // Format to two decimal places
                                        formattedDistanceInKm = String.valueOf(distanceInKm); // Without any decimal place restriction

                                        // Use runOnUiThread to update the UI safely
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {

                                                setNewDistanceAndUpdateLocation(formattedDistanceInKm);

                                            }
                                        });

                                        System.out.println("Distance: " + distanceInKm + " Km");

                                    } else {
                                        System.out.println("Error: No route found");
                                    }
                                }

                                @Override
                                public void onFailure(Throwable e) {
                                    System.out.println("Error calculating distance: " + e.getMessage());
                                }


                            });


                    /*// set New formattedDistanceInKm as TextView
                    try{
                        TextView txtDistance = getActivity().findViewById(R.id.txtDistance);

                        if(!String.valueOf(distanceInKm).isEmpty()){
                            getActivity().findViewById(R.id.progressBarDistance).setVisibility(View.GONE); //Dismiss ProgressBar
                            txtDistance.setVisibility(View.VISIBLE);
                            txtDistance.setText(String.valueOf(distanceInKm));
                        }

                        System.out.println("Marker Distance in Km:" + distanceInKm);
                    }
                    catch(Exception e){
                        System.out.println("Here Distance Exception:"+e);
                    }

                    //Save Distance in SharedPreference
                    Global.saveStringInSharedPref(getContext(),"formattedDistanceInKm",formattedDistanceInKm);

                    // Update location
                    try{
                         dataSetId = GoogleMapsActivity.dataSetId;
                        SaveLocationOfCustomerViewModel saveLocationOfCustomerViewModel = new ViewModelProvider(MapFragment.this).get(SaveLocationOfCustomerViewModel.class);

                        if(null != dataSetId){
                            System.out.println("Here dataSetId:"+dataSetId);
                            saveLocationOfCustomerViewModel.getSavedLocationOfCustomerData(dataSetId,String.valueOf(userMarkerLatitude),String.valueOf(userMarkerLongitude),String.valueOf(distanceInKm));
                            System.out.println("Here userMarkerLatitude:"+userMarkerLatitude+" "+"userMarkerLongitude:"+userMarkerLongitude);
                        }

                        //if dataSetId == null , in case User Again clicks on Capture Button
                        else {
                            // get from Shared Preference stored in LoanCollectionAdapter
                            dataSetId = Global.getStringFromSharedPref(getActivity(),"dataSetId");
                            System.out.println("Here dataSetId:"+dataSetId);
                            saveLocationOfCustomerViewModel.getSavedLocationOfCustomerData(dataSetId,String.valueOf(userMarkerLatitude),String.valueOf(userMarkerLongitude),String.valueOf(distanceInKm));
                            System.out.println("Here userMarkerLatitude:"+userMarkerLatitude+" "+"userMarkerLongitude:"+userMarkerLongitude);
                        }
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }

*/

                }

            }, null);
        }

    }

    public void callGetDistanceUsingApi(double oriLat, double oriLon ,double destLat , double destLon){
        distanceViewModel.getDistance(oriLat,oriLon,destLat,destLon);
    }

    public void initObserverGetDistanceUsingApi(){

        getActivity().findViewById(R.id.progressBarDistance).setVisibility(View.VISIBLE);
        distanceViewModel.getMutDistance_ResponseApi().observe(getActivity(),result->{

            if(null!=result){
               System.out.println("Distance Using Api : "+result);
               formattedDistanceInKm = String.valueOf(result);
               setNewDistanceAndUpdateLocation(formattedDistanceInKm);
            }


        });

        //handle error
        distanceViewModel.getMutErrorResponse().observe(this, error -> {

            if (error != null && !error.isEmpty()) {
                //  Global.showSnackBar(view, "DetailsOfCustomerActivity Exception: "+error);
                System.out.println("Here DetailsOfCustomerActivity Exception: " + error);

            } else {
                Global.showToast(getActivity(), getResources().getString(R.string.check_internet_connection));
            }
        });

    }

    public void setNewDistanceAndUpdateLocation(String formattedDistanceInKm){

        // set New formattedDistanceInKm as TextView
        try{
            TextView txtDistance = getActivity().findViewById(R.id.txtDistance);

            if(!formattedDistanceInKm.isEmpty()){
                getActivity().findViewById(R.id.progressBarDistance).setVisibility(View.GONE); //Dismiss ProgressBar
                txtDistance.setVisibility(View.VISIBLE);
                txtDistance.setText(String.valueOf(formattedDistanceInKm));
            }

            System.out.println("Marker Distance in Km:" + formattedDistanceInKm);

            //Save Distance in SharedPreference
            Global.saveStringInSharedPref(getContext(),"formattedDistanceInKm",formattedDistanceInKm);
        }
        catch(Exception e){
            System.out.println("Here Distance Exception:"+e);
        }


        // Update location
        try{
            dataSetId = GoogleMapsActivity.dataSetId;
            SaveLocationOfCustomerViewModel saveLocationOfCustomerViewModel = new ViewModelProvider(MapFragment.this).get(SaveLocationOfCustomerViewModel.class);

            if(null != dataSetId && GoogleMapsActivity.saveDistanceBoolean){
                System.out.println("Here dataSetId:"+dataSetId);
                saveLocationOfCustomerViewModel.getSavedLocationOfCustomerData(dataSetId,String.valueOf(userMarkerLatitude),String.valueOf(userMarkerLongitude),formattedDistanceInKm);
                System.out.println("Here userMarkerLatitude:"+userMarkerLatitude+" "+"userMarkerLongitude:"+userMarkerLongitude);
            }

            //if dataSetId goes null
            else if (GoogleMapsActivity.isSaveButtonClicked && dataSetId == null){
                String dataSetId = Global.getStringFromSharedPref(getActivity(),"dataSetId");
                System.out.println("Here dataSetId:"+dataSetId);
                saveLocationOfCustomerViewModel.getSavedLocationOfCustomerData(dataSetId,String.valueOf(userMarkerLatitude),String.valueOf(userMarkerLongitude),formattedDistanceInKm);
                System.out.println("Here userMarkerLatitude:"+userMarkerLatitude+" "+"userMarkerLongitude:"+userMarkerLongitude);

            }

        }
        catch(Exception e){
            e.printStackTrace();
        }



    }

    // For Location Permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        //requestCode used in getDistanceBetweenMarkerAndUser()
        if(requestCode == 1){

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permissions granted
                //getDistanceBetweenMarkerAndUser(userMarkerLatitude,userMarkerLongitude);
                callGetDistanceUsingApi(userLatitude,userLongitude,userLatitude,userLongitude);
                initObserverGetDistanceUsingApi();


            } else {
                // Permissions denied
                // Request the location permission if it is not granted
                ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            }

        }

        //for BackGroundLocation for Android 11 & Higher
        //coming from DetailsOfCustomerAdapter Navigation Button Click
        if (requestCode == Global.REQUEST_BACKGROUND_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Background location access permission granted
                DetailsOfCustomerActivity detailsOfCustomerActivity = new DetailsOfCustomerActivity();
                detailsOfCustomerActivity.navigateToGoogleMapsForNavigation();
            } else {
                // Background location access permission denied
                Global.isBackgroundLocationAccessEnabled(getActivity()); // request BackGroundLocation Again
            }
        }
    }
}