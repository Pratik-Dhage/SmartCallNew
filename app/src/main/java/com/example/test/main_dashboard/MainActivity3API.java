package com.example.test.main_dashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.test.R;
import com.example.test.databinding.ActivityMainActivity3ApiBinding;
import com.example.test.fragments_activity.ActivityOfFragments;
import com.example.test.helper_classes.Global;
import com.example.test.helper_classes.NetworkUtilities;
import com.example.test.main_dashboard.adapter.MainDashBoardAdapter;
import com.example.test.main_dashboard.model.DashBoardResponseModel;
import com.example.test.npa_flow.loan_collection.LoanCollectionActivity;
import com.example.test.schedule_flow.ScheduleDetailsActivity;

public class MainActivity3API extends AppCompatActivity {

    ActivityMainActivity3ApiBinding binding;
    View view;
    MainDashBoardViewModel mainDashBoardViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_main_activity3_api);

        initializeFields();
        onClickListener();

        initObserver();

        if(NetworkUtilities.getConnectivityStatus(this)){
            callDashBoardApi();

        }
       else{
            Global.showToast(this,getString(R.string.check_internet_connection));
        }

    }

    private void initializeFields() {

        binding = DataBindingUtil.setContentView(this,R.layout.activity_main_activity3_api);
        view = binding.getRoot();
        mainDashBoardViewModel = new ViewModelProvider(this).get(MainDashBoardViewModel.class);
        binding.setViewModel(mainDashBoardViewModel);
    }

    private void callDashBoardApi() {
        mainDashBoardViewModel.getDashBoardData();
    }

    private void setUpDashBoardRecyclerView() {
        mainDashBoardViewModel.updateDashBoardData();
        RecyclerView recyclerView = binding.rvDashBoardMain;
        recyclerView.setAdapter(new MainDashBoardAdapter(mainDashBoardViewModel.arrListDashBoardData));
    }


    private void initObserver(){

        binding.loadingProgressBar.setVisibility(View.VISIBLE);
        mainDashBoardViewModel.getMutDashBoardResponseApi().observe(this, result -> {

            if(NetworkUtilities.getConnectivityStatus(this)) {
                if (result != null) {
                    //   Global.showToast(MainActivity.this, "Size:" + result.size());

                    mainDashBoardViewModel.arrListDashBoardData.clear();

                    setUpDashBoardRecyclerView();
                    mainDashBoardViewModel.arrListDashBoardData.addAll(result);
                    binding.loadingProgressBar.setVisibility(View.GONE);


                    //to get TotalMembers Assigned value
                    int totalCompletedCalls = 0;
                    int totalPendingCalls = 0;
                    int TotalMembersAssigned = 0;

                    for(DashBoardResponseModel a : result){

                        totalCompletedCalls += a.getCompletedCalls();
                        totalPendingCalls += a.getPendingCalls();


                         TotalMembersAssigned = totalCompletedCalls + totalPendingCalls;
                    }

                    // Total Members(Marketing+Collection(NPA)+Welcome Call+ Renewal)
                    binding.labelTotalAssignedValue.setText(String.valueOf(TotalMembersAssigned));
                    binding.labelPendingMembersAssignedValue.setText(String.valueOf(TotalMembersAssigned)); // same value for Pending because Completed = 0

                }
            }
            else{
                Global.showSnackBar(view,getResources().getString(R.string.check_internet_connection));
            }

        });

        //handle  error response
        mainDashBoardViewModel.getMutErrorResponse().observe(this, error -> {

            if (error != null && !error.isEmpty()) {
                Global.showSnackBar(view, error);
                System.out.println("Here: " + error);
            } else {
                Global.showSnackBar(view, getResources().getString(R.string.check_internet_connection));
            }
        });

    }


    private void onClickListener(){

        //Scheduled for the day section

        binding.ivSchedule.setOnClickListener(v->{
            Intent i = new Intent(this, ScheduleDetailsActivity.class);
            startActivity(i);
        });

        binding.ivRightArrowVisits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity3API.this, ActivityOfFragments.class);
                startActivity(i);
            }
        });

        binding.ivRightArrowCalls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity3API.this, LoanCollectionActivity.class);
                i.putExtra("isFromCallsForTheDay","isFromCallsForTheDay");
                startActivity(i);
            }
        });


        //Assigned Section
        binding.ivRightArrowMembersAssigned.setOnClickListener(v->{

            if(binding.cardView2.getVisibility()==View.INVISIBLE){
                binding.ivRightArrowMembersAssigned.setImageResource(R.drawable.down_arrow);
                binding.cardView2.setVisibility(View.VISIBLE);
            }
            else{
                binding.ivRightArrowMembersAssigned.setImageResource(R.drawable.right_arrow);
                binding.cardView2.setVisibility(View.INVISIBLE);
            }

        });





    }



}