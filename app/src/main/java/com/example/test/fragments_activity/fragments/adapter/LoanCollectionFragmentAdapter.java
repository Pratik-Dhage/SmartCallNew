package com.example.test.fragments_activity.fragments.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test.R;
import com.example.test.databinding.ItemLoanCollectionBinding;
import com.example.test.fragments_activity.CustomerDetailsActivity;
import com.example.test.npa_flow.WebViewActivity;
import com.example.test.npa_flow.details_of_customer.DetailsOfCustomerActivity;
import com.example.test.npa_flow.loan_collection.LoanCollectionListResponseModel;
import com.example.test.npa_flow.loan_collection.adapter.LoanCollectionAdapter;

import java.util.ArrayList;

public class LoanCollectionFragmentAdapter extends RecyclerView.Adapter<LoanCollectionFragmentAdapter.MyViewHolderClass> {


    public LoanCollectionFragmentAdapter() {
        //for dataSetId
    }

    ArrayList<LoanCollectionListResponseModel> loanCollectionListResponseModelArrayList;

    public LoanCollectionFragmentAdapter(ArrayList<LoanCollectionListResponseModel> loanCollectionListResponseModelArrayList) {
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
        holder.binding.txtStatus.setText(a.getActionStatus());
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

            String dataSetId = a.getDataSetId().toString();
            Intent i = new Intent(context, CustomerDetailsActivity.class);
            i.putExtra("dataSetId",dataSetId);
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
