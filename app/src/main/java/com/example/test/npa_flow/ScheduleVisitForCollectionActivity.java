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
import com.example.test.npa_flow.loan_collection.LoanCollectionActivity;

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

        if(getIntent().hasExtra("isFromPaymentNotificationOfCustomerActivity")
                || getIntent().hasExtra("isFromPaymentModeStatusActivity")
        || getIntent().hasExtra("isCall") || getIntent().hasExtra("isFromPaymentInfoOfCustomerActivity")

        ){
           binding.labelScheduleVisit.setText(getString(R.string.schedule_call));
           binding.btnUpdateSchedule.setText(getString(R.string.update));
        }

        if(getIntent().hasExtra("isFromPaymentInfoOfCustomerActivity")){
            binding.btnWillPayLumpsum.setVisibility(View.VISIBLE);
        }
    }

    private void onClickListener() {

        binding.btnUpdateSchedule.setOnClickListener(v -> {

            Global.showToast(ScheduleVisitForCollectionActivity.this,getResources().getString(R.string.schedule_update_successfully));

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(ScheduleVisitForCollectionActivity.this, LoanCollectionActivity.class);
                    startActivity(intent);
                }
            }, 2000);


        });

        binding.btnWillPayLumpsum.setOnClickListener(v->{

            //get Details from PaymentInfoOfCustomerActivity and Pass To VisitCompletionOfCustomerActivity

            Intent i = new Intent(this,VisitCompletionOfCustomerActivity.class);
            i.putExtra("name",getIntent().getStringExtra("name"));
            i.putExtra("village_name",getIntent().getStringExtra("village_name"));
            i.putExtra("mobile_no", getIntent().getStringExtra("mobile_no"));
            i.putExtra("aadhaar_no",getIntent().getStringExtra("aadhaar_no"));
            i.putExtra("dob",  getIntent().getStringExtra("dob"));
            i.putExtra("father_name", getIntent().getStringExtra("father_name"));
            i.putExtra("loan_acc_no", getIntent().getStringExtra("loan_acc_no"));
            i.putExtra("product", getIntent().getStringExtra("product"));
            i.putExtra("amt_due", getIntent().getStringExtra("amt_due"));
            i.putExtra("total_amt_paid", getIntent().getStringExtra("total_amt_paid"));
            i.putExtra("balance_interest",getIntent().getStringExtra("balance_interest"));
            i.putExtra("total_payable_amt",getIntent().getStringExtra("total_payable_amt"));
            startActivity(i);
        });

    }
}