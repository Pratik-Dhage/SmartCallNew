package com.example.test.helper_classes;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.UserHandle;
import android.support.v4.media.MediaBrowserCompat;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.ColorRes;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;
import androidx.core.view.ContentInfoCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.test.R;
import com.example.test.api_manager.RestClient;
import com.example.test.api_manager.WebServices;
import com.example.test.api_manager.RestClient;
import com.example.test.fragment_visits_flow.VisitsFlowCallDetailsActivity;
import com.example.test.main_dashboard.MainActivity3API;
import com.example.test.notes_history.NotesHistoryResponseModel;
import com.example.test.notes_history.NotesHistoryViewModel;
import com.example.test.notes_history.adapter.NotesHistoryAdapter;
import com.example.test.npa_flow.details_of_customer.DetailsOfCustomerActivity;
import com.example.test.npa_flow.details_of_customer.adapter.DetailsOfCustomerAdapter;
import com.example.test.npa_flow.loan_collection.GpsLocationListner;
import com.example.test.roomDB.dao.MPinDao;
import com.example.test.roomDB.dao.UserNameDao;
import com.example.test.roomDB.database.LeadListDB;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.disposables.Disposable;

public class Global {

    public static RestClient apiService(){
       return WebServices.create();
        //RestClient.create()
    }

    public static void showToast(Context context, String str){
        Toast.makeText(context,str, Toast.LENGTH_SHORT).show();
    }

