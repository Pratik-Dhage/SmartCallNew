package com.example.test.lead.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.example.test.lead.LeadsActivity;
import com.example.test.roomDB.dao.LeadCallDao;
import com.example.test.roomDB.dao.LeadDao;
import com.example.test.roomDB.database.LeadListDB;
import com.example.test.roomDB.model.LeadModelRoom;

import java.util.ArrayList;

public class Room_LeadListAdapter extends RecyclerView.Adapter<Room_LeadListAdapter.MyViewHolderClass> {


    ArrayList<LeadModelRoom> leadModelRoomArrayList;

    //call this Constructor in MainActivity
    public Room_LeadListAdapter(ArrayList<LeadModelRoom> leadModelRoomArrayList) {
        this.leadModelRoomArrayList = leadModelRoomArrayList;
    }

    @NonNull
    @Override
    public MyViewHolderClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ItemLeadListBinding view = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_lead_list, null, false) ;
        return new MyViewHolderClass(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderClass holder, int position) {

        LeadModelRoom a = leadModelRoomArrayList.get(position);
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

        // for deleting a Lead
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                // Pass the position of the itemView that was clicked to the LeadsActivity class
                int position = holder.getAdapterPosition();
                ((LeadsActivity)context).onItemViewClick(position,a);

                return false;
            }
        });


        //for setting Call Attempts ImageView
        LeadCallDao leadCallDao = LeadListDB.getInstance(context).leadCallDao();

        String phoneNumber=a.getPhoneNumber(); // get Phone Number

        if(leadCallDao.getCallCountUsingPhoneNumber(phoneNumber)>2){
            leadCallDao.UpdateLeadCalls(0,phoneNumber); // if leadCallCount >2 make it back to zero
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
        return leadModelRoomArrayList.size();
    }

    @Override
    public void onViewAttachedToWindow(@NonNull Room_LeadListAdapter.MyViewHolderClass holder) {
        super.onViewAttachedToWindow(holder);
        holder.setIsRecyclable(true);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull Room_LeadListAdapter.MyViewHolderClass holder) {
        super.onViewDetachedFromWindow(holder);
        holder.setIsRecyclable(false); // to prevent ImageView from being disappeared when scrolled upwards
    }


    class MyViewHolderClass extends RecyclerView.ViewHolder {

        private ItemLeadListBinding binding;

        public MyViewHolderClass(ItemLeadListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }



}
