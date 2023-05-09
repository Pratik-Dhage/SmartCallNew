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
import com.example.test.helper_classes.Global;

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

        binding.btnCheckEligibility.setOnClickListener(v->{
            try {
                String age = binding.edtAge.getText().toString().trim();
                String required_loan_amt = binding.edtRequiredLoanAmount.getText().toString().trim();
                String net_monthly_income = binding.edtMonthlyIncome.getText().toString().trim();
                String other_emi = binding.edtOtherEmi.getText().toString().trim();
                String tenure = binding.edtTenure.getText().toString().trim();
                String rate_of_interest = binding.edtRateOfInterest.getText().toString().trim();

                if(age.isEmpty() || other_emi.isEmpty() || net_monthly_income.isEmpty()){
                    Global.showSnackBar(binding.getRoot(),getString(R.string.age_monthly_income_other_emi_cannot_be_empty));
                    return;
                }

                double otherEmi = Double.parseDouble(other_emi);
                double monthlyIncome = Double.parseDouble(net_monthly_income);
                double emiRatio = otherEmi / monthlyIncome;

                if(Integer.parseInt(age)<21 || Integer.parseInt(age)>68){
                    Global.showSnackBar(binding.getRoot()," Age must be min.21 and max.68 ");
                    return;
                }

                if (emiRatio < 0.35) {
                    // Lead is eligible
                    Global.showSnackBar(binding.getRoot(),getString(R.string.lead_is_eligible));
                } else {
                    // Lead is not eligible
                    Global.showSnackBar(binding.getRoot(),getString(R.string.lead_is_not_eligible));
                }
            } catch (NumberFormatException e) {
                // Handle the case where the input is not a valid number
                e.printStackTrace();
                Global.showSnackBar(binding.getRoot(), "Invalid input");
            } catch (NullPointerException e) {
                // Handle the case where the input field is null
                e.printStackTrace();
                Global.showSnackBar(binding.getRoot(), "Input field is null");
            }
        });

    }



}