package com.example.test.call_scheduled.adapter;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test.R;
import com.example.test.databinding.ItemCallScheduledBinding;
import com.example.test.databinding.ItemLeadListBinding;
import com.example.test.lead.adapter.LeadListAdapter;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.util.ArrayList;

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

        //to get Current time and compare it with the time coming from Api
        // if time difference is enough make time textColor Green
        // if there is 15 min gap then make textColor Yellow
        //if the time has passed (if time difference is zero(0)) then make it Red

        LocalTime currentTime = LocalTime.now();
        int hour = currentTime.getHour();
        int minute = currentTime.getMinute();
        int second = currentTime.getSecond();



    }

    @Override
    public int getItemCount() {
        return 0;
    }

       //Another Method to get Time Difference
    public void getTimeDifferenceInSeconds(){

    /*    // Parse the time string received from the API
       Instant apiTime = Instant.parse(apiTimeString);

// Get the device's current time
        Instant deviceTime = Instant.now();

// Calculate the time difference between the two instants
        Duration timeDifference = Duration.between(apiTime, deviceTime);

// Get the time difference in seconds
       long timeDifferenceSeconds = timeDifference.toSeconds();

    */

    }


    class MyViewHolderClass extends RecyclerView.ViewHolder {

        private ItemCallScheduledBinding binding;

        public MyViewHolderClass(ItemCallScheduledBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
