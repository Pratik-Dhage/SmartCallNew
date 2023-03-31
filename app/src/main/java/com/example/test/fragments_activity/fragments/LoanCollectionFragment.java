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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.test.R;
import com.example.test.databinding.FragmentLoanCollectionBinding;
import com.example.test.fragments_activity.CustomerDetailsActivity;
import com.example.test.npa_flow.NearByCustomersActivity;
import com.example.test.npa_flow.WebViewActivity;


public class LoanCollectionFragment extends Fragment {


    FragmentLoanCollectionBinding binding;


    private void onClickListener() {

        // on click each item of customer
         binding.rootConstraintLayout.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent i = new Intent(getContext(), CustomerDetailsActivity.class);
                 startActivity(i);
             }
         });

        //opens Google Maps
        binding.ivMap1.setOnClickListener(v -> {

            //Below commented code is working in my device but not in Other devices
           /* String location = "Mumbai";
            Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + location);
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            if (mapIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivity(mapIntent);
            }*/

            Intent googleMapsIntent = new Intent(getContext(), WebViewActivity.class);
            startActivity(googleMapsIntent);
        });

        //opens Google Maps
        binding.ivMap2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.ivMap1.performClick();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.fragment_loan_collection, container, false);

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_loan_collection, container, false);
        onClickListener();
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }
}