package com.example.test.lead.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test.R;
import com.example.test.databinding.ItemLeadListBinding;
import com.example.test.lead.model.LeadModel;

import java.util.List;

public class RawLeadListAdapter  extends RecyclerView.Adapter<RawLeadListAdapter.MyViewHolderClass>{


    List<Object> listRecyclerItem;

    // call this constructor in MainActivity
    public RawLeadListAdapter( List<Object> listRecyclerItem) {
        this.listRecyclerItem = listRecyclerItem;
    }

    @NonNull
    @Override
    public RawLeadListAdapter.MyViewHolderClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemLeadListBinding view = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_lead_list, null, false) ;
        return   new MyViewHolderClass(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RawLeadListAdapter.MyViewHolderClass holder, int position) {

        LeadModel a = (LeadModel) listRecyclerItem.get(position);
        Context context = holder.itemView.getContext();

        holder.binding.txtLeadName.setText(a.getFirstName());
        holder.binding.txtMobileNumber.setText(a.getPhoneNumber());

    }

    @Override
    public int getItemCount() {
         return listRecyclerItem.size();
    }

    class MyViewHolderClass extends RecyclerView.ViewHolder {

        private ItemLeadListBinding binding;

        public MyViewHolderClass(ItemLeadListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
