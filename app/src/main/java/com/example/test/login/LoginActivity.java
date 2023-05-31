package com.example.test.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.example.test.R;
import com.example.test.databinding.ActivityLoginBinding;
import com.example.test.helper_classes.Global;
import com.example.test.helper_classes.NetworkUtilities;
import com.example.test.main_dashboard.MainActivity3API;
import com.example.test.otp.OTPActivity;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    View view;
    LoginViewModel loginViewModel;

    public String userId ;
    public String userPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initializeFields();
        onClickListener();
        CustomEditTextWatcher();

       // callLoginAPi();


    }

    private void initializeFields() {
        binding = DataBindingUtil.setContentView(this,R.layout.activity_login);
        view = binding.getRoot();
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        binding.setViewModel(loginViewModel);


    }



    private void callLoginApi(){

        userId  = binding.edtUserID.getText().toString().trim();
        userPassword   = binding.edtUserPassword.getText().toString().trim();

        loginViewModel.callLoginApi(userId,userPassword);
    }

    private void initObserver(){

        loginViewModel.getMutLoginResponseApi().observe(LoginActivity.this, result -> {

            //to check if userId from Login Page and API response is same
            if(result.getUserId().contentEquals(userId)){

                if(result.getAuthenticationResult().toLowerCase().contains("success")){

                    Global.showToast(LoginActivity.this, "Login :" + result.getAuthenticationResult());

                    Intent i = new Intent(LoginActivity.this, MainActivity3API.class);
                    startActivity(i);
                }
            }



        });

        //handle  error response
        loginViewModel.getMutErrorResponse().observe(LoginActivity.this, error -> {

            if (error != null && !error.isEmpty()) {
                Global.showSnackBar(view, error);
                System.out.println("Here: " + error);
            } else {
                Global.showSnackBar(view, getResources().getString(R.string.check_internet_connection));
            }
        });
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

                   if(validations()) {

                       callLoginApi();
                       initObserver();
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