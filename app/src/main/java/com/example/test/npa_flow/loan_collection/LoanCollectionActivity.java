package com.example.test.npa_flow.loan_collection;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;

import com.example.test.R;
import com.example.test.databinding.ActivityLoanCollectionBinding;
import com.example.test.google_maps.GoogleMapsActivity;
import com.example.test.helper_classes.Global;
import com.example.test.helper_classes.NetworkUtilities;
import com.example.test.main_dashboard.MainActivity3API;
import com.example.test.npa_flow.VisitCompletionOfCustomerActivity;
import com.example.test.npa_flow.details_of_customer.DetailsOfCustomerActivity;
import com.example.test.npa_flow.details_of_customer.DetailsOfCustomerViewModel;
import com.example.test.npa_flow.loan_collection.adapter.LoanCollectionAdapter;
import com.example.test.npa_flow.status_of_customer.StatusOfCustomerActivity;
import com.example.test.npa_flow.status_of_customer.model.Activity;
import com.example.test.roomDB.dao.MPinDao;
import com.example.test.roomDB.dao.UserNameDao;
import com.example.test.roomDB.database.LeadListDB;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;

import java.util.ArrayList;
import java.util.Map;

public class LoanCollectionActivity extends AppCompatActivity {

    //This Activity Contains List of Members

