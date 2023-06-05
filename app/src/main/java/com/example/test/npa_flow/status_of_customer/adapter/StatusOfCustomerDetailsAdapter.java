package com.example.test.npa_flow.status_of_customer.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test.R;
import com.example.test.databinding.ItemStatusDetailsOfCustomerBinding;
import com.example.test.helper_classes.Global;
import com.example.test.npa_flow.dpd.DPD_ResponseModel;
import com.example.test.npa_flow.status_of_customer.model.Activity;
import com.example.test.npa_flow.status_of_customer.model.ActivityDetail;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

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

        // format should be  -> 25th May 23,Friday,5:00pm COMPLETE ShivKumar
        holder.binding.txtStatusDetailsOfCustomer.setText(a.getActivityDate()+","+a.getDay()+","+a.getActivityTime()+" "+a.getActivityStatus());



        holder.binding.txtBottomMainStatusInfo.setText(a.getActivityStatus());


        holder.binding.ivDownArrowStatus.setOnClickListener(v -> {
            if (holder.binding.ivUpArrowStatus.getVisibility() == View.INVISIBLE) {
                holder.binding.ivDownArrowStatus.setVisibility(View.INVISIBLE);
                holder.binding.ivUpArrowStatus.setVisibility(View.VISIBLE);
                holder.binding.clStatusInfo.setVisibility(View.VISIBLE);
                holder.binding.txtBottomMainStatusInfo.setVisibility(View.VISIBLE);

                // Complete / Pending
                if(a.getActivityStatus().toLowerCase().contains("complete")){
                    holder.binding.txtBottomMainStatusInfo.setText("Complete");
                }
                if(a.getActivityStatus().toLowerCase().contains("pending")){
                    holder.binding.txtBottomMainStatusInfo.setText("Pending");
                }

                // Status Info
                for (ActivityDetail details : a.getActivityDetails()) {
                    if (details.getAttemptFlow() != null) {
                        String attemptFlow = details.getAttemptFlow().toLowerCase();

                        //1)Spoke To The Customer
                        if (attemptFlow.contains("sttc")) {
                            holder.binding.txtHeadStatusInfo.setText("Spoke To The Customer");
                        }
                        //2)Ready To Pay / Not Ready To Pay / Asked To Call Back Later
                        if (attemptFlow.contains("rtp")) {
                            holder.binding.txtMidStatusInfo1.setText("Ready To Pay");
                        }
                        if (attemptFlow.contains("nrtp")) {
                            holder.binding.txtMidStatusInfo1.setText("Not Ready To Pay");
                        }
                        if (attemptFlow.contains("atcl")) {
                            holder.binding.txtMidStatusInfo1.setText("Asked To Call Back Later");
                        }

                        //3)Send Visit For Collection / Send Link For Online Payment / FO Not Visited / Loan taken By Relative / Already Paid / Will Pay later
                        if (attemptFlow.contains("svfc")) {
                            holder.binding.txtMidStatusInfo2.setText("Send Visit For Collection");
                        }
                        if (attemptFlow.contains("slfop")) {
                            holder.binding.txtMidStatusInfo2.setText("Send Link For Online Payment");
                        }
                        if (attemptFlow.contains("fnv")) {
                            holder.binding.txtMidStatusInfo2.setText("FO Not Visited");
                        }
                        if (attemptFlow.contains("ltbr")) {
                            holder.binding.txtMidStatusInfo2.setText("Loan Taken By Relative");
                        }
                        if (attemptFlow.contains("ap")) {
                            holder.binding.txtMidStatusInfo2.setText("Already Paid");
                        }
                        if (attemptFlow.contains("wpl")) {
                            holder.binding.txtMidStatusInfo2.setText("Will Pay Later");
                        }

                        //4)Full Amt. Paid /Partial Amt. Paid/ Wil Pay Later / Will Pay Lump sump
                        if(attemptFlow.contains("fap")){
                            holder.binding.txtMidStatusInfo3.setText("Full Amount Paid");
                        }
                        if(attemptFlow.contains("pap")){
                            holder.binding.txtMidStatusInfo3.setText("Partial Amount Paid");
                        }
                        if(attemptFlow.contains("wpl")){
                            holder.binding.txtMidStatusInfo3.setText("Will Pay Later");
                        }
                        if(attemptFlow.contains("wpls")){
                            holder.binding.txtMidStatusInfo3.setText("Will Pay Lump sump");
                        }
                    }
                }
            }
        });

        holder.binding.ivUpArrowStatus.setOnClickListener(v -> {
            if (holder.binding.ivUpArrowStatus.getVisibility() == View.VISIBLE) {
                holder.binding.ivDownArrowStatus.setVisibility(View.VISIBLE);
                holder.binding.ivUpArrowStatus.setVisibility(View.INVISIBLE);
                holder.binding.clStatusInfo.setVisibility(View.GONE);
                holder.binding.txtBottomMainStatusInfo.setVisibility(View.GONE);
            }
        });



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
/*
      // to convert 24-05-2034 to 24 May 2023
    public static String convertDateFormat(String inputDate, String outputFormat) {
        try {
            SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            Date date = inputDateFormat.parse(inputDate);

            SimpleDateFormat outputDateFormat = new SimpleDateFormat(outputFormat, Locale.getDefault());
            return outputDateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

*/

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
