package com.example.test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;

import com.example.test.databinding.ActivityLeadsBinding;

public class LeadsActivity extends AppCompatActivity {

    ActivityLeadsBinding binding;
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initializeFields();
        onClickListener();
    }

    private void initializeFields() {

        binding = DataBindingUtil.setContentView(this,R.layout.activity_leads);
        view = binding.getRoot();
    }

    private void onClickListener() {
    }
}