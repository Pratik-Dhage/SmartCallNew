package com.example.test.schedule_flow;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.test.R;
import com.example.test.databinding.ActivityScheduleDetailsBinding;
import com.example.test.helper_classes.Global;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScheduleDetailsActivity extends AppCompatActivity {

    ActivityScheduleDetailsBinding binding;
    View view;
    View customDialogSearchDate;
   public static String fromDate;
   public static String toDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_schedule_details);

    initializeFields();
    onClickListener();

    }

    private void initializeFields() {
        binding = DataBindingUtil.setContentView(this,R.layout.activity_schedule_details);
        view = binding.getRoot();
    }

    private void onClickListener() {
        binding.ivBack.setOnClickListener(v->{
            onBackPressed();
        });


        //for Searching Record of Schedule Visits
        binding.ivCalendar.setOnClickListener(v->{

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

            btnSearch.setOnClickListener(v1->{

                fromDate = edtFromDate.getText().toString().trim();
                 toDate = edtToDate.getText().toString().trim();

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                sdf.setLenient(false);  // This will make the parsing strict

                try {
                    Date date_FromDate = sdf.parse(fromDate);
                    Date date_ToDate = sdf.parse(toDate);
                    // Date is successfully parsed, and it matches the desired format
                    Global.showToast(this,"Correct Date format");

                } catch (ParseException e) {
                   txtDateError.setVisibility(View.VISIBLE);
                }

            });

        });
    }



}