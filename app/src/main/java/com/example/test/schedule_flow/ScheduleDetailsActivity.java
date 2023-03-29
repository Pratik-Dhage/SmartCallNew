package com.example.test.schedule_flow;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;

import com.example.test.R;
import com.example.test.databinding.ActivityScheduleDetailsBinding;

public class ScheduleDetailsActivity extends AppCompatActivity {

    ActivityScheduleDetailsBinding binding;
    View view;

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
    }

}