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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.test.R;
import com.example.test.api_manager.WebServices;
import com.example.test.databinding.ActivityCallDetailOfCustomerBinding;
import com.example.test.databinding.ActivityNotSpokeToCustomerBinding;
import com.example.test.fragments_activity.AlternateNumberApiCall;
import com.example.test.fragments_activity.BalanceInterestCalculationActivity;
import com.example.test.helper_classes.Global;
import com.example.test.helper_classes.NetworkUtilities;
import com.example.test.main_dashboard.MainActivity3API;
import com.example.test.npa_flow.call_details.CallDetailsViewModel;
import com.example.test.npa_flow.details_of_customer.DetailsOfCustomerActivity;
import com.example.test.npa_flow.details_of_customer.DetailsOfCustomerResponseModel;
import com.example.test.npa_flow.details_of_customer.DetailsOfCustomerViewModel;
import com.example.test.npa_flow.details_of_customer.adapter.DetailsOfCustomerAdapter;
import com.example.test.npa_flow.loan_collection.LoanCollectionActivity;
import com.example.test.npa_flow.radio_buttons.RadioButtonsReasonAdapter;
import com.example.test.npa_flow.radio_buttons.RadioButtonsViewModel;
import com.example.test.roomDB.dao.CallDetailsListDao;
import com.example.test.roomDB.dao.LeadCallDao;
import com.example.test.roomDB.dao.MPinDao;
import com.example.test.roomDB.database.LeadListDB;
import com.example.test.roomDB.model.CallDetailsListRoomModel;
import com.example.test.roomDB.model.LeadCallModelRoom;
import com.example.test.schedule_flow.calls_for_the_day.CallsForTheDayActivity;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class NotSpokeToCustomerActivity extends AppCompatActivity {

    ActivityNotSpokeToCustomerBinding binding;
    View view;
    DetailsOfCustomerViewModel detailsOfCustomerViewModel;
    CallDetailsViewModel callDetailsViewModel;
    RadioButtonsViewModel radioButtonsViewModel;
    ArrayList<DetailsOfCustomerResponseModel> detailsList;
    public static boolean notSpokeToCustomer = false; // for Call Attempts(Hands) to display ONLY in Case if User Did Not Spoke To Customer
    public static boolean isRadioButtonSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not_spoke_to_customer);


        initializeFields();
        setUpDetailsOfCustomerRecyclerView();
        onClickListener();
        initObserver();
        callRadioButtonReasonApi();
        initObserverRadioButtonData();

    }

    private void initializeFields() {

        binding = DataBindingUtil.setContentView(this, R.layout.activity_not_spoke_to_customer);
        view = binding.getRoot();
        detailsOfCustomerViewModel = new ViewModelProvider(this).get(DetailsOfCustomerViewModel.class);
        callDetailsViewModel = new ViewModelProvider(this).get(CallDetailsViewModel.class);
        radioButtonsViewModel = new ViewModelProvider(this).get(RadioButtonsViewModel.class);
        binding.setViewModel(detailsOfCustomerViewModel);

        //get detailsList
        detailsList = (ArrayList<DetailsOfCustomerResponseModel>) getIntent().getSerializableExtra("detailsList");
        detailsList = (ArrayList<DetailsOfCustomerResponseModel>) Global.getUpdatedDetailsList(detailsList); //to get Updated List

        notSpokeToCustomer = false;
        isRadioButtonSelected = false; //initially it will be false, in RadioButtonReasonAdapter only when radioButton is clicked it will be true

        // **For DetailsOfCustomerActivity.FullName &  DetailsOfCustomerActivity.Mobile_Number to Not go Null **

        //Get FullName from detailsList
       if(detailsList.get(0).getLable().toLowerCase().contains("name")) {
           DetailsOfCustomerActivity.FullName = detailsList.get(0).getValue().toString();
       }

       //Get MobileNumber from detailsList
       detailsList.forEach(it->{
           if (it.getLable().toLowerCase().contains("mobile") || it.getLable().toLowerCase().contains("phone")){

             if(null!=it.getValue()){
                 DetailsOfCustomerActivity.Mobile_Number = String.valueOf(it.getValue());
                 System.out.println("Here NotSpokeToCustomerActivity detailsList Mobile Number:"+DetailsOfCustomerActivity.Mobile_Number);
             }

           }
       });


        System.out.println("DetailsOfCustomerActivity.FullName:"+DetailsOfCustomerActivity.FullName);
        System.out.println("DetailsOfCustomerActivity.Mobile_Number:"+ DetailsOfCustomerActivity.Mobile_Number);
        System.out.println("NotSpokeToCustomerActivity Selected Mobile_Number:"+ DetailsOfCustomerActivity.selectedMobileNumber);


        //UserID & BranchCode from RoomDB
        MPinDao mPinDao = LeadListDB.getInstance(this).mPinDao();
        MainActivity3API.UserID = mPinDao.getUserID();
        MainActivity3API.BranchCode = mPinDao.getBranchCode();

    }


    private void setUpDetailsOfCustomerRecyclerView() {

        detailsOfCustomerViewModel.updateDetailsOfCustomer_Data();
        RecyclerView recyclerView = binding.rvDetailsOfCustomer;
        recyclerView.setAdapter(new DetailsOfCustomerAdapter(this,detailsList));
    }


    private void callRadioButtonReasonApi(){
        if(NetworkUtilities.getConnectivityStatus(this)){
            radioButtonsViewModel.getRadioButtonsReason_Data(WebServices.radio_buttons_reason);
        }
        else{
            Global.showSnackBar(view,getString(R.string.check_internet_connection));
        }
    }

    private void initObserverRadioButtonData(){
        if(NetworkUtilities.getConnectivityStatus(this)){
            radioButtonsViewModel.getMutRadioButtonsReason_ResponseApi().observe(this,result2->{

                if(result2!=null){
                    radioButtonsViewModel.arrList_RadioButtonsReason_Data.clear();
                    radioButtonsViewModel.arrList_RadioButtonsReason_Data.addAll(result2);
                }
            });

            radioButtonsViewModel.getMutErrorResponse().observe(this,error->{
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

    private void initObserver(){

        if(NetworkUtilities.getConnectivityStatus(this)) {
            callDetailsViewModel.getMutCallDetailsResponseApi().observe(this, result -> {

                if(result!=null){
                    // Global.showToast(this,"Server Response:"+result);
                    Global.showSnackBar(view, result);
                    System.out.println("Here Server Response: "+result);

                    //Remove CallDetails
                    // After NoResponse/Busy & NotReachableSwitchOff Api Call to send 1 New List Everytime
                    CallDetailsListDao callDetailsListDao = LeadListDB.getInstance(this).callDetailsListDao();
                    String dataSetId = getIntent().getStringExtra("dataSetId");
                    callDetailsListDao.deleteCallDetailsListUsingDataSetId(dataSetId);

                    // Navigate To Respective List after Server Response
                    if(getIntent().hasExtra("isFromCallsForTheDayAdapter")|| Global.getStringFromSharedPref(this,"isFromCallsForTheDayAdapter").equals("true")){
                        navigateToCallsForTheDayList();
                    }
                    else{
                        navigateToNPAList();
                    }
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

                //Call Save Alternate Number API
                if(null!= DetailsOfCustomerAdapter.alternateNumber && null!=DetailsOfCustomerAdapter.dataSetId){
                    System.out.println("Save Alternate No. Api dataSetId:"+DetailsOfCustomerAdapter.dataSetId+" Alternate No. :"+DetailsOfCustomerAdapter.alternateNumber);
                    AlternateNumberApiCall.saveAlternateNumber(NotSpokeToCustomerActivity.this,DetailsOfCustomerAdapter.alternateNumber,DetailsOfCustomerAdapter.dataSetId);
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

            Global.showNotesEditDialog(this);
        });

        //for History
        binding.ivHistory.setOnClickListener(v -> {

            String dataSetId = getIntent().getStringExtra("dataSetId");
            Global.showNotesHistoryDialog(this,dataSetId);

        });


        binding.btnNoResponseBusy.setOnClickListener(v -> {

            //Call Save Alternate Number API
            if(null!= DetailsOfCustomerAdapter.alternateNumber && null!=DetailsOfCustomerAdapter.dataSetId){
                System.out.println("Save Alternate No. Api dataSetId:"+DetailsOfCustomerAdapter.dataSetId+" Alternate No. :"+DetailsOfCustomerAdapter.alternateNumber);
                AlternateNumberApiCall.saveAlternateNumber(this,DetailsOfCustomerAdapter.alternateNumber,DetailsOfCustomerAdapter.dataSetId);
            }

            String dataSetId = getIntent().getStringExtra("dataSetId");
            System.out.println("NoResponseBusy dataSetId:"+dataSetId);

            //From CallsForTheDayAdapter
            if(getIntent().hasExtra("isFromCallsForTheDayAdapter")){
                System.out.println("Here isFromCallsForTheDayAdapter_NotSpokeToCustomerActivity");
                notSpokeToCustomer = true; // if Not Spoke To Customer is True Only then Show Call Attempts(Hands)

                if(null!= dataSetId){

                    //if CallCount is becoming 5(0,1,2,3,4)
                    //At 5th Attempt Navigate to ScheduleVisitForCollectionActivity
                    if(getCallCountFromRoomDB(dataSetId)==4){
                        navigateToScheduleVisitForCollectionActivity("NoResponseBusy");
                       // storeCallCountInRoomDB(dataSetId); //to remove hand gesture
                      //  storeCallDetailsInRoomDB(0,dataSetId); //to get  CallDateTime,CallDuration
                    }

                    else{
                        storeCallCountInRoomDB(dataSetId);
                        storeCallDetailsInRoomDB(0,dataSetId);

                       // String dataSetId = getIntent().getStringExtra("dataSetId");
                        CallDetailsListDao callDetailsListDao = LeadListDB.getInstance(this).callDetailsListDao();
                        List<CallDetailsListRoomModel> callDetailsListRoomModelList = callDetailsListDao.getCallLogDetailsUsingDataSetId(dataSetId);
                        callDetailsViewModel.postCallDetailsNotSpokeToCustomer_NumberIsBusy_SwitchedOff(WebServices.notSpokeToCustomer_numberIsBusy, dataSetId, callDetailsListRoomModelList);
                       // navigateToCallsForTheDayList();

                    }

                }


            }

            //From NPA
            else{
                System.out.println("Here isFromNPA_NotSpokeToCustomerActivity");
                notSpokeToCustomer = true; // if Not Spoke To Customer is True Only then Show Call Attempts(Hands)

                System.out.println("DetailsOfCustomerActivity.FullName"+DetailsOfCustomerActivity.FullName);
                System.out.println("DetailsOfCustomerActivity.selectedMobileNumber"+DetailsOfCustomerActivity.selectedMobileNumber);

                if(null!= dataSetId){

                    //if CallCount is becoming 5(0,1,2,3,4)
                    //At 5th Attempt Navigate to ScheduleVisitForCollectionActivity
                    if(getCallCountFromRoomDB(dataSetId)==4){
                        navigateToScheduleVisitForCollectionActivity("NoResponseBusy");
                       // storeCallCountInRoomDB(dataSetId); //to remove hand gesture
                     //   storeCallDetailsInRoomDB(0, dataSetId); //to get  CallDateTime,CallDuration
                    }
                    else{
                        storeCallCountInRoomDB(dataSetId);
                        storeCallDetailsInRoomDB(0,dataSetId);

                       // String dataSetId = getIntent().getStringExtra("dataSetId");
                        CallDetailsListDao callDetailsListDao = LeadListDB.getInstance(this).callDetailsListDao();
                        List<CallDetailsListRoomModel> callDetailsListRoomModelList = callDetailsListDao.getCallLogDetailsUsingDataSetId(dataSetId);
                        callDetailsViewModel.postCallDetailsNotSpokeToCustomer_NumberIsBusy_SwitchedOff(WebServices.notSpokeToCustomer_numberIsBusy, dataSetId, callDetailsListRoomModelList);
                        // navigateToNPAList();

                    }

                }

            }

        });

        binding.btnNotReachableSwitchedOff.setOnClickListener(v -> {

            String dataSetId = getIntent().getStringExtra("dataSetId");
            System.out.println("NotReachableSwitchOff dataSetId:"+dataSetId);

            //From CallsForTheDayAdapter
            if(getIntent().hasExtra("isFromCallsForTheDayAdapter")){
                System.out.println("Here isFromCallsForTheDayAdapter_NotSpokeToCustomerActivity");
                notSpokeToCustomer = true; // if Not Spoke To Customer is True Only then Show Call Attempts(Hands)

                if(null!= dataSetId){

                    //if CallCount is becoming 5(0,1,2,3,4)
                    //At 5th Attempt Navigate to ScheduleVisitForCollectionActivity
                    if(getCallCountFromRoomDB(dataSetId)==4){
                        navigateToScheduleVisitForCollectionActivity("SwitchOff");
                      //  storeCallCountInRoomDB(dataSetId); //to remove hand gesture
                      //  storeCallDetailsInRoomDB(0, dataSetId); //to get  CallDateTime,CallDuration
                    }
                    else{

                        storeCallCountInRoomDB(dataSetId);
                        storeCallDetailsInRoomDB(0, dataSetId);

                       // String dataSetId = getIntent().getStringExtra("dataSetId");
                        CallDetailsListDao callDetailsListDao = LeadListDB.getInstance(this).callDetailsListDao();
                        List<CallDetailsListRoomModel> callDetailsListRoomModelList = callDetailsListDao.getCallLogDetailsUsingDataSetId(dataSetId);
                        callDetailsViewModel.postCallDetailsNotSpokeToCustomer_NumberIsBusy_SwitchedOff(WebServices.notSpokeToCustomer_numberSwitchedOff, dataSetId, callDetailsListRoomModelList);
                       // navigateToCallsForTheDayList();
                    }

                }


            }

            //From NPA
            else{
                System.out.println("Here isFromNPA_NotSpokeToCustomerActivity");
                notSpokeToCustomer = true; // if Not Spoke To Customer is True Only then Show Call Attempts(Hands)

                if(null!= dataSetId){

                    //if CallCount is becoming 5(0,1,2,3,4)
                    //At 5th Attempt Navigate to ScheduleVisitForCollectionActivity
                    if(getCallCountFromRoomDB(dataSetId)==4){
                        navigateToScheduleVisitForCollectionActivity("SwitchOff");
                       // storeCallCountInRoomDB(dataSetId); //to remove hand gesture
                     //   storeCallDetailsInRoomDB(0,dataSetId); //to get  CallDateTime,CallDuration
                    }
                    else{
                        storeCallCountInRoomDB(dataSetId);
                        storeCallDetailsInRoomDB(0,dataSetId);

                      //  String dataSetId = getIntent().getStringExtra("dataSetId");
                        CallDetailsListDao callDetailsListDao = LeadListDB.getInstance(this).callDetailsListDao();
                        List<CallDetailsListRoomModel> callDetailsListRoomModelList = callDetailsListDao.getCallLogDetailsUsingDataSetId(dataSetId);
                        callDetailsViewModel.postCallDetailsNotSpokeToCustomer_NumberIsBusy_SwitchedOff(WebServices.notSpokeToCustomer_numberSwitchedOff, dataSetId, callDetailsListRoomModelList);
                        //navigateToNPAList();
                    }

                }

            }

        });



        binding.btnNumberIsInvalid.setOnClickListener(v -> {
            Intent i = new Intent(NotSpokeToCustomerActivity.this, SubmitCompletionActivityOfCustomer.class);
            i.putExtra("dataSetId", getIntent().getStringExtra("dataSetId"));
            i.putExtra("detailsList",detailsList);
            i.putExtra("isFromNotSpokeToCustomer_NumberInvalid","isFromNotSpokeToCustomer_NumberInvalid");
            startActivity(i);
        });


        // Send Reason Compulsory
        binding.btnPhysicalVisitRequired.setOnClickListener(v->{

           View customDialogRadioButton = LayoutInflater.from(this).inflate(R.layout.custom_dialog_radio_button, null);
            ImageView ivCancel = customDialogRadioButton.findViewById(R.id.ivCancel);

            Button btnProceed = customDialogRadioButton.findViewById(R.id.btnProceed);
            RecyclerView recyclerView = customDialogRadioButton.findViewById(R.id.rvRadioButton);


            //setUpRadioButtonDataRecyclerView()
            radioButtonsViewModel.updateRadioButtonReasons_Data();
            recyclerView.setAdapter(new RadioButtonsReasonAdapter(radioButtonsViewModel.arrList_RadioButtonsReason_Data));

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(customDialogRadioButton);
            final AlertDialog dialog = builder.create();
            dialog.setCancelable(true);
            dialog.show();


            btnProceed.setOnClickListener(v1->{

                    if(!isRadioButtonSelected){
                        Global.showSnackBar(view,"Please Select Reason");
                    }

                else  {

                    Intent i = new Intent(NotSpokeToCustomerActivity.this, ScheduleVisitForCollectionActivity.class);
                    i.putExtra("dataSetId", getIntent().getStringExtra("dataSetId"));
                    i.putExtra("reason",DetailsOfCustomerActivity.send_reason);
                    i.putExtra("isFromNotSpokeToCustomer_PhysicalVisitRequired","isFromNotSpokeToCustomer_PhysicalVisitRequired");
                    startActivity(i);

                }

                });

            ivCancel.setOnClickListener(v1 -> {
                dialog.dismiss();
            });

        });

    }

    public void navigateToScheduleVisitForCollectionActivity(String apiType){


        if(apiType.contentEquals("NoResponseBusy")){
            Intent i = new Intent(this, ScheduleVisitForCollectionActivity.class);
            i.putExtra("dataSetId", getIntent().getStringExtra("dataSetId"));
            i.putExtra("detailsList",detailsList);
            i.putExtra("isFromNotSpokeToCustomer_NoResponseBusy","isFromNotSpokeToCustomer_NoResponseBusy");
            startActivity(i);

        }
        else if( apiType.contentEquals("SwitchOff")){
            Intent i = new Intent(this, ScheduleVisitForCollectionActivity.class);
            i.putExtra("dataSetId", getIntent().getStringExtra("dataSetId"));
            i.putExtra("detailsList",detailsList);
            i.putExtra("isFromNotSpokeToCustomer_SwitchOff","isFromNotSpokeToCustomer_SwitchOff");
            startActivity(i);
        }

    }

    private void navigateToCallsForTheDayList(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(NotSpokeToCustomerActivity.this, CallsForTheDayActivity.class);
                startActivity(i);
            }
        },1000);
    }

    private void navigateToNPAList(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                // Get DPD_row_position saved in SharedPreference in DPD_Adapter Class
                int DPD_row_position = Integer.parseInt(Global.getStringFromSharedPref(NotSpokeToCustomerActivity.this, "DPD_row_position"));
                Intent i = new Intent(NotSpokeToCustomerActivity.this, LoanCollectionActivity.class);
                i.putExtra("DPD_row_position", DPD_row_position);
                startActivity(i);

            }
        },1000);
    }

    public int getCallCountFromRoomDB(String dataSetId){
        LeadCallDao leadCallDao = LeadListDB.getInstance(this).leadCallDao();
        System.out.println("Here getCallCountFromRoomDB for :"+dataSetId+" is "+leadCallDao.getCallCountUsingDataSetId(dataSetId));
        return leadCallDao.getCallCountUsingDataSetId(dataSetId);
    }

    public void storeCallDetailsInRoomDB(int callDuration, String dataSetId){
        CallDetailsListDao callDetailsListDao = LeadListDB.getInstance(this).callDetailsListDao();

        callDetailsListDao.deleteCallDetailsListUsingDataSetId(dataSetId); //to delete previous call details list if exists
        System.out.println("Before Storing CallDetailsListFromRoomDB Size for: "+dataSetId+" is "+callDetailsListDao.getCountOfCallDetailsListUsingDataSetId(dataSetId));

        String pattern = "yyyy-MM-dd HH:mm:ss"; // Pattern to match the date format
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        try{
            Date date = new Date();
            String formattedCallDateTime = dateFormat.format(date);

            // Check the current attemptNo for the phoneNumber in the database

            LeadCallDao leadCallDao = LeadListDB.getInstance(this).leadCallDao();
           // int callCount = leadCallDao.getCallCountUsingPhoneNumber(phoneNumber);

            int currentAttemptNo = leadCallDao.getCallCountUsingDataSetId(dataSetId); //using callCount as attemptNo.
            System.out.println(currentAttemptNo);
            CallDetailsListRoomModel callDetailsListRoomModel = new CallDetailsListRoomModel(formattedCallDateTime,callDuration,currentAttemptNo,dataSetId);
            callDetailsListDao.insert(callDetailsListRoomModel);


         // If attemptNo is at attemptNo=5 i.e (0,1,2,3,4) then make it zero
            if (currentAttemptNo > 5) {
                currentAttemptNo=0;
            }
            callDetailsListDao.UpdateAttemptNoUsingDataSetId(currentAttemptNo,dataSetId);




            // Will get  CallDateTime in Logcat
            System.out.println("Here CallDetailsDateTimeFromDB:"+callDetailsListDao.getCallDateTimeUsingDataSetId(dataSetId));
            System.out.println("After Storing CallDetailsListFromRoomDB Size for: "+dataSetId+" is "+callDetailsListDao.getCountOfCallDetailsListUsingDataSetId(dataSetId));

        }
        catch(Exception e ){
            e.printStackTrace();
        }

    }


    public void storeCallCountInRoomDB( String dataSetId) {

        if( null!= dataSetId){

            LeadCallDao leadCallDao = LeadListDB.getInstance(this).leadCallDao();
            int callCount = leadCallDao.getCallCountUsingDataSetId(dataSetId);
            callCount++; //callCount+1
            LeadCallModelRoom leadCallModelRoom = new LeadCallModelRoom(callCount, dataSetId);

            leadCallDao.insert(leadCallModelRoom);
            //  if CallCount=5 means (0,1,2,3,4) then make it to zero

            if (callCount > 5) {
                callCount = 0;
            }

            leadCallDao.UpdateLeadCallsUsingDataSetId(callCount, dataSetId);

            System.out.println("Here Call Count for "+ dataSetId + " is:" +leadCallDao.getCallCountUsingDataSetId(dataSetId));
           // System.out.println("Here Call Count for " + phoneNumber + " is: " + leadCallDao.getCallCountUsingPhoneNumber(phoneNumber));

            DetailsOfCustomerActivity.send_callAttemptNo = callCount;

        }


    }


    @Override
    protected void onResume() {
        initializeFields();
        setUpDetailsOfCustomerRecyclerView();
        onClickListener();
        initObserver();
        callRadioButtonReasonApi();
        initObserverRadioButtonData();
        super.onResume();
    }

    public ActivityNotSpokeToCustomerBinding getBinding(){
        return binding;
    }
}