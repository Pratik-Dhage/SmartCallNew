package com.example.test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.test.databinding.ActivityMainBinding;


//This is Dashboard Activity
public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initializeFields();
        onClickListener();
    }

    private void initializeFields() {
     binding = DataBindingUtil.setContentView(this,R.layout.activity_main) ;
      view = binding.getRoot();
    }

    private void onClickListener() {
        binding.labelMarketing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               Intent i = new Intent(MainActivity.this,LeadsActivity.class);
               startActivity(i);
            }
        });

    }


}