package com.example.test.otp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.test.R;
import com.example.test.databinding.ActivityOtpactivityBinding;
import com.example.test.helper_classes.Global;
import com.example.test.helper_classes.NetworkUtilities;
import com.example.test.login.LoginActivity;
import com.example.test.login.LoginWithMPinActivity;
import com.example.test.mPin.MPinActivity;
import com.example.test.roomDB.dao.MPinDao;
import com.example.test.roomDB.database.LeadListDB;

import java.util.Locale;

public class OTPActivity extends AppCompatActivity {

    ActivityOtpactivityBinding binding;
    View view;
    OTPViewModel otpViewModel;
    boolean isFromRegisterPasswordActivity = true;
    boolean isFromLoginForgotPassword ;
    public String userId;
    public static String BranchCode;
    public static  String UserID;
    public static String UserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        initializeFields();
        onClickListener();
        CustomEditTextWatcher();
    }

    private void initializeFields() {
        binding = DataBindingUtil.setContentView(this,R.layout.activity_otpactivity);
        view = binding.getRoot();
        otpViewModel = new ViewModelProvider(this).get(OTPViewModel.class);
        binding.setViewModel(otpViewModel);

        isFromLoginForgotPassword = false; //initially isFromLoginForgotPassword will be false
    }

    private void callGenerateOTP_Api(){

        userId  = binding.edtOTPUserId.getText().toString().trim();
        otpViewModel.callGenerateOTP_Api(userId);

        Global.saveStringInSharedPref(this,"userId",userId); // saving userId  for calling api in RegisterPasswordActivity for Setting And Resetting Password
    }

    private void initObserver(){

        otpViewModel.getMutGenerateOTP_ResponseApi().observe(this,result->{

            if(NetworkUtilities.getConnectivityStatus(this)){

                if(result!=null){

                    // Store UserID and BranchCode To Call In API's
                    UserID =  String.valueOf(result.getUserId());
                    BranchCode = String.valueOf(result.getBranchCode());
                    UserName = String.valueOf(result.getUserName());

                    //Store in Shared Preference For Storing MPin
                    Global.saveStringInSharedPref(this,"UserID",UserID);
                    Global.saveStringInSharedPref(this,"BranchCode",BranchCode);
                    Global.saveStringInSharedPref(this,"UserName",UserName);

                    //First Time User Registration
                    System.out.println("Here AuthenticationResult: "+result.getAuthenticationResult());

                  if (null != result.getAuthenticationResult() && result.getAuthenticationResult().toString().toLowerCase().contains("already registered")) {
                        //Global.showToast(this, result.getAuthenticationResult().toString());
                        System.out.println("Here Authentication Result :"+result.getAuthenticationResult().toString());
                        binding.txtAlreadyRegistered.setVisibility(View.VISIBLE);
                        binding.txtLoginWithMPin.setVisibility(View.VISIBLE);
                        binding.txtLoginWithUserCredentials.setVisibility(View.VISIBLE);
                        binding.inputLayoutUserId.setVisibility(View.INVISIBLE);
                        binding.btnSendOTP.setVisibility(View.INVISIBLE);
                        binding.txtUserIDError.setVisibility(View.GONE);

                    }
                    else if (null != result.getAuthenticationResult() && result.getAuthenticationResult().toString().toLowerCase().contains("invalid userid")) {
                      //  Global.showToast(this, result.getAuthenticationResult().toString());
                        System.out.println("Here Authentication Result :"+result.getAuthenticationResult().toString());
                        binding.txtUserIDError.setVisibility(View.VISIBLE);
                        binding.txtUserIDError.setText(String.valueOf(result.getAuthenticationResult()));


                    }

                    // if getAuthenticationResult==null
                   /* else{

                        //to display OTP code
                        Global.showToast(this,"OTP Code: "+ result.getOtpCode());

                        Intent i = new Intent(OTPActivity.this, OTPVerificationActivity.class);
                        i.putExtra("userId",userId);
                        startActivity(i);
                    }*/

                    // if getAuthenticationResult==null
                  else{

                        //to display OTP code
                        Global.showToast(this,"OTP Code: "+ result.getOtpCode());

                        // Save UserName in SharedPreferences for Saving UserName in RoomDB
                        Global.saveStringInSharedPref(this,"userNameFromOTPResponse",String.valueOf(result.getUserName()));

                        //if coming from RegisterPasswordActivity
                        if(getIntent().hasExtra("isFromRegisterPasswordActivity")){
                            Intent i = new Intent(OTPActivity.this, OTPVerificationActivity.class);
                            i.putExtra("isFromRegisterPasswordActivity",isFromRegisterPasswordActivity);
                            i.putExtra("userId",userId);
                            startActivity(i);
                        }

                        //if coming from ForgotPassword / Reset Password
                        else  if(getIntent().hasExtra("isFromLoginForgotPassword")){
                            Intent i = new Intent(OTPActivity.this, OTPVerificationActivity.class);
                            isFromLoginForgotPassword = true;
                            i.putExtra("isFromLoginForgotPassword",isFromLoginForgotPassword);
                            i.putExtra("userId",userId);
                            startActivity(i);
                        }

                        //First Time Registration
                        else{
                            Intent i = new Intent(OTPActivity.this, OTPVerificationActivity.class);
                            i.putExtra("userId",userId);
                            startActivity(i);
                        }



                    }



                }

                //handle  error response
                otpViewModel.getMutErrorResponse().observe(this, error -> {

                    if (error != null && !error.isEmpty()) {
                        Global.showSnackBar(view, error);
                        System.out.println("Here: " + error);
                    } else {
                        Global.showSnackBar(view, getResources().getString(R.string.check_internet_connection));
                    }
                });


            }
            else{
                Global.showSnackBar(view,getString(R.string.check_internet_connection));
            }

        });
    }

    private void onClickListener() {

        binding.btnSendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(NetworkUtilities.getConnectivityStatus(OTPActivity.this))
                {

                    if(validations()){

                        callGenerateOTP_Api();
                        initObserver();
                         onClickListener();
                    }

                }

                else {
                    Global.showSnackBar(view,getResources().getString(R.string.check_internet_connection));
                }
            }
        });

        binding.txtLoginWithMPin.setOnClickListener(v->{
            if( binding.txtLoginWithMPin.getVisibility()==View.VISIBLE){
                checkIfMPinExists();
            }
        });

        binding.txtLoginWithUserCredentials.setOnClickListener(v->{
            if(binding.txtLoginWithUserCredentials.getVisibility()==View.VISIBLE){
                Intent i = new Intent(OTPActivity.this, LoginActivity.class);
                i.putExtra("isFromOTPActivityLoginWithCredentials","isFromOTPActivityLoginWithCredentials");
                startActivity(i);
                Log.d("Debug", "Login with User Credentials clicked");
                finish();
            }

        });
    }

    private void checkIfMPinExists(){

        MPinDao mPinDao = LeadListDB.getInstance(this).mPinDao();
        int countOfMPin = mPinDao.checkAnyMPinExists();
       String UserIDInRoomDB = mPinDao.getUserID();

       // If UserID == UserID in RoomDB
        if (countOfMPin > 0 && UserIDInRoomDB==UserID) {
            // At least one mPin exists in the table
            startActivity(new Intent(this, LoginWithMPinActivity.class));
        } else {
            // No mPin exists in the table
              Intent i = new Intent(OTPActivity.this, MPinActivity.class);
              startActivity(i);
        }

    }

    private boolean validations(){

        if(binding.edtOTPUserId.getText().toString().isEmpty()){
            binding.inputLayoutUserId.setError(getResources().getString(R.string.user_id_cannot_be_empty));
            return false;
        }

        return true;
    }

    private void CustomEditTextWatcher(){

        binding.edtOTPUserId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.inputLayoutUserId.setError(null);
                binding.txtUserIDError.setVisibility(View.GONE);
                binding.txtAlreadyRegistered.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }
}