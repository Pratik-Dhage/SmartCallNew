package com.example.test.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.test.R;
import com.example.test.databinding.ActivityLoginWithMpinBinding;
import com.example.test.mPin.ResetMPinActivity;

public class LoginWithMPinActivity extends AppCompatActivity {

    ActivityLoginWithMpinBinding binding;
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_with_mpin);

        initializeFields();
        onClickListener();
    }

    private void initializeFields() {
        binding = DataBindingUtil.setContentView(this,R.layout.activity_login_with_mpin);
        view = binding.getRoot();
    }

    private void onClickListener() {

        binding.labelForgotMPin.setOnClickListener(v->{

            Intent i = new Intent(this , ResetMPinActivity.class);
            startActivity(i);
        });
    }
}