package com.example.test.mPin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.test.R;
import com.example.test.success.SuccessActivity;
import com.example.test.databinding.ActivityMpinBinding;
import com.example.test.databinding.ActivityRegisterPasswordBinding;
import com.example.test.helper_classes.Global;
import com.example.test.helper_classes.NetworkUtilities;
import com.example.test.register_password.RegisterPasswordActivity;
import com.example.test.success.SuccessActivity;

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


                if(NetworkUtilities.getConnectivityStatus(MPinActivity.this)){

                    if(validations()){

                        Intent i = new Intent(MPinActivity.this, SuccessActivity.class);
                        i.putExtra("isFromMPinActivity",isFromMPinActivity);
                        startActivity(i);
                    }

                }

                else {
                    Global.showSnackBar(view,getResources().getString(R.string.check_internet_connection));
                }

            }
        });
    }

    private boolean validations(){

        if(binding.edtSetPin.getText().toString().isEmpty()){
            binding.edtSetPin.setError(getResources().getString(R.string.pin_cannot_be_empty));
            return false;
        }

        if(binding.edtReEnterPin.getText().toString().isEmpty()){
            binding.edtReEnterPin.setError(getResources().getString(R.string.pin_cannot_be_empty));
            return false;
        }

        if(!binding.edtReEnterPin.getText().toString().equals(binding.edtSetPin.getText().toString())){
            binding.edtReEnterPin.setError(getResources().getString(R.string.pin_not_matching));
            return false;
        }

        return true;
    }
}