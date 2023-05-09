package com.example.test.view_products.fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.test.R;
import com.example.test.databinding.FragmentEligibilityBinding;

public class EligibilityFragment extends Fragment {

       FragmentEligibilityBinding binding;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_eligibility, container, false);
        onClickListener();
        
        return binding.getRoot();
    }

    private void onClickListener() {

           binding.radioButtonSalaried.setOnClickListener(v->{
               binding.radioButtonSalaried.setChecked(true);
               binding.radioButtonSelfEmployed.setChecked(false);
           });

           binding.radioButtonSelfEmployed.setOnClickListener(v->{
               binding.radioButtonSelfEmployed.setChecked(true);
               binding.radioButtonSalaried.setChecked(false);
           });

           //Age Notification
           binding.ivAgeNotify.setOnClickListener(v->{
               if(binding.txtAgeNotify.getVisibility()==View.INVISIBLE){
                   binding.txtAgeNotify.setVisibility(View.VISIBLE);
               }
               else{
                   binding.txtAgeNotify.setVisibility(View.INVISIBLE);
               }
           });

           //Net Monthly Income Notification
        binding.ivMonthlyIncomeNotify.setOnClickListener(v->{
            if(binding.txtMonthlyIncomeNotify.getVisibility()==View.INVISIBLE){
                binding.txtMonthlyIncomeNotify.setVisibility(View.VISIBLE);
            }
            else{
                binding.txtMonthlyIncomeNotify.setVisibility(View.INVISIBLE);
            }
        });
    }


}