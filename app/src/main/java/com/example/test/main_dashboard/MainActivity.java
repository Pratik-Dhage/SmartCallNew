package com.example.test.main_dashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.test.R;
import com.example.test.databinding.ActivityMainBinding;
import com.example.test.databinding.ActivityMainUpdatedBinding;
import com.example.test.lead.LeadsActivity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


//This is Dashboard Activity
public class MainActivity extends AppCompatActivity {

    ActivityMainUpdatedBinding binding;
    View view;
    MainDashBoardViewModel mainDashBoardViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initializeFields();
        onClickListener();
        setUpDashboardData();
    }

    private void initializeFields() {
     binding = DataBindingUtil.setContentView(this, R.layout.activity_main_updated) ;
      view = binding.getRoot();
      mainDashBoardViewModel = new ViewModelProvider(this).get(MainDashBoardViewModel.class);
      binding.setViewModel(mainDashBoardViewModel);
    }

    private void setUpDashboardData(){

        //for Date
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, EEEE", Locale.ENGLISH);
        String formattedDate = currentDate.format(formatter);
        binding.txtDate.setText(formattedDate);
    }


    private void onClickListener() {
        binding.labelMarketing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               Intent i = new Intent(MainActivity.this, LeadsActivity.class);
               startActivity(i);
            }
        });

    }


}