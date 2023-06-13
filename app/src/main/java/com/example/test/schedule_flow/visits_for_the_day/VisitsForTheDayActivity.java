package com.example.test.schedule_flow.visits_for_the_day;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.test.R;
import com.example.test.databinding.ActivityVisitsForTheDayBinding;
import com.example.test.helper_classes.Global;
import com.example.test.helper_classes.NetworkUtilities;
import com.example.test.main_dashboard.MainActivity3API;
import com.example.test.npa_flow.status_of_customer.adapter.StatusOfCustomerDetailsAdapter;
import com.example.test.schedule_flow.visits_for_the_day.adapter.VisitsForTheDayAdapter;

public class VisitsForTheDayActivity extends AppCompatActivity {

    ActivityVisitsForTheDayBinding binding;
    View view;
    VisitsForTheDayViewModel visitsForTheDayViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visits_for_the_day);

        initializeFields();
        onClickListeners();

        if (NetworkUtilities.getConnectivityStatus(this)) {
          callVisitsForTheDayAPi();
          initObserver();

        } else {
            Global.showToast(this, getString(R.string.check_internet_connection));
        }
    }


    private void initializeFields() {
        binding = DataBindingUtil.setContentView(this,R.layout.activity_visits_for_the_day);
        view = binding.getRoot();
        visitsForTheDayViewModel = new ViewModelProvider(this).get(VisitsForTheDayViewModel.class);
        binding.setViewModel(visitsForTheDayViewModel);
    }

    private void callVisitsForTheDayAPi() {
        visitsForTheDayViewModel.getVisitsForTheDayData();
    }

    private void setUpVisitsForTheDayRecyclerViewData(){
        visitsForTheDayViewModel.updateVisitsForTheDayData();
        RecyclerView recyclerView = binding.rvVisitsForTheDay;
        recyclerView.setAdapter(new VisitsForTheDayAdapter(visitsForTheDayViewModel.arrListVisitsForTheDayData));

    }

    private void initObserver() {

        visitsForTheDayViewModel.getMutVisitsForTheDayResponseApi().observe(this,result->{

            if(NetworkUtilities.getConnectivityStatus(this)){

                if(result!=null){

                    visitsForTheDayViewModel.arrListVisitsForTheDayData.clear();
                    setUpVisitsForTheDayRecyclerViewData();
                    visitsForTheDayViewModel.arrListVisitsForTheDayData.addAll(result);
                }

                if(result.isEmpty()){
                    Global.showToast(this,"No Data for Visits");
                }



                //handle  error response
                visitsForTheDayViewModel.getMutErrorResponse().observe(this, error -> {

                    if (error != null && !error.isEmpty()) {
                        Global.showSnackBar(view, error);
                        System.out.println("Here: " + error);
                    } else {
                        Global.showSnackBar(view, getResources().getString(R.string.check_internet_connection));
                    }
                });

            }

            else {
                Global.showSnackBar(view, getString(R.string.check_internet_connection));
            }

        });
    }


    private void onClickListeners() {

        binding.ivBack.setOnClickListener(v->{
            onBackPressed();
        });

        binding.ivHome.setOnClickListener(v->{
            startActivity(new Intent(this, MainActivity3API.class));
        });
    }
}