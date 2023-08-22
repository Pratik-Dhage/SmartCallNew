package com.example.test.npa_flow.loan_collection.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test.R;
import com.example.test.databinding.ItemLoanCollectionBinding;
import com.example.test.google_maps.GoogleMapsActivity;
import com.example.test.google_maps.MapFragment;
import com.example.test.helper_classes.Global;
import com.example.test.helper_classes.NetworkUtilities;
import com.example.test.main_dashboard.MainActivity3API;
import com.example.test.npa_flow.NotSpokeToCustomerActivity;
import com.example.test.npa_flow.WebViewActivity;
import com.example.test.npa_flow.details_of_customer.DetailsOfCustomerActivity;
import com.example.test.npa_flow.details_of_customer.adapter.DetailsOfCustomerAdapter;
import com.example.test.npa_flow.loan_collection.LoanCollectionActivity;
import com.example.test.npa_flow.loan_collection.LoanCollectionListResponseModel;
import com.example.test.npa_flow.save_location.SaveLocationOfCustomerViewModel;
import com.example.test.npa_flow.status_of_customer.StatusOfCustomerActivity;
import com.example.test.roomDB.dao.LeadCallDao;
import com.example.test.roomDB.dao.NotSpokeToCustomerDao;
import com.example.test.roomDB.database.LeadListDB;
import com.example.test.roomDB.model.LeadCallModelRoom;
import com.example.test.roomDB.model.NotSpokeToCustomerRoomModel;
import com.example.test.schedule_flow.calls_for_the_day.adapter.CallsForTheDayAdapter;

import java.util.ArrayList;
import java.util.Map;

public class LoanCollectionAdapter extends RecyclerView.Adapter<LoanCollectionAdapter.MyViewHolderClass> {


    private Location currentLocation;
    public static String LoanCollectionAdapter_Distance ="0.0"; //initial value
    public static String LoanCollectionAdapter_dataSetId ="";

    public LoanCollectionAdapter() {
        //to use dataSetId in DetailsOfCustomerViewModel
    }

    ArrayList<LoanCollectionListResponseModel> loanCollectionListResponseModelArrayList;

    public LoanCollectionAdapter(ArrayList<LoanCollectionListResponseModel> loanCollectionListResponseModelArrayList, Location location) {
        this.loanCollectionListResponseModelArrayList = loanCollectionListResponseModelArrayList;
        this.currentLocation = location;
    }

