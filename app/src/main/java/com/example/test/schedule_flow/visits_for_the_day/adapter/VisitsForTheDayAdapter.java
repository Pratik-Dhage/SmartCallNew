package com.example.test.schedule_flow.visits_for_the_day.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test.R;
import com.example.test.databinding.ItemStatusDetailsOfCustomerBinding;
import com.example.test.databinding.ItemVisitsForTheDayBinding;
import com.example.test.helper_classes.Global;
import com.example.test.npa_flow.status_of_customer.adapter.StatusOfCustomerDetailsAdapter;
import com.example.test.npa_flow.status_of_customer.model.Activity;
import com.example.test.schedule_flow.visits_for_the_day.model.VisitsForTheDayResponseModel;

import java.util.ArrayList;

public class VisitsForTheDayAdapter extends RecyclerView.Adapter<VisitsForTheDayAdapter.MyViewHolderClass> {


    ArrayList<VisitsForTheDayResponseModel> visitsForTheDayResponseModelArrayList;

    public VisitsForTheDayAdapter(ArrayList<VisitsForTheDayResponseModel> visitsForTheDayResponseModelArrayList) {
        this.visitsForTheDayResponseModelArrayList = visitsForTheDayResponseModelArrayList;
    }

    @NonNull
    @Override
    public MyViewHolderClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemVisitsForTheDayBinding view = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_visits_for_the_day,parent,false);
        return new MyViewHolderClass(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderClass holder, int position) {

        VisitsForTheDayResponseModel a = visitsForTheDayResponseModelArrayList.get(position);
          Context context = holder.itemView.getContext();

        String userName = Global.getStringFromSharedPref(context,"userName");
        holder.binding.txtUserName.setText(userName);

        // format should be  -> 25th May 23,Friday,5:00pm COMPLETE ShivKumar
        if(a.getScheduleDateTime()!=null && a.getAttemptStatus()!=null){
            holder.binding.txtVisitsForTheDay.setText(a.getScheduleDateTime()+","+a.getAttemptStatus());
        }

    }

    @Override
    public int getItemCount() {
        return visitsForTheDayResponseModelArrayList.size();
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
    public ArrayList setData(ArrayList<VisitsForTheDayResponseModel> data)  {
        if (data.isEmpty()) {
            visitsForTheDayResponseModelArrayList =  new ArrayList();
        }
        visitsForTheDayResponseModelArrayList = data;
        notifyDataSetChanged();

        return visitsForTheDayResponseModelArrayList;
    }


    class MyViewHolderClass extends RecyclerView.ViewHolder {

        private ItemVisitsForTheDayBinding binding;

        public MyViewHolderClass(ItemVisitsForTheDayBinding binding) {
            super(binding.getRoot());
            this.binding =binding;
        }

    }
}
