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
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test.R;
import com.example.test.api_manager.WebServices;
import com.example.test.databinding.ItemDetailsOfCustomerBinding;
import com.example.test.fragment_visits_flow.VisitsFlowCallDetailsActivity;
import com.example.test.google_maps.GoogleMapsActivity;
import com.example.test.google_maps.MapFragment;
import com.example.test.helper_classes.Global;
import com.example.test.helper_classes.NetworkUtilities;
import com.example.test.main_dashboard.MainActivity3API;
import com.example.test.npa_flow.details_of_customer.DetailsOfCustomerActivity;
import com.example.test.npa_flow.details_of_customer.DetailsOfCustomerResponseModel;
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

    public DetailsOfCustomerAdapter(ArrayList<DetailsOfCustomerResponseModel> detailsOfCustomer_responseModelArrayList) {
        this.detailsOfCustomer_responseModelArrayList = detailsOfCustomer_responseModelArrayList;
    }

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

    @NonNull
    @Override
    public MyViewHolderClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ItemDetailsOfCustomerBinding view = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_details_of_customer, parent, false);
        return new MyViewHolderClass(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolderClass holder, int position) {

        DetailsOfCustomerResponseModel a = detailsOfCustomer_responseModelArrayList.get(position);
        Context context = holder.itemView.getContext();

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


/*

        //for Name And Loan A/c No. Creating conflicts
        if (a.getLable().contentEquals("Name") || a.getLable().contentEquals("Loan A/c No.")) {
            holder.binding.txtDetailName.setVisibility(View.VISIBLE);
            holder.binding.edtDetail.setVisibility(View.GONE);
            holder.binding.viewLine.setVisibility(View.INVISIBLE);
        }

        //for  Last Interest Paid On
        if(a.getLable().contentEquals("Last Interest Paid On") ){

            String input = a.getValue();
            Log.d("Date from response",input);
            DateTimeFormatter inputFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
            LocalDateTime dateTime = LocalDateTime.parse(input, inputFormatter);
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            String output = dateTime.format(outputFormatter);
//            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
//            try {
//                Date output = sdf.parse(input);
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
            holder.binding.txtDetailName.setText(a.getValue());
        }
        //for DOB
        if(a.getLable().contentEquals("DOB") ){

            String input = a.getValue();
            DateTimeFormatter inputFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
            LocalDateTime dateTime = LocalDateTime.parse(input, inputFormatter);
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            String output = dateTime.format(outputFormatter);
            holder.binding.txtDetailName.setText(output);
        }



        //for Amount Paid
        if (a.getLable().contentEquals("Amount Paid") ) {

            holder.binding.txtDetailName.setVisibility(View.VISIBLE);
            holder.binding.edtDetail.setOnFocusChangeListener((v, hasFocus) -> {

                holder.binding.btnSaveAmountPaid.setVisibility(View.VISIBLE);
               // holder.binding.btnDetail.setText(R.string.save);

            });

            holder.binding.btnSaveAmountPaid.setOnClickListener(v -> {

                   String Amount_Paid = holder.binding.edtDetail.getText().toString();

                   Global.saveStringInSharedPref(context, "Amount_Paid", Amount_Paid); //save Amount Paid in SharedPreference
                   // Global.showToast(context,"Saved");
                    holder.binding.btnSaveAmountPaid.setVisibility(View.GONE);
            });


            if (!Global.getStringFromSharedPref(context, "Amount_Paid").isEmpty()) {

                String Amount_Paid_From_SharedPreference = Global.getStringFromSharedPref(context, "Amount_Paid");
                holder.binding.edtDetail.setText(Amount_Paid_From_SharedPreference);

            }

        }

        //for Total Payable as on (Total Due + Balance Interest)
        if (a.getLable().contentEquals("Total payable as on") ) {

            if (a.getValue() != null || !a.getValue().isEmpty()) {  //if Total Payable is Coming from API
                holder.binding.txtDetailName.setText(a.getValue());
                holder.binding.edtDetail.setVisibility(View.GONE);
            }

            if (a.getValue().isEmpty() || a.getValue().contentEquals("") || a.getValue() == null) { // If Total Payable not coming from API

                if (TotalDue != null && BalanceInterestResult != null && !BalanceInterestResult.isEmpty() && !TotalDue.isEmpty()) {
                    Double TotalPayableAsOn = Double.parseDouble(TotalDue) + Double.parseDouble(BalanceInterestResult);
                    holder.binding.txtDetailName.setText(String.valueOf(TotalPayableAsOn));
                }

                if (BalanceInterestResult == null || TotalDue == null) {
                    holder.binding.txtDetailName.setText("");//Empty
                }

            }

        }



        //For Total Due and Interest Rate to Calculate in Balance Interest Calculation Activity
        if (a.getLable().contentEquals("Total Due") ) {
            Total_due = Double.parseDouble(a.getValue());
            TotalDue = Total_due.toString();
            holder.binding.btnDetail.setVisibility(View.GONE);
        }

        if (a.getLable().contentEquals("Interest Rate") ) {
            Interest_rate = Double.parseDouble(a.getValue());
            InterestRate = Interest_rate.toString();
            holder.binding.btnDetail.setVisibility(View.GONE);
        }


        //for Balance Interest Result
        if (a.getLable().contentEquals("Balance Interest as on") ) {

            if (a.getValue() != null || !a.getValue().isEmpty()) {  // if BalanceInterest is coming from API
                holder.binding.txtDetailName.setText(a.getValue());
                holder.binding.edtDetail.setVisibility(View.GONE);
            }


            if (a.getValue().contentEquals("") || a.getValue() == null) { //if BalanceInterest Not coming from API

                if (Global.getStringFromSharedPref(context, "BalanceInterestResult") != null) {

                    BalanceInterestResult = Global.getStringFromSharedPref(context, "BalanceInterestResult");
                    holder.binding.txtDetailName.setText(BalanceInterestResult);

                }

            }

        }

*/

        //for Call Icon to be visible when coming from Visits For The Day(DashBoard)
        if(MainActivity3API.showCallIcon ){
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
                                Manifest.permission.RECORD_AUDIO}, VisitsFlowCallDetailsActivity.visitsCallRequestCode);
                    }
                    else {

                        // Permission has already been granted, make the call
                         phoneNumber = String.valueOf(a.getValue()); //use mobile number fetched from result(API Response)

                        VisitsFlowCallDetailsActivity.visits_MobileNumber = phoneNumber; // store in variable to use in VisitsFlowCallDetailsActivity
                       // Intent dial = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
                          Intent dial = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
                        context.startActivity(dial);

                        try{

                            getCallRecordingAndCallLogs(context);
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
                /*Intent latLongIntent = new Intent(context, GoogleMapsActivity.class);
                latLongIntent.putExtra("latitudeFromDetailsOfCustomerAdapter",latitudeFromLoanCollectionResponse);
                latLongIntent.putExtra("longitudeFromDetailsOfCustomerAdapter", longitudeFromLoanCollectionResponse);
                latLongIntent.putExtra("LatLongFromDetailsOfCustomerAdapter","LatLongFromDetailsOfCustomerAdapter");
                latLongIntent.putExtra("dataSetId", LoanCollectionAdapter.LoanCollectionAdapter_dataSetId);
                context.startActivity(latLongIntent);*/

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

                if(!Global.isLocationEnabled(context)){
                    Global.showToast(context, "Please Turn Location On");
                }
                else if (Global.isLocationEnabled(context)){
                    Intent i = new Intent(context, GoogleMapsActivity.class); //for Google Maps
                    i.putExtra("isFromDetailsOfCustomerAdapter_CaptureButton","isFromDetailsOfCustomerAdapter_CaptureButton");
                    i.putExtra("dataSetId", LoanCollectionAdapter.LoanCollectionAdapter_dataSetId);
                    context.startActivity(i);
                }


                if (Global.getStringFromSharedPref(context, "formattedDistanceInKm") != null) {
                    Global.removeStringInSharedPref(context, "formattedDistanceInKm"); // Remove previously stored distance
                }

            }
        });

        // for Distance between User and Address
        if (a.getLable().contentEquals("Pincode")) {

            // Setting Width of txtDetailName programmatically in case of Pincode
            ViewGroup.LayoutParams layoutParams = holder.binding.txtDetailName.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            holder.binding.txtDetailName.setLayoutParams(layoutParams);

            //LoanCollectionAdapter_Distance - By default it will display beside Pincode Only if it is not 0.0
            if ((a.getValue()!=null && LoanCollectionAdapter_Distance != null) &&
                    (!LoanCollectionAdapter_Distance.isEmpty() && !LoanCollectionAdapter_Distance.contentEquals("0.0")) ){
                holder.binding.txtDetailName.setText(a.getValue() + ", " + LoanCollectionAdapter_Distance + "Km");
            }

            if (Global.getStringFromSharedPref(context, "formattedDistanceInKm").isEmpty()) {
                //  holder.binding.txtDetailName.setText(a.getValue());
            } else {

                String savedDistance = Global.getStringFromSharedPref(context, "formattedDistanceInKm");
                if(a.getValue()!=null){
                    holder.binding.txtDetailName.setText(a.getValue() + ", " + savedDistance + "Km");}
             else
                {
                  holder.binding.txtDetailName.setText(savedDistance + "Km");
              }



                // Call Save Location of Customer API Here
               callSaveLocationOfCustomerAPI(context,savedDistance);

                //initObserverSavedLocationOfCustomer
             initObserverSavedLocationOfCustomer(context);
            }

        }

        //for Button & Navigate Button(If Capture Button is Visible , then Navigate Button will also be Visible)
        //Navigate & Capture Button Only visible for NPA & VisitsForTheDay using condition CallsForTheDayAdapter.isFromCallsForTheDayAdapter==null
        if (Objects.equals(a.getButton(), "Y") && CallsForTheDayAdapter.isFromCallsForTheDayAdapter==null) {
            holder.binding.btnDetail.setVisibility(View.VISIBLE);
            holder.binding.btnNavigate.setVisibility(View.VISIBLE);
            holder.binding.btnDetail.setText(a.getButtonLable().toString());

        }


        // for EditText
        if (Objects.equals(a.getEditable(), "Y")) {
            holder.binding.edtDetail.setVisibility(View.VISIBLE);
            holder.binding.txtDetailName.setVisibility(View.INVISIBLE);

            //for Amount Paid
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

    public void callSaveLocationOfCustomerAPI(Context context, String savedDistance){
        SaveLocationOfCustomerViewModel saveLocationOfCustomerViewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(SaveLocationOfCustomerViewModel.class);

        // DetailsOfCustomerAdapter.dataSetId != null && GoogleMapsActivity.latitude!=null && GoogleMapsActivity.longitude!=null
        if(DetailsOfCustomerAdapter.dataSetId!=null && savedDistance !=null ){

            // Save LatLong if userMarkerLatitude!=0.0  && userMarkerLongitude!=0.0
            if((String.valueOf(MapFragment.userMarkerLatitude)!=null &&  MapFragment.userMarkerLatitude!=0.0) &&
                    ( String.valueOf(MapFragment.userMarkerLongitude)!=null  && MapFragment.userMarkerLongitude!=0.0)){

                System.out.println("Here DetailsOfCustomerAdapter dataSetId:"+DetailsOfCustomerAdapter.dataSetId);
                System.out.println("Here DetailsOfCustomerAdapter Latitude:"+MapFragment.userMarkerLatitude);
                System.out.println("Here DetailsOfCustomerAdapter Longitude:"+ MapFragment.userMarkerLongitude);

                //Save Location of Customer API
                if(NetworkUtilities.getConnectivityStatus(context)){
                    saveLocationOfCustomerViewModel.getSavedLocationOfCustomerData(DetailsOfCustomerAdapter.dataSetId,String.valueOf(MapFragment.userMarkerLatitude),String.valueOf(MapFragment.userMarkerLongitude),savedDistance);
                }

            }

        }
    }

    public void initObserverSavedLocationOfCustomer(Context context){
        SaveLocationOfCustomerViewModel saveLocationOfCustomerViewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(SaveLocationOfCustomerViewModel.class);

        saveLocationOfCustomerViewModel.getMutSaveLocationOfCustomerResponseApi().observe((LifecycleOwner) context, result->{
            if(result!=null){

              //  Global.showToast(context,result);
                System.out.println("Here SavedDistanceOfCustomerResponse: "+result);
            }
        });

        //handle  error response
        saveLocationOfCustomerViewModel.getMutErrorResponse().observe((LifecycleOwner)context, error -> {

            if (error != null && !error.isEmpty()) {
                Global.showToast(context, error);
                System.out.println("Here: " + error);
            } else {
                Global.showToast(context,"Check internet connection");
            }
        });
    }

    @Override
    public void onViewAttachedToWindow(@NonNull MyViewHolderClass holder) {
        super.onViewAttachedToWindow(holder);
       // holder.setIsRecyclable(true);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull MyViewHolderClass holder) {
        super.onViewDetachedFromWindow(holder);
       // holder.setIsRecyclable(false); // to prevent data from being disappeared when scrolled upwards
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

    private void getCallLogs(Context context) {

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
