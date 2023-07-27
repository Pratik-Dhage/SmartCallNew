package com.example.test.npa_flow;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.test.R;
import com.example.test.databinding.ActivityNotSpokeToCustomerBinding;
import com.example.test.fragments_activity.BalanceInterestCalculationActivity;
import com.example.test.helper_classes.Global;
import com.example.test.helper_classes.NetworkUtilities;
import com.example.test.main_dashboard.MainActivity3API;
import com.example.test.npa_flow.details_of_customer.DetailsOfCustomerActivity;
import com.example.test.npa_flow.details_of_customer.DetailsOfCustomerResponseModel;
import com.example.test.npa_flow.details_of_customer.DetailsOfCustomerViewModel;
import com.example.test.npa_flow.details_of_customer.adapter.DetailsOfCustomerAdapter;
import com.example.test.npa_flow.loan_collection.LoanCollectionActivity;
import com.example.test.roomDB.dao.CallDetailsListDao;
import com.example.test.roomDB.dao.LeadCallDao;
import com.example.test.roomDB.database.LeadListDB;
import com.example.test.roomDB.model.CallDetailsListRoomModel;
import com.example.test.roomDB.model.LeadCallModelRoom;
import com.example.test.schedule_flow.calls_for_the_day.CallsForTheDayActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class NotSpokeToCustomerActivity extends AppCompatActivity {

    ActivityNotSpokeToCustomerBinding binding;
    View view;
    DetailsOfCustomerViewModel detailsOfCustomerViewModel;
    ArrayList<DetailsOfCustomerResponseModel> detailsList;
    public static boolean notSpokeToCustomer = false; // for Call Attempts(Hands) to display ONLY in Case if User Did Not Spoke To Customer


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not_spoke_to_customer);


        initializeFields();
        setUpDetailsOfCustomerRecyclerView();
        onClickListener();

    }

    private void initializeFields() {

        binding = DataBindingUtil.setContentView(this, R.layout.activity_not_spoke_to_customer);
        view = binding.getRoot();
        detailsOfCustomerViewModel = new ViewModelProvider(this).get(DetailsOfCustomerViewModel.class);
        binding.setViewModel(detailsOfCustomerViewModel);

        //get detailsList
        detailsList = (ArrayList<DetailsOfCustomerResponseModel>) getIntent().getSerializableExtra("detailsList");

        notSpokeToCustomer = false;

        // **For DetailsOfCustomerActivity.FullName &  DetailsOfCustomerActivity.Mobile_Number to Not go Null **

        //Get FullName from detailsList
       if(detailsList.get(0).getLable().toLowerCase().contains("name")) {
           DetailsOfCustomerActivity.FullName = detailsList.get(0).getValue().toString();
       }

       //Get MobileNumber from detailsList
       detailsList.forEach(it->{
           if (it.getLable().toLowerCase().contains("mobile") || it.getLable().toLowerCase().contains("phone")){
               DetailsOfCustomerActivity.Mobile_Number = String.valueOf(it.getValue());
           }
       });


        System.out.println("DetailsOfCustomerActivity.FullName:"+DetailsOfCustomerActivity.FullName);
        System.out.println("DetailsOfCustomerActivity.Mobile_Number:"+ DetailsOfCustomerActivity.Mobile_Number);

    }


    private void setUpDetailsOfCustomerRecyclerView() {

        detailsOfCustomerViewModel.updateDetailsOfCustomer_Data();
        RecyclerView recyclerView = binding.rvDetailsOfCustomer;
        recyclerView.setAdapter(new DetailsOfCustomerAdapter(detailsList));
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
            //From CallsForTheDayAdapter
            if(getIntent().hasExtra("isFromCallsForTheDayAdapter")){
                System.out.println("Here isFromCallsForTheDayAdapter_NotSpokeToCustomerActivity");
                notSpokeToCustomer = true; // if Not Spoke To Customer is True Only then Show Call Attempts(Hands)

                if(null!= DetailsOfCustomerActivity.FullName || null!= DetailsOfCustomerActivity.Mobile_Number){

                    //if CallCount is becoming 4 then Navigate to SubmitCompletionActivityOfCustomer
                    if(getCallCountFromRoomDB(DetailsOfCustomerActivity.Mobile_Number)==2){
                        navigateToSubmitActivity("NoResponseBusy");
                        storeCallCountInRoomDB(DetailsOfCustomerActivity.FullName,DetailsOfCustomerActivity.Mobile_Number); //to remove hand gesture
                        storeCallDetailsInRoomDB(DetailsOfCustomerActivity.FullName,DetailsOfCustomerActivity.Mobile_Number,0); //to get 3 CallDateTime,CallDuration
                    }

                    else{
                        storeCallCountInRoomDB(DetailsOfCustomerActivity.FullName,DetailsOfCustomerActivity.Mobile_Number);
                        storeCallDetailsInRoomDB(DetailsOfCustomerActivity.FullName,DetailsOfCustomerActivity.Mobile_Number,0);
                        Intent i = new Intent(this, CallsForTheDayActivity.class);
                        startActivity(i);
                    }

                }


            }

            //From NPA
            else{
                System.out.println("Here isFromNPA_NotSpokeToCustomerActivity");
                notSpokeToCustomer = true; // if Not Spoke To Customer is True Only then Show Call Attempts(Hands)

                System.out.println("DetailsOfCustomerActivity.FullName"+DetailsOfCustomerActivity.FullName);
                System.out.println("DetailsOfCustomerActivity.Mobile_Number"+DetailsOfCustomerActivity.Mobile_Number);

                if(null!= DetailsOfCustomerActivity.FullName || null!= DetailsOfCustomerActivity.Mobile_Number){

                    //if CallCount is becoming 4 then Navigate to SubmitCompletionActivityOfCustomer
                    if(getCallCountFromRoomDB(DetailsOfCustomerActivity.Mobile_Number)==2){
                        navigateToSubmitActivity("NoResponseBusy");
                        storeCallCountInRoomDB(DetailsOfCustomerActivity.FullName,DetailsOfCustomerActivity.Mobile_Number); //to remove hand gesture
                        storeCallDetailsInRoomDB(DetailsOfCustomerActivity.FullName,DetailsOfCustomerActivity.Mobile_Number,0); //to get 3 CallDateTime,CallDuration
                    }
                    else{
                        storeCallCountInRoomDB(DetailsOfCustomerActivity.FullName,DetailsOfCustomerActivity.Mobile_Number);
                        storeCallDetailsInRoomDB(DetailsOfCustomerActivity.FullName,DetailsOfCustomerActivity.Mobile_Number,0);
                        // Get DPD_row_position saved in SharedPreference in DPD_Adapter Class
                        int DPD_row_position = Integer.parseInt(Global.getStringFromSharedPref(this, "DPD_row_position"));

                        Intent i = new Intent(NotSpokeToCustomerActivity.this, LoanCollectionActivity.class);
                        i.putExtra("DPD_row_position", DPD_row_position);
                        startActivity(i);
                    }

                }

            }

        });

        binding.btnNotReachableSwitchedOff.setOnClickListener(v -> {
            //From CallsForTheDayAdapter
            if(getIntent().hasExtra("isFromCallsForTheDayAdapter")){
                System.out.println("Here isFromCallsForTheDayAdapter_NotSpokeToCustomerActivity");
                notSpokeToCustomer = true; // if Not Spoke To Customer is True Only then Show Call Attempts(Hands)

                if(null!= DetailsOfCustomerActivity.FullName || null!= DetailsOfCustomerActivity.Mobile_Number){

                    //if CallCount is becoming 4 then Navigate to SubmitCompletionActivityOfCustomer
                    if(getCallCountFromRoomDB(DetailsOfCustomerActivity.Mobile_Number)==2){
                        navigateToSubmitActivity("SwitchOff");
                        storeCallCountInRoomDB(DetailsOfCustomerActivity.FullName,DetailsOfCustomerActivity.Mobile_Number); //to remove hand gesture
                        storeCallDetailsInRoomDB(DetailsOfCustomerActivity.FullName,DetailsOfCustomerActivity.Mobile_Number,0); //to get 3 CallDateTime,CallDuration
                    }
                    else{

                        storeCallCountInRoomDB(DetailsOfCustomerActivity.FullName,DetailsOfCustomerActivity.Mobile_Number);
                        storeCallDetailsInRoomDB(DetailsOfCustomerActivity.FullName,DetailsOfCustomerActivity.Mobile_Number,0);
                        Intent i = new Intent(this, CallsForTheDayActivity.class);
                        startActivity(i);
                    }

                }


            }

            //From NPA
            else{
                System.out.println("Here isFromNPA_NotSpokeToCustomerActivity");
                notSpokeToCustomer = true; // if Not Spoke To Customer is True Only then Show Call Attempts(Hands)

                if(null!= DetailsOfCustomerActivity.FullName || null!= DetailsOfCustomerActivity.Mobile_Number){

                    //if CallCount is becoming 4 then Navigate to SubmitCompletionActivityOfCustomer
                    if(getCallCountFromRoomDB(DetailsOfCustomerActivity.Mobile_Number)==2){
                        navigateToSubmitActivity("SwitchOff");
                        storeCallCountInRoomDB(DetailsOfCustomerActivity.FullName,DetailsOfCustomerActivity.Mobile_Number); //to remove hand gesture
                        storeCallDetailsInRoomDB(DetailsOfCustomerActivity.FullName,DetailsOfCustomerActivity.Mobile_Number,0); //to get 3 CallDateTime,CallDuration
                    }
                    else{
                        storeCallCountInRoomDB(DetailsOfCustomerActivity.FullName,DetailsOfCustomerActivity.Mobile_Number);
                        storeCallDetailsInRoomDB(DetailsOfCustomerActivity.FullName,DetailsOfCustomerActivity.Mobile_Number,0);

                        // Get DPD_row_position saved in SharedPreference in DPD_Adapter Class
                        int DPD_row_position = Integer.parseInt(Global.getStringFromSharedPref(this, "DPD_row_position"));

                        Intent i = new Intent(NotSpokeToCustomerActivity.this, LoanCollectionActivity.class);
                        i.putExtra("DPD_row_position", DPD_row_position);
                        startActivity(i);
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

        binding.btnPhysicalVisitRequired.setOnClickListener(v->{
            Intent i = new Intent(NotSpokeToCustomerActivity.this, ScheduleVisitForCollectionActivity.class);
            i.putExtra("dataSetId", getIntent().getStringExtra("dataSetId"));
            i.putExtra("isFromNotSpokeToCustomer_PhysicalVisitRequired","isFromNotSpokeToCustomer_PhysicalVisitRequired");
            startActivity(i);
        });

    }

    public void navigateToSubmitActivity(String apiType){


        if(apiType.contentEquals("NoResponseBusy")){
            Intent i = new Intent(this, SubmitCompletionActivityOfCustomer.class);
            i.putExtra("dataSetId", getIntent().getStringExtra("dataSetId"));
            i.putExtra("detailsList",detailsList);
            i.putExtra("isFromNotSpokeToCustomer_NoResponseBusy","isFromNotSpokeToCustomer_NoResponseBusy");
            startActivity(i);

        }
        else if( apiType.contentEquals("SwitchOff")){
            Intent i = new Intent(this, SubmitCompletionActivityOfCustomer.class);
            i.putExtra("dataSetId", getIntent().getStringExtra("dataSetId"));
            i.putExtra("detailsList",detailsList);
            i.putExtra("isFromNotSpokeToCustomer_SwitchOff","isFromNotSpokeToCustomer_SwitchOff");
            startActivity(i);
        }

    }

    public int getCallCountFromRoomDB(String phoneNumber){
        LeadCallDao leadCallDao = LeadListDB.getInstance(this).leadCallDao();
        System.out.println("Here getCallCountFromRoomDB for :"+phoneNumber+" is "+leadCallDao.getCallCountUsingPhoneNumber(phoneNumber));
        return leadCallDao.getCallCountUsingPhoneNumber(phoneNumber);
    }

    public void storeCallDetailsInRoomDB(String fullName ,String phoneNumber  ,int callDuration){
        CallDetailsListDao callDetailsListDao = LeadListDB.getInstance(this).callDetailsListDao();

        String pattern = "yyyy-MM-dd HH:mm:ss"; // Pattern to match the date format
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        try{
            Date date = new Date();
            String formattedCallDateTime = dateFormat.format(date);
            CallDetailsListRoomModel callDetailsListRoomModel = new CallDetailsListRoomModel(fullName,phoneNumber,formattedCallDateTime,callDuration);
            callDetailsListDao.insert(callDetailsListRoomModel);
            // Will get 1st CallDateTime in Logcat for all 3 calls as it is fetching using same mobileNumber
            System.out.println("Here CallDetailsDateTimeFromDB:"+callDetailsListDao.getCallDateTimeUsingMobileNumber(phoneNumber));
        }
        catch(Exception e ){
            e.printStackTrace();
        }

    }


    public void storeCallCountInRoomDB(String firstName, String phoneNumber) {

        LeadCallDao leadCallDao = LeadListDB.getInstance(this).leadCallDao();
        int callCount = leadCallDao.getCallCountUsingPhoneNumber(phoneNumber);
        callCount++; //callCount+1
        LeadCallModelRoom leadCallModelRoom = new LeadCallModelRoom(callCount, firstName, phoneNumber);

        leadCallDao.insert(leadCallModelRoom);
        //  if Call Count >2 then make it to zero

        if (callCount > 2) {
            callCount = 0;
        }

        leadCallDao.UpdateLeadCalls(callCount, phoneNumber);

       // Global.showToast(this, "Call Count for " + phoneNumber + " is: " + leadCallDao.getCallCountUsingPhoneNumber(phoneNumber));
        System.out.println("Here Call Count for " + phoneNumber + " is: " + leadCallDao.getCallCountUsingPhoneNumber(phoneNumber));

        DetailsOfCustomerActivity.send_callAttemptNo = callCount;
    }


    @Override
    protected void onResume() {
        initializeFields();
        onClickListener();
        setUpDetailsOfCustomerRecyclerView();
        super.onResume();
    }
}