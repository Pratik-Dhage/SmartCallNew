package com.example.test.npa_flow.status_of_customer;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test.R;
import com.example.test.databinding.ActivityStatusOfCustomerBinding;
import com.example.test.helper_classes.Global;
import com.example.test.helper_classes.NetworkUtilities;
import com.example.test.npa_flow.status_of_customer.adapter.StatusOfCustomerDetailsAdapter;
import com.example.test.schedule_flow.adapter.ScheduleDetailsAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class StatusOfCustomerActivity extends AppCompatActivity {


    ActivityStatusOfCustomerBinding binding;
    View view;
    StatusOfCustomerViewModel statusOfCustomerViewModel;
    View customDialogSearchDate;
    public static String fromDate;
    public static String toDate;
    public String searchedDateRange;
    boolean isCalendarClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_of_customer);

        initializeFields();
        if(NetworkUtilities.getConnectivityStatus(this)){
            callStatusDetailsOfCustomerApi();
            initObserver();
        }
        else{
            Global.showSnackBar(view,getResources().getString(R.string.check_internet_connection));
        }

        onClickListener();
    }



    private void initializeFields() {
        binding = DataBindingUtil.setContentView(this,R.layout.activity_status_of_customer);
        view = binding.getRoot();
        statusOfCustomerViewModel = new ViewModelProvider(this).get(StatusOfCustomerViewModel.class);
        binding.setViewModel(statusOfCustomerViewModel);

        // Is Calendar icon Clicked for Searching records
        if(isCalendarClicked && searchedDateRange!=null){
            binding.txtSearchedDateRange.setVisibility(View.VISIBLE);
            binding.txtClearAll.setVisibility(View.VISIBLE);
        }
        else{
            binding.txtSearchedDateRange.setVisibility(View.GONE);
            binding.txtClearAll.setVisibility(View.GONE);
        }
    }

    private void callStatusDetailsOfCustomerApi() {

        String dataSetId = getIntent().getStringExtra("dataSetId");
        statusOfCustomerViewModel.getDetailsOfStatusOfCustomerData(dataSetId,"","");
    }

    private void setUpStatusDetailsRecyclerViewData(){
        statusOfCustomerViewModel.updateStatusOfCustomerData();
        RecyclerView recyclerView = binding.rvStatusOfCustomerDetails;
        recyclerView.setAdapter(new StatusOfCustomerDetailsAdapter(statusOfCustomerViewModel.arrListActivityData));

    }


    private void initObserver(){

        statusOfCustomerViewModel.getMutActivityOfStatusResponseApi().observe(this,result->{

            if(NetworkUtilities.getConnectivityStatus(this)){


                if(result!=null){

                    statusOfCustomerViewModel.arrListActivityData.clear();
                    setUpStatusDetailsRecyclerViewData();
                    statusOfCustomerViewModel.arrListActivityData.addAll(result);


                }




                //handle  error response
                statusOfCustomerViewModel.getMutErrorResponse().observe(this, error -> {

                    if (error != null && !error.isEmpty()) {
                        Global.showSnackBar(view, error);
                        System.out.println("Here: " + error);
                    } else {
                        Global.showSnackBar(view, getResources().getString(R.string.check_internet_connection));
                    }
                });
            }

            else{
                Global.showSnackBar(view,getResources().getString(R.string.check_internet_connection));
            }

        });

    }

    private void onClickListener(){

        binding.ivBack.setOnClickListener(v->{
            onBackPressed();
        });

        binding.txtClearAll.setOnClickListener(v->{
            //Clear Searched Records And Show Unfiltered List Of Records
            binding.txtClearAll.setVisibility(View.GONE);
            binding.txtSearchedDateRange.setVisibility(View.GONE);
            callStatusDetailsOfCustomerApi();
            initObserver();
        });

         // fro Searching Record
        binding.ivCalendar.setOnClickListener(v -> {

            isCalendarClicked = true;

            customDialogSearchDate = LayoutInflater.from(this).inflate(R.layout.custom_dialog_search_date, null);
            Button btnSearch = customDialogSearchDate.findViewById(R.id.btnSearch);
            EditText edtFromDate = customDialogSearchDate.findViewById(R.id.edtFromDate);
            EditText edtToDate = customDialogSearchDate.findViewById(R.id.edtToDate);
            TextView txtDateError = customDialogSearchDate.findViewById(R.id.txtDateError);
            ImageView ivClose =customDialogSearchDate.findViewById(R.id.ivClose);
            ImageView ivResetDate =customDialogSearchDate.findViewById(R.id.ivResetDate);


            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(customDialogSearchDate);
            final AlertDialog dialog = builder.create();
            dialog.setCancelable(true);
            dialog.show();

            //for FromDate
            edtFromDate.setOnClickListener(v1->{
                showDatePickerDialogAndSetDate(edtFromDate);
            });

            edtFromDate.setOnFocusChangeListener((v2,hasFocus)->{
                if(hasFocus){
                    showDatePickerDialogAndSetDate(edtFromDate);}
            });

            //for ToDate
            edtToDate.setOnClickListener(v1->{
                showDatePickerDialogAndSetDate(edtToDate);
            });

            edtToDate.setOnFocusChangeListener((v3,hasFocus)->{
                if(hasFocus){
                    showDatePickerDialogAndSetDate(edtToDate);}
            });

            ivClose.setOnClickListener(v4->{
                dialog.dismiss();
            });

            ivResetDate.setOnClickListener(v5->{
                edtFromDate.setText("");
                edtToDate.setText("");
                txtDateError.setVisibility(View.INVISIBLE);
            });

            btnSearch.setOnClickListener(v1 -> {

                fromDate = edtFromDate.getText().toString().trim();
                toDate = edtToDate.getText().toString().trim();

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                sdf.setLenient(false);  // This will make the parsing strict

                try {
                    Date date_FromDate = sdf.parse(fromDate);
                    Date date_ToDate = sdf.parse(toDate);
                    // Date is successfully parsed, and it matches the desired format

                    if(date_FromDate!=null && date_ToDate!=null){
                        searchedDateRange = fromDate+" to "+toDate; // Display Searched Date Range
                        System.out.println("Here Searched Date: "+searchedDateRange );
                    }

                    // Is Calendar icon Clicked for Searching records
                    if(isCalendarClicked && searchedDateRange!=null){
                        binding.txtSearchedDateRange.setVisibility(View.VISIBLE);
                        binding.txtSearchedDateRange.setText(searchedDateRange);
                        binding.txtClearAll.setVisibility(View.VISIBLE);
                    }
                    else{
                        binding.txtSearchedDateRange.setVisibility(View.GONE);
                        binding.txtClearAll.setVisibility(View.GONE);
                    }


                    dialog.dismiss();

                    //Get Schedule Details According to fromDate and toDate
                    String dataSetId = getIntent().getStringExtra("dataSetId");
                    statusOfCustomerViewModel.getDetailsOfStatusOfCustomerData(dataSetId,fromDate,toDate);

                } catch (ParseException e) {

                    if(fromDate.isEmpty()){
                        txtDateError.setVisibility(View.VISIBLE);
                        txtDateError.setText(R.string.please_select_from_date);
                    }
                    else  if(toDate.isEmpty()){
                        txtDateError.setVisibility(View.VISIBLE);
                        txtDateError.setText(R.string.please_select_to_date);
                    }

                }

            });

        });
    }

    private void showDatePickerDialogAndSetDate(final EditText editText) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Set the selected date to the EditText
                        String selectedDate = dayOfMonth + "-" + (month + 1) + "-" + year;
                        editText.setText(selectedDate);
                    }
                }, year, month, dayOfMonth);

        // Set the minimum and maximum dates allowed
        //  datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000); // Will start from current date
        // datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 7));

        datePickerDialog.show();
    }
}