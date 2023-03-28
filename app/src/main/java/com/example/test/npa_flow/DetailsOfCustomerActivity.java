package com.example.test.npa_flow;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.test.R;
import com.example.test.databinding.ActivityDetailsOfCustomerBinding;
import com.example.test.fragments_activity.CustomerDetailsActivity;

public class DetailsOfCustomerActivity extends AppCompatActivity {

    ActivityDetailsOfCustomerBinding binding;
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_details_of_customer);

        initializeFields();
        onClickListener();

    }

    private void initializeFields() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_details_of_customer);
        view = binding.getRoot();

    }

    private void onClickListener() {

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.ivCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(DetailsOfCustomerActivity.this,CallDetailOfCustomerActivity.class);
                startActivity(i);

            }
        });

        binding.btnNearBy.setOnClickListener(v->{
            Intent i = new Intent(this, NearByCustomersActivity.class);
            startActivity(i);
        });
    }
}