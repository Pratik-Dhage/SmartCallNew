package com.example.test.npa_flow;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.test.R;
import com.example.test.api_manager.WebServices;
import com.example.test.databinding.ActivityVisitCompletionOfCustomerBinding;
import com.example.test.fragment_visits_flow.VisitsFlowCallDetailsActivity;
import com.example.test.fragment_visits_flow.VisitsFlowViewModel;
import com.example.test.fragments_activity.BalanceInterestCalculationActivity;
import com.example.test.helper_classes.Global;
import com.example.test.helper_classes.NetworkUtilities;
import com.example.test.main_dashboard.MainActivity3API;
import com.example.test.npa_flow.call_details.CallDetailsViewModel;
import com.example.test.npa_flow.details_of_customer.DetailsOfCustomerResponseModel;
import com.example.test.npa_flow.details_of_customer.DetailsOfCustomerViewModel;
import com.example.test.npa_flow.details_of_customer.adapter.DetailsOfCustomerAdapter;
import com.example.test.npa_flow.loan_collection.LoanCollectionActivity;
import com.example.test.roomDB.dao.MPinDao;
import com.example.test.roomDB.dao.UserNameDao;
import com.example.test.roomDB.database.LeadListDB;
import com.example.test.schedule_flow.calls_for_the_day.CallsForTheDayActivity;
import com.example.test.schedule_flow.calls_for_the_day.adapter.CallsForTheDayAdapter;
import com.example.test.schedule_flow.visits_for_the_day.VisitsForTheDayActivity;

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
    VisitsFlowViewModel visitsFlowViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  setContentView(R.layout.activity_visit_completion_of_customer);

        initializeFields();
        setUpDetailsOfCustomerRecyclerView();
        onClickListener();

       initObserver();
       initObserverVisitsFlow();
    }

    private void setUpToolbarTitle() {

        if (getIntent().hasExtra("isFromVisitNPAStatusActivity") || getIntent().hasExtra("isFromVisitNPANotificationActivity")
                || getIntent().hasExtra("isFromVisitNPARescheduleActivity")
                || getIntent().hasExtra("isFromVisitNPANotAvailableActivity") || getIntent().hasExtra("isFromVisitNPAPaymentModeActivity")
                || getIntent().hasExtra("isFromVisitsForTheDayFlow_PaymentModeStatusActivity")
                || getIntent().hasExtra("isFromVisitsForTheDayFlow_Visit_NPA_PaymentModeActivity")
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
        visitsFlowViewModel = new ViewModelProvider(this).get(VisitsFlowViewModel.class);

        //get detailsList
        detailsList = (ArrayList<DetailsOfCustomerResponseModel>) getIntent().getSerializableExtra("detailsList");
        setUpToolbarTitle();

        // Get UserName , UserID , BranchCode
        MPinDao mPinDao = LeadListDB.getInstance(this).mPinDao();
        UserNameDao userNameDao = LeadListDB.getInstance(this).userNameDao();
        String userName = userNameDao.getUserNameUsingUserIDInUserNameRoomDB(mPinDao.getUserID());
        // Store UserName in SharedPreference and Use in StatusOfCustomerDetailsAdapter
        Global.saveStringInSharedPref(this,"userName",userName);

        MainActivity3API.UserID = mPinDao.getUserID();
        MainActivity3API.BranchCode = mPinDao.getBranchCode();

        System.out.println("Here VisitCompletionOfCustomerActivity initializeFields UserID:"+MainActivity3API.UserID);
        System.out.println("Here VisitCompletionOfCustomerActivity initializeFields BranchCode:"+MainActivity3API.BranchCode);

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

    private void initObserverVisitsFlow(){

        if(NetworkUtilities.getConnectivityStatus(this)) {
            visitsFlowViewModel.getMutVisitsCallDetailsResponseApi().observe(this, result -> {

                if(result!=null){
                    Global.showToast(this,"Server Response:"+result);
                }
                if(result==null){
                    Global.showToast(this,"Server Response: Null");
                }

            });

            //to handle error
            visitsFlowViewModel.getMutErrorResponse().observe(this,error->{
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

            Global.saveStringInSharedPref(this,"notes",""); //make Notes Empty After Complete

            //3 Scenarios 1) Calls For The Day Flow 2) Call Details Flow (NPA List) 3) Visits For The Day Flow

            //Visits For The Day Flow

            // VisitsForTheDay->Did Not Visit The Customer-> Payment Already Made
            if(getIntent().hasExtra("isFromVisitNPARescheduleActivity_payment_already_made")){
                String dataSetId = getIntent().getStringExtra("dataSetId");
                String payment_already_made_proceed = WebServices.visit_did_not_visit_payment_already_made;

                VisitsFlowCallDetailsActivity visitsFlowCallDetailsActivity = new VisitsFlowCallDetailsActivity();
                visitsFlowViewModel.postVisitsFlow_DidNotVisitTheCustomer(payment_already_made_proceed,dataSetId,"","",",","","","",visitsFlowCallDetailsActivity.sendCallLogDetailsList_VisitsFlow());
                navigateToDashBoard();

            }


                    if(getIntent().hasExtra("isFromVisitsForTheDayFlow_Visit_NPA_PaymentModeActivity")){
                        Global.showToast(this,"Payment Complete");

                        String dataSetId = getIntent().getStringExtra("dataSetId");
                        if(NetworkUtilities.getConnectivityStatus(this)){
                            if(getIntent().hasExtra("isVisitsReadyToPaySendLinkForOnlinePayment")){
                                String sendLinkForOnlinePayment = WebServices.visit_ready_to_pay_send_link_for_online_payment;
                                visitsFlowViewModel.postVisitsFlowCallDateTime(sendLinkForOnlinePayment,dataSetId,"","","","","");
                            }
                          else  if(getIntent().hasExtra("isVisitsReadyToPayCashPayment")){
                                String cashPayment = WebServices.visit_ready_to_pay_cash_payment;
                                String amountCollected = Global.getStringFromSharedPref(this,"Amount_Paid");
                                visitsFlowViewModel.postVisitsFlowCallDateTimeCheque_Cash(cashPayment,dataSetId,"","","","","",
                                        amountCollected,"","","","");
                            }
                          else  if(getIntent().hasExtra("isVisitsReadyToPayChequePayment")){
                                String chequePayment = WebServices.visit_ready_to_pay_cheque_payment;
                                String amountCollected = Global.getStringFromSharedPref(this,"Amount_Paid");
                                String chequeDate = getIntent().getStringExtra("ChequeDate") ;
                                String chequeNumber = getIntent().getStringExtra("ChequeNumber");
                                String chequeAmount = getIntent().getStringExtra("ChequeAmount");
                                String bankName = getIntent().getStringExtra("BankName");

                                visitsFlowViewModel.postVisitsFlowCallDateTimeCheque_Cash(chequePayment,dataSetId,"","","","","",
                                       amountCollected,chequeDate,chequeNumber,chequeAmount,bankName);
                            }

                        }
                        else{
                            Global.showSnackBar(view,getString(R.string.check_internet_connection));
                        }

                        Handler handler = new Handler();
                        Runnable startVisitsActivity = new Runnable() {
                            @Override
                            public void run() {
                                Intent i = new Intent(VisitCompletionOfCustomerActivity.this, NearByCustomersActivity.class);
                                startActivity(i);
                            }
                        };
                        handler.postDelayed(startVisitsActivity, 3000);

                    }

            //Calls For the Day Flow
            if(CallsForTheDayAdapter.isFromCallsForTheDayAdapter !=null){
                Intent i = new Intent(this, CallsForTheDayActivity.class);
                startActivity(i);
                CallsForTheDayAdapter.isFromCallsForTheDayAdapter = null; // make it null to reset the flows
                System.out.println("Here isFromCallsForTheDayAdapter: "+CallsForTheDayAdapter.isFromCallsForTheDayAdapter);
            }

            else{


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

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                        //then redirect to LoanCollection List
                                        Intent i = new Intent(new Intent(VisitCompletionOfCustomerActivity.this, LoanCollectionActivity.class));
                                        int DPD_row_position = Integer.parseInt(Global.getStringFromSharedPref(VisitCompletionOfCustomerActivity.this,"DPD_row_position"));
                                        i.putExtra("DPD_row_position",DPD_row_position);
                                        i.putExtra("isFromFull_Partial_AmountPaid_CompleteNoChange","isFromFull_Partial_AmountPaid_CompleteNoChange");
                                        startActivity(i);

                                    }
                                },2000);


                            }catch(Exception e){
                                if(e.getLocalizedMessage()!=null){
                                    Log.d("Here Post CallDetailsException",e.getLocalizedMessage());
                                }
                                e.printStackTrace();
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

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                        //then redirect to LoanCollection List
                                        Intent i = new Intent(new Intent(VisitCompletionOfCustomerActivity.this, LoanCollectionActivity.class));
                                        int DPD_row_position = Integer.parseInt(Global.getStringFromSharedPref(VisitCompletionOfCustomerActivity.this,"DPD_row_position"));
                                        i.putExtra("DPD_row_position",DPD_row_position);
                                        i.putExtra("isFromFull_Partial_AmountPaid_CompleteNoChange","isFromFull_Partial_AmountPaid_CompleteNoChange");
                                        startActivity(i);

                                    }
                                },2000);


                            }catch(Exception e){
                                if(e.getLocalizedMessage()!=null){
                                    Log.d("Here Post CallDetailsException",e.getLocalizedMessage());
                                }
                                e.printStackTrace();
                            }

                        }

                   /* Intent i = new Intent(VisitCompletionOfCustomerActivity.this, NearByCustomersActivity.class);
                    startActivity(i);*/

                    }
                    else{

                        Global.showSnackBar(view,getString(R.string.check_internet_connection));
                    }

                    //(PAYMENT INFORMATION OF CUSTOMER) ALREADY PAID
                    if(getIntent().hasExtra("isAlreadyPaid")){
                        String dataSetId = getIntent().getStringExtra("dataSetId");

                        if(NetworkUtilities.getConnectivityStatus(this)){

                            callDetailsViewModel.postScheduledDateTime_AP(dataSetId,"","","","","");

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent i = new Intent(VisitCompletionOfCustomerActivity.this,MainActivity3API.class);
                                    startActivity(i);
                                }
                            },2000);

                        }
                        else{
                            Global.showSnackBar(view,getString(R.string.check_internet_connection));
                        }
                    }
                }



            }

            //Visits Flow (Not Ready to Pay Claims Payment Made)
            if(getIntent().hasExtra("NotReadyToPay_ClaimsPaymentMade")){
                String dataSetId = getIntent().getStringExtra("dataSetId");
                String claimsPaymentMade = WebServices.visit_not_ready_to_pay_claims_payment_made;
                if(NetworkUtilities.getConnectivityStatus(this)){
                    visitsFlowViewModel.postVisitsFlowCallDateTime(claimsPaymentMade,dataSetId,"","","","","");
                }
                else{
                    Global.showSnackBar(view,getString(R.string.check_internet_connection));
                }
            }
        });

        binding.btnCompleteNeedToUpdateDetails.setOnClickListener(v -> {

            Global.saveStringInSharedPref(this,"notes",""); //make Notes Empty After Complete

            //3 Scenarios 1) Calls For The Day Flow 2) Call Details Flow (NPA List) 3) Visits For The Day Flow

            //Visits For The Day Flow

            // VisitsForTheDay->Did Not Visit The Customer-> Payment Already Made
            if(getIntent().hasExtra("isFromVisitNPARescheduleActivity_payment_already_made")){
                String dataSetId = getIntent().getStringExtra("dataSetId");
                String payment_already_made_proceed = WebServices.visit_did_not_visit_payment_already_made;

                VisitsFlowCallDetailsActivity visitsFlowCallDetailsActivity = new VisitsFlowCallDetailsActivity();
                visitsFlowViewModel.postVisitsFlow_DidNotVisitTheCustomer(payment_already_made_proceed,dataSetId,"","",",","","","",visitsFlowCallDetailsActivity.sendCallLogDetailsList_VisitsFlow());
                navigateToDashBoard();

            }


            if(getIntent().hasExtra("isFromVisitsForTheDayFlow_Visit_NPA_PaymentModeActivity")){
                Global.showToast(this,"Payment Complete");

                String dataSetId = getIntent().getStringExtra("dataSetId");
                if(NetworkUtilities.getConnectivityStatus(this)){
                    if(getIntent().hasExtra("isVisitsReadyToPaySendLinkForOnlinePayment")){
                        String sendLinkForOnlinePayment = WebServices.visit_ready_to_pay_send_link_for_online_payment;
                        visitsFlowViewModel.postVisitsFlowCallDateTime(sendLinkForOnlinePayment,dataSetId,"","","","","");
                    }
                    else  if(getIntent().hasExtra("isVisitsReadyToPayCashPayment")){
                        String cashPayment = WebServices.visit_ready_to_pay_cash_payment;
                        String amountCollected = Global.getStringFromSharedPref(this,"Amount_Paid");
                        visitsFlowViewModel.postVisitsFlowCallDateTimeCheque_Cash(cashPayment,dataSetId,"","","","","",
                                amountCollected,"","","","");
                    }
                    else  if(getIntent().hasExtra("isVisitsReadyToPayChequePayment")){
                        String chequePayment = WebServices.visit_ready_to_pay_cheque_payment;
                        String amountCollected = Global.getStringFromSharedPref(this,"Amount_Paid");
                        String chequeDate = getIntent().getStringExtra("ChequeDate") ;
                        String chequeNumber = getIntent().getStringExtra("ChequeNumber");
                        String chequeAmount = getIntent().getStringExtra("ChequeAmount");
                        String bankName = getIntent().getStringExtra("BankName");

                        visitsFlowViewModel.postVisitsFlowCallDateTimeCheque_Cash(chequePayment,dataSetId,"","","","","",
                                amountCollected,chequeDate,chequeNumber,chequeAmount,bankName);
                    }

                }
                else{
                    Global.showSnackBar(view,getString(R.string.check_internet_connection));
                }

                Handler handler = new Handler();
                Runnable startVisitsActivity = new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(VisitCompletionOfCustomerActivity.this, NearByCustomersActivity.class);
                        startActivity(i);
                    }
                };
                handler.postDelayed(startVisitsActivity, 3000);

            }

            //Calls For the Day Flow
            if(CallsForTheDayAdapter.isFromCallsForTheDayAdapter !=null){
                Intent i = new Intent(this, CallsForTheDayActivity.class);
                startActivity(i);
                CallsForTheDayAdapter.isFromCallsForTheDayAdapter = null; // make it null to reset the flows
                System.out.println("Here isFromCallsForTheDayAdapter: "+CallsForTheDayAdapter.isFromCallsForTheDayAdapter);
            }

            else{

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

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                        //then redirect to LoanCollection List
                                        Intent i = new Intent(new Intent(VisitCompletionOfCustomerActivity.this, LoanCollectionActivity.class));
                                        int DPD_row_position = Integer.parseInt(Global.getStringFromSharedPref(VisitCompletionOfCustomerActivity.this,"DPD_row_position"));
                                        i.putExtra("DPD_row_position",DPD_row_position);
                                        startActivity(i);

                                    }
                                },2000);

                            }catch(Exception e){
                                if(e.getLocalizedMessage()!=null){
                                    Log.d("Here Post CallDetailsException",e.getLocalizedMessage());
                                }
                                e.printStackTrace();
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

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                        //then redirect to LoanCollection List
                                        Intent i = new Intent(new Intent(VisitCompletionOfCustomerActivity.this, LoanCollectionActivity.class));
                                        int DPD_row_position = Integer.parseInt(Global.getStringFromSharedPref(VisitCompletionOfCustomerActivity.this,"DPD_row_position"));
                                        i.putExtra("DPD_row_position",DPD_row_position);
                                        startActivity(i);

                                    }
                                },2000);

                            }catch(Exception e){
                                if(e.getLocalizedMessage()!=null){
                                    Log.d("Here Post CallDetailsException",e.getLocalizedMessage());
                                }
                                e.printStackTrace();
                            }

                        }

                   /* Intent i = new Intent(VisitCompletionOfCustomerActivity.this, NearByCustomersActivity.class);
                    startActivity(i);*/
                    }
                    else{

                        Global.showSnackBar(view,getString(R.string.check_internet_connection));
                    }

                    //(PAYMENT INFORMATION OF CUSTOMER) ALREADY PAID
                    if(getIntent().hasExtra("isAlreadyPaid")){
                        String dataSetId = getIntent().getStringExtra("dataSetId");

                        if(NetworkUtilities.getConnectivityStatus(this)){

                            callDetailsViewModel.postScheduledDateTime_AP(dataSetId,"","","","","");

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent i = new Intent(VisitCompletionOfCustomerActivity.this,MainActivity3API.class);
                                    startActivity(i);
                                }
                            },2000);
                        }
                        else{
                            Global.showSnackBar(view,getString(R.string.check_internet_connection));
                        }
                    }

                }

            }

            //Visits Flow (Not Ready to Pay Claims Payment Made)
            if(getIntent().hasExtra("NotReadyToPay_ClaimsPaymentMade")){
                String dataSetId = getIntent().getStringExtra("dataSetId");
                String claimsPaymentMade = WebServices.visit_not_ready_to_pay_claims_payment_made;
                if(NetworkUtilities.getConnectivityStatus(this)){
                    visitsFlowViewModel.postVisitsFlowCallDateTime(claimsPaymentMade,dataSetId,"","","","","");
                }
                else{
                    Global.showSnackBar(view,getString(R.string.check_internet_connection));
                }
            }


        });


        binding.btnCompleteEscalateToBM.setOnClickListener(v -> {

            Global.saveStringInSharedPref(this,"notes",""); //make Notes Empty After Complete

            //3 Scenarios 1) Calls For The Day Flow 2) Call Details Flow (NPA List) 3) Visits For The Day Flow

            //Visits For The Day Flow

            // VisitsForTheDay->Did Not Visit The Customer-> Payment Already Made
            if(getIntent().hasExtra("isFromVisitNPARescheduleActivity_payment_already_made")){
                String dataSetId = getIntent().getStringExtra("dataSetId");
                String payment_already_made_proceed = WebServices.visit_did_not_visit_payment_already_made;

                VisitsFlowCallDetailsActivity visitsFlowCallDetailsActivity = new VisitsFlowCallDetailsActivity();
                visitsFlowViewModel.postVisitsFlow_DidNotVisitTheCustomer(payment_already_made_proceed,dataSetId,"","",",","","","",visitsFlowCallDetailsActivity.sendCallLogDetailsList_VisitsFlow());
                navigateToDashBoard();

            }

            if(getIntent().hasExtra("isFromVisitsForTheDayFlow_Visit_NPA_PaymentModeActivity")){
                Global.showToast(this,"Payment Complete");

                String dataSetId = getIntent().getStringExtra("dataSetId");
                if(NetworkUtilities.getConnectivityStatus(this)){
                    if(getIntent().hasExtra("isVisitsReadyToPaySendLinkForOnlinePayment")){
                        String sendLinkForOnlinePayment = WebServices.visit_ready_to_pay_send_link_for_online_payment;
                        visitsFlowViewModel.postVisitsFlowCallDateTime(sendLinkForOnlinePayment,dataSetId,"","","","","");
                    }
                    else  if(getIntent().hasExtra("isVisitsReadyToPayCashPayment")){
                        String cashPayment = WebServices.visit_ready_to_pay_cash_payment;
                        String amountCollected = Global.getStringFromSharedPref(this,"Amount_Paid");
                        visitsFlowViewModel.postVisitsFlowCallDateTimeCheque_Cash(cashPayment,dataSetId,"","","","","",
                                amountCollected,"","","","");
                    }
                    else  if(getIntent().hasExtra("isVisitsReadyToPayChequePayment")){
                        String chequePayment = WebServices.visit_ready_to_pay_cheque_payment;
                        String amountCollected = Global.getStringFromSharedPref(this,"Amount_Paid");
                        String chequeDate = getIntent().getStringExtra("ChequeDate") ;
                        String chequeNumber = getIntent().getStringExtra("ChequeNumber");
                        String chequeAmount = getIntent().getStringExtra("ChequeAmount");
                        String bankName = getIntent().getStringExtra("BankName");

                        visitsFlowViewModel.postVisitsFlowCallDateTimeCheque_Cash(chequePayment,dataSetId,"","","","","",
                                amountCollected,chequeDate,chequeNumber,chequeAmount,bankName);
                    }

                }
                else{
                    Global.showSnackBar(view,getString(R.string.check_internet_connection));
                }

                Handler handler = new Handler();
                Runnable startVisitsActivity = new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(VisitCompletionOfCustomerActivity.this, NearByCustomersActivity.class);
                        startActivity(i);
                    }
                };
                handler.postDelayed(startVisitsActivity, 3000);

            }

            //Calls For the Day Flow
            if(CallsForTheDayAdapter.isFromCallsForTheDayAdapter !=null){
                Intent i = new Intent(this, CallsForTheDayActivity.class);
                startActivity(i);
                CallsForTheDayAdapter.isFromCallsForTheDayAdapter = null; // make it null to reset the flows
                System.out.println("Here isFromCallsForTheDayAdapter: "+CallsForTheDayAdapter.isFromCallsForTheDayAdapter);
            }

            else{

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

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                        //then redirect to LoanCollection List
                                        Intent i = new Intent(new Intent(VisitCompletionOfCustomerActivity.this, LoanCollectionActivity.class));
                                        int DPD_row_position = Integer.parseInt(Global.getStringFromSharedPref(VisitCompletionOfCustomerActivity.this,"DPD_row_position"));
                                        i.putExtra("DPD_row_position",DPD_row_position);
                                        startActivity(i);
                                    }
                                },2000);

                            }catch(Exception e){
                                if(e.getLocalizedMessage()!=null){
                                    Log.d("Here Post CallDetailsException",e.getLocalizedMessage());
                                }
                                e.printStackTrace();
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

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        //then redirect to LoanCollection List
                                        Intent i = new Intent(new Intent(VisitCompletionOfCustomerActivity.this, LoanCollectionActivity.class));
                                        int DPD_row_position = Integer.parseInt(Global.getStringFromSharedPref(VisitCompletionOfCustomerActivity.this,"DPD_row_position"));
                                        i.putExtra("DPD_row_position",DPD_row_position);
                                        startActivity(i);

                                    }
                                },2000);

                            }catch(Exception e){
                                if(e.getLocalizedMessage()!=null){
                                    Log.d("Here Post CallDetailsException",e.getLocalizedMessage());
                                }
                                e.printStackTrace();
                            }

                        }

                   /* Intent i = new Intent(VisitCompletionOfCustomerActivity.this, NearByCustomersActivity.class);
                    startActivity(i);*/
                    }
                    else{

                        Global.showSnackBar(view,getString(R.string.check_internet_connection));
                    }

                    //(PAYMENT INFORMATION OF CUSTOMER) ALREADY PAID
                    if(getIntent().hasExtra("isAlreadyPaid")){
                        String dataSetId = getIntent().getStringExtra("dataSetId");

                        if(NetworkUtilities.getConnectivityStatus(this)){

                            callDetailsViewModel.postScheduledDateTime_AP(dataSetId,"","","","","");

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent i = new Intent(VisitCompletionOfCustomerActivity.this,MainActivity3API.class);
                                    startActivity(i);
                                }
                            },2000);
                        }
                        else{
                            Global.showSnackBar(view,getString(R.string.check_internet_connection));
                        }
                    }

                }

            }


            //Visits Flow (Not Ready to Pay Claims Payment Made)
            if(getIntent().hasExtra("NotReadyToPay_ClaimsPaymentMade")){
                String dataSetId = getIntent().getStringExtra("dataSetId");
                String claimsPaymentMade = WebServices.visit_not_ready_to_pay_claims_payment_made;
                if(NetworkUtilities.getConnectivityStatus(this)){
                    visitsFlowViewModel.postVisitsFlowCallDateTime(claimsPaymentMade,dataSetId,"","","","","");
                }
                else{
                    Global.showSnackBar(view,getString(R.string.check_internet_connection));
                }
            }


        });


        //for Notes
        binding.ivNotesIcon.setOnClickListener(v -> {

            Global.showNotesEditDialog(this);
        });

        //for History
        binding.ivHistory.setOnClickListener(v -> {

            String dataSetId = getIntent().getStringExtra("dataSetId");
            Global.showNotesHistoryDialog(this,dataSetId);


        });

    }

    private void navigateToDashBoard(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(VisitCompletionOfCustomerActivity.this,MainActivity3API.class);
                startActivity(i);
            }
        },1000);

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