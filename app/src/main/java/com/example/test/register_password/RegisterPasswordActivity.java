package com.example.test.register_password;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.test.R;
import com.example.test.success.SuccessActivity;
import com.example.test.databinding.ActivityRegisterPasswordBinding;
import com.example.test.helper_classes.Global;
import com.example.test.helper_classes.NetworkUtilities;
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

                if(NetworkUtilities.getConnectivityStatus(RegisterPasswordActivity.this)){

                    if(validations()){

                        Intent i = new Intent(RegisterPasswordActivity.this, SuccessActivity.class);
                        i.putExtra("isFromRegisterPasswordActivity",isFromRegisterPasswordActivity);
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

        //Default Password
        if(binding.edtDefaultPassword.getText().toString().isEmpty()){
            binding.edtDefaultPassword.setError(getResources().getString(R.string.password_cannot_be_empty));
            return false;
        }

        // Set Password for empty
        if(binding.edtSetPassword.getText().toString().isEmpty()){
           binding.edtSetPassword.setError(getResources().getString(R.string.password_cannot_be_empty));
            return false;
        }

         //Set Password for 8-12 characters long,alphanumeric,with at least 1 special character
        if(!Global.isValidPassword(binding.edtSetPassword.getText().toString())){
            binding.edtSetPassword.setError(getResources().getString(R.string.password_not_valid));
            return false;
        }

        // Confirm Password for empty
        if(binding.edtConfirmPassword.getText().toString().isEmpty()){
            binding.edtConfirmPassword.setError(getResources().getString(R.string.password_cannot_be_empty));
            return false;
        }


         //if Confirm Password not equals Set Password
        if(!binding.edtConfirmPassword.getText().toString().equals(binding.edtSetPassword.getText().toString())) {
            binding.edtConfirmPassword.setError(getResources().getString(R.string.password_not_matching));
            return false;
        }

        return true;
    }


}