package com.example.test.fragments_activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;

import com.example.test.R;
import com.example.test.databinding.ActivityBalanceInterestCalculationBinding;

import java.util.Locale;

public class BalanceInterestCalculationActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {


ActivityBalanceInterestCalculationBinding binding;
View view ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance_interest_calculation);

        initializeFields();
        setRupeeSeekBarSlider();
        onClickListener();
    }

    private void initializeFields() {
        binding = DataBindingUtil.setContentView(this,R.layout.activity_balance_interest_calculation);
        view = binding.getRoot();
    }

    private void onClickListener() {

   binding.btnCalculate.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View v) {
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
           binding.edtBalanceDays.setText("");
           binding.balanceDaysSeekBarSlider.setProgress(0);
       }
   });

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

           /* if(progress == 0 ){
                binding.edtBalanceDays.setText("0");
            }*/

                int balanceDaysValue = progress + 1;
                String formattedValue2 = String.format(Locale.getDefault(), "%d", balanceDaysValue);
                binding.edtBalanceDays.setText(formattedValue2);

        }


        if(fromUser && seekBar == binding.interestSeekBarSlider){

            int interestValue = progress+1;
            String formattedValue3 = String.format(Locale.getDefault(), "%d", interestValue);
            binding.edtInterestRate.setText(formattedValue3);

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