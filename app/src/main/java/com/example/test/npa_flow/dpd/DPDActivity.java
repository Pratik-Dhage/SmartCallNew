package com.example.test.npa_flow.dpd;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.test.R;
import com.example.test.databinding.ActivityDpdactivityBinding;
import com.example.test.helper_classes.Global;
import com.example.test.helper_classes.NetworkUtilities;
import com.example.test.npa_flow.LoanCollectionActivity;
import com.example.test.npa_flow.dpd.DPD_ViewModel;
import com.example.test.npa_flow.dpd.adapter.DPD_Adapter;

public class DPDActivity extends AppCompatActivity {


    ActivityDpdactivityBinding binding;
    View view ;
    DPD_ViewModel dpdViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_dpdactivity);

        initializeFields();
        if(NetworkUtilities.getConnectivityStatus(this)){
            initObserver();
            call_DPD_Api();
        }
        else{
            Global.showToast(this,getString(R.string.check_internet_connection));
        }

        onClickListener();
    }

    private void call_DPD_Api() {
        dpdViewModel.getDPD_Data();
    }

    private void initializeFields() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dpdactivity);
        view = binding.getRoot();
        dpdViewModel = new ViewModelProvider(this).get(DPD_ViewModel.class);
        binding.setViewModel(dpdViewModel);

    }

    private void setUpDPDRecyclerView(){

        dpdViewModel.updateDashBoardData();
        RecyclerView recyclerView = binding.rvDPD;
        recyclerView.setAdapter(new DPD_Adapter(dpdViewModel.arrList_DPD_Data));
    }

    private void initObserver(){

       dpdViewModel.getMutDPD_ResponseApi().observe(this,result->{

           if(NetworkUtilities.getConnectivityStatus(this)){

               if(result!=null){

                   dpdViewModel.arrList_DPD_Data.clear();
                   setUpDPDRecyclerView();
                   dpdViewModel.arrList_DPD_Data.addAll(result);
               }

           }
               else{
               Global.showToast(this,getString(R.string.check_internet_connection));
           }

       });

        //handle  error response
        dpdViewModel.getMutErrorResponse().observe(this, error -> {

            if (error != null && !error.isEmpty()) {
                Global.showSnackBar(view, error);
                System.out.println("Here: " + error);
            } else {
                Global.showSnackBar(view, getResources().getString(R.string.check_internet_connection));
            }
        });



    }

    private void onClickListener() {

        binding.MainConstraint.setOnClickListener(v -> {

            Intent i = new Intent(DPDActivity.this, LoanCollectionActivity.class);
            startActivity(i);

        });

        binding.ivBack.setOnClickListener(v->{
            onBackPressed();
        });
    }


}