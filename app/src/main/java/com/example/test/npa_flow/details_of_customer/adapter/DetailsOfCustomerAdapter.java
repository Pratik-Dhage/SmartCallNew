package com.example.test.npa_flow.details_of_customer.adapter;

import static android.content.Context.TELEPHONY_SERVICE;
//import static androidx.core.app.AppOpsManagerCompat.Api23Impl.getSystemService;
import static com.example.test.npa_flow.loan_collection.adapter.LoanCollectionAdapter.LoanCollectionAdapter_Distance;
import static com.example.test.npa_flow.loan_collection.adapter.LoanCollectionAdapter.LoanCollectionAdapter_dataSetId;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.provider.CallLog;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test.R;
import com.example.test.api_manager.WebServices;
import com.example.test.databinding.ItemDetailsOfCustomerBinding;
import com.example.test.fragment_visits_flow.Visit_NPA_NotAvailableActivity;
import com.example.test.fragment_visits_flow.Visit_NPA_NotificationActivity;
import com.example.test.fragment_visits_flow.Visit_NPA_PaymentModeActivity;
import com.example.test.fragment_visits_flow.Visit_NPA_RescheduledActivity;
import com.example.test.fragment_visits_flow.Visit_NPA_StatusActivity;
import com.example.test.fragment_visits_flow.VisitsFlowCallDetailsActivity;
import com.example.test.fragments_activity.AlternateNumberApiCall;
import com.example.test.fragments_activity.CustomerDetailsActivity;
import com.example.test.google_maps.GoogleMapsActivity;
import com.example.test.google_maps.MapFragment;
import com.example.test.helper_classes.Global;
import com.example.test.helper_classes.NetworkUtilities;
import com.example.test.main_dashboard.MainActivity3API;
import com.example.test.npa_flow.CallDetailOfCustomerActivity;
import com.example.test.npa_flow.NotSpokeToCustomerActivity;
import com.example.test.npa_flow.PaymentInfoOfCustomerActivity;
import com.example.test.npa_flow.PaymentModeActivity;
import com.example.test.npa_flow.PaymentModeStatusActivity;
import com.example.test.npa_flow.PaymentNotificationOfCustomerActivity;
import com.example.test.npa_flow.SubmitCompletionActivityOfCustomer;
import com.example.test.npa_flow.VisitCompletionOfCustomerActivity;
import com.example.test.npa_flow.details_of_customer.DetailsOfCustomerActivity;
import com.example.test.npa_flow.details_of_customer.DetailsOfCustomerResponseModel;
import com.example.test.npa_flow.details_of_customer.DetailsOfCustomerViewModel;
import com.example.test.npa_flow.loan_collection.LoanCollectionActivity;
import com.example.test.npa_flow.loan_collection.adapter.LoanCollectionAdapter;
import com.example.test.npa_flow.save_location.SaveLocationOfCustomerViewModel;
import com.example.test.schedule_flow.calls_for_the_day.adapter.CallsForTheDayAdapter;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Objects;

public class DetailsOfCustomerAdapter extends RecyclerView.Adapter<DetailsOfCustomerAdapter.MyViewHolderClass> {

    ArrayList<DetailsOfCustomerResponseModel> detailsOfCustomer_responseModelArrayList;

    public static String dataSetId; // used in LoanCollectionAdapter for saving location when Capture/Navigate Button is Clicked
    private Context context;
    public static AppCompatActivity activity;
    public DetailsOfCustomerAdapter(AppCompatActivity activity,ArrayList<DetailsOfCustomerResponseModel> detailsOfCustomer_responseModelArrayList) {
        this.activity = activity;
        this.detailsOfCustomer_responseModelArrayList = detailsOfCustomer_responseModelArrayList;
    }

    private Location currentLocation;//User's Current location may change everytime when Capture button is clicked

     public static String phoneNumber =""; //to use in VisitsFlowCallDetailsActivity to Call if permission already granted

