package com.example.test.call_status;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.example.test.R;
import com.example.test.databinding.ActivityCallStatusBinding;
import com.example.test.helper_classes.Global;

public class CallStatusActivity extends AppCompatActivity {

    ActivityCallStatusBinding binding;
    View view;
    int REQUEST_CALL = 1; // can use any integer value

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        initializeFields();
        setUpData();
        onClickListener();
    }

    private void initializeFields() {

        binding = DataBindingUtil. setContentView(this,R.layout.activity_call_status);
        view = binding.getRoot();
    }

    private void setUpData(){
        String firstName = getIntent().getStringExtra("firstName");
        String phoneNumber = getIntent().getStringExtra("phoneNumber");

        binding.txtLeadName.setText(firstName);
        binding.txtLeadMobileNumber.setText(phoneNumber);
    }

    private void onClickListener() {

        binding.ivWifiCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // Permission is not granted, request the permission
                    ActivityCompat.requestPermissions(CallStatusActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
                } else {
                    // Permission has already been granted, make the call
                    String phoneNumber = getIntent().getStringExtra("phoneNumber");
                    Intent dial = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+phoneNumber));
                    startActivity(dial);
                }

            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted, make the call
                String phoneNumber = getIntent().getStringExtra("phoneNumber");
                Intent dial = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+phoneNumber));
                startActivity(dial);
            } else {
                // Permission is denied, show a message
                Global.showSnackBar(view,"Permission to make a call is denied");
            }
        }
    }

}
