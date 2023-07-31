package com.example.test.google_maps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.example.test.R;
import com.example.test.databinding.ActivityGoogleMapsBinding;
import com.example.test.helper_classes.Global;
import com.example.test.helper_classes.NetworkUtilities;
import com.example.test.main_dashboard.MainActivity3API;
import com.example.test.npa_flow.details_of_customer.DetailsOfCustomerActivity;
import com.example.test.npa_flow.loan_collection.LoanCollectionViewModel;
import com.example.test.npa_flow.save_location.SaveLocationOfCustomerViewModel;

public class GoogleMapsActivity extends AppCompatActivity {

    ActivityGoogleMapsBinding binding;
    View view;
   public static double latitude , longitude;
   public static String isFromLoanCollectionAdapter;
   public static double latitude_visitsForTheDay, longitude_visitsForTheDay;
   public static String isFromVisitsForTheDayAdapter;
    public static double latitude_callsForTheDay, longitude_callsForTheDay;
   public static String isFromCallsForTheDayAdapter;
   public static double latitudeFromDetailsOfCustomerAdapter , longitudeFromDetailsOfCustomerAdapter;
   public static String LatLongFromDetailsOfCustomerAdapter ;
   public static String dataSetId;
   public static String isFromDetailsOfCustomerAdapter_CaptureButton;

    public static boolean saveDistanceBoolean = false; //to Save Distance
    public static boolean isSaveButtonClicked = false;

