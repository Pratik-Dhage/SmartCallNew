package com.example.test.view_products.fragments;

import android.os.Bundle;

import androidx.appcompat.widget.TooltipCompat;
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

           //Age ToolTip
        String age_toolTip = getString(R.string.age_should_be_min_21_and_max_68);
        TooltipCompat.setTooltipText(binding.ivAgeNotify,age_toolTip);



           //Net Monthly Income ToolTip
       String net_monthly_income_toolTip = getString(R.string.income_after_deducting_taxes);
       TooltipCompat.setTooltipText(binding.ivMonthlyIncomeNotify,net_monthly_income_toolTip);
    }


}