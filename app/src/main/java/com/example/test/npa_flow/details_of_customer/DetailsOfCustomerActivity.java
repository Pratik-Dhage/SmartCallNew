package com.example.test.npa_flow.details_of_customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.test.R;
import com.example.test.databinding.ActivityCallDetailOfCustomerBinding;
import com.example.test.databinding.ActivityDetailsOfCustomer2Binding;
import com.example.test.fragments_activity.AlternateNumberApiCall;
import com.example.test.helper_classes.Global;
import com.example.test.helper_classes.NetworkUtilities;
import com.example.test.main_dashboard.MainActivity3API;
import com.example.test.npa_flow.CallDetailOfCustomerActivity;
import com.example.test.npa_flow.call_details.CallDetails;
import com.example.test.npa_flow.details_of_customer.adapter.DetailsOfCustomerAdapter;
import com.example.test.roomDB.dao.LeadCallDao;
import com.example.test.roomDB.dao.MPinDao;
import com.example.test.roomDB.dao.UserNameDao;
import com.example.test.roomDB.database.LeadListDB;
import com.example.test.roomDB.model.LeadCallModelRoom;
import com.example.test.schedule_flow.calls_for_the_day.adapter.CallsForTheDayAdapter;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class DetailsOfCustomerActivity extends AppCompatActivity {

    ActivityDetailsOfCustomer2Binding binding;
    View view;
    DetailsOfCustomerViewModel detailsOfCustomerViewModel;
    public static String FullName; //for storing call attempts
    public static String Mobile_Number; //for calling purpose
    public static String Alternate_Mobile_Number; //for calling purpose if Primary Mobile Number is Invalid/ SwitchOff etc.
    public static String selectedMobileNumber;
  public static int REQUEST_CALL = 1; // can use any integer value

    //To Send to Backend Using Post Method
    public static String send_callDateTime;
    public static Date send_callDateTime_asDate;
    public static String send_callDuration;
    public static String send_callRecording;
    public static byte[] send_callRecordingInByteArray;
    public static String send_callNotes;
    public static int send_callAttemptNo;
    public static String send_reason;
    public  String send_callScheduledTime  ;
    public  String send_DateOfVisitPromised;
    public  String send_FoName;
    public  String send_RelativeName;
    public  String send_RelativeContact;
    public static String visits_FirstName , visits_MobileNumber;
    public static ConstraintLayout constraintLayoutSecondHalf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_details_of_customer);

        initializeFields();
        onClickListener();


        initObserver();
        if (NetworkUtilities.getConnectivityStatus(this)) {
            callDetailsOfCustomerApi();
        } else {
            Global.showToast(this, getString(R.string.check_internet_connection));
        }


    }

    //for Full/Partial Amount Paid / NotSpokeToCustomerActivity- Number is Invalid
    public  List<CallDetails> sendCallLogDetailsList_FullPartialAmountPaid() {
        // String pattern = "dd-MM-yyyy HH:mm:ss"; // Pattern to match the date format
        String pattern = "yyyy-MM-dd HH:mm:ss"; // Pattern to match the date format
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);

        List<CallDetails> callDetailsList = new ArrayList<>(); //List to hold CallDetails Object
        CallDetails callDetails = new CallDetails();    //CallDetails Object

        if (send_callDateTime != null) {

            Date date = new Date();
            String callDateTime = dateFormat.format(date);
            //CallDetails Object
            callDetails.setCallDateTime(callDateTime);

        }
        else if(send_callDateTime == null){
            Date date = new Date();
            String callDateTime = dateFormat.format(date);
            //CallDetails Object
            callDetails.setCallDateTime(callDateTime);
        }

        callDetails.setScheduledCallDateTime(""); // scheduleDateTime will be null for Full/Partial Amount Paid

        if (send_callDuration != null) {
            callDetails.setCallDuration(Integer.parseInt(send_callDuration));
        }

        if (send_callRecording != null) {
            //  callDetails.setCallRecording(send_callRecordingInByteArray);
        }

        if(send_callNotes!=null){
            callDetails.setNotes(send_callNotes);
        }

        if(String.valueOf(send_callAttemptNo)!=null){
            callDetails.setAttemptNo(send_callAttemptNo);
        }

        callDetailsList.add(callDetails);
        return callDetailsList;
    }

