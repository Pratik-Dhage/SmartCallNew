package com.example.test.schedule_flow.visits_for_the_day.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import com.example.test.google_maps.GoogleMapsActivity;
import com.example.test.google_maps.MapFragment;
import com.example.test.helper_classes.Global;
import com.example.test.npa_flow.WebViewActivity;
import com.example.test.npa_flow.details_of_customer.DetailsOfCustomerActivity;
import com.example.test.npa_flow.loan_collection.adapter.LoanCollectionAdapter;
import com.example.test.npa_flow.status_of_customer.StatusOfCustomerActivity;
import com.example.test.npa_flow.status_of_customer.adapter.StatusOfCustomerDetailsAdapter;
import com.example.test.schedule_flow.visits_for_the_day.model.VisitsForTheDayResponseModel;

import java.util.ArrayList;

public class VisitsForTheDayAdapter extends RecyclerView.Adapter<VisitsForTheDayAdapter.MyViewHolderClass> {


    ArrayList<VisitsForTheDayResponseModel> visitsForTheDayResponseModelArrayList;
    public static boolean showNearByCustomerButton ;

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

              showNearByCustomerButton = true; //Only for Visits For The Day Flow VisitNearByCustomer Button is Visible in NearByCustomerActivity

              //DetailsOfCustomer Only visible if Status is Pending
              if(a.getActionStatus().toLowerCase().contains("pending")){

                  System.out.println("Here VisitsForTheDayAdapter dataSetId:"+a.getDataSetId().toString());
                  String dataSetId = a.getDataSetId().toString();
                  LoanCollectionAdapter.LoanCollectionAdapter_dataSetId = a.getDataSetId().toString();
                  Intent i = new Intent(context, CustomerDetailsActivity.class);
                  i.putExtra("dataSetId",LoanCollectionAdapter.LoanCollectionAdapter_dataSetId); // for Saving UpdatedLocation on Capture Button click
                  context.startActivity(i);

                  // to use in DetailsOfCustomerAdapter on Capture Button click to updateLocation in case dataSetId goes null
                  //in case where coming Back from GoogleMaps App and again clicking Capture Button
                  //in getDistanceBetweenMarkerAndUser()
                  Global.saveStringInSharedPref(context,"dataSetId",String.valueOf(a.getDataSetId()));

                  // to display in DetailsOfCustomerAdapter
                  Global.saveStringInSharedPref(context,"formattedDistanceInKm",String.valueOf(a.getDistance()));

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
        holder.setIsRecyclable(true); //for Distance
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
