package com.example.test.mPin.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.test.R;
import com.example.test.databinding.FragmentLoginWithOTPBinding;
import com.example.test.helper_classes.Global;
import com.example.test.helper_classes.NetworkUtilities;
import com.example.test.otp.OTPActivity;
import com.example.test.otp.OTPVerificationActivity;
import com.example.test.otp.OTPViewModel;


public class LoginWithOTPFragment extends Fragment {

    FragmentLoginWithOTPBinding binding;
    OTPViewModel otpViewModel;
    public String userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login_with_o_t_p, container, false);
        otpViewModel = new ViewModelProvider(this).get(OTPViewModel.class);
        binding.setViewModel(otpViewModel);


        onClickListener();
        CustomEditTextWatcher();

        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    private void callGenerateOTP_Api(){

        userId  = binding.edtOTPUserId.getText().toString().trim();
        otpViewModel.callGenerateOTP_Api(userId);
    }

    private void initObserver(){

        otpViewModel.getMutGenerateOTP_ResponseApi().observe(this,result->{

            if(NetworkUtilities.getConnectivityStatus(getContext())){

                if(result!=null){

                    Global.showToast(getContext(),String.valueOf(result.getOtpCode()));

                        Intent i = new Intent(getContext(), OTPVerificationActivity.class);
                        i.putExtra("userId",userId);
                        i.putExtra("isFromLoginWithOTPFragment_ResetMPin","isFromLoginWithOTPFragment_ResetMPin");
                        startActivity(i);

                    //Store in Shared Preference For Storing MPin
                    Global.saveStringInSharedPref(getContext(),"UserID",String.valueOf(result.getUserId()));
                    Global.saveStringInSharedPref(getContext(),"BranchCode",String.valueOf(result.getBranchCode()));

                }

                //handle  error response
                otpViewModel.getMutErrorResponse().observe(this, error -> {

                    if (error != null && !error.isEmpty()) {
                        Global.showSnackBar(binding.getRoot(), error);
                        System.out.println("Here: " + error);
                    } else {
                        Global.showSnackBar(binding.getRoot(), getResources().getString(R.string.check_internet_connection));
                    }
                });


            }
            else{
                Global.showSnackBar(binding.getRoot(),getString(R.string.check_internet_connection));
            }

        });
    }

    private void onClickListener() {

        binding.btnSendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(NetworkUtilities.getConnectivityStatus(getContext()))
                {

                    if(validations()){

                        callGenerateOTP_Api();
                        initObserver();
                    }

                }

                else {
                    Global.showSnackBar(binding.getRoot(),getResources().getString(R.string.check_internet_connection));
                }
            }
        });
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
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

}