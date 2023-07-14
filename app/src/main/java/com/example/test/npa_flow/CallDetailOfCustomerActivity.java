package com.example.test.npa_flow;

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
import com.example.test.databinding.ActivityCallDetailOfCustomerBinding;
import com.example.test.fragments_activity.BalanceInterestCalculationActivity;
import com.example.test.helper_classes.Global;
import com.example.test.helper_classes.NetworkUtilities;
import com.example.test.main_dashboard.MainActivity3API;
import com.example.test.npa_flow.details_of_customer.DetailsOfCustomerActivity;
import com.example.test.npa_flow.details_of_customer.DetailsOfCustomerResponseModel;
import com.example.test.npa_flow.details_of_customer.DetailsOfCustomerViewModel;
import com.example.test.npa_flow.details_of_customer.adapter.DetailsOfCustomerAdapter;
import com.example.test.schedule_flow.calls_for_the_day.adapter.CallsForTheDayAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CallDetailOfCustomerActivity extends AppCompatActivity {

    ActivityCallDetailOfCustomerBinding binding;
    View view ;
    DetailsOfCustomerViewModel detailsOfCustomerViewModel;
    ArrayList<DetailsOfCustomerResponseModel> detailsList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     //   setContentView(R.layout.activity_call_detail_of_customer);


        initializeFields();
        initObserver();
        if(NetworkUtilities.getConnectivityStatus(this)){
            callDetailsOfCustomerApi();
        }
        else{
            Global.showToast(this,getString(R.string.check_internet_connection));
        }
        onClickListener();

    }

    private void setToolBarTitle(){
        if(CallsForTheDayAdapter.isFromCallsForTheDayAdapter!=null || Global.getStringFromSharedPref(this,"isFromCallsForTheDayAdapter")!=null){
            binding.txtToolbarHeading.setText(R.string.calls_for_the_day_npa_details);
        }
    }

    private void initializeFields() {

        binding = DataBindingUtil.setContentView(this, R.layout.activity_call_detail_of_customer);
        view = binding.getRoot();
        detailsOfCustomerViewModel = new ViewModelProvider(this).get(DetailsOfCustomerViewModel.class);
        binding.setViewModel(detailsOfCustomerViewModel);
        setToolBarTitle();
    }

    private void callDetailsOfCustomerApi(){

        String dataSetId = getIntent().getStringExtra("dataSetId");
        detailsOfCustomerViewModel.getDetailsOfCustomer_Data(dataSetId); // call Details Of Customer API
    }


    private void setUpDetailsOfCustomerRecyclerView(){

        detailsOfCustomerViewModel.updateDetailsOfCustomer_Data();
        RecyclerView recyclerView = binding.rvDetailsOfCustomer;
        recyclerView.setAdapter(new DetailsOfCustomerAdapter(detailsOfCustomerViewModel.arrList_DetailsOfCustomer_Data));
    }

    private void initObserver(){

        if(NetworkUtilities.getConnectivityStatus(this)){

            binding.loadingProgressBar.setVisibility(View.VISIBLE);

            detailsOfCustomerViewModel.getMutDetailsOfCustomer_ResponseApi().observe(this,result->{

                if(result!=null) {

                    detailsList = (ArrayList<DetailsOfCustomerResponseModel>) result;

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




    private void onClickListener() {

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.ivHome.setOnClickListener(v->{
            startActivity(new Intent(this, MainActivity3API.class));
        });

        binding.btnSpokeToCustomer.setOnClickListener(v -> {

                    //From CallsForTheDayAdapter
                    if(CallsForTheDayAdapter.isFromCallsForTheDayAdapter!=null || Global.getStringFromSharedPref(this,"isFromCallsForTheDayAdapter")!=null){
                        System.out.println("Here isFromCallsForTheDayAdapter_CallDetailOfCustomerActivity");
                        Intent i = new Intent(this, PaymentNotificationOfCustomerActivity.class);
                        i.putExtra("dataSetId",getIntent().getStringExtra("dataSetId"));
                        i.putExtra("isFromCallsForTheDayAdapter","isFromCallsForTheDayAdapter");
                        i.putExtra("detailsList",detailsList);
                        startActivity(i);
                    }

                    //From NPA (Assigned)
                    else{
                        System.out.println("Here fromNPA_CallDetailOfCustomerActivity");
                        Intent i = new Intent(this, PaymentNotificationOfCustomerActivity.class);
                        i.putExtra("dataSetId",getIntent().getStringExtra("dataSetId"));
                        i.putExtra("detailsList",detailsList);
                        startActivity(i);
                    }

        });

        binding.btnNotSpokeToCustomer.setOnClickListener(v->{

            //From CallsForTheDayAdapter
            if(CallsForTheDayAdapter.isFromCallsForTheDayAdapter!=null || Global.getStringFromSharedPref(this,"isFromCallsForTheDayAdapter")!=null){
                System.out.println("Here isFromCallsForTheDayAdapter");
                Intent i = new Intent(CallDetailOfCustomerActivity.this,NotSpokeToCustomerActivity.class);
                i.putExtra("dataSetId",getIntent().getStringExtra("dataSetId"));
                i.putExtra("isFromCallsForTheDayAdapter","isFromCallsForTheDayAdapter");
                i.putExtra("detailsList",detailsList);
                startActivity(i);
            }

            //From NPA (Assigned)
            else{
                System.out.println("Here fromNPA_CallDetailOfCustomerActivity");
                Intent i = new Intent(CallDetailOfCustomerActivity.this,NotSpokeToCustomerActivity.class);
                i.putExtra("dataSetId",getIntent().getStringExtra("dataSetId"));
                i.putExtra("detailsList",detailsList);
                startActivity(i);
            }


        });



        //for Editing Time
      /*  binding.ivEditTime.setOnClickListener(v->{

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

        });*/



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

    // For Getting Calculated Balance Interest Result back from SharedPreference
    @Override
    protected void onResume() {
        initializeFields();
        onClickListener();
        initObserver();
        callDetailsOfCustomerApi();
        super.onResume();
    }
}