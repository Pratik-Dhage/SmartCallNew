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
        binding = DataBindingUtil.setContentView(this,R.layout.activity_offers_list);
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

        binding.ivHome.setOnClickListener(v->{
            startActivity(new Intent(this, MainActivity3API.class));
        });


        binding.labelCreditCard.setOnClickListener(v->{

            String firstName = binding.txtLeadName.getText().toString();
            String phoneNumber = binding.txtLeadMobileNumber.getText().toString();

            Intent i = new Intent(this, ProductInterestStatusActivity.class);
            i.putExtra("firstName",firstName);
            i.putExtra("phoneNumber",phoneNumber);
            startActivity(i);

        });

        binding.labelPersonalLoan.setOnClickListener(v->{

            String firstName = binding.txtLeadName.getText().toString();
            String phoneNumber = binding.txtLeadMobileNumber.getText().toString();

            Intent i = new Intent(this, ProductInterestStatusActivity.class);
            i.putExtra("firstName",firstName);
            i.putExtra("phoneNumber",phoneNumber);
            startActivity(i);
        });

        //for Notes
        binding.ivNotesIcon.setOnClickListener(v->{

            View customDialog = LayoutInflater.from(this).inflate(R.layout.custom_dialog_box, null);

            TextView customText =  customDialog.findViewById(R.id.txtCustomDialog);
            Button customButton = customDialog.findViewById(R.id.btnCustomDialog);
            EditText customEditBox = customDialog.findViewById(R.id.edtCustomDialog);
            customEditBox.setVisibility(View.VISIBLE);

            customText.setText(getResources().getString(R.string.lead_interaction));

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(customDialog);
            final AlertDialog dialog = builder.create();
            dialog.show();

            customButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

        });

        //for History
        binding.ivHistory.setOnClickListener(v->{

            View customDialog = LayoutInflater.from(this).inflate(R.layout.custom_dialog_box, null);

            TextView customText =  customDialog.findViewById(R.id.txtCustomDialog);
            Button customButton = customDialog.findViewById(R.id.btnCustomDialog);
            TextView txtCustom = customDialog.findViewById(R.id.txtCustom);
            txtCustom.setVisibility(View.VISIBLE);

            customText.setText(getResources().getString(R.string.lead_history));

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(customDialog);
            final AlertDialog dialog = builder.create();
            dialog.show();

            customButton.setText(R.string.close);
            customButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

        });



    }


}