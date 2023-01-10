package com.example.test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.test.databinding.ActivitySuccessBinding;
import com.example.test.mPin.MPinActivity;
import com.example.test.otp.OTPActivity;
import com.example.test.otp.OTPVerifyActivity;
import com.example.test.register_password.RegisterPasswordActivity;

public class SuccessActivity extends AppCompatActivity {

    ActivitySuccessBinding binding;
    View view;
    boolean isFromRegisterPasswordActivity = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Use this activity for Registration Password Success & Pin has been generated Success
        initializeFields();
        onClickListener();
    }

    private void initializeFields() {
        binding = DataBindingUtil.setContentView(this,R.layout.activity_success);
        view = binding.getRoot();

        if(getIntent().hasExtra("isFromRegisterPasswordActivity")){
            binding.labelSuccess.setText(getResources().getString(R.string.registration_successful));
            binding.txtLink.setText(getResources().getString(R.string.generate_mpin));
        }


    }

    private void onClickListener() {
        binding.txtLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //on Clicking Generate MPIN
               /* Intent i = new Intent(SuccessActivity.this, OTPActivity.class);
                i.putExtra("isFromRegisterPasswordActivity",isFromRegisterPasswordActivity);
                startActivity(i);*/

                //on Clicking Generate MPIN // NORMAL FLOW
                Intent mPinIntent = new Intent(SuccessActivity.this, MPinActivity.class);
                startActivity(mPinIntent);
            }
        });
    }
}