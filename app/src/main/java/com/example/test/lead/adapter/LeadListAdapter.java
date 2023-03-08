package com.example.test.lead.adapter;

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
import com.example.test.call_status.CallStatusActivity;
import com.example.test.databinding.ItemLeadListBinding;
import com.example.test.helper_classes.Global;
import com.example.test.lead.model.LeadModel;
import com.example.test.roomDB.dao.LeadCallDao;
import com.example.test.roomDB.database.LeadListDB;

import java.util.ArrayList;
import java.util.List;

public class LeadListAdapter extends RecyclerView.Adapter<LeadListAdapter.MyViewHolderClass> {

    // Context context;
    ArrayList<LeadModel> LeadModelClassArrayList;



    // call this constructor in LeadsViewModel
    public LeadListAdapter(ArrayList<LeadModel> arrListData) {
        this.LeadModelClassArrayList = arrListData;
    }


    @NonNull
    @Override
    public MyViewHolderClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ItemLeadListBinding view = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_lead_list, null, false) ;
        return new MyViewHolderClass(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderClass holder, int position) {

        LeadModel a = LeadModelClassArrayList.get(position);
        Context context = holder.itemView.getContext();

         holder.binding.txtLeadName.setText(a.getFirstName());
         holder.binding.txtMobileNumber.setText(a.getPhoneNumber());

         holder.itemView.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 Intent i = new Intent(context, CallStatusActivity.class);
                 i.putExtra("firstName",a.getFirstName());
                 i.putExtra("phoneNumber",a.getPhoneNumber());
                 context.startActivity(i);
             }
         });

        //for setting Call Attempts ImageView
        LeadCallDao leadCallDao = LeadListDB.getInstance(context).leadCallDao();

        String phoneNumber=a.getPhoneNumber(); // get Phone Number

        if(leadCallDao.getCallCountUsingPhoneNumber(phoneNumber)>3){
            leadCallDao.UpdateLeadCalls(0,phoneNumber); // if leadCallCount >3 make it back to zero
        }

        int callCount =  leadCallDao.getCallCountUsingPhoneNumber(phoneNumber);

        switch (callCount){

            case 0:  holder.binding.ivLeadList.setVisibility(View.INVISIBLE);
                break;
            case 1:holder.binding.ivLeadList.setImageResource(R.drawable.attempttwo);
                break;
            case 2 : holder.binding.ivLeadList.setImageResource(R.drawable.attemptthree);
                break;

        }

    }

    @Override
    public int getItemCount() {
        return LeadModelClassArrayList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public ArrayList setData(ArrayList<LeadModel> data)  {
        if (data.isEmpty()) {
            LeadModelClassArrayList =  new ArrayList();
        }
        LeadModelClassArrayList = data;
        notifyDataSetChanged();

        return LeadModelClassArrayList;
    }

    class MyViewHolderClass extends RecyclerView.ViewHolder {

        private   ItemLeadListBinding binding;

        public MyViewHolderClass(ItemLeadListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
