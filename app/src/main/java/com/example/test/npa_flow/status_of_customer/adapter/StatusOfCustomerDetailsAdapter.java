package com.example.test.npa_flow.status_of_customer.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test.R;
import com.example.test.databinding.ItemStatusDetailsOfCustomerBinding;
import com.example.test.helper_classes.Global;
import com.example.test.npa_flow.dpd.DPD_ResponseModel;
import com.example.test.npa_flow.status_of_customer.model.Activity;

import java.util.ArrayList;

public class StatusOfCustomerDetailsAdapter extends RecyclerView.Adapter<StatusOfCustomerDetailsAdapter.MyViewHolderClass> {

      ArrayList<Activity> activityArrayList;

    public StatusOfCustomerDetailsAdapter(ArrayList<Activity> activityArrayList) {
        this.activityArrayList = activityArrayList;
    }

    @NonNull
    @Override
    public MyViewHolderClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ItemStatusDetailsOfCustomerBinding view = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_status_details_of_customer,parent,false);
        return new MyViewHolderClass(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderClass holder, int position) {

        Activity a = activityArrayList.get(position);


        Context context = holder.itemView.getContext();

        String userName = Global.getStringFromSharedPref(context,"userName");
        holder.binding.txtUserName.setText(userName);

        // format -> 25th May 23,Friday,5:00pm COMPLETE ShivKumar
        holder.binding.txtStatusDetailsOfCustomer.setText(a.getActivityDate()+","+a.getDay()+","+a.getActivityTime()+" "+a.getActivityStatus());

    }

    @Override
    public int getItemCount() {
        return activityArrayList.size();
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
    public ArrayList setData(ArrayList<Activity> data)  {
        if (data.isEmpty()) {
            activityArrayList =  new ArrayList();
        }
        activityArrayList = data;
        notifyDataSetChanged();

        return activityArrayList;
    }


    class MyViewHolderClass extends RecyclerView.ViewHolder {

        private ItemStatusDetailsOfCustomerBinding binding;

        public MyViewHolderClass(ItemStatusDetailsOfCustomerBinding binding) {
            super(binding.getRoot());
            this.binding =binding;
        }

    }

}
