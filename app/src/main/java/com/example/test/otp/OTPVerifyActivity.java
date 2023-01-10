package com.example.test.otp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.test.R;
import com.example.test.databinding.ActivityOtpverifyBinding;
import com.example.test.login.LoginActivity;
import com.example.test.register_password.RegisterPasswordActivity;

public class OTPVerifyActivity extends AppCompatActivity {

    ActivityOtpverifyBinding binding;
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initializeFields();
        onClickListener();
    }

    private void initializeFields() {

        binding = DataBindingUtil.setContentView(this,R.layout.activity_otpverify);
        view = binding.getRoot();
    }

    private void onClickListener() {

        if(binding.edtOTP.getText().toString().isEmpty()){

        }
        else{

            binding.btnVerifyOTP.setBackgroundColor(getResources().getColor(R.color.textBlue));
            binding.btnVerifyOTP.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent i = new Intent(OTPVerifyActivity.this, RegisterPasswordActivity.class);
                    startActivity(i);


                }
            });
        }

    }
}