package com.example.test.npa_flow.details_of_customer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.test.R;
import com.example.test.databinding.ActivityDetailsOfCustomer2Binding;
import com.example.test.databinding.ActivityDetailsOfCustomerBinding;
import com.example.test.fragments_activity.BalanceInterestCalculationActivity;
import com.example.test.fragments_activity.CustomerDetailsActivity;
import com.example.test.helper_classes.Global;
import com.example.test.helper_classes.NetworkUtilities;
import com.example.test.npa_flow.CallDetailOfCustomerActivity;
import com.example.test.npa_flow.NearByCustomersActivity;
import com.example.test.npa_flow.WebViewActivity;
import com.example.test.npa_flow.details_of_customer.DetailsOfCustomerViewModel;
import com.example.test.npa_flow.details_of_customer.adapter.DetailsOfCustomerAdapter;
import com.example.test.npa_flow.dpd.adapter.DPD_Adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DetailsOfCustomerActivity extends AppCompatActivity {

    ActivityDetailsOfCustomer2Binding binding;
    View view;
    DetailsOfCustomerViewModel detailsOfCustomerViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_details_of_customer);

        initializeFields();
        onClickListener();
       //  getDetailsOfCustomerDataFromApi(); // will act as initObserver

        initObserver();
        if(NetworkUtilities.getConnectivityStatus(this)){
            callDetailsOfCustomerApi();
        }
        else{
            Global.showToast(this,getString(R.string.check_internet_connection));
        }


    }

    private void callDetailsOfCustomerApi(){

        String dataSetId = getIntent().getStringExtra("dataSetId");
        detailsOfCustomerViewModel.getDetailsOfCustomer_Data(dataSetId); // call Details Of Customer API
    }

    private void initObserver(){

        if(NetworkUtilities.getConnectivityStatus(this)){

            binding.loadingProgressBar.setVisibility(View.VISIBLE);

            detailsOfCustomerViewModel.getMutDetailsOfCustomer_ResponseApi().observe(this,result->{

                if(result!=null) {

                    //for Hiding Amount Paid ONLY in Details Of Customer Activity
                    result.iterator().forEachRemaining(it->{
                        if(it.getSequence()==16 || it.getLable().contentEquals("Amount Paid")){
                             it.setLable("");
                            it.setEditable("");
                        }
                    });

                    detailsOfCustomerViewModel.arrList_DetailsOfCustomer_Data.clear();
                    setUpDetailsOfCustomerRecyclerView();
                    detailsOfCustomerViewModel.arrList_DetailsOfCustomer_Data.addAll(result);
                    binding.loadingProgressBar.setVisibility(View.INVISIBLE);



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

    private void getDetailsOfCustomerDataFromApi(){
        if(getIntent().hasExtra("dataSetId")){

            String dataSetId = getIntent().getStringExtra("dataSetId");

            if(NetworkUtilities.getConnectivityStatus(this)){

                binding.loadingProgressBar.setVisibility(View.VISIBLE);
                detailsOfCustomerViewModel.getDetailsOfCustomer_Data(dataSetId); // call Details Of Customer API

                detailsOfCustomerViewModel.getMutDetailsOfCustomer_ResponseApi().observe(this,result->{

                    if(result!=null){

                        binding.loadingProgressBar.setVisibility(View.INVISIBLE);

                      /*  result.iterator().forEachRemaining(it->{

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

                        });*/

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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_details_of_customer2);
        view = binding.getRoot();
        detailsOfCustomerViewModel = new ViewModelProvider(this).get(DetailsOfCustomerViewModel.class);
        binding.setViewModel(detailsOfCustomerViewModel);

    }

    private void setUpDetailsOfCustomerRecyclerView(){

        detailsOfCustomerViewModel.updateDetailsOfCustomer_Data();
        RecyclerView recyclerView = binding.rvDetailsOfCustomer;
        recyclerView.setAdapter(new DetailsOfCustomerAdapter(detailsOfCustomerViewModel.arrList_DetailsOfCustomer_Data));
    }


    private void onClickListener(){
        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.ivCall.setOnClickListener(v->{
            Intent i = new Intent(this,CallDetailOfCustomerActivity.class);
            i.putExtra("dataSetId",getIntent().getStringExtra("dataSetId"));
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

    // For Getting Calculated Balance Interest Result back from SharedPreference
    @Override
    protected void onResume() {
        initializeFields();
        onClickListener();
        initObserver();
        callDetailsOfCustomerApi();
        super.onResume();
    }

  /*
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

              //  sendDetailsOfCustomer();

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
*/

}