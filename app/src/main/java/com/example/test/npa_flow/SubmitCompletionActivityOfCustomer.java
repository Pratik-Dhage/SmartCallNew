package com.example.test.npa_flow;

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
import com.example.test.fragment_visits_flow.VisitsFlowCallDetailsActivity;
import com.example.test.fragment_visits_flow.VisitsFlowViewModel;
import com.example.test.helper_classes.Global;
import com.example.test.helper_classes.NetworkUtilities;
import com.example.test.main_dashboard.MainActivity3API;
import com.example.test.npa_flow.call_details.CallDetailsViewModel;
import com.example.test.npa_flow.details_of_customer.DetailsOfCustomerActivity;
import com.example.test.npa_flow.details_of_customer.DetailsOfCustomerResponseModel;
import com.example.test.npa_flow.details_of_customer.DetailsOfCustomerViewModel;
import com.example.test.npa_flow.details_of_customer.adapter.DetailsOfCustomerAdapter;
import com.example.test.roomDB.dao.CallDetailsListDao;
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
         || getIntent().hasExtra("isFromVisitNPARescheduleActivity")
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

         //initially button are Clickable
         binding.btnSubmitNoChange.setClickable(true);
         binding.btnSubmitNeedToUpdateDetails.setClickable(true);
         binding.btnSubmitEscalateToBM.setClickable(true);

        //get detailsList
        detailsList = (ArrayList<DetailsOfCustomerResponseModel>) getIntent().getSerializableExtra("detailsList");

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
        recyclerView.setAdapter(new DetailsOfCustomerAdapter(detailsList));
    }

    private void onClickListener() {

        binding.ivBack.setOnClickListener(v -> onBackPressed());

        binding.ivHome.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity3API.class));
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


        binding.btnSubmitNoChange.setOnClickListener(v-> {

            //to check if Submit Button is clicked Once or not
            if (binding.btnSubmitNoChange.isClickable())
                System.out.println("Here Submit is Clickable:true");
            {

                binding.btnSubmitNoChange.setClickable(false);
                System.out.println("Here Submit is Clickable:false");

                //NotSpokeToCustomer - NumberIsBusy
                if (getIntent().hasExtra("isFromNotSpokeToCustomer_NoResponseBusy")) {
                    String dataSetId = getIntent().getStringExtra("dataSetId");

                    CallDetailsListDao callDetailsListDao = LeadListDB.getInstance(this).callDetailsListDao();

                    List<CallDetailsListRoomModel> callDetailsListRoomModelList = callDetailsListDao.getCallLogDetailsUsingMobileNumber(DetailsOfCustomerActivity.Mobile_Number);
                    System.out.println("Here MobileNumber: " + DetailsOfCustomerActivity.Mobile_Number);

                    callDetailsViewModel.postCallDetailsNotSpokeToCustomer_NumberIsBusy_SwitchedOff(WebServices.notSpokeToCustomer_numberIsBusy, dataSetId, callDetailsListRoomModelList);
                    navigateToDashBoard();
                }

            //NotSpokeToCustomer - SwitchOff
            if (getIntent().hasExtra("isFromNotSpokeToCustomer_SwitchOff")) {
                String dataSetId = getIntent().getStringExtra("dataSetId");

                CallDetailsListDao callDetailsListDao = LeadListDB.getInstance(this).callDetailsListDao();

                List<CallDetailsListRoomModel> callDetailsListRoomModelList = callDetailsListDao.getCallLogDetailsUsingMobileNumber(DetailsOfCustomerActivity.Mobile_Number);
                System.out.println("Here MobileNumber: " + DetailsOfCustomerActivity.Mobile_Number);

                callDetailsViewModel.postCallDetailsNotSpokeToCustomer_NumberIsBusy_SwitchedOff(WebServices.notSpokeToCustomer_numberSwitchedOff, dataSetId, callDetailsListRoomModelList);
                navigateToDashBoard();
            }


            //NotSpokeToCustomerActivity - Number is Invalid
            if (getIntent().hasExtra("isFromNotSpokeToCustomer_NumberInvalid")) {
                String dataSetId = getIntent().getStringExtra("dataSetId");
                callDetailsViewModel.postCallDetailsNotSpokeToCustomer_NumberInvalid(dataSetId);
                navigateToDashBoard();
            }

            //Visit-NPA Reschedule -> Others -> Proceed
            if (getIntent().hasExtra("isFromVisitNPARescheduleActivity_Others")) {
                String dataSetId = getIntent().getStringExtra("dataSetId");
                String others_proceed = WebServices.visit_did_not_visit_others;

                VisitsFlowCallDetailsActivity visitsFlowCallDetailsActivity = new VisitsFlowCallDetailsActivity();
                visitsFlowViewModel.postVisitsFlow_DidNotVisitTheCustomer(others_proceed, dataSetId, "", "", "", "", "", VisitsFlowCallDetailsActivity.send_reason, visitsFlowCallDetailsActivity.sendCallLogDetailsList_VisitsFlow());
                navigateToDashBoard();
            }

            //Visit-NPA Reschedule -> Payment Already Made -> Skip & Proceed
            if (getIntent().hasExtra("isFromVisitNPARescheduleActivity_payment_already_made")) {
                String dataSetId = getIntent().getStringExtra("dataSetId");
                String payment_already_made_skip_and_proceed = WebServices.visit_did_not_visit_payment_already_made;

                VisitsFlowCallDetailsActivity visitsFlowCallDetailsActivity = new VisitsFlowCallDetailsActivity();
                visitsFlowViewModel.postVisitsFlow_DidNotVisitTheCustomer(payment_already_made_skip_and_proceed, dataSetId, "", "", ",", "", "", "", visitsFlowCallDetailsActivity.sendCallLogDetailsList_VisitsFlow());
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
                callDetailsViewModel.postScheduledDateTime_OTHERS(dataSetId, "", "", "", "", "",reason);

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
                Intent i = new Intent(SubmitCompletionActivityOfCustomer.this, MainActivity3API.class);
                startActivity(i);
            }

            //PAYMENT  NOTIFICATION OF CUSTOMER ->OTHERS
            if (getIntent().hasExtra("isPaymentNotificationOfCustomer_Others")) {
                Intent i = new Intent(SubmitCompletionActivityOfCustomer.this, MainActivity3API.class);
                startActivity(i);
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


            //PAYMENT INFO WILL PAY LATER->WILL PAY LUMPSUM
            if (getIntent().hasExtra("paymentInfo_WillPayLater")) {
                String dataSetId = getIntent().getStringExtra("dataSetId");
                if (NetworkUtilities.getConnectivityStatus(this)) {
                    callDetailsViewModel.postScheduledDateTime_WPLS(dataSetId, "", "", "", "", "");

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent i = new Intent(SubmitCompletionActivityOfCustomer.this, MainActivity3API.class);
                            startActivity(i);
                        }
                    }, 3000);
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


        }// button SubmitNoChange is Clicked Once or not ends here

        });

        binding.btnSubmitNeedToUpdateDetails.setOnClickListener(v-> {

            if (binding.btnSubmitNeedToUpdateDetails.isClickable())

            {
                System.out.println("Here Submit Button is Clickable:true");

                binding.btnSubmitNeedToUpdateDetails.setClickable(false);
                System.out.println("Here Submit Button is Clickable:false");

                //NotSpokeToCustomer - NumberIsBusy
                if (getIntent().hasExtra("isFromNotSpokeToCustomer_NoResponseBusy")) {
                    String dataSetId = getIntent().getStringExtra("dataSetId");

                    CallDetailsListDao callDetailsListDao = LeadListDB.getInstance(this).callDetailsListDao();

                    List<CallDetailsListRoomModel> callDetailsListRoomModelList = callDetailsListDao.getCallLogDetailsUsingMobileNumber(DetailsOfCustomerActivity.Mobile_Number);
                    System.out.println("Here MobileNumber: " + DetailsOfCustomerActivity.Mobile_Number);

                    callDetailsViewModel.postCallDetailsNotSpokeToCustomer_NumberIsBusy_SwitchedOff(WebServices.notSpokeToCustomer_numberIsBusy, dataSetId, callDetailsListRoomModelList);
                    navigateToDashBoard();
                }

            //NotSpokeToCustomer - SwitchOff
            if (getIntent().hasExtra("isFromNotSpokeToCustomer_SwitchOff")) {
                String dataSetId = getIntent().getStringExtra("dataSetId");

                CallDetailsListDao callDetailsListDao = LeadListDB.getInstance(this).callDetailsListDao();

                List<CallDetailsListRoomModel> callDetailsListRoomModelList = callDetailsListDao.getCallLogDetailsUsingMobileNumber(DetailsOfCustomerActivity.Mobile_Number);
                System.out.println("Here MobileNumber: " + DetailsOfCustomerActivity.Mobile_Number);

                callDetailsViewModel.postCallDetailsNotSpokeToCustomer_NumberIsBusy_SwitchedOff(WebServices.notSpokeToCustomer_numberSwitchedOff, dataSetId, callDetailsListRoomModelList);
                navigateToDashBoard();
            }

            //NotSpokeToCustomerActivity - Number is Invalid
            if (getIntent().hasExtra("isFromNotSpokeToCustomer_NumberInvalid")) {
                String dataSetId = getIntent().getStringExtra("dataSetId");
                callDetailsViewModel.postCallDetailsNotSpokeToCustomer_NumberInvalid(dataSetId);
                navigateToDashBoard();
            }

            //Visit-NPA Reschedule -> Others -> Proceed
            if (getIntent().hasExtra("isFromVisitNPARescheduleActivity_Others")) {
                String dataSetId = getIntent().getStringExtra("dataSetId");
                String others_proceed = WebServices.visit_did_not_visit_others;

                VisitsFlowCallDetailsActivity visitsFlowCallDetailsActivity = new VisitsFlowCallDetailsActivity();
                visitsFlowViewModel.postVisitsFlow_DidNotVisitTheCustomer(others_proceed, dataSetId, "", "", "", "", "", VisitsFlowCallDetailsActivity.send_reason, visitsFlowCallDetailsActivity.sendCallLogDetailsList_VisitsFlow());
                navigateToDashBoard();
            }

            //Visit-NPA Reschedule -> Payment Already Made -> Skip & Proceed
            if (getIntent().hasExtra("isFromVisitNPARescheduleActivity_payment_already_made")) {
                String dataSetId = getIntent().getStringExtra("dataSetId");
                String payment_already_made_skip_and_proceed = WebServices.visit_did_not_visit_payment_already_made;

                VisitsFlowCallDetailsActivity visitsFlowCallDetailsActivity = new VisitsFlowCallDetailsActivity();
                visitsFlowViewModel.postVisitsFlow_DidNotVisitTheCustomer(payment_already_made_skip_and_proceed, dataSetId, "", "", ",", "", "", "", visitsFlowCallDetailsActivity.sendCallLogDetailsList_VisitsFlow());
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
                callDetailsViewModel.postScheduledDateTime_OTHERS(dataSetId, "", "", "", "", "",reason);

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
                Intent i = new Intent(SubmitCompletionActivityOfCustomer.this, MainActivity3API.class);
                startActivity(i);
            }

            //PAYMENT  NOTIFICATION OF CUSTOMER ->OTHERS
            if (getIntent().hasExtra("isPaymentNotificationOfCustomer_Others")) {
                Intent i = new Intent(SubmitCompletionActivityOfCustomer.this, MainActivity3API.class);
                startActivity(i);
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


            //PAYMENT INFO WILL PAY LATER->WILL PAY LUMPSUM
            if (getIntent().hasExtra("paymentInfo_WillPayLater")) {
                String dataSetId = getIntent().getStringExtra("dataSetId");
                if (NetworkUtilities.getConnectivityStatus(this)) {
                    callDetailsViewModel.postScheduledDateTime_WPLS(dataSetId, "", "", "", "", "");

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent i = new Intent(SubmitCompletionActivityOfCustomer.this, MainActivity3API.class);
                            startActivity(i);
                        }
                    }, 3000);
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


        }//button SubmitNeedToUpdateDetails is clicked once or not ends here

        });

        binding.btnSubmitEscalateToBM.setOnClickListener(v-> {

            if (binding.btnSubmitEscalateToBM.isClickable())

            {
                System.out.println("Here Submit is Clickable:true");
                binding.btnSubmitEscalateToBM.setClickable(false);
                System.out.println("Here Submit is Clickable:false");

                //NotSpokeToCustomer - NumberIsBusy
                if (getIntent().hasExtra("isFromNotSpokeToCustomer_NoResponseBusy")) {
                    String dataSetId = getIntent().getStringExtra("dataSetId");

                    CallDetailsListDao callDetailsListDao = LeadListDB.getInstance(this).callDetailsListDao();

                    List<CallDetailsListRoomModel> callDetailsListRoomModelList = callDetailsListDao.getCallLogDetailsUsingMobileNumber(DetailsOfCustomerActivity.Mobile_Number);
                    System.out.println("Here MobileNumber: " + DetailsOfCustomerActivity.Mobile_Number);

                    callDetailsViewModel.postCallDetailsNotSpokeToCustomer_NumberIsBusy_SwitchedOff(WebServices.notSpokeToCustomer_numberIsBusy, dataSetId, callDetailsListRoomModelList);
                    navigateToDashBoard();
                }

            //NotSpokeToCustomer - SwitchOff
            if (getIntent().hasExtra("isFromNotSpokeToCustomer_SwitchOff")) {
                String dataSetId = getIntent().getStringExtra("dataSetId");

                CallDetailsListDao callDetailsListDao = LeadListDB.getInstance(this).callDetailsListDao();

                List<CallDetailsListRoomModel> callDetailsListRoomModelList = callDetailsListDao.getCallLogDetailsUsingMobileNumber(DetailsOfCustomerActivity.Mobile_Number);
                System.out.println("Here MobileNumber: " + DetailsOfCustomerActivity.Mobile_Number);

                callDetailsViewModel.postCallDetailsNotSpokeToCustomer_NumberIsBusy_SwitchedOff(WebServices.notSpokeToCustomer_numberSwitchedOff, dataSetId, callDetailsListRoomModelList);
                navigateToDashBoard();
            }

            //NotSpokeToCustomerActivity - Number is Invalid
            if (getIntent().hasExtra("isFromNotSpokeToCustomer_NumberInvalid")) {
                String dataSetId = getIntent().getStringExtra("dataSetId");
                callDetailsViewModel.postCallDetailsNotSpokeToCustomer_NumberInvalid(dataSetId);
                navigateToDashBoard();
            }

            //Visit-NPA Reschedule -> Others -> Proceed
            if (getIntent().hasExtra("isFromVisitNPARescheduleActivity_Others")) {
                String dataSetId = getIntent().getStringExtra("dataSetId");
                String others_proceed = WebServices.visit_did_not_visit_others;

                VisitsFlowCallDetailsActivity visitsFlowCallDetailsActivity = new VisitsFlowCallDetailsActivity();
                visitsFlowViewModel.postVisitsFlow_DidNotVisitTheCustomer(others_proceed, dataSetId, "", "", "", "", "", VisitsFlowCallDetailsActivity.send_reason, visitsFlowCallDetailsActivity.sendCallLogDetailsList_VisitsFlow());
                navigateToDashBoard();
            }

            //Visit-NPA Reschedule -> Payment Already Made -> Skip & Proceed
            if (getIntent().hasExtra("isFromVisitNPARescheduleActivity_payment_already_made")) {
                String dataSetId = getIntent().getStringExtra("dataSetId");
                String payment_already_made_skip_and_proceed = WebServices.visit_did_not_visit_payment_already_made;

                VisitsFlowCallDetailsActivity visitsFlowCallDetailsActivity = new VisitsFlowCallDetailsActivity();
                visitsFlowViewModel.postVisitsFlow_DidNotVisitTheCustomer(payment_already_made_skip_and_proceed, dataSetId, "", "", ",", "", "", "", visitsFlowCallDetailsActivity.sendCallLogDetailsList_VisitsFlow());
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
                callDetailsViewModel.postScheduledDateTime_OTHERS(dataSetId, "", "", "", "", "",reason);

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
                Intent i = new Intent(SubmitCompletionActivityOfCustomer.this, MainActivity3API.class);
                startActivity(i);
            }

            //PAYMENT  NOTIFICATION OF CUSTOMER ->OTHERS
            if (getIntent().hasExtra("isPaymentNotificationOfCustomer_Others")) {
                Intent i = new Intent(SubmitCompletionActivityOfCustomer.this, MainActivity3API.class);
                startActivity(i);
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


            //PAYMENT INFO WILL PAY LATER->WILL PAY LUMPSUM
            if (getIntent().hasExtra("paymentInfo_WillPayLater")) {
                String dataSetId = getIntent().getStringExtra("dataSetId");
                if (NetworkUtilities.getConnectivityStatus(this)) {
                    callDetailsViewModel.postScheduledDateTime_WPLS(dataSetId, "", "", "", "", "");

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent i = new Intent(SubmitCompletionActivityOfCustomer.this, MainActivity3API.class);
                            startActivity(i);
                        }
                    }, 3000);
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

        }// button SubmitEscalateToBM clicked once or not ends here

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
        },1000);

    }


    @Override
    protected void onResume() {
        initializeFields();
        onClickListener();
        setUpDetailsOfCustomerRecyclerView();
        super.onResume();
    }
}