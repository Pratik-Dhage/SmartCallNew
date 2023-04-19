package com.example.test.npa_flow;

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
import android.widget.TimePicker;

import com.example.test.R;
import com.example.test.databinding.ActivityNotSpokeToCustomerBinding;
import com.example.test.fragments_activity.BalanceInterestCalculationActivity;
import com.example.test.npa_flow.loan_collection.LoanCollectionActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class NotSpokeToCustomerActivity extends AppCompatActivity {

    ActivityNotSpokeToCustomerBinding binding;
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not_spoke_to_customer);


        initializeFields();
        getDetailsOfCustomerFromIntent();
        onClickListener();

    }

    private void initializeFields() {

        binding = DataBindingUtil.setContentView(this,R.layout.activity_not_spoke_to_customer);
        view = binding.getRoot();

    }

    private void getDetailsOfCustomerFromIntent(){

        binding.txtName.setText(getIntent().getStringExtra("name"));
        binding.txtMobileNumber.setText(getIntent().getStringExtra("mobile_no"));
        binding.txtAadharNumber.setText(getIntent().getStringExtra("aadhaar_no"));
        binding.txtDOB.setText(getIntent().getStringExtra("dob"));
        binding.txtFatherName.setText(getIntent().getStringExtra("father_name"));
        binding.txtLoanAccountNumber.setText(getIntent().getStringExtra("loan_acc_no"));
        binding.txtProduct.setText(getIntent().getStringExtra("product"));
        binding.txtAmountDueAsOnAmount.setText(getIntent().getStringExtra("amt_due"));
        binding.txtTotalAmountPaid.setText(getIntent().getStringExtra("total_amt_paid"));
        binding.txtBalanceInterest.setText(getIntent().getStringExtra("balance_interest"));
        binding.txtTotalPayableAmount.setText(getIntent().getStringExtra("total_payable_amt"));

    }

    private void onClickListener() {

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
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

        //for Editing Time
        binding.ivEditTime.setOnClickListener(v->{

            View customDialog2 = LayoutInflater.from(this).inflate(R.layout.custom_dialog_timepicker, null);
            Button customButton = customDialog2.findViewById(R.id.btnOK);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(customDialog2);
            final AlertDialog dialog = builder.create();
            dialog.setCancelable(false);
            dialog.show();

            //on Clicking OK button
            customButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // get a reference to the TimePicker
                    TimePicker timePicker = customDialog2.findViewById(R.id.customDialogTimePicker);

                    // get the selected hour and minute
                    int hour = timePicker.getHour();
                    int minute = timePicker.getMinute();

                    // create a Date object with the selected time
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.HOUR_OF_DAY, hour);
                    calendar.set(Calendar.MINUTE, minute);
                    Date selectedTime = calendar.getTime();

                    // create a SimpleDateFormat object with the desired format string
                    SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");

                    // format the time and store it in a string
                    String formattedTime = dateFormat.format(selectedTime);

                    // display the selected time
                    binding.txtScheduledTime.setText(":"+formattedTime);

                    dialog.dismiss();
                }
            });

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

            customButton.setOnClickListener(v1 -> dialog.dismiss());

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
            customButton.setOnClickListener(v2 -> dialog.dismiss());



        });


        binding.btnNoResponseBusy.setOnClickListener(v->{
            Intent i = new Intent(NotSpokeToCustomerActivity.this, LoanCollectionActivity.class);
            startActivity(i);

        });

        binding.btnNotReachableSwitchedOff.setOnClickListener(v->{
            Intent i = new Intent(NotSpokeToCustomerActivity.this,LoanCollectionActivity.class);
            startActivity(i);

        });

        binding.btnNumberIsInvalid.setOnClickListener(v->{
            Intent i = new Intent(NotSpokeToCustomerActivity.this,VisitCompletionOfCustomerActivity.class);
            i.putExtra("name",binding.txtName.getText().toString());
            i.putExtra("mobile_no",binding.txtMobileNumber.getText().toString());
            i.putExtra("aadhaar_no",binding.txtAadharNumber.getText().toString());
            i.putExtra("dob",binding.txtDOB.getText().toString());
            i.putExtra("father_name",binding.txtFatherName.getText().toString());
            i.putExtra("loan_acc_no",binding.txtLoanAccountNumber.getText().toString());
            i.putExtra("product",binding.txtProduct.getText().toString());
            i.putExtra("amt_due",binding.txtAmountDueAsOnAmount.getText().toString());
            i.putExtra("total_amt_paid",binding.txtTotalAmountPaid.getText().toString());
            i.putExtra("balance_interest",binding.txtBalanceInterest.getText().toString());
            i.putExtra("total_payable_amt",binding.txtTotalPayableAmount.getText().toString());
            startActivity(i);
        });

    }
}