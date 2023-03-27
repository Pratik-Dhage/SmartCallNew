package com.example.test.npa_flow;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;

import com.example.test.R;
import com.example.test.databinding.ActivityVisitCompletionOfCustomerBinding;

public class VisitCompletionOfCustomerActivity extends AppCompatActivity {


ActivityVisitCompletionOfCustomerBinding binding;
View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setContentView(R.layout.activity_visit_completion_of_customer);

        initializeFields();
        onClickListener();
    }

    private void initializeFields() {

        binding = DataBindingUtil.setContentView(this,R.layout.activity_visit_completion_of_customer);
        view = binding.getRoot();
    }

    private void onClickListener() {

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}