    public static boolean isValidEmail(String target) {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public static boolean isValidCellPhone(String number) {
      return Patterns.PHONE.matcher(number).matches();
    }

    public static void showSnackBar(View view, String str){
        Snackbar snackBar = Snackbar.make(view,str,Snackbar.LENGTH_SHORT);
        View snackBarView = snackBar.getView();
        snackBarView.setBackgroundColor(ContextCompat.getColor(view.getContext(),R.color.textBlue));
        snackBar.show();
    }

    public static void hideKeyboard(Activity activity) {

        View view = activity.getCurrentFocus().getRootView();
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void saveStringInSharedPref( Context context, String key, String value) {
        SharedPreferenceHelper.writeString(context, key, value);
    }

    public static String getStringFromSharedPref( Context context,String key) {
        return SharedPreferenceHelper.getString(context, key, "");
    }

    public static void removeStringInSharedPref( Context context, String  key) {
        SharedPreferenceHelper.writeString(context, key, "");
    }

    //for password purpose
    // Define the regular expression for the password policy
    private static final String PASSWORD_PATTERN = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&_])[A-Za-z\\d@$!%*#?&_]{8,12}$";

    // Compile the regular expression into a pattern
    private static final Pattern PATTERN = Pattern.compile(PASSWORD_PATTERN);

    public static boolean isValidPassword(String password) {
        Matcher matcher = PATTERN.matcher(password);
        return matcher.matches();
    }


/*

    For Password to be 8-12 character long, alphanumeric,with at least 1 special character:

           1) ^ and $ - Anchors that assert the position at the start and end of the string, respectively.
           2) (?=.*[A-Za-z]) - A positive lookahead that asserts that at least one alphabetical character must be present in the password.
           3) (?=.*\d) - A positive lookahead that asserts that at least one numeric digit must be present in the password.
           4) (?=.*[@$!%*#?&]) - A positive lookahead that asserts that at least one special character (@,$,!,%,*,#,?,&,_) must be present in the password.
          5)  [A-Za-z\d@$!%*#?&_]{8,12} - Character set and the quantifier that limit the length to 8-12

*/

    public static boolean isValidMobileNumber(Context context,String mobileNumber){

        if(mobileNumber.isEmpty()){
           Global.showToast(context,"Number cannot be empty");
            return false;
        }

       else if(mobileNumber.startsWith("0") || !mobileNumber.matches("^[1-9][0-9]{9}$")){
            Global.showToast(context,"Please Enter valid  Number");
            return false;
        }

        return true;
    }

    //To Display Notes_Edit Dialog (Calls / NPA Flow)
    public static void showNotesEditDialog(Context context){

        View customDialog = LayoutInflater.from(context).inflate(R.layout.custom_dialog_box, null);

        TextView customText =  customDialog.findViewById(R.id.txtCustomDialog);
        ImageView ivClose = customDialog.findViewById(R.id.ivClose);
        Button customButton = customDialog.findViewById(R.id.btnCustomDialog);
        EditText customEditBox = customDialog.findViewById(R.id.edtCustomDialog);
        customEditBox.setVisibility(View.VISIBLE);

        customText.setText(R.string.customer_interaction);


        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(customDialog);
        final AlertDialog dialog = builder.create();
        dialog.show();

        //Edit Text
        if(Global.getStringFromSharedPref(context,"notes")!=null){
                    customEditBox.setText(Global.getStringFromSharedPref(context,"notes"));
                }

         //OK Button
        customButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DetailsOfCustomerActivity.send_callNotes = customEditBox.getText().toString();
                dialog.dismiss();
            }
        });

        ivClose.setOnClickListener(v->{ dialog.dismiss();});

        // to Display previous written Notes
        customEditBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                  Global.saveStringInSharedPref(context,"notes",s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    //To Display Notes_Edit Dialog (Visits Flow)
    public static void showNotesEditDialogVisits(Context context){

        View customDialog = LayoutInflater.from(context).inflate(R.layout.custom_dialog_box, null);

        TextView customText =  customDialog.findViewById(R.id.txtCustomDialog);
        ImageView ivClose = customDialog.findViewById(R.id.ivClose);
        Button customButton = customDialog.findViewById(R.id.btnCustomDialog);
        EditText customEditBox = customDialog.findViewById(R.id.edtCustomDialog);
        customEditBox.setVisibility(View.VISIBLE);

        customText.setText(R.string.customer_interaction);


        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(customDialog);
        final AlertDialog dialog = builder.create();
        dialog.show();

        //Edit Text
        if(Global.getStringFromSharedPref(context,"notes")!=null){
            customEditBox.setText(Global.getStringFromSharedPref(context,"notes"));
        }

        //OK Button
        customButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                VisitsFlowCallDetailsActivity.send_callNotes = customEditBox.getText().toString();
                dialog.dismiss();
            }
        });

        ivClose.setOnClickListener(v->{dialog.dismiss();});

        // to Display previous written Notes
        customEditBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                Global.saveStringInSharedPref(context,"notes",s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }


    // TO Display Notes_History Dialog (Visits / Calls & NPA Flows)
    public static NotesHistoryViewModel notesHistoryViewModel ;
     static ProgressBar progressBar;
     static RecyclerView recyclerViewNotesHistory;
    public static void showNotesHistoryDialog(Context context, String dataSetId){

        notesHistoryViewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(NotesHistoryViewModel.class);
        View customDialog = LayoutInflater.from(context).inflate(R.layout.custom_dialog_notes_history, null);

        TextView customNotesHistoryTextHeading = customDialog.findViewById(R.id.txtCustomDialog);
        Button btnCloseDialog = customDialog.findViewById(R.id.btnCloseDialog);
         recyclerViewNotesHistory = customDialog.findViewById(R.id.rvNotesHistory);
         progressBar = customDialog.findViewById(R.id.loadingProgressBar);

        customNotesHistoryTextHeading.setText(R.string.customer_history);



        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(customDialog);
        final AlertDialog dialog = builder.create();
        dialog.show();


        callNotesHistoryApi(dataSetId); // Call NotesHistory Api
        initObserverNotesHistory(context); // initObserver


        btnCloseDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


    }

    public static void setUpNotesHistoryRecyclerView(Context context){
        View customDialog = LayoutInflater.from(context).inflate(R.layout.custom_dialog_notes_history, null);
        RecyclerView recyclerViewNotesHistory = customDialog.findViewById(R.id.rvNotesHistory);

        notesHistoryViewModel.updateNotesHistory_Data();
        recyclerViewNotesHistory.setAdapter(new NotesHistoryAdapter(notesHistoryViewModel.arrList_NotesHistory_Data));

    }

    public static void callNotesHistoryApi(String dataSetId){
        notesHistoryViewModel.getNotesHistoryData(dataSetId);
    }

    public static void initObserverNotesHistory(Context context){

         progressBar.setVisibility(View.VISIBLE);

        if(NetworkUtilities.getConnectivityStatus(context)){

            notesHistoryViewModel.getMutNotesHistory_ResponseApi().observe((LifecycleOwner) context, result->{
                if(result!=null){

                    progressBar.setVisibility(View.INVISIBLE);
                    notesHistoryViewModel.arrList_NotesHistory_Data.clear();

                    //Set Notes History Recycler View
                    notesHistoryViewModel.updateNotesHistory_Data();
                    recyclerViewNotesHistory.setAdapter(new NotesHistoryAdapter(notesHistoryViewModel.arrList_NotesHistory_Data));


                    // if notes is null it will not be included in  notesHistoryViewModel.arrList_NotesHistory_Data
                    for (NotesHistoryResponseModel item : result) {
                        if (item.getNotes() != null) {
                            notesHistoryViewModel.arrList_NotesHistory_Data.add(item);
                        }
                    }

                  // notesHistoryViewModel.arrList_NotesHistory_Data.addAll(result);
                }

                if(result==null || result.isEmpty()){
                   // Global.showToast(context,"No Data");
                    System.out.println("Here Notes History :No Data");
                }
            });

            //handle  error response
            notesHistoryViewModel.getMutErrorResponse().observe((LifecycleOwner)context, error -> {

                if (error != null && !error.isEmpty()) {
                  //  Global.showToast(context, error);
                    System.out.println("Here: " + error);
                } else {
                    Global.showToast(context, String.valueOf(R.string.check_internet_connection));
                }
            });
        }
        else{
            Global.showToast(context, String.valueOf(R.string.check_internet_connection));
        }

    }


    public static void getUserID_BranchCode_UserName_From_RoomDB(Context context){
        // Get UserName , UserID , BranchCode

        MPinDao mPinDao = LeadListDB.getInstance(context).mPinDao();
        UserNameDao userNameDao = LeadListDB.getInstance(context).userNameDao();

        String userName = userNameDao.getUserNameUsingUserIDInUserNameRoomDB(mPinDao.getUserID());
        MainActivity3API.UserID = mPinDao.getUserID();
        MainActivity3API.BranchCode = mPinDao.getBranchCode();

        System.out.println("Here Global UserID From RoomDB:"+ MainActivity3API.UserID);
        System.out.println("Here Global BranchCode From RoomDB:"+MainActivity3API.BranchCode);

    }

    public static boolean isLocationEnabled(Context context){

        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean isLocationEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        return isLocationEnabled;
    }

    // Required for Android 11(R) & Higher
    public static final int REQUEST_BACKGROUND_LOCATION = 101;
    public static boolean isBackgroundLocationAccessEnabled(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            int permissionState = ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_BACKGROUND_LOCATION);
            if (permissionState == PackageManager.PERMISSION_GRANTED) {
                System.out.println("Here BackGroundLocation Permission: true");
                return true;
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, REQUEST_BACKGROUND_LOCATION);
                System.out.println("Here Requesting BackGroundLocation Permission");
                return false;
            }
        } else {
            // Background location access is always granted prior to Android Q
            System.out.println("Here BackGroundLocation Permission: true");
            return true;
        }
    }

    //For Getting User Device Android Version
    public static String getAndroidVersionAndApiLevel() {
        String versionName = "";

        try {
            versionName = String.valueOf(Build.VERSION.RELEASE)+" API Level:"+ Build.VERSION.SDK_INT;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionName;
    }

    //Convert the HTML instruction to String
    public static String  htmlToNormalString(String htmlString){

        // Convert the HTML instruction to a Spanned object
        Spanned spannedInstruction =  HtmlCompat.fromHtml(htmlString, HtmlCompat.FROM_HTML_MODE_LEGACY);
        return spannedInstruction.toString();
    }

    public static void navigateToGoogleMaps( Context context , double userLatitude , double userLongitude ,double  MarkerLatitude , double MarkerLongitude){

        String uri = "https://www.google.com/maps/dir/?api=1&origin=" +
                userLatitude + "," + userLongitude +
                "&destination=" + MarkerLatitude + "," + MarkerLongitude;

// Create an intent with the Google Maps URI
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));

