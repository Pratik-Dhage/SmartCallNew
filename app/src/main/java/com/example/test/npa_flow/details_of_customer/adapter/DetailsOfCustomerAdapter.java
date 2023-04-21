package com.example.test.npa_flow.details_of_customer.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test.R;
import com.example.test.databinding.ItemDetailsOfCustomerBinding;
import com.example.test.databinding.ItemDpdBinding;
import com.example.test.npa_flow.details_of_customer.DetailsOfCustomer_ResponseModel;
import com.example.test.npa_flow.dpd.DPD_ResponseModel;
import com.example.test.npa_flow.dpd.adapter.DPD_Adapter;

import java.util.ArrayList;
import java.util.Objects;

public class DetailsOfCustomerAdapter extends RecyclerView.Adapter<DetailsOfCustomerAdapter.MyViewHolderClass> {

    ArrayList<DetailsOfCustomer_ResponseModel> detailsOfCustomer_responseModelArrayList;

    public DetailsOfCustomerAdapter(ArrayList<DetailsOfCustomer_ResponseModel> detailsOfCustomer_responseModelArrayList) {
        this.detailsOfCustomer_responseModelArrayList = detailsOfCustomer_responseModelArrayList;
    }

    @NonNull
    @Override
    public MyViewHolderClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ItemDetailsOfCustomerBinding view = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_details_of_customer,parent,false);
        return new MyViewHolderClass(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolderClass holder, int position) {

        DetailsOfCustomer_ResponseModel a = detailsOfCustomer_responseModelArrayList.get(position);
        Context context = holder.itemView.getContext();

        holder.binding.labelDetailName.setText(a.getLable());
        holder.binding.txtDetailName.setText(a.getValue());

        //for separation line between Personal and Account Details
        if(Objects.equals(a.getLable(), "Father's Name")){
            holder.binding.viewLine.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return detailsOfCustomer_responseModelArrayList.size();
    }


    @SuppressLint("NotifyDataSetChanged")
    public ArrayList setData(ArrayList<DetailsOfCustomer_ResponseModel> data)  {
        if (data.isEmpty()) {
            detailsOfCustomer_responseModelArrayList =  new ArrayList();
        }
        detailsOfCustomer_responseModelArrayList = data;
        notifyDataSetChanged();

        return detailsOfCustomer_responseModelArrayList;
    }



    class MyViewHolderClass extends RecyclerView.ViewHolder {

        private ItemDetailsOfCustomerBinding binding;

        public MyViewHolderClass(ItemDetailsOfCustomerBinding binding) {
            super(binding.getRoot());
            this.binding =binding;
        }

    }

}
