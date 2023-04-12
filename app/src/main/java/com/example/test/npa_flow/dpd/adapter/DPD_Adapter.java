package com.example.test.npa_flow.dpd.adapter;

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
import com.example.test.databinding.ItemDpdBinding;
import com.example.test.main_dashboard.model.DashBoardResponseModel;
import com.example.test.npa_flow.dpd.DPDActivity;
import com.example.test.npa_flow.loan_collection.LoanCollectionActivity;
import com.example.test.npa_flow.dpd.DPD_ResponseModel;

import java.util.ArrayList;

public class DPD_Adapter extends RecyclerView.Adapter<DPD_Adapter.MyViewHolderClass>{

    ArrayList<DPD_ResponseModel> dpdResponseModelArrayList;

    public DPD_Adapter(ArrayList<DPD_ResponseModel> dpdResponseModelArrayList) {
        this.dpdResponseModelArrayList = dpdResponseModelArrayList;
    }

    @NonNull
    @Override
    public MyViewHolderClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

     ItemDpdBinding view = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_dpd,parent,false);
     return new MyViewHolderClass(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderClass holder, int position) {

        DPD_ResponseModel a = dpdResponseModelArrayList.get(position);
        Context context = holder.itemView.getContext();

        holder.binding.txtDPDQueueName.setText(a.getDpdQueueName());
        holder.binding.txtCompleted.setText(a.getCompleted().toString());
        holder.binding.txtNoOfMember.setText(a.getNoOfMembers().toString());
        holder.binding.txtPending.setText(a.getPending().toString());


        holder.itemView.setOnClickListener(v->{
            Intent i = new Intent(context, LoanCollectionActivity.class);
            context.startActivity(i);
        });

    }

    @Override
    public int getItemCount() {
        return dpdResponseModelArrayList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public ArrayList setData(ArrayList<DPD_ResponseModel> data)  {
        if (data.isEmpty()) {
            dpdResponseModelArrayList =  new ArrayList();
        }
        dpdResponseModelArrayList = data;
        notifyDataSetChanged();

        return dpdResponseModelArrayList;
    }


    class MyViewHolderClass extends RecyclerView.ViewHolder {

        private ItemDpdBinding binding;

        public MyViewHolderClass(ItemDpdBinding binding) {
            super(binding.getRoot());
            this.binding =binding;
        }

    }

}
