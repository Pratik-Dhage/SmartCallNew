package com.example.test.npa_flow.details_of_customer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.test.R;
import com.example.test.databinding.ActivityDetailsOfCustomerBinding;
import com.example.test.fragments_activity.BalanceInterestCalculationActivity;
import com.example.test.fragments_activity.CustomerDetailsActivity;
import com.example.test.helper_classes.Global;
import com.example.test.helper_classes.NetworkUtilities;
import com.example.test.npa_flow.CallDetailOfCustomerActivity;
import com.example.test.npa_flow.NearByCustomersActivity;
import com.example.test.npa_flow.WebViewActivity;
import com.example.test.npa_flow.details_of_customer.DetailsOfCustomerViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DetailsOfCustomerActivity extends AppCompatActivity {

    ActivityDetailsOfCustomerBinding binding;
    View view;
    DetailsOfCustomerViewModel detailsOfCustomerViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_details_of_customer);

        initializeFields();
        onClickListener();
         getDetailsOfCustomerDataFromApi(); // will act as initObserver


    }

    private void getDetailsOfCustomerDataFromApi(){
        if(getIntent().hasExtra("dataSetId")){

            String dataSetId = getIntent().getStringExtra("dataSetId");

            if(NetworkUtilities.getConnectivityStatus(this)){

                binding.loadingProgressBar.setVisibility(View.VISIBLE);
                detailsOfCustomerViewModel.getDetailsOfCustomer_Data(dataSetId); // call Details Of Customer API

                detailsOfCustomerViewModel.getMutDetailsOfCustomer_ResponseApi().observe(this,result->{

                    if(result!=null){

                        binding.loadingProgressBar.setVisibility(View.INVISIBLE);

                        result.iterator().forEachRemaining(it->{

                            if(it.getSequence()==1){
                                binding.txtName.setText(it.getValue()); //Name
                            }

                            if(it.getSequence()==2){
                                binding.txtVillageName.setText(it.getValue()); //Village Name
                            }


                            if(it.getSequence()==4){
                                binding.txtMobileNumber.setText(it.getValue()); //Mobile No.
                            }

                            if(it.getSequence()==5){
                                binding.txtAadharNumber.setText(it.getValue()); //Aadhaar No.
                            }

                            if(it.getSequence()==6){
                                binding.txtDOB.setText(it.getValue()); //Date of Birth
                            }

                            if(it.getSequence()==7){
                                binding.txtFatherName.setText(it.getValue()); //Father's Name
                            }

                            if(it.getSequence()==8){
                                binding.txtLoanAccountNumber.setText(it.getValue()); //Loan Acc. No.
                            }

                            if(it.getSequence()==9){
                                binding.txtProduct.setText(it.getValue()); //Product
                            }

                            if(it.getSequence()==10){
                                binding.txtAmountDueAsOnAmount.setText(it.getValue()); //Amt. Due as OutStanding Balance
                            }

                            if(it.getSequence()==12){
                                binding.txtTotalAmountPaid.setText(it.getValue()); // Total Amount Paid
                            }

                            if(it.getSequence()==13){
                                binding.txtBalanceInterest.setText(it.getValue()); //Balance Interest
                            }

                            if(it.getSequence()==14){
                                binding.txtTotalPayableAmount.setText(it.getValue()); //Total Payable Amount
                            }

                        });

                    }


                });


                //handle  error response
                detailsOfCustomerViewModel.getMutErrorResponse().observe(this, error -> {

                    if (error != null && !error.isEmpty()) {
                        Global.showSnackBar(view, error);
                        System.out.println("Here: " + error);
                    } else {
                        Global.showSnackBar(view, getResources().getString(R.string.check_internet_connection));
                    }
                });

            }
            else{
                Global.showToast(this,getString(R.string.check_internet_connection));
            }

        }
        else{
            Global.showToast(this,getString(R.string.details_not_found));
        }
    }


    private void initializeFields() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_details_of_customer);
        view = binding.getRoot();
        detailsOfCustomerViewModel = new ViewModelProvider(this).get(DetailsOfCustomerViewModel.class);
        binding.setViewModel(detailsOfCustomerViewModel);

    }

    private void sendDetailsOfCustomer(){

        Intent i = new Intent(DetailsOfCustomerActivity.this, CallDetailOfCustomerActivity.class);
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

                sendDetailsOfCustomer();

            }
        });

        // NearBy == Capture Button
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

   /* private boolean checkTimeFormat(){
        // get the text from the EditText
        String text = binding.edtScheduledTime.getText().toString();

// create a SimpleDateFormat object with the desired format string
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");

        try {
            // try to parse the text as a time
            Date parsedTime = dateFormat.parse(text);

            // if parsing was successful, the text is in the proper time format
            Global.showToast(this,"Text is in proper time format");
            binding.txtScheduledTime.setText(parsedTime.toString());

            return true;
        } catch (ParseException e) {
            // if parsing failed, the text is not in the proper time format
            Global.showToast(this,"Text is not in proper time format");
            return false;
        }


    }*/
}