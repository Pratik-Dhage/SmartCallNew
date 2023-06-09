package com.example.test.npa_flow.details_of_customer;

import static com.example.test.npa_flow.ScheduleVisitForCollectionActivity.scheduleVisitForCollection_dateTime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.test.R;
import com.example.test.broadcast_receiver.MyBroadCastReceiverClass;
import com.example.test.call_status.CallStatusActivity;
import com.example.test.databinding.ActivityDetailsOfCustomer2Binding;
import com.example.test.databinding.ActivityDetailsOfCustomerBinding;
import com.example.test.fragments_activity.BalanceInterestCalculationActivity;
import com.example.test.fragments_activity.CustomerDetailsActivity;
import com.example.test.helper_classes.Global;
import com.example.test.helper_classes.NetworkUtilities;
import com.example.test.main_dashboard.MainActivity3API;
import com.example.test.npa_flow.CallDetailOfCustomerActivity;
import com.example.test.npa_flow.NearByCustomersActivity;
import com.example.test.npa_flow.ScheduleVisitForCollectionActivity;
import com.example.test.npa_flow.SubmitCompletionActivityOfCustomer;
import com.example.test.npa_flow.WebViewActivity;
import com.example.test.npa_flow.call_details.CallDetails;
import com.example.test.npa_flow.call_details.CallDetailsViewModel;
import com.example.test.npa_flow.details_of_customer.DetailsOfCustomerViewModel;
import com.example.test.npa_flow.details_of_customer.adapter.DetailsOfCustomerAdapter;
import com.example.test.npa_flow.dpd.adapter.DPD_Adapter;
import com.example.test.roomDB.dao.LeadCallDao;
import com.example.test.roomDB.database.LeadListDB;
import com.example.test.roomDB.model.LeadCallModelRoom;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DetailsOfCustomerActivity extends AppCompatActivity {

    ActivityDetailsOfCustomer2Binding binding;
    View view;
    DetailsOfCustomerViewModel detailsOfCustomerViewModel;
    public static String FullName; //for storing call attempts
    public static String Mobile_Number; //for calling purpose
  public static int REQUEST_CALL = 1; // can use any integer value

    //To Send to Backend Using Post Method
    public static String send_callDateTime;
    public static Date send_callDateTime_asDate;
    public static String send_callDuration;
    public static String send_callRecording;
    public static byte[] send_callRecordingInByteArray;
    public static String send_callNotes;
    public static int send_callAttemptNo;
    public  String send_callScheduledTime  ;
    public  String send_DateOfVisitPromised;
    public  String send_FoName;
    public  String send_RelativeName;
    public  String send_RelativeContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_details_of_customer);

        initializeFields();
        onClickListener();
        //  getDetailsOfCustomerDataFromApi(); // will act as initObserver

        initObserver();
        if (NetworkUtilities.getConnectivityStatus(this)) {
            callDetailsOfCustomerApi();
        } else {
            Global.showToast(this, getString(R.string.check_internet_connection));
        }

       // sendCallLogDetailsList();
    }

    //for Full/Partial Amount Paid
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

       // ScheduleVisitForCollectionActivity scheduleVisitForCollectionActivity = new ScheduleVisitForCollectionActivity();
        //send_callScheduledTime = scheduleVisitForCollectionActivity.();
        String scheduleVisitForCollection_dateTime = Global.getStringFromSharedPref(this,"scheduleVisitForCollection_dateTime");
        send_callScheduledTime = scheduleVisitForCollection_dateTime;

        if(send_callScheduledTime!=null ){
            callDetails.setScheduledCallDateTime(send_callScheduledTime); // for Will Pay Later flow
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

                    //Get Name , Mobile Number , Schedule Time  for Calling Purpose and Storing Call Attempts
                    result.iterator().forEachRemaining(it -> {
                        String lowercase_label = String.valueOf(it.getLable()).toLowerCase(); //make labels lowercase

                        if (lowercase_label.contains("name")) {
                            FullName = String.valueOf(it.getValue()); //store name
                            Global.saveStringInSharedPref(this,"FullName",FullName); //save in SharedPreference fro checking Call Attempt No.
                        }

                        if (lowercase_label.contains("mobile") || lowercase_label.contains("phone")) {

                            //test purpose as Mobile number from back end is Long/Double
                            DecimalFormat decimalFormat = new DecimalFormat("#");
                            String mobile_number = decimalFormat.format(it.getValue());
                            Mobile_Number = String.valueOf(mobile_number); //store mobile_no
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
                    Global.showSnackBar(view, error);
                    System.out.println("Here: " + error);
                } else {
                    Global.showSnackBar(view, getResources().getString(R.string.check_internet_connection));
                }
            });
        } else {
            Global.showToast(this, getString(R.string.check_internet_connection));
        }

    }

    private void getDetailsOfCustomerDataFromApi() {
        if (getIntent().hasExtra("dataSetId")) {

            String dataSetId = getIntent().getStringExtra("dataSetId");

            if (NetworkUtilities.getConnectivityStatus(this)) {

                binding.loadingProgressBar.setVisibility(View.VISIBLE);
                detailsOfCustomerViewModel.getDetailsOfCustomer_Data(dataSetId); // call Details Of Customer API

                detailsOfCustomerViewModel.getMutDetailsOfCustomer_ResponseApi().observe(this, result -> {

                    if (result != null) {

                        binding.loadingProgressBar.setVisibility(View.INVISIBLE);

                      /*  result.iterator().forEachRemaining(it->{

                            if(it.getSequence()==1){
                                binding.txtName.setText(it.getValue()); //Name
                            }

                            if(it.getSequence()==2){
                                binding.txtVillageName.setText(it.getValue()); //Village Name
                            }


                            if(it.getSequence()==4){
                                binding.txtMobileNumber.setText(it.getValue()); //Mobile No.
                            }

                            if(it.getSequence()==5){
                                binding.txtAadharNumber.setText(it.getValue()); //Aadhaar No.
                            }

                            if(it.getSequence()==6){
                                binding.txtDOB.setText(it.getValue()); //Date of Birth
                            }

                            if(it.getSequence()==7){
                                binding.txtFatherName.setText(it.getValue()); //Father's Name
                            }

                            if(it.getSequence()==8){
                                binding.txtLoanAccountNumber.setText(it.getValue()); //Loan Acc. No.
                            }

                            if(it.getSequence()==9){
                                binding.txtProduct.setText(it.getValue()); //Product
                            }

                            if(it.getSequence()==10){
                                binding.txtAmountDueAsOnAmount.setText(it.getValue()); //Amt. Due as OutStanding Balance
                            }

                            if(it.getSequence()==12){
                                binding.txtTotalAmountPaid.setText(it.getValue()); // Total Amount Paid
                            }

                            if(it.getSequence()==13){
                                binding.txtBalanceInterest.setText(it.getValue()); //Balance Interest
                            }

                            if(it.getSequence()==14){
                                binding.txtTotalPayableAmount.setText(it.getValue()); //Total Payable Amount
                            }

                        });*/

                    }


                });


                //handle  error response
                detailsOfCustomerViewModel.getMutErrorResponse().observe(this, error -> {

                    if (error != null && !error.isEmpty()) {
                        Global.showSnackBar(view, error);
                        System.out.println("Here: " + error);
                    } else {
                        Global.showSnackBar(view, getResources().getString(R.string.check_internet_connection));
                    }
                });

            } else {
                Global.showToast(this, getString(R.string.check_internet_connection));
            }

        } else {
            Global.showToast(this, getString(R.string.details_not_found));
        }
    }

    private void setToolBarTitle(){
        if(getIntent().hasExtra("isFromCallsForTheDayAdapter")){
            binding.txtToolbarHeading.setText(R.string.calls_for_the_day_npa);
        }
    }

    private void initializeFields() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_details_of_customer2);
        view = binding.getRoot();
        detailsOfCustomerViewModel = new ViewModelProvider(this).get(DetailsOfCustomerViewModel.class);
        binding.setViewModel(detailsOfCustomerViewModel);

        Global.removeStringInSharedPref(this, "Amount_Paid"); // remove Amount Paid from SharePreferences for next activities to have New value
          setToolBarTitle();
    }

    private void setUpDetailsOfCustomerRecyclerView() {

        detailsOfCustomerViewModel.updateDetailsOfCustomer_Data();
        RecyclerView recyclerView = binding.rvDetailsOfCustomer;
        recyclerView.setAdapter(new DetailsOfCustomerAdapter(detailsOfCustomerViewModel.arrList_DetailsOfCustomer_Data));
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
                // Permission has already been granted, make the call
                String phoneNumber = Mobile_Number; //use mobile number fetched from result(API Response)
                Intent dial = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
              //  Intent dial = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
                startActivity(dial);

                //Store Call Count in RoomDB
                storeCallCountInRoomDB(FullName, Mobile_Number);

                try {

                   /* // Register the broadcast receiver in your activity or service
                    MyBroadCastReceiverClass receiver = new MyBroadCastReceiverClass();
                    IntentFilter filter = new IntentFilter(Intent.ACTION_NEW_OUTGOING_CALL);
                    registerReceiver(receiver, filter);*/

                   getCallRecordingAndCallLogs();

                   //   getCallRecordingAndCallLogs();
                } catch (Exception e) {
                    Global.showSnackBar(view, "Call Error" + e);
                    System.out.println("Here Call Error:" + e);
                }

                //From CallsForTheDayAdapter
                if(getIntent().hasExtra("isFromCallsForTheDayAdapter")){
                    Intent i = new Intent(this, CallDetailOfCustomerActivity.class);
                    i.putExtra("dataSetId", getIntent().getStringExtra("dataSetId"));
                    startActivity(i);
                }

                // From NPA (Assigned)
                // While Call is going , Move the User to Next Activity
                Intent i = new Intent(this, CallDetailOfCustomerActivity.class);
                i.putExtra("dataSetId", getIntent().getStringExtra("dataSetId"));
                startActivity(i);

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

          send_callNotes = customEditBox.getText().toString();

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


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted, make the call
                String phoneNumber = Mobile_Number; //use mobile number fetched from result(API Response)
                Intent dial = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
                startActivity(dial);

                try {

                    // Register the broadcast receiver in your activity or service
                    /*MyBroadCastReceiverClass receiver = new MyBroadCastReceiverClass();
                    IntentFilter filter = new IntentFilter(Intent.ACTION_NEW_OUTGOING_CALL);
                    registerReceiver(receiver, filter);
*/
                    getCallRecordingAndCallLogs();

                    //Store Call Count in RoomDB
                    storeCallCountInRoomDB(FullName, Mobile_Number);

                } catch (Exception e) {
                    System.out.println("Here Error:" + e);
                    Global.showSnackBar(view, "Call Error" + e);
                }


                // While Call is going , Move the User to Next Activity
                Intent i = new Intent(this, CallDetailOfCustomerActivity.class);
                i.putExtra("dataSetId", getIntent().getStringExtra("dataSetId"));
                startActivity(i);

            } else {
                // Permission is denied, show a message
                Global.showSnackBar(view, getResources().getString(R.string.permission_to_call_denied));
            }
        }
    }


    private void storeCallCountInRoomDB(String firstName, String phoneNumber) {

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
                            Log.d("Here Call Recording Exception:", e.getLocalizedMessage());
                            e.printStackTrace();
                        }
                        finally {
                            // Release the MediaRecorder
                            recorder.release();

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
                    phNumber = Mobile_Number; //Customer/Member Mobile Number
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


                    Toast.makeText(getBaseContext(), phNumber + callDuration + callDayTimes + direction, Toast.LENGTH_LONG).show(); // you can use strings in this line
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


    // For Getting Calculated Balance Interest Result back from SharedPreference
    @Override
    protected void onResume() {
        initializeFields();
        onClickListener();
        initObserver();
        callDetailsOfCustomerApi();
        super.onResume();
    }


}