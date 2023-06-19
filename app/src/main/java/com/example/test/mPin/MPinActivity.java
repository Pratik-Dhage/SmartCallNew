package com.example.test.mPin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.example.test.R;
import com.example.test.login.LoginActivity;
import com.example.test.login.LoginWithMPinActivity;
import com.example.test.roomDB.dao.LeadCallDao;
import com.example.test.roomDB.dao.MPinDao;
import com.example.test.roomDB.database.LeadListDB;
import com.example.test.roomDB.model.MPinRoomModel;
import com.example.test.success.SuccessActivity;
import com.example.test.databinding.ActivityMpinBinding;
import com.example.test.databinding.ActivityRegisterPasswordBinding;
import com.example.test.helper_classes.Global;
import com.example.test.helper_classes.NetworkUtilities;
import com.example.test.register_password.RegisterPasswordActivity;
import com.example.test.success.SuccessActivity;

import java.util.Objects;

public class MPinActivity extends AppCompatActivity {

    ActivityMpinBinding binding;
    View view;
    MPinViewModel mPinViewModel;
    boolean isFromMPinActivity = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mpin);

        initializeFields();
        onClickListener();
    }

    private void initializeFields() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_mpin);
        view = binding.getRoot();
        mPinViewModel = new ViewModelProvider(this).get(MPinViewModel.class);
        binding.setViewModel(mPinViewModel);

        setToolBarTitle();
    }

    private void setToolBarTitle(){
        if(getIntent().hasExtra("isFromLoginWithOTPFragment_ResetMPin") || getIntent().hasExtra("isFromLoginWithUserCredential_ResetMPin"))
        {
          binding.txtToolbarHeading.setText(R.string.reset_mpin);
        }
    }

    private void onClickListener() {

        binding.ivBack.setOnClickListener(v->{
            onBackPressed();
        });

        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (NetworkUtilities.getConnectivityStatus(MPinActivity.this)) {

                    if (validation()) {

                        String userMPin = binding.setMpinView.getOTP();
                        Global.saveStringInSharedPref(MPinActivity.this, "userMPin", userMPin); //save MPin in SharedPreferences for Logging using MPin
                        System.out.println("Here userMPin: "+userMPin);
                        saveMPinInRoomDB(userMPin);

                        if(getIntent().hasExtra("isFromLoginWithOTPFragment_ResetMPin") || getIntent().hasExtra("isFromLoginWithUserCredential_ResetMPin")){
                            Intent i = new Intent(MPinActivity.this, SuccessActivity.class);
                            i.putExtra("isFromResetMPin","isFromResetMPin");
                            startActivity(i);
                        }

                        // 1st time Generating MPin
                        else{
                            Intent i = new Intent(MPinActivity.this, SuccessActivity.class);
                            i.putExtra("isFromMPinActivity", isFromMPinActivity);
                            startActivity(i);
                        }

                    }

                } else {
                    Global.showSnackBar(view, getResources().getString(R.string.check_internet_connection));
                }

            }
        });
    }


    private boolean validation() {

        //means if Set New mPin != Re_enter Pin with Null safety
        if (!Objects.equals(binding.setMpinView.getOTP(), binding.reEnterMpinView.getOTP())) {
            binding.txtErrorPIN.setVisibility(View.VISIBLE);
            return false;
        }


        return true;
    }

    private void saveMPinInRoomDB(String userMPin) {
        MPinDao mPinDao = LeadListDB.getInstance(this).mPinDao();
        String userNameFromOTPResponse = Global.getStringFromSharedPref(this, "userNameFromOTPResponse");
        String UserID = Global.getStringFromSharedPref(this,"UserID");

        checkIfMPinExists(userMPin);

        System.out.println("Here userMPinInRoomDB: " + mPinDao.getMPinFromRoomDB(UserID));
        System.out.println("Here userNameMPinInRoomDB: " + mPinDao.getUserNameUsingMPinInRoomDB(userMPin));
        System.out.println("Here UserIDinRoomDB: " + mPinDao.getUserID(userMPin));
        System.out.println("Here BranchCodeInInRoomDB: " + mPinDao.getBranchCode(userMPin));
    }

    private void checkIfMPinExists(String userMPin){

        MPinDao mPinDao = LeadListDB.getInstance(this).mPinDao();
        int countOfMPin = mPinDao.checkAnyMPinExists();
        String userNameFromOTPResponse = Global.getStringFromSharedPref(this, "userNameFromOTPResponse");

        String UserID = Global.getStringFromSharedPref(this,"UserID");
        String BranchCode = Global.getStringFromSharedPref(this,"BranchCode");

        if (countOfMPin > 0) {
            // At least one mPin exists in the table
            mPinDao.updateMPin(userMPin,UserID,BranchCode);
            Global.showToast(this,"Updated mPin");

        }
        else {
            // No mPin exists in the table
           // Insert the new mPin in the Room database

          //  MPinRoomModel newMPinRoomModel = new MPinRoomModel(userMPin, userNameFromOTPValidationResponse);
          //  mPinDao.insert(newMPinRoomModel);

            MPinRoomModel newMPinRoomModel = new MPinRoomModel(userMPin,  UserID , BranchCode);
            mPinDao.insert(newMPinRoomModel);
            Global.showToast(this,"Saved mPin");
        }

    }

}