    @NonNull
    @Override
    public MyViewHolderClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ItemLoanCollectionBinding view = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_loan_collection,parent,false);
        return new MyViewHolderClass(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderClass holder, int position) {


        LoanCollectionListResponseModel a = loanCollectionListResponseModelArrayList.get(position);
        Context context = holder.itemView.getContext();

        if(a.getMemberName()!=null){
            holder.binding.txtName.setText(a.getMemberName());
        }

//        if(a.getLocation()!=null){
//            holder.binding.txtLocation.setText(a.getLocation());
//        }
        holder.binding.txtLocation.setText("");
        if(a.getDistance()!=null){
            holder.binding.txtDistance.setText(String.valueOf(a.getDistance()));
        }

        if(a.getActionStatus()!=null){

            holder.binding.txtStatus.setText(a.getActionStatus());

            //set Colors for Status
            if(a.getActionStatus().toLowerCase().contains("pending")){
                holder.binding.txtStatus.setTextColor(ContextCompat.getColor(holder.itemView.getContext(),R.color.chilliRed) );
            }

            if(a.getActionStatus().toLowerCase().contains("in-process")){
                holder.binding.txtStatus.setTextColor(ContextCompat.getColor(holder.itemView.getContext(),R.color.blueButton) );
            }

            if(a.getActionStatus().toLowerCase().contains("re_assigned")){
                holder.binding.txtStatus.setTextColor(ContextCompat.getColor(holder.itemView.getContext(),R.color.purple) );
            }

            if(a.getActionStatus().toLowerCase().contains("complete")){
                holder.binding.txtStatus.setTextColor(ContextCompat.getColor(holder.itemView.getContext(),R.color.green) );
                holder.binding.ivLoanCollectionAttempt.setVisibility(View.INVISIBLE); // hide Attempt No. when Status:Complete
            }

        }

//        holder.binding.txtScheduledTime.setText(a.getScheduleDateTime().toString()); //Note: in API Response Scheduled Time is null

        if(a.getMobileNumber()!=null){
         holder.binding.txtMobileNumber.setText(a.getMobileNumber());
        }
        // to set empty if Mobile Number is null //This else block is needed
        else{
            holder.binding.txtMobileNumber.setText("");
        }

        if(a.getPinCode()!=null){
            holder.binding.txtPinCode.setText(a.getPinCode());
        }



        //opens Google Maps
        holder.binding.ivMap.setOnClickListener(v->{

            //will Open GoogleMaps in app when Status is Pending
            if(a.getActionStatus().toLowerCase().contains("pending")){

                System.out.println("Here LoanCollectionAdapter ivMap Location msg");
                //check if Location Turned On
                if(!Global.isLocationEnabled(context) || !Global.isBackgroundLocationAccessEnabled((Activity) context)){
                    Global.showToast(context, "Please Turn Location On");
                    // Open location settings
               /* Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                ((Activity) context).startActivityForResult(intent, LoanCollectionActivity.LocationRequestCode);*/
                }

                else if(Global.isLocationEnabled(context) && Global.isBackgroundLocationAccessEnabled((Activity) context)) {

                if(null != currentLocation) {
                    Intent googleMapsIntent = new Intent(context, GoogleMapsActivity.class);
                    googleMapsIntent.putExtra("longitude",currentLocation.getLongitude());
                    googleMapsIntent.putExtra("latitude", currentLocation.getLatitude());
                    googleMapsIntent.putExtra("isFromLoanCollectionAdapter", "isFromLoanCollectionAdapter");
                    googleMapsIntent.putExtra("isFromLoanCollectionAdapter_ivMap","isFromLoanCollectionAdapter_ivMap");
                    googleMapsIntent.putExtra("dataSetId",String.valueOf(a.getDataSetId()));
                    context.startActivity(googleMapsIntent);
                }
                // if User Turns Location Off
                else if (null==currentLocation){
                    currentLocation = Global.getDeviceLocation(context);
                    Global.showToast(context,context.getString(R.string.getting_device_location));
                }
                else{
                    Global.showToast(context, context.getString(R.string.unable_to_get_device_location));
                }

            }

            }

        });


        holder.itemView.setOnClickListener(v->{
            System.out.println("Here LoanCollectionAdapter dataSetId:"+a.getDataSetId().toString());

            Global.saveStringInSharedPref(context,"isFromCallsForTheDayAdapter","false"); //to goto NPA Flow
            CallsForTheDayAdapter.isFromCallsForTheDayAdapter = null; // to Reset CallsForTheDayFlow  & GOTO NPA flow
            System.out.println("Here LoanCollectionAdapter isFromCallsForTheDayAdapter:"+ CallsForTheDayAdapter.isFromCallsForTheDayAdapter);

            //DetailsOfCustomer Only visible if Status is Pending
            if(a.getActionStatus().toLowerCase().contains("pending")){

                LoanCollectionActivity.LoanCollectionLayoutAdapterPosition = holder.getAdapterPosition();
                Global.saveStringInSharedPref(context,"LoanCollectionLayoutAdapterPosition",String.valueOf(holder.getAdapterPosition()));
                System.out.println("Here LoanCollectionAdapter Position:"+holder.getAdapterPosition());

                //Use LoanCollectionAdapter_Distance value in DetailsOfCustomerAdapter beside Pincode
                if(a.getDistance()!=null && !String.valueOf(a.getDistance()).contentEquals("0.0")){
                    LoanCollectionAdapter_Distance = String.valueOf(a.getDistance());
                    System.out.println("Here LoanCollectionAdapter_Distance: "+LoanCollectionAdapter_Distance);
                    Global.saveStringInSharedPref(context,"formattedDistanceInKm", LoanCollectionAdapter.LoanCollectionAdapter_Distance);
                }


                //if lat long not null , send to DetailsOfCustomerAdapter ,
                // on clicking Navigate button(beside Capture button)  navigate to  GoogleMaps
                if(  (null!= a.getLattitute() && null!= a.getLongitute() ) && null!=a.getDataSetId()) {
                    System.out.println("Here Latitude:"+a.getLattitute()+" & Longitude:"+a.getLongitute());
                    //convert BigDecimal corresponding to double value
                    DetailsOfCustomerAdapter.latitudeFromLoanCollectionResponse = a.getLattitute().doubleValue();
                    DetailsOfCustomerAdapter.longitudeFromLoanCollectionResponse = a.getLongitute().doubleValue();
                    DetailsOfCustomerAdapter.dataSetId = a.getDataSetId().toString(); // to pass in Save Location API

                    //Save LatLong for Navigate Button in DetailsOfCustomerAdapter Also in DetailsOfCustomerActivity onResume()
                    Global.saveStringInSharedPref(context,"latitudeFromLoanCollectionAdapter",String.valueOf(a.getLattitute()));
                    Global.saveStringInSharedPref(context,"longitudeFromLoanCollectionAdapter",String.valueOf(a.getLongitute()));

                }


                //on Item Click save Name of Member
                Global.saveStringInSharedPref(context,"FullNameFromAdapter",String.valueOf(a.getMemberName()));
                MainActivity3API.showCallIcon = false; // //from Visits For The Day Flow to be True Else False
                LoanCollectionAdapter_dataSetId = a.getDataSetId().toString();
                DetailsOfCustomerAdapter.dataSetId = a.getDataSetId().toString(); // store dataSetId for Saving Alternate No. , Saving Location of Customer along with LatLong,Distance in DetailsOfCustomerAdapter
                Intent i = new Intent(context, DetailsOfCustomerActivity.class);
                i.putExtra("dataSetId", DetailsOfCustomerAdapter.dataSetId);
                context.startActivity(i);

                // to use in DetailsOfCustomerAdapter on Capture Button click to updateLocation in case dataSetId goes null
                //in case where coming Back from GoogleMaps App and again clicking Capture Button
                //in getDistanceBetweenMarkerAndUser()
                Global.saveStringInSharedPref(context,"dataSetId",String.valueOf(a.getDataSetId()));
            }

            /*
            Global.saveStringInSharedPref(context,"FullNameFromAdapter",String.valueOf(a.getMemberName()));

            String dataSetId = a.getDataSetId().toString();
            Intent i = new Intent(context, DetailsOfCustomerActivity.class);
            i.putExtra("dataSetId",dataSetId);
            context.startActivity(i);
*/
        });

        //for Status , Navigate to StatusOfCustomerActivity
        holder.binding.ivStatusInfo.setOnClickListener(v->{
            String dataSetId = a.getDataSetId().toString();
            Intent i = new Intent(context, StatusOfCustomerActivity.class);
            i.putExtra("dataSetId",dataSetId);
            i.putExtra("isFromLoanCollectionAdapterToStatusOfCustomerActivity","isFromLoanCollectionAdapterToStatusOfCustomerActivity");
            context.startActivity(i);
        });



        //to Display Hand Gestures for Not Spoke To The Customer dataSetId only for PENDING Status from RoomDB

        if(null!= a.getActionStatus() && a.getActionStatus().toLowerCase().contains("pending")){

            LeadCallDao leadCallDao = LeadListDB.getInstance(context).leadCallDao();

            int callCount =  leadCallDao.getCallCountUsingDataSetId(String.valueOf(a.getDataSetId()));

            switch (callCount){

                case 0:  holder.binding.ivLoanCollectionAttempt.setVisibility(View.INVISIBLE);
                    break;
                case 1:holder.binding.ivLoanCollectionAttempt.setImageResource(R.drawable.attempttwo);
                    break;
                case 2 : holder.binding.ivLoanCollectionAttempt.setImageResource(R.drawable.attemptthree);
                    break;
                case 3: holder.binding.ivLoanCollectionAttempt.setImageResource(R.drawable.attemptfour);
                    break;
                case 4 : holder.binding.ivLoanCollectionAttempt.setImageResource(R.drawable.attemptfiveblue);
                    break;
            }

        }


    }

    @Override
    public int getItemCount() {
        return loanCollectionListResponseModelArrayList.size();
    }

    @Override
    public void onViewAttachedToWindow(@NonNull MyViewHolderClass holder) {
        super.onViewAttachedToWindow(holder);

        holder.setIsRecyclable(false); // for hand gestures to not disappear

        /*if(GoogleMapsActivity.saveDistanceBoolean && GoogleMapsActivity.isSaveButtonClicked){
            holder.setIsRecyclable(true);
        }
        else{
            holder.setIsRecyclable(false);
        }
*/
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull MyViewHolderClass holder) {
        super.onViewDetachedFromWindow(holder);

        holder.setIsRecyclable(false); // for hand gestures to not disappear

       /* if(GoogleMapsActivity.saveDistanceBoolean && GoogleMapsActivity.isSaveButtonClicked){
            holder.setIsRecyclable(true); // for distance in Km to keep fetching New distance everytime
        }
        else{
            holder.setIsRecyclable(false);
        }
*/
    }


    @SuppressLint("NotifyDataSetChanged")
    public ArrayList setData(ArrayList<LoanCollectionListResponseModel> data)  {
        if (data.isEmpty()) {
            loanCollectionListResponseModelArrayList =  new ArrayList();
        }
        loanCollectionListResponseModelArrayList = data;
        notifyDataSetChanged();

        return loanCollectionListResponseModelArrayList;
    }


    class MyViewHolderClass extends RecyclerView.ViewHolder {

        private ItemLoanCollectionBinding binding;

        public MyViewHolderClass(ItemLoanCollectionBinding binding) {
            super(binding.getRoot());
            this.binding =binding;
        }

    }

}
