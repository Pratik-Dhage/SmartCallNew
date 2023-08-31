package com.example.test.npa_flow;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.example.test.R;
import com.example.test.api_manager.WebServices;
import com.example.test.databinding.ActivityScheduleVisitForCollectionBinding;
import com.example.test.fragment_visits_flow.VisitsFlowCallDetailsActivity;
import com.example.test.fragment_visits_flow.VisitsFlowViewModel;
import com.example.test.fragments_activity.ActivityOfFragments;
import com.example.test.helper_classes.Global;
import com.example.test.helper_classes.NetworkUtilities;
import com.example.test.main_dashboard.MainActivity3API;
import com.example.test.npa_flow.call_details.CallDetailsViewModel;
import com.example.test.npa_flow.details_of_customer.DetailsOfCustomerActivity;
import com.example.test.npa_flow.details_of_customer.DetailsOfCustomerResponseModel;
import com.example.test.npa_flow.details_of_customer.DetailsOfCustomerViewModel;
import com.example.test.npa_flow.loan_collection.LoanCollectionActivity;
import com.example.test.roomDB.dao.CallDetailsListDao;
import com.example.test.roomDB.dao.LeadCallDao;
import com.example.test.roomDB.dao.MPinDao;
import com.example.test.roomDB.dao.UserNameDao;
import com.example.test.roomDB.database.LeadListDB;
import com.example.test.roomDB.model.CallDetailsListRoomModel;
import com.example.test.roomDB.model.LeadCallModelRoom;
import com.example.test.schedule_flow.visits_for_the_day.adapter.VisitsForTheDayAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ScheduleVisitForCollectionActivity extends AppCompatActivity {

    ActivityScheduleVisitForCollectionBinding binding;
    View view;
    ArrayList<DetailsOfCustomerResponseModel> detailsList;
    CallDetailsViewModel callDetailsViewModel;
    VisitsFlowViewModel visitsFlowViewModel;
    public static String scheduleVisitForCollection_dateTime  ;
    DetailsOfCustomerActivity detailsOfCustomerActivity = new DetailsOfCustomerActivity();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  setContentView(R.layout.activity_schedule_visit_for_collection);


        initializeFields();
        onClickListener();
        initObserver();
    }

    private void initializeFields() {

        binding = DataBindingUtil.setContentView(this, R.layout.activity_schedule_visit_for_collection);
        view = binding.getRoot();

        callDetailsViewModel = new ViewModelProvider(this).get(CallDetailsViewModel.class);
        visitsFlowViewModel = new ViewModelProvider(this).get(VisitsFlowViewModel.class);

        // button Update/Update Schedule will be clickable initially
        binding.btnUpdateSchedule.setClickable(true);
        System.out.println("Here Update/Update Schedule Clickable:true");

        //for current date
        DatePicker datePicker = binding.datePickerCalendarView;
        Calendar calendar = Calendar.getInstance();
        long today = calendar.getTimeInMillis(); //for min date
        datePicker.setMinDate(today);
        datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), null);

        //get detailsList
        detailsList = (ArrayList<DetailsOfCustomerResponseModel>) getIntent().getSerializableExtra("detailsList");

        setUpTitleAndButtonText();


        // Get UserName , UserID , BranchCode

        MPinDao mPinDao = LeadListDB.getInstance(this).mPinDao();
        UserNameDao userNameDao = LeadListDB.getInstance(this).userNameDao();
        String userName = userNameDao.getUserNameUsingUserIDInUserNameRoomDB(mPinDao.getUserID());
        // Store UserName in SharedPreference and Use in StatusOfCustomerDetailsAdapter
        Global.saveStringInSharedPref(this,"userName",userName);

        MainActivity3API.UserID = mPinDao.getUserID();
        MainActivity3API.BranchCode = mPinDao.getBranchCode();

        System.out.println("Here ScheduleVisitForCustomerActivity initializeFields() UserID:"+MainActivity3API.UserID);
        System.out.println("Here ScheduleVisitForCustomerActivity initializeFields() BranchCode:"+MainActivity3API.BranchCode);

          CallDetailsViewModel.userId =  mPinDao.getUserID(); // for Calling Agent to not go null
        System.out.println("Here ScheduleVisitForCustomerActivity CallingAgent UserID:"+CallDetailsViewModel.userId);

    }

    private void initObserver(){

        if(NetworkUtilities.getConnectivityStatus(this)) {
            callDetailsViewModel.getMutCallDetailsResponseApi().observe(this, result -> {

                if(result!=null){
                  //  Global.showToast(this,"Server Response:"+result);
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

    private void setUpTitleAndButtonText() {

        if (getIntent().hasExtra("isFromPaymentNotificationOfCustomerActivity")
                || getIntent().hasExtra("isFromPaymentModeStatusActivity")
                || getIntent().hasExtra("isCall") || getIntent().hasExtra("isFromPaymentInfoOfCustomerActivity")
                || getIntent().hasExtra("Complete_Schedule_A_Call") || getIntent().hasExtra("isFromVisit_NPAStatus_NeedToCallLater")
                || getIntent().hasExtra("Submit_ScheduleACall") || getIntent().hasExtra("Visit_NPA_Reschedule_ScheduleCall")
        ) {
            binding.labelScheduleVisit.setText(getString(R.string.schedule_call));
            binding.btnUpdateSchedule.setText(getString(R.string.update));
        }

    }

    private void initObserverScheduleCallScheduleVisit(){

        if(NetworkUtilities.getConnectivityStatus(this)){

            visitsFlowViewModel.getMutVisitsCallDetailsResponseApi().observe(this,result->{

                if(result!=null){
                    System.out.println("ScheduleCall_ScheduleVisit Response:"+result);
                    Global.showSnackBar(view,result);

                }

            });

            //handle error response
            visitsFlowViewModel.getMutErrorResponse().observe(this, error -> {

                if (error != null && !error.isEmpty()) {

                    System.out.println("Here ScheduleCall_ScheduleVisit  Exception: " + error);

                } else {
                    Global.showSnackBar(view, getResources().getString(R.string.check_internet_connection));
                }
            });

        }
        else {
            Global.showSnackBar(view, getResources().getString(R.string.check_internet_connection));
        }

    }


    private void onClickListener() {

        binding.ivBack.setOnClickListener(v->{
            onBackPressed();
        });

        binding.btnUpdateSchedule.setOnClickListener(v -> {

        //To Check if Button Update / Update Schedule is Clicked Once or not
            if(binding.btnUpdateSchedule.isClickable())
                System.out.println("Here Button Update/Update Schedule Clickable:true");
            {

                //if Button Update / Update Schedule is Clicked Once , it will be UnClickable for 2nd time
                binding.btnUpdateSchedule.setClickable(false);
                System.out.println("Here Button Update/Update Schedule Clickable:false");

                //  Global.showToast(ScheduleVisitForCollectionActivity.this, getResources().getString(R.string.schedule_update_successfully));

                //on Clicking update call this api   callDetailsViewModel.postCallDetails with ScheduleDateTime
                //in pattern   String pattern = "yyyy-MM-dd HH:mm:ss"; // Pattern to match the date format



                // UPDATE BUTTON Text (FOR ASKED TO CALL LATER AND WILL PAY LATER FLOW)
                if (binding.btnUpdateSchedule.getText() == getString(R.string.update)) {

                    if (getIntent().hasExtra("isWillPayLater")) {
                        //get scheduleDateTime
                        getScheduleDateTime();
                        String dataSetId = getIntent().getStringExtra("dataSetId");
                        String will_pay_later = "will pay later";
                        DetailsOfCustomerActivity detailsOfCustomerActivity = new DetailsOfCustomerActivity();
                        //using payment type as will pay later
                        callDetailsViewModel.postScheduledDateTime(dataSetId, will_pay_later, scheduleVisitForCollection_dateTime, detailsOfCustomerActivity.sendCallLogDetailsList_WillPayLater());
                    }


                    //ASKED TO CALL BACK LATER(Payment Notification of Customer)
                    if (getIntent().hasExtra("isAskedToCallLater")) {
                        //get scheduleDateTime
                        getScheduleDateTime();
                        String dataSetId = getIntent().getStringExtra("dataSetId");
                        DetailsOfCustomerActivity detailsOfCustomerActivity = new DetailsOfCustomerActivity();
                        callDetailsViewModel.postScheduledDateTime_ATCL(dataSetId, scheduleVisitForCollection_dateTime, detailsOfCustomerActivity.sendCallLogDetailsList_WillPayLater());

                        //Reset Call Count of dataSetId that will Appear in CallsForTheDay List
                        if (getIntent().hasExtra("dataSetIdToResetCallCount") ) {
                            System.out.println("Here ScheduleVisitForCollection AskedToCallBackLater dataSetId"+ dataSetId);
                            LeadCallDao leadCallDao = LeadListDB.getInstance(this).leadCallDao();
                            leadCallDao.UpdateLeadCallsUsingDataSetId(0, dataSetId);
                        }
                    }

                }

                //Visit_NPA_StatusActivity -> Asked To Visit Later - Update Schedule
                if (binding.btnUpdateSchedule.getText() == getString(R.string.update_schedule_space) && getIntent().hasExtra("isFromVisit_NPAStatus_AskedToVisitLater")) {
                    String dataSetId = getIntent().getStringExtra("dataSetId");
                    String visitedTheCustomer_AskedToVisitLater = WebServices.visit_asked_to_visit_later;
                    //get scheduleDateTime
                    getScheduleDateTime();
                    VisitsFlowCallDetailsActivity visitsFlowCallDetailsActivity = new VisitsFlowCallDetailsActivity();
                    visitsFlowViewModel.postVisitsFlow_DidNotVisitTheCustomer(visitedTheCustomer_AskedToVisitLater, dataSetId, scheduleVisitForCollection_dateTime, "", "", "", "", "", visitsFlowCallDetailsActivity.sendCallLogDetailsList_VisitsFlow());
                }


                //Visit_NPA_NotAvailableActivity -> Customer Not Available
                if ( getIntent().hasExtra("isFromVisitNPANotAvailableActivity_CustomerNotAvailable")) {
                    String dataSetId = getIntent().getStringExtra("dataSetId");
                    //get scheduleDateTime
                    getScheduleDateTime();
                    VisitsFlowCallDetailsActivity visitsFlowCallDetailsActivity = new VisitsFlowCallDetailsActivity();
                    if(getIntent().hasExtra("Visit_NPA_Reschedule_ScheduleVisit")){
                        visitsFlowViewModel.postVisitsFlow_DidNotVisitTheCustomer(WebServices.did_not_visit_customer_CustomerNotAvailable_ScheduleVisit, dataSetId, scheduleVisitForCollection_dateTime, "", "", "", "", "", visitsFlowCallDetailsActivity.sendCallLogDetailsList_VisitsFlow());
                    }
                   else if(getIntent().hasExtra("Visit_NPA_Reschedule_ScheduleCall")){
                        visitsFlowViewModel.postVisitsFlow_DidNotVisitTheCustomer(WebServices.did_not_visit_customer_CustomerNotAvailable_ScheduleCall, dataSetId, scheduleVisitForCollection_dateTime, "", "", "", "", "", visitsFlowCallDetailsActivity.sendCallLogDetailsList_VisitsFlow());
                    }
                }

                //Visit_NPA_NotAvailable -> Late For Visit
                if ( getIntent().hasExtra("isFromVisitNPANotAvailableActivity_LateForVisit")) {
                    String dataSetId = getIntent().getStringExtra("dataSetId");
                    //get scheduleDateTime
                    getScheduleDateTime();
                    VisitsFlowCallDetailsActivity visitsFlowCallDetailsActivity = new VisitsFlowCallDetailsActivity();
                    if(getIntent().hasExtra("Visit_NPA_Reschedule_ScheduleVisit")){
                        visitsFlowViewModel.postVisitsFlow_DidNotVisitTheCustomer(WebServices.did_not_visit_customer_LateForVisit_ScheduleVisit, dataSetId, scheduleVisitForCollection_dateTime, "", "", "", "", VisitsFlowCallDetailsActivity.send_reason, visitsFlowCallDetailsActivity.sendCallLogDetailsList_VisitsFlow());
                    }
                    else if(getIntent().hasExtra("Visit_NPA_Reschedule_ScheduleCall")){
                        visitsFlowViewModel.postVisitsFlow_DidNotVisitTheCustomer(WebServices.did_not_visit_customer_LateForVisit_ScheduleCall, dataSetId, scheduleVisitForCollection_dateTime, "", "", "", "", VisitsFlowCallDetailsActivity.send_reason, visitsFlowCallDetailsActivity.sendCallLogDetailsList_VisitsFlow());
                    }
                }

                //Visit_NPA_NotAvailable -> Others
                if ( getIntent().hasExtra("isFromVisitNPANotAvailableActivity_Others")) {
                    String dataSetId = getIntent().getStringExtra("dataSetId");
                    //get scheduleDateTime
                    getScheduleDateTime();
                    VisitsFlowCallDetailsActivity visitsFlowCallDetailsActivity = new VisitsFlowCallDetailsActivity();
                    if(getIntent().hasExtra("Visit_NPA_Reschedule_ScheduleVisit")){
                        visitsFlowViewModel.postVisitsFlow_DidNotVisitTheCustomer(WebServices.did_not_visit_customer_Others_ScheduleVisit, dataSetId, scheduleVisitForCollection_dateTime, "", "", "", "", VisitsFlowCallDetailsActivity.send_reason, visitsFlowCallDetailsActivity.sendCallLogDetailsList_VisitsFlow());
                    }
                    else if(getIntent().hasExtra("Visit_NPA_Reschedule_ScheduleCall")){
                        visitsFlowViewModel.postVisitsFlow_DidNotVisitTheCustomer(WebServices.did_not_visit_customer_Others_ScheduleCall, dataSetId, scheduleVisitForCollection_dateTime, "", "", "", "", VisitsFlowCallDetailsActivity.send_reason, visitsFlowCallDetailsActivity.sendCallLogDetailsList_VisitsFlow());
                    }

                }

                //Payment Mode -> Schedule Visit For Collection (SVFC)
                if (binding.btnUpdateSchedule.getText() == getString(R.string.update_schedule_space) && getIntent().hasExtra("isFromPaymentMode_ScheduleVisitForCollection")) {

                    // in NearByCustomerActivity show VisitNearbyCustomer button when Schedule Visit For Collection is clicked in PaymentModeActivity
                    VisitsForTheDayAdapter.showNearByCustomerButton = false; //kept false for temporary basis

                    //get scheduleDateTime
                    getScheduleDateTime();
                    String dataSetId = getIntent().getStringExtra("dataSetId");
                    DetailsOfCustomerActivity detailsOfCustomerActivity = new DetailsOfCustomerActivity();
                    callDetailsViewModel.postScheduledDateTime_SVFC(dataSetId, scheduleVisitForCollection_dateTime, detailsOfCustomerActivity.sendCallLogDetailsList_WillPayLater());

                }

                //NotSpokeToCustomer-> Physical Visit Required
                if( binding.btnUpdateSchedule.getText() == getString(R.string.update_schedule_space) && getIntent().hasExtra("isFromNotSpokeToCustomer_PhysicalVisitRequired")){

                    getScheduleDateTime();
                    String dataSetId = getIntent().getStringExtra("dataSetId");
                    String reason = getIntent().getStringExtra("reason");
                    DetailsOfCustomerActivity detailsOfCustomerActivity = new DetailsOfCustomerActivity();
                    callDetailsViewModel.postScheduledDateTime_PVR(dataSetId, scheduleVisitForCollection_dateTime, reason,detailsOfCustomerActivity.sendCallLogDetailsList_WillPayLater());

                }

                //NotSpokeToCustomer - No Response/Busy
                if(binding.btnUpdateSchedule.getText() == getString(R.string.update_schedule_space) && getIntent().hasExtra("isFromNotSpokeToCustomer_NoResponseBusy")){

                    String dataSetId = getIntent().getStringExtra("dataSetId");
                    sendFinalAttemptAndUpdateCallCount(dataSetId); // after 5th Attempt make call count to zero to remove hand gesture
                    getScheduleDateTime();
                    System.out.println("Here NoResponseBusy dataSetId:"+dataSetId);
                    String physicalVisitRequired = WebServices.notSpokeToCustomer_physicalVisitRequired; //using DNSTC-PVR flow for Member to appear in VisitsFlow at 5th Attempt
                    DetailsOfCustomerActivity detailsOfCustomerActivity = new DetailsOfCustomerActivity();
                    callDetailsViewModel.postCallDetailsNotSpokeToCustomer_NumberIsBusy_SwitchedOff_WithScheduleDateTime(physicalVisitRequired,dataSetId, scheduleVisitForCollection_dateTime, detailsOfCustomerActivity.sendCallLogDetailsList_WillPayLater());

                }

                //NotSpokeToCustomer - Not Reachable/Switched Off
                if(binding.btnUpdateSchedule.getText() == getString(R.string.update_schedule_space) && getIntent().hasExtra("isFromNotSpokeToCustomer_SwitchOff")){

                    String dataSetId = getIntent().getStringExtra("dataSetId");
                    sendFinalAttemptAndUpdateCallCount(dataSetId); // after 5th Attempt make call count to zero to remove hand gesture
                    getScheduleDateTime();
                    System.out.println("Here NotReachableSwitchOff dataSetId:"+dataSetId);
                    String physicalVisitRequired = WebServices.notSpokeToCustomer_physicalVisitRequired;//using DNSTC-PVR flow for Member to appear in VisitsFlow at 5th Attempt
                    DetailsOfCustomerActivity detailsOfCustomerActivity = new DetailsOfCustomerActivity();
                    callDetailsViewModel.postCallDetailsNotSpokeToCustomer_NumberIsBusy_SwitchedOff_WithScheduleDateTime(physicalVisitRequired,dataSetId, scheduleVisitForCollection_dateTime, detailsOfCustomerActivity.sendCallLogDetailsList_WillPayLater());

                }

                //VisitCompletionActivity -> Schedule A Call
                if(getIntent().hasExtra("Complete_Schedule_A_Call")){
                    String dataSetId = getIntent().getStringExtra("dataSetId");
                    String amountCollected = Global.getStringFromSharedPref(this, "Amount_Paid");
                    VisitsFlowCallDetailsActivity visitsFlowCallDetailsActivity = new VisitsFlowCallDetailsActivity();
                    getScheduleDateTime();
                    String scheduleACall = "-SC";
                    String completeScheduleACallCashPayment = WebServices.completeScheduleACallCashPayment;
                    String completeScheduleACallChequePayment = WebServices.completeScheduleACallChequePayment;


                    // Cash Flow
                    if(getIntent().hasExtra("from_payment_status_full_amt_paid") && getIntent().hasExtra("isVisitsReadyToPayCashPayment")){
                       String fullAmtPaid = "-FAP";
                       visitsFlowViewModel.postScheduleCall_ScheduleVisit(completeScheduleACallCashPayment+fullAmtPaid+scheduleACall,dataSetId,scheduleVisitForCollection_dateTime,"","","","","",amountCollected,"","","","",visitsFlowCallDetailsActivity.sendCallLogDetailsList_VisitsFlow());
                    }
                    else if(getIntent().hasExtra("from_payment_status_partial_amt_paid") && getIntent().hasExtra("isVisitsReadyToPayCashPayment")){
                        String partialAmtPaid = "-PAP";
                        visitsFlowViewModel.postScheduleCall_ScheduleVisit(completeScheduleACallCashPayment+partialAmtPaid+scheduleACall,dataSetId,scheduleVisitForCollection_dateTime,"","","","","",amountCollected,"","","","",visitsFlowCallDetailsActivity.sendCallLogDetailsList_VisitsFlow());
                    }

                    //Cheque Flow
                   else if(getIntent().hasExtra("from_payment_status_full_amt_paid") && getIntent().hasExtra("isVisitsReadyToPayChequePayment")){
                       String fullAmtPaid = "-FAP";
                        String chequeDate = getIntent().getStringExtra("ChequeDate");
                        String chequeNumber = getIntent().getStringExtra("ChequeNumber");
                        String chequeAmount = getIntent().getStringExtra("ChequeAmount");
                        String bankName = getIntent().getStringExtra("BankName");
                        visitsFlowViewModel.postScheduleCall_ScheduleVisit(completeScheduleACallChequePayment+fullAmtPaid+scheduleACall,dataSetId,scheduleVisitForCollection_dateTime,"","","","","",amountCollected,chequeDate,chequeNumber,chequeAmount,bankName,visitsFlowCallDetailsActivity.sendCallLogDetailsList_VisitsFlow());
                    }
                    else if(getIntent().hasExtra("from_payment_status_partial_amt_paid") && getIntent().hasExtra("isVisitsReadyToPayChequePayment")){
                        String partialAmtPaid = "-PAP";
                        String chequeDate = getIntent().getStringExtra("ChequeDate");
                        String chequeNumber = getIntent().getStringExtra("ChequeNumber");
                        String chequeAmount = getIntent().getStringExtra("ChequeAmount");
                        String bankName = getIntent().getStringExtra("BankName");
                        visitsFlowViewModel.postScheduleCall_ScheduleVisit(completeScheduleACallChequePayment+partialAmtPaid+scheduleACall,dataSetId,scheduleVisitForCollection_dateTime,"","","","","",amountCollected,chequeDate,chequeNumber,chequeAmount,bankName,visitsFlowCallDetailsActivity.sendCallLogDetailsList_VisitsFlow());
                    }

                }

                //VisitCompletionActivity -> Schedule A Visit
                if(getIntent().hasExtra("Complete_Schedule_A_Visit")){
                    String dataSetId = getIntent().getStringExtra("dataSetId");
                    String amountCollected = Global.getStringFromSharedPref(this, "Amount_Paid");
                    VisitsFlowCallDetailsActivity visitsFlowCallDetailsActivity = new VisitsFlowCallDetailsActivity();
                    getScheduleDateTime();
                    String scheduleAVisit = "-SV";
                    String completeScheduleACallCashPayment = WebServices.completeScheduleACallCashPayment;
                    String completeScheduleACallChequePayment = WebServices.completeScheduleACallChequePayment;

                    // Cash Flow
                    if(getIntent().hasExtra("from_payment_status_full_amt_paid") && getIntent().hasExtra("isVisitsReadyToPayCashPayment")){
                        String fullAmtPaid = "-FAP";
                        visitsFlowViewModel.postScheduleCall_ScheduleVisit(completeScheduleACallCashPayment+fullAmtPaid+scheduleAVisit,dataSetId,scheduleVisitForCollection_dateTime,"","","","","",amountCollected,"","","","",visitsFlowCallDetailsActivity.sendCallLogDetailsList_VisitsFlow());
                    }
                    else if(getIntent().hasExtra("from_payment_status_partial_amt_paid") && getIntent().hasExtra("isVisitsReadyToPayCashPayment")){
                        String partialAmtPaid = "-PAP";
                        visitsFlowViewModel.postScheduleCall_ScheduleVisit(completeScheduleACallCashPayment+partialAmtPaid+scheduleAVisit,dataSetId,scheduleVisitForCollection_dateTime,"","","","","",amountCollected,"","","","",visitsFlowCallDetailsActivity.sendCallLogDetailsList_VisitsFlow());
                    }

                    //Cheque Flow
                    else if(getIntent().hasExtra("from_payment_status_full_amt_paid") && getIntent().hasExtra("isVisitsReadyToPayChequePayment")){
                        String fullAmtPaid = "-FAP";
                        String chequeDate = getIntent().getStringExtra("ChequeDate");
                        String chequeNumber = getIntent().getStringExtra("ChequeNumber");
                        String chequeAmount = getIntent().getStringExtra("ChequeAmount");
                        String bankName = getIntent().getStringExtra("BankName");
                        visitsFlowViewModel.postScheduleCall_ScheduleVisit(completeScheduleACallChequePayment+fullAmtPaid+scheduleAVisit,dataSetId,scheduleVisitForCollection_dateTime,"","","","","",amountCollected,chequeDate,chequeNumber,chequeAmount,bankName,visitsFlowCallDetailsActivity.sendCallLogDetailsList_VisitsFlow());
                    }
                    else if(getIntent().hasExtra("from_payment_status_partial_amt_paid") && getIntent().hasExtra("isVisitsReadyToPayChequePayment")){
                        String partialAmtPaid = "-PAP";
                        String chequeDate = getIntent().getStringExtra("ChequeDate");
                        String chequeNumber = getIntent().getStringExtra("ChequeNumber");
                        String chequeAmount = getIntent().getStringExtra("ChequeAmount");
                        String bankName = getIntent().getStringExtra("BankName");
                        visitsFlowViewModel.postScheduleCall_ScheduleVisit(completeScheduleACallChequePayment+partialAmtPaid+scheduleAVisit,dataSetId,scheduleVisitForCollection_dateTime,"","","","","",amountCollected,chequeDate,chequeNumber,chequeAmount,bankName,visitsFlowCallDetailsActivity.sendCallLogDetailsList_VisitsFlow());
                    }

                }

                //Visit_NPA_StatusActivity -> Need To Call Later ->flow=VTC-ATCL
                if(getIntent().hasExtra("isFromVisit_NPAStatus_NeedToCallLater")){
                    getScheduleDateTime();
                    String dataSetId = getIntent().getStringExtra("dataSetId");
                    String visitedTheCustomer_AskedToCallLater = WebServices.visit_asked_to_call_later;
                    //get scheduleDateTime
                    getScheduleDateTime();
                    VisitsFlowCallDetailsActivity visitsFlowCallDetailsActivity = new VisitsFlowCallDetailsActivity();
                    visitsFlowViewModel.postVisitsFlow_DidNotVisitTheCustomer(visitedTheCustomer_AskedToCallLater, dataSetId, scheduleVisitForCollection_dateTime, "", "", "", "", "", visitsFlowCallDetailsActivity.sendCallLogDetailsList_VisitsFlow());


                    //Reset Call Count of dataSetId that will Appear in CallsForTheDay List
                    if (getIntent().hasExtra("dataSetIdToResetCallCount") ) {
                        System.out.println("Here ScheduleVisitForCollection AskedToCallBackLater dataSetId"+ dataSetId);
                        LeadCallDao leadCallDao = LeadListDB.getInstance(this).leadCallDao();
                        leadCallDao.UpdateLeadCallsUsingDataSetId(0, dataSetId);
                    }
                }

                //Visit_NPA_StatusActivity->Others->ScheduleCall
                if(getIntent().hasExtra("isFromVisitNPAStatusActivity_Others_ScheduleCall")){
                    getScheduleDateTime();
                    String dataSetId = getIntent().getStringExtra("dataSetId");
                    VisitsFlowCallDetailsActivity visitsFlowCallDetailsActivity = new VisitsFlowCallDetailsActivity();
                    visitsFlowViewModel.postScheduleCall_ScheduleVisit(WebServices.visit_othersScheduleCall,dataSetId,scheduleVisitForCollection_dateTime,"","","","",VisitsFlowCallDetailsActivity.send_reason,"","","","","",visitsFlowCallDetailsActivity.sendCallLogDetailsList_VisitsFlow());

                    //Reset Call Count of dataSetId that will Appear in CallsForTheDay List
                    if (getIntent().hasExtra("dataSetIdToResetCallCount") ) {
                        System.out.println("Here ScheduleVisitForCollection AskedToCallBackLater dataSetId"+ dataSetId);
                        LeadCallDao leadCallDao = LeadListDB.getInstance(this).leadCallDao();
                        leadCallDao.UpdateLeadCallsUsingDataSetId(0, dataSetId);
                    }

                }

                //Visit_NPA_StatusActivity->Others->ScheduleVisit
                if(getIntent().hasExtra("isFromVisitNPAStatusActivity_Others_ScheduleVisit")) {
                    String dataSetId = getIntent().getStringExtra("dataSetId");
                    getScheduleDateTime();
                    VisitsFlowCallDetailsActivity visitsFlowCallDetailsActivity = new VisitsFlowCallDetailsActivity();
                    visitsFlowViewModel.postScheduleCall_ScheduleVisit(WebServices.visit_othersScheduleVisit,dataSetId,scheduleVisitForCollection_dateTime,"","","","",VisitsFlowCallDetailsActivity.send_reason,"","","","","",visitsFlowCallDetailsActivity.sendCallLogDetailsList_VisitsFlow());
                }

                //for Visit_NPA_Notification->ScheduleCall
                //6 Buttons(LOF,PAM/CPM,NTL,LTBR,WPLS,OTH )
               if(getIntent().hasExtra("isFromVisitNPANotification_6ButtonsScheduleCall")){
                   getScheduleDateTime();
                   String dataSetId = getIntent().getStringExtra("dataSetId");
                   VisitsFlowCallDetailsActivity visitsFlowCallDetailsActivity = new VisitsFlowCallDetailsActivity();

                   if(getIntent().hasExtra("NotReadyToPay_LackOfFunds")){
                      visitsFlowViewModel.postScheduleCall_ScheduleVisit(WebServices.lackOfFundsScheduleCall,dataSetId,scheduleVisitForCollection_dateTime,"","","","","","","","","","",visitsFlowCallDetailsActivity.sendCallLogDetailsList_VisitsFlow());
                   }
                   else if(getIntent().hasExtra("NotReadyToPay_ClaimsPaymentMade")){
                       visitsFlowViewModel.postScheduleCall_ScheduleVisit(WebServices.paymentAlreadyMadeScheduleCall,dataSetId,scheduleVisitForCollection_dateTime,"","","","","","","","","","",visitsFlowCallDetailsActivity.sendCallLogDetailsList_VisitsFlow());
                   }
                   else if(getIntent().hasExtra("NotReadyToPay_NotTakenLoan")){
                       visitsFlowViewModel.postScheduleCall_ScheduleVisit(WebServices.notTakenLoanScheduleCall,dataSetId,scheduleVisitForCollection_dateTime,"","","","","","","","","","",visitsFlowCallDetailsActivity.sendCallLogDetailsList_VisitsFlow());
                   }
                   else if(getIntent().hasExtra("NotReadyToPay_LoanTakenByRelative")){
                       visitsFlowViewModel.postScheduleCall_ScheduleVisit(WebServices.loanTakenByRelativeScheduleCall,dataSetId,scheduleVisitForCollection_dateTime,"","",VisitsFlowCallDetailsActivity.send_RelativeName,VisitsFlowCallDetailsActivity.send_RelativeContact,"","","","","","",visitsFlowCallDetailsActivity.sendCallLogDetailsList_VisitsFlow());
                   }
                   else if(getIntent().hasExtra("NotReadyToPay_WillPayLumpSump")){
                       visitsFlowViewModel.postScheduleCall_ScheduleVisit(WebServices.willPayLumpSumScheduleCall,dataSetId,scheduleVisitForCollection_dateTime,"","","","","","","","","","",visitsFlowCallDetailsActivity.sendCallLogDetailsList_VisitsFlow());
                   }
                   else if(getIntent().hasExtra("NotReadyToPay_Others")){
                       visitsFlowViewModel.postScheduleCall_ScheduleVisit(WebServices.others_VisitNPANotificationScheduleCall,dataSetId,scheduleVisitForCollection_dateTime,"","","","",VisitsFlowCallDetailsActivity.send_reason,"","","","","",visitsFlowCallDetailsActivity.sendCallLogDetailsList_VisitsFlow());
                   }

                   //Reset Call Count of dataSetId that will Appear in CallsForTheDay List
                   if (getIntent().hasExtra("dataSetIdToResetCallCount") ) {
                       System.out.println("Here ScheduleVisitForCollection AskedToCallBackLater dataSetId"+ dataSetId);
                       LeadCallDao leadCallDao = LeadListDB.getInstance(this).leadCallDao();
                       leadCallDao.UpdateLeadCallsUsingDataSetId(0, dataSetId);
                   }
               }


               //for Visit_NPA_Notification->ScheduleVisit
                // 6 Buttons(LOF,PAM/CPM,NTL,LTBR,WPLS,OTH )
                if(getIntent().hasExtra("isFromVisitNPANotification_6ButtonsScheduleVisit")){
                    String dataSetId = getIntent().getStringExtra("dataSetId");

                    getScheduleDateTime();
                    VisitsFlowCallDetailsActivity visitsFlowCallDetailsActivity = new VisitsFlowCallDetailsActivity();

                    if(getIntent().hasExtra("NotReadyToPay_LackOfFunds")){
                        visitsFlowViewModel.postScheduleCall_ScheduleVisit(WebServices.lackOfFundsScheduleVisit,dataSetId,scheduleVisitForCollection_dateTime,"","","","","","","","","","",visitsFlowCallDetailsActivity.sendCallLogDetailsList_VisitsFlow());
                    }
                    else if(getIntent().hasExtra("NotReadyToPay_ClaimsPaymentMade")){
                        visitsFlowViewModel.postScheduleCall_ScheduleVisit(WebServices.paymentAlreadyMadeScheduleVisit,dataSetId,scheduleVisitForCollection_dateTime,"","","","","","","","","","",visitsFlowCallDetailsActivity.sendCallLogDetailsList_VisitsFlow());
                    }
                    else if(getIntent().hasExtra("NotReadyToPay_NotTakenLoan")){
                        visitsFlowViewModel.postScheduleCall_ScheduleVisit(WebServices.notTakenLoanScheduleVisit,dataSetId,scheduleVisitForCollection_dateTime,"","","","","","","","","","",visitsFlowCallDetailsActivity.sendCallLogDetailsList_VisitsFlow());
                    }
                    else if(getIntent().hasExtra("NotReadyToPay_LoanTakenByRelative")){
                        visitsFlowViewModel.postScheduleCall_ScheduleVisit(WebServices.loanTakenByRelativeScheduleVisit,dataSetId,scheduleVisitForCollection_dateTime,"","",VisitsFlowCallDetailsActivity.send_RelativeName,VisitsFlowCallDetailsActivity.send_RelativeContact,"","","","","","",visitsFlowCallDetailsActivity.sendCallLogDetailsList_VisitsFlow());
                    }
                    else if(getIntent().hasExtra("NotReadyToPay_WillPayLumpSump")){
                        visitsFlowViewModel.postScheduleCall_ScheduleVisit(WebServices.willPayLumpSumScheduleVisit,dataSetId,scheduleVisitForCollection_dateTime,"","","","","","","","","","",visitsFlowCallDetailsActivity.sendCallLogDetailsList_VisitsFlow());
                    }
                    else if(getIntent().hasExtra("NotReadyToPay_Others")){
                        visitsFlowViewModel.postScheduleCall_ScheduleVisit(WebServices.others_VisitNPANotificationScheduleVisit,dataSetId,scheduleVisitForCollection_dateTime,"","","","",VisitsFlowCallDetailsActivity.send_reason,"","","","","",visitsFlowCallDetailsActivity.sendCallLogDetailsList_VisitsFlow());
                    }

                }

                // Visit_Npa_NotAvailable_NeedToCloseVisit -  showDialogCloseAccount() - No Button click - ScheduleCall
                if(getIntent().hasExtra("Visit_Npa_NotAvailable_NeedToCloseVisit_ScheduleCall")){
                    getScheduleDateTime();
                    String dataSetId = getIntent().getStringExtra("dataSetId");
                    VisitsFlowCallDetailsActivity visitsFlowCallDetailsActivity = new VisitsFlowCallDetailsActivity();
                    visitsFlowViewModel.postScheduleCall_ScheduleVisit(WebServices.needToCloseVisitScheduleCall,dataSetId,scheduleVisitForCollection_dateTime,"","","","",VisitsFlowCallDetailsActivity.send_reason,"","","","","",visitsFlowCallDetailsActivity.sendCallLogDetailsList_VisitsFlow());

                    //Reset Call Count of dataSetId that will Appear in CallsForTheDay List
                    if (getIntent().hasExtra("dataSetIdToResetCallCount") ) {
                        System.out.println("Here ScheduleVisitForCollection AskedToCallBackLater dataSetId"+ dataSetId);
                        LeadCallDao leadCallDao = LeadListDB.getInstance(this).leadCallDao();
                        leadCallDao.UpdateLeadCallsUsingDataSetId(0, dataSetId);
                    }
                }

                // Visit_Npa_NotAvailable_NeedToCloseVisit -  showDialogCloseAccount() - No Button click - ScheduleVisit
                if(getIntent().hasExtra("Visit_Npa_NotAvailable_NeedToCloseVisit_ScheduleVisit")){
                    getScheduleDateTime();
                    String dataSetId = getIntent().getStringExtra("dataSetId");
                    VisitsFlowCallDetailsActivity visitsFlowCallDetailsActivity = new VisitsFlowCallDetailsActivity();
                    visitsFlowViewModel.postScheduleCall_ScheduleVisit(WebServices.needToCloseVisitScheduleVisit,dataSetId,scheduleVisitForCollection_dateTime,"","","","",VisitsFlowCallDetailsActivity.send_reason,"","","","","",visitsFlowCallDetailsActivity.sendCallLogDetailsList_VisitsFlow());
                }

                // PaymentInfoOfCustomerActivity 6 ButtonsScheduleCall(WPLS,AP,FNV,NTL,LTBR,OTH)->NPA Flow
                if(getIntent().hasExtra("PaymentInfoOfCustomerActivity6Buttons_ScheduleCall")){
                    getScheduleDateTime();
                    String dataSetId = getIntent().getStringExtra("dataSetId");
                    DetailsOfCustomerActivity detailsOfCustomerActivity = new DetailsOfCustomerActivity();

                    if(getIntent().hasExtra("isPaymentInfoOfCustomerActivity_WillPayLumpSum")){
                        visitsFlowViewModel.postScheduleCall_ScheduleVisit(WebServices.willPayLumpSum_PaymentInfo_ScheduleCall,dataSetId,scheduleVisitForCollection_dateTime,"","","","","","","","","","",detailsOfCustomerActivity.sendCallLogDetailsList_WillPayLater());
                    }
                   else if(getIntent().hasExtra("isPaymentInfoOfCustomerActivity_AlreadyPaid")){
                        visitsFlowViewModel.postScheduleCall_ScheduleVisit(WebServices.alreadyPaid_PaymentInfo_ScheduleCall,dataSetId,scheduleVisitForCollection_dateTime,"","","","","","","","","","",detailsOfCustomerActivity.sendCallLogDetailsList_WillPayLater());
                    }
                  else if(getIntent().hasExtra("isPaymentInfoOfCustomerActivity_FoNotVisited")){
                        visitsFlowViewModel.postScheduleCall_ScheduleVisit(WebServices.foNotVisited_PaymentInfo_ScheduleCall,dataSetId,scheduleVisitForCollection_dateTime, DetailsOfCustomerActivity.send_DateOfVisitPromised,DetailsOfCustomerActivity.send_FoName,"","","","","","","","",detailsOfCustomerActivity.sendCallLogDetailsList_FNV_LTBR("FNV"));
                    }
                 else if(getIntent().hasExtra("isPaymentInfoOfCustomerActivity_NotTakenLoan")){
                        visitsFlowViewModel.postScheduleCall_ScheduleVisit(WebServices.notTakenLoan_PaymentInfo_ScheduleCall,dataSetId,scheduleVisitForCollection_dateTime,"","","","","","","","","","",detailsOfCustomerActivity.sendCallLogDetailsList_WillPayLater());
                    }
                else if(getIntent().hasExtra("isPaymentInfoOfCustomerActivity_LoanTakenByRelative")){
                        visitsFlowViewModel.postScheduleCall_ScheduleVisit(WebServices.loanTakenByRelative_PaymentInfo_ScheduleCall,dataSetId,scheduleVisitForCollection_dateTime, "","",DetailsOfCustomerActivity.send_RelativeName,DetailsOfCustomerActivity.send_RelativeContact,"","","","","","",detailsOfCustomerActivity.sendCallLogDetailsList_FNV_LTBR("LTBR"));
                    }
                else if(getIntent().hasExtra("isPaymentInfoOfCustomerActivity_Others")){
                        visitsFlowViewModel.postScheduleCall_ScheduleVisit(WebServices.others_PaymentInfo_ScheduleCall,dataSetId,scheduleVisitForCollection_dateTime,"","","","",DetailsOfCustomerActivity.send_reason,"","","","","",detailsOfCustomerActivity.sendCallLogDetailsList_WillPayLater());
                    }

                    //Reset Call Count of dataSetId that will Appear in CallsForTheDay List
                    if (getIntent().hasExtra("dataSetIdToResetCallCount") ) {
                        System.out.println("Here ScheduleVisitForCollection AskedToCallBackLater dataSetId"+ dataSetId);
                        LeadCallDao leadCallDao = LeadListDB.getInstance(this).leadCallDao();
                        leadCallDao.UpdateLeadCallsUsingDataSetId(0, dataSetId);
                    }
                }


               // PaymentInfoOfCustomerActivity 6 ButtonsScheduleVisit(WPLS,AP,FNV,NTL,LTBR,OTH)
                  if(getIntent().hasExtra("PaymentInfoOfCustomerActivity6Buttons_ScheduleVisit")){

                      getScheduleDateTime();
                      String dataSetId = getIntent().getStringExtra("dataSetId");
                      DetailsOfCustomerActivity detailsOfCustomerActivity = new DetailsOfCustomerActivity();

                      if(getIntent().hasExtra("isPaymentInfoOfCustomerActivity_WillPayLumpSum")){
                          visitsFlowViewModel.postScheduleCall_ScheduleVisit(WebServices.willPayLumpSum_PaymentInfo_ScheduleVisit,dataSetId,scheduleVisitForCollection_dateTime,"","","","","","","","","","",detailsOfCustomerActivity.sendCallLogDetailsList_WillPayLater());
                      }
                    else  if(getIntent().hasExtra("isPaymentInfoOfCustomerActivity_AlreadyPaid")){
                          visitsFlowViewModel.postScheduleCall_ScheduleVisit(WebServices.alreadyPaid_PaymentInfo_ScheduleVisit,dataSetId,scheduleVisitForCollection_dateTime,"","","","","","","","","","",detailsOfCustomerActivity.sendCallLogDetailsList_WillPayLater());
                      }
                   else if(getIntent().hasExtra("isPaymentInfoOfCustomerActivity_FoNotVisited")){
                          visitsFlowViewModel.postScheduleCall_ScheduleVisit(WebServices.foNotVisited_PaymentInfo_ScheduleVisit,dataSetId,scheduleVisitForCollection_dateTime, DetailsOfCustomerActivity.send_DateOfVisitPromised,DetailsOfCustomerActivity.send_FoName,"","","","","","","","",detailsOfCustomerActivity.sendCallLogDetailsList_FNV_LTBR("FNV"));
                      }
                  else if(getIntent().hasExtra("isPaymentInfoOfCustomerActivity_NotTakenLoan")){
                          visitsFlowViewModel.postScheduleCall_ScheduleVisit(WebServices.notTakenLoan_PaymentInfo_ScheduleVisit,dataSetId,scheduleVisitForCollection_dateTime,"","","","","","","","","","",detailsOfCustomerActivity.sendCallLogDetailsList_WillPayLater());
                      }
                  else if(getIntent().hasExtra("isPaymentInfoOfCustomerActivity_LoanTakenByRelative")){
                          visitsFlowViewModel.postScheduleCall_ScheduleVisit(WebServices.loanTakenByRelative_PaymentInfo_ScheduleVisit,dataSetId,scheduleVisitForCollection_dateTime, "","",DetailsOfCustomerActivity.send_RelativeName,DetailsOfCustomerActivity.send_RelativeContact,"","","","","","",detailsOfCustomerActivity.sendCallLogDetailsList_FNV_LTBR("LTBR"));
                      }
                   else  if(getIntent().hasExtra("isPaymentInfoOfCustomerActivity_Others")){
                          visitsFlowViewModel.postScheduleCall_ScheduleVisit(WebServices.others_PaymentInfo_ScheduleVisit,dataSetId,scheduleVisitForCollection_dateTime,"","","","",DetailsOfCustomerActivity.send_reason,"","","","","",detailsOfCustomerActivity.sendCallLogDetailsList_WillPayLater());
                      }


                  }


                  //PaymentNotificationOfCustomer -> Others -> ScheduleCall
                if(getIntent().hasExtra("isPaymentNotificationOfCustomer_Others_ScheduleCall")){
                    getScheduleDateTime();
                    String dataSetId = getIntent().getStringExtra("dataSetId");
                    DetailsOfCustomerActivity detailsOfCustomerActivity = new DetailsOfCustomerActivity();
                    visitsFlowViewModel.postScheduleCall_ScheduleVisit(WebServices.call_details_spoke_to_customer_others_ScheduleCall,dataSetId,scheduleVisitForCollection_dateTime,"","","","",DetailsOfCustomerActivity.send_reason,"","","","","",detailsOfCustomerActivity.sendCallLogDetailsList_WillPayLater());

                    //Reset Call Count of dataSetId that will Appear in CallsForTheDay List
                    if (getIntent().hasExtra("dataSetIdToResetCallCount") ) {
                        System.out.println("Here ScheduleVisitForCollection AskedToCallBackLater dataSetId"+ dataSetId);
                        LeadCallDao leadCallDao = LeadListDB.getInstance(this).leadCallDao();
                        leadCallDao.UpdateLeadCallsUsingDataSetId(0, dataSetId);
                    }
                }

                //PaymentNotificationOfCustomer -> Others ->ScheduleVisit
                if(getIntent().hasExtra("isPaymentNotificationOfCustomer_Others_ScheduleVisit")){
                    getScheduleDateTime();
                    String dataSetId = getIntent().getStringExtra("dataSetId");
                    DetailsOfCustomerActivity detailsOfCustomerActivity = new DetailsOfCustomerActivity();
                    visitsFlowViewModel.postScheduleCall_ScheduleVisit(WebServices.call_details_spoke_to_customer_others_ScheduleVisit,dataSetId,scheduleVisitForCollection_dateTime,"","","","",DetailsOfCustomerActivity.send_reason,"","","","","",detailsOfCustomerActivity.sendCallLogDetailsList_WillPayLater());
                }

                //coming from NotSpokeToCustomer->NumberIsInvalid ->ScheduleVisit (ScheduleCall will not come for NumberIsInvalid scenario)
                if(getIntent().hasExtra("isFromNotSpokeToCustomer_NumberInvalid")){
                    getScheduleDateTime();
                    String dataSetId = getIntent().getStringExtra("dataSetId");
                    DetailsOfCustomerActivity detailsOfCustomerActivity = new DetailsOfCustomerActivity();
                    visitsFlowViewModel.postScheduleCall_ScheduleVisit(WebServices.notSpokeToCustomer_numberIsInvalid_ScheduleVisit,dataSetId,scheduleVisitForCollection_dateTime,"","","","","","","","","","",detailsOfCustomerActivity.sendCallLogDetailsList_WillPayLater());
                }

                initObserverScheduleCallScheduleVisit();

            } //if Button Update/Update Schedule is Clickable ends here

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    if(getIntent().hasExtra("isFromVisitNPANotAvailableActivity")){
                        Intent i = new Intent(ScheduleVisitForCollectionActivity.this, ActivityOfFragments.class);
                        startActivity(i);
                    }

                    if(getIntent().hasExtra("isWillPayLater")){

                        //then redirect to LoanCollection List
                        Intent i = new Intent(new Intent(ScheduleVisitForCollectionActivity.this, LoanCollectionActivity.class));
                        int DPD_row_position = Integer.parseInt(Global.getStringFromSharedPref(ScheduleVisitForCollectionActivity.this,"DPD_row_position"));
                        i.putExtra("DPD_row_position",DPD_row_position);
                        startActivity(i);
                    }


                    else{

                        Intent intent = new Intent(ScheduleVisitForCollectionActivity.this, NearByCustomersActivity.class);
                        startActivity(intent);
                    }

                }
            }, 2000);


        });

        binding.btnWillPayLumpsum.setOnClickListener(v -> {

            //get Details from PaymentInfoOfCustomerActivity and Pass To VisitCompletionOfCustomerActivity

            Intent i = new Intent(this, SubmitCompletionActivityOfCustomer.class);
            i.putExtra("dataSetId", getIntent().getStringExtra("dataSetId"));
            i.putExtra("detailsList",detailsList);
            i.putExtra("paymentInfo_WillPayLater",getIntent().getStringExtra("paymentInfo_WillPayLater"));
            i.putExtra("will_pay_later_will_pay_lumpsump","will_pay_later_will_pay_lumpsump"); //for Navigating to dashboard
            startActivity(i);
        });

    }

    private void getScheduleDateTime(){

        DatePicker datePicker = findViewById(R.id.datePickerCalendarView);
        TimePicker timePicker = findViewById(R.id.timePicker);

// Get the selected date from the DatePicker
        int year = datePicker.getYear();
        int month = datePicker.getMonth(); // Note: month starts from 0
        int dayOfMonth = datePicker.getDayOfMonth();

// Get the selected time from the TimePicker
        int hour, minute;
        // if android version > Marshmallow
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            hour = timePicker.getHour();
            minute = timePicker.getMinute();
        } else {
            hour = timePicker.getCurrentHour();
            minute = timePicker.getCurrentMinute();
        }

// Create a Calendar instance and set the selected date and time
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth, hour, minute);

// Retrieve the date and time in the desired format
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        scheduleVisitForCollection_dateTime = dateFormat.format(calendar.getTime());

        System.out.println("Here scheduledDateTime:"+scheduleVisitForCollection_dateTime);
  // Now, dateTime variable contains the selected date and time in the desired format
     //  String scheduleVisitForCollection_dateTime_new  = scheduleVisitForCollection_dateTime; //had to save in new String

        if(!Global.getStringFromSharedPref(this,"scheduleVisitForCollection_dateTime").isEmpty()){
            Global.removeStringInSharedPref(this,"scheduleVisitForCollection_dateTime");
        }

            Global.saveStringInSharedPref(this,"scheduleVisitForCollection_dateTime",scheduleVisitForCollection_dateTime);


    }

    public void sendFinalAttemptAndUpdateCallCount(String dataSetId) {

        if( null!= dataSetId){
            DetailsOfCustomerActivity.send_callAttemptNo = 5; //sending final Attempt No. as 5

            LeadCallDao leadCallDao = LeadListDB.getInstance(this).leadCallDao();
            leadCallDao.UpdateLeadCallsUsingDataSetId(0, dataSetId); //after 5th Attempt make call count to zero to remove hand gesture
            System.out.println("Here Call Count for "+ dataSetId + " is:" +leadCallDao.getCallCountUsingDataSetId(dataSetId));
        }


    }

    @Override
    protected void onResume() {

        // Get UserName , UserID , BranchCode

        MPinDao mPinDao = LeadListDB.getInstance(this).mPinDao();
        UserNameDao userNameDao = LeadListDB.getInstance(this).userNameDao();
        String userName = userNameDao.getUserNameUsingUserIDInUserNameRoomDB(mPinDao.getUserID());
        // Store UserName in SharedPreference and Use in StatusOfCustomerDetailsAdapter
        Global.saveStringInSharedPref(this,"userName",userName);

        MainActivity3API.UserID = mPinDao.getUserID();
        MainActivity3API.BranchCode = mPinDao.getBranchCode();

        System.out.println("Here ScheduleVisitForCustomerActivity onResume() UserID:"+MainActivity3API.UserID);
        System.out.println("Here ScheduleVisitForCustomerActivity onResume() BranchCode:"+MainActivity3API.BranchCode);

        CallDetailsViewModel.userId =  mPinDao.getUserID(); // for Calling Agent to not go null
        System.out.println("Here ScheduleVisitForCustomerActivity CallingAgent UserID:"+CallDetailsViewModel.userId);

        initializeFields();
        onClickListener();
        initObserver();

        super.onResume();
    }
}