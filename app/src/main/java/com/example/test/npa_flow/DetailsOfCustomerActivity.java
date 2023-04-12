package com.example.test.npa_flow;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.test.R;
import com.example.test.databinding.ActivityDetailsOfCustomerBinding;
import com.example.test.fragments_activity.BalanceInterestCalculationActivity;
import com.example.test.fragments_activity.CustomerDetailsActivity;
import com.example.test.helper_classes.Global;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DetailsOfCustomerActivity extends AppCompatActivity {

    ActivityDetailsOfCustomerBinding binding;
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_details_of_customer);

        initializeFields();
        onClickListener();

    }

    private void initializeFields() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_details_of_customer);
        view = binding.getRoot();

    }

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

                Intent i = new Intent(DetailsOfCustomerActivity.this,CallDetailOfCustomerActivity.class);
                startActivity(i);

            }
        });

        binding.btnNearBy.setOnClickListener(v->{
            Intent i = new Intent(this, NearByCustomersActivity.class);
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

   /* private boolean checkTimeFormat(){
        // get the text from the EditText
        String text = binding.edtScheduledTime.getText().toString();

// create a SimpleDateFormat object with the desired format string
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");

        try {
            // try to parse the text as a time
            Date parsedTime = dateFormat.parse(text);

            // if parsing was successful, the text is in the proper time format
            Global.showToast(this,"Text is in proper time format");
            binding.txtScheduledTime.setText(parsedTime.toString());

            return true;
        } catch (ParseException e) {
            // if parsing failed, the text is not in the proper time format
            Global.showToast(this,"Text is not in proper time format");
            return false;
        }


    }*/
}