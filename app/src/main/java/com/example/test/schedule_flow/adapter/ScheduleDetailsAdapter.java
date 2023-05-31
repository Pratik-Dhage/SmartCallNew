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
import com.example.test.helper_classes.Global;
import com.example.test.schedule_flow.model.ScheduleVisitDetails;

import java.util.ArrayList;

public class ScheduleDetailsAdapter extends RecyclerView.Adapter<ScheduleDetailsAdapter.MyViewHolderClass> {

    ArrayList<ScheduleVisitDetails> scheduleVisitDetails_List;
    public ScheduleDetailsAdapter(ArrayList<ScheduleVisitDetails> scheduleVisitDetails_List) {
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
        ScheduleVisitDetails a = scheduleVisitDetails_List.get(position);
        holder.binding.txtMember.setText(a.getMemberName());
        holder.binding.txtScheduledTime.setText(a.getScheduledTime());
        holder.binding.txtQueue.setText(a.getQueue());

        String previousDate = "";
        String currentDate;
        for (int i = 0; i <= position; i++) {
            a = scheduleVisitDetails_List.get(i);
            currentDate = a.getScheduledDate();
            if (!currentDate.equals(previousDate)) {
                holder.binding.txtDate.setText(currentDate);
                holder.binding.txtDate.setVisibility(View.VISIBLE);
                previousDate = currentDate;
            } else {
                holder.binding.txtDate.setVisibility(View.INVISIBLE);
            }

        }

        holder.itemView.setOnClickListener(v->{
            Global.showToast(v.getContext(), "Position:"+position);
        });
    }


    @Override
    public int getItemCount() {return scheduleVisitDetails_List.size();}

    @Override
    public void onViewAttachedToWindow(@NonNull MyViewHolderClass holder) {
        super.onViewAttachedToWindow(holder);
        holder.setIsRecyclable(true);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull MyViewHolderClass holder) {
        super.onViewDetachedFromWindow(holder);
        holder.setIsRecyclable(false);
    }

    @SuppressLint("NotifyDataSetChanged")
    public ArrayList setData(ArrayList<ScheduleVisitDetails> data) {
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
