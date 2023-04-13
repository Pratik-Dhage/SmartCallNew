package com.example.test.npa_flow;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.DatePicker;

import com.example.test.MainActivity3;
import com.example.test.R;
import com.example.test.databinding.ActivityScheduleVisitForCollectionBinding;
import com.example.test.helper_classes.Global;
import com.example.test.main_dashboard.MainActivity3API;

import java.util.Calendar;

public class ScheduleVisitForCollectionActivity extends AppCompatActivity {

    ActivityScheduleVisitForCollectionBinding binding;
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setContentView(R.layout.activity_schedule_visit_for_collection);

        initializeFields();
        onClickListener();
    }

    private void initializeFields() {

        binding = DataBindingUtil.setContentView(this,R.layout.activity_schedule_visit_for_collection);
        view = binding.getRoot();

        //for current date
        DatePicker datePicker = binding.datePickerCalendarView;
        Calendar calendar = Calendar.getInstance();
        datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), null);

        setUpTitleAndButtonText();
    }

    private void setUpTitleAndButtonText(){

        if(getIntent().hasExtra("isFromPaymentNotificationOfCustomerActivity")){
           binding.labelScheduleVisit.setText(getString(R.string.schedule_call));
           binding.btnUpdateSchedule.setText(getString(R.string.update));
        }
    }

    private void onClickListener() {

        binding.btnUpdateSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Global.showToast(ScheduleVisitForCollectionActivity.this,getResources().getString(R.string.schedule_update_successfully));

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(ScheduleVisitForCollectionActivity.this, MainActivity3API.class);
                        startActivity(intent);
                    }
                }, 2000);


            }
        });

    }
}