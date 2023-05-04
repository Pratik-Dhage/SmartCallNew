package com.example.test.npa_flow;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.test.R;
import com.example.test.databinding.ActivityWebViewBinding;
import com.example.test.helper_classes.Global;
import com.example.test.helper_classes.NetworkUtilities;
import com.example.test.roomDB.dao.CustomerDao;
import com.example.test.roomDB.database.LeadListDB;
import com.example.test.roomDB.model.CustomerLocationRoomModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class WebViewActivity extends AppCompatActivity implements OnMapReadyCallback {

    ActivityWebViewBinding binding;
    View view;
    GoogleMap googleMap;

    private static final int PLACE_PICKER_REQUEST = 1;
    private LatLng mPinnedLocation;

    private FusedLocationProviderClient fusedLocationClient;
    private Location currentLocation;
    String Latitude ;
    String Longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  setContentView(R.layout.activity_web_view);

        initializeFields();
        MapsInitializer.initialize(this);
        if (NetworkUtilities.getConnectivityStatus(this)) {

            setUpGoogleMaps();
        } else {
            Global.showToast(this, getResources().getString(R.string.check_internet_connection));
        }

        onClickListener();

    }

    private void initializeFields() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_web_view);
        view = binding.getRoot();
    }

    private void onClickListener() {
        binding.ivBack.setOnClickListener(v -> {
            onBackPressed();
        });
    }


    private void setUpGoogleMaps() {

        WebView webView = binding.webView;
        webView.getSettings().setJavaScriptEnabled(true); // enable JavaScript
        webView.setWebViewClient(new WebViewClient()); //  WebViewClient to handle page loading
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);


        if (getIntent().hasExtra("latitude") && getIntent().hasExtra("longitude")) {

            String latitude = getIntent().getStringExtra("latitude");
            String longitude = getIntent().getStringExtra("longitude");
            // String location = "Navi Mumbai";
            String location = latitude + "," + longitude;  // set LatLong as location
            String url = "https://www.google.com/maps?q=" + location;
            webView.loadUrl(url); // load the Google Maps URL with the location

        }


       /*else{
            String location = "Navi Mumbai";
           String url = "https://www.google.com/maps?q=" + location;
           webView.loadUrl(url); // load the Google Maps URL with the location

       }*/

        else {

            // Initialize the FusedLocationProviderClient
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

            // Check for location permission
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                // If location permission is granted, request the current location
                requestCurrentLocation();
            } else {
                // If location permission is not granted, request it
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        Integer.parseInt(Manifest.permission.ACCESS_COARSE_LOCATION));
            }


            //go to Google Maps App and Current Device Location will appear in search bar

            String latLong = Latitude + "," + Longitude;
            Uri gmmIntentUri = Uri.parse("geo:" + latLong);

            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);

            webView.getSettings().setJavaScriptEnabled(true);
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    // Inject JavaScript code to listen for map click events and send the latitude and longitude to the Android code
                    view.loadUrl("javascript:" +
                            "var map = document.getElementById('searchbox_map');" +
                            "google.maps.event.addListener(map, 'click', function(event) {" +
                            "  window.Android.onMapClick(event.latLng.lat(), event.latLng.lng());" +
                            "});" +
                            // Add code to reverse geocode the clicked location and send the address back to the Android code
                            "var geocoder = new google.maps.Geocoder();" +
                            "geocoder.geocode({'location': event.latLng}, function(results, status) {" +
                            "  if (status === 'OK') {" +
                            "    window.Android.onMapAddress(results[0].formatted_address);" +
                            "  }" +
                            "});");
                }
            });

            MapJavascriptInterface mapInterface = new MapJavascriptInterface();
            webView.addJavascriptInterface(mapInterface, "Android");
            webView.loadUrl("https://www.google.com/maps");

        }

        //Test Purpose
     /*   else{


           webView.loadUrl("https://www.google.com/maps");
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setDomStorageEnabled(true);
            webView.setWebChromeClient(new WebChromeClient());
            webView.setWebViewClient(new WebViewClient() {



               @Override
               public void onPageFinished(WebView view, String url) {

                   //for API key
                   view.loadUrl("javascript:var script = document.createElement('script');script.src = 'https://maps.googleapis.com/maps/api/js?key=AIzaSyDymn9B6l2_MnHSr6dI_jjZaXEeFWPn5vU&callback=initMap';document.head.appendChild(script);");



                   // Inject JavaScript code to listen for map click events and send the latitude and longitude to the Android code
                   view.loadUrl("javascript:" +
                           "var map = document.getElementById('searchbox_map');" +
                           "google.maps.event.addListener(map, 'click', function(event) {" +
                           "  window.Android.onMapClick(event.latLng.lat(), event.latLng.lng());" +
                           "});" +
                           // Add code to reverse geocode the clicked location and send the address back to the Android code
                           "var geocoder = new google.maps.Geocoder();" +
                           "geocoder.geocode({'location': event.latLng}, function(results, status) {" +
                           "  if (status === 'OK') {" +
                           "    window.Android.onMapAddress(results[0].formatted_address);" +
                           "  }" +
                           "});");

                   // Enable clicking on Google Maps by injecting JavaScript code
                   view.loadUrl("javascript:(function() { " +
                           "var target = document.getElementsByClassName('widget-scene-canvas')[0];" +
                           "target.addEventListener('touchstart', function(event) { " +
                           "    var touch = event.touches[0]; " +
                           "    var mouseEvent = new MouseEvent('mousedown', { " +
                           "        clientX: touch.clientX, " +
                           "        clientY: touch.clientY " +
                           "    }); " +
                           "    target.dispatchEvent(mouseEvent); " +
                           "}, false);" +
                           "target.addEventListener('touchmove', function(event) { " +
                           "    event.preventDefault();" +
                           "    var touch = event.touches[0]; " +
                           "    var mouseEvent = new MouseEvent('mousemove', { " +
                           "        clientX: touch.clientX, " +
                           "        clientY: touch.clientY " +
                           "    }); " +
                           "    target.dispatchEvent(mouseEvent); " +
                           "}, false);" +
                           "target.addEventListener('touchend', function(event) { " +
                           "    var mouseEvent = new MouseEvent('mouseup', {}); " +
                           "    target.dispatchEvent(mouseEvent); " +
                           "}, false);" +
                           "})();");

               }
           });

           MapJavascriptInterface mapInterface = new MapJavascriptInterface();
           webView.addJavascriptInterface(mapInterface, "Android");





        }
*/
    }

    private void requestCurrentLocation() {
        // Use the FusedLocationProviderClient to request the current location
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Location services are disabled, prompt the user to turn them on
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // If the location is not null, save it to the currentLocation variable
                        if (location != null) {
                            currentLocation = location;
                            double latitude = currentLocation.getLatitude();
                            double longitude = currentLocation.getLongitude();

                            Latitude = String.valueOf(latitude);
                            Longitude = String.valueOf(longitude);
                        }
                    }
                });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        Global.showToast(this,"Map Ready");
