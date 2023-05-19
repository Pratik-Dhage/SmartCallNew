package com.example.test.npa_flow;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.test.R;
import com.example.test.databinding.ActivityVisitCompletionOfCustomerBinding;
import com.example.test.fragments_activity.BalanceInterestCalculationActivity;
import com.example.test.helper_classes.Global;
import com.example.test.helper_classes.NetworkUtilities;
import com.example.test.main_dashboard.MainActivity3API;
import com.example.test.npa_flow.call_details.CallDetailsViewModel;
import com.example.test.npa_flow.details_of_customer.DetailsOfCustomerResponseModel;
import com.example.test.npa_flow.details_of_customer.DetailsOfCustomerViewModel;
import com.example.test.npa_flow.details_of_customer.adapter.DetailsOfCustomerAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class VisitCompletionOfCustomerActivity extends AppCompatActivity {


    ActivityVisitCompletionOfCustomerBinding binding;
    View view;
    DetailsOfCustomerViewModel detailsOfCustomerViewModel;
    ArrayList<DetailsOfCustomerResponseModel> detailsList;
    CallDetailsViewModel callDetailsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  setContentView(R.layout.activity_visit_completion_of_customer);

        initializeFields();
        setUpDetailsOfCustomerRecyclerView();
        onClickListener();

       initObserver();
    }

    private void setUpToolbarTitle() {

        if (getIntent().hasExtra("isFromVisitNPAStatusActivity") || getIntent().hasExtra("isFromVisitNPANotificationActivity")
                || getIntent().hasExtra("isFromVisitNPARescheduleActivity")
                || getIntent().hasExtra("isFromVisitNPANotAvailableActivity") || getIntent().hasExtra("isFromVisitNPAPaymentModeActivity")

        ) {
            binding.txtToolbarHeading.setText(getString(R.string.visit_complete));
        }
    }

    private void initializeFields() {

        binding = DataBindingUtil.setContentView(this, R.layout.activity_visit_completion_of_customer);
        view = binding.getRoot();
        detailsOfCustomerViewModel = new ViewModelProvider(this).get(DetailsOfCustomerViewModel.class);
        binding.setViewModel(detailsOfCustomerViewModel);

        callDetailsViewModel = new ViewModelProvider(this).get(CallDetailsViewModel.class);


        //get detailsList
        detailsList = (ArrayList<DetailsOfCustomerResponseModel>) getIntent().getSerializableExtra("detailsList");
        setUpToolbarTitle();
    }


    private void setUpDetailsOfCustomerRecyclerView() {

        detailsOfCustomerViewModel.updateDetailsOfCustomer_Data();
        RecyclerView recyclerView = binding.rvDetailsOfCustomer;
        recyclerView.setAdapter(new DetailsOfCustomerAdapter(detailsList));
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

    private void onClickListener() {

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.ivHome.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity3API.class));
        });

        binding.btnCompleteNoChange.setOnClickListener(v -> {

            // Call Details Flow
            if(binding.txtToolbarHeading.getText()==getString(R.string.call_complete)){

                if(NetworkUtilities.getConnectivityStatus(this)){


                    if(getIntent().hasExtra("from_payment_status_partial_amt_paid")){

                        //Call Call Details API for Partial Amount Paid
                        String dataSetId = getIntent().getStringExtra("dataSetId");
                        String from_payment_status_partial_amt_paid = getIntent().getStringExtra("from_payment_status_partial_amt_paid");
                        String complete_no_change = "complete_no_change";

                        try{
                            callDetailsViewModel.postCallDetails(dataSetId,from_payment_status_partial_amt_paid,complete_no_change);
                        }catch(Exception e){
                            Log.d("Here Post CallDetailsException",e.getLocalizedMessage());
                        }

                      /*  Intent i = new Intent(VisitCompletionOfCustomerActivity.this, NearByCustomersActivity.class);
                        startActivity(i);*/
                    }

                    if(getIntent().hasExtra("from_payment_status_full_amt_paid")){

                        //Call Call Details API for Full Amount Paid
                        String dataSetId = getIntent().getStringExtra("dataSetId");
                        String from_payment_status_full_amt_paid = getIntent().getStringExtra("from_payment_status_full_amt_paid");
                        String complete_no_change = "complete_no_change";

                        try{
                            callDetailsViewModel.postCallDetails(dataSetId,from_payment_status_full_amt_paid,complete_no_change);
                        }catch(Exception e){
                            Log.d("Here Post CallDetailsException",e.getLocalizedMessage());
                        }

                    }

                   /* Intent i = new Intent(VisitCompletionOfCustomerActivity.this, NearByCustomersActivity.class);
                    startActivity(i);*/

                }
                else{

                    Global.showSnackBar(view,getString(R.string.check_internet_connection));
                }

            }




        });

        binding.btnCompleteNeedToUpdateDetails.setOnClickListener(v -> {

            // Call Details Flow
            if(binding.txtToolbarHeading.getText()==getString(R.string.call_complete)){

                if(NetworkUtilities.getConnectivityStatus(this)){

                    //Call Call Details API
                    if(getIntent().hasExtra("from_payment_status_partial_amt_paid")){

                        //Call Call Details API for Partial Amount Paid
                        String dataSetId = getIntent().getStringExtra("dataSetId");
                        String from_payment_status_partial_amt_paid = getIntent().getStringExtra("from_payment_status_partial_amt_paid");
                        String complete_need_to_update_details = "complete_need_to_update_details";

                        try{
                            callDetailsViewModel.postCallDetails(dataSetId,from_payment_status_partial_amt_paid,complete_need_to_update_details);
                        }catch(Exception e){
                            Log.d("Here Post CallDetailsException",e.getLocalizedMessage());
                        }

                       /* Intent i = new Intent(VisitCompletionOfCustomerActivity.this, NearByCustomersActivity.class);
                        startActivity(i);*/
                    }

                    if(getIntent().hasExtra("from_payment_status_full_amt_paid")){

                        //Call Call Details API for Full Amount Paid
                        String dataSetId = getIntent().getStringExtra("dataSetId");
                        String from_payment_status_full_amt_paid = getIntent().getStringExtra("from_payment_status_full_amt_paid");
                        String complete_need_to_update_details = "complete_need_to_update_details";

                        try{
                            callDetailsViewModel.postCallDetails(dataSetId,from_payment_status_full_amt_paid,complete_need_to_update_details);
                        }catch(Exception e){
                            Log.d("Here Post CallDetailsException",e.getLocalizedMessage());
                        }

                    }

                   /* Intent i = new Intent(VisitCompletionOfCustomerActivity.this, NearByCustomersActivity.class);
                    startActivity(i);*/
                }
                else{

                    Global.showSnackBar(view,getString(R.string.check_internet_connection));
                }

            }

        });


        binding.btnCompleteEscalateToBM.setOnClickListener(v -> {

            // Call Details Flow
            if(binding.txtToolbarHeading.getText()==getString(R.string.call_complete)){

                if(NetworkUtilities.getConnectivityStatus(this)){

                    //Call Call Details API
                    if(getIntent().hasExtra("from_payment_status_partial_amt_paid")){

                        //Call Call Details API for Partial Amount Paid
                        String dataSetId = getIntent().getStringExtra("dataSetId");
                        String from_payment_status_partial_amt_paid = getIntent().getStringExtra("from_payment_status_partial_amt_paid");
                        String complete_escalate_to_bm = "complete_escalate_to_bm";

                        try{
                            callDetailsViewModel.postCallDetails(dataSetId,from_payment_status_partial_amt_paid,complete_escalate_to_bm);
                        }catch(Exception e){
                            Log.d("Here Post CallDetailsException",e.getLocalizedMessage());
                        }

                        /*Intent i = new Intent(VisitCompletionOfCustomerActivity.this, NearByCustomersActivity.class);
                        startActivity(i);*/
                    }

                    if(getIntent().hasExtra("from_payment_status_full_amt_paid")){

                        //Call Call Details API for Full Amount Paid
                        String dataSetId = getIntent().getStringExtra("dataSetId");
                        String from_payment_status_full_amt_paid = getIntent().getStringExtra("from_payment_status_full_amt_paid");
                        String complete_escalate_to_bm = "complete_escalate_to_bm";

                        try{
                            callDetailsViewModel.postCallDetails(dataSetId,from_payment_status_full_amt_paid,complete_escalate_to_bm);
                        }catch(Exception e){
                            Log.d("Here Post CallDetailsException",e.getLocalizedMessage());
                        }

                    }

                   /* Intent i = new Intent(VisitCompletionOfCustomerActivity.this, NearByCustomersActivity.class);
                    startActivity(i);*/
                }
                else{

                    Global.showSnackBar(view,getString(R.string.check_internet_connection));
                }

            }
        });


        //for Notes
        binding.ivNotesIcon.setOnClickListener(v -> {

            View customDialog = LayoutInflater.from(this).inflate(R.layout.custom_dialog_box, null);

            TextView customText = customDialog.findViewById(R.id.txtCustomDialog);
            Button customButton = customDialog.findViewById(R.id.btnCustomDialog);
            EditText customEditBox = customDialog.findViewById(R.id.edtCustomDialog);
            customEditBox.setVisibility(View.VISIBLE);

            customText.setText(getResources().getString(R.string.lead_interaction));

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(customDialog);
            final AlertDialog dialog = builder.create();
            dialog.show();

            customButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

        });

        //for History
        binding.ivHistory.setOnClickListener(v -> {

            View customDialog = LayoutInflater.from(this).inflate(R.layout.custom_dialog_box, null);

            TextView customText = customDialog.findViewById(R.id.txtCustomDialog);
            Button customButton = customDialog.findViewById(R.id.btnCustomDialog);
            TextView txtCustom = customDialog.findViewById(R.id.txtCustom);
            txtCustom.setVisibility(View.VISIBLE);

            customText.setText(getResources().getString(R.string.lead_history));

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(customDialog);
            final AlertDialog dialog = builder.create();
            dialog.show();

            customButton.setText(R.string.close);
            customButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });


        });

    }

    // For Getting Calculated Balance Interest Result back from SharedPreference
    @Override
    protected void onResume() {
        initializeFields();
        onClickListener();
        setUpDetailsOfCustomerRecyclerView();
        super.onResume();
    }
}