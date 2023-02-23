package com.example.test.main_dashboard.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test.R;
import com.example.test.databinding.ItemDashboardBinding;
import com.example.test.databinding.ItemLeadListBinding;
import com.example.test.lead.adapter.LeadListAdapter;
import com.example.test.lead.model.LeadModel;
import com.example.test.main_dashboard.model.DashBoardModel;

import java.util.ArrayList;

public class MainDashBoardAdapter extends RecyclerView.Adapter<MainDashBoardAdapter.MyViewHolderClass> {

    ArrayList<DashBoardModel> DashBoardModelClassList;

    public MainDashBoardAdapter(ArrayList<DashBoardModel> dashBoardModelClassList) {
        DashBoardModelClassList = dashBoardModelClassList;
    }

    @NonNull
    @Override
    public MyViewHolderClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemDashboardBinding view = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_dashboard, null, false) ;
        return new MyViewHolderClass(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderClass holder, int position) {

        DashBoardModel a = DashBoardModelClassList.get(position);
        Context context = holder.itemView.getContext();

        holder.binding.txtDashBoardListName.setText(a.getQueueName());
        holder.binding.txtCompletedCalls.setText(a.getCompletedCalls());
        holder.binding.txtPendingCalls.setText(a.getPendingCalls());


    }

    @Override
    public int getItemCount() {
        return DashBoardModelClassList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public ArrayList setData(ArrayList<DashBoardModel> data)  {
        if (data.isEmpty()) {
            DashBoardModelClassList =  new ArrayList();
        }
        DashBoardModelClassList = data;
        notifyDataSetChanged();

        return DashBoardModelClassList;
    }


    class MyViewHolderClass extends RecyclerView.ViewHolder {

        private ItemDashboardBinding binding;

        public MyViewHolderClass(ItemDashboardBinding binding) {
            super(binding.getRoot());
               this.binding =binding;
        }

    }

}
