package com.example.test.otp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.example.test.R;
import com.example.test.databinding.ActivityOtpverifyBinding;
import com.example.test.helper_classes.Global;
import com.example.test.helper_classes.NetworkUtilities;
import com.example.test.login.LoginActivity;
import com.example.test.mPin.MPinActivity;
import com.example.test.register_password.RegisterPasswordActivity;

public class OTPVerifyActivity extends AppCompatActivity {

    ActivityOtpverifyBinding binding;
    View view;
    OTPVerifyViewModel otpVerifyViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initializeFields();
        onClickListener();
    }

    private void initializeFields() {

        binding = DataBindingUtil.setContentView(this,R.layout.activity_otpverify);
        view = binding.getRoot();
        otpVerifyViewModel = binding.getViewModel();
    }

    private void onClickListener() {

        if(binding.edtOTP.getText().toString().isEmpty()){

        }

        //textwatcher for otp verify button

        binding.edtOTP.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.toString().length()==4){
                    binding.btnVerifyOTP.setBackgroundColor(getResources().getColor(R.color.textBlue));
                }
                else{
                    binding.btnVerifyOTP.setBackgroundColor(getResources().getColor(R.color.borderColor));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });




            binding.btnVerifyOTP.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(NetworkUtilities.getConnectivityStatus(OTPVerifyActivity.this)){

                        if(validations()){

                            //  Toast.makeText(OTPVerifyActivity.this, ""+getIntent().hasExtra("isFromRegisterActivity"), Toast.LENGTH_SHORT).show();

                            if(getIntent().hasExtra("isFromRegisterActivity") ){
                                Intent mPinIntent = new Intent(OTPVerifyActivity.this, MPinActivity.class);
                                startActivity(mPinIntent);
                            }

                            else{
                                // means user is coming from Login Activity
                                Intent i = new Intent(OTPVerifyActivity.this, RegisterPasswordActivity.class);
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

        if(binding.edtOTP.getText().toString().length()!=4){
            binding.edtOTP.setError(getResources().getString(R.string.enter_valid_otp));
           return false;
        }

        return true;
    }

    @Override
    public void onBackPressed() {
       // super.onBackPressed();
    }
}