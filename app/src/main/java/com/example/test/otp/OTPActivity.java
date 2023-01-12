package com.example.test.otp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

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
        otpViewModel = binding.getViewModel();
    }

    private void onClickListener() {

        binding.btnSendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(NetworkUtilities.getConnectivityStatus(OTPActivity.this))
                {

                    if(validations()){

                        //if coming from RegisterPasswordActivity
                        if(getIntent().hasExtra("isFromRegisterPasswordActivity")){
                            Intent i = new Intent(OTPActivity.this, OTPVerifyActivity.class);
                            i.putExtra("isFromRegisterPasswordActivity",isFromRegisterPasswordActivity);
                            startActivity(i);
                        }

                        else{
                            Intent i = new Intent(OTPActivity.this, OTPVerifyActivity.class);
                            startActivity(i);
                        }
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