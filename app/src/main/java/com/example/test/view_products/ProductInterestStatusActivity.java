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
import com.example.test.databinding.ActivityProductInterestStatusBinding;
import com.example.test.helper_classes.Global;
import com.example.test.lead.LeadsActivity;
import com.example.test.main_dashboard.MainActivity3API;

public class ProductInterestStatusActivity extends AppCompatActivity {

   ActivityProductInterestStatusBinding binding;
   View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_interest_status);

        initializeFields();
        onClickListener();

    }

    private void initializeFields() {
        binding = DataBindingUtil.setContentView(this,R.layout.activity_product_interest_status);
        view = binding.getRoot();

        binding.txtLeadName.setText(getIntent().getStringExtra("firstName"));
        binding.txtLeadMobileNumber.setText(getIntent().getStringExtra("phoneNumber"));
    }

    private void onClickListener() {

        binding.ivBack.setOnClickListener(v -> {
            onBackPressed();
        });

        binding.ivHome.setOnClickListener(v->{
            startActivity(new Intent(this, MainActivity3API.class));
        });

        binding.btnIsInterested.setOnClickListener(v->{
            Intent i = new Intent(this, LeadsActivity.class);
            startActivity(i);
        });

        //for Notes
        binding.ivNotesIcon.setOnClickListener(v->{

            Global.showNotesEditDialog(this);


        });

        //for History
        binding.ivHistory.setOnClickListener(v->{

            String dataSetId = getIntent().getStringExtra("dataSetId");
            Global.showNotesHistoryDialog(this,dataSetId);

        });

    }
}