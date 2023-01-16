package com.example.test.main_dashboard.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test.R;
import com.example.test.databinding.ItemDashboardBinding;
import com.example.test.databinding.ItemLeadListBinding;
import com.example.test.lead.adapter.LeadListAdapter;

public class MainDashBoardAdapter extends RecyclerView.Adapter<MainDashBoardAdapter.MyViewHolderClass> {


    @NonNull
    @Override
    public MyViewHolderClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemDashboardBinding view = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_dashboard, null, false) ;
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

        public MyViewHolderClass(ItemDashboardBinding binding) {
            super(binding.getRoot());

        }

    }

}
