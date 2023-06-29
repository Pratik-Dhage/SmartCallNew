package com.example.test.google_maps;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.example.test.R;
import com.example.test.databinding.ActivityGoogleMapsBinding;
import com.example.test.helper_classes.Global;
import com.example.test.helper_classes.NetworkUtilities;
import com.example.test.main_dashboard.MainActivity3API;
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

        //From LoanCollectionAdapter
        latitude = getIntent().getDoubleExtra("latitude",0.0);
        longitude = getIntent().getDoubleExtra("longitude",0.0);
        isFromLoanCollectionAdapter = getIntent().getStringExtra("isFromLoanCollectionAdapter");

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
    }

    @Override
    public void onBackPressed() {

        System.out.println("Here onBackPressed() GoogleMapsActivity");
        if(getIntent().hasExtra("isFromLoanCollectionAdapter_ivMap")){

            //Save Location of Customer API
            if(NetworkUtilities.getConnectivityStatus(this)){
                SaveLocationOfCustomerViewModel saveLocationOfCustomerViewModel = new ViewModelProvider(this).get(SaveLocationOfCustomerViewModel.class);

                String savedDistance = Global.getStringFromSharedPref(this, "formattedDistanceInKm");
                String dataSetId = getIntent().getStringExtra("dataSetId");
                if(savedDistance!=null){
                    saveLocationOfCustomerViewModel.getSavedLocationOfCustomerData(dataSetId,String.valueOf(MapFragment.userMarkerLatitude),String.valueOf(MapFragment.userMarkerLongitude),savedDistance);
                    System.out.println("Here savedDistance: "+savedDistance);
                    initObserverSavedLocationOfCustomer(this);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            performBackPressedAction();
                        }
                    }, 1000);
                }

            }
        }

        else {
            super.onBackPressed();
        }

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
            }
        });
    }

}