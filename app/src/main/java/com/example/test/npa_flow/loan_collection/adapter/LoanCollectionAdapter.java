package com.example.test.npa_flow.loan_collection.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test.R;
import com.example.test.databinding.ItemDpdBinding;
import com.example.test.databinding.ItemLoanCollectionBinding;
import com.example.test.npa_flow.DetailsOfCustomerActivity;
import com.example.test.npa_flow.WebViewActivity;
import com.example.test.npa_flow.dpd.DPD_ResponseModel;
import com.example.test.npa_flow.dpd.adapter.DPD_Adapter;
import com.example.test.npa_flow.loan_collection.LoanCollectionListResponseModel;

import java.util.ArrayList;
import java.util.List;

public class LoanCollectionAdapter extends RecyclerView.Adapter<LoanCollectionAdapter.MyViewHolderClass> {

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


        LoanCollectionListResponseModel a = (LoanCollectionListResponseModel) loanCollectionListResponseModelArrayList.get(position);
        Context context = holder.itemView.getContext();

        holder.binding.txtName.setText(a.getMemberName());
        holder.binding.txtLocation.setText(a.getLocation());
        holder.binding.txtDistance.setText(a.getDistance().toString());
//        holder.binding.txtScheduledTime.setText(a.getScheduleDateTime().toString()); //Note: in API Response Scheduled Time is null

        //opens Google Maps
        holder.binding.ivMap.setOnClickListener(v->{

            // Pass LatLong to Google Maps in WebViewActivity
             String latitude = a.getLattitute().toString();
             String longitude = a.getLongitute().toString();

            Intent googleMapsIntent = new Intent(context, WebViewActivity.class);
            googleMapsIntent.putExtra("latitude",latitude);
            googleMapsIntent.putExtra("longitude",longitude);
            context.startActivity(googleMapsIntent);
        });

        holder.itemView.setOnClickListener(v->{
            Intent i = new Intent(context, DetailsOfCustomerActivity.class);
            context.startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return loanCollectionListResponseModelArrayList.size();
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
