package com.example.test.npa_flow.dpd.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test.R;
import com.example.test.databinding.ItemDpdBinding;
import com.example.test.helper_classes.Global;
import com.example.test.npa_flow.dpd.DPD_ResponseModel;
import com.example.test.npa_flow.loan_collection.LoanCollectionActivity;

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

        if(a.getDpdQueueName()!=null){
            holder.binding.txtDPDQueueName.setText(a.getDpdQueueName());
        }

        if(a.getCompleted()!=null){
            holder.binding.txtCompleted.setText(String.valueOf(a.getCompleted()));
        }

        if(a.getInprocess()!=null){
            holder.binding.txtInProcess.setText(String.valueOf(a.getInprocess()));
        }

        if(a.getPending()!=null){
            holder.binding.txtPending.setText(String.valueOf(a.getPending()));

        }


        // to show Respective list of LoanCollectionList of Customers according to Row Position Of DPD Queue in DPD Activity
        holder.itemView.setOnClickListener(v->{

            int DPD_row_position = holder.getAdapterPosition();

                Intent i = new Intent(context, LoanCollectionActivity.class);
                i.putExtra("DPD_row_position",DPD_row_position);
                context.startActivity(i);

                //Store DPD_row_position and use in Not Spoke To Customer Activity when No Response/Busy button is clicked
            Global.saveStringInSharedPref(context,"DPD_row_position", String.valueOf(DPD_row_position));

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
