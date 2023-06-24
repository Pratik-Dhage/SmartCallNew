package com.example.test.fragment_visits_flow;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.test.R;
import com.example.test.databinding.ActivityVisitNpaNotAvailableBinding;
import com.example.test.helper_classes.Global;
import com.example.test.helper_classes.NetworkUtilities;
import com.example.test.main_dashboard.MainActivity3API;
import com.example.test.npa_flow.ScheduleVisitForCollectionActivity;
import com.example.test.npa_flow.VisitCompletionOfCustomerActivity;
import com.example.test.npa_flow.details_of_customer.DetailsOfCustomerResponseModel;
import com.example.test.npa_flow.details_of_customer.DetailsOfCustomerViewModel;
import com.example.test.npa_flow.details_of_customer.adapter.DetailsOfCustomerAdapter;

import java.util.ArrayList;

public class Visit_NPA_NotAvailableActivity extends AppCompatActivity {

    ActivityVisitNpaNotAvailableBinding binding;
    View view;
    DetailsOfCustomerViewModel detailsOfCustomerViewModel;
    ArrayList<DetailsOfCustomerResponseModel> detailsList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_npa_not_available);

        initializeFields();
        setUpDetailsOfCustomerRecyclerView();
        onClickListener();

    }

    private void initializeFields() {

        binding = DataBindingUtil.setContentView(this, R.layout.activity_visit_npa_not_available);
        view = binding.getRoot();
        detailsOfCustomerViewModel = new ViewModelProvider(this).get(DetailsOfCustomerViewModel.class);
        binding.setViewModel(detailsOfCustomerViewModel);

        //get detailsList
        detailsList = (ArrayList<DetailsOfCustomerResponseModel>) getIntent().getSerializableExtra("detailsList");

    }


    private void setUpDetailsOfCustomerRecyclerView() {

        detailsOfCustomerViewModel.updateDetailsOfCustomer_Data();
        RecyclerView recyclerView = binding.rvDetailsOfCustomer;
        recyclerView.setAdapter(new DetailsOfCustomerAdapter(detailsList));
    }


    private void onClickListener() {

        binding.ivBack.setOnClickListener(v -> {
            onBackPressed();
        });

        binding.ivHome.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity3API.class));
        });

        binding.btnCustomerNotAvailable.setOnClickListener(v -> {

            Intent i = new Intent(this, ScheduleVisitForCollectionActivity.class);
            i.putExtra("dataSetId", getIntent().getStringExtra("dataSetId"));
            i.putExtra("detailsList", detailsList);
            i.putExtra("isFromVisitNPANotAvailableActivity", "isFromVisitNPANotAvailableActivity");
            i.putExtra("isFromVisitNPANotAvailableActivity_CustomerNotAvailable","isFromVisitNPANotAvailableActivity_CustomerNotAvailable");
            startActivity(i);

        });

        binding.btnLateForVisit.setOnClickListener(v -> {

            View customDialogEditable = LayoutInflater.from(this).inflate(R.layout.custom_dialog_editable, null);
            ImageView ivCancel = customDialogEditable.findViewById(R.id.ivCancel);

            Button btnProceed = customDialogEditable.findViewById(R.id.btnProceed);
            EditText edtPleaseSpecify = customDialogEditable.findViewById(R.id.edtPleaseSpecifyName);
            EditText edtPleaseSpecifyContact = customDialogEditable.findViewById(R.id.edtPleaseSpecifyContact);
            edtPleaseSpecify.setHint(getString(R.string.please_specify_reason));
            edtPleaseSpecifyContact.setVisibility(View.GONE);


            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(customDialogEditable);
            final AlertDialog dialog = builder.create();
            dialog.setCancelable(true);
           // dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            dialog.show();


            btnProceed.setOnClickListener(v2 -> {

                VisitsFlowCallDetailsActivity.send_reason = edtPleaseSpecify.getText().toString().trim(); // send reason to Server

                Intent i = new Intent(this, ScheduleVisitForCollectionActivity.class);
                i.putExtra("dataSetId", getIntent().getStringExtra("dataSetId"));
                i.putExtra("detailsList", detailsList);
                i.putExtra("isFromVisitNPANotAvailableActivity", "isFromVisitNPANotAvailableActivity");
                i.putExtra("isFromVisitNPANotAvailableActivity_LateForVisit","isFromVisitNPANotAvailableActivity_LateForVisit");
                startActivity(i);
            });

            ivCancel.setOnClickListener(v1 -> {
                dialog.dismiss();
            });


        });

        binding.btnOthers.setOnClickListener(v -> {

            View customDialogEditable = LayoutInflater.from(this).inflate(R.layout.custom_dialog_editable, null);
            ImageView ivCancel = customDialogEditable.findViewById(R.id.ivCancel);

            Button btnProceed = customDialogEditable.findViewById(R.id.btnProceed);
            EditText edtPleaseSpecify = customDialogEditable.findViewById(R.id.edtPleaseSpecifyName);
            EditText edtPleaseSpecifyContact = customDialogEditable.findViewById(R.id.edtPleaseSpecifyContact);
            edtPleaseSpecify.setHint(getString(R.string.please_specify));
            edtPleaseSpecifyContact.setVisibility(View.GONE);


            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(customDialogEditable);
            final AlertDialog dialog = builder.create();
            dialog.setCancelable(true);
           // dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            dialog.show();


            btnProceed.setOnClickListener(v2 -> {

                VisitsFlowCallDetailsActivity.send_reason = edtPleaseSpecify.getText().toString().trim();

                Intent i = new Intent(this, ScheduleVisitForCollectionActivity.class);
                i.putExtra("dataSetId", getIntent().getStringExtra("dataSetId"));
                i.putExtra("detailsList", detailsList);
                i.putExtra("isFromVisitNPANotAvailableActivity", "isFromVisitNPANotAvailableActivity");
                i.putExtra("isFromVisitNPANotAvailableActivity_Others","isFromVisitNPANotAvailableActivity_Others");
                startActivity(i);
            });

            ivCancel.setOnClickListener(v1 -> {
                dialog.dismiss();
            });


        });

        //for Notes
        binding.ivNotesIcon.setOnClickListener(v -> {

            Global.showNotesEditDialogVisits(this);
        });

        //for History
        binding.ivHistory.setOnClickListener(v -> {

            String dataSetId = getIntent().getStringExtra("dataSetId");
            Global.showNotesHistoryDialog(this,dataSetId);

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