    // Get LatLong from LoanCollectionAdapter and use in DetailsOfCustomer Page
    // on Clicking Navigate Button it will navigate to Google Maps and get the distance
    public static double latitudeFromLoanCollectionResponse, longitudeFromLoanCollectionResponse;


    //For Calculating Balance Interest
    Double Total_due;
    Double Interest_rate;

    String TotalDue;
    String InterestRate;
    String BalanceInterestResult;

  public static String alternateNumber;
  public static boolean isSavingAlternateNumber = false;

    @NonNull
    @Override
    public MyViewHolderClass onCreateViewHolder( @NonNull ViewGroup parent, int viewType) {

        ItemDetailsOfCustomerBinding view = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_details_of_customer, parent, false);
        return new MyViewHolderClass(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolderClass holder, int position) {

        DetailsOfCustomerResponseModel a = detailsOfCustomer_responseModelArrayList.get(position);
        context = holder.itemView.getContext();
        currentLocation = Global.getDeviceLocation(context); //for Capture Button

        // Sort by number in getSequence coming from DetailsOfCustomerResponseModel
        detailsOfCustomer_responseModelArrayList.sort(Comparator.comparingInt(DetailsOfCustomerResponseModel::getSequence));

        if (a.getLable() != null) {
            holder.binding.labelDetailName.setText(a.getLable());
        }

        if (a.getValue() != null) {

            Object value = a.getValue();
            if (value instanceof Number) {
                // value is a Long or a Double
                Number numberValue = (Number) value;

                if (numberValue instanceof Long) {
                    // value is a Long
                    long longValue = numberValue.longValue();
                    DecimalFormat df = new DecimalFormat("#.00"); //after decimal 2 digits
                    holder.binding.txtDetailName.setText(df.format(longValue));
                }
                if (numberValue instanceof Double) {
                    // value is a Double
                    double doubleValue = numberValue.doubleValue();
                    DecimalFormat df = new DecimalFormat("#.00"); //after decimal 2 digits
                    holder.binding.txtDetailName.setText(df.format(doubleValue));
                }
            } else {
                // value is not a Number(i.e String)
                holder.binding.txtDetailName.setText(String.valueOf(a.getValue()));
            }

        }




        //for Call Icon to be visible when coming from Visits For The Day(DashBoard)
        if(MainActivity3API.showCallIcon || Global.getStringFromSharedPref(context,"showCallIcon").equals("true") ){
            if(a.getLable().toLowerCase().contains("mobile")){
                holder.binding.ivCallLogo.setVisibility(View.VISIBLE);
            }

            holder.binding.txtDetailName.setOnClickListener(v->{
                holder.binding.ivCallLogo.performClick();
            });

            holder.binding.ivCallLogo.setOnClickListener(v-> {
                if (a.getLable().toLowerCase().contains("mobile")) {
                    //make an actual call
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED ||
                            ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED ||
                            ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED ||
                            ActivityCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED

                    ) {
                           //if permission not granted store mobile number in VisitsFlowCallDetailsActivity.visits_MobileNumber to make call
                        // after permission is granted in VisitsFlowCallDetailsActivity onRequestPermissionsResult
                        VisitsFlowCallDetailsActivity.visits_MobileNumber = String.valueOf(a.getValue()); //use mobile number fetched from result(API Response)
                        System.out.println("Here VisitFlow VisitsFlowCallDetailsActivity.visits_MobileNumber"+VisitsFlowCallDetailsActivity.visits_MobileNumber);

                        Activity activity = (Activity) context;
                        // Permission is not granted, request the permission
                        ActivityCompat.requestPermissions(activity, new String[]{
                                Manifest.permission.CALL_PHONE,
                                Manifest.permission.READ_CALL_LOG,
                                Manifest.permission.READ_PHONE_STATE,
                                Manifest.permission.RECORD_AUDIO}, VisitsFlowCallDetailsActivity.visitsCallRequestCode);
                    }
                    else {

                        // Permission has already been granted, make the call
                         phoneNumber = String.valueOf(a.getValue()); //use mobile number fetched from result(API Response)

                      /*  VisitsFlowCallDetailsActivity.visits_MobileNumber = phoneNumber; // store in variable to use in VisitsFlowCallDetailsActivity
                       // Intent dial = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
                          Intent dial = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
                        context.startActivity(dial);*/

                        try{
                             Global.showSelectedMobileNumberDialog(phoneNumber,alternateNumber,context);
                           // getCallRecordingAndCallLogs(context);
                            System.out.println("Here Call Recording called");

                        }
                        catch(Exception e){
                            if(e.getLocalizedMessage() != null){
                                System.out.println("Here Visits Call Exception :"+e.getLocalizedMessage());
                            }
                            e.printStackTrace();
                        }

                    }

                }

            });


        }

