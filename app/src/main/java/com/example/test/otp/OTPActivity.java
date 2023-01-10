package com.example.test.otp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.test.R;
import com.example.test.databinding.ActivityOtpactivityBinding;
import com.example.test.login.LoginActivity;

public class OTPActivity extends AppCompatActivity {

    ActivityOtpactivityBinding binding;
    View view;

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

                Intent i = new Intent(OTPActivity.this, OTPVerifyActivity.class);
                startActivity(i);
            }
        });
    }
}