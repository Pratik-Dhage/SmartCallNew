package com.example.test.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;

import com.example.test.R;
import com.example.test.databinding.ActivityLoginBinding;
import com.example.test.helper_classes.Global;
import com.example.test.helper_classes.NetworkUtilities;
import com.example.test.otp.OTPActivity;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    View view;
    LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initializeFields();
        onClickListener();
        CustomEditTextWatcher();
    }

    private void initializeFields() {
        binding = DataBindingUtil.setContentView(this,R.layout.activity_login);
        view = binding.getRoot();
        loginViewModel = binding.getViewModel();
    }

    private void onClickListener() {

        binding.txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(LoginActivity.this, OTPActivity.class);
                startActivity(i);

            }
        });

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               if(NetworkUtilities.getConnectivityStatus(LoginActivity.this)){

                   if(validations()){
                       Global.showToast(LoginActivity.this,"All Clear");
                   }

               }

               else{
                   Global.showSnackBar(view,getResources().getString(R.string.check_internet_connection));
               }


            }
        });
    }

    private boolean validations(){

        if(binding.edtUserID.getText().toString().isEmpty()){
            binding.inputLayoutId.setError(getResources().getString(R.string.user_id_cannot_be_empty));
            return false;
        }

        if(binding.edtUserPassword.getText().toString().isEmpty()){
           binding.inputLayoutPassword.setError(getResources().getString(R.string.password_cannot_be_empty));
           return false;
        }

        if(!Global.isValidPassword(binding.edtUserPassword.getText().toString())){
            binding.inputLayoutPassword.setError(getResources().getString(R.string.password_not_valid));
            return false;
        }

        return true;

    }

    private void CustomEditTextWatcher(){

        binding.edtUserID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.inputLayoutId.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        binding.edtUserPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.inputLayoutPassword.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

}