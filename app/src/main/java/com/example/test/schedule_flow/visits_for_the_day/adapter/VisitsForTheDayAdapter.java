package com.example.test.schedule_flow.visits_for_the_day.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test.R;
import com.example.test.databinding.ItemStatusDetailsOfCustomerBinding;
import com.example.test.databinding.ItemVisitsForTheDayBinding;
import com.example.test.fragments_activity.CustomerDetailsActivity;
import com.example.test.google_maps.MapFragment;
import com.example.test.helper_classes.Global;
import com.example.test.npa_flow.WebViewActivity;
import com.example.test.npa_flow.details_of_customer.DetailsOfCustomerActivity;
import com.example.test.npa_flow.status_of_customer.StatusOfCustomerActivity;
import com.example.test.npa_flow.status_of_customer.adapter.StatusOfCustomerDetailsAdapter;
import com.example.test.npa_flow.status_of_customer.model.Activity;
import com.example.test.schedule_flow.visits_for_the_day.model.VisitsForTheDayResponseModel;

import java.util.ArrayList;

public class VisitsForTheDayAdapter extends RecyclerView.Adapter<VisitsForTheDayAdapter.MyViewHolderClass> {


    ArrayList<VisitsForTheDayResponseModel> visitsForTheDayResponseModelArrayList;

    public VisitsForTheDayAdapter(ArrayList<VisitsForTheDayResponseModel> visitsForTheDayResponseModelArrayList) {
        this.visitsForTheDayResponseModelArrayList = visitsForTheDayResponseModelArrayList;
    }

    @NonNull
    @Override
    public MyViewHolderClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemVisitsForTheDayBinding view = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_visits_for_the_day,parent,false);
        return new MyViewHolderClass(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderClass holder, int position) {

        VisitsForTheDayResponseModel a = visitsForTheDayResponseModelArrayList.get(position);
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
        if(a.getScheduleDateTime()!=null) {

            // Split the datetime string into date and time parts
            String[] parts = String.valueOf(a.getScheduleDateTime()).split(" ");
            String timeString = parts[1]; // Extract the time part
            String am_or_pm = parts[2];

            holder.binding.txtScheduleTime.setText(timeString + " " + am_or_pm);

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

                Intent googleMapsIntent = new Intent(context, MapFragment.class);
                googleMapsIntent.putExtra("latitude_visitsForTheDay", Double.parseDouble(latitude));
                googleMapsIntent.putExtra("longitude_visitsForTheDay", Double.parseDouble(longitude));
                googleMapsIntent.putExtra("isFromVisitsForTheDayAdapter","isFromVisitsForTheDayAdapter");
                context.startActivity(googleMapsIntent);

            }

        });

          holder.itemView.setOnClickListener(v->{

              //DetailsOfCustomer Only visible if Status is Pending
              if(a.getActionStatus().toLowerCase().contains("pending")){

                  String dataSetId = a.getDataSetId().toString();
                  Intent i = new Intent(context, CustomerDetailsActivity.class);
                  i.putExtra("dataSetId",dataSetId);
                  context.startActivity(i);

              }
          });

        //for Status , Navigate to StatusOfCustomerActivity
        holder.binding.ivStatusInfo.setOnClickListener(v->{
            String dataSetId = a.getDataSetId().toString();
            Intent i = new Intent(context, StatusOfCustomerActivity.class);
            i.putExtra("dataSetId",dataSetId);
            context.startActivity(i);
        });


    }

    @Override
    public int getItemCount() {
        return visitsForTheDayResponseModelArrayList.size();
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
    public ArrayList setData(ArrayList<VisitsForTheDayResponseModel> data)  {
        if (data.isEmpty()) {
            visitsForTheDayResponseModelArrayList =  new ArrayList();
        }
        visitsForTheDayResponseModelArrayList = data;
        notifyDataSetChanged();

        return visitsForTheDayResponseModelArrayList;
    }


    class MyViewHolderClass extends RecyclerView.ViewHolder {

        private ItemVisitsForTheDayBinding binding;

        public MyViewHolderClass(ItemVisitsForTheDayBinding binding) {
            super(binding.getRoot());
            this.binding =binding;
        }

    }
}
