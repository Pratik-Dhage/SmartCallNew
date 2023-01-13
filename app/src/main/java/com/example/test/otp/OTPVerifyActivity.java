package com.example.test.otp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test.R;
import com.example.test.databinding.ActivityOtpverifyBinding;
import com.example.test.helper_classes.Global;
import com.example.test.helper_classes.NetworkUtilities;
import com.example.test.login.LoginActivity;
import com.example.test.mPin.MPinActivity;
import com.example.test.register_password.RegisterPasswordActivity;

public class OTPVerifyActivity extends AppCompatActivity {

    ActivityOtpverifyBinding binding;
    View view;
    OTPVerifyViewModel otpVerifyViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //This activity is made to check OTP manually

        initializeFields();
        onClickListener();
        CustomTextWatcher();

    }

    private void initializeFields() {

        binding = DataBindingUtil.setContentView(this,R.layout.activity_otpverify);
        view = binding.getRoot();
        otpVerifyViewModel = binding.getViewModel();
    }

    private void onClickListener() {


            binding.btnVerifyOTP.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(NetworkUtilities.getConnectivityStatus(OTPVerifyActivity.this)){

                        if(validations()){

                                Intent mPinIntent = new Intent(OTPVerifyActivity.this, MPinActivity.class);
                                startActivity(mPinIntent);
                        }

                    }

                    else {
                        Global.showSnackBar(view,getResources().getString(R.string.check_internet_connection));
                    }




                }
            });


    }

    private boolean validations(){

             if(binding.edt4.getText().toString().isEmpty() ||
                binding.edt3.getText().toString().isEmpty() ||
                binding.edt2.getText().toString().isEmpty() ||
                binding.edt4.getText().toString().isEmpty())
             {
          binding.txtErrorOTP.setVisibility(View.VISIBLE);
          binding.txtOTPTimer.setVisibility(View.INVISIBLE);
          binding.labelResendOTP.setVisibility(View.INVISIBLE);

          binding.btnVerifyOTP.setBackgroundColor(getResources().getColor(R.color.borderColor));
           return false;
        }

        return true;
    }


    private void CustomTextWatcher(){

        binding.edt1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.toString().length()==1){
                    binding.txtErrorOTP.setVisibility(View.INVISIBLE);
                    binding.txtOTPTimer.setVisibility(View.VISIBLE);
                    binding.labelResendOTP.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(binding.edt4.getText().toString().isEmpty()){
                binding.btnVerifyOTP.setBackgroundColor(getResources().getColor(R.color.borderColor));
                }
                else{
                    binding.btnVerifyOTP.setBackgroundColor(getResources().getColor(R.color.textBlue));
                }
            }
        });



        binding.edt2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.toString().length()==1){
                    binding.txtErrorOTP.setVisibility(View.INVISIBLE);
                    binding.txtOTPTimer.setVisibility(View.VISIBLE);
                    binding.labelResendOTP.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(binding.edt4.getText().toString().isEmpty()){
                    binding.btnVerifyOTP.setBackgroundColor(getResources().getColor(R.color.borderColor));
                }
                else{
                    binding.btnVerifyOTP.setBackgroundColor(getResources().getColor(R.color.textBlue));
                }
            }
        });


        binding.edt3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.toString().length()==1){
                    binding.txtErrorOTP.setVisibility(View.INVISIBLE);
                    binding.txtOTPTimer.setVisibility(View.VISIBLE);
                    binding.labelResendOTP.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(binding.edt4.getText().toString().isEmpty()){
                    binding.btnVerifyOTP.setBackgroundColor(getResources().getColor(R.color.borderColor));
                }
                else{
                    binding.btnVerifyOTP.setBackgroundColor(getResources().getColor(R.color.textBlue));
                }
            }
        });


        //TextWatcher for otp verify button
        // Verify button will be Enabled only when edt4 is NOT EMPTY

        binding.edt4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.toString().length()==1){
                    binding.btnVerifyOTP.setBackgroundColor(getResources().getColor(R.color.textBlue));
                    binding.txtErrorOTP.setVisibility(View.INVISIBLE);
                    binding.txtOTPTimer.setVisibility(View.VISIBLE);
                    binding.labelResendOTP.setVisibility(View.VISIBLE);
                }
                else{
                    binding.btnVerifyOTP.setBackgroundColor(getResources().getColor(R.color.borderColor));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }


    @Override
    public void onBackPressed() {
       // super.onBackPressed();
    }
}