package com.example.test.lead.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test.R;
import com.example.test.call_status.CallStatusActivity;
import com.example.test.databinding.ItemLeadListBinding;
import com.example.test.roomDB.model.LeadModelRoom;

import java.util.ArrayList;

// This Adapter Extends ListAdapter
public class Room_LeadList_ListAdapter extends ListAdapter<LeadModelRoom, Room_LeadList_ListAdapter.MyViewHolderClass> {


    ArrayList<LeadModelRoom> leadModelRoomArrayList;

    public Room_LeadList_ListAdapter(ArrayList<LeadModelRoom> leadModelRoomArrayList) {
        super(CALLBACK);
        this.leadModelRoomArrayList = leadModelRoomArrayList;
    }



    //to Check firstName and phoneNumber of Leads are same or not
    private static final DiffUtil.ItemCallback<LeadModelRoom> CALLBACK = new DiffUtil.ItemCallback<LeadModelRoom>() {
        @Override
        public boolean areItemsTheSame(@NonNull LeadModelRoom oldItem, @NonNull LeadModelRoom newItem) {
            return oldItem.getLeadID()== newItem.getLeadID(); //== because it is int type
        }

        @Override
        public boolean areContentsTheSame(@NonNull LeadModelRoom oldItem, @NonNull LeadModelRoom newItem) {
            return oldItem.getFirstName().equals(newItem.getFirstName())
                    && oldItem.getPhoneNumber().equals(newItem.getPhoneNumber()); //equals() because String Type
        }
    };


    @NonNull
    @Override
    public MyViewHolderClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ItemLeadListBinding view = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_lead_list, null, false) ;
        return new MyViewHolderClass(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderClass holder, int position) {

        LeadModelRoom a = getItem(position);
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

    public ArrayList<LeadModelRoom> getLeads(){
        return leadModelRoomArrayList;
    }

    class MyViewHolderClass extends RecyclerView.ViewHolder {

        private ItemLeadListBinding binding;

        public MyViewHolderClass(ItemLeadListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
