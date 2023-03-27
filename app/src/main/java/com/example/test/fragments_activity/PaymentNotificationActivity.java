package com.example.test.fragments_activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.test.R;
import com.example.test.databinding.ActivityPaymentNotificationBinding;

public class PaymentNotificationActivity extends AppCompatActivity {

    ActivityPaymentNotificationBinding binding;
    View view ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_payment_notification);

         initializeFields();
         onClickListener();

    }

    private void initializeFields() {
        binding = DataBindingUtil.setContentView(this,R.layout.activity_payment_notification);
        view = binding.getRoot();
    }

    private void onClickListener() {

        binding.btnFullAmountPaid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PaymentNotificationActivity.this,VisitCompletionActivity.class);
                startActivity(i);
            }
        });

        binding.btnPartialAmountPaid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PaymentNotificationActivity.this,VisitCompletionActivity.class);
                startActivity(i);
            }
        });

        binding.btnNeedsWaiver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.edtReasonForWaiver.setVisibility(View.VISIBLE);
            }
        });

        binding.btnOthers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.edtPleaseSpecify.setVisibility(View.VISIBLE);
            }
        });
    }
}