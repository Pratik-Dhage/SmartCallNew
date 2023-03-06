package com.example.test.location_gps.user_location;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import com.example.test.R;
import com.example.test.databinding.ActivityUserLocationBinding;
import com.example.test.helper_classes.Global;
import com.example.test.roomDB.dao.UserDao;
import com.example.test.roomDB.database.LeadListDB;
import com.example.test.roomDB.model.UserLocationRoomModel;

import java.util.function.Consumer;

public class UserLocationActivity extends AppCompatActivity implements LocationListener {

    ActivityUserLocationBinding binding;
    View view;
    LocationManager locationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        initializeFields();
        onClickListener();

    }

    private void check_If_LocationTurnedOn(){

        Global.showToast(this,"Location Access Needed");
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {


            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
            ) {
                // Request the location permission if it is not granted
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            }

            // Location services are disabled, prompt the user to turn them on
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }

    }

    private void initializeFields() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_location);
        view = binding.getRoot();

        //get the Location Permission first
        check_If_LocationTurnedOn();
    }

    private void onClickListener() {

        binding.btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               getCurrentLocation();

            }
        });


        binding.btnRetrieveFromRoomDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retrieveLatLongFromRoomDB();
            }
        });


    }

    private void getCurrentLocation(){


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
           && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            // Request the location permission if it is not granted
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        } else {

            if (locationManager == null) {
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            }

            // Get the device location
            locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {
                    // Do something with the location
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();

                 //   Global.showToast(UserLocationActivity.this,Double.toString(latitude)+Double.toString(longitude));

                    String Latitude = Double.toString(latitude);
                    String Longitude = Double.toString(longitude);

                    //Store in RoomDB
                   storeUserLatLongInRoomDB(Latitude,Longitude);

                    //Global.showToast(UserLocationActivity.this,Latitude+Longitude);
                    binding.locationProgressBar.setVisibility(View.VISIBLE);
                    binding.userLat.setVisibility(View.INVISIBLE);
                    binding.userLong.setVisibility(View.INVISIBLE);

                    //show LatLong after 4 secs
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            binding.locationProgressBar.setVisibility(View.INVISIBLE);
                            binding.userLat.setVisibility(View.VISIBLE);
                            binding.userLong.setVisibility(View.VISIBLE);

                            binding.userLat.setText("Latitude:"+Latitude);
                            binding.userLong.setText("Longitude:"+Longitude);
                        }
                    }, 4000); // 4000 milliseconds = 4 seconds delay



                }

            }, null);
        }

    }

    private void storeUserLatLongInRoomDB(String Latitude, String Longitude) {

        UserDao userDao = LeadListDB.getInstance(this).userDao();


        UserLocationRoomModel userLocationRoomModel =
                new UserLocationRoomModel("Pratik","D.","1234567890",Latitude,Longitude);
        userDao.insert(userLocationRoomModel);

        Global.showToast(this,"Saved in LocalDB Successfully");
    }

    private void retrieveLatLongFromRoomDB(){
        UserDao userDao = LeadListDB.getInstance(this).userDao();
             if(userDao.getUserLatitude("1234567890")!=null && userDao.getUserLongitude("1234567890")!=null)
             {
                 binding.userName.setText(userDao.getUserName("1234567890"));
                 binding.userPhone.setText(userDao.getUserPhone("Pratik"));
                binding.userAddress.setText(userDao.getUserLatitude("1234567890")+" "+userDao.getUserLongitude("1234567890"));
             }
         else{
             Global.showToast(this,"No Data Found");
             }

    }


    //if Permission is Granted
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==1){
            getCurrentLocation();
        }
        else{
            Global.showToast(this,"Permission Denied");
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        String Latitude = Double.toString(latitude);
        String Longitude = Double.toString(longitude);

        Global.showToast(this,Latitude+Longitude);

        // Do something with the latitude and longitude, for example display them in a TextView
           binding.userLat.setText(Latitude);
           binding.userLong.setText(Longitude);

        // Stop listening for location updates after getting the location once
        locationManager.removeUpdates(this);
    }

    @Override
    public void onProviderDisabled(String provider) {

        try{

           check_If_LocationTurnedOn();
        }
        catch(Exception e){

            Global.showToast(this,"Location Provider Error:"+provider);
            System.out.println("Here Location Provider Error: "+provider);
            System.out.println("Here Location Provider Exception: "+e);

        }

        System.out.println("Here Location Provider Error: "+provider);

    }

    @Override
    public void onProviderEnabled(String provider) {

        getCurrentLocation();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

        Global.showToast(this,"Network status Changed: "+provider+" status code: "+status);
        System.out.println("Here Network status Changed: "+provider+" status code: "+status);
    }
}





