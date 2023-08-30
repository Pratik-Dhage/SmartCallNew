package com.example.test.otp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
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
    boolean isFromLoginForgotPassword ;
    OTPViewModel otpViewModel; // for Resend OTP

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpverification);


        //This activity is made by externally adding an OTP Library
        initializeFields();
        onClickListeners();
        otpListener();
        startCountDownTimer();
    }

    private void initializeFields() {

        binding = DataBindingUtil.setContentView(this,R.layout.activity_otpverification);
        view = binding.getRoot();
        otpVerifyViewModel = new ViewModelProvider(this).get(OTPVerifyViewModel.class);
        binding.setViewModel(otpVerifyViewModel);

        isFromLoginForgotPassword = false ; //initially isFromLoginForgotPassword will be false

        otpViewModel = new ViewModelProvider(this).get(OTPViewModel.class); // for Resend OTP
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
                   // Global.showToast(this,String.valueOf(result.getUserName()));

                    Global.showToast(this,result); // SUCCESS / FAILED

   /*                 if(result.getUserId().toString().contentEquals(userId)){
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


                    }*/

                    if(result.toLowerCase().contains("success")){
                        // coming From Reset MPin (LoginWithOTPFragment)
                        if(getIntent().hasExtra("isFromLoginWithOTPFragment_ResetMPin")){
                            Intent mpinIntent = new Intent(this,MPinActivity.class);
                            mpinIntent.putExtra("isFromLoginWithOTPFragment_ResetMPin","isFromLoginWithOTPFragment_ResetMPin");
                            startActivity(mpinIntent);
                        }

                        //coming from isFromLoginForgotPassword
                        else   if(getIntent().hasExtra("isFromLoginForgotPassword")){
                            Intent registerIntent = new Intent(OTPVerificationActivity.this, RegisterPasswordActivity.class);
                            isFromLoginForgotPassword = true;
                            registerIntent.putExtra("isFromLoginForgotPassword",isFromLoginForgotPassword);
                            startActivity(registerIntent);
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

    // on Clicking Resend OTP Call generateOTP API
    private void callGenerateOTP_Api(){

        userId  = getIntent().getStringExtra("userId");;
        otpViewModel.callGenerateOTP_ApiEverytime(userId); // using this api to get OTP even if User is Already Registered

    }

    private void initObserverGenerateOTP(){

        otpViewModel.getMutGenerateOTP_ResponseApi().observe(this,result->{

            if(NetworkUtilities.getConnectivityStatus(this)){

                if(result!=null){

                    Global.showToast(this,result.getOtpCode().toString());
                    System.out.println("Here OTPVerificationActivity Resend OTP called: "+result.getOtpCode().toString());
                }

                //handle  error response
                otpViewModel.getMutErrorResponse().observe(this, error -> {

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



        });

    }


    private void onClickListeners() {

        //on Clicking Resend OTP
        binding.labelResendOTP.setOnClickListener(v1->{
            System.out.println("Resend OTP clicked");
            startCountDownTimer();
            callGenerateOTP_Api();
            initObserverGenerateOTP();

        });


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

    private void startCountDownTimer(){
         CountDownTimer countdownTimer;
        // Start the countdown timer with 30 seconds duration and 1-second intervals
        countdownTimer = new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // Convert milliseconds to seconds
                int seconds = (int) (millisUntilFinished / 1000);

                // Format the time as "mm:ss"
                String time = String.format("%02d:%02d", seconds / 60, seconds % 60);

                // Update the TextView
               binding.txtOTPTimer.setText(time);
            }

            @Override
            public void onFinish() {
                // Countdown finished, perform any necessary actions
                binding.txtOTPTimer.setText("00:00");
                // Add code to handle timer completion
            }
        };

        countdownTimer.start(); // Start the countdown timer
    }

}