// Define a member variable to store the current marker's position
        final LatLng[] currentMarkerPosition = new LatLng[1];

// Set up a marker click listener
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(Marker marker) {
                // Get the marker's position and save it to the member variable
                currentMarkerPosition[0] = marker.getPosition();

                // Return false to allow the default behavior (opening the marker info window)
                return false;
            }
        });

        String latLongString = String.format("%f,%f", currentMarkerPosition[0].latitude, currentMarkerPosition[0].longitude);

        //Store in RoomDB
        storeCustomerDataInRoomDB(getCompleteAddress(currentMarkerPosition[0].latitude, currentMarkerPosition[0].longitude));
        Global.showToast(WebViewActivity.this,getCompleteAddress(currentMarkerPosition[0].latitude, currentMarkerPosition[0].longitude));

        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setAllGesturesEnabled(true);


    }


    class MapJavascriptInterface  {
        @JavascriptInterface
        public void onMapClick(double latitude, double longitude) {
            // Save the latitude and longitude to a database or do something else with them
            Log.d("MapJavascriptInterface", "onMapClick: " + latitude + ", " + longitude);

            //Store in RoomDB
            storeCustomerDataInRoomDB(getCompleteAddress(latitude, longitude));
            Global.showToast(WebViewActivity.this,getCompleteAddress(latitude,longitude));

            //Retrieve Lat Long from RoomDB
          //  Global.showToast(WebViewActivity.this,retrieveCustomerData());
            Global.showToast(WebViewActivity.this,"Clicked");
        }



    }


    private String getCompleteAddress(Double latitude, Double longitude) {

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        Address address = null;

        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && addresses.size() > 0) {
                address = addresses.get(0);


                System.out.println("Here Address: " + address.toString());

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert address != null;
        return address.getAddressLine(0);
    }

    private void storeCustomerDataInRoomDB(String address){

        CustomerDao customerDao = LeadListDB.getInstance(this).customerDao();

        // later get firstName and phoneNumber value from Details Of Customer Adapter
        String firstName = "Pratik "; // for testing
        String phoneNumber = "0123456789"; //for testing

        CustomerLocationRoomModel customerLocationRoomModel = new CustomerLocationRoomModel(firstName,"",phoneNumber,address);

        customerDao.insert(customerLocationRoomModel);

        Global.showToast(this, "Saved in LocalDB Successfully");
    }


  private String retrieveCustomerData(){
      CustomerDao customerDao = LeadListDB.getInstance(this).customerDao();
     return customerDao.getCustomerAddress("0123456789"); // Later get phoneNumber from Details Of Customer Adapter

  }


}