    ActivityLoanCollectionBinding binding;
    View view;
    private LocationManager locationManager;
    LoanCollectionViewModel loanCollectionViewModel;
    private Location currentLocation;
   // public static int LocationRequestCode = 1 ; //used in LoanCollectionAdapterClass for if Location is not Enabled
   public static int LoanCollectionLayoutAdapterPosition = 0; // by default it is 0 , getting position from LoanCollectionAdapter

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_loan_collection);

        initializeFields();
        onClickListener();

        if (getIntent().hasExtra("isFromFull_Partial_AmountPaid_CompleteNoChange")) {
            int DPD_row_position = Integer.parseInt(Global.getStringFromSharedPref(this, "DPD_row_position"));
            call_LoanCollectionList_Api(DPD_row_position); // coming from Full/Partial Amt Paid Flow
            initObserver();
        }

        if (getIntent().hasExtra("NearByCustomerActivity")) {
            int DPD_row_position = Integer.parseInt(Global.getStringFromSharedPref(this, "DPD_row_position"));
            call_LoanCollectionList_Api(DPD_row_position); // coming from (Payment Mode)ScheduleVisitForCollection Flow
            initObserver();
        }


        if (NetworkUtilities.getConnectivityStatus(this)) {
            int DPD_row_position = getIntent().getIntExtra("DPD_row_position", 0);
            call_LoanCollectionList_Api(DPD_row_position); // using row position from DPD Activity and pass in LoanCollectionViewModel
            initObserver();
        } else {
            Global.showToast(this, getString(R.string.check_internet_connection));
        }

    }

    private void initializeFields() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_loan_collection);
        view = binding.getRoot();
        loanCollectionViewModel = new ViewModelProvider(this).get(LoanCollectionViewModel.class);
        binding.setViewModel(loanCollectionViewModel);

        //Whenever List is Loaded Remove BalanceInterestResult, Distance between User & Destination from SharedPreferences
        Global.removeStringInSharedPref(this, "BalanceInterestResult");

        Global.removeStringInSharedPref(this, "formattedDistanceInKm");

        //Whenever List is Loaded Make Notes Empty
        Global.saveStringInSharedPref(this, "notes", ""); //make Notes Empty After Complete

        setToolbarTitle();

        LoanCollectionAdapter.LoanCollectionAdapter_Distance ="0.0"; //initial value

        //UserID & BranchCode from RoomDB
        MPinDao mPinDao = LeadListDB.getInstance(this).mPinDao();
        MainActivity3API.UserID = mPinDao.getUserID();
        MainActivity3API.BranchCode = mPinDao.getBranchCode();

        GoogleMapsActivity.isSaveButtonClicked = false; // Only true if User clicks Yes OR Save button to save Location (Distance in Km)

    }

    private void setToolbarTitle() {
        int DPD_row_position = getIntent().getIntExtra("DPD_row_position", 0);

        if (DPD_row_position == 0) {
            binding.txtToolbarHeading.setText(getString(R.string.calling_1_30_dpd));
        }
        if (DPD_row_position == 1) {
            binding.txtToolbarHeading.setText(getString(R.string.calling_31_60_dpd));
        }
        if (DPD_row_position == 2) {
            binding.txtToolbarHeading.setText(getString(R.string.calling_60_90_dpd));
        }
        if (DPD_row_position == 3) {
            binding.txtToolbarHeading.setText(getString(R.string.calling_above_90));
        }


        if (getIntent().hasExtra("isFromCallsForTheDay")) {
            binding.txtToolbarHeading.setText(getResources().getString(R.string.calls_for_the_day));
        }

        //for UAT only . Later delete this
        if (getIntent().hasExtra("isFromNearByCustomerActivity")) {
            binding.txtToolbarHeading.setText(R.string.near_by_customers);
        }

    }

    private void call_LoanCollectionList_Api(int DPD_row_position) {
        loanCollectionViewModel.getLoanCollectionList_Data(DPD_row_position);

        DetailsOfCustomerViewModel detailsOfCustomerViewModel = new DetailsOfCustomerViewModel();
        detailsOfCustomerViewModel.dpd_row_position = DPD_row_position; // to call DetailsOfCustomer api according to position
    }

    private void setUpLoanCollectionList_RecyclerView() {

        loanCollectionViewModel.updateLoanCollectionData();
        RecyclerView recyclerView = binding.rvLoanCollection;
        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
            else if (!Global.isBackgroundLocationAccessEnabled(this)){
                //request BackGroundLocation Access
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, Global.REQUEST_BACKGROUND_LOCATION);
                System.out.println("Here Requesting BackGroundLocation Permission");
            }

            else
                currentLocation = getDeviceLocation();
            System.out.println("Here BackGroundLocation Permission Granted");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // go to previously stored adapter item position
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        // LoanCollectionLayoutAdapterPosition will be Reset in DPDActivity
        if(LoanCollectionLayoutAdapterPosition!=0){
            System.out.println("Here LoanCollectionLayoutAdapterPosition:"+LoanCollectionLayoutAdapterPosition);
            assert layoutManager != null;
            layoutManager.scrollToPosition(LoanCollectionLayoutAdapterPosition);

        }
        // Getting LoanCollection Adapter Position from SharedPreference
        else{
            try{
                String LoanCollectionLayoutAdapterPosition = Global.getStringFromSharedPref(this,"LoanCollectionLayoutAdapterPosition");

               if(null!=LoanCollectionLayoutAdapterPosition && !LoanCollectionLayoutAdapterPosition.isEmpty() ){
                   int LoanCollectionAdapterPosition = Integer.parseInt(LoanCollectionLayoutAdapterPosition);
                   assert layoutManager != null;
                   layoutManager.scrollToPosition(LoanCollectionAdapterPosition);
               }

            }
            catch(Exception e){
                e.printStackTrace();
            }

        }
        recyclerView.setAdapter(new LoanCollectionAdapter(loanCollectionViewModel.arrList_LoanCollectionList, currentLocation));



    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101) {
            currentLocation = getDeviceLocation();
        }

        //coming from LoanCollectionAdapter ivMap Click
        if (requestCode == Global.REQUEST_BACKGROUND_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Background location access permission granted
                DetailsOfCustomerActivity detailsOfCustomerActivity = new DetailsOfCustomerActivity();
                detailsOfCustomerActivity.navigateToGoogleMapsForNavigation();
            } else {
                // Background location access permission denied
                Global.isBackgroundLocationAccessEnabled(this); // request BackGroundLocation Again
            }
        }

    }

    private void initObserver() {

        binding.loadingProgressBar.setVisibility(View.VISIBLE);

        if (NetworkUtilities.getConnectivityStatus(this)) {

            loanCollectionViewModel.getMutLoanCollectionList_ResponseApi().observe(this, result -> {


                if (result != null) {

                    loanCollectionViewModel.arrList_LoanCollectionList.clear();
                    setUpLoanCollectionList_RecyclerView();
                    loanCollectionViewModel.arrList_LoanCollectionList.addAll(result);
                    binding.loadingProgressBar.setVisibility(View.GONE);

                }


            });

            //handle  error response
            loanCollectionViewModel.getMutErrorResponse().observe(this, error -> {

                if (error != null && !error.isEmpty()) {
                    Global.showSnackBar(view, error);
                    System.out.println("Here: " + error);
                } else {
                    Global.showSnackBar(view, getResources().getString(R.string.check_internet_connection));
                }
            });

        } else {
            Global.showToast(this, getString(R.string.check_internet_connection));
        }

    }


    private void onClickListener() {

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.ivHome.setOnClickListener(v -> {
            Intent i = new Intent(this, MainActivity3API.class);
            startActivity(i);
        });

        binding.ivSearchCancelIcon.setOnClickListener(v -> {
            binding.ivSearchIcon.setVisibility(View.VISIBLE);
            binding.txtToolbarHeading.setVisibility(View.VISIBLE);
            binding.edtSearchFromList.setVisibility(View.INVISIBLE);
            binding.ivSearchCancelIcon.setVisibility(View.GONE);
            binding.clChip.setVisibility(View.GONE);
            setUpLoanCollectionList_RecyclerView();

        });

        //for Chips(Name/PinCode/Status/Mobile)
        binding.chipName.setOnClickListener(v -> {
            binding.edtSearchFromList.setHint(getString(R.string.search_by));
            binding.chipName.setTextColor(ContextCompat.getColor(this, R.color.white));
            binding.chipName.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.textBlue)));
            binding.chipPinCode.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.skyBlue)));
            binding.chipStatus.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.skyBlue)));
            binding.chipPinCode.setTextColor(ContextCompat.getColor(this, R.color.black));
            binding.chipStatus.setTextColor(ContextCompat.getColor(this, R.color.black));
            binding.chipMobile.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.skyBlue)));
            binding.chipMobile.setTextColor(ContextCompat.getColor(this, R.color.black));
            binding.edtSearchFromList.setText("");
            binding.edtSearchFromList.setVisibility(View.VISIBLE);
            binding.txtToolbarHeading.setVisibility(View.INVISIBLE);
            binding.ivSearchIcon.performClick();
        });

        binding.chipPinCode.setOnClickListener(v -> {
            binding.edtSearchFromList.setHint(getString(R.string.search_by));
            binding.chipPinCode.setTextColor(ContextCompat.getColor(this, R.color.white));
            binding.chipPinCode.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.textBlue)));
            binding.chipName.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.skyBlue)));
            binding.chipStatus.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.skyBlue)));
            binding.chipName.setTextColor(ContextCompat.getColor(this, R.color.black));
            binding.chipStatus.setTextColor(ContextCompat.getColor(this, R.color.black));
            binding.chipMobile.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.skyBlue)));
            binding.chipMobile.setTextColor(ContextCompat.getColor(this, R.color.black));
            binding.edtSearchFromList.setText("");
            binding.edtSearchFromList.setVisibility(View.VISIBLE);
            binding.txtToolbarHeading.setVisibility(View.INVISIBLE);
            binding.ivSearchIcon.performClick();
        });

        binding.chipStatus.setOnClickListener(v -> {
            binding.edtSearchFromList.setHint(getString(R.string.search_by));
            binding.chipStatus.setTextColor(ContextCompat.getColor(this, R.color.white));
            binding.chipStatus.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.textBlue)));
            binding.chipPinCode.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.skyBlue)));
            binding.chipName.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.skyBlue)));
            binding.chipPinCode.setTextColor(ContextCompat.getColor(this, R.color.black));
            binding.chipName.setTextColor(ContextCompat.getColor(this, R.color.black));
            binding.chipMobile.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.skyBlue)));
            binding.chipMobile.setTextColor(ContextCompat.getColor(this, R.color.black));
            binding.edtSearchFromList.setText("");
            binding.edtSearchFromList.setVisibility(View.VISIBLE);
            binding.txtToolbarHeading.setVisibility(View.INVISIBLE);
            binding.ivSearchIcon.performClick();
        });

        binding.chipMobile.setOnClickListener(v -> {
            binding.edtSearchFromList.setHint(getString(R.string.search_by));
            binding.chipMobile.setTextColor(ContextCompat.getColor(this, R.color.white));
            binding.chipMobile.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.textBlue)));
            binding.chipPinCode.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.skyBlue)));
            binding.chipName.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.skyBlue)));
            binding.chipStatus.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.skyBlue)));
            binding.chipPinCode.setTextColor(ContextCompat.getColor(this, R.color.black));
            binding.chipName.setTextColor(ContextCompat.getColor(this, R.color.black));
            binding.chipStatus.setTextColor(ContextCompat.getColor(this, R.color.black));
            binding.edtSearchFromList.setText("");
            binding.edtSearchFromList.setVisibility(View.VISIBLE);
            binding.txtToolbarHeading.setVisibility(View.INVISIBLE);
            binding.ivSearchIcon.performClick();

        });

        binding.ivSearchIcon.setOnClickListener(v -> {

            binding.ivSearchCancelIcon.setVisibility(View.VISIBLE);
            binding.ivSearchIcon.setVisibility(View.INVISIBLE);
            binding.edtSearchFromList.setVisibility(View.VISIBLE);
            binding.clChip.setVisibility(View.VISIBLE);
            binding.txtToolbarHeading.setVisibility(View.INVISIBLE);
            binding.edtSearchFromList.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    // Perform the search operation
                    performSearch(s.toString());

                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });

        });
    }

    private void performSearch(String userSearchedText) {

        // Create a new ArrayList to hold the search results
        ArrayList<LoanCollectionListResponseModel> searchResults = new ArrayList<>();

        // Remove any spaces from the userSearchedText
        userSearchedText = userSearchedText.replace(" ", "");

        // Iterate through the original ArrayList(LoanCollectionResponseModel) and check if matcher with userSearchedText
        for (LoanCollectionListResponseModel item : loanCollectionViewModel.arrList_LoanCollectionList) {
            if ((null != item.getMemberName() && item.getMemberName().toLowerCase().replace(" ", "").contains(userSearchedText.toLowerCase()))
                    || (null != item.getPinCode() && item.getPinCode().toLowerCase().replace(" ", "").contains(userSearchedText.toLowerCase()))
                    || (null != item.getActionStatus() && item.getActionStatus().toLowerCase().replace(" ", "").contains(userSearchedText.toLowerCase()))
                    || (null != item.getMobileNumber() && item.getMobileNumber().replace(" ", "").contains(userSearchedText.toLowerCase()))
            ) {
                searchResults.add(item);
            }
        }

        if (searchResults.isEmpty()) {
            Global.showToast(this, getString(R.string.no_data_found));
        }

        // Use the searchResults ArrayList to update  UI
        // updateUI(searchResults);
        RecyclerView recyclerView = binding.rvLoanCollection;
        recyclerView.setAdapter(new LoanCollectionAdapter(searchResults, currentLocation));
    }


    @Override
    protected void onPause() {
        System.out.println("LoanCollectionActivity on Pause() called");
        int DPD_row_position = getIntent().getIntExtra("DPD_row_position", 0);
        call_LoanCollectionList_Api(DPD_row_position);
        initObserver();
        super.onPause();
    }

    @Override
    protected void onResume() {

        System.out.println("LoanCollectionActivity on Resume() called");

        // Get UserName , UserID , BranchCode

        MPinDao mPinDao = LeadListDB.getInstance(this).mPinDao();
        UserNameDao userNameDao = LeadListDB.getInstance(this).userNameDao();

        String userName = userNameDao.getUserNameUsingUserIDInUserNameRoomDB(mPinDao.getUserID());

        // Store UserName in SharedPreference and Use in StatusOfCustomerDetailsAdapter
        Global.saveStringInSharedPref(this,"userName",userName);

        MainActivity3API.UserID = mPinDao.getUserID();
        MainActivity3API.BranchCode = mPinDao.getBranchCode();

        System.out.println("Here LoanCollectionActivity onResume() UserID:"+MainActivity3API.UserID);
        System.out.println("Here LoanCollectionActivity onResume() BranchCode:"+MainActivity3API.BranchCode);


        if(StatusOfCustomerActivity.isFromStatusOfCustomerActivity){
            System.out.println("Here from LoanCollectionToStatusOfCustomerActivity: "+StatusOfCustomerActivity.isFromStatusOfCustomerActivity);
            //When User Searched String is not empty (When pressing back button in StatusOfCustomerActivity)
            if (!binding.edtSearchFromList.getText().toString().isEmpty()) {
                performSearch(binding.edtSearchFromList.getText().toString());
            }

            StatusOfCustomerActivity.isFromStatusOfCustomerActivity = false; // make false to reset the flow
            System.out.println("Here After Reset flow LoanCollectionToStatusOfCustomerActivity: "+StatusOfCustomerActivity.isFromStatusOfCustomerActivity);
        }

        //coming from GoogleMapsActivity
        else {

            initializeFields();
            onClickListener();
            int DPD_row_position = getIntent().getIntExtra("DPD_row_position", 0);
            call_LoanCollectionList_Api(DPD_row_position);
            initObserver();
            //to get LoanCollectionAdapterPosition
            setUpLoanCollectionList_RecyclerView(); // acts as refresh to show correct Attempt No. And Distance in Km
           System.out.println("Here LoanCollectionLayoutAdapterPosition from SharedPref:"+Global.getStringFromSharedPref(this,"LoanCollectionLayoutAdapterPosition"));

        }


        super.onResume();
    }

    @Override
    protected void onDestroy() {

        //Whenever List is Loaded Remove BalanceInterestResult,  Distance between User & Destination from SharedPreferences
        Global.removeStringInSharedPref(this, "BalanceInterestResult");
        Global.removeStringInSharedPref(this, "formattedDistanceInKm");
        super.onDestroy();
    }


    private Location getDeviceLocation() {
        Location currentLocation = null;
        System.out.println("Method called");
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
        }else {

            Double latitude;
            Double longitude;
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            boolean gpsProviderEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean networkProviderEnable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            GpsLocationListner gpsLocationListener = new GpsLocationListner();
            if (gpsProviderEnabled) {
                locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        5000,
                        0F,
                        gpsLocationListener
                );
            }
            if (networkProviderEnable) {
                locationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        5000,
                        0F,
                        gpsLocationListener
                );
            }

            Location lastKnownLocationByGps =
                    locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location lastKnownLocationByNetwork =
                    locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (lastKnownLocationByGps != null && lastKnownLocationByNetwork != null) {
                if (lastKnownLocationByGps.getAccuracy() > lastKnownLocationByNetwork.getAccuracy()) {
                    currentLocation = lastKnownLocationByGps;
                    latitude = currentLocation.getLatitude();
                    longitude = currentLocation.getLongitude();
                    // use latitude and longitude as per your need
                } else {
                    currentLocation = lastKnownLocationByNetwork;
                    latitude = currentLocation.getLatitude();
                    longitude = currentLocation.getLongitude();
                    // use latitude and longitude as per your need
                }
            } else if (lastKnownLocationByGps != null) {
                currentLocation = lastKnownLocationByGps;
                latitude = currentLocation.getLatitude();
                longitude = currentLocation.getLongitude();
            } else if (lastKnownLocationByNetwork != null) {
                currentLocation = lastKnownLocationByNetwork;
                latitude = currentLocation.getLatitude();
                longitude = currentLocation.getLongitude();
            }
        }
        return currentLocation;
    }

  /*  @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == LocationRequestCode){
            Global.showToast(this,"Yes");

            if(resultCode == RESULT_OK){
                Global.showToast(this,"OK");
            }
            else if (resultCode == RESULT_CANCELED){
                Global.showToast(this,"Cancelled");
            }
        }
    }*/
}