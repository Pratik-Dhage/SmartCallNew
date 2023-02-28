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
import com.example.test.main_dashboard.model.DashBoardResponseModel;

import java.util.ArrayList;

public class MainDashBoardAdapter extends RecyclerView.Adapter<MainDashBoardAdapter.MyViewHolderClass> {

    ArrayList<DashBoardResponseModel> dashBoardResponseModelArrayList;

    public MainDashBoardAdapter(ArrayList<DashBoardResponseModel> dashBoardResponseModelArrayList) {
        this.dashBoardResponseModelArrayList = dashBoardResponseModelArrayList;

    }

    @NonNull
    @Override
    public MyViewHolderClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemDashboardBinding view = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_dashboard, null, false) ;
        return new MyViewHolderClass(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderClass holder, int position) {

        DashBoardResponseModel a = dashBoardResponseModelArrayList.get(position);
        Context context = holder.itemView.getContext();

        holder.binding.txtDashBoardListName.setText(a.getQueueName());
       holder.binding.txtCompletedCalls.setText(a.getCompletedCalls().toString());
        holder.binding.txtPendingCalls.setText(a.getPendingCalls().toString());


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


    class MyViewHolderClass extends RecyclerView.ViewHolder {

        private ItemDashboardBinding binding;

        public MyViewHolderClass(ItemDashboardBinding binding) {
            super(binding.getRoot());
               this.binding =binding;
        }

    }

}
