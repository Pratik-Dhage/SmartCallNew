package com.example.test.schedule_flow;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.test.R;
import com.example.test.databinding.ActivityScheduleDetailsBinding;
import com.example.test.helper_classes.Global;
import com.example.test.helper_classes.NetworkUtilities;
import com.example.test.schedule_flow.adapter.ScheduleDetailsAdapter;
import com.example.test.schedule_flow.model.ScheduleVisitDetails;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ScheduleDetailsActivity extends AppCompatActivity {

   // When ScheduledVisitForCollection Button is clicked in PaymentModeActivity,( date,time,callLog,Member name is stored in Server/Backend)
    //This Activity will contain those stored Scheduled Visits coming from Backend/Api

    ActivityScheduleDetailsBinding binding;
    View view;
    ScheduleDetailsViewModel scheduleDetailsViewModel;

    View customDialogSearchDate;
    public static String fromDate;
    public static String toDate;
    public String searchedDateRange;
    boolean isCalendarClicked = false;

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
        scheduleDetailsViewModel.get_ScheduleDetails_Data("","");
    }

    private void initializeFields() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_schedule_details);
        view = binding.getRoot();
        scheduleDetailsViewModel = new ViewModelProvider(this).get(ScheduleDetailsViewModel.class);
        binding.setViewModel(scheduleDetailsViewModel);

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

    private void setUpScheduledVisitDetailsRecyclerView() {

        scheduleDetailsViewModel.updateScheduledVisitDetails_Data();
        RecyclerView recyclerView = binding.rvScheduleDetails;
        recyclerView.setAdapter(new ScheduleDetailsAdapter(scheduleDetailsViewModel.arrList_scheduledVisitDetails_Data));
    }

    private void initObserver() {

        if (NetworkUtilities.getConnectivityStatus(this)) {

           scheduleDetailsViewModel.getMutActivity_ResponseApi().observe(this,result->{

               if(result!=null){
                //   Global.showToast(this, String.valueOf(result.getActivityDetails().size()));

                   scheduleDetailsViewModel.arrList_scheduledVisitDetails_Data.clear();
                   setUpScheduledVisitDetailsRecyclerView();
              //  System.out.println("ScheduleDetailsList: "+arrangeDates(result));
                   scheduleDetailsViewModel.arrList_scheduledVisitDetails_Data.addAll(result);

               }

           });


            //to handle error
            scheduleDetailsViewModel.getMutErrorResponse().observe(this,error->{
                if (error != null && !error.isEmpty()) {
                    Global.showSnackBar(view, error);
                    System.out.println("Here error : " + error);
                    //Here error : End of input at line 1 column 1 path $ (if Server response body is empty, we get this error)
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

        binding.txtClearAll.setOnClickListener(v->{
            //Clear Searched Records And Show Unfiltered List Of Records
            binding.txtClearAll.setVisibility(View.GONE);
            binding.txtSearchedDateRange.setVisibility(View.GONE);
            call_ScheduleDetailsApi();
            initObserver();
        });


        //for Searching Record of Schedule Visits
        binding.ivCalendar.setOnClickListener(v -> {

            isCalendarClicked = true;

            customDialogSearchDate = LayoutInflater.from(this).inflate(R.layout.custom_dialog_search_date, null);
            Button btnSearch = customDialogSearchDate.findViewById(R.id.btnSearch);
            EditText edtFromDate = customDialogSearchDate.findViewById(R.id.edtFromDate);
            EditText edtToDate = customDialogSearchDate.findViewById(R.id.edtToDate);
            TextView txtDateError = customDialogSearchDate.findViewById(R.id.txtDateError);
            ImageView ivClose =customDialogSearchDate.findViewById(R.id.ivClose);
            ImageView ivResetDate =customDialogSearchDate.findViewById(R.id.ivResetDate);
            TextView labelSearchRecord = customDialogSearchDate.findViewById(R.id.labelSearchRecord);
            labelSearchRecord.setText(getString(R.string.search_by_date_range));


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
                edtFromDate.requestFocus();//for Cursor to be on From Date After ResetDate icon is clicked

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
                    scheduleDetailsViewModel.get_ScheduleDetails_Data(fromDate,toDate);

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

    // Gives List of Dates(with respective list)
    public static  Map<Date, List<ScheduleVisitDetails>> arrangeDates(List<ScheduleVisitDetails> visits) {

        List<Date> dates = new ArrayList<Date>();

        for(ScheduleVisitDetails visit:visits) {

            Date date;

            try {

                date = new SimpleDateFormat("dd-MMM-yyyy").parse(visit.getScheduledDate());

                dates.add(date);

            } catch (ParseException e) {

                e.printStackTrace();
            }

        }

        List<Date> sortedDates = dates.stream().sorted().collect(Collectors.toList());

        Map<Date,List<ScheduleVisitDetails>> groupedVisits = new HashMap<Date, List<ScheduleVisitDetails>>();

        for(Date sortedDate:sortedDates) {

            groupedVisits.put(sortedDate,new ArrayList<ScheduleVisitDetails>());

        }

        for(ScheduleVisitDetails scheduledVistit:visits) {

            Date date;

            try {

                date = new SimpleDateFormat("dd-MMM-yyyy").parse(scheduledVistit.getScheduledDate());

                groupedVisits.get(date).add(scheduledVistit);

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return groupedVisits;


    }

}