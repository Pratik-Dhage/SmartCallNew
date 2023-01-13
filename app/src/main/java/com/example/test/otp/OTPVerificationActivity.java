package com.example.test.otp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

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

public class OTPVerificationActivity extends AppCompatActivity {

    ActivityOtpverificationBinding binding;
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpverification);


        //This activity is made by externally adding an OTP Library
        initializeFields();
        onClickListeners();

    }

    private void initializeFields() {

        binding = DataBindingUtil.setContentView(this,R.layout.activity_otpverification);
        view = binding.getRoot();
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
                    Intent registerIntent = new Intent(OTPVerificationActivity.this, RegisterPasswordActivity.class);
                    startActivity(registerIntent);
                }


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
            }
        });
    }


}