//1) Will Pay Later 2) SVFC(Schedule Visit For Customer) Flow 3) Asked To Call Back Later Flow(ATCL) 4)Already Paid
    public  List<CallDetails> sendCallLogDetailsList_WillPayLater() {
       // String pattern = "dd-MM-yyyy HH:mm:ss"; // Pattern to match the date format
        String pattern = "yyyy-MM-dd HH:mm:ss"; // Pattern to match the date format
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);

        List<CallDetails> callDetailsList = new ArrayList<>(); //List to hold CallDetails Object
        CallDetails callDetails = new CallDetails();    //CallDetails Object

        if (send_callDateTime != null) {

                Date date = new Date();
                String callDateTime = dateFormat.format(date);
                   //CallDetails Object
                callDetails.setCallDateTime(callDateTime);
        }
        else if(send_callDateTime == null){
            Date date = new Date();
            String callDateTime = dateFormat.format(date);
            //CallDetails Object
            callDetails.setCallDateTime(callDateTime);
        }


        if(null!= Global.getStringFromSharedPref(this,"scheduleVisitForCollection_dateTime")){
            String scheduleVisitForCollection_dateTime = Global.getStringFromSharedPref(this,"scheduleVisitForCollection_dateTime");
            send_callScheduledTime = scheduleVisitForCollection_dateTime;
        }
        if(send_callScheduledTime!=null ){
            callDetails.setScheduledCallDateTime(send_callScheduledTime); // for Will Pay Later flow
            Global.saveStringInSharedPref(this,"scheduleVisitForCollection_dateTime",""); // make empty to reset
        }

        if (send_callDuration != null) {
            callDetails.setCallDuration(Integer.parseInt(send_callDuration));
        }

        if (send_callRecording != null) {
          //  callDetails.setCallRecording(send_callRecordingInByteArray);
        }

        if(send_callNotes!=null){
            callDetails.setNotes(send_callNotes);
        }

        if(String.valueOf(send_callAttemptNo)!=null){
            callDetails.setAttemptNo(send_callAttemptNo);
        }

        if(send_reason!=null){
            callDetails.setReason(send_reason);
        }

        callDetailsList.add(callDetails);
        return callDetailsList;
    }

    //For 1)Fo Not Visited  2)Loan Taken By Relative
    public  List<CallDetails> sendCallLogDetailsList_FNV_LTBR(String type) {
        // String pattern = "dd-MM-yyyy HH:mm:ss"; // Pattern to match the date format
        String pattern = "yyyy-MM-dd HH:mm:ss"; // Pattern to match the date format
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);

        List<CallDetails> callDetailsList = new ArrayList<>(); //List to hold CallDetails Object
        CallDetails callDetails = new CallDetails();    //CallDetails Object

        if (send_callDateTime != null) {

            Date date = new Date();
            String callDateTime = dateFormat.format(date);
            //CallDetails Object
            callDetails.setCallDateTime(callDateTime);
        }
        else if(send_callDateTime == null){
            Date date = new Date();
            String callDateTime = dateFormat.format(date);
            //CallDetails Object
            callDetails.setCallDateTime(callDateTime);
        }

        // ScheduleVisitForCollectionActivity scheduleVisitForCollectionActivity = new ScheduleVisitForCollectionActivity();
        //send_callScheduledTime = scheduleVisitForCollectionActivity.();
        String scheduleVisitForCollection_dateTime = Global.getStringFromSharedPref(this,"scheduleVisitForCollection_dateTime");
        send_callScheduledTime = scheduleVisitForCollection_dateTime;

        if(send_callScheduledTime!=null ){
           // callDetails.setScheduledCallDateTime(send_callScheduledTime); // for Will Pay Later flow
        }

        if (send_callDuration != null) {
            callDetails.setCallDuration(Integer.parseInt(send_callDuration));
        }

        if (send_callRecording != null) {
            //  callDetails.setCallRecording(send_callRecordingInByteArray);
        }

        if(send_callNotes!=null){
            callDetails.setNotes(send_callNotes);
        }

        if(String.valueOf(send_callAttemptNo)!=null){
            callDetails.setAttemptNo(send_callAttemptNo);
        }

        //FO not visited
        if(type.contains("FNV")){
            send_DateOfVisitPromised = Global.getStringFromSharedPref(this,"dateOfVisitPromised");
            if(send_DateOfVisitPromised!=null){
                callDetails.setDateOfVisitPromised(send_DateOfVisitPromised);
            }


            send_FoName = Global.getStringFromSharedPref(this,"foName");
            if(send_FoName!=null){
                callDetails.setFoName(send_FoName);
            }
        }
         else {
            callDetails.setDateOfVisitPromised("");
            callDetails.setFoName("");
        }


        //Loan Taken By Relative
        if(type.contains("LTBR")){

            send_RelativeName = Global.getStringFromSharedPref(this,"relativeName");
            if(send_RelativeName!=null){
                callDetails.setRelativeName(send_RelativeName);
            }


            send_RelativeContact = Global.getStringFromSharedPref(this,"relativeContact");
            if(send_RelativeContact!=null){
                callDetails.setRelativeContact(send_RelativeContact);
            }
        }
        else{
            callDetails.setRelativeName("");
            callDetails.setRelativeContact("");
        }



        callDetailsList.add(callDetails);
        return callDetailsList;
    }


    private void callDetailsOfCustomerApi() {

        String dataSetId = getIntent().getStringExtra("dataSetId");
        detailsOfCustomerViewModel.getDetailsOfCustomer_Data(dataSetId); // call Details Of Customer API
    }

    private void initObserver() {

        if (NetworkUtilities.getConnectivityStatus(this)) {

            binding.loadingProgressBar.setVisibility(View.VISIBLE);

            detailsOfCustomerViewModel.getMutDetailsOfCustomer_ResponseApi().observe(this, result -> {

                if (result != null) {

                    //for Hiding Amount Paid ONLY in Details Of Customer Activity
                    result.iterator().forEachRemaining(it -> {
                        if (it.getLable().contentEquals("Amount Paid")) {
                            it.setLable("");
                            it.setEditable("");
                        }
                    });

                    //Get Name for Storing Call Attempts , Name is at index = 0 in result(DetailsOfCustomerResponseModel)
                   if(result.get(0).getLable().toLowerCase().contains("name")){
                       System.out.println("DetailsOFCustomerFullName:"+result.get(0).getValue().toString());
                       DetailsOfCustomerActivity.FullName = result.get(0).getValue().toString();

                   }

                    //Get Name , Mobile Number , Schedule Time  for Calling Purpose and Storing Call Attempts
                    result.forEach(it -> {
                        String lowercase_label = String.valueOf(it.getLable()).toLowerCase(); //make labels lowercase

                        if (lowercase_label.contains("name")) {
                          //  FullName = String.valueOf(it.getValue()); //store name
                          //  System.out.println("DetailsOfCustomerActivity.FullName"+DetailsOfCustomerActivity.FullName);
                            Global.saveStringInSharedPref(this,"FullName",FullName); //save in SharedPreference fro checking Call Attempt No.
                        }

                        if (lowercase_label.contains("mobile") || lowercase_label.contains("phone")) {

                               if(null!=it.getValue()){
                                   Mobile_Number = String.valueOf(it.getValue()); //store mobile_no
                                   System.out.println("Here Mobile Number: "+Mobile_Number);
                               }

                        }

                        //for Alternate Mobile Number either coming from API OR DetailsOFCustomerAdapter
                        if(lowercase_label.contains("alternate")){

                            if(null!= it.getValue()){
                                Alternate_Mobile_Number = String.valueOf(it.getValue());
                                System.out.println("Here Alternate Number: "+it.getValue());
                            }

                        }


                    });

                    detailsOfCustomerViewModel.arrList_DetailsOfCustomer_Data.clear();
                    setUpDetailsOfCustomerRecyclerView();
                    detailsOfCustomerViewModel.arrList_DetailsOfCustomer_Data.addAll(result);
                    binding.loadingProgressBar.setVisibility(View.INVISIBLE);


                }
            });

            //handle  error response
            detailsOfCustomerViewModel.getMutErrorResponse().observe(this, error -> {

                if (error != null && !error.isEmpty()) {
                  //  Global.showSnackBar(view, "DetailsOfCustomerActivity Exception: "+error);
                    System.out.println("Here DetailsOfCustomerActivity Exception: " + error);

                } else {
                    Global.showSnackBar(view, getResources().getString(R.string.check_internet_connection));
                }
            });
        } else {
            Global.showToast(this, getString(R.string.check_internet_connection));
        }

    }



    private void setToolBarTitle(){
        if(getIntent().hasExtra("isFromCallsForTheDayAdapter")){
            binding.txtToolbarHeading.setText(R.string.calls_for_the_day_npa);
        }
    }

    public void initializeFields() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_details_of_customer2);
        view = binding.getRoot();
        detailsOfCustomerViewModel = new ViewModelProvider(this).get(DetailsOfCustomerViewModel.class);
        binding.setViewModel(detailsOfCustomerViewModel);

        Global.removeStringInSharedPref(this, "Amount_Paid"); // remove Amount Paid from SharePreferences for next activities to have New value
          setToolBarTitle();

        constraintLayoutSecondHalf = binding.clSecondHalf;

        //Initial values will be empty ,
        // after calling callDetailsOfCustomerApi() , values of MobileNumber & Alternate_Mobile_Number will be set
        Mobile_Number = "";
        Alternate_Mobile_Number = "";
    }

    private void setUpDetailsOfCustomerRecyclerView() {

        detailsOfCustomerViewModel.updateDetailsOfCustomer_Data();
        RecyclerView recyclerView = binding.rvDetailsOfCustomer;
        recyclerView.setAdapter(new DetailsOfCustomerAdapter(this,detailsOfCustomerViewModel.arrList_DetailsOfCustomer_Data));
    }


    private void onClickListener() {

        //Call Save Alternate Number API
        if(null!= DetailsOfCustomerAdapter.alternateNumber && null!=DetailsOfCustomerAdapter.dataSetId){
            System.out.println("Save Alternate No. Api dataSetId:"+DetailsOfCustomerAdapter.dataSetId+" Alternate No. :"+DetailsOfCustomerAdapter.alternateNumber);
            AlternateNumberApiCall.saveAlternateNumber(this,DetailsOfCustomerAdapter.alternateNumber,DetailsOfCustomerAdapter.dataSetId);
        }

        binding.ivBack.setOnClickListener(v -> onBackPressed());

        binding.ivHome.setOnClickListener(v -> {

            //Call Save Alternate Number API
            if(null!= DetailsOfCustomerAdapter.alternateNumber && null!=DetailsOfCustomerAdapter.dataSetId){
                System.out.println("Save Alternate No. Api dataSetId:"+DetailsOfCustomerAdapter.dataSetId+" Alternate No. :"+DetailsOfCustomerAdapter.alternateNumber);
                AlternateNumberApiCall.saveAlternateNumber(this,DetailsOfCustomerAdapter.alternateNumber,DetailsOfCustomerAdapter.dataSetId);
            }

            startActivity(new Intent(this, MainActivity3API.class));
        });

        binding.ivCall.setOnClickListener(v -> {

            //make an actual call
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED

            ) {
                // Permission is not granted, request the permission
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.CALL_PHONE,
                        Manifest.permission.READ_CALL_LOG,
                        Manifest.permission.RECORD_AUDIO}, REQUEST_CALL);
            } else {

                //Call Save Alternate Number API
                if(null!= DetailsOfCustomerAdapter.alternateNumber && null!=DetailsOfCustomerAdapter.dataSetId){
                    System.out.println("Save Alternate No. Api dataSetId:"+DetailsOfCustomerAdapter.dataSetId+" Alternate No. :"+DetailsOfCustomerAdapter.alternateNumber);
                    AlternateNumberApiCall.saveAlternateNumber(this,DetailsOfCustomerAdapter.alternateNumber,DetailsOfCustomerAdapter.dataSetId);
                }

                if(noMobileNumberExists()){
                    System.out.println("No Mobile Number exists");
                }

              else  if(onlyOneMobileNumberExists()){
                    System.out.println("Selected Mobile Number:"+selectedMobileNumber);
                }
                else{
                    selectedMobileNumberDialog();
                }


               /* // Permission has already been granted, make the call
               // String phoneNumber = Mobile_Number; //use mobile number fetched from result(API Response)
               //  Intent dial = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
                Intent dial = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
                startActivity(dial);
                System.out.println("Here Mobile Number: "+phoneNumber);

                //Store Call Count in RoomDB
              //  storeCallCountInRoomDB(FullName, Mobile_Number);


                try {

                   getCallRecordingAndCallLogs();

                   //   getCallRecordingAndCallLogs();
                } catch (Exception e) {
                    Global.showSnackBar(view, "Call Error" + e);
                    System.out.println("Here Call Error:" + e);
                }


                //Initiate Call button visible after call made and ivCallLogo invisible
                binding.ivCall.setVisibility(View.INVISIBLE);
                binding.btnCallInitiated.setVisibility(View.VISIBLE);*/



            }

        });

        binding.btnCallInitiated.setOnClickListener(v1->{

            //From CallsForTheDayAdapter
            if(CallsForTheDayAdapter.isFromCallsForTheDayAdapter!=null){
                System.out.println("Here isFromCallsForTheDayAdapter_DetailsOfCustomerActivity");
                Intent i = new Intent(this, CallDetailOfCustomerActivity.class);
                i.putExtra("dataSetId", getIntent().getStringExtra("dataSetId"));
                i.putExtra("isFromCallsForTheDayAdapter","isFromCallsForTheDayAdapter");
                startActivity(i);
            }

            else{
                // From NPA (Assigned)
                // While Call is going , Move the User to Next Activity
                System.out.println("Here fromNPA_DetailsOfCustomerActivity");
                Intent i = new Intent(this, CallDetailOfCustomerActivity.class);
                i.putExtra("dataSetId", getIntent().getStringExtra("dataSetId"));
                startActivity(i);
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


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if(noMobileNumberExists()){
                    System.out.println("No Mobile Number exists");
                }

                else  if(onlyOneMobileNumberExists()){
                    System.out.println("Selected Mobile Number:"+selectedMobileNumber);
                }
                else{
                    selectedMobileNumberDialog();
                }


               /* // Permission is granted, make the call
                String phoneNumber = Mobile_Number; //use mobile number fetched from result(API Response)
               // Intent dial = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
                Intent dial = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
                startActivity(dial);

                try {


                    getCallRecordingAndCallLogs();

                    //Store Call Count in RoomDB
                  //  storeCallCountInRoomDB(FullName, Mobile_Number);

                } catch (Exception e) {
                    System.out.println("Here Error:" + e);
                    Global.showSnackBar(view, "Call Error" + e);
                }



                //Initiate Call button visible after call made and ivCallLogo invisible
                binding.ivCall.setVisibility(View.INVISIBLE);
                binding.btnCallInitiated.setVisibility(View.VISIBLE);*/


            } else {
                // Permission is denied, show a message
                Global.showSnackBar(view, getResources().getString(R.string.permission_to_call_denied));
            }
        }

        //coming from DetailsOfCustomerAdapter Navigation/Capture Button Click
        if (requestCode == Global.REQUEST_BACKGROUND_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Background location access permission granted
               navigateToGoogleMapsForNavigation();
            } else {
                // Background location access permission denied
                Global.isBackgroundLocationAccessEnabled(this); // request BackGroundLocation Again
            }
        }
    }

    public void navigateToGoogleMapsForNavigation(){

        try{
            //  Navigate To Google Maps App for Direction (coming from Either NPA OR VisitsForTheDay)
            double userLatitude =  Global.getDeviceLocation(this).getLatitude();
            double userLongitude =  Global.getDeviceLocation(this).getLongitude();
            double latitudeFromLoanCollectionResponse = Double.parseDouble(Global.getStringFromSharedPref(this,"latitudeFromLoanCollectionAdapter"));
            double longitudeFromLoanCollectionResponse = Double.parseDouble(Global.getStringFromSharedPref(this,"longitudeFromLoanCollectionAdapter"));


            String uri = "https://www.google.com/maps/dir/?api=1&origin=" +
                    userLatitude + "," + userLongitude +
                    "&destination=" + latitudeFromLoanCollectionResponse + "," + longitudeFromLoanCollectionResponse;



            // Create an intent with the Google Maps URI
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));

            // Set the package to explicitly open the Google Maps app
            intent.setPackage("com.google.android.apps.maps");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //used when launching an activity from a context that is not an activity

            //if GoogleMaps installed
            if(Global.isGoogleMapsInstalled(this)){
                System.out.println("isGoogleMaps installed: true");
               startActivity(intent);
            }
            //if GoogleMaps not installed
            else{
                Global.showToast(this,String.valueOf(R.string.kindly_install_google_maps));
            }

        }
        catch (Exception e)
        {
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

        Global.showToast(this, "Call Count for " + phoneNumber + " is: " + leadCallDao.getCallCountUsingPhoneNumber(phoneNumber));
        System.out.println("Here Call Count for " + phoneNumber + " is: " + leadCallDao.getCallCountUsingPhoneNumber(phoneNumber));

        send_callAttemptNo = callCount;
    }


    public void getCallRecordingAndCallLogs() throws IOException {

        //for Call Recoding in Internal Storage (here Filename is call_recording.mp3) (Convert to Byte Array and Send to Server)
        String filePath = getFilesDir().getAbsolutePath() + "/call_recording.mp3";

        //fro Call Recording in External Storage
        //  String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/call_recording.mp3";

        //to get Call Recording
    final  MediaRecorder recorder = new MediaRecorder();
       // recorder.setAudioSource(MediaRecorder.AudioSource.VOICE_CALL);
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        recorder.setOutputFile(filePath);

        recorder.prepare();
        recorder.start();


        PhoneStateListener phoneStateListener = new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                super.onCallStateChanged(state, incomingNumber);
                switch (state) {
                    case TelephonyManager.CALL_STATE_IDLE:
                        // The call has ended
                        getCallLogs();
                     //   retrieveCallLog();
                        // Stop recording after the call has ended
                        try {
                       // recorder.stop(); //getting Exception for stop (Media Recorder not able to stop so using pause())
                            recorder.pause();
                        recorder.reset();
                        //recorder.release();

                            byte[] bytes_array = convertFileToByteArray(filePath); // convert Audio to Byte Array and Send to Server
                           System.out.println("Here byte_array:" + Arrays.toString(bytes_array)); ;
                            send_callRecording = Arrays.toString(bytes_array);
                            send_callRecordingInByteArray = bytes_array;

                           Log.d("Here byte_array to String:", bytes_array.toString()) ;
                        } catch (Exception e) {
                            if(e.getLocalizedMessage()!=null){
                                Log.d("Here Call Recording Exception:", e.getLocalizedMessage());
                            }

                            e.printStackTrace();
                        }
                        finally {
                            // Release the MediaRecorder
                            recorder.release();


                            // Get UserName , UserID , BranchCode

                            MPinDao mPinDao = LeadListDB.getInstance(DetailsOfCustomerActivity.this).mPinDao();
                            UserNameDao userNameDao = LeadListDB.getInstance(DetailsOfCustomerActivity.this).userNameDao();
                            String userName = userNameDao.getUserNameUsingUserIDInUserNameRoomDB(mPinDao.getUserID());
                            // Store UserName in SharedPreference and Use in StatusOfCustomerDetailsAdapter
                            Global.saveStringInSharedPref(DetailsOfCustomerActivity.this,"userName",userName);

                            MainActivity3API.UserID = mPinDao.getUserID();
                            MainActivity3API.BranchCode = mPinDao.getBranchCode();

                            System.out.println("Here DetailsOfCustomerActivity AfterCall UserID:"+MainActivity3API.UserID);
                            System.out.println("Here DetailsOfCustomerActivity AfterCall BranchCode:"+MainActivity3API.BranchCode);


                             //Initiate Call button visible after call made and ivCallLogo invisible
                           binding.ivCall.setVisibility(View.INVISIBLE);
                           binding.btnCallInitiated.setVisibility(View.VISIBLE);
                           binding.btnCallInitiated.performClick();

                        }

                        break;
                }
            }
        };

        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);

    }

    private void getCallLogs() {

        ContentResolver cr = getBaseContext().getContentResolver();
        Cursor c = cr.query(CallLog.Calls.CONTENT_URI, null, null, null, null);

        int totalCall;
        String phNumber, callDate, callDuration, callDayTimes;
        Date dateFormat;

        if (c != null) {
            totalCall = 1; // integer call log limit

            if (c.moveToLast()) { //starts pulling logs from last - you can use moveToFirst() for first logs , moveToLast() for last logs
                for (int j = 0; j < totalCall; j++) {


                    // phNumber = c.getString(c.getColumnIndexOrThrow(CallLog.Calls.NUMBER));
                    //phNumber = getIntent().getStringExtra("phoneNumber"); // for getting current phoneNumber

                    //phNumber = Mobile_Number; //Customer/Member Mobile Number
                    phNumber = selectedMobileNumber;
                    callDate = c.getString(c.getColumnIndexOrThrow(CallLog.Calls.DATE));
                    callDuration = c.getString(c.getColumnIndexOrThrow(CallLog.Calls.DURATION));
                    dateFormat = new Date(Long.valueOf(callDate));
                    callDayTimes = String.valueOf(dateFormat);

                    String direction = null;
                    switch (Integer.parseInt(c.getString(c.getColumnIndexOrThrow(CallLog.Calls.TYPE)))) {
                        case CallLog.Calls.OUTGOING_TYPE:
                            direction = "OUTGOING";
                            break;
                        case CallLog.Calls.INCOMING_TYPE:
                            direction = "INCOMING";
                            break;
                        case CallLog.Calls.MISSED_TYPE:
                            direction = "MISSED";
                            break;
                        default:
                            break;
                    }

                    c.moveToPrevious(); // if you used moveToFirst() for first logs, you should this line to moveToNext


                  //  Toast.makeText(getBaseContext(), phNumber + callDuration + callDayTimes + direction, Toast.LENGTH_LONG).show(); // you can use strings in this line
                    System.out.println("CallLog: Phone Number" + phNumber + "\nDuration" + callDuration + "\nDate_n_Time" + callDayTimes + "\nType" + direction);

               // To send to backend
                    send_callDateTime = callDayTimes;
                    send_callDuration = callDuration;
                    send_callDateTime_asDate = dateFormat;

                }


            }
            c.close();


        }
    }





    // for Converting Audio(Call Recording(.mp3 format) to Byte Array)
    public byte[] convertFileToByteArray(String path) throws IOException {

        FileInputStream fis = new FileInputStream(path);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] b = new byte[1024];

        for (int readNum; (readNum = fis.read(b)) != -1; ) {
            bos.write(b, 0, readNum);
        }

        byte[] bytesArray = bos.toByteArray();

        return bytesArray;
    }

    //if Primary Number & Alternate Number both does not exists
    private boolean noMobileNumberExists(){

        if(null==DetailsOfCustomerActivity.Mobile_Number || DetailsOfCustomerActivity.Mobile_Number.isEmpty()
                && (null==Alternate_Mobile_Number || Alternate_Mobile_Number.isEmpty() )  )
        {
            binding.ivCall.setVisibility(View.INVISIBLE);
            binding.btnCallInitiated.setVisibility(View.VISIBLE);
            binding.btnCallInitiated.performClick();

            return  true;
        }

        return false;
    }


    //if Only One Mobile Number exists ( Primary Mobile Number OR  Alternate Mobile number)
    private boolean onlyOneMobileNumberExists(){

        //if only Primary Mobile Number exists
        if(null!=DetailsOfCustomerActivity.Mobile_Number && !DetailsOfCustomerActivity.Mobile_Number.isEmpty()
                && (null==Alternate_Mobile_Number || Alternate_Mobile_Number.isEmpty() )  )
        {
            selectedMobileNumber = DetailsOfCustomerActivity.Mobile_Number;

            String phoneNumber = selectedMobileNumber;
            //  Intent dial = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
            Intent dial = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
            startActivity(dial);
            System.out.println("Here Mobile Number: "+phoneNumber);

            try {
                getCallRecordingAndCallLogs();

            } catch (Exception e) {
                Global.showSnackBar(view, "Call Error" + e);
                System.out.println("Here Call Error:" + e);
            }

            return  true;
        }

        // if only Alternate Mobile Number exists
      else  if( null!=Alternate_Mobile_Number && !Alternate_Mobile_Number.isEmpty()
            && (null==DetailsOfCustomerActivity.Mobile_Number || DetailsOfCustomerActivity.Mobile_Number.isEmpty())

        ){
             selectedMobileNumber = Alternate_Mobile_Number;

            String phoneNumber = selectedMobileNumber;
            //  Intent dial = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
            Intent dial = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
            startActivity(dial);
            System.out.println("Here Mobile Number: "+phoneNumber);

            try {
                getCallRecordingAndCallLogs();

            } catch (Exception e) {
                Global.showSnackBar(view, "Call Error" + e);
                System.out.println("Here Call Error:" + e);
            }

            return  true;
        }

        return false;
    }


    // To select Between Primary Mobile Number & Alternate Mobile number
    private void selectedMobileNumberDialog(){

       selectedMobileNumber = DetailsOfCustomerActivity.Mobile_Number; //By default

        View customDialogMobileNumber = LayoutInflater.from(this).inflate(R.layout.custom_dialog_mobile_numbers, null);
        ImageView ivCancel = customDialogMobileNumber.findViewById(R.id.ivCancel);
        Button btnProceed = customDialogMobileNumber.findViewById(R.id.btnProceed);
        RadioButton radioButton1 = customDialogMobileNumber.findViewById(R.id.radioButton1);
        RadioButton radioButton2 = customDialogMobileNumber.findViewById(R.id.radioButton2);
        TextView txtRadioButton1 = customDialogMobileNumber.findViewById(R.id.txtRadioButton1);
        TextView txtRadioButton2 = customDialogMobileNumber.findViewById(R.id.txtRadioButton2);


        if(null!=DetailsOfCustomerActivity.Mobile_Number && !DetailsOfCustomerActivity.Mobile_Number.isEmpty()){
            radioButton1.setVisibility(View.VISIBLE);
            radioButton1.setChecked(true); //By default Primary Mobile Number coming from Api response will be selected
            txtRadioButton1.setVisibility(View.VISIBLE);
            txtRadioButton1.setText(DetailsOfCustomerActivity.Mobile_Number);

        }

        if(null!=Alternate_Mobile_Number && !Alternate_Mobile_Number.isEmpty()){
            radioButton2.setVisibility(View.VISIBLE);
            txtRadioButton2.setVisibility(View.VISIBLE);
            txtRadioButton2.setText(Alternate_Mobile_Number);
        }


        //for RadioButton 1
        radioButton1.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if(isChecked){
                radioButton2.setChecked(false);
            }
        });

        //for RadioButton 2
        radioButton2.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if(isChecked){
                radioButton1.setChecked(false);
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(customDialogMobileNumber);
        final AlertDialog dialog = builder.create();
        dialog.show();



        btnProceed.setOnClickListener(v->{

            if(radioButton1.isChecked()){
                 selectedMobileNumber= DetailsOfCustomerActivity.Mobile_Number;
                 System.out.println("Selected Mobile Number Primary:"+DetailsOfCustomerActivity.Mobile_Number);

                String phoneNumber = selectedMobileNumber;
                //  Intent dial = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
                Intent dial = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
                startActivity(dial);
                System.out.println("Here Mobile Number: "+phoneNumber);


                try {
                    getCallRecordingAndCallLogs();

                } catch (Exception e) {
                    Global.showSnackBar(view, "Call Error" + e);
                    System.out.println("Here Call Error:" + e);
                }

            }

            else if(radioButton2.isChecked()){
                selectedMobileNumber = Alternate_Mobile_Number ;
                System.out.println("Selected Alternate Number Primary:"+Alternate_Mobile_Number);

                String phoneNumber = selectedMobileNumber;
                //  Intent dial = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
                Intent dial = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
                startActivity(dial);
                System.out.println("Here Mobile Number: "+phoneNumber);


                try {
                    getCallRecordingAndCallLogs();

                } catch (Exception e) {
                    Global.showSnackBar(view, "Call Error" + e);
                    System.out.println("Here Call Error:" + e);
                }

            }

           dialog.dismiss(); // to disappear if DetailsOfCustomerActivity's onResume() is called

        });

        ivCancel.setOnClickListener(v->{
            dialog.dismiss();
        });


    }


    @Override
    public void onBackPressed() {

        binding.ivCall.setVisibility(View.VISIBLE);
        binding.btnCallInitiated.setVisibility(View.INVISIBLE);

        //Call Save Alternate Number API
        if(null!= DetailsOfCustomerAdapter.alternateNumber && null!=DetailsOfCustomerAdapter.dataSetId){
            System.out.println("Save Alternate No. Api dataSetId:"+DetailsOfCustomerAdapter.dataSetId+" Alternate No. :"+DetailsOfCustomerAdapter.alternateNumber);
            AlternateNumberApiCall.saveAlternateNumber(this,DetailsOfCustomerAdapter.alternateNumber,DetailsOfCustomerAdapter.dataSetId);
        }


        super.onBackPressed();
    }

    @Override
    protected void onResume() {



       // initializeFields();
        Global.removeStringInSharedPref(this, "Amount_Paid"); // remove Amount Paid from SharePreferences for next activities to have New value
        setToolBarTitle();


        onClickListener();
        initObserver();
        callDetailsOfCustomerApi();

        //if User Was on GoogleMaps App & resumes DetailsOfCustomerActivity
        Global.getStringFromSharedPref(this,"formattedDistanceInKm");
        Global.getStringFromSharedPref(this,"latitudeFromLoanCollectionAdapter");
        Global.getStringFromSharedPref(this,"longitudeFromLoanCollectionAdapter");

        // Get UserName , UserID , BranchCode

        MPinDao mPinDao = LeadListDB.getInstance(this).mPinDao();
        UserNameDao userNameDao = LeadListDB.getInstance(this).userNameDao();

        String userName = userNameDao.getUserNameUsingUserIDInUserNameRoomDB(mPinDao.getUserID());

        // Store UserName in SharedPreference and Use in StatusOfCustomerDetailsAdapter
        Global.saveStringInSharedPref(this,"userName",userName);

        MainActivity3API.UserID = mPinDao.getUserID();
        MainActivity3API.BranchCode = mPinDao.getBranchCode();

        System.out.println("Here DetailsOfCustomerActivity onResume() UserID:"+MainActivity3API.UserID);
        System.out.println("Here DetailsOfCustomerActivity onResume() BranchCode:"+MainActivity3API.BranchCode);


        super.onResume();
    }

    public ActivityDetailsOfCustomer2Binding getBinding(){
        return binding;
    }

}