package com.example.test.npa_flow.nearby_customer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test.R;
import com.example.test.databinding.ItemLoanCollectionBinding;
import com.example.test.fragments_activity.CustomerDetailsActivity;
import com.example.test.main_dashboard.MainActivity3API;
import com.example.test.npa_flow.loan_collection.LoanCollectionListResponseModel;
import com.example.test.npa_flow.loan_collection.adapter.LoanCollectionAdapter;
import com.example.test.npa_flow.status_of_customer.StatusOfCustomerActivity;
import com.example.test.schedule_flow.visits_for_the_day.adapter.VisitsForTheDayAdapter;

import java.util.ArrayList;


// Using Model Class & Item from loanCollectionAdapter
public class NearByCustomerListAdapter extends RecyclerView.Adapter<NearByCustomerListAdapter.MyViewHolderClass> {

    private Location currentLocation;
    ArrayList<LoanCollectionListResponseModel> loanCollectionListResponseModelArrayList;
    public static boolean isFromNearByCustomerAdapter = true;

    public NearByCustomerListAdapter(ArrayList<LoanCollectionListResponseModel> loanCollectionListResponseModelArrayList, Location location) {
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

            if(a.getActionStatus().toLowerCase().contains("re-assigned")){
                holder.binding.txtStatus.setTextColor(ContextCompat.getColor(holder.itemView.getContext(),R.color.purple) );
            }

            if(a.getActionStatus().toLowerCase().contains("complete")){
                holder.binding.txtStatus.setTextColor(ContextCompat.getColor(holder.itemView.getContext(),R.color.green) );
                holder.binding.ivLoanCollectionAttempt.setVisibility(View.INVISIBLE); // hide Attempt No. when Status:Complete
            }

        }

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

        holder.itemView.setOnClickListener(v->{

            VisitsForTheDayAdapter.showNearByCustomerButton = true; //Only for Visits
            MainActivity3API.showCallIcon = true; //Only for Visits
            isFromNearByCustomerAdapter = true; // for Back To Member List Button in NearByCustomerActivity

            //DetailsOfCustomer Only visible if Status is Pending
            if(a.getActionStatus().toLowerCase().contains("pending")){

                Intent i = new Intent(context, CustomerDetailsActivity.class);
                String dataSetId = a.getDataSetId().toString();
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
        holder.setIsRecyclable(true);
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
