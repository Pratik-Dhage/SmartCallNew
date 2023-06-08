package com.example.test.schedule_flow.calls_for_the_day.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test.R;
import com.example.test.databinding.ItemCallsForTheDayBinding;
import com.example.test.google_maps.GoogleMapsActivity;
import com.example.test.google_maps.MapFragment;
import com.example.test.helper_classes.Global;
import com.example.test.npa_flow.details_of_customer.DetailsOfCustomerActivity;
import com.example.test.roomDB.dao.LeadCallDao;
import com.example.test.roomDB.database.LeadListDB;
import com.example.test.schedule_flow.calls_for_the_day.model.CallsForTheDayResponseModel;
import com.example.test.schedule_flow.visits_for_the_day.model.VisitsForTheDayResponseModel;

import java.util.ArrayList;

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
        }

        //opens Google Maps
        holder.binding.ivMap.setOnClickListener(v->{

            if(a.getLattitute()!=null && a.getLongitute()!=null ){

                String latitude = String.valueOf(a.getLattitute());
                String longitude = String.valueOf(a.getLongitute());

                Intent googleMapsIntent = new Intent(context, GoogleMapsActivity.class);
                googleMapsIntent.putExtra("latitude_callsForTheDay", Double.parseDouble(latitude));
                googleMapsIntent.putExtra("longitude_callsForTheDay", Double.parseDouble(longitude));
                googleMapsIntent.putExtra("isFromCallsForTheDayAdapter","isFromCallsForTheDayAdapter");
                context.startActivity(googleMapsIntent);

            }

        });

        holder.itemView.setOnClickListener(v->{

            //DetailsOfCustomer Only visible if Status is Pending
            if(a.getActionStatus().toLowerCase().contains("pending")){

                Global.saveStringInSharedPref(context,"FullNameFromAdapter",String.valueOf(a.getMemberName()));

                //for using in VisitCompletionOfCustomerActivity and redirecting to Calls For The Day List
                isFromCallsForTheDayAdapter = "isFromCallsForTheDayAdapter";
                System.out.println("Here isFromCallsForTheDayAdapter: "+isFromCallsForTheDayAdapter);

                String dataSetId = a.getDataSetId().toString();
                Intent i = new Intent(context, DetailsOfCustomerActivity.class);
                i.putExtra("dataSetId",dataSetId);
                context.startActivity(i);

            }
        });


        //for setting Call Attempts ImageView to Selected Item(Member) for Calling
        LeadCallDao leadCallDao = LeadListDB.getInstance(context).leadCallDao();

        // get mobile Number and Name from DetailsOfCustomerActivity
        if(DetailsOfCustomerActivity.Mobile_Number!=null ){

            String phoneNumber=DetailsOfCustomerActivity.Mobile_Number; // get Phone Number from DetailsOfCustomerActivity
            String first_Name = DetailsOfCustomerActivity.FullName;

            if(leadCallDao.getCallCountUsingPhoneNumber(phoneNumber)>2){
                leadCallDao.UpdateLeadCalls(0,phoneNumber); // if leadCallCount >2 make it back to zero
            }

            //   int callCount =  leadCallDao.getCallCountUsingPhoneNumber(phoneNumber);
            int callCount =  leadCallDao.getCallCountUsingPhoneNumber(phoneNumber);

            // match Name coming from api list  and name stored in SharedPreferences in this Adapter on ItemView Click
            String FullNameFromAdapter = Global.getStringFromSharedPref(context,"FullNameFromAdapter");

            if(FullNameFromAdapter.contains(String.valueOf(a.getMemberName()))){


                switch (callCount){

                    case 0:  holder.binding.ivCallsAttempt.setVisibility(View.INVISIBLE);
                        break;
                    case 1:holder.binding.ivCallsAttempt.setImageResource(R.drawable.attempttwo);
                        break;
                    case 2 : holder.binding.ivCallsAttempt.setImageResource(R.drawable.attemptthree);
                        break;

                }
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
        holder.setIsRecyclable(true);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull MyViewHolderClass holder) {
        super.onViewDetachedFromWindow(holder);
        holder.setIsRecyclable(false);
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
