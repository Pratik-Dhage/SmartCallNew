package com.example.test.fragments_activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;

import com.example.test.R;
import com.example.test.databinding.ActivityVisitCompletionBinding;

public class VisitCompletionActivity extends AppCompatActivity {

    ActivityVisitCompletionBinding binding;
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_visit_completion);

        initializeFields();
    }

    private void initializeFields() {
        binding = DataBindingUtil.setContentView(this,R.layout.activity_visit_completion);
        view = binding.getRoot();
    }


}