    public static boolean isMapMarkerClicked = false; // if User clicks on Maps for New Marker Position ,used to setMessage in showAlertDialogToSaveDistance()

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_maps);


        initializeFields();
        onClickListener();
        setUpFragmentForGoogleMaps();
    }

    private void initializeFields() {
        binding = DataBindingUtil.setContentView(this,R.layout.activity_google_maps);
        view = binding.getRoot();

         dataSetId = getIntent().getStringExtra("dataSetId"); // for MapFragment (coming from ivMap(LoanCollectionAdapter) & Capture Button(DetailsOfCustomerAdapter))
          isSaveButtonClicked = false; // initially it will be false
          saveDistanceBoolean = false; // initially it will be false
          isMapMarkerClicked = false; // initially it will be false

        //From LoanCollectionAdapter
        latitude = getIntent().getDoubleExtra("latitude",0.0);
        longitude = getIntent().getDoubleExtra("longitude",0.0);
        isFromLoanCollectionAdapter = getIntent().getStringExtra("isFromLoanCollectionAdapter");

        // LatLong from loanCollectionAdapter Response Sent to DetailsOfCustomerAdapter for Navigate Button
        latitudeFromDetailsOfCustomerAdapter = getIntent().getDoubleExtra("latitudeFromDetailsOfCustomerAdapter",0.0);
        longitudeFromDetailsOfCustomerAdapter = getIntent().getDoubleExtra("longitudeFromDetailsOfCustomerAdapter",0.0);
        LatLongFromDetailsOfCustomerAdapter = getIntent().getStringExtra("LatLongFromDetailsOfCustomerAdapter");

        //From DetailsOfCustomerAdapter Capture Button
        isFromDetailsOfCustomerAdapter_CaptureButton = getIntent().getStringExtra("isFromDetailsOfCustomerAdapter_CaptureButton");

        //FromVisitsForTheDayAdapter
       latitude_visitsForTheDay = getIntent().getDoubleExtra("latitude_visitsForTheDay",0.0);
        longitude_visitsForTheDay = getIntent().getDoubleExtra("longitude_visitsForTheDay",0.0);
        isFromVisitsForTheDayAdapter = getIntent().getStringExtra("isFromVisitsForTheDayAdapter");

        //FromCallsForTheDayAdapter
        latitude_callsForTheDay = getIntent().getDoubleExtra("latitude_callsForTheDay",0.0);
        longitude_callsForTheDay = getIntent().getDoubleExtra("longitude_callsForTheDay",0.0);
        isFromCallsForTheDayAdapter = getIntent().getStringExtra("isFromCallsForTheDayAdapter");
    }

    private void setUpFragmentForGoogleMaps(){

        Fragment fragment = new MapFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.googleMapsFrameLayout,fragment).commit();

    }

    private void onClickListener() {

        binding.ivBack.setOnClickListener(v -> onBackPressed());

        binding.ivHome.setOnClickListener(v->{
            startActivity(new Intent(this, MainActivity3API.class));
        });


        binding.btnSaveDistance.setOnClickListener(v->{

            //Save Location of Customer API
            try{
                showAlertDialogToSaveDistance();
            }
            catch (Exception e){
                System.out.println("Here Save Location Exception:"+e);
            }

        });
    }

    // For Location Permission (For Versions - till Android 10 & Android 11 & above )
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        //requestCode used in getDistanceBetweenMarkerAndUser()
        if(requestCode == 1){

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permissions granted
                MapFragment mapFragment = new MapFragment();
                mapFragment.getDistanceBetweenMarkerAndUser(MapFragment.userMarkerLatitude,MapFragment.userMarkerLongitude);

            } else {
                // Permissions denied
                // Request the location permission if it is not granted
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
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
                Global.isBackgroundLocationAccessEnabled(this); // request BackGroundLocation Again
            }
        }
    }




    private void showAlertDialogToSaveDistance() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Action");
        if(GoogleMapsActivity.isMapMarkerClicked){
            builder.setMessage("Save Marked Location?");
        }
        else {
            builder.setMessage("Save Current Location?");
        }


  // Yes Button Click
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               saveDistanceBoolean = true;
               isSaveButtonClicked = true;
             // binding.btnSaveDistance.performClick();

                if(NetworkUtilities.getConnectivityStatus(GoogleMapsActivity.this) && saveDistanceBoolean && !binding.txtDistance.getText().toString().isEmpty() && isSaveButtonClicked){
                    SaveLocationOfCustomerViewModel saveLocationOfCustomerViewModel = new ViewModelProvider(GoogleMapsActivity.this).get(SaveLocationOfCustomerViewModel.class);

                    String savedDistance = Global.getStringFromSharedPref(GoogleMapsActivity.this, "formattedDistanceInKm");
                    dataSetId = getIntent().getStringExtra("dataSetId");

                    //get from SHaredPreference stored in LoanCollectionAdapter
                    if(null == dataSetId){
                        dataSetId = Global.getStringFromSharedPref(GoogleMapsActivity.this,"dataSetId");
                    }


                    if(savedDistance!=null && saveDistanceBoolean){

                        //coming from LoanCollectionAdapter on red ivMap clicking
                        //coming from VisitForTheDayAdapter on red ivMap clicking
                        if(getIntent().hasExtra("isFromLoanCollectionAdapter_ivMap") || getIntent().hasExtra("isFromVisitsForTheDayAdapter")){
                            System.out.println("Here from LoanCollectionAdapter on red ivMap clicking dataSetId:"+dataSetId);
                            System.out.println("Here from VisitForTheDayAdapter on red ivMap clicking dataSetId:"+dataSetId);
                            saveLocationOfCustomerViewModel.getSavedLocationOfCustomerData(dataSetId,String.valueOf(MapFragment.userMarkerLatitude),String.valueOf(MapFragment.userMarkerLongitude),savedDistance);
                        }

                        // Coming From DetailsOfCustomerAdapter Capture button clicking
                        else if (getIntent().hasExtra(isFromDetailsOfCustomerAdapter_CaptureButton)){
                            System.out.println("Here from DetailsOfCustomerAdapter Capture button clicking dataSetId:"+dataSetId);
                            saveLocationOfCustomerViewModel.getSavedLocationOfCustomerData(dataSetId,String.valueOf(MapFragment.userMarkerLatitude),String.valueOf(MapFragment.userMarkerLongitude),savedDistance);
                        }


                        System.out.println("Here savedDistance: "+savedDistance);
                        initObserverSavedLocationOfCustomer(GoogleMapsActivity.this);

                    }
                    else if(savedDistance==null){
                        Global.showSnackBar(view,getResources().getString(R.string.something_went_wrong));
                    }

                }

            }
        });

        // No Button Click
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveDistanceBoolean = false;
                isSaveButtonClicked =false;
             performBackPressedAction();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onBackPressed() {

          //if Save Button not clicked after getting distance
        if(!binding.txtDistance.getText().toString().isEmpty() && !isSaveButtonClicked){
            showAlertDialogToSaveDistance();
        }


           //if Save Button is clicked
    /*  else  if(saveDistanceBoolean && !binding.txtDistance.getText().toString().isEmpty() && isSaveButtonClicked){

            System.out.println("Here onBackPressed() GoogleMapsActivity");
            if(getIntent().hasExtra("isFromLoanCollectionAdapter_ivMap") || getIntent().hasExtra("isFromVisitsForTheDayAdapter") ||

                    getIntent().hasExtra("LatLongFromDetailsOfCustomerAdapter")){

                //Save Location of Customer API
                if(NetworkUtilities.getConnectivityStatus(this) && saveDistanceBoolean){
                    SaveLocationOfCustomerViewModel saveLocationOfCustomerViewModel = new ViewModelProvider(this).get(SaveLocationOfCustomerViewModel.class);

                    String savedDistance = Global.getStringFromSharedPref(this, "formattedDistanceInKm");
                    dataSetId = getIntent().getStringExtra("dataSetId");

                    //get from SHaredPreference stored in LoanCollectionAdapter
                    if(null == dataSetId){
                        dataSetId = Global.getStringFromSharedPref(this,"dataSetId");
                    }


                if(savedDistance!=null && saveDistanceBoolean){

                        //coming from LoanCollectionAdapter on red ivMap clicking
                        //coming from VisitForTheDayAdapter on red ivMap clicking
                        if(getIntent().hasExtra("isFromLoanCollectionAdapter_ivMap") || getIntent().hasExtra("isFromVisitsForTheDayAdapter")){
                            System.out.println("Here from LoanCollectionAdapter on red ivMap clicking dataSetId:"+dataSetId);
                            System.out.println("Here from VisitForTheDayAdapter on red ivMap clicking dataSetId:"+dataSetId);
                            saveLocationOfCustomerViewModel.getSavedLocationOfCustomerData(dataSetId,String.valueOf(MapFragment.userMarkerLatitude),String.valueOf(MapFragment.userMarkerLongitude),savedDistance);
                        }

                        // Coming From DetailsOfCustomerAdapter Capture button clicking
                        else if (getIntent().hasExtra(isFromDetailsOfCustomerAdapter_CaptureButton)){
                            System.out.println("Here from DetailsOfCustomerAdapter Capture button clicking dataSetId:"+dataSetId);
                            saveLocationOfCustomerViewModel.getSavedLocationOfCustomerData(dataSetId,String.valueOf(MapFragment.userMarkerLatitude),String.valueOf(MapFragment.userMarkerLongitude),savedDistance);
                        }


                        System.out.println("Here savedDistance: "+savedDistance);
                        initObserverSavedLocationOfCustomer(this);

                       *//* new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                performBackPressedAction();
                            }
                        }, 1000);*//*
                    }
                else if(savedDistance==null){
                    Global.showSnackBar(view,getResources().getString(R.string.something_went_wrong));
                }

                }
            }



        }

        else{
          performBackPressedAction();
        }
*/

    }

    private void performBackPressedAction(){
        super.onBackPressed();
    }


    public void initObserverSavedLocationOfCustomer(Context context){
        SaveLocationOfCustomerViewModel saveLocationOfCustomerViewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(SaveLocationOfCustomerViewModel.class);

        saveLocationOfCustomerViewModel.getMutSaveLocationOfCustomerResponseApi().observe((LifecycleOwner) context, result->{
            if(result!=null){

                //  Global.showToast(context,result);
                System.out.println("Here SavedDistanceOfCustomerResponse: "+result);
                Global.showSnackBar(view,result);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        performBackPressedAction();
                    }
                }, 2000);
            }
        });
    }

    @Override
    protected void onResume() {
        initializeFields();
        onClickListener();
        setUpFragmentForGoogleMaps();
        super.onResume();
    }
}