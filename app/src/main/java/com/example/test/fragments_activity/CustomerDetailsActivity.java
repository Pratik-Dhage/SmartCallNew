package com.example.test.fragments_activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.test.R;
import com.example.test.databinding.ActivityCustomerDetailsBinding;

public class CustomerDetailsActivity extends AppCompatActivity {

    ActivityCustomerDetailsBinding binding;
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_customer_details);

        initializeFields();
        onClickListener();
    }

    private void initializeFields() {

        binding= DataBindingUtil.setContentView(this,R.layout.activity_customer_details);
        view = binding.getRoot();
    }

    private void onClickListener() {

        binding.btnVisitedTheCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CustomerDetailsActivity.this, PaymentNotificationActivity.class);
                startActivity(i);
            }
        });


        binding.btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CustomerDetailsActivity.this,BalanceInterestCalculationActivity.class);
                startActivity(i);
            }
        });

      /*  binding.ivRightArrowPersonalDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.labelPersonalDetails.setVisibility(View.INVISIBLE);
                binding.ivRightArrowPersonalDetails.setVisibility(View.INVISIBLE);
                binding.cardViewPersonalDetails.setVisibility(View.VISIBLE);
                binding.btnVisitedTheCustomer.setVisibility(View.VISIBLE);
                binding.btnDidNotVisitedTheCustomer.setVisibility(View.VISIBLE);
                binding.ivNotesIcon.setVisibility(View.VISIBLE);
                binding.ivHistory.setVisibility(View.VISIBLE);
            }
        });*/

      /*  binding.ivDownArrowPersonalDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.cardViewPersonalDetails.setVisibility(View.GONE);
                binding.labelPersonalDetails.setVisibility(View.VISIBLE);
                binding.ivRightArrowPersonalDetails.setVisibility(View.VISIBLE);
                binding.btnVisitedTheCustomer.setVisibility(View.GONE);
                binding.btnDidNotVisitedTheCustomer.setVisibility(View.GONE);
                binding.ivNotesIcon.setVisibility(View.GONE);
                binding.ivHistory.setVisibility(View.GONE);
            }
        });*/
    }
}