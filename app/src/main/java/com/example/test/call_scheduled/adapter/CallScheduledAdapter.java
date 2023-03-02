package com.example.test.call_scheduled.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test.R;
import com.example.test.databinding.ItemCallScheduledBinding;
import com.example.test.databinding.ItemLeadListBinding;
import com.example.test.lead.adapter.LeadListAdapter;

public class CallScheduledAdapter extends RecyclerView.Adapter<CallScheduledAdapter.MyViewHolderClass>{


    @NonNull
    @Override
    public MyViewHolderClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCallScheduledBinding view = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_call_scheduled, null, false) ;
        return new MyViewHolderClass(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderClass holder, int position) {

        Context context = holder.itemView.getContext();
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class MyViewHolderClass extends RecyclerView.ViewHolder {

        private ItemCallScheduledBinding binding;

        public MyViewHolderClass(ItemCallScheduledBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
