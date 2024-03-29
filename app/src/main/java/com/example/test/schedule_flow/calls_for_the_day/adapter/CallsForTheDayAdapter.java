package com.example.test.schedule_flow.calls_for_the_day.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test.R;
import com.example.test.databinding.ItemCallsForTheDayBinding;
import com.example.test.google_maps.GoogleMapsActivity;
import com.example.test.google_maps.MapFragment;
import com.example.test.helper_classes.Global;
import com.example.test.npa_flow.CallDetailOfCustomerActivity;
import com.example.test.npa_flow.NotSpokeToCustomerActivity;
import com.example.test.npa_flow.details_of_customer.DetailsOfCustomerActivity;
import com.example.test.npa_flow.loan_collection.adapter.LoanCollectionAdapter;
import com.example.test.npa_flow.status_of_customer.StatusOfCustomerActivity;
import com.example.test.roomDB.dao.LeadCallDao;
import com.example.test.roomDB.dao.NotSpokeToCustomerDao;
import com.example.test.roomDB.database.LeadListDB;
import com.example.test.roomDB.model.NotSpokeToCustomerRoomModel;
import com.example.test.schedule_flow.calls_for_the_day.model.CallsForTheDayResponseModel;
import com.example.test.schedule_flow.visits_for_the_day.model.VisitsForTheDayResponseModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CallsForTheDayAdapter extends RecyclerView.Adapter<CallsForTheDayAdapter.MyViewHolderClass> {

    ArrayList<CallsForTheDayResponseModel> callsForTheDayResponseModelArrayList;
    public static String isFromCallsForTheDayAdapter;

    public CallsForTheDayAdapter(ArrayList<CallsForTheDayResponseModel> callsForTheDayResponseModelArrayList) {
        this.callsForTheDayResponseModelArrayList = callsForTheDayResponseModelArrayList;
    }

    @NonNull
    @Override
    public MyViewHolderClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCallsForTheDayBinding view =  DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_calls_for_the_day,parent,false);
        return new MyViewHolderClass(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderClass holder, int position) {

        CallsForTheDayResponseModel a = callsForTheDayResponseModelArrayList.get(position);
        Context context = holder.itemView.getContext();

        if(a.getMemberName()!=null){
            holder.binding.txtName.setText(a.getMemberName());
        }

        if(a.getDistance()!=null){
            holder.binding.txtDistance.setText(String.valueOf(a.getDistance()));
        }

        if(a.getLocation()!=null){
            holder.binding.txtLocation.setText(a.getLocation());
        }

        if(a.getActionStatus()!=null){
            holder.binding.txtStatus.setText(String.valueOf(a.getActionStatus()));

            //set Colors for Status
            if(a.getActionStatus().toLowerCase().contains("pending")){
                holder.binding.txtStatus.setTextColor(ContextCompat.getColor(holder.itemView.getContext(),R.color.chilliRed) );
            }
        }

        if(a.getMobileNumber()!=null){
            holder.binding.txtMobileNumber.setText(a.getMobileNumber());
        }

        if(a.getPinCode()!=null){
            holder.binding.txtPinCode.setText(a.getPinCode());
        }


        //for ScheduleTime
        if(a.getScheduleDateTime()!=null){

            // Split the datetime string into date and time parts
            String[] parts = String.valueOf(a.getScheduleDateTime()).split(" ");
            String timeString = parts[1]; // Extract the time part
            String am_or_pm = parts[2];

            holder.binding.txtScheduleTime.setText(timeString+" "+am_or_pm);

        //  SimpleDateFormat object to parse the time string
          /*  SimpleDateFormat inputFormat = new SimpleDateFormat("HH:mm");

            try {
                Date time = inputFormat.parse(timeString); // Parse the time string
                SimpleDateFormat outputFormat = new SimpleDateFormat("hh:mm a"); // Format the time

                assert time != null; //time can never be null
                String formattedTime = outputFormat.format(time);

                holder.binding.txtScheduleTime.setText(formattedTime);

            } catch (ParseException e) {
                System.out.println("Here Call ScheduleTime Exception :"+e.getLocalizedMessage());
                e.printStackTrace();
            }
*/


        }

        //opens Google Maps
        holder.binding.ivMap.setOnClickListener(v->{

            if(a.getLattitute()!=null && a.getLongitute()!=null ){

                String latitude = String.valueOf(a.getLattitute());
                String longitude = String.valueOf(a.getLongitute());

                if(!Global.isLocationEnabled(context) || !Global.isBackgroundLocationAccessEnabled((Activity) context)){
                    Global.showToast(context, "Please Turn Location On");
                }

                else if (Global.isLocationEnabled(context) && Global.isBackgroundLocationAccessEnabled((Activity) context) ){

                    Intent googleMapsIntent = new Intent(context, GoogleMapsActivity.class);
                    googleMapsIntent.putExtra("latitude_visitsForTheDay", Double.parseDouble(latitude));
                    googleMapsIntent.putExtra("longitude_visitsForTheDay", Double.parseDouble(longitude));
                    googleMapsIntent.putExtra("isFromVisitsForTheDayAdapter","isFromVisitsForTheDayAdapter");
                    googleMapsIntent.putExtra("dataSetId",String.valueOf(a.getDataSetId()));
                    context.startActivity(googleMapsIntent);


                }

            }

        });

        holder.itemView.setOnClickListener(v->{

            //DetailsOfCustomer Only visible if Status is Pending
            if(a.getActionStatus().toLowerCase().contains("pending")){

                Global.saveStringInSharedPref(context,"FullNameFromAdapter",String.valueOf(a.getMemberName()));

                //for using in VisitCompletionOfCustomerActivity and redirecting to Calls For The Day List
                isFromCallsForTheDayAdapter = "isFromCallsForTheDayAdapter";
                Global.saveStringInSharedPref(context,"isFromCallsForTheDayAdapter","isFromCallsForTheDayAdapter"); // for checking if User coming from CallsForTheDay Flow
                System.out.println("Here isFromCallsForTheDayAdapter: "+isFromCallsForTheDayAdapter);

                String dataSetId = a.getDataSetId().toString();
                Intent i = new Intent(context, DetailsOfCustomerActivity.class);
                i.putExtra("dataSetId",dataSetId);
                i.putExtra("isFromCallsForTheDayAdapter",isFromCallsForTheDayAdapter);
                context.startActivity(i);

                // to use in DetailsOfCustomerAdapter on Capture Button click to updateLocation in case dataSetId goes null
                //in case where coming Back from GoogleMaps App and again clicking Capture Button
                //in getDistanceBetweenMarkerAndUser()
                Global.saveStringInSharedPref(context,"dataSetId",String.valueOf(a.getDataSetId()));

                // to display in DetailsOfCustomerAdapter
                Global.saveStringInSharedPref(context,"formattedDistanceInKm",String.valueOf(a.getDistance()));
                LoanCollectionAdapter.LoanCollectionAdapter_Distance = String.valueOf(a.getDistance()); //it will only display if it is not 0.0km

                // for Navigate Button in DetailsOfCustomerAdapter , to navigate to GoogleMaps
                Global.saveStringInSharedPref(context,"latitudeFromLoanCollectionAdapter",String.valueOf(a.getLattitute()));
                Global.saveStringInSharedPref(context,"longitudeFromLoanCollectionAdapter",String.valueOf(a.getLongitute()));

            }
        });

        //for Status , Navigate to StatusOfCustomerActivity
        holder.binding.ivStatusInfo.setOnClickListener(v->{
            String dataSetId = a.getDataSetId().toString();
            Intent i = new Intent(context, StatusOfCustomerActivity.class);
            i.putExtra("dataSetId",dataSetId);
            context.startActivity(i);
        });



        //to Display Hand Gestures for Not Spoke To The Customer dataSetId only for PENDING Status from RoomDB

        if(null!= a.getActionStatus() && a.getActionStatus().toLowerCase().contains("pending")){

            LeadCallDao leadCallDao = LeadListDB.getInstance(context).leadCallDao();

            int callCount =  leadCallDao.getCallCountUsingDataSetId(String.valueOf(a.getDataSetId()));

            switch (callCount){

                case 0:  holder.binding.ivCallsAttempt.setVisibility(View.INVISIBLE);
                    break;
                case 1:holder.binding.ivCallsAttempt.setImageResource(R.drawable.attempttwo);
                    break;
                case 2 : holder.binding.ivCallsAttempt.setImageResource(R.drawable.attemptthree);
                    break;
                case 3: holder.binding.ivCallsAttempt.setImageResource(R.drawable.attemptfour);
                    break;
                case 4 : holder.binding.ivCallsAttempt.setImageResource(R.drawable.attemptfiveblue);
                    break;
            }

        }


    }

    @Override
    public int getItemCount() {
        return callsForTheDayResponseModelArrayList.size();
    }

    @Override
    public void onViewAttachedToWindow(@NonNull MyViewHolderClass holder) {
        super.onViewAttachedToWindow(holder);

        holder.setIsRecyclable(false); // for hand gestures to not disappear

       /* if(GoogleMapsActivity.saveDistanceBoolean && GoogleMapsActivity.isSaveButtonClicked){
            holder.setIsRecyclable(true);
        }
        else{
            holder.setIsRecyclable(false);
        }*/
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull MyViewHolderClass holder) {
        super.onViewDetachedFromWindow(holder);

        holder.setIsRecyclable(false); // for hand gestures to not disappear

        /*if(GoogleMapsActivity.saveDistanceBoolean && GoogleMapsActivity.isSaveButtonClicked){
            holder.setIsRecyclable(true); // for distance in Km to keep fetching New distance everytime
        }
        else{
            holder.setIsRecyclable(false);
        }*/
    }

    @SuppressLint("NotifyDataSetChanged")
    public ArrayList setData(ArrayList<CallsForTheDayResponseModel> data)  {
        if (data.isEmpty()) {
            callsForTheDayResponseModelArrayList =  new ArrayList();
        }
        callsForTheDayResponseModelArrayList = data;
        notifyDataSetChanged();

        return callsForTheDayResponseModelArrayList;
    }

    class MyViewHolderClass extends RecyclerView.ViewHolder {

        private ItemCallsForTheDayBinding binding;

        public MyViewHolderClass(ItemCallsForTheDayBinding binding) {
            super(binding.getRoot());
            this.binding =binding;
        }

    }

}
