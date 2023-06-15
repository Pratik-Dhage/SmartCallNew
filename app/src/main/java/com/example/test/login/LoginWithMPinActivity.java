package com.example.test.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.example.test.R;
import com.example.test.databinding.ActivityLoginWithMpinBinding;
import com.example.test.helper_classes.Global;
import com.example.test.mPin.ResetMPinActivity;
import com.example.test.main_dashboard.MainActivity3API;
import com.example.test.roomDB.dao.MPinDao;
import com.example.test.roomDB.database.LeadListDB;

import in.aabhasjindal.otptextview.OTPListener;
import in.aabhasjindal.otptextview.OtpTextView;

public class LoginWithMPinActivity extends AppCompatActivity {

    ActivityLoginWithMpinBinding binding;
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_with_mpin);

        initializeFields();
        onClickListener();

        otpListener();
    }

    private void initializeFields() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login_with_mpin);
        view = binding.getRoot();


        //if coming from Login Activity->Generate MPin
        if(LoginActivity.userName!=null){
            binding.txtWelcomeUser.setText("Welcome "+LoginActivity.userName);
        }

    }

    private void otpListener() {

        OtpTextView otpTextView = binding.mpinView;
        otpTextView.setOtpListener(new OTPListener() {
            @Override
            public void onInteractionListener() {
                // This method will be called when any digit is entered or deleted.

                binding.labelMPinIsInCorrect.setVisibility(View.INVISIBLE);

                if (otpTextView.getOTP().length() == 4) {
                    // All four entries of the OTP have been written by the user.
                    navigateToDashBoardUsingMPin();

                }
            }

            @Override
            public void onOTPComplete(String otp) {

            }
        });

    }

    private void onClickListener() {

        binding.labelForgotMPin.setOnClickListener(v -> {

            Intent i = new Intent(this, ResetMPinActivity.class);
            startActivity(i);
        });
    }

    private void navigateToDashBoardUsingMPin() {

        MPinDao mPinDao = LeadListDB.getInstance(this).mPinDao();

        String mPinUserNameFromRomDB = mPinDao.getUserNameUsingMPinInRoomDB(binding.mpinView.getOTP());

        String mPinFromRoomDB = mPinDao.getMPinFromRoomDB(mPinUserNameFromRomDB);

        String UserID = mPinDao.getUserID(binding.mpinView.getOTP());

        String BranchCode = mPinDao.getBranchCode(binding.mpinView.getOTP());

        Global.saveStringInSharedPref(this,"UserID",UserID);
        Global.saveStringInSharedPref(this,"BranchCode",BranchCode);

        if (binding.mpinView.getOTP().equals(mPinFromRoomDB)) {

            Intent i = new Intent(this, MainActivity3API.class);
            i.putExtra("isFromLoginWithMPin","isFromLoginWithMPin");
            i.putExtra("MPin",binding.mpinView.getOTP());
            i.putExtra("UserID",UserID);
            i.putExtra("BranchCode",BranchCode);
            startActivity(i);

            Global.showToast(this,getString(R.string.login_successful));
            System.out.println("Here UserID from RoomDB "+UserID);
            System.out.println("Here BranchCode from RoomDB "+BranchCode);

            //Save MPin SharedPreferences
            Global.saveStringInSharedPref(this,"MPin",binding.mpinView.getOTP());
        }

        else{

            //Global.showToast(this,getString(R.string.mpin_is_incorrect));
            binding.labelMPinIsInCorrect.setVisibility(View.VISIBLE);
        }

        System.out.println("Here mPinFromRoomDB: " + mPinFromRoomDB);
        System.out.println("Here mPinUserNameFromRoomDB: " + mPinUserNameFromRomDB);


    }


}