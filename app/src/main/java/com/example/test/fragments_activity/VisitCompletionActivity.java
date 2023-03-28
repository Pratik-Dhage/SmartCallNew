package com.example.test.fragments_activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.test.R;
import com.example.test.databinding.ActivityVisitCompletionBinding;
import com.example.test.npa_flow.NearByCustomersActivity;

public class VisitCompletionActivity extends AppCompatActivity {

    ActivityVisitCompletionBinding binding;
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_visit_completion);

        initializeFields();
        onClickListener();
    }

    private void initializeFields() {
        binding = DataBindingUtil.setContentView(this,R.layout.activity_visit_completion);
        view = binding.getRoot();
    }

    private void onClickListener(){

        binding.btnVisitCompleteNoChange.setOnClickListener(v->{
            Intent i = new Intent(VisitCompletionActivity.this, NearByCustomersActivity.class);
            startActivity(i);
        });

        binding.btnVisitCompleteUpdateDetails.setOnClickListener(v->{
            binding.btnVisitCompleteNoChange.performClick();
        });

        binding.btnVisitCompleteEscalateToBM.setOnClickListener(v->{
            binding.btnVisitCompleteNoChange.performClick();
        });
    }

}