package com.example.test.npa_flow.status_of_customer;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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

         // fro Searching Record
        binding.ivCalendar.setOnClickListener(v -> {

            customDialogSearchDate = LayoutInflater.from(this).inflate(R.layout.custom_dialog_search_date, null);
            Button btnSearch = customDialogSearchDate.findViewById(R.id.btnSearch);
            EditText edtFromDate = customDialogSearchDate.findViewById(R.id.edtFromDate);
            EditText edtToDate = customDialogSearchDate.findViewById(R.id.edtToDate);
            TextView txtDateError = customDialogSearchDate.findViewById(R.id.txtDateError);


            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(customDialogSearchDate);
            final AlertDialog dialog = builder.create();
            dialog.setCancelable(true);
            dialog.show();

            //for FromDate
            edtFromDate.setOnFocusChangeListener((v2,hasFocus)->{
                if(hasFocus){
                    showDatePickerDialogAndSetDate(edtFromDate);}
            });

            //for ToDate
            edtToDate.setOnFocusChangeListener((v3,hasFocus)->{
                if(hasFocus){
                    showDatePickerDialogAndSetDate(edtToDate);}
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
                    Global.showToast(this, "Correct Date format");
                    dialog.dismiss();

                    //Get Schedule Details According to fromDate and toDate
                    String dataSetId = getIntent().getStringExtra("dataSetId");
                    statusOfCustomerViewModel.getDetailsOfStatusOfCustomerData(dataSetId,fromDate,toDate);

                } catch (ParseException e) {
                    txtDateError.setVisibility(View.VISIBLE);
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