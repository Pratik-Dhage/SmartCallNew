package com.example.test.main_dashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.test.R;
import com.example.test.call_scheduled.CallScheduledActivity;
//import com.example.test.databinding.ActivityMainBinding;
import com.example.test.databinding.ActivityMainUpdated2Binding;
//import com.example.test.databinding.ActivityMainUpdatedBinding;
import com.example.test.encryption_decryption.EncryptionUtils;
import com.example.test.helper_classes.Global;
import com.example.test.helper_classes.NetworkUtilities;
import com.example.test.lead.LeadsActivity;
import com.example.test.main_dashboard.adapter.MainDashBoardAdapter;
import com.example.test.main_dashboard.model.DashBoardModel;
import com.example.test.main_dashboard.model.DashBoardResponseModel;
import com.example.test.user.UserModel;

import org.json.JSONObject;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


//This is Dashboard Activity
public class MainActivity extends AppCompatActivity {

    ActivityMainUpdated2Binding binding;
    View view;
    MainDashBoardViewModel mainDashBoardViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initializeFields();
        onClickListener();

        initObserver();
      callDashBoardApi();

    }

    private void initializeFields() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main_updated2);
        view = binding.getRoot();
        mainDashBoardViewModel = new ViewModelProvider(this).get(MainDashBoardViewModel.class);
        binding.setViewModel(mainDashBoardViewModel);
    }

    private void callDashBoardApi() {
        mainDashBoardViewModel.getDashBoardData();
    }

    private void setUpDashBoardRecyclerView() {
        mainDashBoardViewModel.updateDashBoardData();
        RecyclerView recyclerView = binding.rvDashBoardMain;
        recyclerView.setAdapter(new MainDashBoardAdapter(mainDashBoardViewModel.arrListDashBoardData));
    }

    private void initObserver() {

        binding.loadingProgressBar.setVisibility(View.VISIBLE);
        mainDashBoardViewModel.getMutDashBoardResponseApi().observe(this, result -> {

           if(NetworkUtilities.getConnectivityStatus(this)) {
               if (result != null) {
                //   Global.showToast(MainActivity.this, "Size:" + result.size());

                   mainDashBoardViewModel.arrListDashBoardData.clear();

                   setUpDashBoardRecyclerView();
                   mainDashBoardViewModel.arrListDashBoardData.addAll(result);
                   binding.loadingProgressBar.setVisibility(View.GONE);

               }
           }
           else{
               Global.showSnackBar(view,getResources().getString(R.string.check_internet_connection));
           }

        });

        //handle  error response
        mainDashBoardViewModel.getMutErrorResponse().observe(this, error -> {

            if (error != null && !error.isEmpty()) {
                Global.showSnackBar(view, error);
                System.out.println("Here: " + error);
            } else {
                Global.showSnackBar(view, getResources().getString(R.string.check_internet_connection));
            }
        });
    }


   /* private void setUpDashboardData() {

        //for Date
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, EEEE", Locale.ENGLISH);
        String formattedDate = currentDate.format(formatter);
        binding.txtDate.setText(formattedDate);
    }
*/

    private void onClickListener() {


        binding.clTotalCallScheduled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(MainActivity.this, CallScheduledActivity.class);
                startActivity(i);
            }
        });

        //Display Queue(Marketing,Collection etc) , Completed Calls , Pending Calls table
        // Hide slashArrow and Notepad Icon, edittext , add and cancel buttons
        binding.clTotalCallScheduledForMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (binding.clQueueCompletedCallsPendingCallsTable.getVisibility() == View.GONE) {
                    binding.clQueueCompletedCallsPendingCallsTable.setVisibility(View.VISIBLE);
                    binding.rvDashBoardMain.setVisibility(View.VISIBLE);

                    binding.ivRightArrow.setVisibility(View.GONE);
                    binding.ivDownArrow.setVisibility(View.VISIBLE);
                   // binding.ivAddNote.setVisibility(View.GONE);
                    binding.edtNotepad.setVisibility(View.GONE);
                    binding.btnAddNote.setVisibility(View.GONE);
                    binding.btnCancelNote.setVisibility(View.GONE);
                } else {
                    binding.clQueueCompletedCallsPendingCallsTable.setVisibility(View.GONE);

                    binding.ivRightArrow.setVisibility(View.VISIBLE);
                  //  binding.ivAddNote.setVisibility(View.VISIBLE);
                    binding.ivDownArrow.setVisibility(View.GONE);
                }

            }
        });

        //Display Edit Text as Notepad , Add and Cancel Buttons and Hide SlashArrow
       /* binding.ivAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.ivRightArrow.setVisibility(View.GONE);
                binding.ivAddNote.setVisibility(View.GONE);
                binding.edtNotepad.setVisibility(View.VISIBLE);
                binding.btnAddNote.setVisibility(View.VISIBLE);
                binding.btnCancelNote.setVisibility(View.VISIBLE);


            }
        });

        //on Cancel Button
        binding.btnCancelNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.btnAddNote.setVisibility(View.GONE);
                binding.btnCancelNote.setVisibility(View.GONE);
                binding.edtNotepad.setVisibility(View.GONE);
                binding.ivAddNote.setVisibility(View.VISIBLE);
                binding.ivRightArrow.setVisibility(View.VISIBLE);


            }
        });

*/

    }



}

