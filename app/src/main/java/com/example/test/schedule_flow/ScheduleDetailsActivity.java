package com.example.test.schedule_flow;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.test.R;
import com.example.test.databinding.ActivityScheduleDetailsBinding;
import com.example.test.helper_classes.Global;
import com.example.test.helper_classes.NetworkUtilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ScheduleDetailsActivity extends AppCompatActivity {

    ActivityScheduleDetailsBinding binding;
    View view;
    ScheduleDetailsViewModel scheduleDetailsViewModel;

    View customDialogSearchDate;
    public static String fromDate;
    public static String toDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_schedule_details);

        initializeFields();
        onClickListener();
        initObserver();
        if(NetworkUtilities.getConnectivityStatus(this)){
           call_ScheduleDetailsApi();
        }
        else {
            Global.showSnackBar(view, getString(R.string.check_internet_connection));
        }
    }

    private void call_ScheduleDetailsApi(){
        //fromDate=null , toDate=null will give All Schedule Details by default
        scheduleDetailsViewModel.get_ScheduleDetails_Data(null,null);
    }

    private void initializeFields() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_schedule_details);
        view = binding.getRoot();
        scheduleDetailsViewModel = new ViewModelProvider(this).get(ScheduleDetailsViewModel.class);
        binding.setViewModel(scheduleDetailsViewModel);
    }

    private void initObserver() {

        if (NetworkUtilities.getConnectivityStatus(this)) {

           scheduleDetailsViewModel.getMutActivity_ResponseApi().observe(this,result->{

               if(result!=null){
                   Global.showToast(this, String.valueOf(result.getActivityDetails().size()));
               }

           });


        } else {
            Global.showSnackBar(view, getString(R.string.check_internet_connection));
        }


    }


    private void onClickListener() {
        binding.ivBack.setOnClickListener(v -> {
            onBackPressed();
        });


        //for Searching Record of Schedule Visits
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
               if(hasFocus)
                showDatePickerDialogAndSetDate(edtFromDate);
            });

            //for ToDate
            edtToDate.setOnFocusChangeListener((v3,hasFocus)->{
                if(hasFocus)
                showDatePickerDialogAndSetDate(edtToDate);
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
                    scheduleDetailsViewModel.get_ScheduleDetails_Data(fromDate,toDate);

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