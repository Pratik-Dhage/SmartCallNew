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

               openAlertDialogForDeletingLead(context,a);

                return false;
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

    private void openAlertDialogForDeletingLead(Context context, LeadModelRoom a){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete");
        builder.setCancelable(true);
        builder.setMessage("Do you want to delete this Lead?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                LeadModelRoom leadModelRoom = a; // a will give the position of selected Lead

                // delete that particular lead
                LeadDao lead_Dao = LeadListDB.getInstance(context).leadDao();
                lead_Dao.delete(leadModelRoom);
                notifyDataSetChanged(); // refresh the Lead List after Deletion

            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
