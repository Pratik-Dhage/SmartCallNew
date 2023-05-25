package com.example.test.schedule_flow.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test.R;
import com.example.test.databinding.ItemScheduleDetailsBinding;
import com.example.test.schedule_flow.model.ScheduleVisit_Details;

import java.util.ArrayList;

public class ScheduleDetailsAdapter extends RecyclerView.Adapter<ScheduleDetailsAdapter.MyViewHolderClass> {

    ArrayList<ScheduleVisit_Details> scheduleVisitDetails_List;

    public ScheduleDetailsAdapter(ArrayList<ScheduleVisit_Details> scheduleVisitDetails_List) {
        this.scheduleVisitDetails_List = scheduleVisitDetails_List;
    }

    @NonNull
    @Override
    public MyViewHolderClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemScheduleDetailsBinding view = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_schedule_details, parent, false);
        return new MyViewHolderClass(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderClass holder, int position) {

        ScheduleVisit_Details a = scheduleVisitDetails_List.get(position);

        holder.binding.txtMember.setText(a.getMemberName());
        holder.binding.txtScheduledTime.setText(a.getScheduledTime());
        holder.binding.txtQueue.setText(a.getQueue());

        String currentDate = a.getScheduledDate();
        String previousDate = "";

        if (!currentDate.equals(previousDate)) {
            holder.binding.txtDate.setText(currentDate);
            holder.binding.txtDate.setVisibility(View.VISIBLE);
            previousDate = currentDate;
        } else {
            holder.binding.txtDate.setVisibility(View.INVISIBLE);
        }


    }

    @Override
    public int getItemCount() {
        return scheduleVisitDetails_List.size();
    }


    @SuppressLint("NotifyDataSetChanged")
    public ArrayList setData(ArrayList<ScheduleVisit_Details> data) {
        if (data.isEmpty()) {
            scheduleVisitDetails_List = new ArrayList();
        }
        scheduleVisitDetails_List = data;
        notifyDataSetChanged();

        return scheduleVisitDetails_List;
    }

    class MyViewHolderClass extends RecyclerView.ViewHolder {

        private ItemScheduleDetailsBinding binding;

        public MyViewHolderClass(ItemScheduleDetailsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }

}
