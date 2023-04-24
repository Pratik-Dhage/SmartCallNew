package com.example.test.fragments_activity.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.test.R;
import com.example.test.databinding.FragmentLoanCollectionBinding;
import com.example.test.fragments_activity.CustomerDetailsActivity;
import com.example.test.fragments_activity.fragments.adapter.LoanCollectionFragmentAdapter;
import com.example.test.helper_classes.Global;
import com.example.test.helper_classes.NetworkUtilities;
import com.example.test.npa_flow.NearByCustomersActivity;
import com.example.test.npa_flow.WebViewActivity;
import com.example.test.npa_flow.details_of_customer.DetailsOfCustomerViewModel;
import com.example.test.npa_flow.loan_collection.LoanCollectionViewModel;
import com.example.test.npa_flow.loan_collection.adapter.LoanCollectionAdapter;


public class LoanCollectionFragment extends Fragment {


    FragmentLoanCollectionBinding binding;
    LoanCollectionViewModel loanCollectionViewModel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.fragment_loan_collection, container, false);

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_loan_collection, container, false);
        loanCollectionViewModel = new ViewModelProvider(this).get(LoanCollectionViewModel.class);
        binding.setViewModel(loanCollectionViewModel);


        initObserver();
        if(NetworkUtilities.getConnectivityStatus(getContext())){
            int DPD_row_position = 0; //test purpose
            call_LoanCollectionList_Api(DPD_row_position); // using row position from DPD Activity and pass in LoanCollectionViewModel
        }
        else{
            Global.showToast(getContext(),getString(R.string.check_internet_connection));
        }

        onClickListener();
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }



    private void call_LoanCollectionList_Api(int DPD_row_position) {
        loanCollectionViewModel.getLoanCollectionList_Data(DPD_row_position);

        DetailsOfCustomerViewModel detailsOfCustomerViewModel = new DetailsOfCustomerViewModel();
        detailsOfCustomerViewModel.dpd_row_position = DPD_row_position; // to call DetailsOfCustomer api according to position
    }

    private void setUpLoanCollectionList_RecyclerView(){

        loanCollectionViewModel.updateLoanCollectionFragmentData();
        RecyclerView recyclerView = binding.rvLoanCollection;
        recyclerView.setAdapter(new LoanCollectionFragmentAdapter(loanCollectionViewModel.arrList_LoanCollectionList));
    }

    private void initObserver(){

        binding.loadingProgressBar.setVisibility(View.VISIBLE);

        if(NetworkUtilities.getConnectivityStatus(getContext())){

            loanCollectionViewModel.getMutLoanCollectionList_ResponseApi().observe(getViewLifecycleOwner(),result->{



                if(result!=null){

                    loanCollectionViewModel.arrList_LoanCollectionList.clear();
                    setUpLoanCollectionList_RecyclerView();
                    loanCollectionViewModel.arrList_LoanCollectionList.addAll(result);
                    binding.loadingProgressBar.setVisibility(View.GONE);

                }


            });

            //handle  error response
            loanCollectionViewModel.getMutErrorResponse().observe(getViewLifecycleOwner(), error -> {

                if (error != null && !error.isEmpty()) {
                    Global.showToast(getContext(), error);
                    System.out.println("Here: " + error);
                } else {
                    Global.showToast(getContext(), getResources().getString(R.string.check_internet_connection));
                }
            });

        }
        else{
            Global.showToast(getContext(),getString(R.string.check_internet_connection));
        }

    }

    private void onClickListener() {

        // on click each item of customer
        binding.rvLoanCollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), CustomerDetailsActivity.class);
                startActivity(i);
            }
        });

    }


}