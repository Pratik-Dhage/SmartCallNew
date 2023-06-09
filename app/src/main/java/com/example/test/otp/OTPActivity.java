package com.example.test.otp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.example.test.R;
import com.example.test.databinding.ActivityOtpactivityBinding;
import com.example.test.helper_classes.Global;
import com.example.test.helper_classes.NetworkUtilities;
import com.example.test.login.LoginActivity;

public class OTPActivity extends AppCompatActivity {

    ActivityOtpactivityBinding binding;
    View view;
    OTPViewModel otpViewModel;
    boolean isFromRegisterPasswordActivity = true;
    boolean isFromLoginForgotPassword ;
    public String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        initializeFields();
        onClickListener();
        CustomEditTextWatcher();
    }

    private void initializeFields() {
        binding = DataBindingUtil.setContentView(this,R.layout.activity_otpactivity);
        view = binding.getRoot();
        otpViewModel = new ViewModelProvider(this).get(OTPViewModel.class);
        binding.setViewModel(otpViewModel);

        isFromLoginForgotPassword = false; //initially isFromLoginForgotPassword will be false
    }

    private void callGenerateOTP_Api(){

        userId  = binding.edtOTPUserId.getText().toString().trim();
        otpViewModel.callGenerateOTP_Api(userId);

        Global.saveStringInSharedPref(this,"userId",userId); // saving userId  for calling api in RegisterPasswordActivity for Setting And Resetting Password
    }

    private void initObserver(){

        otpViewModel.getMutGenerateOTP_ResponseApi().observe(this,result->{

            if(NetworkUtilities.getConnectivityStatus(this)){

                if(result!=null){

                    //if coming from RegisterPasswordActivity
                    if(getIntent().hasExtra("isFromRegisterPasswordActivity")){
                        Intent i = new Intent(OTPActivity.this, OTPVerificationActivity.class);
                        i.putExtra("isFromRegisterPasswordActivity",isFromRegisterPasswordActivity);
                        i.putExtra("userId",userId);
                        startActivity(i);
                    }

                    //if coming from ForgotPassword / Reset Password
                  else  if(getIntent().hasExtra("isFromLoginForgotPassword")){
                        Intent i = new Intent(OTPActivity.this, OTPVerificationActivity.class);
                        isFromLoginForgotPassword = true;
                        i.putExtra("isFromLoginForgotPassword",isFromLoginForgotPassword);
                        i.putExtra("userId",userId);
                        startActivity(i);
                    }

                    else{
                        Intent i = new Intent(OTPActivity.this, OTPVerificationActivity.class);
                        i.putExtra("userId",userId);
                        startActivity(i);
                    }
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

    private void onClickListener() {

        binding.btnSendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(NetworkUtilities.getConnectivityStatus(OTPActivity.this))
                {

                    if(validations()){

                        callGenerateOTP_Api();
                        initObserver();
                    }

                }

                else {
                    Global.showSnackBar(view,getResources().getString(R.string.check_internet_connection));
                }
            }
        });
    }

    private boolean validations(){

        if(binding.edtOTPUserId.getText().toString().isEmpty()){
            binding.inputLayoutUserId.setError(getResources().getString(R.string.user_id_cannot_be_empty));
            return false;
        }

        return true;
    }

    private void CustomEditTextWatcher(){

        binding.edtOTPUserId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.inputLayoutUserId.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }
}