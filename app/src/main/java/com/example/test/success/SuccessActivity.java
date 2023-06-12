package com.example.test.success;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;

import com.example.test.R;
import com.example.test.databinding.ActivitySuccessBinding;
import com.example.test.login.LoginActivity;
import com.example.test.login.LoginWithMPinActivity;
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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_success);
        view = binding.getRoot();

        //to make Generate Pin OR Login Now UnderLined
        binding.txtLink.setPaintFlags(binding.txtLink.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        if(getIntent().hasExtra("isFromRegisterPasswordActivity")){
            binding.labelSuccess.setText(getResources().getString(R.string.registration_successful));
            binding.txtLink.setText(getResources().getString(R.string.generate_mpin));
        }

     else   if(getIntent().hasExtra("isFromMPinActivity")){
            binding.labelSuccess.setText(getResources().getString(R.string.pin_generated_success));
            binding.txtLink.setText(getResources().getString(R.string.login_now));
        }

     else if(getIntent().hasExtra("isFromResetMPin")){
            binding.labelSuccess.setText(R.string.pin_reset_success);
            binding.txtLink.setText(getResources().getString(R.string.login_now));
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

                if(binding.txtLink.getText().toString().contentEquals(getResources().getString(R.string.login_now)))
                {
                    Intent loginIntent = new Intent(SuccessActivity.this, LoginWithMPinActivity.class);
                    loginIntent.putExtra("isFromLoginWithOTPFragment_ResetMPin","isFromLoginWithOTPFragment_ResetMPin");
                    startActivity(loginIntent);
                }

                //on Clicking Generate MPIN // NORMAL FLOW
                else{
                    Intent mPinIntent = new Intent(SuccessActivity.this, MPinActivity.class);
                    startActivity(mPinIntent);
                }

            }
        });
    }
}