package com.example.test.register_password;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.test.R;
import com.example.test.SuccessActivity;
import com.example.test.databinding.ActivityRegisterPasswordBinding;
import com.example.test.login.LoginActivity;
import com.example.test.otp.OTPActivity;

public class RegisterPasswordActivity extends AppCompatActivity {

    ActivityRegisterPasswordBinding binding;
    View view;
    boolean isFromRegisterPasswordActivity = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initializeFields();
        onClickListener();
    }

    private void initializeFields() {
        binding = DataBindingUtil.setContentView(this,R.layout.activity_register_password);
        view = binding.getRoot();
    }

    private void onClickListener() {

        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(RegisterPasswordActivity.this, SuccessActivity.class);
                i.putExtra("isFromRegisterPasswordActivity",isFromRegisterPasswordActivity);
                startActivity(i);

            }
        });
    }


}