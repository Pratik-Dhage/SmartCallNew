package com.example.test.schedule_flow.calls_for_the_day.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test.R;
import com.example.test.databinding.ItemCallsForTheDayBinding;
import com.example.test.google_maps.GoogleMapsActivity;
import com.example.test.google_maps.MapFragment;
import com.example.test.schedule_flow.calls_for_the_day.model.CallsForTheDayResponseModel;
import com.example.test.schedule_flow.visits_for_the_day.model.VisitsForTheDayResponseModel;

import java.util.ArrayList;

public class CallsForTheDayAdapter extends RecyclerView.Adapter<CallsForTheDayAdapter.MyViewHolderClass> {

    ArrayList<CallsForTheDayResponseModel> callsForTheDayResponseModelArrayList;

    public CallsForTheDayAdapter(ArrayList<CallsForTheDayResponseModel> callsForTheDayResponseModelArrayList) {
        this.callsForTheDayResponseModelArrayList = callsForTheDayResponseModelArrayList;
    }

    @NonNull
    @Override
    public MyViewHolderClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCallsForTheDayBinding view =  DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_calls_for_the_day,parent,false);
        return new MyViewHolderClass(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderClass holder, int position) {

        CallsForTheDayResponseModel a = callsForTheDayResponseModelArrayList.get(position);
        Context context = holder.itemView.getContext();

        if(a.getMemberName()!=null){
            holder.binding.txtName.setText(a.getMemberName());
        }

        if(a.getDistance()!=null){
            holder.binding.txtDistance.setText(String.valueOf(a.getDistance()));
        }

        if(a.getLocation()!=null){
            holder.binding.txtLocation.setText(a.getLocation());
        }

        if(a.getActionStatus()!=null){
            holder.binding.txtStatus.setText(String.valueOf(a.getActionStatus()));
        }

        //opens Google Maps
        holder.binding.ivMap.setOnClickListener(v->{

            if(a.getLattitute()!=null && a.getLongitute()!=null ){

                String latitude = String.valueOf(a.getLattitute());
                String longitude = String.valueOf(a.getLongitute());

                Intent googleMapsIntent = new Intent(context, GoogleMapsActivity.class);
                googleMapsIntent.putExtra("latitude", Double.parseDouble(latitude));
                googleMapsIntent.putExtra("longitude", Double.parseDouble(longitude));
                googleMapsIntent.putExtra("isFromCallsForTheDayAdapter","isFromCallsForTheDayAdapter");
                context.startActivity(googleMapsIntent);

            }

        });

    }

    @Override
    public int getItemCount() {
        return callsForTheDayResponseModelArrayList.size();
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
    public ArrayList setData(ArrayList<CallsForTheDayResponseModel> data)  {
        if (data.isEmpty()) {
            callsForTheDayResponseModelArrayList =  new ArrayList();
        }
        callsForTheDayResponseModelArrayList = data;
        notifyDataSetChanged();

        return callsForTheDayResponseModelArrayList;
    }

    class MyViewHolderClass extends RecyclerView.ViewHolder {

        private ItemCallsForTheDayBinding binding;

        public MyViewHolderClass(ItemCallsForTheDayBinding binding) {
            super(binding.getRoot());
            this.binding =binding;
        }

    }

}
