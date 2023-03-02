package com.example.test.call_scheduled;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.example.test.R;
import com.example.test.databinding.ActivityCallScheduledBinding;
import com.example.test.databinding.ActivityCallStatusBinding;

public class CallScheduledActivity extends AppCompatActivity {

    ActivityCallScheduledBinding binding;
    View view;
    CallScheduledViewModel callScheduledViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_call_scheduled);


        initializeFields();
        onClickListener();
        callCallScheduledApi();
    }

    private void callCallScheduledApi() {
    }

    private void onClickListener() {
    }

    private void initializeFields() {

          binding = DataBindingUtil.setContentView(this,R.layout.activity_call_scheduled);
           view = binding.getRoot();
           callScheduledViewModel = new ViewModelProvider(this).get(CallScheduledViewModel.class);
           binding.setCallScheduledViewModel(callScheduledViewModel);
    }
}