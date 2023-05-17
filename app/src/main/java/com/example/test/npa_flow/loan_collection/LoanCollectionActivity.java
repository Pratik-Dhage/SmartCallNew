package com.example.test.npa_flow.loan_collection;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.example.test.R;
import com.example.test.databinding.ActivityLoanCollectionBinding;
import com.example.test.helper_classes.Global;
import com.example.test.helper_classes.NetworkUtilities;
import com.example.test.npa_flow.details_of_customer.DetailsOfCustomerViewModel;
import com.example.test.npa_flow.loan_collection.adapter.LoanCollectionAdapter;

public class LoanCollectionActivity extends AppCompatActivity {

    //This Activity Contains List of Members

    ActivityLoanCollectionBinding binding;
    View view;
    LoanCollectionViewModel loanCollectionViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_loan_collection);

        initializeFields();
        onClickListener();

        initObserver();
        if (NetworkUtilities.getConnectivityStatus(this)) {
            int DPD_row_position = getIntent().getIntExtra("DPD_row_position", 0);
            call_LoanCollectionList_Api(DPD_row_position); // using row position from DPD Activity and pass in LoanCollectionViewModel
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

        setToolbarTitle();

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
            binding.txtToolbarHeading.setText(getString(R.string.calling_above_60_dpd));
        }

        if (getIntent().hasExtra("isFromCallsForTheDay")) {
            binding.txtToolbarHeading.setText(getResources().getString(R.string.calls_for_the_day));
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
        recyclerView.setAdapter(new LoanCollectionAdapter(loanCollectionViewModel.arrList_LoanCollectionList));
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

       /*  //opens Google Maps
        binding.ivMap1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              //Below commented code is working in my device but not in other devices
               *//* String location = "Mumbai";
                Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + location);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                }*//*

                Intent googleMapsIntent = new Intent(LoanCollectionActivity.this, WebViewActivity.class);
                startActivity(googleMapsIntent);
            }
        });
*/
       /* //opens Google Maps
        binding.ivMap2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.ivMap1.performClick();
            }
        });*/


        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onResume() {

        //Whenever List is Loaded Remove BalanceInterestResult,  Distance between User & Destination from SharedPreferences
        Global.removeStringInSharedPref(this, "BalanceInterestResult");
        Global.removeStringInSharedPref(this, "formattedDistanceInKm");
        setUpLoanCollectionList_RecyclerView(); // acts as refresh to show correct Attempt No.
        super.onResume();
    }

    @Override
    protected void onDestroy() {

        //Whenever List is Loaded Remove BalanceInterestResult,  Distance between User & Destination from SharedPreferences
        Global.removeStringInSharedPref(this, "BalanceInterestResult");
        Global.removeStringInSharedPref(this, "formattedDistanceInKm");
        super.onDestroy();
    }
}