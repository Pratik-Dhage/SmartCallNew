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
import com.example.test.main_dashboard.MainActivity3API;
import com.example.test.npa_flow.loan_collection.LoanCollectionActivity;
import com.example.test.npa_flow.dpd.DPD_ViewModel;
import com.example.test.npa_flow.dpd.adapter.DPD_Adapter;
import com.example.test.roomDB.dao.MPinDao;
import com.example.test.roomDB.dao.UserNameDao;
import com.example.test.roomDB.database.LeadListDB;

public class DPDActivity extends AppCompatActivity {


    ActivityDpdactivityBinding binding;
    View view ;
    DPD_ViewModel dpdViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_dpdactivity);

        initializeFields();
        initObserver();
        if(NetworkUtilities.getConnectivityStatus(this)){

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

        LoanCollectionActivity.LoanCollectionLayoutAdapterPosition = 0; // Reset to 0 to reset adapter position
        Global.removeStringInSharedPref(this,"LoanCollectionLayoutAdapterPosition"); // remove LoanCollectionAdapterPosition to reset flow
        System.out.println("Here DPDActivity LoanCollectionLayoutAdapterPosition:"+LoanCollectionActivity.LoanCollectionLayoutAdapterPosition);

        // Get UserName , UserID , BranchCode
        MPinDao mPinDao = LeadListDB.getInstance(this).mPinDao();
        UserNameDao userNameDao = LeadListDB.getInstance(this).userNameDao();
        String userName = userNameDao.getUserNameUsingUserIDInUserNameRoomDB(mPinDao.getUserID());
        // Store UserName in SharedPreference and Use in StatusOfCustomerDetailsAdapter
        Global.saveStringInSharedPref(this,"userName",userName);

        MainActivity3API.UserID = mPinDao.getUserID();
        MainActivity3API.BranchCode = mPinDao.getBranchCode();

        System.out.println("Here DPDActivity initializeFields() UserID:"+MainActivity3API.UserID);
        System.out.println("Here DPDActivity initializeFields() BranchCode:"+MainActivity3API.BranchCode);

    }

    private void setUpDPDRecyclerView(){

        dpdViewModel.updateDPDData();
        RecyclerView recyclerView = binding.rvDPD;
        recyclerView.setAdapter(new DPD_Adapter(dpdViewModel.arrList_DPD_Data));
    }

    private void initObserver(){

        binding.loadingProgressBar.setVisibility(View.VISIBLE);
       dpdViewModel.getMutDPD_ResponseApi().observe(this,result->{

           if(NetworkUtilities.getConnectivityStatus(this)){

               if(result!=null){

                   dpdViewModel.arrList_DPD_Data.clear();
                   setUpDPDRecyclerView();
                   dpdViewModel.arrList_DPD_Data.addAll(result);
                   binding.loadingProgressBar.setVisibility(View.GONE);
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

        binding.ivBack.setOnClickListener(v->{
            onBackPressed();
        });
    }

    @Override
    protected void onResume() {

        initializeFields();
        initObserver();
        if(NetworkUtilities.getConnectivityStatus(this)){
            call_DPD_Api();
        }
        else{
            Global.showToast(this,getString(R.string.check_internet_connection));
        }
        onClickListener();

        super.onResume();
    }
}