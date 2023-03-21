package com.example.test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.test.api_manager.CommonViewModel;
import com.example.test.api_manager.WebServices;
import com.example.test.databinding.ActivityMainUpdated3Binding;
import com.example.test.fragments_activity.ActivityOfFragments;
import com.example.test.helper_classes.Global;
import com.example.test.lead.model.LeadListResponseModel;
import com.example.test.lead.model.LeadModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MainActivity3 extends AppCompatActivity {

    ActivityMainUpdated3Binding binding;
    View view;
    CommonViewModel commonViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_main_updated3);

        initializeFields();


        onClickListener();
    }

    private void initializeFields() {

        binding = DataBindingUtil.setContentView(this,R.layout.activity_main_updated3);
        view = binding.getRoot();
        commonViewModel = new ViewModelProvider(this).get(CommonViewModel.class);
        binding.setViewModel(commonViewModel);
    }



    private void onClickListener(){

         binding.ivRightArrowVisits.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent i = new Intent(MainActivity3.this, ActivityOfFragments.class);
             startActivity(i);
             }
         });
    }
}