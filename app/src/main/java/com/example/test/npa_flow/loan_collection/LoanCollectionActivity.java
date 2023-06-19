package com.example.test.npa_flow.loan_collection;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.example.test.R;
import com.example.test.databinding.ActivityLoanCollectionBinding;
import com.example.test.helper_classes.Global;
import com.example.test.helper_classes.NetworkUtilities;
import com.example.test.npa_flow.VisitCompletionOfCustomerActivity;
import com.example.test.npa_flow.details_of_customer.DetailsOfCustomerViewModel;
import com.example.test.npa_flow.loan_collection.adapter.LoanCollectionAdapter;

import java.util.ArrayList;

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

        if(getIntent().hasExtra("isFromFull_Partial_AmountPaid_CompleteNoChange")){
            int DPD_row_position = Integer.parseInt(Global.getStringFromSharedPref(this,"DPD_row_position"));
            call_LoanCollectionList_Api(DPD_row_position); // coming from Full/Partial Amt Paid Flow
            initObserver();
        }

        if(getIntent().hasExtra("NearByCustomerActivity")){
            int DPD_row_position = Integer.parseInt(Global.getStringFromSharedPref(this,"DPD_row_position"));
            call_LoanCollectionList_Api(DPD_row_position); // coming from (Payment Mode)ScheduleVisitForCollection Flow
            initObserver();
        }

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

        //Whenever List is Loaded Make Notes Empty
        Global.saveStringInSharedPref(this,"notes",""); //make Notes Empty After Complete

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
            binding.txtToolbarHeading.setText(getString(R.string.calling_60_90_dpd));
        }
        if (DPD_row_position == 3) {
            binding.txtToolbarHeading.setText(getString(R.string.calling_above_90));
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

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.ivSearchCancelIcon.setOnClickListener(v->{
            binding.ivSearchIcon.setVisibility(View.VISIBLE);
            binding.txtToolbarHeading.setVisibility(View.VISIBLE);
            binding.edtSearchFromList.setVisibility(View.INVISIBLE);
            binding.ivSearchCancelIcon.setVisibility(View.GONE);
            binding.clChip.setVisibility(View.GONE);
            setUpLoanCollectionList_RecyclerView();

        });

        //for Chips(Name/PinCode/Status/Mobile)
        binding.chipName.setOnClickListener(v->{
            binding.edtSearchFromList.setHint(getString(R.string.search_by));
            binding.chipName.setTextColor(ContextCompat.getColor(this,R.color.white));
            binding.chipName.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(this,R.color.textBlue)));
            binding.chipPinCode.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(this,R.color.skyBlue)));
            binding.chipStatus.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(this,R.color.skyBlue)));
            binding.chipPinCode.setTextColor(ContextCompat.getColor(this,R.color.black));
            binding.chipStatus.setTextColor(ContextCompat.getColor(this,R.color.black));
            binding.chipMobile.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(this,R.color.skyBlue)));
            binding.chipMobile.setTextColor(ContextCompat.getColor(this,R.color.black));
            binding.edtSearchFromList.setText("");
            binding.edtSearchFromList.setVisibility(View.VISIBLE);
            binding.txtToolbarHeading.setVisibility(View.INVISIBLE);
            binding.ivSearchIcon.performClick();
        });

        binding.chipPinCode.setOnClickListener(v->{
            binding.edtSearchFromList.setHint(getString(R.string.search_by));
            binding.chipPinCode.setTextColor(ContextCompat.getColor(this,R.color.white));
            binding.chipPinCode.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(this,R.color.textBlue)));
            binding.chipName.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(this,R.color.skyBlue)));
            binding.chipStatus.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(this,R.color.skyBlue)));
            binding.chipName.setTextColor(ContextCompat.getColor(this,R.color.black));
            binding.chipStatus.setTextColor(ContextCompat.getColor(this,R.color.black));
            binding.chipMobile.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(this,R.color.skyBlue)));
            binding.chipMobile.setTextColor(ContextCompat.getColor(this,R.color.black));
            binding.edtSearchFromList.setText("");
            binding.edtSearchFromList.setVisibility(View.VISIBLE);
            binding.txtToolbarHeading.setVisibility(View.INVISIBLE);
            binding.ivSearchIcon.performClick();
        });

        binding.chipStatus.setOnClickListener(v->{
            binding.edtSearchFromList.setHint(getString(R.string.search_by));
            binding.chipStatus.setTextColor(ContextCompat.getColor(this,R.color.white));
            binding.chipStatus.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(this,R.color.textBlue)));
            binding.chipPinCode.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(this,R.color.skyBlue)));
            binding.chipName.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(this,R.color.skyBlue)));
            binding.chipPinCode.setTextColor(ContextCompat.getColor(this,R.color.black));
            binding.chipName.setTextColor(ContextCompat.getColor(this,R.color.black));
            binding.chipMobile.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(this,R.color.skyBlue)));
            binding.chipMobile.setTextColor(ContextCompat.getColor(this,R.color.black));
            binding.edtSearchFromList.setText("");
            binding.edtSearchFromList.setVisibility(View.VISIBLE);
            binding.txtToolbarHeading.setVisibility(View.INVISIBLE);
            binding.ivSearchIcon.performClick();
        });

        binding.chipMobile.setOnClickListener(v->{
            binding.edtSearchFromList.setHint(getString(R.string.search_by));
            binding.chipMobile.setTextColor(ContextCompat.getColor(this,R.color.white));
            binding.chipMobile.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(this,R.color.textBlue)));
            binding.chipPinCode.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(this,R.color.skyBlue)));
            binding.chipName.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(this,R.color.skyBlue)));
            binding.chipStatus.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(this,R.color.skyBlue)));
            binding.chipPinCode.setTextColor(ContextCompat.getColor(this,R.color.black));
            binding.chipName.setTextColor(ContextCompat.getColor(this,R.color.black));
            binding.chipStatus.setTextColor(ContextCompat.getColor(this,R.color.black));
            binding.edtSearchFromList.setText("");
            binding.edtSearchFromList.setVisibility(View.VISIBLE);
            binding.txtToolbarHeading.setVisibility(View.INVISIBLE);
            binding.ivSearchIcon.performClick();

        });

        binding.ivSearchIcon.setOnClickListener(v->{

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
            if (item.getMemberName().toLowerCase().replace(" ", "").contains(userSearchedText.toLowerCase())
                    || item.getPinCode().toLowerCase().replace(" ", "").contains(userSearchedText.toLowerCase())
                    || item.getActionStatus().toLowerCase().replace(" ", "").contains(userSearchedText.toLowerCase())
                    || item.getMobileNumber().replace(" ", "").contains(userSearchedText.toLowerCase())
            ) {
                searchResults.add(item);
            }
        }

        if(searchResults.isEmpty()){
            Global.showToast(this,getString(R.string.no_data_found));
        }

        // Use the searchResults ArrayList to update  UI
       // updateUI(searchResults);
        RecyclerView recyclerView = binding.rvLoanCollection;
        recyclerView.setAdapter(new LoanCollectionAdapter(searchResults));
    }





    @Override
    protected void onResume() {

        //Whenever List is Loaded Remove BalanceInterestResult,  Distance between User & Destination from SharedPreferences
        Global.removeStringInSharedPref(this, "BalanceInterestResult");
        Global.removeStringInSharedPref(this, "formattedDistanceInKm");
        setUpLoanCollectionList_RecyclerView(); // acts as refresh to show correct Attempt No.


        //When User Searched String is not empty (When pressing back button in StatusOfCustomerActivity)
       if(!binding.edtSearchFromList.getText().toString().isEmpty()){
           performSearch(binding.edtSearchFromList.getText().toString());
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
}