package com.example.test.mPin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.test.R;
import com.example.test.SuccessActivity;
import com.example.test.databinding.ActivityMpinBinding;
import com.example.test.databinding.ActivityRegisterPasswordBinding;
import com.example.test.register_password.RegisterPasswordActivity;

public class MPinActivity extends AppCompatActivity {

    ActivityMpinBinding binding;
    View view;
    boolean isFromMPinActivity = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mpin);

        initializeFields();
        onClickListener();
    }

    private void initializeFields() {
        binding = DataBindingUtil.setContentView(this,R.layout.activity_mpin);
        view = binding.getRoot();
    }

    private void onClickListener() {
        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(MPinActivity.this, SuccessActivity.class);
                i.putExtra("isFromMPinActivity",isFromMPinActivity);
                startActivity(i);

            }
        });
    }
}