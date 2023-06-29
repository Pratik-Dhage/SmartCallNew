package com.example.test.npa_flow.details_of_customer.adapter;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.example.test.google_maps.GoogleMapsActivity;
import com.example.test.google_maps.MapFragment;
import com.example.test.helper_classes.Global;
import com.example.test.helper_classes.NetworkUtilities;
import com.example.test.main_dashboard.MainActivity3API;
import com.example.test.npa_flow.details_of_customer.DetailsOfCustomerActivity;
import com.example.test.npa_flow.details_of_customer.DetailsOfCustomerResponseModel;
import com.example.test.npa_flow.save_location.SaveLocationOfCustomerViewModel;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;

public class DetailsOfCustomerAdapter extends RecyclerView.Adapter<DetailsOfCustomerAdapter.MyViewHolderClass> {

    ArrayList<DetailsOfCustomerResponseModel> detailsOfCustomer_responseModelArrayList;

    public static String dataSetId; // used in LoanCollectionAdapter for saving location when Capture Button is Clicked

    public DetailsOfCustomerAdapter(ArrayList<DetailsOfCustomerResponseModel> detailsOfCustomer_responseModelArrayList) {
        this.detailsOfCustomer_responseModelArrayList = detailsOfCustomer_responseModelArrayList;
    }


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
                        Activity activity = (Activity) context;
                        // Permission is not granted, request the permission
                        ActivityCompat.requestPermissions(activity, new String[]{
                                Manifest.permission.CALL_PHONE,
                                Manifest.permission.READ_CALL_LOG,
                                Manifest.permission.RECORD_AUDIO}, DetailsOfCustomerActivity.REQUEST_CALL);
                    } else {

                        // Permission has already been granted, make the call
                        String phoneNumber = String.valueOf(a.getValue()); //use mobile number fetched from result(API Response)
                        Intent dial = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
                        //  Intent dial = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
                        context.startActivity(dial);

                    }

                }

            });


        }

        //for Capture Button
        holder.binding.btnDetail.setOnClickListener(v -> {

            //if ( a.getLable().contentEquals("Village"))
            if (a.getButtonLable().contentEquals("Capture")) {
                Intent i = new Intent(context, GoogleMapsActivity.class); //for Google Maps
                context.startActivity(i);

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

        //for Button
        if (Objects.equals(a.getButton(), "Y")) {
            holder.binding.btnDetail.setVisibility(View.VISIBLE);
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

            if((String.valueOf(MapFragment.userMarkerLatitude)!=null && String.valueOf(MapFragment.userMarkerLongitude)!=null)){

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
        holder.setIsRecyclable(true);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull MyViewHolderClass holder) {
        super.onViewDetachedFromWindow(holder);
        holder.setIsRecyclable(false); // to prevent data from being disappeared when scrolled upwards
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

}
