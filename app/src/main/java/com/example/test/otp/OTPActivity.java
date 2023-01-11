package com.example.test.otp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.test.R;
import com.example.test.databinding.ActivityOtpactivityBinding;
import com.example.test.helper_classes.Global;
import com.example.test.helper_classes.NetworkUtilities;
import com.example.test.login.LoginActivity;

public class OTPActivity extends AppCompatActivity {

    ActivityOtpactivityBinding binding;
    View view;
    boolean isFromRegisterPasswordActivity = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        initializeFields();
        onClickListener();
    }

    private void initializeFields() {
        binding = DataBindingUtil.setContentView(this,R.layout.activity_otpactivity);
        view = binding.getRoot();
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
            binding.edtOTPUserId.setError(getResources().getString(R.string.user_id_cannot_be_empty));
            return false;
        }

        return true;
    }
}