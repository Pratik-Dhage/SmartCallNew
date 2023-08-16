package com.example.test.fragment_visits_flow;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.test.R;
import com.example.test.api_manager.WebServices;
import com.example.test.databinding.ActivityVisitNpaNotAvailableBinding;
import com.example.test.fragments_activity.AlternateNumberApiCall;
import com.example.test.helper_classes.Global;
import com.example.test.helper_classes.NetworkUtilities;
import com.example.test.main_dashboard.MainActivity3API;
import com.example.test.npa_flow.ScheduleVisitForCollectionActivity;
import com.example.test.npa_flow.SubmitCompletionActivityOfCustomer;
import com.example.test.npa_flow.VisitCompletionOfCustomerActivity;
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
     AlertDialog dialogUploadReceipt;
    public  ActivityResultLauncher<Intent> pickImageLauncher;
    public View customDialogImagePicker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_npa_not_available);

        initializeFields();
        setUpDetailsOfCustomerRecyclerView();
        onClickListener();
        callRadioButtonReasonApiNeedToCloseVisit();
        initObserverRadioButtonDataNeedToCloseVisit();
        setUpImagePicker();
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

    public void showOthersDialog(){
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

        dialog.show();

        //TextWatcher For Others
        Global.CustomTextWatcher(edtPleaseSpecify , tilSpecify);

        btnProceed.setOnClickListener(v2 -> {

            if(edtPleaseSpecify.getText().toString().isEmpty()){
                tilSpecify.setError(getResources().getString(R.string.please_specify_reason));
            }
            else{
                //Reason for Others
                VisitsFlowCallDetailsActivity.send_reason = edtPleaseSpecify.getText().toString().trim();
                showDialogCloseAccount();
                dialog.dismiss();
            }

        });

        ivCancel.setOnClickListener(v1 -> {
            dialog.dismiss();
        });
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

            // for NeedToCloseVisit(No Outstanding Dues & Customer Relocated & Others & PaymentAlreadyMade)
            VisitsFlowCallDetailsActivity visitsFlowCallDetailsActivity = new VisitsFlowCallDetailsActivity();
            visitsFlowViewModel.postScheduleCall_ScheduleVisit("activity/submitcall?flow=DNVTC-VR-NTCV",dataSetId,"","","","","",VisitsFlowCallDetailsActivity.send_reason,"","","","","",visitsFlowCallDetailsActivity.sendCallLogDetailsList_VisitsFlow());
            dialogYesNo.dismiss();
            dialogRadioButton.dismiss();
            initObserverScheduleCallScheduleVisit();

        });

        btnNo.setOnClickListener(v->{

            System.out.println("RadioButtonsCloseVisitAdapter.closeVisitReasonGenericId:"+RadioButtonsCloseVisitAdapter.closeVisitReasonGenericId);
            //For PaymentAlreadyMade(genericId=19) navigate to VisitCompletionOfCustomerActivity
            if(RadioButtonsCloseVisitAdapter.closeVisitReasonGenericId==19){

                Intent i = new Intent(this, VisitCompletionOfCustomerActivity.class);
                i.putExtra("dataSetId", dataSetId);
                i.putExtra("detailsList", detailsList);
                i.putExtra("Visit_Npa_NotAvailable_NeedToCloseVisit","Visit_Npa_NotAvailable_NeedToCloseVisit"); //for ScheduleCall & ScheduleVisit
                i.putExtra("Visit_NPA_NotAvailableActivity_NeedToCloseVisit_PaymentAlreadyMade","Visit_NPA_NotAvailableActivity_NeedToCloseVisit_PaymentAlreadyMade"); //for Complete
                startActivity(i);
            }

            //For No Outstanding Dues,Customer Relocated,Others
            else {
                //Navigating to SubmitCompletionActivityOfCustomer and Displaying only 2 buttons Schedule Call , Schedule Visit
                Intent i = new Intent(this, SubmitCompletionActivityOfCustomer.class);
                i.putExtra("dataSetId", dataSetId);
                i.putExtra("detailsList", detailsList);
                i.putExtra("Visit_Npa_NotAvailable_NeedToCloseVisit","Visit_Npa_NotAvailable_NeedToCloseVisit");
                startActivity(i);
            }

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

            Intent i = new Intent(this, Visit_NPA_RescheduledActivity.class);
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

                    Intent i = new Intent(this, Visit_NPA_RescheduledActivity.class);
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

                    Intent i = new Intent(this, Visit_NPA_RescheduledActivity.class);
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

    private void initObserverScheduleCallScheduleVisit(){

        if(NetworkUtilities.getConnectivityStatus(this)){

            visitsFlowViewModel.getMutVisitsCallDetailsResponseApi().observe(this,result->{

                if(result!=null){
                    System.out.println("ScheduleCall_ScheduleVisit Response:"+result);
                    Global.showSnackBar(view,result);

                    navigateToDashBoard();
                }

            });

            //handle error response
            visitsFlowViewModel.getMutErrorResponse().observe(this, error -> {

                if (error != null && !error.isEmpty()) {

                    System.out.println("Here ScheduleCall_ScheduleVisit  Exception: " + error);

                } else {
                    Global.showSnackBar(view, getResources().getString(R.string.check_internet_connection));
                }
            });

        }
        else {
            Global.showSnackBar(view, getResources().getString(R.string.check_internet_connection));
        }

    }

    private void navigateToDashBoard(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(Visit_NPA_NotAvailableActivity.this,MainActivity3API.class);
                startActivity(i);
            }
        },1000);
    }


    public void showUploadReceiptDialog(Context context){
        customDialogImagePicker = LayoutInflater.from(context).inflate(R.layout.custom_dialog_image_picker, null);
        ImageView ivCancel = customDialogImagePicker.findViewById(R.id.ivCancel);
        TextView txtSkipAndProceed = customDialogImagePicker.findViewById(R.id.txtSkipAndProceed);
        Button btnUploadReceipt = customDialogImagePicker.findViewById(R.id.btnUploadReceipt);


        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(customDialogImagePicker);
         dialogUploadReceipt = builder.create();
        dialogUploadReceipt.show();

        ivCancel.setOnClickListener(v1 -> {
            dialogUploadReceipt.dismiss();
        });

        btnUploadReceipt.setOnClickListener(v2 -> {

            // Open gallery to pick an image
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            pickImageLauncher.launch(Intent.createChooser(intent, "Select File"));


        });

       txtSkipAndProceed.setOnClickListener(v->{
           Intent i = new Intent(this, SubmitCompletionActivityOfCustomer.class);
           i.putExtra("dataSetId", getIntent().getStringExtra("dataSetId"));
           i.putExtra("detailsList", detailsList);
           i.putExtra("Visit_Npa_NotAvailable_NeedToCloseVisit","Visit_Npa_NotAvailable_NeedToCloseVisit"); //for ScheduleCall & ScheduleVisit
           i.putExtra("Visit_NPA_NotAvailableActivity_NeedToCloseVisit_PaymentAlreadyMade","Visit_NPA_NotAvailableActivity_NeedToCloseVisit_PaymentAlreadyMade"); //for Submit
           startActivity(i);

           dialogRadioButton.dismiss();
           dialogUploadReceipt.dismiss();
       });

    }

    private void setUpImagePicker() {
        // Initialize the ActivityResultLauncher
        pickImageLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {

                try {

                    // Get the file URI and file name
                    Uri uri = result.getData().getData();
                    String fileName = getFileNameFromUri(uri);

                    // Set the file name on the TextView
                    TextView txtUploadReceipt = customDialogImagePicker.findViewById(R.id.txtUploadReceipt);
                    TextView txtProceed = customDialogImagePicker.findViewById(R.id.txtProceed);
                    TextView txtSkipAndProceed = customDialogImagePicker.findViewById(R.id.txtSkipAndProceed);
                    Button btnUploadReceipt = customDialogImagePicker.findViewById(R.id.btnUploadReceipt);
                    ImageView ivRefreshCancel = customDialogImagePicker.findViewById(R.id.ivRefreshCancel);
                    ImageView ivFileUpload = customDialogImagePicker.findViewById(R.id.ivFileUpload);
                    ImageView ivCancel = customDialogImagePicker.findViewById(R.id.ivCancel);
                    ImageView ivViewUploadedReceipt = customDialogImagePicker.findViewById(R.id.ivViewUploadedReceipt);
                    TextView txtViewUploadedReceipt = customDialogImagePicker.findViewById(R.id.txtViewUploadedReceipt);
                    TextView txtCloseUploadedReceipt = customDialogImagePicker.findViewById(R.id.txtCloseUploadedReceipt);

                    if(null!=fileName){
                        txtUploadReceipt.setText(fileName); // Name of Uploaded Receipt File
                    }

                    txtViewUploadedReceipt.setVisibility(View.VISIBLE);
                    txtProceed.setVisibility(View.VISIBLE);
                    ivRefreshCancel.setVisibility(View.VISIBLE);
                    txtSkipAndProceed.setVisibility(View.GONE);
                    ivFileUpload.setVisibility(View.GONE);
                    btnUploadReceipt.setVisibility(View.INVISIBLE);

                    txtProceed.setOnClickListener(v -> {
                        dialogUploadReceipt.dismiss();
                        showDialogCloseAccount();
                    });

                    ivRefreshCancel.setOnClickListener(v -> {
                        btnUploadReceipt.performClick();
                    });

                    txtViewUploadedReceipt.setOnClickListener(v -> {
                        if (fileName != null || !fileName.isEmpty()) {
                            ivViewUploadedReceipt.setVisibility(View.VISIBLE);
                            txtCloseUploadedReceipt.setVisibility(View.VISIBLE);
                            Glide.with(this).load(uri).into(ivViewUploadedReceipt); //set selected Image of Receipt
                            // Picasso.get().load(uri).into(ivViewUploadedReceipt);
                            txtViewUploadedReceipt.setVisibility(View.GONE);
                            txtProceed.setVisibility(View.GONE);
                            txtUploadReceipt.setVisibility(View.GONE);
                            ivRefreshCancel.setVisibility(View.GONE);

                            if (ivViewUploadedReceipt.getVisibility() == View.VISIBLE) {
                                ivCancel.setVisibility(View.INVISIBLE);
                            }


                        }
                    });

                    txtCloseUploadedReceipt.setOnClickListener(v -> {
                        txtViewUploadedReceipt.setVisibility(View.VISIBLE);
                        ivCancel.setVisibility(View.VISIBLE);
                        txtProceed.setVisibility(View.VISIBLE);
                        txtUploadReceipt.setVisibility(View.VISIBLE);
                        ivRefreshCancel.setVisibility(View.VISIBLE);
                        txtCloseUploadedReceipt.setVisibility(View.GONE);
                        ivViewUploadedReceipt.setVisibility(View.GONE);

                    });


                } catch (Exception e) {

                    Log.d("File Receipt Exception", "File Receipt Exception:" + e);
                    //  Global.showToast(this,"File Receipt Exception:"+e.getLocalizedMessage());
                }


            }
        });
    }

    // Get the file name from the file URI
    private String getFileNameFromUri(Uri uri) {

        String fileName="";
        try{
             fileName = null;
            String scheme = uri.getScheme();
            if (scheme != null && scheme.equals("content")) {
                Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    int columnIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (columnIndex != -1) {
                        fileName = cursor.getString(columnIndex);
                    }
                    cursor.close();
                }
            }
            if (fileName == null) {
                fileName = uri.getPath();
                int cut = fileName.lastIndexOf('/');
                if (cut != -1) {
                    fileName = fileName.substring(cut + 1);
                }
            }

        }
        catch (Exception e){
            e.printStackTrace();
            System.out.println("Visit_NPA_NotAvailableActivity getFileNameFromUri Exception:"+e);
        }


        return fileName;
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