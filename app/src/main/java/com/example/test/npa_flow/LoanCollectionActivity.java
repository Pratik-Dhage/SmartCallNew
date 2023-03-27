package com.example.test.npa_flow;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.example.test.R;
import com.example.test.databinding.ActivityLoanCollectionBinding;

public class LoanCollectionActivity extends AppCompatActivity {

    ActivityLoanCollectionBinding binding;
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_loan_collection);

        initializeFields();
        onClickListener();

    }

    private void onClickListener() {
        binding.clLoanCollectionData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoanCollectionActivity.this,DetailsOfCustomerActivity.class);
                startActivity(i);

            }
        });

         //opens Google Maps
        binding.ivMap1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String location = "Mumbai";
                Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + location);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                }
            }
        });

        //opens Google Maps
        binding.ivMap2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.ivMap1.performClick();
            }
        });


        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initializeFields() {
        binding = DataBindingUtil.setContentView(this,R.layout.activity_loan_collection);
        view = binding.getRoot();

    }
}