        //for Navigate Button
        holder.binding.btnNavigate.setOnClickListener(v->{

            //Navigate Button will work only from NPA flow & VisitsForTheDayFlow

            //BackGround Location Access is mandatory for Android 11 & Higher

                if(!Global.isLocationEnabled(context) || !Global.isBackgroundLocationAccessEnabled((Activity) context)){
                    Global.showToast(context, "Please Turn Location On");
                }
                else if (Global.isLocationEnabled(context) && Global.isBackgroundLocationAccessEnabled((Activity) context) &&
                        (Global.getStringFromSharedPref(context,"latitudeFromLoanCollectionAdapter")!=null) && Global.getStringFromSharedPref(context,"longitudeFromLoanCollectionAdapter")!=null ){

                    System.out.println("Here DetailsOfCustomerAdapter dataSetId:"+LoanCollectionAdapter.LoanCollectionAdapter_dataSetId);
                    System.out.println("Here LatitudeFromDetailsOfCustomerAdapter:"+Global.getStringFromSharedPref(context,"latitudeFromLoanCollectionAdapter")+" & LongitudeFromDetailsOfCustomerAdapter:"+Global.getStringFromSharedPref(context,"longitudeFromLoanCollectionAdapter"));

                    System.out.println("Here isBackGroundLocationEnabled:"+ Global.isBackgroundLocationAccessEnabled((Activity) context));

                       try{
                           //  Navigate To Google Maps App for Direction (coming from Either NPA OR VisitsForTheDay)
                           double userLatitude =  Global.getDeviceLocation(context).getLatitude();
                           double userLongitude =  Global.getDeviceLocation(context).getLongitude();
                           double latitudeFromLoanCollectionResponse = Double.parseDouble(Global.getStringFromSharedPref(context,"latitudeFromLoanCollectionAdapter"));
                           double longitudeFromLoanCollectionResponse = Double.parseDouble(Global.getStringFromSharedPref(context,"longitudeFromLoanCollectionAdapter"));


                           String uri = "https://www.google.com/maps/dir/?api=1&origin=" +
                                   userLatitude + "," + userLongitude +
                                   "&destination=" + latitudeFromLoanCollectionResponse + "," + longitudeFromLoanCollectionResponse;



                           // Create an intent with the Google Maps URI
                           Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));

                           // Set the package to explicitly open the Google Maps app
                           intent.setPackage("com.google.android.apps.maps");
                           intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //used when launching an activity from a context that is not an activity

                           //if GoogleMaps installed
                           if(Global.isGoogleMapsInstalled(context)){
                               System.out.println("isGoogleMaps installed: true");
                               context.startActivity(intent);
                           }
                           //if GoogleMaps not installed
                           else{
                               Global.showToast(context,"Kindly install GoogleMaps");
                           }

                       }
                       catch (Exception e)
                       {
                           e.printStackTrace();
                       }

                }

        });


        //for Capture Button
        holder.binding.btnDetail.setOnClickListener(v -> {

            //if ( a.getLable().contentEquals("Village"))
            if (a.getButtonLable().contentEquals("Capture")) {

                //check if Location Turned On
                if(!Global.isLocationEnabled(context)  || !Global.isBackgroundLocationAccessEnabled((Activity) context)){
                    Global.showLocationMessageDialog(context.getString(R.string.pls_turn_on_location),context);
                }
                // if User Turns Location Off / To get current Location everytime when user clicks Capture Button
                else if (null==currentLocation){
                    currentLocation = Global.getDeviceLocation(context);
                    Global.showLocationMessageDialog(context.getString(R.string.getting_device_location),context);
                }
                else if (Global.isLocationEnabled(context) && Global.isBackgroundLocationAccessEnabled((Activity) context) && null!=currentLocation){

                    LoanCollectionAdapter.LoanCollectionAdapter_dataSetId = Global.getStringFromSharedPref(context,"dataSetId");

                    if(null!=LoanCollectionAdapter.LoanCollectionAdapter_dataSetId){
                        Intent i = new Intent(context, GoogleMapsActivity.class); //for Google Maps
                        i.putExtra("isFromDetailsOfCustomerAdapter_CaptureButton","isFromDetailsOfCustomerAdapter_CaptureButton");
                        i.putExtra("dataSetId", LoanCollectionAdapter.LoanCollectionAdapter_dataSetId);
                        context.startActivity(i);
                    }

                }


                if (Global.getStringFromSharedPref(context, "formattedDistanceInKm") != null) {
                    Global.removeStringInSharedPref(context, "formattedDistanceInKm"); // Remove previously stored distance
                }

            }
        });

        //for Location
        if (a.getLable().contentEquals("Location")) {

            if(GoogleMapsActivity.isSaveButtonClicked && GoogleMapsActivity.saveDistanceBoolean){
                System.out.println("LoanCollectionAdapter isSaveButtonClicked:"+GoogleMapsActivity.isSaveButtonClicked);
                ViewGroup.LayoutParams layoutParams = holder.binding.txtDetailName.getLayoutParams();
                layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                holder.binding.txtDetailName.setLayoutParams(layoutParams);
                holder.binding.txtDetailName.setText(Global.getStringFromSharedPref(context,"formattedDistanceInKm")+" Km");
            }

            // if Distance in Km is not Null
          else  if(null!=a.getValue()){
                // Setting Width of txtDetailName programmatically in case of Location
                ViewGroup.LayoutParams layoutParams = holder.binding.txtDetailName.getLayoutParams();
                layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                holder.binding.txtDetailName.setLayoutParams(layoutParams);
                holder.binding.txtDetailName.setText(String.valueOf(a.getValue())+" Km");//Keep String.valueOf()
            }
        }



        //for Alternate Number
        if(a.getLable().contentEquals("Alternate Number")){
            holder.binding.txtDetailName.setVisibility(View.GONE);
            holder.binding.edtDetail.setHint("");

            // if Alternate Number is pre-populated -> Disable edtDetail
           if(null!= a.getValue()){
               holder.binding.edtDetail.setText(String.valueOf(a.getValue()));
               holder.binding.ivLockedIcon.setVisibility(View.VISIBLE);
               holder.binding.ivSaveAlternateNumber.setVisibility(View.GONE);
               holder.binding.edtDetail.setEnabled(false);
               alternateNumber = String.valueOf(a.getValue());
           }
           else{
               holder.binding.ivSaveAlternateNumber.setVisibility(View.VISIBLE);
               alternateNumber = null;
           }

                //on clicking Lock icon-> Allow editing Alternate Number
            holder.binding.ivLockedIcon.setOnClickListener(v->{
                holder.binding.ivSaveAlternateNumber.setVisibility(View.VISIBLE);
                holder.binding.ivLockedIcon.setVisibility(View.GONE);
                holder.binding.edtDetail.setEnabled(true);

                //for hiding clSecondHalf
                Global.showHideConstraintLayoutSecondHalf(activity,true);
            });

           //for hiding clSecondHalf on click on editText
            holder.binding.edtDetail.setOnFocusChangeListener((v,hasFocus)->{

                if(hasFocus){
                    Global.showHideConstraintLayoutSecondHalf(activity,true);
                }
            });


            textWatcherForEditText(holder.binding.edtDetail,holder.binding.ivSaveAlternateNumber );
            holder.binding.ivSaveAlternateNumber.setOnClickListener(v1->{

                if(Global.isValidMobileNumber(context,holder.binding.edtDetail.getText().toString().trim())){

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Save Alternate Number");
                    builder.setMessage("Are you sure?");

                    //Yes Button , Lock the Alternate Number
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                             alternateNumber = holder.binding.edtDetail.getText().toString().trim();

                            holder.binding.edtDetail.setEnabled(false);
                            holder.binding.ivLockedIcon.setVisibility(View.VISIBLE);
                            holder.binding.ivSaveAlternateNumber.setVisibility(View.GONE);

                            //After Saving if User Calls On Alternate No.
                            DetailsOfCustomerActivity.Alternate_Mobile_Number =alternateNumber;

                            //After Saving Alternate No. show clSecondHalf
                            Global.showHideConstraintLayoutSecondHalf(activity,false);

                            //boolean value to save Alternate No. on Button clicks for Activities that contain DetailsOfCustomerAdapter
                            isSavingAlternateNumber = true;
                             System.out.println("isSaveAlternateNumber:"+isSavingAlternateNumber);


                            // to update Alternate no. where DetailsOfCustomerApi is called
                            DetailsOfCustomerViewModel detailsOfCustomerViewModel = new DetailsOfCustomerViewModel();
                            Global.getUpdatedDetailsList(detailsOfCustomer_responseModelArrayList);


                             //call saveAlternateNumber Api
                            if(null!= dataSetId){
                                detailsOfCustomerViewModel.saveAlternateNumber_Data(dataSetId,alternateNumber);

                            }
                            //in case dataSetId goes null, getting it from LoanCollectionAdapter
                            else {
                                String dataSetId = Global.getStringFromSharedPref(context,"dataSetId");
                                detailsOfCustomerViewModel.saveAlternateNumber_Data(dataSetId,alternateNumber);
                            }

                        }
                    });
                    //No Button
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            isSavingAlternateNumber =false;

                            //if User does not want to save Alternate Number
                            Global.showHideConstraintLayoutSecondHalf(activity,false);

                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();

                }



            });

        }

        //for Button & Navigate Button(If Capture Button is Visible , then Navigate Button will also be Visible)
        //Navigate & Capture Button Only visible for NPA & VisitsForTheDay using condition CallsForTheDayAdapter.isFromCallsForTheDayAdapter==null
        if (Objects.equals(a.getButton(), "Y") /*&& ( CallsForTheDayAdapter.isFromCallsForTheDayAdapter==null
                || Global.getStringFromSharedPref(context,"isFromCallsForTheDayAdapter")==null)*/ ) {
            holder.binding.btnDetail.setVisibility(View.VISIBLE);
            holder.binding.btnNavigate.setVisibility(View.VISIBLE);
            holder.binding.btnDetail.setText(a.getButtonLable().toString());

        }


        // for EditText
        if (Objects.equals(a.getEditable(), "Y")) {
            holder.binding.edtDetail.setVisibility(View.VISIBLE);
            holder.binding.txtDetailName.setVisibility(View.INVISIBLE);

        }

        //for Amount Paid
        if(a.getLable().contentEquals("Amount Paid")){

            holder.binding.edtDetail.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    Global.saveStringInSharedPref(context, "Amount_Paid", s.toString()); //save Amount Paid in SharedPreference
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });

            if(!Global.getStringFromSharedPref(context,"Amount_Paid").isEmpty()){
                String Amount_Paid = Global.getStringFromSharedPref(context,"Amount_Paid");
                holder.binding.edtDetail.setText(Amount_Paid);
            }

        }

        //for separation line between Personal and Account Details
        if (a.getLable().toLowerCase().contains("father's name")) {
            holder.binding.viewLine.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return detailsOfCustomer_responseModelArrayList.size();
    }



    //for Saving Alternate MobileNumber



    @Override
    public void onViewAttachedToWindow(@NonNull MyViewHolderClass holder) {
        super.onViewAttachedToWindow(holder);
       // holder.setIsRecyclable(true);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull MyViewHolderClass holder) {
        super.onViewDetachedFromWindow(holder);
        holder.setIsRecyclable(false); // for Distance in Km beside Capture button to not Disappear
    }

    @SuppressLint("NotifyDataSetChanged")
    public ArrayList setData(ArrayList<DetailsOfCustomerResponseModel> data) {
        if (data.isEmpty()) {
            detailsOfCustomer_responseModelArrayList = new ArrayList();
        }
        detailsOfCustomer_responseModelArrayList = data;
        notifyDataSetChanged();

        return detailsOfCustomer_responseModelArrayList;
    }


    class MyViewHolderClass extends RecyclerView.ViewHolder {

        private ItemDetailsOfCustomerBinding binding;

        public MyViewHolderClass(ItemDetailsOfCustomerBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }

    public static void getCallRecordingAndCallLogs(Context context) throws IOException {

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
                        getCallLogs(context);
                        //   retrieveCallLog();
                        // Stop recording after the call has ended
                        try {
                            // recorder.stop(); //getting Exception for stop (Media Recorder not able to stop so using pause())
                            recorder.pause();
                            recorder.reset();
                            //recorder.release();

                            byte[] bytes_array = convertFileToByteArray(filePath); // convert Audio to Byte Array and Send to Server
                            System.out.println("Here byte_array:" + Arrays.toString(bytes_array)); ;
                            VisitsFlowCallDetailsActivity.send_callRecording = Arrays.toString(bytes_array);
                            VisitsFlowCallDetailsActivity.send_callRecordingInByteArray = bytes_array;

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

        TelephonyManager telephonyManager = (TelephonyManager)  context.getSystemService(TELEPHONY_SERVICE);
        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);

    }

    private static void getCallLogs(Context context) {

        ContentResolver cr = context.getContentResolver();
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
                    phNumber = VisitsFlowCallDetailsActivity.visits_MobileNumber; //Customer/Member Mobile Number
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


                   // Toast.makeText(context, phNumber + callDuration + callDayTimes + direction, Toast.LENGTH_LONG).show(); // you can use strings in this line
                    System.out.println("CallLog: Phone Number" + phNumber + "\nDuration" + callDuration + "\nDate_n_Time" + callDayTimes + "\nType" + direction);

                    // To send to backend
                    VisitsFlowCallDetailsActivity.send_callDateTime = callDayTimes;
                    VisitsFlowCallDetailsActivity.send_callDuration = callDuration;
                    VisitsFlowCallDetailsActivity.send_callDateTime_asDate = dateFormat;

                }


            }
            c.close();


        }
    }

    // for Converting Audio(Call Recording(.mp3 format) to Byte Array)
    public static byte[]  convertFileToByteArray(String path) throws IOException {

        FileInputStream fis = new FileInputStream(path);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] b = new byte[1024];

        for (int readNum; (readNum = fis.read(b)) != -1; ) {
            bos.write(b, 0, readNum);
        }

        byte[] bytesArray = bos.toByteArray();

        return bytesArray;
    }

    //For Alternate Number
   public void textWatcherForEditText(EditText editText, ImageView imageView){

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(editText.getText().toString().length()==10){

                    //Show Constraint Layout SecondHalf if editText is 10 digits
                  //  Global.showHideConstraintLayoutSecondHalf(activity,false);

                }

                //After typing if user deletes & makes editText empty
               /* else if(editText.getText().toString().isEmpty()){
                    editText.clearFocus();
                    Global.showHideConstraintLayoutSecondHalf(activity,false);
                }*/

                else{
                    imageView.setImageResource(R.drawable.unlocked);

                    //Hide Constraint Layout SecondHalf
                    Global.showHideConstraintLayoutSecondHalf(activity,true);

                }

            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

   }



}
