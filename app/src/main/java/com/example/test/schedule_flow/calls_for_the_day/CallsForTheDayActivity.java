package com.example.test.schedule_flow.calls_for_the_day;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;

import com.example.test.R;
import com.example.test.databinding.ActivityCallsForTheDayBinding;
import com.example.test.helper_classes.Global;
import com.example.test.helper_classes.NetworkUtilities;
import com.example.test.schedule_flow.visits_for_the_day.VisitsForTheDayViewModel;

public class CallsForTheDayActivity extends AppCompatActivity {

    ActivityCallsForTheDayBinding binding;
    View view;
    CallsForTheDayViewModel callsForTheDayViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calls_for_the_day);

        initializeFields();
        onClickListeners();
        if (NetworkUtilities.getConnectivityStatus(this)) {
         callCallForTheDayApi();
         initObserver();

        } else {
            Global.showToast(this, getString(R.string.check_internet_connection));
        }

    }



    private void initializeFields() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_calls_for_the_day);
        view = binding.getRoot();
        callsForTheDayViewModel = new ViewModelProvider(this).get(CallsForTheDayViewModel.class);
        binding.setViewModel(callsForTheDayViewModel);
    }


    private void callCallForTheDayApi() {
        callsForTheDayViewModel.getCallsForTheDayData();
    }

    private void initObserver() {

        callsForTheDayViewModel.getMutCallsForTheDayResponseApi().observe(this,result->{

            if(NetworkUtilities.getConnectivityStatus(this)){

                if(result!=null){

                }

                if(result.isEmpty()){
                    Global.showToast(this,"Calls Api Called");
                }



                //handle  error response
                callsForTheDayViewModel.getMutErrorResponse().observe(this, error -> {

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

        binding.ivBack.setOnClickListener(v -> {
            onBackPressed();
        });
    }
}