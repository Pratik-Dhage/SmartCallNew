package com.example.test.location_gps.customer_location;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
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
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;

import com.example.test.R;
import com.example.test.databinding.ActivityCustomerLocationBinding;
import com.example.test.helper_classes.Global;
import com.example.test.helper_classes.NetworkUtilities;
import com.example.test.roomDB.dao.CustomerDao;
import com.example.test.roomDB.database.LeadListDB;
import com.example.test.roomDB.model.CustomerLocationRoomModel;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class CustomerLocationActivity extends AppCompatActivity {

    ActivityCustomerLocationBinding binding;
    View view ;
    LocationManager locationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_location);

        if(NetworkUtilities.getConnectivityStatus(this))
        {initializeFields();
            onClickListener();}
        else{
            Global.showToast(this,"No Internet Connection");
        }
}



    private void initializeFields() {

        binding = DataBindingUtil.setContentView(this, R.layout.activity_customer_location);
        view = binding.getRoot();

        //get the Location Permission first
        check_If_LocationTurnedOn();
    }

    private void check_If_LocationTurnedOn() {

        Global.showToast(this, "Location Access Needed");
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {


            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
            ) {
                // Request the location permission if it is not granted
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            }

            // Location services are disabled, prompt the user to turn them on
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }

    }

    private void onClickListener() {

    binding.btnGetCustomerAddress.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if(validation()){
                getCurrentLocation();
            }


        }
    });

    binding.btnRetrieveCustomerData.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            retrieveCustomerData();
        }
    });

    }

    private void retrieveCustomerData(){
        CustomerDao customerDao = LeadListDB.getInstance(CustomerLocationActivity.this).customerDao();

        if(customerDao.getCustomerAddress(binding.edtCustomerPhoneNumber.getText().toString())!=null) {
            binding.txtCustomerName.setText(customerDao.getCustomerName(binding.edtCustomerPhoneNumber.getText().toString()));
            binding.txtCustomerAddressRetrieve.setText(customerDao.getCustomerAddress(binding.edtCustomerPhoneNumber.getText().toString()));
            binding.txtCustomerPhone.setText(customerDao.getCustomerPhone(binding.edtCustomerName.getText().toString()));
        }
        else{
            Global.showToast(this, "No Data Found");
        }
    }


    private void getCurrentLocation() {


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            // Request the location permission if it is not granted
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
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


                    //for getting complete address using latitude and longitude
                    // getCompleteAddress(latitude,longitude);

                    //Store in RoomDB
                    storeCustomerDataInRoomDB(getCompleteAddress(latitude, longitude));


                    //Global.showToast(UserLocationActivity.this,Latitude+Longitude);
                    binding.locationProgressBar.setVisibility(View.VISIBLE);

                    //show LatLong after 4 secs
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            binding.locationProgressBar.setVisibility(View.INVISIBLE);

                        }
                    }, 2000); // 4000 milliseconds = 4 seconds delay


                }

            }, null);
        }

    }

    private void storeCustomerDataInRoomDB(String address){

        CustomerDao customerDao = LeadListDB.getInstance(this).customerDao();

        String firstName = binding.edtCustomerName.getText().toString();
        String phoneNumber = binding.edtCustomerPhoneNumber.getText().toString();

        CustomerLocationRoomModel customerLocationRoomModel = new CustomerLocationRoomModel(firstName,"",phoneNumber,address);

        customerDao.insert(customerLocationRoomModel);

        Global.showToast(this, "Saved in LocalDB Successfully");
    }

    private boolean validation() {
        if(binding.edtCustomerName.getText().toString().isEmpty()){
            Global.showToast(this,getResources().getString(R.string.customer_name_cannot_be_empty));
            return false;
        }

        if(binding.edtCustomerPhoneNumber.getText().toString().isEmpty()){
            Global.showToast(this,getResources().getString(R.string.customer_phone_cannot_be_empty));
            return false;
        }


        return true;
    }

    private String getCompleteAddress(Double latitude, Double longitude) {

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        Address address = null;

        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && addresses.size() > 0) {
                address = addresses.get(0);

                   binding.txtCustomerAddress.setText(address.getAddressLine(0));
                System.out.println("Here Address: " + address.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert address != null;
        return address.getAddressLine(0);
    }


}