// Set the package to explicitly open the Google Maps app
        intent.setPackage("com.google.android.apps.maps");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

    }

    public static boolean isGoogleMapsInstalled(Context context)
    {
        try
        {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo("com.google.android.apps.maps", 0 );
            return true;
        }
        catch(PackageManager.NameNotFoundException e)
        {
           e.printStackTrace();
            return false;
        }
    }

    public static Location getDeviceLocation(Context context) {
        Location currentLocation = null;
        System.out.println("Method called");
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
        }else {

            Double latitude;
            Double longitude;
            LocationManager locationManager;
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            boolean gpsProviderEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean networkProviderEnable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            GpsLocationListner gpsLocationListener = new GpsLocationListner();
            if (gpsProviderEnabled) {
                locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        5000,
                        0F,
                        gpsLocationListener
                );
            }
            if (networkProviderEnable) {
                locationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        5000,
                        0F,
                        gpsLocationListener
                );
            }

            Location lastKnownLocationByGps =
                    locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location lastKnownLocationByNetwork =
                    locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (lastKnownLocationByGps != null && lastKnownLocationByNetwork != null) {
                if (lastKnownLocationByGps.getAccuracy() > lastKnownLocationByNetwork.getAccuracy()) {
                    currentLocation = lastKnownLocationByGps;
                    latitude = currentLocation.getLatitude();
                    longitude = currentLocation.getLongitude();
                    // use latitude and longitude as per your need
                } else {
                    currentLocation = lastKnownLocationByNetwork;
                    latitude = currentLocation.getLatitude();
                    longitude = currentLocation.getLongitude();
                    // use latitude and longitude as per your need
                }
            } else if (lastKnownLocationByGps != null) {
                currentLocation = lastKnownLocationByGps;
                latitude = currentLocation.getLatitude();
                longitude = currentLocation.getLongitude();
            } else if (lastKnownLocationByNetwork != null) {
                currentLocation = lastKnownLocationByNetwork;
                latitude = currentLocation.getLatitude();
                longitude = currentLocation.getLongitude();
            }
        }
        return currentLocation;
    }



    public static void CustomTextWatcher(EditText edtPleaseSpecify , TextInputLayout tilSpecify){

        //TextWatcher for Others
        edtPleaseSpecify.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tilSpecify.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

}
