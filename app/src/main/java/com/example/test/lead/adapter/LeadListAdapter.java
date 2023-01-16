package com.example.test.lead.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test.R;
import com.example.test.databinding.ItemLeadListBinding;

public class LeadListAdapter extends RecyclerView.Adapter<LeadListAdapter.MyViewHolderClass> {



    @NonNull
    @Override
    public MyViewHolderClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ItemLeadListBinding view = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_lead_list, null, false) ;
        return new MyViewHolderClass(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderClass holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class MyViewHolderClass extends RecyclerView.ViewHolder {

        public MyViewHolderClass(ItemLeadListBinding binding) {
            super(binding.getRoot());

        }
    }
}
