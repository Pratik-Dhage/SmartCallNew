package com.example.test.fragment_visits_flow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Context;
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
import android.widget.Toast;

import com.example.test.R;
import com.example.test.helper_classes.Global;
import com.example.test.npa_flow.call_details.CallDetails;
import com.example.test.npa_flow.details_of_customer.adapter.DetailsOfCustomerAdapter;
import com.example.test.roomDB.dao.LeadCallDao;
import com.example.test.roomDB.database.LeadListDB;
import com.example.test.roomDB.model.LeadCallModelRoom;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class VisitsFlowCallDetailsActivity extends AppCompatActivity {

    public static String visits_FirstName,visits_MobileNumber;

    public static int visitsCallRequestCode = 1;

    //to send using Post method
    public static int send_callAttemptNo;
    public static String send_callDateTime;
    public static Date send_callDateTime_asDate;
    public static String send_callDuration;
    public static String send_callRecording;
    public static byte[] send_callRecordingInByteArray;
    public static String send_callNotes;
    public static String send_RelativeName;
    public static String send_RelativeContact;
    public static String send_amountCollected;
    public static String send_chequeDate;
    public static String send_chequeNumber;
    public static String send_chequeAmount;
    public static String send_bankName;
    public static String send_reason;
    public static String send_scheduleDateTime;

    public static boolean startCallRecording = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visits_flow_call_details);



        send_scheduleDateTime = Global.getStringFromSharedPref(this,"scheduleVisitForCollection_dateTime");

    }

    //from Visits For The Day
    public List<CallDetails> sendCallLogDetailsList_VisitsFlow() {
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

       // send_RelativeName = Global.getStringFromSharedPref(this,"relativeName");
        //Getting send_RelativeName from Visit_NPA_notificationActivity
        if(send_RelativeName!=null){
            callDetails.setRelativeName(send_RelativeName);
        }


      //  send_RelativeContact = Global.getStringFromSharedPref(this,"relativeContact");
        //Getting send_RelativeContact from Visit_NPA_notificationActivity
        if(send_RelativeContact!=null){
            callDetails.setRelativeContact(send_RelativeContact);
        }

        else{
        callDetails.setRelativeName("");
        callDetails.setRelativeContact("");
    }

        if(send_amountCollected!=null){
         callDetails.setAmountCollected(send_amountCollected);
        }

        if(send_chequeDate!=null){
            callDetails.setChequeDate(send_chequeDate);
        }

        if(send_chequeNumber!=null){
           callDetails.setChequeNumber(send_chequeNumber);
        }

        if(send_chequeAmount!=null){
            callDetails.setChequeAmount(send_chequeAmount);
        }

        if(send_bankName!=null){
            callDetails.setBankName(send_bankName);
        }

        if(send_reason!=null){
            callDetails.setReason(send_reason);
        }

        send_scheduleDateTime = Global.getStringFromSharedPref(this,"scheduleVisitForCollection_dateTime");
        System.out.println("Here VisitFlowCallDetailsList send_scheduleDateTime before: "+Global.getStringFromSharedPref(this,"scheduleVisitForCollection_dateTime"));
        if(send_scheduleDateTime!=null){
            callDetails.setScheduledCallDateTime(send_scheduleDateTime);
            Global.saveStringInSharedPref(this,"scheduleVisitForCollection_dateTime",""); // make empty to reset
            System.out.println("Here VisitFlowCallDetailsList send_scheduleDateTime after: "+Global.getStringFromSharedPref(this,"scheduleVisitForCollection_dateTime"));
        }

        callDetailsList.add(callDetails);
        return callDetailsList;
    }


    // Request made in DetailsOfCustomerAdapter on clicking ivCallLogo in VisitsForTheDayFlow
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

       if(visitsCallRequestCode == requestCode){
           if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

               Intent dial = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + VisitsFlowCallDetailsActivity.visits_MobileNumber));
               startActivity(dial);

               try{

                   getCallRecordingAndCallLogs(this);

               }
               catch(Exception e){
                   if(e.getLocalizedMessage() != null){
                       System.out.println("Here Visits Call Exception :"+e.getLocalizedMessage());
                   }
                  e.printStackTrace();
               }

           }
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

        //  Global.showToast(this, "Call Count for " + phoneNumber + " is: " + leadCallDao.getCallCountUsingPhoneNumber(phoneNumber));
        System.out.println("Here Call Count for " + phoneNumber + " is: " + leadCallDao.getCallCountUsingPhoneNumber(phoneNumber));

        send_callAttemptNo = callCount;
    }

    public void getCallRecordingAndCallLogs(Context context) throws IOException {

        //for Call Recoding in Internal Storage (here Filename is call_recording.mp3) (Convert to Byte Array and Send to Server)
        String filePath = context.getFilesDir().getAbsolutePath() + "/call_recording.mp3";

        //fro Call Recording in External Storage
        //  String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/call_recording.mp3";

        //to get Call Recording
        final MediaRecorder recorder = new MediaRecorder();
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
                    phNumber = visits_MobileNumber; //Customer/Member Mobile Number
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


                   // Toast.makeText(getBaseContext(), phNumber + callDuration + callDayTimes + direction, Toast.LENGTH_LONG).show(); // you can use strings in this line
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




}