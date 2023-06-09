package com.example.test.schedule_flow.visits_for_the_day;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import com.example.test.R;
import com.example.test.databinding.ActivityVisitsForTheDayBinding;
import com.example.test.helper_classes.Global;
import com.example.test.helper_classes.NetworkUtilities;
import com.example.test.main_dashboard.MainActivity3API;
import com.example.test.npa_flow.details_of_customer.DetailsOfCustomerActivity;
import com.example.test.npa_flow.status_of_customer.adapter.StatusOfCustomerDetailsAdapter;
import com.example.test.roomDB.dao.MPinDao;
import com.example.test.roomDB.dao.UserNameDao;
import com.example.test.roomDB.database.LeadListDB;
import com.example.test.schedule_flow.visits_for_the_day.adapter.VisitsForTheDayAdapter;

public class VisitsForTheDayActivity extends AppCompatActivity {

    ActivityVisitsForTheDayBinding binding;
    View view;
    VisitsForTheDayViewModel visitsForTheDayViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visits_for_the_day);

        initializeFields();
        onClickListeners();

        if (NetworkUtilities.getConnectivityStatus(this)) {
          callVisitsForTheDayAPi();
          initObserver();

        } else {
            Global.showToast(this, getString(R.string.check_internet_connection));
        }
    }


    private void initializeFields() {
        binding = DataBindingUtil.setContentView(this,R.layout.activity_visits_for_the_day);
        view = binding.getRoot();
        visitsForTheDayViewModel = new ViewModelProvider(this).get(VisitsForTheDayViewModel.class);
        binding.setViewModel(visitsForTheDayViewModel);

        Global.saveStringInSharedPref(this,"notes",""); //make Notes Empty After Complete

        //Whenever List is Loaded remove previously stored formattedDistanceInKm
        Global.removeStringInSharedPref(this, "formattedDistanceInKm");

        //Get UserIdD & BranchCode
        MPinDao mPinDao = LeadListDB.getInstance(this).mPinDao();
        UserNameDao userNameDao = LeadListDB.getInstance(this).userNameDao();

        String userName = userNameDao.getUserNameUsingUserIDInUserNameRoomDB(mPinDao.getUserID());

        MainActivity3API.UserID = mPinDao.getUserID();
        MainActivity3API.BranchCode = mPinDao.getBranchCode();

        System.out.println("Here VisitsForTheDay initializeFields() UserID:"+MainActivity3API.UserID);
        System.out.println("Here VisitsForTheDay initializeFields() BranchCode:"+MainActivity3API.BranchCode);


    }

    private void callVisitsForTheDayAPi() {
        visitsForTheDayViewModel.getVisitsForTheDayData();
    }

    private void setUpVisitsForTheDayRecyclerViewData(){
        visitsForTheDayViewModel.updateVisitsForTheDayData();
        RecyclerView recyclerView = binding.rvVisitsForTheDay;
        recyclerView.setAdapter(new VisitsForTheDayAdapter(visitsForTheDayViewModel.arrListVisitsForTheDayData));

    }

    private void initObserver() {

        visitsForTheDayViewModel.getMutVisitsForTheDayResponseApi().observe(this,result->{

            if(NetworkUtilities.getConnectivityStatus(this)){

                if(result!=null){

                    visitsForTheDayViewModel.arrListVisitsForTheDayData.clear();
                    setUpVisitsForTheDayRecyclerViewData();
                    visitsForTheDayViewModel.arrListVisitsForTheDayData.addAll(result);


                    if(result.isEmpty()){
                        binding.txtNoDataForVisits.setVisibility(View.VISIBLE);
                    }

                }




                //handle  error response
                visitsForTheDayViewModel.getMutErrorResponse().observe(this, error -> {

                    if (error != null && !error.isEmpty()) {
                        Global.showSnackBar(view, error);
                        System.out.println("Here: " + error);
                    } else {
                        Global.showSnackBar(view, getResources().getString(R.string.check_internet_connection));
                    }
                });

            }

            else {
                Global.showSnackBar(view, getString(R.string.check_internet_connection));
            }

        });
    }


    private void onClickListeners() {

        binding.ivBack.setOnClickListener(v->{
            onBackPressed();
        });

        binding.ivHome.setOnClickListener(v->{
            startActivity(new Intent(this, MainActivity3API.class));
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        //coming from VisitsForTheDayAdapter ivMap Click
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

    @Override
    protected void onResume() {
        initializeFields();
        onClickListeners();

        if (NetworkUtilities.getConnectivityStatus(this)) {
            callVisitsForTheDayAPi();
            initObserver();

        } else {
            Global.showToast(this, getString(R.string.check_internet_connection));
        }

        super.onResume();
    }
}