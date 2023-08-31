package com.example.test.npa_flow;

import static com.example.test.npa_flow.ScheduleVisitForCollectionActivity.scheduleVisitForCollection_dateTime;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.test.R;
import com.example.test.api_manager.WebServices;
import com.example.test.databinding.ActivitySubmitCompletionOfCustomerBinding;
import com.example.test.databinding.ActivityVisitNpaNotAvailableBinding;
import com.example.test.fragment_visits_flow.VisitsFlowCallDetailsActivity;
import com.example.test.fragment_visits_flow.VisitsFlowViewModel;
import com.example.test.fragments_activity.AlternateNumberApiCall;
import com.example.test.helper_classes.Global;
import com.example.test.helper_classes.NetworkUtilities;
import com.example.test.main_dashboard.MainActivity3API;
import com.example.test.npa_flow.call_details.CallDetailsViewModel;
import com.example.test.npa_flow.details_of_customer.DetailsOfCustomerActivity;
import com.example.test.npa_flow.details_of_customer.DetailsOfCustomerResponseModel;
import com.example.test.npa_flow.details_of_customer.DetailsOfCustomerViewModel;
import com.example.test.npa_flow.details_of_customer.adapter.DetailsOfCustomerAdapter;
import com.example.test.roomDB.dao.CallDetailsListDao;
import com.example.test.roomDB.dao.LeadCallDao;
import com.example.test.roomDB.dao.MPinDao;
import com.example.test.roomDB.dao.UserNameDao;
import com.example.test.roomDB.database.LeadListDB;
import com.example.test.roomDB.model.CallDetailsListRoomModel;

import java.util.ArrayList;
import java.util.List;

public class SubmitCompletionActivityOfCustomer extends AppCompatActivity {


