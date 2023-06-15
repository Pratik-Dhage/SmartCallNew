package com.example.test.view_products;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.test.R;
import com.example.test.databinding.ActivityOffersListBinding;
import com.example.test.helper_classes.Global;
import com.example.test.main_dashboard.MainActivity3API;

public class OffersListActivity extends AppCompatActivity {

    ActivityOffersListBinding binding;
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offers_list);

        initializeFields();
        setUpData();
        onClickListener();
    }

    private void initializeFields() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_offers_list);
        view = binding.getRoot();
    }

    private void setUpData() {
        String firstName = getIntent().getStringExtra("firstName");
        String phoneNumber = getIntent().getStringExtra("phoneNumber");

        binding.txtLeadName.setText(firstName);
        binding.txtLeadMobileNumber.setText(phoneNumber);
    }

    private void onClickListener() {

        binding.ivBack.setOnClickListener(v -> {
            onBackPressed();
        });

        binding.ivHome.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity3API.class));
        });


        binding.clViewOtherOffer.setOnClickListener(v -> {
            startActivity(new Intent(this, ViewProductsActivity.class));
        });


        binding.btnSpokeToCustomer.setOnClickListener(v -> {

            if (binding.checkboxCreditCard.isChecked() || binding.checkboxPersonalLoan.isChecked()) {
                String firstName = binding.txtLeadName.getText().toString();
                String phoneNumber = binding.txtLeadMobileNumber.getText().toString();

                Intent i = new Intent(this, ProductInterestStatusActivity.class);
                i.putExtra("firstName", firstName);
                i.putExtra("phoneNumber", phoneNumber);
                startActivity(i);
            }
        });

        //for Notes
        binding.ivNotesIcon.setOnClickListener(v -> {

            Global.showNotesEditDialog(this);

        });

        //for History
        binding.ivHistory.setOnClickListener(v -> {

            String dataSetId = getIntent().getStringExtra("dataSetId");
            Global.showNotesHistoryDialog(this,dataSetId);


        });


    }


}