package com.example.test.fragment_visits_flow;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.test.R;
import com.example.test.api_manager.WebServices;
import com.example.test.databinding.ActivityVisitNpaNotAvailableBinding;
import com.example.test.fragments_activity.AlternateNumberApiCall;
import com.example.test.helper_classes.Global;
import com.example.test.helper_classes.NetworkUtilities;
import com.example.test.main_dashboard.MainActivity3API;
import com.example.test.npa_flow.ScheduleVisitForCollectionActivity;
import com.example.test.npa_flow.SubmitCompletionActivityOfCustomer;
import com.example.test.npa_flow.details_of_customer.DetailsOfCustomerResponseModel;
import com.example.test.npa_flow.details_of_customer.DetailsOfCustomerViewModel;
import com.example.test.npa_flow.details_of_customer.adapter.DetailsOfCustomerAdapter;
import com.example.test.npa_flow.radio_buttons.RadioButtonsCloseVisitAdapter;
import com.example.test.npa_flow.radio_buttons.RadioButtonsViewModel;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class Visit_NPA_NotAvailableActivity extends AppCompatActivity {

    ActivityVisitNpaNotAvailableBinding binding;
    View view;
    DetailsOfCustomerViewModel detailsOfCustomerViewModel;
    public static ArrayList<DetailsOfCustomerResponseModel> detailsList;
    public static String  dataSetId; // for showDialogCloseAccount()
    RadioButtonsViewModel radioButtonsViewModel;
    public static boolean isRadioButtonSelected = false;
    VisitsFlowViewModel visitsFlowViewModel;
     AlertDialog dialogRadioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_npa_not_available);

        initializeFields();
        setUpDetailsOfCustomerRecyclerView();
        onClickListener();
        callRadioButtonReasonApiNeedToCloseVisit();
        initObserverRadioButtonDataNeedToCloseVisit();
    }

    private void initializeFields() {

        binding = DataBindingUtil.setContentView(this, R.layout.activity_visit_npa_not_available);
        view = binding.getRoot();
        detailsOfCustomerViewModel = new ViewModelProvider(this).get(DetailsOfCustomerViewModel.class);
        binding.setViewModel(detailsOfCustomerViewModel);
        radioButtonsViewModel = new ViewModelProvider(this).get(RadioButtonsViewModel.class);
        visitsFlowViewModel = new ViewModelProvider(this).get(VisitsFlowViewModel.class);

        dataSetId = getIntent().getStringExtra("dataSetId");
        //get detailsList
        detailsList = (ArrayList<DetailsOfCustomerResponseModel>) getIntent().getSerializableExtra("detailsList");
        detailsList = (ArrayList<DetailsOfCustomerResponseModel>) Global.getUpdatedDetailsList(detailsList); //to get Updated List

    }


    private void setUpDetailsOfCustomerRecyclerView() {

        detailsOfCustomerViewModel.updateDetailsOfCustomer_Data();
        RecyclerView recyclerView = binding.rvDetailsOfCustomer;
        recyclerView.setAdapter(new DetailsOfCustomerAdapter(this,detailsList));
    }

    private void callRadioButtonReasonApiNeedToCloseVisit(){
        if(NetworkUtilities.getConnectivityStatus(this)){
            radioButtonsViewModel.getRadioButtonsReason_Data(WebServices.radio_button_need_to_close_visit);
        }
        else{
            Global.showSnackBar(view,getString(R.string.check_internet_connection));
        }
    }

    private void initObserverRadioButtonDataNeedToCloseVisit(){
        if(NetworkUtilities.getConnectivityStatus(this)){
            radioButtonsViewModel.getMutRadioButtonsReason_ResponseApi().observe(this,result2->{

                if(result2!=null){
                    radioButtonsViewModel.arrList_RadioButtonsReason_Data2.clear();
                    radioButtonsViewModel.arrList_RadioButtonsReason_Data2.addAll(result2);
                }
            });

            radioButtonsViewModel.getMutErrorResponse().observe(this,error->{
                if (error != null && !error.isEmpty()) {
                    Global.showSnackBar(view, error);
                    System.out.println("Here error : " + error);
                    //Here error : End of input at line 1 column 1 path $ (if Server response body is empty, we get this error)
                }
            });
        }

        else{
            Global.showSnackBar(view,getString(R.string.check_internet_connection));
        }

    }

    public  void showDialogCloseAccount(){

        View customDialogYesNo = LayoutInflater.from(this).inflate(R.layout.custom_dialog_yes_no, null);
        TextView txtCustomDialogYesNo = customDialogYesNo.findViewById(R.id.txtCustomDialogYesNo);
        txtCustomDialogYesNo.setText(R.string.do_you_want_to_close_this_acc_permanently);
        ImageView ivClose = customDialogYesNo.findViewById(R.id.ivClose);
        Button btnYes = customDialogYesNo.findViewById(R.id.btnYes);
        Button btnNo = customDialogYesNo.findViewById(R.id.btnNo);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(customDialogYesNo);
        final AlertDialog dialogYesNo = builder.create();
        dialogYesNo.show();

        ivClose.setOnClickListener(v->{dialogYesNo.dismiss();});

        btnYes.setOnClickListener(v->{

            // Api call
            VisitsFlowCallDetailsActivity visitsFlowCallDetailsActivity = new VisitsFlowCallDetailsActivity();
            visitsFlowViewModel.postScheduleCall_ScheduleVisit("activity/submitcall?flow=DNVTC-VR-NTCV",dataSetId,"","","","","",VisitsFlowCallDetailsActivity.send_reason,"","","","","",visitsFlowCallDetailsActivity.sendCallLogDetailsList_VisitsFlow());

            dialogYesNo.dismiss();
            dialogRadioButton.dismiss();
        });

        btnNo.setOnClickListener(v->{

            //Navigating to SubmitCompletionActivityOfCustomer and Displaying only 2 buttons Schedule Call , Schedule Visit
            Intent i = new Intent(this, SubmitCompletionActivityOfCustomer.class);
            i.putExtra("dataSetId", dataSetId);
            i.putExtra("detailsList", detailsList);
            i.putExtra("Visit_Npa_NotAvailable_NeedToCloseVisit","Visit_Npa_NotAvailable_NeedToCloseVisit");
            startActivity(i);

            dialogYesNo.dismiss();
            dialogRadioButton.dismiss();

        });
    }


    private void onClickListener() {

        //Call Save Alternate Number API
        if(null!= DetailsOfCustomerAdapter.alternateNumber && null!=DetailsOfCustomerAdapter.dataSetId){
            System.out.println("Save Alternate No. Api dataSetId:"+DetailsOfCustomerAdapter.dataSetId+" Alternate No. :"+DetailsOfCustomerAdapter.alternateNumber);
            AlternateNumberApiCall.saveAlternateNumber(this,DetailsOfCustomerAdapter.alternateNumber,DetailsOfCustomerAdapter.dataSetId);
        }

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

        binding.btnNeedToCloseVisit.setOnClickListener(v->{

            View customDialogRadioButton = LayoutInflater.from(this).inflate(R.layout.custom_dialog_radio_button, null);
            ImageView ivCancel = customDialogRadioButton.findViewById(R.id.ivCancel);
            TextView txtToolbarHeading = customDialogRadioButton.findViewById(R.id.txtToolbarHeading);
            txtToolbarHeading.setText(R.string.select_reason); //toolbar Heading
            Button btnProceed = customDialogRadioButton.findViewById(R.id.btnProceed);
            btnProceed.setVisibility(View.GONE); // hide Proceed Button
            RecyclerView recyclerView = customDialogRadioButton.findViewById(R.id.rvRadioButton);


            //setUpRadioButtonDataRecyclerView()
            radioButtonsViewModel.updateRadioButtonCloseVisit_Data();
            recyclerView.setAdapter(new RadioButtonsCloseVisitAdapter(radioButtonsViewModel.arrList_RadioButtonsReason_Data2));

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(customDialogRadioButton);
            dialogRadioButton = builder.create();
            dialogRadioButton.setCancelable(true);
            dialogRadioButton.show();

            ivCancel.setOnClickListener(v1 -> {
                dialogRadioButton.dismiss();
            });

        });

        binding.btnLateForVisit.setOnClickListener(v -> {

            View customDialogEditable = LayoutInflater.from(this).inflate(R.layout.custom_dialog_editable, null);
            ImageView ivCancel = customDialogEditable.findViewById(R.id.ivCancel);

            Button btnProceed = customDialogEditable.findViewById(R.id.btnProceed);
            EditText edtPleaseSpecify = customDialogEditable.findViewById(R.id.edtPleaseSpecifyName);
            TextInputLayout tilSpecify = customDialogEditable.findViewById(R.id.tilSpecifyName);
            EditText edtPleaseSpecifyContact = customDialogEditable.findViewById(R.id.edtPleaseSpecifyContact);
            edtPleaseSpecify.setHint(getString(R.string.please_specify_reason));
            edtPleaseSpecifyContact.setVisibility(View.GONE);


            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(customDialogEditable);
            final AlertDialog dialog = builder.create();
            dialog.setCancelable(true);
           // dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            dialog.show();

            //TextWatcher For Reason
            Global.CustomTextWatcher(edtPleaseSpecify , tilSpecify);

            btnProceed.setOnClickListener(v2 -> {

                if(edtPleaseSpecify.getText().toString().isEmpty()){
                    tilSpecify.setError(getResources().getString(R.string.please_specify_reason));
                }

                else{
                    VisitsFlowCallDetailsActivity.send_reason = edtPleaseSpecify.getText().toString().trim(); // send reason to Server

                    Intent i = new Intent(this, ScheduleVisitForCollectionActivity.class);
                    i.putExtra("dataSetId", getIntent().getStringExtra("dataSetId"));
                    i.putExtra("detailsList", detailsList);
                    i.putExtra("isFromVisitNPANotAvailableActivity", "isFromVisitNPANotAvailableActivity");
                    i.putExtra("isFromVisitNPANotAvailableActivity_LateForVisit","isFromVisitNPANotAvailableActivity_LateForVisit");
                    startActivity(i);
                }


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
            TextInputLayout tilSpecify = customDialogEditable.findViewById(R.id.tilSpecifyName);
            EditText edtPleaseSpecifyContact = customDialogEditable.findViewById(R.id.edtPleaseSpecifyContact);
            edtPleaseSpecify.setHint(getString(R.string.please_specify));
            edtPleaseSpecifyContact.setVisibility(View.GONE);


            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(customDialogEditable);
            final AlertDialog dialog = builder.create();
            dialog.setCancelable(true);
           // dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            dialog.show();

            //TextWatcher For Others
            Global.CustomTextWatcher(edtPleaseSpecify , tilSpecify);

            btnProceed.setOnClickListener(v2 -> {

                if(edtPleaseSpecify.getText().toString().isEmpty()){
                    tilSpecify.setError(getResources().getString(R.string.please_specify_reason));
                }

                else{
                    VisitsFlowCallDetailsActivity.send_reason = edtPleaseSpecify.getText().toString().trim();

                    Intent i = new Intent(this, ScheduleVisitForCollectionActivity.class);
                    i.putExtra("dataSetId", getIntent().getStringExtra("dataSetId"));
                    i.putExtra("detailsList", detailsList);
                    i.putExtra("isFromVisitNPANotAvailableActivity", "isFromVisitNPANotAvailableActivity");
                    i.putExtra("isFromVisitNPANotAvailableActivity_Others","isFromVisitNPANotAvailableActivity_Others");
                    startActivity(i);
                }


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

    public ActivityVisitNpaNotAvailableBinding getBinding(){
        return binding;
    }
}