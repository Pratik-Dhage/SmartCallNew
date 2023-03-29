package com.example.test.npa_flow;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.test.R;
import com.example.test.databinding.ActivityDpdactivityBinding;

public class DPDActivity extends AppCompatActivity {


    ActivityDpdactivityBinding binding;
    View view ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_dpdactivity);

        initializeFields();
        onClickListener();
    }

    private void initializeFields() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dpdactivity);
        view = binding.getRoot();
    }

    private void onClickListener() {

        binding.MainConstraint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(DPDActivity.this,LoanCollectionActivity.class);
                startActivity(i);

            }
        });

        binding.ivBack.setOnClickListener(v->{
            onBackPressed();
        });
    }


}