package com.example.test.call_scheduled.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test.R;
import com.example.test.call_scheduled.model.TestModel;
import com.example.test.databinding.ItemCallScheduledBinding;
import com.example.test.helper_classes.Global;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CallScheduledAdapter extends RecyclerView.Adapter<CallScheduledAdapter.MyViewHolderClass>{


    ArrayList<TestModel> testModelArrayList;

    TestModel testModel1 = new TestModel("Marketing","Abc","09/03/2023","8:03:33");
    TestModel testModel2 = new TestModel("Collection","Pqr","08/02/2023","7:53:13");
    TestModel testModel3 = new TestModel("Welcome Call","Mno","10/01/2023","6:12:27");



    public CallScheduledAdapter(ArrayList<TestModel> testModelArrayList) {
        this.testModelArrayList = testModelArrayList;
        testModelArrayList.clear();
       testModelArrayList.add(testModel1);
       testModelArrayList.add(testModel2);
       testModelArrayList.add(testModel3);
    }



    @NonNull
    @Override
    public MyViewHolderClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCallScheduledBinding view = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_call_scheduled, null, false) ;
        return new MyViewHolderClass(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderClass holder, int position) {

        TestModel a = testModelArrayList.get(position);
        Context context = holder.itemView.getContext();

        holder.binding.txtQueue.setText(a.getQueue());
        holder.binding.txtName.setText(a.getName());
        holder.binding.txtCallDate.setText(a.getDate());
        holder.binding.txtCallTime.setText(a.getTime());

        //to get Current time and compare it with the time coming from Api
        // if time difference is enough make time textColor Green
        // if there is 15 min gap then make textColor Yellow
        //if the time has passed (if time difference is zero(0)) then make it Red

        LocalTime currentTime = LocalTime.now();
        int DeviceHour = currentTime.getHour();
        int DeviceMinute = currentTime.getMinute();
        int DeviceSecond = currentTime.getSecond();

      //  Global.showToast(context,"Current Time:"+DeviceHour+":"+DeviceMinute+":"+DeviceSecond);

      List time_list = List.of(a.getTime().split(":"));

      int hr = Integer.parseInt((String) time_list.get(0));
      int min = Integer.parseInt((String) time_list.get(1));
      int sec = Integer.parseInt((String) time_list.get(2));

      int hr_diff = DeviceHour - hr;
      int min_diff = DeviceMinute - min;
      int sec_diff = DeviceSecond - sec;

      //  LocalTime time = LocalTime.parse(a.getTime());

        if(hr_diff==0 && min_diff==0 && sec_diff<=900){
            holder.binding.txtCallTime.setTextColor(Color.YELLOW);
        }

        else if(hr_diff>1 && min_diff>1 && sec_diff>900){
            holder.binding.txtCallTime.setTextColor(Color.GREEN);
        }

        else{
            holder.binding.txtCallTime.setTextColor(Color.RED);
        }

/*

        // Convert the time string to a LocalTime object
        LocalTime time = LocalTime.parse(a.getTime(), DateTimeFormatter.ofPattern("HH:mm:ss"));

        // Calculate the time difference in minutes
        long timeDiffMinutes = Duration.between(LocalTime.now(), time).toMinutes();

        // Define the different colors for the text view based on the time difference
        int yellowColor = Color.parseColor("#FFFF00"); // Yellow
        int greenColor = Color.parseColor("#00FF00"); // Green
        int redColor = Color.parseColor("#FF0000"); // Red
        int textColor = timeDiffMinutes > 15 ? greenColor : timeDiffMinutes > 0 ? yellowColor : redColor;

        // Set the text color of the text view
        holder.binding.txtCallTime.setTextColor(textColor);
*/

    }

    @Override
    public int getItemCount() {
        return testModelArrayList.size();
    }

       //Another Method to get Time Difference
    public long getTimeDifferenceInSeconds(String time){

        // Parse the time string received from the API
       Instant apiTime = Instant.parse(time);

// Get the device's current time
        Instant deviceTime = Instant.now();

// Calculate the time difference between the two instants
        Duration timeDifference = Duration.between(apiTime, deviceTime);

// Get the time difference in seconds
        long timeDifferenceSeconds=timeDifference.toMillis();

     return timeDifferenceSeconds;
    }


    class MyViewHolderClass extends RecyclerView.ViewHolder {

        private ItemCallScheduledBinding binding;

        public MyViewHolderClass(ItemCallScheduledBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
