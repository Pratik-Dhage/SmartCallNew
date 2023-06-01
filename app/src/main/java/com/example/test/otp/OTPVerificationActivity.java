package com.example.test.otp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.test.R;
import com.example.test.databinding.ActivityOtpverificationBinding;
import com.example.test.helper_classes.Global;
import com.example.test.helper_classes.NetworkUtilities;
import com.example.test.mPin.MPinActivity;
import com.example.test.register_password.RegisterPasswordActivity;

import in.aabhasjindal.otptextview.OTPListener;
import in.aabhasjindal.otptextview.OtpTextView;

public class OTPVerificationActivity extends AppCompatActivity {

    //OTP Validation Activity
    ActivityOtpverificationBinding binding;
    View view;
    OTPVerifyViewModel otpVerifyViewModel;
    public String userId;
    public int otpCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpverification);


        //This activity is made by externally adding an OTP Library
        initializeFields();
        onClickListeners();
        otpListener();
    }

    private void initializeFields() {

        binding = DataBindingUtil.setContentView(this,R.layout.activity_otpverification);
        view = binding.getRoot();
        otpVerifyViewModel = new ViewModelProvider(this).get(OTPVerifyViewModel.class);
        binding.setViewModel(otpVerifyViewModel);

    }

    private void callValidateOTP_Api(){

        userId = getIntent().getStringExtra("userId");
        otpCode = Integer.parseInt(binding.otpView.getOTP());

        otpVerifyViewModel.callValidateOTP_Api(userId,otpCode);
    }

    private void initObserver(){

        if(NetworkUtilities.getConnectivityStatus(this)){

            otpVerifyViewModel.getMutValidateOTP_ResponseApi().observe(this,result->{

                if(result!=null){

                    if(result.getUserId().toString().contentEquals(userId)){
                        String userNameFromOTPValidationResponse;
                        try{
                             userNameFromOTPValidationResponse = result.getUserName().toString();

                             if(result.getUserName()==null){
                                 Global.saveStringInSharedPref(this,"userNameFromOTPValidationResponse","userNameIsNull"); // Save UserName in SharedPreferences for storing mPin with userName in RoomDB
                             }
                             else{
                                 Global.saveStringInSharedPref(this,"userNameFromOTPValidationResponse",userNameFromOTPValidationResponse); // Save UserName in SharedPreferences for storing mPin with userName in RoomDB
                             }
                        }
                        catch(Exception e){
                            System.out.println("Here Error:"+e);
                        }


                        if(getIntent().hasExtra("isFromLoginWithOTPFragment_ResetMPin")){
                            Intent mpinIntent = new Intent(this,MPinActivity.class);
                            startActivity(mpinIntent);
                        }

                        else {
                            Intent registerIntent = new Intent(OTPVerificationActivity.this, RegisterPasswordActivity.class);
                            startActivity(registerIntent);
                        }
                    }


                }

            });

            //handle  error response
            otpVerifyViewModel.getMutErrorResponse().observe(this, error -> {

                if (error != null && !error.isEmpty()) {
                    Global.showSnackBar(view, error);
                    System.out.println("Here: " + error);
                } else {
                    Global.showSnackBar(view, getResources().getString(R.string.check_internet_connection));
                }
            });

        }

        else{
            Global.showSnackBar(view,getString(R.string.check_internet_connection));
        }
    }

    private void otpListener(){

        OtpTextView otpTextView = binding.otpView;
        otpTextView.setOtpListener(new OTPListener() {
            @Override
            public void onInteractionListener() {
                // This method will be called when any digit is entered or deleted.

                if (otpTextView.getOTP().length() == 4) {
                    // All four entries of the OTP have been written by the user.
                    binding.btnVerifyOTP.setBackgroundColor(ContextCompat.getColor(OTPVerificationActivity.this,R.color.textBlue));

                }
                else{
                    binding.btnVerifyOTP.setBackgroundColor(ContextCompat.getColor(OTPVerificationActivity.this,R.color.borderColor));
                }
            }

            @Override
            public void onOTPComplete(String otp) {
                binding.btnVerifyOTP.setBackgroundColor(ContextCompat.getColor(OTPVerificationActivity.this,R.color.textBlue));

            }
        });
    }


    private void onClickListeners() {


        binding.btnVerifyOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(binding.otpView.getOTP().isEmpty()){
                    binding.txtErrorOTP.setVisibility(View.VISIBLE);
                    binding.btnVerifyOTP.setBackgroundColor(ContextCompat.getColor(OTPVerificationActivity.this,R.color.borderColor));
                }

                if(!binding.otpView.getOTP().isEmpty()){
                    binding.txtErrorOTP.setVisibility(View.INVISIBLE);
                    binding.btnVerifyOTP.setBackgroundColor(ContextCompat.getColor(OTPVerificationActivity.this,R.color.textBlue));

                 callValidateOTP_Api();
                 initObserver();


                }

/*
                if(NetworkUtilities.getConnectivityStatus(OTPVerificationActivity.this)){

                    binding.otpView.setOtpListener(new OTPListener() {
                        @Override
                        public void onInteractionListener() {

                         //  Global.showToast(OTPVerificationActivity.this,"Typing");
                            binding.txtErrorOTP.setVisibility(View.INVISIBLE);

                        }

                        @Override
                        public void onOTPComplete(String otp) {

                            binding.txtErrorOTP.setVisibility(View.INVISIBLE);
                            binding.btnVerifyOTP.setBackgroundColor(ContextCompat.getColor(OTPVerificationActivity.this,R.color.textBlue));
                            Intent mPinIntent = new Intent(OTPVerificationActivity.this, MPinActivity.class);
                            startActivity(mPinIntent);

                        }
                    });

                }
                else{
                    Global.showSnackBar(view,getResources().getString(R.string.check_internet_connection));
                }
 */

            }
        });
    }


}