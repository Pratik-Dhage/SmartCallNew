package com.example.test.fragments_activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;

import com.example.test.R;
import com.example.test.databinding.ActivityBalanceInterestCalculationBinding;
import com.example.test.helper_classes.Global;
import com.example.test.npa_flow.details_of_customer.DetailsOfCustomerActivity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Locale;

public class BalanceInterestCalculationActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {


ActivityBalanceInterestCalculationBinding binding;
View view ;

String TotalDue,InterestRate,BalanceDays; // for BalanceInterestCalculation

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance_interest_calculation);

        initializeFields();
        getTotalDue_InterestRateFromIntent();
        setRupeeSeekBarSlider();
        onClickListener();
    }

    private void initializeFields() {
        binding = DataBindingUtil.setContentView(this,R.layout.activity_balance_interest_calculation);
        view = binding.getRoot();
    }

    private void getTotalDue_InterestRateFromIntent(){

        if(getIntent().hasExtra("TotalDue")){
             TotalDue = getIntent().getStringExtra("TotalDue");
            binding.txtTotalDue.setText(TotalDue);
        }
        else{
            Global.showToast(this,"Total Due not found");
        }

        if(getIntent().hasExtra("InterestRate")){
             InterestRate = getIntent().getStringExtra("InterestRate");
            binding.txtInterestRate.setText(InterestRate);

        }

        binding.edtBalanceDays.setText("1");  // default balance days will be 1 (cannot be zero else calculation not possible)

    }

    private String calculateBalanceInterest(String TotalDue, String InterestRate, String BalanceDays){

        // Convert input values to BigDecimal
        BigDecimal totalDue = new BigDecimal(TotalDue);
        BigDecimal interestRate = new BigDecimal(InterestRate);

       // Convert balance days to int
        int balanceDays = Integer.parseInt(BalanceDays);

     // for result
        BigDecimal numerator = totalDue.multiply(BigDecimal.valueOf(balanceDays)).multiply(interestRate);
        BigDecimal denominator = BigDecimal.valueOf(36500);
        BigDecimal result = numerator.divide(denominator, 2, RoundingMode.HALF_UP);

        System.out.println(result); // Prints the calculated result with 2 decimal places

        Global.showToast(this,"Balance Interest:"+result);

        return result.toString();
    }

    private void onClickListener() {

        binding.ivBack.setOnClickListener(v->{
            onBackPressed();
        });

   binding.btnCalculate.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View v) {


           TotalDue = binding.txtTotalDue.getText().toString();
           InterestRate = binding.txtInterestRate.getText().toString();
           BalanceDays = binding.edtBalanceDays.getText().toString();

           onBackPressed();

       }
   });

   //clear data
   binding.ivRefreshClearData.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View v) {
           binding.edtLoanAmount.setText("");
           binding.rupeeSeekBarSlider.setProgress(0);
           binding.edtInterestRate.setText("");
           binding.interestSeekBarSlider.setProgress(0);
           binding.edtBalanceDays.setText("1");
           binding.balanceDaysSeekBarSlider.setProgress(0);
       }
   });

    }


    @Override
    public void onBackPressed() {

        String BalanceInterestResult = calculateBalanceInterest(TotalDue,InterestRate,BalanceDays);

        Intent intent = new Intent();
        intent.putExtra("BalanceInterestResult", BalanceInterestResult);
        setResult(RESULT_OK, intent);
        super.onBackPressed();

    }

    private void setRupeeSeekBarSlider(){

       // binding.rupeeSeekBarSlider.setMax(10);
        binding.rupeeSeekBarSlider.setOnSeekBarChangeListener(BalanceInterestCalculationActivity.this);
        binding.interestSeekBarSlider.setOnSeekBarChangeListener(BalanceInterestCalculationActivity.this);
        binding.balanceDaysSeekBarSlider.setOnSeekBarChangeListener(BalanceInterestCalculationActivity.this);

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        //for Loan Amount
        if(fromUser && seekBar == binding.rupeeSeekBarSlider){
            int value = progress * 5000;
            String formattedValue = String.format(Locale.getDefault(), "%d", value);
            //   binding.txtRupeeSeekSliderValue.setText(formattedValue);
            binding.edtLoanAmount.setText(formattedValue); // set value in Edit Text

        }

        //for Balance Days
        if(fromUser && seekBar == binding.balanceDaysSeekBarSlider ){

            if(progress == 0 ){
                binding.edtBalanceDays.setText("1");
            }

                int balanceDaysValue = progress + 1;
                String formattedValue2 = String.format(Locale.getDefault(), "%d", balanceDaysValue);
                binding.edtBalanceDays.setText(formattedValue2);

        }


        if(fromUser && seekBar == binding.interestSeekBarSlider){

            int interestValue = progress+1;
            String formattedValue3 = String.format(Locale.getDefault(), "%d", interestValue);
            binding.edtInterestRate.setText(formattedValue3);

            binding.txtInterestRate.setVisibility(View.INVISIBLE); // hide Interest Rate TextView when changed using seekBar
        }

       /* ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) binding.txtRupeeSeekSliderValue.getLayoutParams();
        params.topMargin = seekBar.getBottom() + getResources().getDimensionPixelSize(com.intuit.sdp.R.dimen._5sdp);
        binding.txtRupeeSeekSliderValue.setLayoutParams(params);*/

       /* int seekBarWidth = seekBar.getWidth() - seekBar.getPaddingLeft() - seekBar.getPaddingRight();
        int thumbOffset = seekBar.getThumbOffset();
        int seekBarValue = seekBar.getMax() * progress / 100;
        float x = (seekBarWidth * progress / 100) + seekBar.getPaddingLeft() - thumbOffset + (thumbOffset * 2 - binding.txtRupeeSeekSliderValue.getWidth()) / 2;
        binding.txtRupeeSeekSliderValue.setX(x);
        binding.txtRupeeSeekSliderValue.setText(String.valueOf(seekBarValue));*/

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}