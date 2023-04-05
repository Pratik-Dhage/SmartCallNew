package com.example.test.main_dashboard.adapter;

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
import com.example.test.databinding.ItemDashboard3ApiBinding;
import com.example.test.databinding.ItemDashboardBinding;
import com.example.test.helper_classes.Global;
import com.example.test.lead.LeadsActivity;
import com.example.test.main_dashboard.model.DashBoardResponseModel;
import com.example.test.npa_flow.DPDActivity;

import java.util.ArrayList;

public class MainDashBoardAdapter extends RecyclerView.Adapter<MainDashBoardAdapter.MyViewHolderClass> {

    ArrayList<DashBoardResponseModel> dashBoardResponseModelArrayList;

    public MainDashBoardAdapter(ArrayList<DashBoardResponseModel> dashBoardResponseModelArrayList) {
        this.dashBoardResponseModelArrayList = dashBoardResponseModelArrayList;

    }

    @NonNull
    @Override
    public MyViewHolderClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemDashboard3ApiBinding view = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_dashboard3_api, null, false) ;
        return new MyViewHolderClass(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderClass holder, int position) {

        DashBoardResponseModel a = dashBoardResponseModelArrayList.get(position);
        Context context = holder.itemView.getContext();

        holder.binding.txtDashBoardListName.setText(a.getQueueName());

       holder.binding.txtCompletedMembersAssigned.setText(a.getCompletedCalls().toString());
        holder.binding.txtPendingMembersAssigned.setText(a.getPendingCalls().toString());


        //for Total Assigned Members( for Marketing, Collection, Welcome Call,Renewal respectively)
        int totalMembers = a.getCompletedCalls()+a.getPendingCalls();
        holder.binding.txtTotalMembersAssigned.setText(String.valueOf(totalMembers));


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle item click
                launchActivityForItem(context,holder.getAdapterPosition());
            }
        });

    }

    @Override
    public int getItemCount() {
        return dashBoardResponseModelArrayList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public ArrayList setData(ArrayList<DashBoardResponseModel> data)  {
        if (data.isEmpty()) {
            dashBoardResponseModelArrayList =  new ArrayList();
        }
        dashBoardResponseModelArrayList = data;
        notifyDataSetChanged();

        return dashBoardResponseModelArrayList;
    }


    private void launchActivityForItem( Context context,int position) {

        switch (position) {
            case 0:
                // Launch activity for item at position 0 i.e. Marketing
                Intent intent0 = new Intent(context, LeadsActivity.class);
                context.startActivity(intent0);
                break;
            case 1:
                // Launch activity for item at position 1 i.e. NPA (Collection)

                Intent intent1 = new Intent(context, DPDActivity.class);
                context.startActivity(intent1);
                break;

            case 2:
                // Launch activity for item at position 2 i.e. Welcome Call

                Global.showToast(context,""+ dashBoardResponseModelArrayList.get(position).getQueueName());
                break;

            case 3:
                // Launch activity for item at position 3 i.e. Renewal
              /*  Intent intent1 = new Intent(context, Activity1.class);
                context.startActivity(intent1);*/
                Global.showToast(context,""+ dashBoardResponseModelArrayList.get(position).getQueueName());
                break;


            default:
                // Do nothing for other positions
                break;
        }
    }




    class MyViewHolderClass extends RecyclerView.ViewHolder {

        private ItemDashboard3ApiBinding binding;

        public MyViewHolderClass(ItemDashboard3ApiBinding binding) {
            super(binding.getRoot());
               this.binding =binding;
        }

    }

}
