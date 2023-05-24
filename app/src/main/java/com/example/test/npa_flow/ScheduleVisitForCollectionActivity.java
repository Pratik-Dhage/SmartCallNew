package com.example.test.npa_flow;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.example.test.R;
import com.example.test.databinding.ActivityScheduleVisitForCollectionBinding;
import com.example.test.fragments_activity.ActivityOfFragments;
import com.example.test.helper_classes.Global;
import com.example.test.helper_classes.NetworkUtilities;
import com.example.test.npa_flow.call_details.CallDetailsViewModel;
import com.example.test.npa_flow.details_of_customer.DetailsOfCustomerActivity;
import com.example.test.npa_flow.details_of_customer.DetailsOfCustomerResponseModel;
import com.example.test.npa_flow.loan_collection.LoanCollectionActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class ScheduleVisitForCollectionActivity extends AppCompatActivity {

    ActivityScheduleVisitForCollectionBinding binding;
    View view;
    ArrayList<DetailsOfCustomerResponseModel> detailsList;
    CallDetailsViewModel callDetailsViewModel;
    public static String scheduleVisitForCollection_dateTime  ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  setContentView(R.layout.activity_schedule_visit_for_collection);

        initializeFields();
        onClickListener();

        initObserver();
    }

    private void initializeFields() {

        binding = DataBindingUtil.setContentView(this, R.layout.activity_schedule_visit_for_collection);
        view = binding.getRoot();

        callDetailsViewModel = new ViewModelProvider(this).get(CallDetailsViewModel.class);


        //for current date
        DatePicker datePicker = binding.datePickerCalendarView;
        Calendar calendar = Calendar.getInstance();
        long today = calendar.getTimeInMillis(); //for min date
        datePicker.setMinDate(today);
        datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), null);

        //get detailsList
        detailsList = (ArrayList<DetailsOfCustomerResponseModel>) getIntent().getSerializableExtra("detailsList");

        setUpTitleAndButtonText();
    }

    private void initObserver(){

        if(NetworkUtilities.getConnectivityStatus(this)) {
            callDetailsViewModel.getMutCallDetailsResponseApi().observe(this, result -> {

                if(result!=null){
                    Global.showToast(this,"Server Response:"+result);
                }
                if(result==null){
                    Global.showToast(this,"Server Response: Null");
                }

            });

            //to handle error
            callDetailsViewModel.getMutErrorResponse().observe(this,error->{
                if (error != null && !error.isEmpty()) {
                    Global.showSnackBar(view, error);
                    System.out.println("Here error : " + error);
                    //Here error : End of input at line 1 column 1 path $ (if Server response body is empty, we get this error)
                }
            });

        }
        else{
            Global.showSnackBar(view,getString(R.string.check_internet_connection));
        }
    }

    private void setUpTitleAndButtonText() {

        if (getIntent().hasExtra("isFromPaymentNotificationOfCustomerActivity")
                || getIntent().hasExtra("isFromPaymentModeStatusActivity")
                || getIntent().hasExtra("isCall") || getIntent().hasExtra("isFromPaymentInfoOfCustomerActivity")

        ) {
            binding.labelScheduleVisit.setText(getString(R.string.schedule_call));
            binding.btnUpdateSchedule.setText(getString(R.string.update));
        }

        if (getIntent().hasExtra("isFromPaymentInfoOfCustomerActivity")) {
            binding.btnWillPayLumpsum.setVisibility(View.VISIBLE);
        }

        if(getIntent().hasExtra("isFromVisitNPANotificationActivity") || getIntent().hasExtra("isFromVisitNPANotAvailableActivity")){
            binding.btnSkipAndProceed.setVisibility(View.VISIBLE);

            binding.btnSkipAndProceed.setOnClickListener(v->{

                Intent i = new Intent(this,VisitCompletionOfCustomerActivity.class);
                i.putExtra("dataSetId", getIntent().getStringExtra("dataSetId"));

                i.putExtra("isFromVisitNPANotAvailableActivity","isFromVisitNPANotAvailableActivity");
                startActivity(i);
            });
        }
    }


    private void onClickListener() {

        binding.ivBack.setOnClickListener(v->{
            onBackPressed();
        });

        binding.btnUpdateSchedule.setOnClickListener(v -> {

            Global.showToast(ScheduleVisitForCollectionActivity.this, getResources().getString(R.string.schedule_update_successfully));

            //on Clicking update call this api   callDetailsViewModel.postCallDetails with ScheduleDateTime
            //in pattern   String pattern = "yyyy-MM-dd HH:mm:ss"; // Pattern to match the date format
            if(  binding.btnUpdateSchedule.getText()==getString(R.string.update)){

                //get scheduleDateTime
                  getScheduleDateTime();

                String dataSetId = getIntent().getStringExtra("dataSetId");
                String will_pay_later = "will pay later";

                //using payment type as will pay later
                callDetailsViewModel.postScheduledDateTime(dataSetId,will_pay_later,scheduleVisitForCollection_dateTime);

            }

            //Payment Mode -> Schedule Visit For Collection (SVFC)
            if(binding.btnUpdateSchedule.getText()==getString(R.string.update_schedule_space)){

                //get scheduleDateTime
                getScheduleDateTime();
                String dataSetId = getIntent().getStringExtra("dataSetId");

                callDetailsViewModel.postScheduledDateTime_SVFC(dataSetId,scheduleVisitForCollection_dateTime);

            }


            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    if(getIntent().hasExtra("isFromVisitNPANotAvailableActivity")){
                        Intent i = new Intent(ScheduleVisitForCollectionActivity.this, ActivityOfFragments.class);
                        startActivity(i);
                    }

                    if(getIntent().hasExtra("isWillPayLater")){

                        //then redirect to LoanCollection List
                        Intent i = new Intent(new Intent(ScheduleVisitForCollectionActivity.this, LoanCollectionActivity.class));
                        int DPD_row_position = Integer.parseInt(Global.getStringFromSharedPref(ScheduleVisitForCollectionActivity.this,"DPD_row_position"));
                        i.putExtra("DPD_row_position",DPD_row_position);
                        startActivity(i);
                    }

                    else{

                        Intent intent = new Intent(ScheduleVisitForCollectionActivity.this, NearByCustomersActivity.class);
                        startActivity(intent);
                    }

                }
            }, 2000);


        });

        binding.btnWillPayLumpsum.setOnClickListener(v -> {

            //get Details from PaymentInfoOfCustomerActivity and Pass To VisitCompletionOfCustomerActivity

            Intent i = new Intent(this, VisitCompletionOfCustomerActivity.class);
            i.putExtra("dataSetId", getIntent().getStringExtra("dataSetId"));
            i.putExtra("detailsList",detailsList);
            startActivity(i);
        });

    }

    private void getScheduleDateTime(){

        DatePicker datePicker = findViewById(R.id.datePickerCalendarView);
        TimePicker timePicker = findViewById(R.id.timePicker);

// Get the selected date from the DatePicker
        int year = datePicker.getYear();
        int month = datePicker.getMonth(); // Note: month starts from 0
        int dayOfMonth = datePicker.getDayOfMonth();

// Get the selected time from the TimePicker
        int hour, minute;
        // if android version > Marshmallow
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            hour = timePicker.getHour();
            minute = timePicker.getMinute();
        } else {
            hour = timePicker.getCurrentHour();
            minute = timePicker.getCurrentMinute();
        }

// Create a Calendar instance and set the selected date and time
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth, hour, minute);

// Retrieve the date and time in the desired format
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        scheduleVisitForCollection_dateTime = dateFormat.format(calendar.getTime());

        System.out.println("Here scheduledDateTime:"+scheduleVisitForCollection_dateTime);
  // Now, dateTime variable contains the selected date and time in the desired format
     //  String scheduleVisitForCollection_dateTime_new  = scheduleVisitForCollection_dateTime; //had to save in new String

        if(!Global.getStringFromSharedPref(this,"scheduleVisitForCollection_dateTime").isEmpty()){
            Global.removeStringInSharedPref(this,"scheduleVisitForCollection_dateTime");
        }

            Global.saveStringInSharedPref(this,"scheduleVisitForCollection_dateTime",scheduleVisitForCollection_dateTime);


    }

}