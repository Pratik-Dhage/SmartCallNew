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
import com.example.test.api_manager.WebServices;
import com.example.test.databinding.ActivityLoginBinding;
import com.example.test.helper_classes.Global;
import com.example.test.helper_classes.NetworkUtilities;
import com.example.test.main_dashboard.MainActivity3API;
import com.example.test.otp.OTPActivity;
import com.example.test.roomDB.dao.MPinDao;
import com.example.test.roomDB.database.LeadListDB;
import com.example.test.success.SuccessActivity;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    View view;
    LoginViewModel loginViewModel;

    public String userId ;
    public String userPassword;
    public static  String userName;
   public static String BranchCode;
   public static  String UserID;
   public static String UserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initializeFields();
        onClickListener();
        CustomEditTextWatcher();

        if(getIntent().hasExtra("isFromOTPActivityLoginWithCredentials")){
            System.out.println("Here isFromOTPActivityLoginWithCredentials");
        }
        else {
            checkIfMPinExists();
        }



    }

    private void initializeFields() {
        binding = DataBindingUtil.setContentView(this,R.layout.activity_login);
        view = binding.getRoot();
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        binding.setViewModel(loginViewModel);


    }

    private void checkIfMPinExists(){

        MPinDao mPinDao = LeadListDB.getInstance(this).mPinDao();
        int countOfMPin = mPinDao.checkAnyMPinExists();

        if (countOfMPin > 0) {
            // At least one mPin exists in the table
            startActivity(new Intent(this,LoginWithMPinActivity.class));
        } else {
            // No mPin exists in the table

        }

    }

    private void callLoginApi(){

        userId  = binding.edtUserID.getText().toString().trim();
        userPassword   = binding.edtUserPassword.getText().toString().trim();
      //  userName = binding.edtUserName.getText().toString().trim();

        loginViewModel.callLoginApi(userId,userPassword);
    }

    private void initObserver(){

        loginViewModel.getMutLoginResponseApi().observe(LoginActivity.this, result -> {

            /*
            //to check if userId from Login Page and API response is same
            if(result.getUserId().contentEquals(userId)){

                if(result.getAuthenticationResult().toLowerCase().contains("success")){

                    Global.showToast(LoginActivity.this, "Login :" + result.getAuthenticationResult());

                    Intent i = new Intent(LoginActivity.this, SuccessActivity.class);
                    i.putExtra("userName",userName);
                    i.putExtra("isFromLoginActivity","isFromLoginActivity");
                    startActivity(i);
                }
            }*/
            if(result!=null){
                Global.showToast(LoginActivity.this, "Login Response :" + result.getAuthenticationResult());

                userName = result.getUserName();

                if(result.getAuthenticationResult().toLowerCase().contains("success")){

                    // Store UserID and BranchCode To Call In API's
                    UserID =  String.valueOf(result.getUserId());
                    BranchCode = String.valueOf(result.getBranchCode());
                    UserName = String.valueOf(result.getUserName());

                    System.out.println("Here UserId & BranchCode:"+UserID+" "+BranchCode);

                    Intent i = new Intent(LoginActivity.this, SuccessActivity.class);
                    i.putExtra("userName",userName);
                    i.putExtra("UserID",UserID);
                    i.putExtra("BranchCode",BranchCode);
                    i.putExtra("isFromLoginActivity","isFromLoginActivity");
                    startActivity(i);


                  //System.out.println("Here UserId & BranchCode:"+UserID+" "+BranchCode);


                  //Store in Shared Preference For Storing MPin
                    Global.saveStringInSharedPref(this,"UserID",UserID);
                    Global.saveStringInSharedPref(this,"BranchCode",BranchCode);
                    Global.saveStringInSharedPref(this,"UserName",UserName);

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
                i.putExtra("isFromLoginSignUp","isFromLoginSignUp");
                startActivity(i);
            }
        });

        binding.labelForgotPassword.setOnClickListener(v->{
            Intent i = new Intent(LoginActivity.this, OTPActivity.class);
            i.putExtra("isFromLoginForgotPassword","isFromLoginForgotPassword");
            startActivity(i);

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

        //UserID
        if(binding.edtUserID.getText().toString().isEmpty()){
            binding.inputLayoutId.setError(getResources().getString(R.string.user_id_cannot_be_empty));
            return false;
        }

        //Password
        if(binding.edtUserPassword.getText().toString().isEmpty()){
           binding.inputLayoutPassword.setError(getResources().getString(R.string.password_cannot_be_empty));
           return false;
        }

        if(!Global.isValidPassword(binding.edtUserPassword.getText().toString())){
            binding.inputLayoutPassword.setError(getResources().getString(R.string.password_not_valid));
            return false;
        }

        //UserName
       /* if(binding.edtUserName.getText().toString().isEmpty()){
            binding.inputLayoutUserName.setError(getResources().getString(R.string.user_name_cannot_be_empty));
            return false;
        }*/


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


       /* binding.edtUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.inputLayoutUserName.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
*/
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}