package com.example.test.lead.adapter;

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

    }

    @Override
    public int getItemCount() {
        return leadModelRoomArrayList.size();
    }

    class MyViewHolderClass extends RecyclerView.ViewHolder {

        private ItemLeadListBinding binding;

        public MyViewHolderClass(ItemLeadListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
