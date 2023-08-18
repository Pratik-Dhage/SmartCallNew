package com.example.test.schedule_flow.calls_for_the_day;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test.R;
import com.example.test.databinding.ActivityCallsForTheDayBinding;
import com.example.test.helper_classes.Global;
import com.example.test.helper_classes.NetworkUtilities;
import com.example.test.main_dashboard.MainActivity3API;
import com.example.test.npa_flow.NearByCustomersActivity;
import com.example.test.npa_flow.details_of_customer.DetailsOfCustomerActivity;
import com.example.test.roomDB.dao.MPinDao;
import com.example.test.roomDB.dao.UserNameDao;
import com.example.test.roomDB.database.LeadListDB;
import com.example.test.schedule_flow.calls_for_the_day.adapter.CallsForTheDayAdapter;

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

        Global.saveStringInSharedPref(this,"notes",""); //make Notes Empty After Complete

        //Whenever List is Loaded remove previously stored formattedDistanceInKm
        Global.removeStringInSharedPref(this, "formattedDistanceInKm");

        //Initial Values to be null Whenever List is loaded
        DetailsOfCustomerActivity.send_callNotes = null;
        DetailsOfCustomerActivity.send_RelativeName = null;
        Global.saveStringInSharedPref(this,"relativeName",""); //empty relativeName
        DetailsOfCustomerActivity.send_RelativeContact = null;
        Global.saveStringInSharedPref(this,"relativeContact",""); //emptyRelativeContact
        DetailsOfCustomerActivity.send_FoName = null;
        Global.saveStringInSharedPref(this,"foName",""); //empty foName
        DetailsOfCustomerActivity.send_DateOfVisitPromised = null;
        Global.saveStringInSharedPref(this,"dateOfVisitPromised",""); //empty dateOfVisitPromised
        DetailsOfCustomerActivity.send_callScheduledTime = null;
        DetailsOfCustomerActivity.send_reason = null;

        //Get UserIdD & BranchCode
        MPinDao mPinDao = LeadListDB.getInstance(this).mPinDao();
        UserNameDao userNameDao = LeadListDB.getInstance(this).userNameDao();

        String userName = userNameDao.getUserNameUsingUserIDInUserNameRoomDB(mPinDao.getUserID());

        MainActivity3API.UserID = mPinDao.getUserID();
        MainActivity3API.BranchCode = mPinDao.getBranchCode();

        System.out.println("Here CallsForTheDay initializeFields() UserID:"+MainActivity3API.UserID);
        System.out.println("Here CallsForTheDay initializeFields() BranchCode:"+MainActivity3API.BranchCode);

        NearByCustomersActivity.backToMemberList = 2; // for CallsForTheDayFlow
        Global.saveStringInSharedPref(this,"backToMemberList","2");
     }


    private void callCallForTheDayApi() {
        callsForTheDayViewModel.getCallsForTheDayData();
    }

    private void setUpCallsForTheDayRecyclerViewData(){
        callsForTheDayViewModel.updateCallsForTheDayData();
        RecyclerView recyclerView = binding.rvCallsForTheDay;
        recyclerView.setAdapter(new CallsForTheDayAdapter(callsForTheDayViewModel.arrListCallsForTheDayData));

    }

    private void initObserver() {

        callsForTheDayViewModel.getMutCallsForTheDayResponseApi().observe(this,result->{

            if(NetworkUtilities.getConnectivityStatus(this)){

                if(result!=null){

                    callsForTheDayViewModel.arrListCallsForTheDayData.clear();
                    setUpCallsForTheDayRecyclerViewData();
                    callsForTheDayViewModel.arrListCallsForTheDayData.addAll(result);


                    if(result.isEmpty()){
                        binding.txtNoDataForCalls.setVisibility(View.VISIBLE);
                    }

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

        binding.ivHome.setOnClickListener(v->{
            startActivity(new Intent(this, MainActivity3API.class));
        });
    }

    @Override
    protected void onResume() {

        initializeFields();
        onClickListeners();
        if (NetworkUtilities.getConnectivityStatus(this)) {
            callCallForTheDayApi();
            initObserver();

        } else {
            Global.showToast(this, getString(R.string.check_internet_connection));
        }
        super.onResume();
    }
}