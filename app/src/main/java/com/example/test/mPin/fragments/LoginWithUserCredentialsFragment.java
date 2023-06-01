package com.example.test.mPin.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.test.R;
import com.example.test.databinding.FragmentLoginWithUserCredentialsBinding;
import com.example.test.helper_classes.Global;
import com.example.test.mPin.MPinActivity;


public class LoginWithUserCredentialsFragment extends Fragment {

    FragmentLoginWithUserCredentialsBinding binding;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login_with_user_credentials, container, false);

        onClickListener();
        CustomEditTextWatcher();

        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    private void onClickListener(){
        binding.btnProceed.setOnClickListener(v->{

            if(validations())
            {
                Intent i = new Intent(getContext(), MPinActivity.class);
                startActivity(i);
            }
        });
    }


    private boolean validations(){

        if(binding.edtOTPUserId.getText().toString().isEmpty()){
            binding.inputLayoutUserId.setError(getResources().getString(R.string.user_id_cannot_be_empty));
            return false;
        }

        if(binding.edtOTPUserPassword.getText().toString().isEmpty()){
            binding.inputLayoutUserPassword.setError(getResources().getString(R.string.password_cannot_be_empty));
        }

        //Set Password for 8-12 characters long,alphanumeric,with at least 1 special character
        if(!Global.isValidPassword(binding.edtOTPUserPassword.getText().toString())){
            binding.inputLayoutUserPassword.setError(getResources().getString(R.string.password_not_valid));
            return false;
        }

        return true;
    }

    private void CustomEditTextWatcher(){

        //UserId
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

        //Password
        binding.edtOTPUserPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.inputLayoutUserPassword.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

}