    ActivitySubmitCompletionOfCustomerBinding binding;
    View view;
    DetailsOfCustomerViewModel detailsOfCustomerViewModel;
    ArrayList<DetailsOfCustomerResponseModel> detailsList;
    CallDetailsViewModel callDetailsViewModel;
    VisitsFlowViewModel visitsFlowViewModel;
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
        initObserverVisitsFlow();
    }

    private void setToolBarTitle(){
        if(getIntent().hasExtra("isFromVisitNPANotificationActivity") || getIntent().hasExtra("isFromVisitNPANotAvailableActivity")
         || getIntent().hasExtra("isFromVisitNPARescheduleActivity") || getIntent().hasExtra("isFromVisitNPAStatusActivity_Others")
         || getIntent().hasExtra("Visit_Npa_NotAvailable_NeedToCloseVisit")
        ){
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
        visitsFlowViewModel = new ViewModelProvider(this).get(VisitsFlowViewModel.class);
         setToolBarTitle();

         //coming from RadioButtonsCloseVisitAdapter(Visit_NPA_NotAvailableActivity - btnNeedToCloseVisit)
         //display only 2 buttons ScheduleCall & ScheduleVisit
        if(getIntent().hasExtra("Visit_Npa_NotAvailable_NeedToCloseVisit")){
            if(getIntent().hasExtra("Visit_NPA_NotAvailableActivity_NeedToCloseVisit_PaymentAlreadyMade")){
                binding.btnSubmit.setVisibility(View.VISIBLE);
            }
            else{
                binding.btnSubmit.setVisibility(View.GONE);
            }

        }

        //coming from NotSpokeToCustomer - NumberIsInvalid , hide ScheduleCall Button
        if(getIntent().hasExtra("isFromNotSpokeToCustomer_NumberInvalid")){
            binding.btnSubmitScheduleACall.setVisibility(View.GONE);
        }

         //initially button are Clickable
         binding.btnSubmitNoChange.setClickable(true);
         binding.btnSubmitNeedToUpdateDetails.setClickable(true);
         binding.btnSubmitEscalateToBM.setClickable(true);

        //get detailsList
        detailsList = (ArrayList<DetailsOfCustomerResponseModel>) getIntent().getSerializableExtra("detailsList");
        detailsList = (ArrayList<DetailsOfCustomerResponseModel>) Global.getUpdatedDetailsList(detailsList); //to get Updated List

        relativeName = getIntent().getStringExtra("relativeName");
        relativeContact =getIntent().getStringExtra("relativeContact");
        dateOfVisitPromised = getIntent().getStringExtra("dateOfVisitPromised");
        foName = getIntent().getStringExtra("foName");


        // Get UserName , UserID , BranchCode
        MPinDao mPinDao = LeadListDB.getInstance(this).mPinDao();
        UserNameDao userNameDao = LeadListDB.getInstance(this).userNameDao();
        String userName = userNameDao.getUserNameUsingUserIDInUserNameRoomDB(mPinDao.getUserID());
        // Store UserName in SharedPreference and Use in StatusOfCustomerDetailsAdapter
        Global.saveStringInSharedPref(this,"userName",userName);

        MainActivity3API.UserID = mPinDao.getUserID();
        MainActivity3API.BranchCode = mPinDao.getBranchCode();

        System.out.println("Here SubmitCompletionActivityOfCustomer initializeFields UserID:"+MainActivity3API.UserID);
        System.out.println("Here SubmitCompletionActivityOfCustomer initializeFields BranchCode:"+MainActivity3API.BranchCode);

    }

    private void initObserver(){

        if(NetworkUtilities.getConnectivityStatus(this)) {
            callDetailsViewModel.getMutCallDetailsResponseApi().observe(this, result -> {

                if(result!=null){
                   // Global.showToast(this,"Server Response:"+result);
                    Global.showSnackBar(view, result);
                    System.out.println("Here Server Response: "+result);
                }
                if(result==null){
                   // Global.showToast(this,"Server Response: Null");
                    System.out.println("Here Server Response: "+result);
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

        if(NetworkUtilities.getConnectivityStatus(this)){

            visitsFlowViewModel.getMutVisitsCallDetailsResponseApi().observe(this,result->{

                if(result!=null){
                   // Global.showToast(this,"Server Response:"+result);
                    Global.showSnackBar(view, result);
                    System.out.println("Here Server Response: "+result);
                }
                if(result==null){
                    //Global.showToast(this,"Server Response: Null");
                    System.out.println("Here Server Response: "+result);
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

    private void setUpDetailsOfCustomerRecyclerView() {

        detailsOfCustomerViewModel.updateDetailsOfCustomer_Data();
        RecyclerView recyclerView = binding.rvDetailsOfCustomer;
        recyclerView.setAdapter(new DetailsOfCustomerAdapter(this,detailsList));
    }

    private void onClickListener() {

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Call Save Alternate Number API
                if(null!= DetailsOfCustomerAdapter.alternateNumber && null!=DetailsOfCustomerAdapter.dataSetId){
                    System.out.println("Save Alternate No. Api dataSetId:"+DetailsOfCustomerAdapter.dataSetId+" Alternate No. :"+DetailsOfCustomerAdapter.alternateNumber);
                    AlternateNumberApiCall.saveAlternateNumber(SubmitCompletionActivityOfCustomer.this,DetailsOfCustomerAdapter.alternateNumber,DetailsOfCustomerAdapter.dataSetId);
                }
                onBackPressed();
            }
        });

        binding.ivHome.setOnClickListener(v -> {
            //Call Save Alternate Number API
            if(null!= DetailsOfCustomerAdapter.alternateNumber && null!=DetailsOfCustomerAdapter.dataSetId){
                System.out.println("Save Alternate No. Api dataSetId:"+DetailsOfCustomerAdapter.dataSetId+" Alternate No. :"+DetailsOfCustomerAdapter.alternateNumber);
                AlternateNumberApiCall.saveAlternateNumber(this,DetailsOfCustomerAdapter.alternateNumber,DetailsOfCustomerAdapter.dataSetId);
            }
            startActivity(new Intent(this, MainActivity3API.class));
        });

        //for Notes
        binding.ivNotesIcon.setOnClickListener(v -> {

            //coming from NPA/CallsForTheDay
            if(Global.getStringFromSharedPref(this,"backToMemberList").equals("1") || Global.getStringFromSharedPref(this,"backToMemberList").equals("2")) {
                Global.showNotesEditDialog(this);
            }
            //coming from VisitsForTheDayFlow
            else if(Global.getStringFromSharedPref(this,"backToMemberList").equals("3")){
                Global.showNotesEditDialogVisits(this);
            }

        });

        //for History
        binding.ivHistory.setOnClickListener(v -> {

            String dataSetId = getIntent().getStringExtra("dataSetId");
            Global.showNotesHistoryDialog(this,dataSetId);

        });


        binding.btnSubmitNoChange.setOnClickListener(v-> {

            //Call Save Alternate Number API
            if(null!= DetailsOfCustomerAdapter.alternateNumber && null!=DetailsOfCustomerAdapter.dataSetId){
                System.out.println("Save Alternate No. Api dataSetId:"+DetailsOfCustomerAdapter.dataSetId+" Alternate No. :"+DetailsOfCustomerAdapter.alternateNumber);
                AlternateNumberApiCall.saveAlternateNumber(this,DetailsOfCustomerAdapter.alternateNumber,DetailsOfCustomerAdapter.dataSetId);
            }

            //to check if Submit Button is clicked Once or not
            if (binding.btnSubmitNoChange.isClickable())
                System.out.println("Here Submit is Clickable:true");
            {

                binding.btnSubmitNoChange.setClickable(false);
                System.out.println("Here Submit is Clickable:false");


                //VisitForTheDay-> Visited The Customer -> Others
                if(getIntent().hasExtra("isFromVisitNPAStatusActivity_Others")){
                    String dataSetId = getIntent().getStringExtra("dataSetId");
                    String reason = getIntent().getStringExtra("reason");
                    String visitedTheCustomer_Others = WebServices.visit_others;
                    visitsFlowViewModel.postVisitsFlowCallDateTime_Others(visitedTheCustomer_Others,dataSetId,"","","","","",reason);
                    navigateToNearByCustomerActivity();
                }

            //NotSpokeToCustomerActivity - Number is Invalid
            if (getIntent().hasExtra("isFromNotSpokeToCustomer_NumberInvalid")) {
                String dataSetId = getIntent().getStringExtra("dataSetId");
                callDetailsViewModel.postCallDetailsNotSpokeToCustomer_NumberInvalid(dataSetId);
                navigateToDashBoard();
            }



            //Visit-NPANotAvailable -> Others -> Skip & Proceed
            if (getIntent().hasExtra("isFromVisitNPANotAvailableActivity_Others")) {
                String dataSetId = getIntent().getStringExtra("dataSetId");
                String others_SkipAndProceed = WebServices.visit_rescheduled_others;

                VisitsFlowCallDetailsActivity visitsFlowCallDetailsActivity = new VisitsFlowCallDetailsActivity();
                visitsFlowViewModel.postVisitsFlow_DidNotVisitTheCustomer(others_SkipAndProceed, dataSetId, "", "", "", "", "", VisitsFlowCallDetailsActivity.send_reason, visitsFlowCallDetailsActivity.sendCallLogDetailsList_VisitsFlow());
                navigateToDashBoard();
            }

            //Visit-NPANotAvailable -> Late For Visit -> Skip & Proceed
            else if (getIntent().hasExtra("isFromVisitNPANotAvailableActivity_LateForVisit")) {

                String dataSetId = getIntent().getStringExtra("dataSetId");
                String lateForVisit_SkipAndProceed = WebServices.visit_rescheduled_late_for_visit_skip_and_proceed;

                VisitsFlowCallDetailsActivity visitsFlowCallDetailsActivity = new VisitsFlowCallDetailsActivity();
                visitsFlowViewModel.postVisitsFlow_DidNotVisitTheCustomer(lateForVisit_SkipAndProceed, dataSetId, "", "", "", "", "", VisitsFlowCallDetailsActivity.send_reason, visitsFlowCallDetailsActivity.sendCallLogDetailsList_VisitsFlow());
                navigateToDashBoard();
            }

            //Visit-NPANotAvailable->Customer Not Available -> Skip & Proceed
            else if (getIntent().hasExtra("isFromVisitNPANotAvailableActivity_CustomerNotAvailable")) {

                String dataSetId = getIntent().getStringExtra("dataSetId");
                String customerNotAvailable_SkipAndProceed = WebServices.visit_rescheduled_customer_not_available_skip_and_proceed;

                VisitsFlowCallDetailsActivity visitsFlowCallDetailsActivity = new VisitsFlowCallDetailsActivity();
                visitsFlowViewModel.postVisitsFlow_DidNotVisitTheCustomer(customerNotAvailable_SkipAndProceed, dataSetId, "", "", "", "", "", "", visitsFlowCallDetailsActivity.sendCallLogDetailsList_VisitsFlow());
                navigateToDashBoard();
            }

            //PAYMENT INFO OF CUSTOMER -> OTHERS
            if (getIntent().hasExtra("isPaymentInfoOfCustomerActivity_Others")) {

                String dataSetId = getIntent().getStringExtra("dataSetId");
                String reason = getIntent().getStringExtra("reason");

                String payment_info_of_customer_others =   WebServices.call_details_payment_info_others;
                callDetailsViewModel.postScheduledDateTime_OTHERS(payment_info_of_customer_others,dataSetId, "", "", "", "", "",reason);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(SubmitCompletionActivityOfCustomer.this, MainActivity3API.class);
                        startActivity(i);
                    }
                }, 1000);

            }

            //PAYMENT INFO OF CUSTOMER-> NOT TAKEN LOAN
            if (getIntent().hasExtra("isPaymentInfoOfCustomerActivity_NotTakenLoan")) {

                String dataSetId = getIntent().getStringExtra("dataSetId");
                String payment_info_not_taken_loan = WebServices.call_details_payment_info_not_taken_loan;

                callDetailsViewModel.postScheduledDateTime_OTHERS(payment_info_not_taken_loan,dataSetId, "", "", "", "", "","");
                navigateToDashBoard();
            }

            //PAYMENT  NOTIFICATION OF CUSTOMER ->OTHERS
            if (getIntent().hasExtra("isPaymentNotificationOfCustomer_Others")) {

                String dataSetId = getIntent().getStringExtra("dataSetId");
                String reason = getIntent().getStringExtra("reason");

                String spoke_to_customer_others = WebServices.call_details_spoke_to_customer_others;
                callDetailsViewModel.postScheduledDateTime_OTHERS(spoke_to_customer_others,dataSetId, "", "", "", "", "",reason);
                navigateToDashBoard();
            }

            //ALREADY PAID
            if (getIntent().hasExtra("isAlreadyPaid")) {
                String dataSetId = getIntent().getStringExtra("dataSetId");
                callDetailsViewModel.postScheduledDateTime_AP(dataSetId, "", "", "", "", "");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(SubmitCompletionActivityOfCustomer.this, MainActivity3API.class);
                        startActivity(i);
                    }
                }, 1000);

            }

            //FO NOT VISITED
            if (getIntent().hasExtra("isFoNotVisited")) {
                String dataSetId = getIntent().getStringExtra("dataSetId");
                //  dateOfVisitPromised = getIntent().getStringExtra("dateOfVisitPromised");
                //  foName = getIntent().getStringExtra("foName");

                if (NetworkUtilities.getConnectivityStatus(this)) {
                    callDetailsViewModel.postScheduledDateTime_FNV(dataSetId, "", dateOfVisitPromised, foName, "", "");

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent i = new Intent(SubmitCompletionActivityOfCustomer.this, MainActivity3API.class);
                            startActivity(i);
                        }
                    }, 1000);

                } else {
                    Global.showSnackBar(view, getString(R.string.check_internet_connection));
                }

            }

            //LOAN TAKEN BY RELATIVE
            if (getIntent().hasExtra("isLoanTakenByRelative")) {
                String dataSetId = getIntent().getStringExtra("dataSetId");
                //  relativeName = getIntent().getStringExtra("relativeName");
                //  relativeContact = getIntent().getStringExtra("relativeContact");

                if (NetworkUtilities.getConnectivityStatus(this)) {
                    callDetailsViewModel.postScheduledDateTime_LTBR(dataSetId, "", "", "", relativeName, relativeContact, "LTBR");

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent i = new Intent(SubmitCompletionActivityOfCustomer.this, MainActivity3API.class);
                            startActivity(i);
                        }
                    }, 1000);

                } else {
                    Global.showSnackBar(view, getString(R.string.check_internet_connection));
                }
            }


            //PAYMENT INFO ->WILL PAY LUMPSUM
            if (getIntent().hasExtra("isPaymentInfoOfCustomerActivity_WillPayLumpSum")) {
                String dataSetId = getIntent().getStringExtra("dataSetId");
                if (NetworkUtilities.getConnectivityStatus(this)) {
                    callDetailsViewModel.postScheduledDateTime_WPLS(dataSetId, "", "", "", "", "");
                    navigateToDashBoard();
                } else {
                    Global.showSnackBar(view, getString(R.string.check_internet_connection));
                }
            }

            //VISITS FOR THE DAY FLOW

            //Visits Flow (Not Ready to Pay Lack Of Funds)
            if (getIntent().hasExtra("NotReadyToPay_LackOfFunds")) {
                String dataSetId = getIntent().getStringExtra("dataSetId");
                String lackOfFunds = WebServices.visit_not_ready_to_pay_lack_of_funds;
                if (NetworkUtilities.getConnectivityStatus(this)) {
                    visitsFlowViewModel.postVisitsFlowCallDateTime(lackOfFunds, dataSetId, "", "", "", "", "");
                     navigateToNearByCustomerActivity();
                } else {
                    Global.showSnackBar(view, getString(R.string.check_internet_connection));
                }
            }

            //Visits Flow (Not Ready to Pay Not Taken Loan)
            if (getIntent().hasExtra("NotReadyToPay_NotTakenLoan")) {
                String dataSetId = getIntent().getStringExtra("dataSetId");
                String notTakenLoan = WebServices.visit_not_ready_to_pay_not_taken_loan;
                if (NetworkUtilities.getConnectivityStatus(this)) {
                    visitsFlowViewModel.postVisitsFlowCallDateTime(notTakenLoan, dataSetId, "", "", "", "", "");
                      navigateToNearByCustomerActivity();
                } else {
                    Global.showSnackBar(view, getString(R.string.check_internet_connection));
                }
            }

            //Visits Flow (Not Ready to Pay Loan Taken By Relative)
            if (getIntent().hasExtra("NotReadyToPay_LoanTakenByRelative")) {
                String dataSetId = getIntent().getStringExtra("dataSetId");
                String relativeName = getIntent().getStringExtra("relativeName");
                String relativeContact = getIntent().getStringExtra("relativeContact");
                String loanTakenByRelative = WebServices.visit_not_ready_to_pay_loan_taken_by_relative;
                if (NetworkUtilities.getConnectivityStatus(this)) {
                    visitsFlowViewModel.postVisitsFlowCallDateTime(loanTakenByRelative, dataSetId, "", "", "", relativeName, relativeContact);
                    navigateToNearByCustomerActivity();
                } else {
                    Global.showSnackBar(view, getString(R.string.check_internet_connection));
                }
            }

            //Visits Flow (Not Ready to Pay Will Pay LumpSump)
            if (getIntent().hasExtra("NotReadyToPay_WillPayLumpSump")) {
                String dataSetId = getIntent().getStringExtra("dataSetId");
                String willPayLumpSump = WebServices.visit_not_ready_to_pay_will_pay_lumpsump;
                if (NetworkUtilities.getConnectivityStatus(this)) {
                    visitsFlowViewModel.postVisitsFlowCallDateTime(willPayLumpSump, dataSetId, "", "", "", "", "");
                     navigateToNearByCustomerActivity();
                } else {
                    Global.showSnackBar(view, getString(R.string.check_internet_connection));
                }
            }

            //Visits Flow (Not Ready to Pay Claims Payment Made)
                //<!--Claims Payment Made Renamed to Payment Already Made as text on button-->
            if (getIntent().hasExtra("NotReadyToPay_ClaimsPaymentMade")) {
                String dataSetId = getIntent().getStringExtra("dataSetId");
                String claimsPaymentMade = WebServices.visit_not_ready_to_pay_claims_payment_made;
                if (NetworkUtilities.getConnectivityStatus(this)) {
                    visitsFlowViewModel.postVisitsFlowCallDateTime(claimsPaymentMade, dataSetId, "", "", "", "", "");
                    navigateToDashBoard();
                } else {
                    Global.showSnackBar(view, getString(R.string.check_internet_connection));
                }
            }

                //VisitFlow-NotReadyToPay - Others
                if(getIntent().hasExtra("isFromVisitNPANotificationActivity_Others")){
                    String dataSetId = getIntent().getStringExtra("dataSetId");
                    String reason = getIntent().getStringExtra("reason");

                    String visited_the_customer_not_ready_to_pay_others = WebServices.visit_not_ready_to_pay_others;
                    visitsFlowViewModel.postVisitsFlowCallDateTime_Others(visited_the_customer_not_ready_to_pay_others,dataSetId,"","","","","",reason);
                    navigateToNearByCustomerActivity();
                }

                //Visit_NPA_NotAvailableActivity - NeedToCloseVisit - PaymentAlreadyMade
                if(getIntent().hasExtra("Visit_NPA_NotAvailableActivity_NeedToCloseVisit_PaymentAlreadyMade")){
                    String dataSetId = getIntent().getStringExtra("dataSetId");
                    VisitsFlowCallDetailsActivity visitsFlowCallDetailsActivity = new VisitsFlowCallDetailsActivity();
                    visitsFlowViewModel.postScheduleCall_ScheduleVisit(WebServices.needToCloseVisit,dataSetId,"","","","","",VisitsFlowCallDetailsActivity.send_reason,"","","","","",visitsFlowCallDetailsActivity.sendCallLogDetailsList_VisitsFlow());
                     navigateToDashBoard();
                }

        }// button SubmitNoChange is Clicked Once or not ends here

        });

        //kept Hidden for Temporary Basis
        binding.btnSubmitNeedToUpdateDetails.setOnClickListener(v-> {

            //Call Save Alternate Number API
            if(null!= DetailsOfCustomerAdapter.alternateNumber && null!=DetailsOfCustomerAdapter.dataSetId){
                System.out.println("Save Alternate No. Api dataSetId:"+DetailsOfCustomerAdapter.dataSetId+" Alternate No. :"+DetailsOfCustomerAdapter.alternateNumber);
                AlternateNumberApiCall.saveAlternateNumber(this,DetailsOfCustomerAdapter.alternateNumber,DetailsOfCustomerAdapter.dataSetId);
            }

            if (binding.btnSubmitNeedToUpdateDetails.isClickable())

            {
                System.out.println("Here Submit Button is Clickable:true");

                binding.btnSubmitNeedToUpdateDetails.setClickable(false);
                System.out.println("Here Submit Button is Clickable:false");

                //VisitForTheDay-> Visited The Customer -> Others
                if(getIntent().hasExtra("isFromVisitNPAStatusActivity_Others")){
                    String dataSetId = getIntent().getStringExtra("dataSetId");
                    String reason = getIntent().getStringExtra("reason");
                    String visitedTheCustomer_Others = WebServices.visit_others;
                    visitsFlowViewModel.postVisitsFlowCallDateTime_Others(visitedTheCustomer_Others,dataSetId,"","","","","",reason);
                    navigateToNearByCustomerActivity();
                }

            //NotSpokeToCustomerActivity - Number is Invalid
            if (getIntent().hasExtra("isFromNotSpokeToCustomer_NumberInvalid")) {
                String dataSetId = getIntent().getStringExtra("dataSetId");
                callDetailsViewModel.postCallDetailsNotSpokeToCustomer_NumberInvalid(dataSetId);
                navigateToDashBoard();
            }



            //Visit-NPANotAvailable -> Others -> Skip & Proceed
            if (getIntent().hasExtra("isFromVisitNPANotAvailableActivity_Others")) {
                String dataSetId = getIntent().getStringExtra("dataSetId");
                String others_SkipAndProceed = WebServices.visit_rescheduled_others;

                VisitsFlowCallDetailsActivity visitsFlowCallDetailsActivity = new VisitsFlowCallDetailsActivity();

                visitsFlowViewModel.postVisitsFlow_DidNotVisitTheCustomer(others_SkipAndProceed, dataSetId, "", "", "", "", "", VisitsFlowCallDetailsActivity.send_reason, visitsFlowCallDetailsActivity.sendCallLogDetailsList_VisitsFlow());
                navigateToDashBoard();
            }

            //Visit-NPANotAvailable -> Late For Visit -> Skip & Proceed
            else if (getIntent().hasExtra("isFromVisitNPANotAvailableActivity_LateForVisit")) {

                String dataSetId = getIntent().getStringExtra("dataSetId");
                String lateForVisit_SkipAndProceed = WebServices.visit_rescheduled_late_for_visit_skip_and_proceed;

                VisitsFlowCallDetailsActivity visitsFlowCallDetailsActivity = new VisitsFlowCallDetailsActivity();

                visitsFlowViewModel.postVisitsFlow_DidNotVisitTheCustomer(lateForVisit_SkipAndProceed, dataSetId, "", "", "", "", "", VisitsFlowCallDetailsActivity.send_reason, visitsFlowCallDetailsActivity.sendCallLogDetailsList_VisitsFlow());
                navigateToDashBoard();
            }

            //Visit-NPANotAvailable->Customer Not Available -> Skip & Proceed
            else if (getIntent().hasExtra("isFromVisitNPANotAvailableActivity_CustomerNotAvailable")) {

                String dataSetId = getIntent().getStringExtra("dataSetId");
                String customerNotAvailable_SkipAndProceed = WebServices.visit_rescheduled_customer_not_available_skip_and_proceed;

                VisitsFlowCallDetailsActivity visitsFlowCallDetailsActivity = new VisitsFlowCallDetailsActivity();
                visitsFlowViewModel.postVisitsFlow_DidNotVisitTheCustomer(customerNotAvailable_SkipAndProceed, dataSetId, "", "", "", "", "", "", visitsFlowCallDetailsActivity.sendCallLogDetailsList_VisitsFlow());
                navigateToDashBoard();
            }

            //PAYMENT INFO OF CUSTOMER -> OTHERS
            if (getIntent().hasExtra("isPaymentInfoOfCustomerActivity_Others")) {

                String dataSetId = getIntent().getStringExtra("dataSetId");
                String reason = getIntent().getStringExtra("reason");

                String payment_info_of_customer_others =   WebServices.call_details_payment_info_others;

                callDetailsViewModel.postScheduledDateTime_OTHERS(payment_info_of_customer_others,dataSetId, "", "", "", "", "",reason);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(SubmitCompletionActivityOfCustomer.this, MainActivity3API.class);
                        startActivity(i);
                    }
                }, 1000);
            }

            //PAYMENT INFO OF CUSTOMER-> NOT TAKEN LOAN
            if (getIntent().hasExtra("isPaymentInfoOfCustomerActivity_NotTakenLoan")) {
                String dataSetId = getIntent().getStringExtra("dataSetId");
                String payment_info_not_taken_loan = WebServices.call_details_payment_info_not_taken_loan;

                callDetailsViewModel.postScheduledDateTime_OTHERS(payment_info_not_taken_loan,dataSetId, "", "", "", "", "","");
                navigateToDashBoard();
            }

            //PAYMENT  NOTIFICATION OF CUSTOMER ->OTHERS
            if (getIntent().hasExtra("isPaymentNotificationOfCustomer_Others")) {

                String dataSetId = getIntent().getStringExtra("dataSetId");
                String reason = getIntent().getStringExtra("reason");

                String spoke_to_customer_others = WebServices.call_details_spoke_to_customer_others;

                callDetailsViewModel.postScheduledDateTime_OTHERS(spoke_to_customer_others,dataSetId, "", "", "", "", "",reason);
                navigateToDashBoard();
            }

            //ALREADY PAID
            if (getIntent().hasExtra("isAlreadyPaid")) {
                String dataSetId = getIntent().getStringExtra("dataSetId");
                callDetailsViewModel.postScheduledDateTime_AP(dataSetId, "", "", "", "", "");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(SubmitCompletionActivityOfCustomer.this, MainActivity3API.class);
                        startActivity(i);
                    }
                }, 1000);

            }

            //FO NOT VISITED
            if (getIntent().hasExtra("isFoNotVisited")) {
                String dataSetId = getIntent().getStringExtra("dataSetId");
                //  dateOfVisitPromised = getIntent().getStringExtra("dateOfVisitPromised");
                //  foName = getIntent().getStringExtra("foName");

                if (NetworkUtilities.getConnectivityStatus(this)) {
                    callDetailsViewModel.postScheduledDateTime_FNV(dataSetId, "", dateOfVisitPromised, foName, "", "");

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent i = new Intent(SubmitCompletionActivityOfCustomer.this, MainActivity3API.class);
                            startActivity(i);
                        }
                    }, 1000);
                } else {
                    Global.showSnackBar(view, getString(R.string.check_internet_connection));
                }

            }

            //LOAN TAKEN BY RELATIVE
            if (getIntent().hasExtra("isLoanTakenByRelative")) {
                String dataSetId = getIntent().getStringExtra("dataSetId");
                //  relativeName = getIntent().getStringExtra("relativeName");
                //  relativeContact = getIntent().getStringExtra("relativeContact");

                if (NetworkUtilities.getConnectivityStatus(this)) {
                    callDetailsViewModel.postScheduledDateTime_LTBR(dataSetId, "", "", "", relativeName, relativeContact, "LTBR");

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent i = new Intent(SubmitCompletionActivityOfCustomer.this, MainActivity3API.class);
                            startActivity(i);
                        }
                    }, 1000);
                } else {
                    Global.showSnackBar(view, getString(R.string.check_internet_connection));
                }
            }


            //PAYMENT INFO ->WILL PAY LUMPSUM
            if (getIntent().hasExtra("isPaymentInfoOfCustomerActivity_WillPayLumpSum")) {
                String dataSetId = getIntent().getStringExtra("dataSetId");
                if (NetworkUtilities.getConnectivityStatus(this)) {
                    callDetailsViewModel.postScheduledDateTime_WPLS(dataSetId, "", "", "", "", "");
                    navigateToDashBoard();
                } else {
                    Global.showSnackBar(view, getString(R.string.check_internet_connection));
                }
            }

            //VISITS FOR THE DAY FLOW

            //Visits Flow (Not Ready to Pay Lack Of Funds)
            if (getIntent().hasExtra("NotReadyToPay_LackOfFunds")) {
                String dataSetId = getIntent().getStringExtra("dataSetId");
                String lackOfFunds = WebServices.visit_not_ready_to_pay_lack_of_funds;
                if (NetworkUtilities.getConnectivityStatus(this)) {
                    visitsFlowViewModel.postVisitsFlowCallDateTime(lackOfFunds, dataSetId, "", "", "", "", "");
                } else {
                    Global.showSnackBar(view, getString(R.string.check_internet_connection));
                }
            }

            //Visits Flow (Not Ready to Pay Not Taken Loan)
            if (getIntent().hasExtra("NotReadyToPay_NotTakenLoan")) {
                String dataSetId = getIntent().getStringExtra("dataSetId");
                String notTakenLoan = WebServices.visit_not_ready_to_pay_not_taken_loan;
                if (NetworkUtilities.getConnectivityStatus(this)) {
                    visitsFlowViewModel.postVisitsFlowCallDateTime(notTakenLoan, dataSetId, "", "", "", "", "");
                        navigateToNearByCustomerActivity();
                } else {
                    Global.showSnackBar(view, getString(R.string.check_internet_connection));
                }
            }

            //Visits Flow (Not Ready to Pay Loan Taken By Relative)
            if (getIntent().hasExtra("NotReadyToPay_LoanTakenByRelative")) {
                String dataSetId = getIntent().getStringExtra("dataSetId");
                String relativeName = getIntent().getStringExtra("relativeName");
                String relativeContact = getIntent().getStringExtra("relativeContact");
                String loanTakenByRelative = WebServices.visit_not_ready_to_pay_loan_taken_by_relative;
                if (NetworkUtilities.getConnectivityStatus(this)) {
                    visitsFlowViewModel.postVisitsFlowCallDateTime(loanTakenByRelative, dataSetId, "", "", "", relativeName, relativeContact);
                } else {
                    Global.showSnackBar(view, getString(R.string.check_internet_connection));
                }
            }

            //Visits Flow (Not Ready to Pay Will Pay LumpSump)
            if (getIntent().hasExtra("NotReadyToPay_WillPayLumpSump")) {
                String dataSetId = getIntent().getStringExtra("dataSetId");
                String willPayLumpSump = WebServices.visit_not_ready_to_pay_will_pay_lumpsump;
                if (NetworkUtilities.getConnectivityStatus(this)) {
                    visitsFlowViewModel.postVisitsFlowCallDateTime(willPayLumpSump, dataSetId, "", "", "", "", "");
                } else {
                    Global.showSnackBar(view, getString(R.string.check_internet_connection));
                }
            }


            //Visits Flow (Not Ready to Pay Claims Payment Made)
                //<!--Claims Payment Made Renamed to Payment Already Made as text on button-->
            if (getIntent().hasExtra("NotReadyToPay_ClaimsPaymentMade")) {
                String dataSetId = getIntent().getStringExtra("dataSetId");
                String claimsPaymentMade = WebServices.visit_not_ready_to_pay_claims_payment_made;
                if (NetworkUtilities.getConnectivityStatus(this)) {
                    visitsFlowViewModel.postVisitsFlowCallDateTime(claimsPaymentMade, dataSetId, "", "", "", "", "");
                    navigateToDashBoard();
                } else {
                    Global.showSnackBar(view, getString(R.string.check_internet_connection));
                }
            }


                //VisitFlow-NotReadyToPay - Others
                if(getIntent().hasExtra("isFromVisitNPANotificationActivity_Others")){
                    String dataSetId = getIntent().getStringExtra("dataSetId");
                    String reason = getIntent().getStringExtra("reason");

                    String visited_the_customer_not_ready_to_pay_others = WebServices.visit_not_ready_to_pay_others;
                    visitsFlowViewModel.postVisitsFlowCallDateTime_Others(visited_the_customer_not_ready_to_pay_others,dataSetId,"","","","","",reason);
                    navigateToNearByCustomerActivity();
                }

                //Visit_NPA_NotAvailableActivity - NeedToCloseVisit - PaymentAlreadyMade
                if(getIntent().hasExtra("Visit_NPA_NotAvailableActivity_NeedToCloseVisit_PaymentAlreadyMade")){
                    String dataSetId = getIntent().getStringExtra("dataSetId");
                    VisitsFlowCallDetailsActivity visitsFlowCallDetailsActivity = new VisitsFlowCallDetailsActivity();
                    visitsFlowViewModel.postScheduleCall_ScheduleVisit(WebServices.needToCloseVisit,dataSetId,"","","","","",VisitsFlowCallDetailsActivity.send_reason,"","","","","",visitsFlowCallDetailsActivity.sendCallLogDetailsList_VisitsFlow());
                    navigateToDashBoard();
                }

        }//button SubmitNeedToUpdateDetails is clicked once or not ends here

        });

        //kept Hidden for Temporary Basis
        binding.btnSubmitEscalateToBM.setOnClickListener(v-> {

            //Call Save Alternate Number API
            if(null!= DetailsOfCustomerAdapter.alternateNumber && null!=DetailsOfCustomerAdapter.dataSetId){
                System.out.println("Save Alternate No. Api dataSetId:"+DetailsOfCustomerAdapter.dataSetId+" Alternate No. :"+DetailsOfCustomerAdapter.alternateNumber);
                AlternateNumberApiCall.saveAlternateNumber(this,DetailsOfCustomerAdapter.alternateNumber,DetailsOfCustomerAdapter.dataSetId);
            }

            if (binding.btnSubmitEscalateToBM.isClickable())

            {
                System.out.println("Here Submit is Clickable:true");
                binding.btnSubmitEscalateToBM.setClickable(false);
                System.out.println("Here Submit is Clickable:false");

                //VisitForTheDay-> Visited The Customer -> Others
                if(getIntent().hasExtra("isFromVisitNPAStatusActivity_Others")){
                    String dataSetId = getIntent().getStringExtra("dataSetId");
                    String reason = getIntent().getStringExtra("reason");
                    String visitedTheCustomer_Others = WebServices.visit_others;
                    visitsFlowViewModel.postVisitsFlowCallDateTime_Others(visitedTheCustomer_Others,dataSetId,"","","","","",reason);
                    navigateToNearByCustomerActivity();
                }

            //NotSpokeToCustomerActivity - Number is Invalid
            if (getIntent().hasExtra("isFromNotSpokeToCustomer_NumberInvalid")) {
                String dataSetId = getIntent().getStringExtra("dataSetId");
                callDetailsViewModel.postCallDetailsNotSpokeToCustomer_NumberInvalid(dataSetId);
                navigateToDashBoard();
            }



            //Visit-NPANotAvailable -> Others -> Skip & Proceed
            if (getIntent().hasExtra("isFromVisitNPANotAvailableActivity_Others")) {
                String dataSetId = getIntent().getStringExtra("dataSetId");
                String others_SkipAndProceed = WebServices.visit_rescheduled_others;

                VisitsFlowCallDetailsActivity visitsFlowCallDetailsActivity = new VisitsFlowCallDetailsActivity();
                visitsFlowViewModel.postVisitsFlow_DidNotVisitTheCustomer(others_SkipAndProceed, dataSetId, "", "", "", "", "", VisitsFlowCallDetailsActivity.send_reason, visitsFlowCallDetailsActivity.sendCallLogDetailsList_VisitsFlow());
                navigateToDashBoard();
            }

            //Visit-NPANotAvailable -> Late For Visit -> Skip & Proceed
            else if (getIntent().hasExtra("isFromVisitNPANotAvailableActivity_LateForVisit")) {

                String dataSetId = getIntent().getStringExtra("dataSetId");
                String lateForVisit_SkipAndProceed = WebServices.visit_rescheduled_late_for_visit_skip_and_proceed;

                VisitsFlowCallDetailsActivity visitsFlowCallDetailsActivity = new VisitsFlowCallDetailsActivity();
                visitsFlowViewModel.postVisitsFlow_DidNotVisitTheCustomer(lateForVisit_SkipAndProceed, dataSetId, "", "", "", "", "", VisitsFlowCallDetailsActivity.send_reason, visitsFlowCallDetailsActivity.sendCallLogDetailsList_VisitsFlow());
                navigateToDashBoard();
            }

            //Visit-NPANotAvailable->Customer Not Available -> Skip & Proceed
            else if (getIntent().hasExtra("isFromVisitNPANotAvailableActivity_CustomerNotAvailable")) {

                String dataSetId = getIntent().getStringExtra("dataSetId");
                String customerNotAvailable_SkipAndProceed = WebServices.visit_rescheduled_customer_not_available_skip_and_proceed;

                VisitsFlowCallDetailsActivity visitsFlowCallDetailsActivity = new VisitsFlowCallDetailsActivity();
                visitsFlowViewModel.postVisitsFlow_DidNotVisitTheCustomer(customerNotAvailable_SkipAndProceed, dataSetId, "", "", "", "", "", "", visitsFlowCallDetailsActivity.sendCallLogDetailsList_VisitsFlow());
                navigateToDashBoard();
            }

            //PAYMENT INFO OF CUSTOMER -> OTHERS
            if (getIntent().hasExtra("isPaymentInfoOfCustomerActivity_Others")) {

                String dataSetId = getIntent().getStringExtra("dataSetId");
                String reason = getIntent().getStringExtra("reason");

                String payment_info_of_customer_others =   WebServices.call_details_payment_info_others;

                callDetailsViewModel.postScheduledDateTime_OTHERS(payment_info_of_customer_others,dataSetId, "", "", "", "", "",reason);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(SubmitCompletionActivityOfCustomer.this, MainActivity3API.class);
                        startActivity(i);
                    }
                }, 1000);

            }

            //PAYMENT INFO OF CUSTOMER-> NOT TAKEN LOAN
            if (getIntent().hasExtra("isPaymentInfoOfCustomerActivity_NotTakenLoan")) {
                String dataSetId = getIntent().getStringExtra("dataSetId");
                String payment_info_not_taken_loan = WebServices.call_details_payment_info_not_taken_loan;

                callDetailsViewModel.postScheduledDateTime_OTHERS(payment_info_not_taken_loan,dataSetId, "", "", "", "", "","");
                navigateToDashBoard();
            }

            //PAYMENT  NOTIFICATION OF CUSTOMER ->OTHERS
            if (getIntent().hasExtra("isPaymentNotificationOfCustomer_Others")) {

                String dataSetId = getIntent().getStringExtra("dataSetId");
                String reason = getIntent().getStringExtra("reason");

                String spoke_to_customer_others = WebServices.call_details_spoke_to_customer_others;

                callDetailsViewModel.postScheduledDateTime_OTHERS(spoke_to_customer_others,dataSetId, "", "", "", "", "",reason);
                navigateToDashBoard();
            }

            //ALREADY PAID
            if (getIntent().hasExtra("isAlreadyPaid")) {
                String dataSetId = getIntent().getStringExtra("dataSetId");
                callDetailsViewModel.postScheduledDateTime_AP(dataSetId, "", "", "", "", "");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(SubmitCompletionActivityOfCustomer.this, MainActivity3API.class);
                        startActivity(i);
                    }
                }, 1000);

            }

            //FO NOT VISITED
            if (getIntent().hasExtra("isFoNotVisited")) {
                String dataSetId = getIntent().getStringExtra("dataSetId");
                //  dateOfVisitPromised = getIntent().getStringExtra("dateOfVisitPromised");
                //  foName = getIntent().getStringExtra("foName");

                if (NetworkUtilities.getConnectivityStatus(this)) {
                    callDetailsViewModel.postScheduledDateTime_FNV(dataSetId, "", dateOfVisitPromised, foName, "", "");

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent i = new Intent(SubmitCompletionActivityOfCustomer.this, MainActivity3API.class);
                            startActivity(i);
                        }
                    }, 1000);
                } else {
                    Global.showSnackBar(view, getString(R.string.check_internet_connection));
                }

            }

            //LOAN TAKEN BY RELATIVE
            if (getIntent().hasExtra("isLoanTakenByRelative")) {
                String dataSetId = getIntent().getStringExtra("dataSetId");
                //  relativeName = getIntent().getStringExtra("relativeName");
                //  relativeContact = getIntent().getStringExtra("relativeContact");

                if (NetworkUtilities.getConnectivityStatus(this)) {
                    callDetailsViewModel.postScheduledDateTime_LTBR(dataSetId, "", "", "", relativeName, relativeContact, "LTBR");

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent i = new Intent(SubmitCompletionActivityOfCustomer.this, MainActivity3API.class);
                            startActivity(i);
                        }
                    }, 1000);
                } else {
                    Global.showSnackBar(view, getString(R.string.check_internet_connection));
                }
            }


            //PAYMENT INFO ->WILL PAY LUMPSUM
            if (getIntent().hasExtra("isPaymentInfoOfCustomerActivity_WillPayLumpSum")) {
                String dataSetId = getIntent().getStringExtra("dataSetId");
                if (NetworkUtilities.getConnectivityStatus(this)) {
                    callDetailsViewModel.postScheduledDateTime_WPLS(dataSetId, "", "", "", "", "");
                    navigateToDashBoard();
                } else {
                    Global.showSnackBar(view, getString(R.string.check_internet_connection));
                }
            }

            //VISITS FOR THE DAY FLOW

            //Visits Flow (Not Ready to Pay Lack Of Funds)
            if (getIntent().hasExtra("NotReadyToPay_LackOfFunds")) {
                String dataSetId = getIntent().getStringExtra("dataSetId");
                String lackOfFunds = WebServices.visit_not_ready_to_pay_lack_of_funds;
                if (NetworkUtilities.getConnectivityStatus(this)) {
                    visitsFlowViewModel.postVisitsFlowCallDateTime(lackOfFunds, dataSetId, "", "", "", "", "");
                } else {
                    Global.showSnackBar(view, getString(R.string.check_internet_connection));
                }
            }

            //Visits Flow (Not Ready to Pay Not Taken Loan)
            if (getIntent().hasExtra("NotReadyToPay_NotTakenLoan")) {
                String dataSetId = getIntent().getStringExtra("dataSetId");
                String notTakenLoan = WebServices.visit_not_ready_to_pay_not_taken_loan;
                if (NetworkUtilities.getConnectivityStatus(this)) {
                    visitsFlowViewModel.postVisitsFlowCallDateTime(notTakenLoan, dataSetId, "", "", "", "", "");
                     navigateToNearByCustomerActivity();
                } else {
                    Global.showSnackBar(view, getString(R.string.check_internet_connection));
                }
            }

            //Visits Flow (Not Ready to Pay Loan Taken By Relative)
            if (getIntent().hasExtra("NotReadyToPay_LoanTakenByRelative")) {
                String dataSetId = getIntent().getStringExtra("dataSetId");
                String relativeName = getIntent().getStringExtra("relativeName");
                String relativeContact = getIntent().getStringExtra("relativeContact");
                String loanTakenByRelative = WebServices.visit_not_ready_to_pay_loan_taken_by_relative;
                if (NetworkUtilities.getConnectivityStatus(this)) {
                    visitsFlowViewModel.postVisitsFlowCallDateTime(loanTakenByRelative, dataSetId, "", "", "", relativeName, relativeContact);
                } else {
                    Global.showSnackBar(view, getString(R.string.check_internet_connection));
                }
            }

            //Visits Flow (Not Ready to Pay Will Pay LumpSump)
            if (getIntent().hasExtra("NotReadyToPay_WillPayLumpSump")) {
                String dataSetId = getIntent().getStringExtra("dataSetId");
                String willPayLumpSump = WebServices.visit_not_ready_to_pay_will_pay_lumpsump;
                if (NetworkUtilities.getConnectivityStatus(this)) {
                    visitsFlowViewModel.postVisitsFlowCallDateTime(willPayLumpSump, dataSetId, "", "", "", "", "");
                } else {
                    Global.showSnackBar(view, getString(R.string.check_internet_connection));
                }
            }


            //Visits Flow (Not Ready to Pay Claims Payment Made)
                //<!--Claims Payment Made Renamed to Payment Already Made as text on button-->
            if (getIntent().hasExtra("NotReadyToPay_ClaimsPaymentMade")) {
                String dataSetId = getIntent().getStringExtra("dataSetId");
                String claimsPaymentMade = WebServices.visit_not_ready_to_pay_claims_payment_made;
                if (NetworkUtilities.getConnectivityStatus(this)) {
                    visitsFlowViewModel.postVisitsFlowCallDateTime(claimsPaymentMade, dataSetId, "", "", "", "", "");
                    navigateToDashBoard();
                } else {
                    Global.showSnackBar(view, getString(R.string.check_internet_connection));
                }
            }

                //VisitFlow-NotReadyToPay - Others
                if(getIntent().hasExtra("isFromVisitNPANotificationActivity_Others")){
                    String dataSetId = getIntent().getStringExtra("dataSetId");
                    String reason = getIntent().getStringExtra("reason");

                    String visited_the_customer_not_ready_to_pay_others = WebServices.visit_not_ready_to_pay_others;
                    visitsFlowViewModel.postVisitsFlowCallDateTime_Others(visited_the_customer_not_ready_to_pay_others,dataSetId,"","","","","",reason);
                    navigateToNearByCustomerActivity();
                }

                //Visit_NPA_NotAvailableActivity - NeedToCloseVisit - PaymentAlreadyMade
                if(getIntent().hasExtra("Visit_NPA_NotAvailableActivity_NeedToCloseVisit_PaymentAlreadyMade")){
                    String dataSetId = getIntent().getStringExtra("dataSetId");
                    VisitsFlowCallDetailsActivity visitsFlowCallDetailsActivity = new VisitsFlowCallDetailsActivity();
                    visitsFlowViewModel.postScheduleCall_ScheduleVisit(WebServices.needToCloseVisit,dataSetId,"","","","","",VisitsFlowCallDetailsActivity.send_reason,"","","","","",visitsFlowCallDetailsActivity.sendCallLogDetailsList_VisitsFlow());
                    navigateToDashBoard();
                }

        }// button SubmitEscalateToBM clicked once or not ends here

        });


        binding.btnSubmit.setOnClickListener(v->{
            binding.btnSubmitNoChange.performClick();
        });

        binding.btnSubmitScheduleACall.setOnClickListener(v->{

            //Visit_NPA_StatusActivity->Others
            if(getIntent().hasExtra("isFromVisitNPAStatusActivity_Others")){
                String dataSetId = getIntent().getStringExtra("dataSetId");
                Intent i = new Intent(this, ScheduleVisitForCollectionActivity.class);
                i.putExtra("dataSetId",dataSetId);
                i.putExtra("Submit_ScheduleACall","Submit_ScheduleACall"); //to Set Title
                i.putExtra("isFromVisitNPAStatusActivity_Others_ScheduleCall","isFromVisitNPAStatusActivity_Others_ScheduleCall");
                i.putExtra("dataSetIdToResetCallCount","dataSetIdToResetCallCount");
                startActivity(i);
            }

            //Visit_NPA_NotificationActivity 6 Buttons-> LOF,PAM/CPM,NTL,LTBR,WPLS,OTH
            if(getIntent().hasExtra("NotReadyToPay_LackOfFunds") || getIntent().hasExtra("NotReadyToPay_ClaimsPaymentMade")
            || getIntent().hasExtra("NotReadyToPay_NotTakenLoan") || getIntent().hasExtra("NotReadyToPay_LoanTakenByRelative")
            || getIntent().hasExtra("NotReadyToPay_WillPayLumpSump") || getIntent().hasExtra("NotReadyToPay_Others")
            )
            {
                String dataSetId = getIntent().getStringExtra("dataSetId");
                Intent i = new Intent(this, ScheduleVisitForCollectionActivity.class);
                i.putExtra("dataSetId",dataSetId);
                i.putExtra("Submit_ScheduleACall","Submit_ScheduleACall"); //to Set Title
                i.putExtra("dataSetIdToResetCallCount","dataSetIdToResetCallCount");
                i.putExtra("isFromVisitNPANotification_6ButtonsScheduleCall","isFromVisitNPANotification_6ButtonsScheduleCall"); //to check in ScheduleVisitForCollection

               if(getIntent().hasExtra("NotReadyToPay_LackOfFunds")){
                  i.putExtra("NotReadyToPay_LackOfFunds","NotReadyToPay_LackOfFunds");
               }
               else if(getIntent().hasExtra("NotReadyToPay_ClaimsPaymentMade")){
                   i.putExtra("NotReadyToPay_ClaimsPaymentMade","NotReadyToPay_ClaimsPaymentMade");
               }
               else if(getIntent().hasExtra("NotReadyToPay_NotTakenLoan")){
                   i.putExtra("NotReadyToPay_NotTakenLoan","NotReadyToPay_NotTakenLoan");
               }
               else if(getIntent().hasExtra("NotReadyToPay_LoanTakenByRelative")){
                   i.putExtra("NotReadyToPay_LoanTakenByRelative","NotReadyToPay_LoanTakenByRelative");
               }
               else if(getIntent().hasExtra("NotReadyToPay_WillPayLumpSump")){
                   i.putExtra("NotReadyToPay_WillPayLumpSump","NotReadyToPay_WillPayLumpSump");
               }
               else if(getIntent().hasExtra("NotReadyToPay_Others")){
                   i.putExtra("NotReadyToPay_Others","NotReadyToPay_Others");
               }

                startActivity(i);
            }

            //Visit_NPA_NotAvailableActivity - showDialogCloseAccount() - No Button click
            if(getIntent().hasExtra("Visit_Npa_NotAvailable_NeedToCloseVisit")){
                String dataSetId = getIntent().getStringExtra("dataSetId");
                Intent i = new Intent(this, ScheduleVisitForCollectionActivity.class);
                i.putExtra("dataSetId",dataSetId);
                i.putExtra("Submit_ScheduleACall","Submit_ScheduleACall"); //to Set Title
                i.putExtra("dataSetIdToResetCallCount","dataSetIdToResetCallCount");
                i.putExtra("Visit_Npa_NotAvailable_NeedToCloseVisit_ScheduleCall","Visit_Npa_NotAvailable_NeedToCloseVisit_ScheduleCall");
                startActivity(i);
            }

            //PaymentInfoOfCustomerActivity - 6 Buttons (WPLS,AP,FNV,NTL,LTBR,OTH)
            if( getIntent().hasExtra("isPaymentInfoOfCustomerActivity_WillPayLumpSum") || getIntent().hasExtra("isPaymentInfoOfCustomerActivity_AlreadyPaid")  || getIntent().hasExtra("isPaymentInfoOfCustomerActivity_FoNotVisited") || getIntent().hasExtra("isPaymentInfoOfCustomerActivity_NotTakenLoan")
            || getIntent().hasExtra("isPaymentInfoOfCustomerActivity_LoanTakenByRelative")  || getIntent().hasExtra("isPaymentInfoOfCustomerActivity_Others")

            ){
                String dataSetId = getIntent().getStringExtra("dataSetId");
                Intent i = new Intent(this, ScheduleVisitForCollectionActivity.class);
                i.putExtra("dataSetId",dataSetId);
                i.putExtra("Submit_ScheduleACall","Submit_ScheduleACall"); //to Set Title
                i.putExtra("dataSetIdToResetCallCount","dataSetIdToResetCallCount");
                i.putExtra("PaymentInfoOfCustomerActivity6Buttons_ScheduleCall","PaymentInfoOfCustomerActivity6Buttons_ScheduleCall");

                if(getIntent().hasExtra("isPaymentInfoOfCustomerActivity_WillPayLumpSum")){
                    i.putExtra("isPaymentInfoOfCustomerActivity_WillPayLumpSum","isPaymentInfoOfCustomerActivity_WillPayLumpSum");
                }

                if(getIntent().hasExtra("isPaymentInfoOfCustomerActivity_AlreadyPaid")){
                    i.putExtra("isPaymentInfoOfCustomerActivity_AlreadyPaid","isPaymentInfoOfCustomerActivity_AlreadyPaid");
                }

                if(getIntent().hasExtra("isPaymentInfoOfCustomerActivity_FoNotVisited")){
                  i.putExtra("isPaymentInfoOfCustomerActivity_FoNotVisited","isPaymentInfoOfCustomerActivity_FoNotVisited");
                }
                if(getIntent().hasExtra("isPaymentInfoOfCustomerActivity_NotTakenLoan")){
                    i.putExtra("isPaymentInfoOfCustomerActivity_NotTakenLoan","isPaymentInfoOfCustomerActivity_NotTakenLoan");
                }
                if(getIntent().hasExtra("isPaymentInfoOfCustomerActivity_LoanTakenByRelative")){
                    i.putExtra("isPaymentInfoOfCustomerActivity_LoanTakenByRelative","isPaymentInfoOfCustomerActivity_LoanTakenByRelative");
                }
                if(getIntent().hasExtra("isPaymentInfoOfCustomerActivity_Others")){
                    i.putExtra("isPaymentInfoOfCustomerActivity_Others","isPaymentInfoOfCustomerActivity_Others");
                }
                startActivity(i);
            }

            //PAYMENT  NOTIFICATION OF CUSTOMER ->OTHERS -> ScheduleCall
            if (getIntent().hasExtra("isPaymentNotificationOfCustomer_Others")) {
                String dataSetId = getIntent().getStringExtra("dataSetId");
                Intent i = new Intent(this,ScheduleVisitForCollectionActivity.class);
                i.putExtra("dataSetId",dataSetId);
                i.putExtra("Submit_ScheduleACall","Submit_ScheduleACall"); //to Set Title
                i.putExtra("dataSetIdToResetCallCount","dataSetIdToResetCallCount");
                i.putExtra("isPaymentNotificationOfCustomer_Others_ScheduleCall","isPaymentNotificationOfCustomer_Others_ScheduleCall");
                startActivity(i);

            }


        });

        binding.btnSubmitScheduleAVisit.setOnClickListener(v->{

            //coming from NotSpokeToCustomer-> NumberIsInvalid-> Only Submit & ScheduleVisit button will display, ScheduleCall will be hidden
            if(getIntent().hasExtra("isFromNotSpokeToCustomer_NumberInvalid")){
                String dataSetId = getIntent().getStringExtra("dataSetId");
                Intent i = new Intent(this, ScheduleVisitForCollectionActivity.class);
                i.putExtra("dataSetId",dataSetId);
                i.putExtra("isFromNotSpokeToCustomer_NumberInvalid","isFromNotSpokeToCustomer_NumberInvalid");
                startActivity(i);
            }

            //Visit_NPA_StatusActivity->Others
            if(getIntent().hasExtra("isFromVisitNPAStatusActivity_Others")){
                String dataSetId = getIntent().getStringExtra("dataSetId");
                Intent i = new Intent(this, ScheduleVisitForCollectionActivity.class);
                i.putExtra("dataSetId",dataSetId);
                i.putExtra("isFromVisitNPAStatusActivity_Others_ScheduleVisit","isFromVisitNPAStatusActivity_Others_ScheduleVisit");
                startActivity(i);
            }

            //Visit_NPA_NotificationActivity 6 Buttons-> LOF,PAM/CPM,NTL,LTBR,WPLS,OTH
            if(getIntent().hasExtra("NotReadyToPay_LackOfFunds") || getIntent().hasExtra("NotReadyToPay_ClaimsPaymentMade")
                    || getIntent().hasExtra("NotReadyToPay_NotTakenLoan") || getIntent().hasExtra("NotReadyToPay_LoanTakenByRelative")
                    || getIntent().hasExtra("NotReadyToPay_WillPayLumpSump") || getIntent().hasExtra("NotReadyToPay_Others")
            ){
                String dataSetId = getIntent().getStringExtra("dataSetId");
                Intent i = new Intent(this, ScheduleVisitForCollectionActivity.class);
                i.putExtra("dataSetId",dataSetId);
                i.putExtra("isFromVisitNPANotification_6ButtonsScheduleVisit","isFromVisitNPANotification_6ButtonsScheduleVisit"); //to check in ScheduleVisitForCollection

                if(getIntent().hasExtra("NotReadyToPay_LackOfFunds")){
                    i.putExtra("NotReadyToPay_LackOfFunds","NotReadyToPay_LackOfFunds");
                }
                else if(getIntent().hasExtra("NotReadyToPay_ClaimsPaymentMade")){
                    i.putExtra("NotReadyToPay_ClaimsPaymentMade","NotReadyToPay_ClaimsPaymentMade");
                }
                else if(getIntent().hasExtra("NotReadyToPay_NotTakenLoan")){
                    i.putExtra("NotReadyToPay_NotTakenLoan","NotReadyToPay_NotTakenLoan");
                }
                else if(getIntent().hasExtra("NotReadyToPay_LoanTakenByRelative")){
                    i.putExtra("NotReadyToPay_LoanTakenByRelative","NotReadyToPay_LoanTakenByRelative");
                }
                else if(getIntent().hasExtra("NotReadyToPay_WillPayLumpSump")){
                    i.putExtra("NotReadyToPay_WillPayLumpSump","NotReadyToPay_WillPayLumpSump");
                }
                else if(getIntent().hasExtra("NotReadyToPay_Others")){
                    i.putExtra("NotReadyToPay_Others","NotReadyToPay_Others");
                }

                startActivity(i);
            }

            //Visit_NPA_NotAvailableActivity - showDialogCloseAccount() - No Button click
            if(getIntent().hasExtra("Visit_Npa_NotAvailable_NeedToCloseVisit")){
                String dataSetId = getIntent().getStringExtra("dataSetId");
                Intent i = new Intent(this, ScheduleVisitForCollectionActivity.class);
                i.putExtra("dataSetId",dataSetId);
                i.putExtra("Visit_Npa_NotAvailable_NeedToCloseVisit_ScheduleVisit","Visit_Npa_NotAvailable_NeedToCloseVisit_ScheduleVisit");
                startActivity(i);
            }

            //PaymentInfoOfCustomerActivity - 6 Buttons (WPLS,AP,FNV,NTL,LTBR,OTH)
            if( getIntent().hasExtra("isPaymentInfoOfCustomerActivity_WillPayLumpSum")|| getIntent().hasExtra("isPaymentInfoOfCustomerActivity_AlreadyPaid") ||
                    getIntent().hasExtra("isPaymentInfoOfCustomerActivity_FoNotVisited") || getIntent().hasExtra("isPaymentInfoOfCustomerActivity_NotTakenLoan")
                    || getIntent().hasExtra("isPaymentInfoOfCustomerActivity_LoanTakenByRelative")  || getIntent().hasExtra("isPaymentInfoOfCustomerActivity_Others")

            ){
                String dataSetId = getIntent().getStringExtra("dataSetId");
                Intent i = new Intent(this, ScheduleVisitForCollectionActivity.class);
                i.putExtra("dataSetId",dataSetId);
                i.putExtra("PaymentInfoOfCustomerActivity6Buttons_ScheduleVisit","PaymentInfoOfCustomerActivity6Buttons_ScheduleVisit");

                if(getIntent().hasExtra("isPaymentInfoOfCustomerActivity_WillPayLumpSum")){
                    i.putExtra("isPaymentInfoOfCustomerActivity_WillPayLumpSum","isPaymentInfoOfCustomerActivity_WillPayLumpSum");
                }
                if(getIntent().hasExtra("isPaymentInfoOfCustomerActivity_AlreadyPaid")){
                    i.putExtra("isPaymentInfoOfCustomerActivity_AlreadyPaid","isPaymentInfoOfCustomerActivity_AlreadyPaid");
                }
                if(getIntent().hasExtra("isPaymentInfoOfCustomerActivity_FoNotVisited")){
                    i.putExtra("isPaymentInfoOfCustomerActivity_FoNotVisited","isPaymentInfoOfCustomerActivity_FoNotVisited");
                }
                if(getIntent().hasExtra("isPaymentInfoOfCustomerActivity_NotTakenLoan")){
                    i.putExtra("isPaymentInfoOfCustomerActivity_NotTakenLoan","isPaymentInfoOfCustomerActivity_NotTakenLoan");
                }
                if(getIntent().hasExtra("isPaymentInfoOfCustomerActivity_LoanTakenByRelative")){
                    i.putExtra("isPaymentInfoOfCustomerActivity_LoanTakenByRelative","isPaymentInfoOfCustomerActivity_LoanTakenByRelative");
                }
                if(getIntent().hasExtra("isPaymentInfoOfCustomerActivity_Others")){
                    i.putExtra("isPaymentInfoOfCustomerActivity_Others","isPaymentInfoOfCustomerActivity_Others");
                }
                startActivity(i);
            }

            //PAYMENT  NOTIFICATION OF CUSTOMER ->OTHERS -> ScheduleVisit
            if (getIntent().hasExtra("isPaymentNotificationOfCustomer_Others")) {
                String dataSetId = getIntent().getStringExtra("dataSetId");
                Intent i = new Intent(this,ScheduleVisitForCollectionActivity.class);
                i.putExtra("dataSetId",dataSetId);
                i.putExtra("isPaymentNotificationOfCustomer_Others_ScheduleVisit","isPaymentNotificationOfCustomer_Others_ScheduleVisit");
                startActivity(i);
            }

            });
    }

    private void navigateToDashBoard(){

        VisitsFlowCallDetailsActivity.send_reason =""; // make empty to reset flow
        Global.removeStringInSharedPref(this,"scheduleVisitForCollection_dateTime"); // make empty to reset flow

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SubmitCompletionActivityOfCustomer.this,MainActivity3API.class);
                startActivity(i);
            }
        },2000);

    }

    private void navigateToNearByCustomerActivity(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SubmitCompletionActivityOfCustomer.this,NearByCustomersActivity.class);
                startActivity(i);
            }
        },1000);

    }



    @Override
    protected void onResume() {
        initializeFields();
        onClickListener();
        setUpDetailsOfCustomerRecyclerView();
        super.onResume();
    }

    public ActivitySubmitCompletionOfCustomerBinding getBinding(){
        return binding;
    }
}