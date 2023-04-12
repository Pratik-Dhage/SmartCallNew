package com.example.test.npa_flow.loan_collection;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.example.test.R;
import com.example.test.databinding.ActivityLoanCollectionBinding;
import com.example.test.helper_classes.Global;
import com.example.test.helper_classes.NetworkUtilities;
import com.example.test.npa_flow.DetailsOfCustomerActivity;
import com.example.test.npa_flow.WebViewActivity;
import com.example.test.npa_flow.loan_collection.adapter.LoanCollectionAdapter;

public class LoanCollectionActivity extends AppCompatActivity {

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
        if(NetworkUtilities.getConnectivityStatus(this)){
            call_LoanCollectionList_Api();
        }
        else{
            Global.showToast(this,getString(R.string.check_internet_connection));
        }

    }

    private void initializeFields() {
        binding = DataBindingUtil.setContentView(this,R.layout.activity_loan_collection);
        view = binding.getRoot();
        loanCollectionViewModel = new ViewModelProvider(this).get(LoanCollectionViewModel.class);
        binding.setViewModel(loanCollectionViewModel);

        if(getIntent().hasExtra("isFromCallsForTheDay")){
            binding.labelLoanCollection.setText(getResources().getString(R.string.calls_for_the_day));
        }

    }

    private void call_LoanCollectionList_Api() {
        loanCollectionViewModel.getLoanCollectionList_Data();
    }

    private void setUpLoanCollectionList_RecyclerView(){

        loanCollectionViewModel.updateLoanCollectionData();
        RecyclerView recyclerView = binding.rvLoanCollection;
        recyclerView.setAdapter(new LoanCollectionAdapter(loanCollectionViewModel.arrList_LoanCollectionList));
    }

    private void initObserver(){

        binding.loadingProgressBar.setVisibility(View.VISIBLE);
        loanCollectionViewModel.getMutLoanCollectionList_ResponseApi().observe(this,result->{

            if(NetworkUtilities.getConnectivityStatus(this)){

                if(result!=null){

                    loanCollectionViewModel.arrList_LoanCollectionList.clear();
                     setUpLoanCollectionList_RecyclerView();
                     loanCollectionViewModel.arrList_LoanCollectionList.addAll(result);
                    binding.loadingProgressBar.setVisibility(View.GONE);

                }
            }
            else{
                Global.showToast(this,getString(R.string.check_internet_connection));
            }

        });

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


}