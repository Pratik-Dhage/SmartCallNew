package com.example.test.npa_flow;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
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
    View customDialogCaptureDetails;
    View customDialogDateOfVisitPromised;
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
                        i.putExtra("isAlreadyPaid","isAlreadyPaid");
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
            i.putExtra("paymentInfo_WillPayLater","paymentInfo_WillPayLater"); //Used For Will Pay LumpSump
            i.putExtra("will_pay_later_update","will_pay_later_update"); // Used For Will Pay Later ->Update
            startActivity(i);

        });

        binding.btnFoNotVisited.setOnClickListener(v -> {

            //Capture Details Dialog
            customDialogCaptureDetails = LayoutInflater.from(this).inflate(R.layout.custom_dialog_capture_details, null);
            ImageView ivCancel = customDialogCaptureDetails.findViewById(R.id.ivCancel);
            Button btnCaptureDetails = customDialogCaptureDetails.findViewById(R.id.btnCaptureDetails);
            Button btnSkipAndProceed = customDialogCaptureDetails.findViewById(R.id.btnSkipAndProceed);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(customDialogCaptureDetails);
            final AlertDialog dialog = builder.create();
            dialog.setCancelable(true);
            dialog.show();

            ivCancel.setOnClickListener(v1->{
                dialog.dismiss();
            });

            btnSkipAndProceed.setOnClickListener(v1->{

                Intent i = new Intent(PaymentInfoOfCustomerActivity.this, SubmitCompletionActivityOfCustomer.class);
                i.putExtra("dataSetId", getIntent().getStringExtra("dataSetId"));
                i.putExtra("detailsList", detailsList);
                i.putExtra("isFoNotVisited","isFoNotVisited");
                i.putExtra("dateOfVisitPromised","");
                i.putExtra("foName","");
                startActivity(i);
            });

            //Date Of Visit Promised Dialog
            btnCaptureDetails.setOnClickListener(v1->{

                customDialogDateOfVisitPromised =  LayoutInflater.from(this).inflate(R.layout.custom_dialog_date_of_visit, null);
                ImageView ivClose = customDialogDateOfVisitPromised.findViewById(R.id.ivClose);
                Button btnProceed = customDialogDateOfVisitPromised.findViewById(R.id.btnProceed);
                EditText edtDateOfVisitPromised = customDialogDateOfVisitPromised.findViewById(R.id.edtDateOfVisitPromised);
                EditText edtFoName = customDialogDateOfVisitPromised.findViewById(R.id.edtFoName);

                AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
                builder2.setView(customDialogDateOfVisitPromised);
                final AlertDialog dialog2 = builder2.create();
                dialog2.setCancelable(true);
                dialog2.show();

                ivClose.setOnClickListener(v3->{
                    dialog2.dismiss();
                });

                edtDateOfVisitPromised.setOnClickListener(v3->{
                    showDatePickerDialogAndSetDate(edtDateOfVisitPromised);
                });

                edtDateOfVisitPromised.setOnFocusChangeListener((v2,hasFocus)->{
                    if(hasFocus){
                        showDatePickerDialogAndSetDate(edtDateOfVisitPromised);
                    }
                });


                btnProceed.setOnClickListener(v2->{

                    if(edtDateOfVisitPromised.getText().toString().isEmpty()){
                        edtDateOfVisitPromised.setError(getString(R.string.date_cannot_be_empty));
                    }
                    else if(edtFoName.getText().toString().isEmpty()){
                        edtFoName.setError(getString(R.string.name_cannot_be_empty));
                    }
                    else{

                        Intent i = new Intent(PaymentInfoOfCustomerActivity.this, SubmitCompletionActivityOfCustomer.class);
                        i.putExtra("dataSetId", getIntent().getStringExtra("dataSetId"));
                        i.putExtra("detailsList", detailsList);
                        i.putExtra("isFoNotVisited","isFoNotVisited");
                        i.putExtra("dateOfVisitPromised",edtDateOfVisitPromised.getText().toString().trim());//Date of Visit Promised
                        i.putExtra("foName",edtFoName.getText().toString().trim()); //FO Name
                        startActivity(i);

                        //Save DateOfVisitPromised and FoName in SharedPreference and use in DetailsOfCustomerActivity
                        Global.saveStringInSharedPref(this,"dateOfVisitPromised",edtDateOfVisitPromised.getText().toString().trim());
                        Global.saveStringInSharedPref(this,"foName",edtFoName.getText().toString().trim());

                    }


                });


            });



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
           // dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

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
                i.putExtra("isAlreadyPaid","isAlreadyPaid");
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
           // dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

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

            //Capture Details Dialog
            customDialogCaptureDetails = LayoutInflater.from(this).inflate(R.layout.custom_dialog_capture_details, null);
            ImageView ivCancel = customDialogCaptureDetails.findViewById(R.id.ivCancel);
            Button btnCaptureDetails = customDialogCaptureDetails.findViewById(R.id.btnCaptureDetails);
            Button btnSkipAndProceed = customDialogCaptureDetails.findViewById(R.id.btnSkipAndProceed);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(customDialogCaptureDetails);
            final AlertDialog dialog = builder.create();
            dialog.setCancelable(true);
            dialog.show();

            ivCancel.setOnClickListener(v1->{
                dialog.dismiss();
            });

            btnSkipAndProceed.setOnClickListener(v1->{

                Intent i = new Intent(PaymentInfoOfCustomerActivity.this, SubmitCompletionActivityOfCustomer.class);
                i.putExtra("dataSetId", getIntent().getStringExtra("dataSetId"));
                i.putExtra("detailsList", detailsList);
                i.putExtra("isLoanTakenByRelative","isLoanTakenByRelative");
                i.putExtra("relativeName","");
                i.putExtra("relativeContact","");
                startActivity(i);
            });

            //Editable Dialog
            btnCaptureDetails.setOnClickListener(v1->{

                 customDialogEditable = LayoutInflater.from(this).inflate(R.layout.custom_dialog_editable, null);
            ImageView ivCancel2 = customDialogEditable.findViewById(R.id.ivCancel);
            EditText edtRelativeName = customDialogEditable.findViewById(R.id.edtPleaseSpecifyName);
            EditText edtRelativeContact = customDialogEditable.findViewById(R.id.edtPleaseSpecifyContact);

            Button btnProceed = customDialogEditable.findViewById(R.id.btnProceed);

            AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
            builder2.setView(customDialogEditable);
            final AlertDialog dialog2 = builder2.create();
            dialog2.setCancelable(true);
            //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            dialog2.show();



            btnProceed.setOnClickListener(v2 -> {

                if(edtRelativeName.getText().toString().isEmpty()){
                    edtRelativeName.setError(getString(R.string.name_cannot_be_empty));
                }

              else  if(edtRelativeContact.length()!=10){
                    edtRelativeContact.setError(getString(R.string.enter_10digit_mobile_number));
                }

                else{
                    Intent i = new Intent(this, SubmitCompletionActivityOfCustomer.class);
                    i.putExtra("dataSetId", getIntent().getStringExtra("dataSetId"));
                    i.putExtra("detailsList", detailsList);
                    i.putExtra("isLoanTakenByRelative","isLoanTakenByRelative");
                    i.putExtra("relativeName",edtRelativeName.getText().toString().trim()); //Relative Name
                    i.putExtra("relativeContact",edtRelativeContact.getText().toString().trim()); //Relative Contact
                    startActivity(i);

                    // Save Relative Name and Contact in SharedPreferences and use in DetailsOfCustomerActivity
                    Global.saveStringInSharedPref(this,"relativeName",edtRelativeName.getText().toString().trim());
                    Global.saveStringInSharedPref(this,"relativeContact",edtRelativeContact.getText().toString().trim());

                }


            });

            ivCancel2.setOnClickListener(v2 -> {
                dialog2.dismiss();
            });

            });



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


    }


    private void showDatePickerDialogAndSetDate(final EditText editText) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Set the selected date to the EditText
                        String selectedDate = dayOfMonth + "-" + (month + 1) + "-" + year;
                        editText.setText(selectedDate);
                    }
                }, year, month, dayOfMonth);

        // Set the minimum and maximum dates allowed
         // datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000); // Will start from current date
        // datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 7));

        datePickerDialog.show();
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