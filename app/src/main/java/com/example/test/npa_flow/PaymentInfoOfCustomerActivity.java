package com.example.test.npa_flow;

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
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.bumptech.glide.Glide;
import com.example.test.R;

import com.example.test.databinding.ActivityPaymentInfoOfCustomer3Binding;
import com.example.test.databinding.ActivityPaymentInfoOfCustomerBinding;
import com.example.test.fragments_activity.BalanceInterestCalculationActivity;
import com.example.test.helper_classes.Global;
import com.example.test.helper_classes.NetworkUtilities;
import com.example.test.main_dashboard.MainActivity3API;
import com.example.test.npa_flow.details_of_customer.DetailsOfCustomerResponseModel;
import com.example.test.npa_flow.details_of_customer.DetailsOfCustomerViewModel;
import com.example.test.npa_flow.details_of_customer.adapter.DetailsOfCustomerAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class PaymentInfoOfCustomerActivity extends AppCompatActivity {

    ActivityPaymentInfoOfCustomerBinding binding;
    View view;
    View customDialogImagePicker;
    View customDialogEditable;
    private ActivityResultLauncher<Intent> pickImageLauncher;
    DetailsOfCustomerViewModel detailsOfCustomerViewModel;
    ArrayList<DetailsOfCustomerResponseModel> detailsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_payment_info_of_customer);

        initializeFields();
        setUpDetailsOfCustomerRecyclerView();
        onClickListener();
        setUpImagePicker();
    }

    private void initializeFields() {

        binding = DataBindingUtil.setContentView(this, R.layout.activity_payment_info_of_customer);
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

                    txtUploadReceipt.setText(fileName); // Name of Uploaded Receipt File
                    txtViewUploadedReceipt.setVisibility(View.VISIBLE);
                    txtProceed.setVisibility(View.VISIBLE);
                    ivRefreshCancel.setVisibility(View.VISIBLE);
                    txtSkipAndProceed.setVisibility(View.GONE);
                    ivFileUpload.setVisibility(View.GONE);
                    btnUploadReceipt.setVisibility(View.INVISIBLE);

                    txtProceed.setOnClickListener(v -> {
                        Intent i = new Intent(this, VisitCompletionOfCustomerActivity.class);
                        i.putExtra("dataSetId", getIntent().getStringExtra("dataSetId"));
                        i.putExtra("detailsList", detailsList);
                        startActivity(i);
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


    private void onClickListener() {

        binding.ivBack.setOnClickListener(v -> onBackPressed());

        binding.ivHome.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity3API.class));
        });

        binding.btnWillPayLater.setOnClickListener(v -> {
            Intent i = new Intent(PaymentInfoOfCustomerActivity.this, ScheduleVisitForCollectionActivity.class);
            i.putExtra("isFromPaymentInfoOfCustomerActivity", "isFromPaymentInfoOfCustomerActivity");
            i.putExtra("dataSetId", getIntent().getStringExtra("dataSetId"));
            i.putExtra("detailsList", detailsList);
            startActivity(i);

        });

        binding.btnFoNotAttendedMeeting.setOnClickListener(v -> {
            Intent i = new Intent(PaymentInfoOfCustomerActivity.this, VisitCompletionOfCustomerActivity.class);
            i.putExtra("dataSetId", getIntent().getStringExtra("dataSetId"));
            i.putExtra("detailsList", detailsList);
            startActivity(i);
        });

        binding.btnNotTakenLoan.setOnClickListener(v -> {
            Intent i = new Intent(PaymentInfoOfCustomerActivity.this, VisitCompletionOfCustomerActivity.class);
            i.putExtra("dataSetId", getIntent().getStringExtra("dataSetId"));
            i.putExtra("detailsList", detailsList);
            startActivity(i);
        });

        binding.btnAlreadyPaid.setOnClickListener(v -> {

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
                i.putExtra("detailsList", detailsList);
                startActivity(i);
            });

            ivCancel.setOnClickListener(v1 -> {
                dialog.dismiss();
            });


        });


        binding.btnOthers.setOnClickListener(v -> {

            customDialogEditable = LayoutInflater.from(this).inflate(R.layout.custom_dialog_editable, null);
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
                i.putExtra("detailsList", detailsList);
                startActivity(i);
            });

            ivCancel.setOnClickListener(v1 -> {
                dialog.dismiss();
            });


        });

        binding.btnLoanTakenByRelative.setOnClickListener(v -> {

            customDialogEditable = LayoutInflater.from(this).inflate(R.layout.custom_dialog_editable, null);
            ImageView ivCancel = customDialogEditable.findViewById(R.id.ivCancel);

            Button btnProceed = customDialogEditable.findViewById(R.id.btnProceed);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(customDialogEditable);
            final AlertDialog dialog = builder.create();
            dialog.setCancelable(true);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            dialog.show();


            btnProceed.setOnClickListener(v2 -> {
                Intent i = new Intent(this, VisitCompletionOfCustomerActivity.class);
                i.putExtra("dataSetId", getIntent().getStringExtra("dataSetId"));
                i.putExtra("detailsList", detailsList);
                startActivity(i);
            });

            ivCancel.setOnClickListener(v1 -> {
                dialog.dismiss();
            });


        });


        //for Notes
        binding.ivNotesIcon.setOnClickListener(v -> {

            View customDialog = LayoutInflater.from(this).inflate(R.layout.custom_dialog_box, null);

            TextView customText = customDialog.findViewById(R.id.txtCustomDialog);
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
        binding.ivHistory.setOnClickListener(v -> {

            View customDialog = LayoutInflater.from(this).inflate(R.layout.custom_dialog_box, null);

            TextView customText = customDialog.findViewById(R.id.txtCustomDialog);
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
        setUpDetailsOfCustomerRecyclerView();
        super.onResume();
    }
}