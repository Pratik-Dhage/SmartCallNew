package com.example.test.google_maps;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.test.R;
import com.example.test.databinding.ActivityGoogleMapsBinding;
import com.example.test.main_dashboard.MainActivity3API;

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
}