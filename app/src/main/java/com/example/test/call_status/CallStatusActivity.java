package com.example.test.call_status;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.content.BroadcastReceiver;
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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test.R;
import com.example.test.broadcast_receiver.MyBroadCastReceiverClass;
import com.example.test.databinding.ActivityCallStatusBinding;
import com.example.test.helper_classes.Global;
import com.example.test.main_dashboard.MainActivity3API;
import com.example.test.roomDB.dao.LeadCallDao;
import com.example.test.roomDB.database.LeadListDB;
import com.example.test.roomDB.model.LeadCallModelRoom;
import com.example.test.view_products.OffersListActivity;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Date;

public class CallStatusActivity extends AppCompatActivity {

    ActivityCallStatusBinding binding;
    View view;
    int REQUEST_CALL = 1; // can use any integer value

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        initializeFields();
        setUpData();
        onClickListener();
    }

    private void initializeFields() {

        binding = DataBindingUtil.setContentView(this,R.layout.activity_call_status);
        view = binding.getRoot();
    }

    private void setUpData(){
        String firstName = getIntent().getStringExtra("firstName");
        String phoneNumber = getIntent().getStringExtra("phoneNumber");

        binding.txtLeadName.setText(firstName);
        binding.txtLeadMobileNumber.setText(phoneNumber);
    }

    private void onClickListener() {

        binding.ivBack.setOnClickListener(v -> {
            onBackPressed(); // back to Lead List Activity
        });

        binding.ivHome.setOnClickListener(v->{
            startActivity(new Intent(this, MainActivity3API.class));
        });

       /* binding.labelPreApprovedOffer.setOnClickListener(v->{

            String firstName = binding.txtLeadName.getText().toString();
            String phoneNumber = binding.txtLeadMobileNumber.getText().toString();

            Intent i = new Intent(this, OffersListActivity.class);
            i.putExtra("firstName",firstName);
            i.putExtra("phoneNumber",phoneNumber);
            startActivity(i);
        });*/

        binding.clPreApprovedOffer.setOnClickListener(v->{
            if(binding.labelCreditCard.getVisibility()==View.INVISIBLE){

                binding.labelNoPreApprovedOffer.setVisibility(View.INVISIBLE);
                binding.labelCreditCard.setVisibility(View.VISIBLE);
                binding.labelCreditCardOfferDetails.setVisibility(View.VISIBLE);

                binding.labelPersonalLoan.setVisibility(View.VISIBLE);
                binding.labelPersonalLoanOfferDetails.setVisibility(View.VISIBLE);

                binding.txtSirMadam.setVisibility(View.INVISIBLE);

            }

            else{

                binding.labelNoPreApprovedOffer.setVisibility(View.VISIBLE);
                binding.labelCreditCard.setVisibility(View.INVISIBLE);
                binding.labelCreditCardOfferDetails.setVisibility(View.INVISIBLE);

                binding.labelPersonalLoan.setVisibility(View.GONE);
                binding.labelPersonalLoanOfferDetails.setVisibility(View.GONE);

               // binding.txtSirMadam.setVisibility(View.VISIBLE);

            }


        });

        binding.clViewOtherOffer.setOnClickListener(v->{

            if(binding.labelCreditCard.getVisibility()==View.VISIBLE){
                binding.txtSirMadam.setVisibility(View.INVISIBLE);
            }
           else{
                binding.txtSirMadam.setVisibility(View.VISIBLE);

            }


        });

        //for test purpose to move to next activity
        binding.ivWifiCall2.setOnClickListener(v->{
            String firstName = binding.txtLeadName.getText().toString();
            String phoneNumber = binding.txtLeadMobileNumber.getText().toString();


            Intent i = new Intent(CallStatusActivity.this,CallStatusWithProductsActivity.class) ;
            i.putExtra("firstName",firstName);
            i.putExtra("phoneNumber",phoneNumber);
            startActivity(i);
        });

        //will make Actual Call to Lead
        binding.ivWifiCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED

                ) {
                    // Permission is not granted, request the permission
                    ActivityCompat.requestPermissions(CallStatusActivity.this, new String[]{
                            Manifest.permission.CALL_PHONE,
                            Manifest.permission.READ_CALL_LOG,
                            Manifest.permission.RECORD_AUDIO}, REQUEST_CALL);
                } else {
                    // Permission has already been granted, make the call
                    String phoneNumber = getIntent().getStringExtra("phoneNumber");
                    Intent dial = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+phoneNumber));
                    startActivity(dial);

                    //Store Call Count in RoomDB
                    String firstName = binding.txtLeadName.getText().toString();
                    storeCallCountInRoomDB(firstName,phoneNumber);

                    try {

                        // Register the broadcast receiver in your activity or service
                        MyBroadCastReceiverClass receiver = new MyBroadCastReceiverClass();
                        IntentFilter filter = new IntentFilter(Intent.ACTION_NEW_OUTGOING_CALL);
                        registerReceiver(receiver, filter);

                      //  getCallRecordingAndCallLogs();
                    } catch (Exception e) {
                        Global.showSnackBar(view,"Call Error"+e);
                        System.out.println("Here Error:"+e);
                    }
                }

            }
        });


        //for Notes
        binding.ivNotesIcon.setOnClickListener(v->{

            View customDialog = LayoutInflater.from(this).inflate(R.layout.custom_dialog_box, null);

            TextView customText =  customDialog.findViewById(R.id.txtCustomDialog);
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
        binding.ivHistory.setOnClickListener(v->{

            View customDialog = LayoutInflater.from(this).inflate(R.layout.custom_dialog_box, null);

            TextView customText =  customDialog.findViewById(R.id.txtCustomDialog);
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
                String phoneNumber = getIntent().getStringExtra("phoneNumber");
                Intent dial = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+phoneNumber));
                startActivity(dial);

                try {

                    // Register the broadcast receiver in your activity or service
                    MyBroadCastReceiverClass receiver = new MyBroadCastReceiverClass();
                    IntentFilter filter = new IntentFilter(Intent.ACTION_NEW_OUTGOING_CALL);
                    registerReceiver(receiver, filter);

                    //getCallRecordingAndCallLogs();

                    //Store Call Count in RoomDB
                    String firstName = binding.txtLeadName.getText().toString();
                    storeCallCountInRoomDB(firstName,phoneNumber);

                } catch (Exception e) {
                   System.out.println("Here Error:"+e);
                    Global.showSnackBar(view,"Call Error"+e);
                }
            } else {
                // Permission is denied, show a message
                Global.showSnackBar(view,getResources().getString(R.string.permission_to_call_denied));
            }
        }
    }

    private void storeCallCountInRoomDB(String firstName,String phoneNumber){

        LeadCallDao leadCallDao = LeadListDB.getInstance(this).leadCallDao();
        int callCount = leadCallDao.getCallCountUsingPhoneNumber(phoneNumber);
        callCount++; //callCount+1
        LeadCallModelRoom leadCallModelRoom = new LeadCallModelRoom(callCount, firstName, phoneNumber);

        leadCallDao.insert(leadCallModelRoom);
      //  if Call Count >2 then make it to zero
        if(callCount>2){
            callCount = 0;
        }

        leadCallDao.UpdateLeadCalls(callCount,phoneNumber);

        Global.showToast(this, "Call Count for " + phoneNumber + " is: " + leadCallDao.getCallCountUsingPhoneNumber(phoneNumber));
        System.out.println("Here Call Count for " + phoneNumber + " is: " + leadCallDao.getCallCountUsingPhoneNumber(phoneNumber));

    }


    public void getCallRecordingAndCallLogs() throws IOException {

        //for Call Recoding in Internal Storage (here Filename is call_recording.mp3)
        String filePath = getFilesDir().getAbsolutePath() + "/call_recording.mp3";

        //fro Call Recording in External Storage
      //  String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/call_recording.mp3";

        //to get Call Recording
        MediaRecorder recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.VOICE_CALL);
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
                        // Stop recording after the call has ended
                        recorder.stop();
                        recorder.reset();
                        recorder.release();
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
        String phNumber,callDate,callDuration,callDayTimes;
        Date dateFormat;

        if (c != null) {
            totalCall = 1; // integer call log limit

            if (c.moveToLast()) { //starts pulling logs from last - you can use moveToFirst() for first logs
                for (int j = 0; j < totalCall; j++) {


                    // phNumber = c.getString(c.getColumnIndexOrThrow(CallLog.Calls.NUMBER));
                    phNumber = getIntent().getStringExtra("phoneNumber"); // for getting current phoneNumber
                     callDate = c.getString(c.getColumnIndexOrThrow(CallLog.Calls.DATE));
                     callDuration = c.getString(c.getColumnIndexOrThrow(CallLog.Calls.DURATION));
                     dateFormat= new Date(Long.valueOf(callDate));
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
                    System.out.println("CallLog: Phone Number"+phNumber + "\nDuration"+callDuration + "\nDate_n_Time"+callDayTimes + "\nType"+direction);
                }


            }
            c.close();



        }
    }


}
