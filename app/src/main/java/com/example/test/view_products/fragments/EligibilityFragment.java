package com.example.test.view_products.fragments;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.TooltipCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.test.R;
import com.example.test.databinding.FragmentEligibilityBinding;
import com.example.test.helper_classes.Global;

public class EligibilityFragment extends Fragment {

    FragmentEligibilityBinding binding;
    Button activityOkButton;
    String selectedValueOfTenureSpinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_eligibility, container, false);
        onClickListener();

        activityOkButton = getActivity().findViewById(R.id.btnOK); // In Eligibility Fragment , initially OK button will not be visible
        activityOkButton.setVisibility(View.INVISIBLE);

        return binding.getRoot();
    }


    private void onClickListener() {

        binding.radioButtonSalaried.setOnClickListener(v -> {
            binding.radioButtonSalaried.setChecked(true);
            binding.radioButtonSelfEmployed.setChecked(false);
        });

        binding.radioButtonSelfEmployed.setOnClickListener(v -> {
            binding.radioButtonSelfEmployed.setChecked(true);
            binding.radioButtonSalaried.setChecked(false);
        });

        //Age ToolTip
        String age_toolTip = getString(R.string.age_should_be_min_21_and_max_68);
        TooltipCompat.setTooltipText(binding.ivAgeNotify, age_toolTip);


        //Net Monthly Income ToolTip
        String net_monthly_income_toolTip = getString(R.string.income_after_deducting_taxes);
        TooltipCompat.setTooltipText(binding.ivMonthlyIncomeNotify, net_monthly_income_toolTip);


        //for Tenure
        binding.spinnerTenureMonths.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                 selectedValueOfTenureSpinner = adapterView.getItemAtPosition(i).toString();
               // binding.edtTenure.setText(selectedValue);
                //User will enter no. of Months / Years for Tenure
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // do nothing
            }
        });


        // Check Eligibility
        binding.btnCheckEligibility.setOnClickListener(v -> {
            try {
                String age = binding.edtAge.getText().toString().trim();
                String required_loan_amt = binding.edtRequiredLoanAmount.getText().toString().trim();
                String net_monthly_income = binding.edtMonthlyIncome.getText().toString().trim();
                String other_emi = binding.edtOtherEmi.getText().toString().trim();
                String tenure = binding.edtTenure.getText().toString().trim();
                String rate_of_interest = binding.edtRateOfInterest.getText().toString().trim();

                if (age.isEmpty() || other_emi.isEmpty() || net_monthly_income.isEmpty()) {
                    Global.showSnackBar(binding.getRoot(), getString(R.string.age_monthly_income_other_emi_cannot_be_empty));
                    return;
                }

                double otherEmi = Double.parseDouble(other_emi);
                double monthlyIncome = Double.parseDouble(net_monthly_income);
                double emiRatio = otherEmi / monthlyIncome;

                if (Integer.parseInt(age) < 21 || Integer.parseInt(age) > 68) {
                    Global.showSnackBar(binding.getRoot(), " Age must be min.21 and max.68 ");
                    return;
                }

                if (emiRatio < 0.35) {
                    // Lead is eligible
                   // Global.showSnackBar(binding.getRoot(), getString(R.string.lead_is_eligible));

                    // for Custom Dialog Eligibility
                    View customDialogEligibility = LayoutInflater.from(getContext()).inflate(R.layout.custom_dialog_eligibility, null);
                    Button customButtonOK = customDialogEligibility.findViewById(R.id.btnOK);
                    Button customButtonCalculateEmi = customDialogEligibility.findViewById(R.id.btnCalculateEmi);
                    TextView txtLeadEligibilityStatus = customDialogEligibility.findViewById(R.id.txtLeadEligibilityStatus);
                    TextView txtEmiValue = customDialogEligibility.findViewById(R.id.txtEmiValue);

                    txtLeadEligibilityStatus.setText(getString(R.string.lead_is_eligible));

                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setView(customDialogEligibility);
                    final AlertDialog dialog = builder.create();
                    dialog.show();

                    customButtonOK.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            activityOkButton.setVisibility(View.VISIBLE); //Now Visible OK button of Activity
                            dialog.dismiss();
                        }
                    });

                    customButtonCalculateEmi.setOnClickListener(v1->{

                        try {
                            String required_loan_amount = binding.edtRequiredLoanAmount.getText().toString().trim();
                            String tenor = binding.edtTenure.getText().toString().trim();
                            String interest_rate = binding.edtRateOfInterest.getText().toString().trim();

                            if (!required_loan_amount.isEmpty() && !tenor.isEmpty() && !interest_rate.isEmpty()) {
                                double principle = Double.parseDouble(required_loan_amount);
                                double interest_rate_yearly = Double.parseDouble(interest_rate);
                                double interest_rate_monthly = interest_rate_yearly / 12 / 100; // convert to monthly interest rate
                                double tenor_months = 0;

                                if (selectedValueOfTenureSpinner.contains("Months")) {
                                    tenor_months = Double.parseDouble(tenor);
                                } else if (selectedValueOfTenureSpinner.contains("Years")) {
                                    tenor_months = Double.parseDouble(tenor) * 12; // convert years to months
                                }

                                double calculatedEmi = calculateInstallment(principle, tenor_months, interest_rate_monthly);
                                String formattedCalculatedEmi = String.format("%.2f", calculatedEmi);
                                txtEmiValue.setText(formattedCalculatedEmi);

                            } else {
                                txtLeadEligibilityStatus.setText(getString(R.string.pls_fill_required_fields));
                            }
                        } catch (Exception e) {
                            System.out.println("EMI_Exception:"+e);
                            txtLeadEligibilityStatus.setText(getString(R.string.pls_fill_required_fields));
                        }




                    });

                } else {
                    // Lead is not eligible
                   // Global.showSnackBar(binding.getRoot(), getString(R.string.lead_is_not_eligible));

                    // for Custom Dialog Eligibility
                    View customDialogEligibility = LayoutInflater.from(getContext()).inflate(R.layout.custom_dialog_eligibility, null);
                    Button customButtonOK = customDialogEligibility.findViewById(R.id.btnOK);
                    Button customButtonCalculateEmi = customDialogEligibility.findViewById(R.id.btnCalculateEmi);
                    TextView txtLeadEligibilityStatus = customDialogEligibility.findViewById(R.id.txtLeadEligibilityStatus);
                    TextView txtEmiValue = customDialogEligibility.findViewById(R.id.txtEmiValue);
                    ImageView ivRupeeIcon = customDialogEligibility.findViewById(R.id.ivRupeeIcon);


                    txtLeadEligibilityStatus.setText(getString(R.string.lead_is_not_eligible));
                    customButtonCalculateEmi.setVisibility(View.INVISIBLE);
                    txtEmiValue.setVisibility(View.INVISIBLE);
                    ivRupeeIcon.setVisibility(View.INVISIBLE);

                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setView(customDialogEligibility);
                    final AlertDialog dialog = builder.create();
                    dialog.show();

                    customButtonOK.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            activityOkButton.setVisibility(View.VISIBLE); //Now Visible OK button of Activity
                            dialog.dismiss();
                        }
                    });


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

    // For Calculating EMI / Installment
    // principle (loan amount), tenor (in months),  interest (in months)
    public static double calculateInstallment(double principle,double tenor,double interest){

        double interestValue = interest/(100*12);

        //double valToPow = tenor*-1;
        double valToPow = - (tenor * 12);

        double powerVal = Math.pow((1+interestValue), valToPow);

        double denominator = (1-powerVal);

        double installment = principle*(interestValue/denominator);

        return installment;

    }


}