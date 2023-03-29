package com.example.test.npa_flow;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.test.R;
import com.example.test.databinding.ActivityPaymentNotificationOfCustomerBinding;
import com.example.test.fragments_activity.BalanceInterestCalculationActivity;

public class PaymentNotificationOfCustomerActivity extends AppCompatActivity {

    ActivityPaymentNotificationOfCustomerBinding binding;
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_payment_notification_of_customer);


        initializeFields();
        onClickListener();

    }

    private void initializeFields() {

        binding = DataBindingUtil.setContentView(this, R.layout.activity_payment_notification_of_customer);
        view = binding.getRoot();
    }

    private void onClickListener() {

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        binding.btnOthers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.edtPleaseSpecify.setVisibility(View.VISIBLE);
            }
        });

        binding.btnReadyToPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(PaymentNotificationOfCustomerActivity.this,PaymentModeActivity.class);
                startActivity(i);
            }
        });

        binding.btnNearBy.setOnClickListener(v->{
            Intent i = new Intent(this, NearByCustomersActivity.class);
            startActivity(i);
        });

        binding.btnCalculate.setOnClickListener(v->{
            Intent i = new Intent(this, BalanceInterestCalculationActivity.class);
            startActivity(i);
        });

    }
}