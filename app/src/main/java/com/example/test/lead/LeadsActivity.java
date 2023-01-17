package com.example.test.lead;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.test.R;
import com.example.test.databinding.ActivityLeadsBinding;
import com.example.test.helper_classes.Global;
import com.example.test.helper_classes.NetworkUtilities;
import com.example.test.lead.adapter.LeadListAdapter;
import com.example.test.login.LoginActivity;

public class LeadsActivity extends AppCompatActivity {

    ActivityLeadsBinding binding;
    View view;
    LeadsViewModel leadsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initializeFields();
        initObserver();
        callAPi();
        onClickListener();
    }

    private void initializeFields() {

        binding = DataBindingUtil.setContentView(this, R.layout.activity_leads);
        view = binding.getRoot();
        leadsViewModel =  new ViewModelProvider(this).get(LeadsViewModel.class);
        binding.setLeadViewModel(leadsViewModel);
    }

    private void setUpRecyclerLeadListData(){

        leadsViewModel.updateLeadListData();
      RecyclerView recyclerView =  binding.rvLeadActivity;
      recyclerView.setAdapter(new LeadListAdapter(leadsViewModel.arrListLeadListData));
    }

    private void callAPi(){
        leadsViewModel.getLeads();
    }

    private void initObserver(){

        leadsViewModel.getMutLeadListResponseApi().observe(this,result->{

            if(NetworkUtilities.getConnectivityStatus(this)) {

               if(result.getAllLeadList()!=null){
                   leadsViewModel.arrListLeadListData.clear();
                   leadsViewModel.arrListLeadListData.addAll(result.getAllLeadList());

                   Global.showToast(this,"Size of Lead List:"+result.getAllLeadList().size());

                   setUpRecyclerLeadListData();
               }


            }
            else Global.showSnackBar(view,getResources().getString(R.string.check_internet_connection));

            });

        //handle  error response
        leadsViewModel.getMutErrorResponse().observe(this, error ->{

            if (error != null && !error.isEmpty()) {
                Global.showSnackBar(view, error);
                System.out.println("Here: " + error);
            } else {
                Global.showSnackBar(view, getResources().getString(R.string.check_internet_connection));
            }
        });

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