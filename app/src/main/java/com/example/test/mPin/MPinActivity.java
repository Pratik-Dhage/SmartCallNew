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
import com.example.test.success.SuccessActivity;
import com.example.test.databinding.ActivityMpinBinding;
import com.example.test.databinding.ActivityRegisterPasswordBinding;
import com.example.test.helper_classes.Global;
import com.example.test.helper_classes.NetworkUtilities;
import com.example.test.register_password.RegisterPasswordActivity;
import com.example.test.success.SuccessActivity;

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
        binding = DataBindingUtil.setContentView(this,R.layout.activity_mpin);
        view = binding.getRoot();
        mPinViewModel = new ViewModelProvider(this).get(MPinViewModel.class);
        binding.setViewModel(mPinViewModel);
    }

    private void onClickListener() {
        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(NetworkUtilities.getConnectivityStatus(MPinActivity.this)){

                    if(validations()){

                        Intent i = new Intent(MPinActivity.this, SuccessActivity.class);
                        i.putExtra("isFromMPinActivity",isFromMPinActivity);
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

        // for Set New Pin
        if(binding.edt1.getText().toString().isEmpty()){
           binding.txtErrorPIN.setVisibility(View.VISIBLE);
            return false;
        }

        if(binding.edt2.getText().toString().isEmpty()){
            binding.txtErrorPIN.setVisibility(View.VISIBLE);
            return false;
        }

        if(binding.edt3.getText().toString().isEmpty()){
            binding.txtErrorPIN.setVisibility(View.VISIBLE);
            return false;
        }

        if(binding.edt4.getText().toString().isEmpty()){
            binding.txtErrorPIN.setVisibility(View.VISIBLE);
            return false;
        }

       // for R-enter Pin
        if(binding.edt5.getText().toString().isEmpty()){
            binding.txtErrorPIN.setVisibility(View.VISIBLE);
            return false;
        }

        if(binding.edt6.getText().toString().isEmpty()){
            binding.txtErrorPIN.setVisibility(View.VISIBLE);
            return false;
        }

        if(binding.edt7.getText().toString().isEmpty()){
            binding.txtErrorPIN.setVisibility(View.VISIBLE);
            return false;
        }

        if(binding.edt8.getText().toString().isEmpty()){
            binding.txtErrorPIN.setVisibility(View.VISIBLE);
            return false;
        }



        //for comparing the Set New PIn & Re-enter Pin
        if(!binding.edt1.getText().toString().equals(binding.edt5.getText().toString())){
            binding.txtErrorPIN.setText(getResources().getString(R.string.pin_not_matching));
            return false;
        }

        if(!binding.edt2.getText().toString().equals(binding.edt6.getText().toString())){
            binding.txtErrorPIN.setText(getResources().getString(R.string.pin_not_matching));
            return false;
        }

        if(!binding.edt3.getText().toString().equals(binding.edt7.getText().toString())){
            binding.txtErrorPIN.setText(getResources().getString(R.string.pin_not_matching));
            return false;
        }

        if(!binding.edt4.getText().toString().equals(binding.edt8.getText().toString())){
            binding.txtErrorPIN.setText(getResources().getString(R.string.pin_not_matching));
            return false;
        }

        binding.txtErrorPIN.setVisibility(View.INVISIBLE);
        return true;
    }


}