package com.example.test.fragment_visits_flow;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.test.R;
import com.example.test.databinding.ActivityVisitNpaRescheduleBinding;
import com.example.test.helper_classes.Global;
import com.example.test.helper_classes.NetworkUtilities;
import com.example.test.main_dashboard.MainActivity3API;
import com.example.test.npa_flow.VisitCompletionOfCustomerActivity;
import com.example.test.npa_flow.details_of_customer.DetailsOfCustomerViewModel;
import com.example.test.npa_flow.details_of_customer.adapter.DetailsOfCustomerAdapter;

public class Visit_NPA_RescheduledActivity extends AppCompatActivity {

    ActivityVisitNpaRescheduleBinding binding;
    View view;
    View customDialogImagePicker;
    private ActivityResultLauncher<Intent> pickImageLauncher;
    DetailsOfCustomerViewModel detailsOfCustomerViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_npa_reschedule);

        initializeFields();
        initObserver();
        if(NetworkUtilities.getConnectivityStatus(this)){
            callDetailsOfCustomerApi();
        }
        else{
            Global.showToast(this,getString(R.string.check_internet_connection));
        }
        onClickListener();
        setUpImagePicker();
    }

    private void initializeFields() {

        binding= DataBindingUtil.setContentView(this,R.layout.activity_visit_npa_reschedule);
        view = binding.getRoot();
        detailsOfCustomerViewModel = new ViewModelProvider(this).get(DetailsOfCustomerViewModel.class);
        binding.setViewModel(detailsOfCustomerViewModel);
    }

    private void callDetailsOfCustomerApi(){

        String dataSetId = getIntent().getStringExtra("dataSetId");
        detailsOfCustomerViewModel.getDetailsOfCustomer_Data(dataSetId); // call Details Of Customer API
    }


    private void setUpDetailsOfCustomerRecyclerView(){

        detailsOfCustomerViewModel.updateDetailsOfCustomer_Data();
        RecyclerView recyclerView = binding.rvDetailsOfCustomer;
        recyclerView.setAdapter(new DetailsOfCustomerAdapter(detailsOfCustomerViewModel.arrList_DetailsOfCustomer_Data));
    }

    private void initObserver(){

        if(NetworkUtilities.getConnectivityStatus(this)){

            binding.loadingProgressBar.setVisibility(View.VISIBLE);

            detailsOfCustomerViewModel.getMutDetailsOfCustomer_ResponseApi().observe(this,result->{

                if(result!=null) {

                    detailsOfCustomerViewModel.arrList_DetailsOfCustomer_Data.clear();
                    setUpDetailsOfCustomerRecyclerView();
                    detailsOfCustomerViewModel.arrList_DetailsOfCustomer_Data.addAll(result);
                    binding.loadingProgressBar.setVisibility(View.INVISIBLE);


                }
            });

            //handle  error response
            detailsOfCustomerViewModel.getMutErrorResponse().observe(this, error -> {

                if (error != null && !error.isEmpty()) {
                    Global.showSnackBar(view, error);
                    System.out.println("Here: " + error);
                } else {
                    Global.showSnackBar(view, getResources().getString(R.string.check_internet_connection));
                }
            });
        }
        else{
            Global.showToast(this,getString(R.string.check_internet_connection));
        }

    }

    private void setUpImagePicker() {
        // Initialize the ActivityResultLauncher
        pickImageLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
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

                txtUploadReceipt.setText(fileName);
                txtProceed.setVisibility(View.VISIBLE);
                ivRefreshCancel.setVisibility(View.VISIBLE);
                txtSkipAndProceed.setVisibility(View.GONE);
                ivFileUpload.setVisibility(View.GONE);
                btnUploadReceipt.setVisibility(View.INVISIBLE);

                txtProceed.setOnClickListener(v -> {
                    Intent i = new Intent(this, VisitCompletionOfCustomerActivity.class);
                    i.putExtra("dataSetId", getIntent().getStringExtra("dataSetId"));
                    i.putExtra("isFromVisitNPARescheduleActivity","isFromVisitNPARescheduleActivity");
                    startActivity(i);
                });

                ivRefreshCancel.setOnClickListener(v -> {
                    btnUploadReceipt.performClick();
                });
            }
        });
    }

    // Get the file name from the file URI
    private String getFileNameFromUri(Uri uri) {
        String fileName = null;
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
        return fileName;
    }


    private void onClickListener(){

        binding.ivBack.setOnClickListener(v->{
            onBackPressed();
        });

        binding.ivHome.setOnClickListener(v->{
            startActivity(new Intent(this, MainActivity3API.class));
        });

        binding.btnVisitRescheduled.setOnClickListener(v->{
            Intent i = new Intent(this,Visit_NPA_NotAvailableActivity.class);
            i.putExtra("dataSetId", getIntent().getStringExtra("dataSetId"));
            startActivity(i);
        });


        binding.btnPaymentAlreadyMade.setOnClickListener(v->{

            customDialogImagePicker = LayoutInflater.from(this).inflate(R.layout.custom_dialog_image_picker, null);
            ImageView ivCancel = customDialogImagePicker.findViewById(R.id.ivCancel);
            TextView txtSkipAndProceed = customDialogImagePicker.findViewById(R.id.txtSkipAndProceed);
            Button btnUploadReceipt = customDialogImagePicker.findViewById(R.id.btnUploadReceipt);


            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(customDialogImagePicker);
            final AlertDialog dialog = builder.create();
            dialog.setCancelable(true);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            dialog.show();


            btnUploadReceipt.setOnClickListener(v2 -> {

                // Open gallery to pick an image
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                pickImageLauncher.launch(Intent.createChooser(intent, "Select File"));


            });

            txtSkipAndProceed.setOnClickListener(v1 -> {
                Intent i = new Intent(this, VisitCompletionOfCustomerActivity.class);
                i.putExtra("dataSetId", getIntent().getStringExtra("dataSetId"));
                i.putExtra("isFromVisitNPARescheduleActivity","isFromVisitNPARescheduleActivity");
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
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            dialog.show();


            btnProceed.setOnClickListener(v2 -> {
                Intent i = new Intent(this, VisitCompletionOfCustomerActivity.class);
                i.putExtra("dataSetId", getIntent().getStringExtra("dataSetId"));
                i.putExtra("isFromVisitNPARescheduleActivity","isFromVisitNPARescheduleActivity");
                startActivity(i);
            });

            ivCancel.setOnClickListener(v1 -> {
                dialog.dismiss();
            });


        });

        //for Notes
        binding.ivNotesIcon.setOnClickListener(v->{

            View customDialog = LayoutInflater.from(this).inflate(R.layout.custom_dialog_box, null);

            TextView customText =  customDialog.findViewById(R.id.txtCustomDialog);
            Button customButton = customDialog.findViewById(R.id.btnCustomDialog);
            EditText customEditBox = customDialog.findViewById(R.id.edtCustomDialog);
            customEditBox.setVisibility(View.VISIBLE);

            customText.setText(getResources().getString(R.string.lead_interaction));

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(customDialog);
            final AlertDialog dialog = builder.create();
            dialog.show();

            customButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

        });

        //for History
        binding.ivHistory.setOnClickListener(v->{

            View customDialog = LayoutInflater.from(this).inflate(R.layout.custom_dialog_box, null);

            TextView customText =  customDialog.findViewById(R.id.txtCustomDialog);
            Button customButton = customDialog.findViewById(R.id.btnCustomDialog);
            TextView txtCustom = customDialog.findViewById(R.id.txtCustom);
            txtCustom.setVisibility(View.VISIBLE);


            customText.setText(getResources().getString(R.string.lead_history));

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(customDialog);
            final AlertDialog dialog = builder.create();
            dialog.show();

            customButton.setText(R.string.close);
            customButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

        });

    }


    // For Getting Calculated Balance Interest Result back from SharedPreference
    @Override
    protected void onResume() {
        initializeFields();
        onClickListener();
        initObserver();
        callDetailsOfCustomerApi();
        super.onResume();
    }
}