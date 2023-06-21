package com.example.test.npa_flow;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.test.R;
import com.example.test.databinding.ActivityNotSpokeToCustomerBinding;
import com.example.test.fragments_activity.BalanceInterestCalculationActivity;
import com.example.test.helper_classes.Global;
import com.example.test.helper_classes.NetworkUtilities;
import com.example.test.main_dashboard.MainActivity3API;
import com.example.test.npa_flow.details_of_customer.DetailsOfCustomerResponseModel;
import com.example.test.npa_flow.details_of_customer.DetailsOfCustomerViewModel;
import com.example.test.npa_flow.details_of_customer.adapter.DetailsOfCustomerAdapter;
import com.example.test.npa_flow.loan_collection.LoanCollectionActivity;
import com.example.test.schedule_flow.calls_for_the_day.CallsForTheDayActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class NotSpokeToCustomerActivity extends AppCompatActivity {

    ActivityNotSpokeToCustomerBinding binding;
    View view;
    DetailsOfCustomerViewModel detailsOfCustomerViewModel;
    ArrayList<DetailsOfCustomerResponseModel> detailsList;
    public static boolean notSpokeToCustomer = false; // for Call Attempts(Hands) to display ONLY in Case if User Did Not Spoke To Customer


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not_spoke_to_customer);


        initializeFields();
        setUpDetailsOfCustomerRecyclerView();
        onClickListener();

    }

    private void initializeFields() {

        binding = DataBindingUtil.setContentView(this, R.layout.activity_not_spoke_to_customer);
        view = binding.getRoot();
        detailsOfCustomerViewModel = new ViewModelProvider(this).get(DetailsOfCustomerViewModel.class);
        binding.setViewModel(detailsOfCustomerViewModel);

        //get detailsList
        detailsList = (ArrayList<DetailsOfCustomerResponseModel>) getIntent().getSerializableExtra("detailsList");

        notSpokeToCustomer = false;
    }


    private void setUpDetailsOfCustomerRecyclerView() {

        detailsOfCustomerViewModel.updateDetailsOfCustomer_Data();
        RecyclerView recyclerView = binding.rvDetailsOfCustomer;
        recyclerView.setAdapter(new DetailsOfCustomerAdapter(detailsList));
    }


    private void onClickListener() {

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.ivHome.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity3API.class));
        });

        //for Notes
        binding.ivNotesIcon.setOnClickListener(v -> {

            Global.showNotesEditDialog(this);
        });

        //for History
        binding.ivHistory.setOnClickListener(v -> {

            String dataSetId = getIntent().getStringExtra("dataSetId");
            Global.showNotesHistoryDialog(this,dataSetId);

        });


        binding.btnNoResponseBusy.setOnClickListener(v -> {
            //From CallsForTheDayAdapter
            if(getIntent().hasExtra("isFromCallsForTheDayAdapter")){

                notSpokeToCustomer = true; // if Not Spoke To Customer is True Only then Show Call Attempts(Hands)

                Intent i = new Intent(this, CallsForTheDayActivity.class);
                startActivity(i);
            }

            //From NPA
            else{

                notSpokeToCustomer = true; // if Not Spoke To Customer is True Only then Show Call Attempts(Hands)

                // Get DPD_row_position saved in SharedPreference in DPD_Adapter Class
                int DPD_row_position = Integer.parseInt(Global.getStringFromSharedPref(this, "DPD_row_position"));

                Intent i = new Intent(NotSpokeToCustomerActivity.this, LoanCollectionActivity.class);
                i.putExtra("DPD_row_position", DPD_row_position);
                startActivity(i);

            }

        });

        binding.btnNotReachableSwitchedOff.setOnClickListener(v -> {
            //From CallsForTheDayAdapter
            if(getIntent().hasExtra("isFromCallsForTheDayAdapter")){

                notSpokeToCustomer = true; // if Not Spoke To Customer is True Only then Show Call Attempts(Hands)

                Intent i = new Intent(this, CallsForTheDayActivity.class);
                startActivity(i);
            }

            //From NPA
            else{

                notSpokeToCustomer = true; // if Not Spoke To Customer is True Only then Show Call Attempts(Hands)

                // Get DPD_row_position saved in SharedPreference in DPD_Adapter Class
                int DPD_row_position = Integer.parseInt(Global.getStringFromSharedPref(this, "DPD_row_position"));

                Intent i = new Intent(NotSpokeToCustomerActivity.this, LoanCollectionActivity.class);
                i.putExtra("DPD_row_position", DPD_row_position);
                startActivity(i);
            }

        });

        binding.btnNumberIsInvalid.setOnClickListener(v -> {
            Intent i = new Intent(NotSpokeToCustomerActivity.this, SubmitCompletionActivityOfCustomer.class);
            i.putExtra("dataSetId", getIntent().getStringExtra("dataSetId"));
            i.putExtra("detailsList",detailsList);
            startActivity(i);
        });

        binding.btnSpokeToFriendFamilyMember.setOnClickListener(v->{
            Intent i = new Intent(NotSpokeToCustomerActivity.this, SubmitCompletionActivityOfCustomer.class);
            i.putExtra("dataSetId", getIntent().getStringExtra("dataSetId"));
            i.putExtra("detailsList",detailsList);
            startActivity(i);
        });

    }

    // For Getting Calculated Balance Interest Result back from SharedPreference
    @Override
    protected void onResume() {
        initializeFields();
        onClickListener();
        setUpDetailsOfCustomerRecyclerView();
        super.onResume();
    }
}