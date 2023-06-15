package com.example.test.call_status;

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
import com.example.test.databinding.ActivityCallStatusWithLeadInteractionBinding;
import com.example.test.helper_classes.Global;
import com.example.test.lead.LeadsActivity;
import com.example.test.view_products.ViewProductsActivity;

public class CallStatusWithLeadInteractionActivity extends AppCompatActivity {

    ActivityCallStatusWithLeadInteractionBinding binding;
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setContentView(R.layout.activity_call_status_with_lead_interaction);

        initializeFields();
        setUpData();
        onClickListener();
    }

    private void initializeFields() {
        binding = DataBindingUtil. setContentView(this,R.layout.activity_call_status_with_lead_interaction);
        view = binding.getRoot();

    }

    private void setUpData(){
        String firstName = getIntent().getStringExtra("firstName");
        String phoneNumber = getIntent().getStringExtra("phoneNumber");

        binding.txtLeadName.setText(firstName);
        binding.txtLeadMobileNumber.setText(phoneNumber);
    }

    private void onClickListener() {

        binding.ivBack.setOnClickListener(v -> {
            onBackPressed();
        });

        binding.btnViewProducts.setOnClickListener(v->{
            startActivity(new Intent(CallStatusWithLeadInteractionActivity.this, ViewProductsActivity.class));
        });

        binding.btnBackToLeadList.setOnClickListener(v->{
            startActivity(new Intent(CallStatusWithLeadInteractionActivity.this, LeadsActivity.class));
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