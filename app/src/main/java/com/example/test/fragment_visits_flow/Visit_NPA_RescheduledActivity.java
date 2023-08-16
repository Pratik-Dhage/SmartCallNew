package com.example.test.fragment_visits_flow;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.test.R;
import com.example.test.api_manager.WebServices;
import com.example.test.databinding.ActivityPaymentInfoOfCustomerBinding;
import com.example.test.databinding.ActivityVisitNpaRescheduleBinding;
import com.example.test.fragments_activity.AlternateNumberApiCall;
import com.example.test.helper_classes.Global;
import com.example.test.helper_classes.NetworkUtilities;
import com.example.test.main_dashboard.MainActivity3API;
import com.example.test.npa_flow.ScheduleVisitForCollectionActivity;
import com.example.test.npa_flow.SubmitCompletionActivityOfCustomer;
import com.example.test.npa_flow.VisitCompletionOfCustomerActivity;
import com.example.test.npa_flow.details_of_customer.DetailsOfCustomerResponseModel;
import com.example.test.npa_flow.details_of_customer.DetailsOfCustomerViewModel;
import com.example.test.npa_flow.details_of_customer.adapter.DetailsOfCustomerAdapter;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class Visit_NPA_RescheduledActivity extends AppCompatActivity {

    ActivityVisitNpaRescheduleBinding binding;
    View view;
    View customDialogImagePicker;
    private ActivityResultLauncher<Intent> pickImageLauncher;
    DetailsOfCustomerViewModel detailsOfCustomerViewModel;
    VisitsFlowViewModel visitsFlowViewModel;
    public static ArrayList<DetailsOfCustomerResponseModel> detailsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_npa_reschedule);

        initializeFields();
        setUpDetailsOfCustomerRecyclerView();
        onClickListener();
        /*initObserver();
        if(NetworkUtilities.getConnectivityStatus(this)){
            callDetailsOfCustomerApi();
        }
        else{
            Global.showToast(this,getString(R.string.check_internet_connection));
        }*/

       // setUpImagePicker();
    }

    private void initializeFields() {

        binding= DataBindingUtil.setContentView(this,R.layout.activity_visit_npa_reschedule);
        view = binding.getRoot();
        detailsOfCustomerViewModel = new ViewModelProvider(this).get(DetailsOfCustomerViewModel.class);
        binding.setViewModel(detailsOfCustomerViewModel);
        visitsFlowViewModel = new ViewModelProvider(this).get(VisitsFlowViewModel.class);

        //get detailsList
        detailsList = (ArrayList<DetailsOfCustomerResponseModel>) getIntent().getSerializableExtra("detailsList");
        detailsList = (ArrayList<DetailsOfCustomerResponseModel>) Global.getUpdatedDetailsList(detailsList); //to get Updated List


    }

    private void callDetailsOfCustomerApi(){

        String dataSetId = getIntent().getStringExtra("dataSetId");
        detailsOfCustomerViewModel.getDetailsOfCustomer_Data(dataSetId); // call Details Of Customer API
    }


    private void setUpDetailsOfCustomerRecyclerView(){

        detailsOfCustomerViewModel.updateDetailsOfCustomer_Data();
        RecyclerView recyclerView = binding.rvDetailsOfCustomer;
        recyclerView.setAdapter(new DetailsOfCustomerAdapter(this,detailsList));
    }

    private void initObserver(){

        if(NetworkUtilities.getConnectivityStatus(this)){

            binding.loadingProgressBar.setVisibility(View.VISIBLE);

            detailsOfCustomerViewModel.getMutDetailsOfCustomer_ResponseApi().observe(this,result->{

                if(result!=null) {

                    detailsList = (ArrayList<DetailsOfCustomerResponseModel>) result;

                    detailsOfCustomerViewModel.arrList_DetailsOfCustomer_Data.clear();
                    setUpDetailsOfCustomerRecyclerView();
                    detailsOfCustomerViewModel.arrList_DetailsOfCustomer_Data.addAll(result);
                    binding.loadingProgressBar.setVisibility(View.INVISIBLE);


                }
            });

            //handle  error response
            detailsOfCustomerViewModel.getMutErrorResponse().observe(this, error -> {

                if (error != null && !error.isEmpty()) {
                   // Global.showSnackBar(view, error);
                    System.out.println("Here: " + error);
                } else {
                    Global.showSnackBar(view, getResources().getString(R.string.check_internet_connection));
                }
            });
        }
        else{
            Global.showToast(this,getString(R.string.check_internet_connection));
        }

    }




    private void onClickListener(){

        //Call Save Alternate Number API
        if(null!= DetailsOfCustomerAdapter.alternateNumber && null!=DetailsOfCustomerAdapter.dataSetId){
            System.out.println("Save Alternate No. Api dataSetId:"+DetailsOfCustomerAdapter.dataSetId+" Alternate No. :"+DetailsOfCustomerAdapter.alternateNumber);
            AlternateNumberApiCall.saveAlternateNumber(this,DetailsOfCustomerAdapter.alternateNumber,DetailsOfCustomerAdapter.dataSetId);
        }

        binding.ivBack.setOnClickListener(v->{
            onBackPressed();
        });

        binding.ivHome.setOnClickListener(v->{
            startActivity(new Intent(this, MainActivity3API.class));
        });

        binding.btnRescheduleVisit.setOnClickListener(v->{
            Intent i = new Intent(this, ScheduleVisitForCollectionActivity.class);
            i.putExtra("dataSetId", getIntent().getStringExtra("dataSetId"));
            i.putExtra("Visit_NPA_Reschedule_ScheduleVisit","Visit_NPA_Reschedule_ScheduleVisit");
            if(getIntent().hasExtra("isFromVisitNPANotAvailableActivity_CustomerNotAvailable")){
                i.putExtra("isFromVisitNPANotAvailableActivity_CustomerNotAvailable","isFromVisitNPANotAvailableActivity_CustomerNotAvailable");
            }
            if(getIntent().hasExtra("isFromVisitNPANotAvailableActivity_LateForVisit")){
                i.putExtra("isFromVisitNPANotAvailableActivity_LateForVisit","isFromVisitNPANotAvailableActivity_LateForVisit");
            }
            if(getIntent().hasExtra("isFromVisitNPANotAvailableActivity_Others")){
               i.putExtra("isFromVisitNPANotAvailableActivity_Others","isFromVisitNPANotAvailableActivity_Others");
            }

            startActivity(i);
        });


        binding.btnScheduleCall.setOnClickListener(v->{
            Intent i = new Intent(this, ScheduleVisitForCollectionActivity.class);
            i.putExtra("dataSetId", getIntent().getStringExtra("dataSetId"));
            i.putExtra("Visit_NPA_Reschedule_ScheduleCall","Visit_NPA_Reschedule_ScheduleCall"); //for title purpose
            if(getIntent().hasExtra("isFromVisitNPANotAvailableActivity_CustomerNotAvailable")){
                i.putExtra("isFromVisitNPANotAvailableActivity_CustomerNotAvailable","isFromVisitNPANotAvailableActivity_CustomerNotAvailable");
            }
            if(getIntent().hasExtra("isFromVisitNPANotAvailableActivity_LateForVisit")){
                i.putExtra("isFromVisitNPANotAvailableActivity_LateForVisit","isFromVisitNPANotAvailableActivity_LateForVisit");
            }
            if(getIntent().hasExtra("isFromVisitNPANotAvailableActivity_Others")){
                i.putExtra("isFromVisitNPANotAvailableActivity_Others","isFromVisitNPANotAvailableActivity_Others");
            }

            startActivity(i);
        });

        binding.btnSubmit.setOnClickListener(v->{

            String dataSetId = getIntent().getStringExtra("dataSetId");

            if(getIntent().hasExtra("isFromVisitNPANotAvailableActivity_CustomerNotAvailable")){
                String customerNotAvailable_Submit= WebServices.did_not_visit_customer_CustomerNotAvailable_Submit;
                VisitsFlowCallDetailsActivity.send_reason =""; //reason should be null or empty
                VisitsFlowCallDetailsActivity visitsFlowCallDetailsActivity = new VisitsFlowCallDetailsActivity();
                visitsFlowViewModel.postVisitsFlow_DidNotVisitTheCustomer(customerNotAvailable_Submit, dataSetId, "", "", "", "", "", "", visitsFlowCallDetailsActivity.sendCallLogDetailsList_VisitsFlow());
            }
         else if(getIntent().hasExtra("isFromVisitNPANotAvailableActivity_LateForVisit")){
                String lateForVisit_Submit = WebServices.did_not_visit_customer_LateForVisit_Submit;
                VisitsFlowCallDetailsActivity visitsFlowCallDetailsActivity = new VisitsFlowCallDetailsActivity();
                visitsFlowViewModel.postVisitsFlow_DidNotVisitTheCustomer(lateForVisit_Submit, dataSetId, "", "", "", "", "", VisitsFlowCallDetailsActivity.send_reason, visitsFlowCallDetailsActivity.sendCallLogDetailsList_VisitsFlow());
            }
          else  if(getIntent().hasExtra("isFromVisitNPANotAvailableActivity_Others")){
                String others_Submit = WebServices.did_not_visit_customer_Others_Submit;
                VisitsFlowCallDetailsActivity visitsFlowCallDetailsActivity = new VisitsFlowCallDetailsActivity();
                visitsFlowViewModel.postVisitsFlow_DidNotVisitTheCustomer(others_Submit, dataSetId, "", "", "", "", "", VisitsFlowCallDetailsActivity.send_reason, visitsFlowCallDetailsActivity.sendCallLogDetailsList_VisitsFlow());
            }

            initObserverSubmit();
        });



        //for Notes
        binding.ivNotesIcon.setOnClickListener(v->{

            Global.showNotesEditDialogVisits(this);
        });

        //for History
        binding.ivHistory.setOnClickListener(v->{

            String dataSetId = getIntent().getStringExtra("dataSetId");
            Global.showNotesHistoryDialog(this,dataSetId);

        });

    }

    private void initObserverSubmit(){

        if(NetworkUtilities.getConnectivityStatus(this)){

            visitsFlowViewModel.getMutVisitsCallDetailsResponseApi().observe(this,result->{

                if(result!=null){
                    System.out.println("Submit Response:"+result);
                    Global.showSnackBar(view,result);
                      navigateToDashBoard();
                }

            });

            //handle error response
            visitsFlowViewModel.getMutErrorResponse().observe(this, error -> {

                if (error != null && !error.isEmpty()) {

                    System.out.println("Here Submit  Exception: " + error);

                } else {
                    Global.showSnackBar(view, getResources().getString(R.string.check_internet_connection));
                }
            });

        }
        else {
            Global.showSnackBar(view, getResources().getString(R.string.check_internet_connection));
        }

    }


    private void navigateToDashBoard(){

        VisitsFlowCallDetailsActivity.send_reason =""; // make empty to reset flow
        Global.removeStringInSharedPref(this,"scheduleVisitForCollection_dateTime"); // make empty to reset flow

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(Visit_NPA_RescheduledActivity.this,MainActivity3API.class);
                startActivity(i);
            }
        },1000);

    }

    // For Getting Calculated Balance Interest Result back from SharedPreference
    @Override
    protected void onResume() {
        initializeFields();
        setUpDetailsOfCustomerRecyclerView();
        onClickListener();
       // setUpImagePicker();
      //  initObserver();
      //  callDetailsOfCustomerApi();
        super.onResume();
    }

    public ActivityVisitNpaRescheduleBinding getBinding(){
        return binding;
    }
}