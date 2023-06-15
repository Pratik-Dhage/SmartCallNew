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
import com.example.test.databinding.ActivityCallStatusWithProductsBinding;
import com.example.test.helper_classes.Global;
import com.example.test.lead.LeadsActivity;
import com.example.test.main_dashboard.MainActivity3API;
import com.example.test.view_products.OffersListActivity;
import com.example.test.view_products.ViewProductsActivity;

public class CallStatusWithProductsActivity extends AppCompatActivity {

    ActivityCallStatusWithProductsBinding binding;
    View view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_call_status_with_products);

        initializeFields();
        setUpData();
        onClickListener();
    }

    private void setUpData(){
        String firstName = getIntent().getStringExtra("firstName");
        String phoneNumber = getIntent().getStringExtra("phoneNumber");

        binding.txtLeadName.setText(firstName);
        binding.txtLeadMobileNumber.setText(phoneNumber);
    }

    private void initializeFields() {

        binding = DataBindingUtil. setContentView(this,R.layout.activity_call_status_with_products);
        view = binding.getRoot();

    }

    private void onClickListener() {

        binding.ivBack.setOnClickListener(v -> {
            onBackPressed();
        });

        binding.ivHome.setOnClickListener(v->{
            startActivity(new Intent(this, MainActivity3API.class));
        });

        binding.labelPreApprovedOffer.setOnClickListener(v->{

            String firstName = binding.txtLeadName.getText().toString();
            String phoneNumber = binding.txtLeadMobileNumber.getText().toString();

            Intent i = new Intent(this, OffersListActivity.class);
            i.putExtra("firstName",firstName);
            i.putExtra("phoneNumber",phoneNumber);
            startActivity(i);
        });

        binding.btnViewProducts.setOnClickListener(v->{

            String firstName = binding.txtLeadName.getText().toString();
            String phoneNumber = binding.txtLeadMobileNumber.getText().toString();

            Intent i = new Intent(CallStatusWithProductsActivity.this,ViewProductsActivity.class) ;
            i.putExtra("firstName",firstName);
            i.putExtra("phoneNumber",phoneNumber);
            startActivity(i);
        });

        binding.btnBackToLeadList.setOnClickListener(v->{
            startActivity(new Intent(CallStatusWithProductsActivity.this, LeadsActivity.class));
        });

        binding.btnSpokeToCustomer.setOnClickListener(v->{

            String firstName = binding.txtLeadName.getText().toString();
            String phoneNumber = binding.txtLeadMobileNumber.getText().toString();


            Intent i = new Intent(CallStatusWithProductsActivity.this,CallStatusWithLeadInteractionActivity.class) ;
            i.putExtra("firstName",firstName);
            i.putExtra("phoneNumber",phoneNumber);
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