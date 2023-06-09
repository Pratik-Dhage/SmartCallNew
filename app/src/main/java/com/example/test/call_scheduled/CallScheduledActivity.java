package com.example.test.call_scheduled;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.Window;

import com.example.test.R;
import com.example.test.call_scheduled.adapter.CallScheduledAdapter;
import com.example.test.call_scheduled.model.TestModel;
import com.example.test.databinding.ActivityCallScheduledBinding;
import com.example.test.databinding.ActivityCallStatusBinding;
import com.example.test.helper_classes.Global;
import com.example.test.roomDB.dao.LeadCallDao;
import com.example.test.roomDB.database.LeadListDB;
import com.example.test.roomDB.model.LeadCallModelRoom;

import java.util.ArrayList;

public class CallScheduledActivity extends AppCompatActivity {

    ActivityCallScheduledBinding binding;
    View view;
    CallScheduledViewModel callScheduledViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_call_scheduled);


        initializeFields();
        onClickListener();
        callCallScheduledApi();
    }

    private void callCallScheduledApi() {
    }

    private void onClickListener() {
    }

    private void initializeFields() {

        binding = DataBindingUtil.setContentView(this, R.layout.activity_call_scheduled);
        view = binding.getRoot();
        callScheduledViewModel = new ViewModelProvider(this).get(CallScheduledViewModel.class);
        binding.setCallScheduledViewModel(callScheduledViewModel);

        if (isDevice24HourFormat()) {

            Global.showToast(this, getResources().getString(R.string.timezone_24hr_format));
          /*  Intent intent = new Intent(android.provider.Settings.ACTION_DATE_SETTINGS);
            startActivity(intent);*/
        } else {
            Global.showToast(this, getResources().getString(R.string.timezone_12hr_format));
        }

        setUpCallScheduledRecyclerView();
    }

    private boolean isDevice24HourFormat() {
        return DateFormat.is24HourFormat(this); // returns true if User device is 24 hr format else returns false
    }


   private void setUpCallScheduledRecyclerView(){
       ArrayList<TestModel> testModelArrayList = new ArrayList<>();
       RecyclerView recyclerView = binding.rvCallScheduled;
       recyclerView.setAdapter(new CallScheduledAdapter(testModelArrayList));
   }



}