package com.example.test.npa_flow.nearby_customer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;

import com.example.test.R;
import com.example.test.databinding.ActivityNearByCustomerListBinding;
import com.example.test.databinding.ActivityNearByCustomersBinding;
import com.example.test.helper_classes.Global;
import com.example.test.helper_classes.NetworkUtilities;
import com.example.test.main_dashboard.MainActivity3API;

public class NearByCustomerListActivity extends AppCompatActivity {

    ActivityNearByCustomerListBinding binding;
    View view;
    NearByCustomerViewModel nearByCustomerViewModel;
    private  Location currentLocation ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_by_customer_list);

        initializeFields();

        if(NetworkUtilities.getConnectivityStatus(this)){
            callNearByCustomerApi();
            initObserver();
        }

        onClickListeners();

    }

    private void initializeFields(){
        binding = DataBindingUtil.setContentView(this,R.layout.activity_near_by_customer_list);
        view = binding.getRoot();
        nearByCustomerViewModel = new ViewModelProvider(this).get(NearByCustomerViewModel.class);
        currentLocation = Global.getDeviceLocation(this);
    }

    private void callNearByCustomerApi(){

        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            } else if (!Global.isBackgroundLocationAccessEnabled(this)) {
                //request BackGroundLocation Access
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, Global.REQUEST_BACKGROUND_LOCATION);
                System.out.println("Here Requesting BackGroundLocation Permission");
            }

            else if(Global.isLocationEnabled(this) && Global.isBackgroundLocationAccessEnabled(this)){

                double userLatitude =  Global.getDeviceLocation(this).getLatitude();
                double userLongitude = Global.getDeviceLocation(this).getLongitude();

                //Pass User's LatLong to Api call in Backend
                nearByCustomerViewModel.getNearByCustomerList_Data(userLatitude,userLongitude);
            }
        }
        catch (Exception e){
            System.out.println("NearByCustomerList LocationException:"+e);
        }

    }

    private void setUpNearByCustomerList_RecyclerView(){

        currentLocation = Global.getDeviceLocation(this);
        nearByCustomerViewModel.updateNearByCustomerData();
        RecyclerView recyclerView = binding.rvNearByCustomers;
        recyclerView.setAdapter(new NearByCustomerListAdapter(nearByCustomerViewModel.arrList_LoanCollectionList,currentLocation));

    }

    private void initObserver() {

        binding.loadingProgressBar.setVisibility(View.VISIBLE);

        if (NetworkUtilities.getConnectivityStatus(this)) {

            nearByCustomerViewModel.getMutLoanCollectionList_ResponseApi().observe(this, result -> {


                if (result != null) {

                    nearByCustomerViewModel.arrList_LoanCollectionList.clear();
                    setUpNearByCustomerList_RecyclerView();
                    nearByCustomerViewModel.arrList_LoanCollectionList.addAll(result);
                    binding.loadingProgressBar.setVisibility(View.GONE);

                }


            });

            //handle  error response
            nearByCustomerViewModel.getMutErrorResponse().observe(this, error -> {

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



    private void onClickListeners(){

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
    }
}