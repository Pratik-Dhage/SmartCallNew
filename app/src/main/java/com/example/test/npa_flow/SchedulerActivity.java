package com.example.test.npa_flow;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.test.R;
import com.example.test.databinding.ActivitySchedulerBinding;

public class SchedulerActivity extends AppCompatActivity {

    ActivitySchedulerBinding binding;
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheduler);

        initializeFields();
        onClickListener();

    }

    private void initializeFields() {
       binding = DataBindingUtil.setContentView(this,R.layout.activity_scheduler);
       view = binding.getRoot();
    }

    private void onClickListener() {
        binding.btnNotSure.setOnClickListener(v->{
            startActivity(new Intent(this, VisitCompletionOfCustomerActivity.class));
        });

        binding.btnScheduleVisit.setOnClickListener(v->{
            Intent i = new Intent(this,ScheduleVisitForCollectionActivity.class);
            i.putExtra("isVisit","isVisit");
            startActivity(i);
        });

        binding.btnScheduleCall.setOnClickListener(v->{
            Intent i = new Intent(this,ScheduleVisitForCollectionActivity.class);
            i.putExtra("isCall","isCall");
            startActivity(i);
        });

    }
}