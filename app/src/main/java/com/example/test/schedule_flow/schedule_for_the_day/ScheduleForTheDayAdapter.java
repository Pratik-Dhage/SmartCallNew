package com.example.test.schedule_flow.schedule_for_the_day;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test.R;
import com.example.test.databinding.ItemScheduleForTheDayBinding;
import com.example.test.databinding.ItemVisitsForTheDayBinding;
import com.example.test.helper_classes.Global;
import com.example.test.main_dashboard.MainActivity3API;
import com.example.test.main_dashboard.model.DashBoardScheduleForTheDayModel;
import com.example.test.schedule_flow.calls_for_the_day.CallsForTheDayActivity;
import com.example.test.schedule_flow.visits_for_the_day.VisitsForTheDayActivity;
import com.example.test.schedule_flow.visits_for_the_day.adapter.VisitsForTheDayAdapter;
import com.example.test.schedule_flow.visits_for_the_day.model.VisitsForTheDayResponseModel;

import java.util.ArrayList;

import io.reactivex.internal.operators.completable.CompletableOnErrorComplete;

public class ScheduleForTheDayAdapter extends RecyclerView.Adapter<ScheduleForTheDayAdapter.MyViewHolderClass>{

    ArrayList<DashBoardScheduleForTheDayModel> dashBoardScheduleForTheDayModelArrayList;

    public ScheduleForTheDayAdapter(ArrayList<DashBoardScheduleForTheDayModel> dashBoardScheduleForTheDayModelArrayList) {
        this.dashBoardScheduleForTheDayModelArrayList = dashBoardScheduleForTheDayModelArrayList;
    }

    @NonNull
    @Override
    public MyViewHolderClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemScheduleForTheDayBinding view = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_schedule_for_the_day,parent,false);
        return new MyViewHolderClass(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderClass holder, int position) {

        DashBoardScheduleForTheDayModel a = dashBoardScheduleForTheDayModelArrayList.get(position);
        Context context = holder.itemView.getContext();

        if(a.getQueue()!=null){
            holder.binding.txtQueueNameScheduleForTheDay.setText(a.getQueue());
        }

        if(a.getComplete()!=null){
            holder.binding.txtCompletedScheduleForTheDay.setText(String.valueOf(a.getComplete()));
        }

        if(a.getPending()!=null){
           holder.binding.txtPendingScheduleForTheDay.setText(String.valueOf(a.getPending()));
        }

        //for Total = Completed + Pending
        if(null!=a.getComplete() && null!=a.getPending()){
            long total = a.getComplete() + a.getPending();
            holder.binding.txtTotalScheduleForTheDay.setText(String.valueOf(total));
        }

       holder.binding.ivRightArrowScheduleForTheDay.setOnClickListener(v->{

           if(a.getQueue().toLowerCase().contains("visit")){
               MainActivity3API.showCallIcon = true;    //from Visits For The Day Flow to be True Else False
               Intent i = new Intent(context, VisitsForTheDayActivity.class);
               i.putExtra("isFromVisitsForTheDay","isFromVisitsForTheDay");
               context.startActivity(i);

           }

           else if(a.getQueue().toLowerCase().contains("call")) {

               MainActivity3API.showCallIcon=false;    //from Visits For The Day Flow to be True Else False
               Intent i = new Intent(context, CallsForTheDayActivity.class);
               i.putExtra("isFromCallsForTheDay","isFromCallsForTheDay");
               context.startActivity(i);
           }

           else{
               MainActivity3API.showCallIcon=false;
           }

       });

    }

    @Override
    public int getItemCount() {
        return dashBoardScheduleForTheDayModelArrayList.size();
    }

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
    public ArrayList setData(ArrayList<DashBoardScheduleForTheDayModel> data)  {
        if (data.isEmpty()) {
            dashBoardScheduleForTheDayModelArrayList =  new ArrayList();
        }
        dashBoardScheduleForTheDayModelArrayList = data;
        notifyDataSetChanged();

        return dashBoardScheduleForTheDayModelArrayList;
    }


    class MyViewHolderClass extends RecyclerView.ViewHolder {

        private ItemScheduleForTheDayBinding binding;

        public MyViewHolderClass(ItemScheduleForTheDayBinding binding) {
            super(binding.getRoot());
            this.binding =binding;
        }

    }

}
