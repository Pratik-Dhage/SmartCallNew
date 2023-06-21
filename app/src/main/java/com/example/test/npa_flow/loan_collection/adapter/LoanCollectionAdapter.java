package com.example.test.npa_flow.loan_collection.adapter;

import android.annotation.SuppressLint;
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
import com.example.test.databinding.ItemLoanCollectionBinding;
import com.example.test.google_maps.GoogleMapsActivity;
import com.example.test.helper_classes.Global;
import com.example.test.main_dashboard.MainActivity3API;
import com.example.test.npa_flow.NotSpokeToCustomerActivity;
import com.example.test.npa_flow.WebViewActivity;
import com.example.test.npa_flow.details_of_customer.DetailsOfCustomerActivity;
import com.example.test.npa_flow.loan_collection.LoanCollectionListResponseModel;
import com.example.test.npa_flow.status_of_customer.StatusOfCustomerActivity;
import com.example.test.roomDB.dao.LeadCallDao;
import com.example.test.roomDB.database.LeadListDB;

import java.util.ArrayList;

public class LoanCollectionAdapter extends RecyclerView.Adapter<LoanCollectionAdapter.MyViewHolderClass> {



    public LoanCollectionAdapter() {
        //to use dataSetId in DetailsOfCustomerViewModel
    }

    ArrayList<LoanCollectionListResponseModel> loanCollectionListResponseModelArrayList;

    public LoanCollectionAdapter(ArrayList<LoanCollectionListResponseModel> loanCollectionListResponseModelArrayList) {
        this.loanCollectionListResponseModelArrayList = loanCollectionListResponseModelArrayList;
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

        if(a.getLocation()!=null){
            holder.binding.txtLocation.setText(a.getLocation());
        }

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

            if(a.getActionStatus().toLowerCase().contains("re-assigned")){
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

        if(a.getPinCode()!=null){
            holder.binding.txtPinCode.setText(a.getPinCode());
        }

        //opens Google Maps
        holder.binding.ivMap.setOnClickListener(v->{

            // Pass LatLong to Google Maps in WebViewActivity
             String latitude = a.getLattitute().toString();
             String longitude = a.getLongitute().toString();

            Intent googleMapsIntent = new Intent(context, GoogleMapsActivity.class);
            googleMapsIntent.putExtra("latitude",Double.parseDouble(latitude));
            googleMapsIntent.putExtra("longitude",Double.parseDouble(longitude));
            googleMapsIntent.putExtra("isFromLoanCollectionAdapter","isFromLoanCollectionAdapter");
            context.startActivity(googleMapsIntent);
        });


        holder.itemView.setOnClickListener(v->{

            //DetailsOfCustomer Only visible if Status is Pending
            if(a.getActionStatus().toLowerCase().contains("pending")){

                //on Item Click save Name of Member
                Global.saveStringInSharedPref(context,"FullNameFromAdapter",String.valueOf(a.getMemberName()));
                MainActivity3API.showCallIcon = false; // //from Visits For The Day Flow to be True Else False
                String dataSetId = a.getDataSetId().toString();
                Intent i = new Intent(context, DetailsOfCustomerActivity.class);
                i.putExtra("dataSetId",dataSetId);
                context.startActivity(i);
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
            context.startActivity(i);
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

            // Show Call Attempts(Hands) Only If Not Spoke To Customer is True(No Response/Busy And Not Reachable/Switched Off)
            if(FullNameFromAdapter.contains(String.valueOf(a.getMemberName())) && NotSpokeToCustomerActivity.notSpokeToCustomer==true){


                switch (callCount){

                    case 0:  holder.binding.ivLoanCollectionAttempt.setVisibility(View.INVISIBLE);
                        break;
                    case 1:holder.binding.ivLoanCollectionAttempt.setImageResource(R.drawable.attempttwo);
                        break;
                    case 2 : holder.binding.ivLoanCollectionAttempt.setImageResource(R.drawable.attemptthree);
                        break;

                }
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
        holder.setIsRecyclable(true);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull MyViewHolderClass holder) {
        super.onViewDetachedFromWindow(holder);
        holder.setIsRecyclable(false); // to prevent ImageView from being disappeared when scrolled upwards
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
