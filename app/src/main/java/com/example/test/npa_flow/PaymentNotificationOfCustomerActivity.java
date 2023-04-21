package com.example.test.npa_flow;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.test.R;
import com.example.test.databinding.ActivityPaymentNotificationOfCustomerBinding;
import com.example.test.fragments_activity.BalanceInterestCalculationActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class PaymentNotificationOfCustomerActivity extends AppCompatActivity {

    ActivityPaymentNotificationOfCustomerBinding binding;
    View view;
    View customDialogEditable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_payment_notification_of_customer);


        initializeFields();
        getDetailsOfCustomerFromIntent();
        onClickListener();

    }

    private void initializeFields() {

        binding = DataBindingUtil.setContentView(this, R.layout.activity_payment_notification_of_customer);
        view = binding.getRoot();
    }

    private void getDetailsOfCustomerFromIntent(){

        binding.txtName.setText(getIntent().getStringExtra("name"));
        binding.txtVillageName.setText(getIntent().getStringExtra("village_name"));
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

        binding.ivBack.setOnClickListener(v -> onBackPressed());

        binding.btnAskedToCallBackLater.setOnClickListener(v->{

            Intent i = new Intent(PaymentNotificationOfCustomerActivity.this,ScheduleVisitForCollectionActivity.class);
            i.putExtra("isFromPaymentNotificationOfCustomerActivity","isFromPaymentNotificationOfCustomerActivity");
            startActivity(i);

        });

        binding.btnOthers.setOnClickListener(v->{

            customDialogEditable = LayoutInflater.from(this).inflate(R.layout.custom_dialog_editable, null);
            ImageView ivCancel = customDialogEditable.findViewById(R.id.ivCancel);

            Button btnProceed = customDialogEditable.findViewById(R.id.btnProceed);
            EditText edtPleaseSpecify = customDialogEditable.findViewById(R.id.edtPleaseSpecifyName);
            EditText edtPleaseSpecifyContact = customDialogEditable.findViewById(R.id.edtPleaseSpecifyContact);
            edtPleaseSpecify.setHint(getString(R.string.please_specify));
            edtPleaseSpecifyContact.setVisibility(View.GONE);



            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(customDialogEditable);
            final AlertDialog dialog = builder.create();
            dialog.setCancelable(true);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            dialog.show();


            btnProceed.setOnClickListener(v2->{
                Intent i = new Intent(this,VisitCompletionOfCustomerActivity.class);
                i.putExtra("name",binding.txtName.getText().toString());
                i.putExtra("village_name",binding.txtVillageName.getText().toString());
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

            ivCancel.setOnClickListener(v1->{
                dialog.dismiss();
            });


        });

        binding.btnReadyToPay.setOnClickListener(v -> {

            Intent i = new Intent(PaymentNotificationOfCustomerActivity.this,PaymentModeActivity.class);
            i.putExtra("name",binding.txtName.getText().toString());
            i.putExtra("village_name",binding.txtVillageName.getText().toString());
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

        binding.btnNotReadyToPay.setOnClickListener(v->{

            Intent i = new Intent(PaymentNotificationOfCustomerActivity.this,PaymentInfoOfCustomerActivity.class);
            i.putExtra("name",binding.txtName.getText().toString());
            i.putExtra("village_name",binding.txtVillageName.getText().toString());
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


        //NearBy==Capture Button
        binding.btnNearBy.setOnClickListener(v->{
            Intent i = new Intent(this, WebViewActivity.class);
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