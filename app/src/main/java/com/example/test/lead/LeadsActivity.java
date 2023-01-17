package com.example.test.lead;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.test.R;
import com.example.test.databinding.ActivityLeadsBinding;
import com.example.test.login.LoginActivity;

public class LeadsActivity extends AppCompatActivity {

    ActivityLeadsBinding binding;
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initializeFields();
        initObserver();
        onClickListener();
    }

    private void initializeFields() {

        binding = DataBindingUtil.setContentView(this, R.layout.activity_leads);
        view = binding.getRoot();
    }

    private void setUpRecyclerLeadListData(){

      RecyclerView recyclerView =  binding.rvLeadActivity;
      //recyclerView.setAdapter();
    }


    private void initObserver(){

    }


    private void onClickListener() {
        binding.leadFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent newLeadIntent = new Intent(LeadsActivity.this,NewLeadDetailsActivity.class);
                startActivity(newLeadIntent);
            }
        });

        binding.txtAddNewLead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.leadFloatingActionButton.performClick();
            }
        });
    }
}