package com.example.test.npa_flow;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.test.R;
import com.example.test.databinding.ActivitySubmitCompletionOfCustomerBinding;
import com.example.test.helper_classes.Global;
import com.example.test.helper_classes.NetworkUtilities;
import com.example.test.main_dashboard.MainActivity3API;
import com.example.test.npa_flow.call_details.CallDetailsViewModel;
import com.example.test.npa_flow.details_of_customer.DetailsOfCustomerResponseModel;
import com.example.test.npa_flow.details_of_customer.DetailsOfCustomerViewModel;
import com.example.test.npa_flow.details_of_customer.adapter.DetailsOfCustomerAdapter;

import java.util.ArrayList;

public class SubmitCompletionActivityOfCustomer extends AppCompatActivity {


    ActivitySubmitCompletionOfCustomerBinding binding;
    View view;
    DetailsOfCustomerViewModel detailsOfCustomerViewModel;
    ArrayList<DetailsOfCustomerResponseModel> detailsList;
    CallDetailsViewModel callDetailsViewModel;
    public  String relativeName ;
    public   String relativeContact ;
    public  String dateOfVisitPromised ; ;
    public  String foName ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_completion_of_customer);

        initializeFields();
        setUpDetailsOfCustomerRecyclerView();
        onClickListener();
        initObserver();

    }

    private void setToolBarTitle(){
        if(getIntent().hasExtra("isFromVisitNPANotificationActivity")){
            binding.txtToolbarHeading.setText(getString(R.string.visit_complete));
        }
        else {
            binding.txtToolbarHeading.setText(getString(R.string.call_complete));
        }
    }

    private void initializeFields() {
        binding = DataBindingUtil.setContentView(this,R.layout.activity_submit_completion_of_customer);
        view =binding.getRoot();
        detailsOfCustomerViewModel = new ViewModelProvider(this).get(DetailsOfCustomerViewModel.class);
        binding.setViewModel(detailsOfCustomerViewModel);

        callDetailsViewModel = new ViewModelProvider(this).get(CallDetailsViewModel.class);
         setToolBarTitle();

        //get detailsList
        detailsList = (ArrayList<DetailsOfCustomerResponseModel>) getIntent().getSerializableExtra("detailsList");

        relativeName = getIntent().getStringExtra("relativeName");
        relativeContact =getIntent().getStringExtra("relativeContact");
        dateOfVisitPromised = getIntent().getStringExtra("dateOfVisitPromised");
        foName = getIntent().getStringExtra("foName");
    }

    private void initObserver(){

        if(NetworkUtilities.getConnectivityStatus(this)) {
            callDetailsViewModel.getMutCallDetailsResponseApi().observe(this, result -> {

                if(result!=null){
                    Global.showToast(this,"Server Response:"+result);
                }
                if(result==null){
                    Global.showToast(this,"Server Response: Null");
                }

            });

            //to handle error
            callDetailsViewModel.getMutErrorResponse().observe(this,error->{
                if (error != null && !error.isEmpty()) {
                    Global.showSnackBar(view, error);
                    System.out.println("Here error : " + error);
                    //Here error : End of input at line 1 column 1 path $ (if Server response body is empty, we get this error)
                }
            });

        }
        else{
            Global.showSnackBar(view,getString(R.string.check_internet_connection));
        }
    }


    private void setUpDetailsOfCustomerRecyclerView() {

        detailsOfCustomerViewModel.updateDetailsOfCustomer_Data();
        RecyclerView recyclerView = binding.rvDetailsOfCustomer;
        recyclerView.setAdapter(new DetailsOfCustomerAdapter(detailsList));
    }

    private void onClickListener() {

        binding.ivBack.setOnClickListener(v -> onBackPressed());

        binding.ivHome.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity3API.class));
        });

        binding.btnSubmitNoChange.setOnClickListener(v->{

            //FO NOT VISITED
            if(getIntent().hasExtra("isFoNotVisited")){
                String dataSetId = getIntent().getStringExtra("dataSetId");
               //  dateOfVisitPromised = getIntent().getStringExtra("dateOfVisitPromised");
               //  foName = getIntent().getStringExtra("foName");

                if(NetworkUtilities.getConnectivityStatus(this)){
                    callDetailsViewModel.postScheduledDateTime_FNV(dataSetId,"",dateOfVisitPromised,foName,"","");

                }
                else{
                    Global.showSnackBar(view,getString(R.string.check_internet_connection));
                }

            }

            //LOAN TAKEN BY RELATIVE
            if(getIntent().hasExtra("isLoanTakenByRelative")){
                String dataSetId = getIntent().getStringExtra("dataSetId");
               //  relativeName = getIntent().getStringExtra("relativeName");
               //  relativeContact = getIntent().getStringExtra("relativeContact");

                if(NetworkUtilities.getConnectivityStatus(this)){
                    callDetailsViewModel.postScheduledDateTime_LTBR(dataSetId,"","","",relativeName,relativeContact,"LTBR");
                }
                else{
                    Global.showSnackBar(view,getString(R.string.check_internet_connection));
                }
            }


          //PAYMENT INFO WILL PAY LATER->WILL PAY LUMPSUM
            if(getIntent().hasExtra("paymentInfo_WillPayLater")){
                String dataSetId = getIntent().getStringExtra("dataSetId");
                if(NetworkUtilities.getConnectivityStatus(this)){
                    callDetailsViewModel.postScheduledDateTime_WPLS(dataSetId,"","","","","");
                }
                else {
                    Global.showSnackBar(view,getString(R.string.check_internet_connection));
                }
            }

        });

        binding.btnSubmitNeedToUpdateDetails.setOnClickListener(v->{

        });

        binding.btnSubmitEscalateToBM.setOnClickListener(v->{

        });
    }


    @Override
    protected void onResume() {
        initializeFields();
        onClickListener();
        setUpDetailsOfCustomerRecyclerView();
        super.onResume();
    }
}