package com.example.test.npa_flow;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.test.R;

import com.example.test.databinding.ActivityPaymentInfoOfCustomer3Binding;
import com.example.test.databinding.ActivityPaymentInfoOfCustomerBinding;
import com.example.test.fragments_activity.BalanceInterestCalculationActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class PaymentInfoOfCustomerActivity extends AppCompatActivity {

    ActivityPaymentInfoOfCustomerBinding binding;
    View view ;
    View customDialogImagePicker;
    View customDialogEditable;
    private ActivityResultLauncher<Intent> pickImageLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_payment_info_of_customer);

        initializeFields();
        getDetailsOfCustomerFromIntent();
       onClickListener();
        setUpImagePicker();
    }

    private void initializeFields() {

        binding = DataBindingUtil.setContentView(this, R.layout.activity_payment_info_of_customer);
        view = binding.getRoot();
    }

    private void getDetailsOfCustomerFromIntent(){

        binding.txtName.setText(getIntent().getStringExtra("name"));
        binding.txtMobileNumber.setText(getIntent().getStringExtra("mobile_no"));
        binding.txtAadharNumber.setText(getIntent().getStringExtra("aadhaar_no"));
        binding.txtDOB.setText(getIntent().getStringExtra("dob"));
        binding.txtFatherName.setText(getIntent().getStringExtra("father_name"));
        binding.txtLoanAccountNumber.setText(getIntent().getStringExtra("loan_acc_no"));
        binding.txtProduct.setText(getIntent().getStringExtra("product"));
        binding.txtAmountDueAsOnAmount.setText(getIntent().getStringExtra("amt_due"));
        binding.txtTotalAmountPaid.setText(getIntent().getStringExtra("total_amt_paid"));
        binding.txtBalanceInterest.setText(getIntent().getStringExtra("balance_interest"));
        binding.txtTotalPayableAmount.setText(getIntent().getStringExtra("total_payable_amt"));

    }

    private void setUpImagePicker(){
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


                ivRefreshCancel.setOnClickListener(v->{
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


    private void onClickListener() {

        binding.ivBack.setOnClickListener(v -> onBackPressed());

        binding.btnNearBy.setOnClickListener(v->{
            Intent i = new Intent(this, NearByCustomersActivity.class);
            startActivity(i);
        });

        binding.btnCalculate.setOnClickListener(v->{
            Intent i = new Intent(this, BalanceInterestCalculationActivity.class);
            startActivity(i);
        });

        binding.btnWillPayLater.setOnClickListener(v->{
            Intent i = new Intent(PaymentInfoOfCustomerActivity.this,ScheduleVisitForCollectionActivity.class);
            i.putExtra("isFromPaymentInfoOfCustomerActivity","isFromPaymentInfoOfCustomerActivity");
            startActivity(i);

        });

        binding.btnFoNotAttendedMeeting.setOnClickListener(v->{
           Intent i = new Intent(PaymentInfoOfCustomerActivity.this,VisitCompletionOfCustomerActivity.class);
            i.putExtra("name",binding.txtName.getText().toString());
            i.putExtra("mobile_no",binding.txtMobileNumber.getText().toString());
            i.putExtra("aadhaar_no",binding.txtAadharNumber.getText().toString());
            i.putExtra("dob",binding.txtDOB.getText().toString());
            i.putExtra("father_name",binding.txtFatherName.getText().toString());
            i.putExtra("loan_acc_no",binding.txtLoanAccountNumber.getText().toString());
            i.putExtra("product",binding.txtProduct.getText().toString());
            i.putExtra("amt_due",binding.txtAmountDueAsOnAmount.getText().toString());
            i.putExtra("total_amt_paid",binding.txtTotalAmountPaid.getText().toString());
            i.putExtra("balance_interest",binding.txtBalanceInterest.getText().toString());
            i.putExtra("total_payable_amt",binding.txtTotalPayableAmount.getText().toString());
            startActivity(i);
        });

        binding.btnNotTakenLoan.setOnClickListener(v->{
            Intent i = new Intent(PaymentInfoOfCustomerActivity.this,VisitCompletionOfCustomerActivity.class);
            i.putExtra("name",binding.txtName.getText().toString());
            i.putExtra("mobile_no",binding.txtMobileNumber.getText().toString());
            i.putExtra("aadhaar_no",binding.txtAadharNumber.getText().toString());
            i.putExtra("dob",binding.txtDOB.getText().toString());
            i.putExtra("father_name",binding.txtFatherName.getText().toString());
            i.putExtra("loan_acc_no",binding.txtLoanAccountNumber.getText().toString());
            i.putExtra("product",binding.txtProduct.getText().toString());
            i.putExtra("amt_due",binding.txtAmountDueAsOnAmount.getText().toString());
            i.putExtra("total_amt_paid",binding.txtTotalAmountPaid.getText().toString());
            i.putExtra("balance_interest",binding.txtBalanceInterest.getText().toString());
            i.putExtra("total_payable_amt",binding.txtTotalPayableAmount.getText().toString());
            startActivity(i);
        });

        binding.btnAlreadyPaid.setOnClickListener(v->{

            customDialogImagePicker = LayoutInflater.from(this).inflate(R.layout.custom_dialog_image_picker, null);
            ImageView ivCancel = customDialogImagePicker.findViewById(R.id.ivCancel);

            Button btnUploadReceipt = customDialogImagePicker.findViewById(R.id.btnUploadReceipt);



            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(customDialogImagePicker);
            final AlertDialog dialog = builder.create();
            dialog.setCancelable(true);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            dialog.show();


            btnUploadReceipt.setOnClickListener(v2->{

                // Open gallery to pick an image
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                pickImageLauncher.launch(Intent.createChooser(intent, "Select File"));


            });

            ivCancel.setOnClickListener(v1->{
                dialog.dismiss();
            });



        });



        binding.btnOthers.setOnClickListener(v->{

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


            btnProceed.setOnClickListener(v2->{


            });

            ivCancel.setOnClickListener(v1->{
                dialog.dismiss();
            });


        });

        binding.btnLoanTakenByRelative.setOnClickListener(v->{

            customDialogEditable = LayoutInflater.from(this).inflate(R.layout.custom_dialog_editable, null);
            ImageView ivCancel = customDialogEditable.findViewById(R.id.ivCancel);

            Button btnProceed = customDialogEditable.findViewById(R.id.btnProceed);




            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(customDialogEditable);
            final AlertDialog dialog = builder.create();
            dialog.setCancelable(true);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            dialog.show();


            btnProceed.setOnClickListener(v2->{


            });

            ivCancel.setOnClickListener(v1->{
                dialog.dismiss();
            });


        });

        binding.ivGoBack2.setOnClickListener(v->{
            binding.ivGoBack2.setVisibility(View.GONE);
            binding.btnLoanTakenByRelative.setVisibility(View.VISIBLE);
            binding.edtPleaseSpecifyName.setVisibility(View.GONE);
            binding.edtPleaseSpecifyContact.setVisibility(View.GONE);
        });

        //for Editing Time
        binding.ivEditTime.setOnClickListener(v->{

            View customDialog2 = LayoutInflater.from(this).inflate(R.layout.custom_dialog_timepicker, null);
            Button customButton = customDialog2.findViewById(R.id.btnOK);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(customDialog2);
            final AlertDialog dialog = builder.create();
            dialog.setCancelable(false);
            dialog.show();

            //on Clicking OK button
            customButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // get a reference to the TimePicker
                    TimePicker timePicker = customDialog2.findViewById(R.id.customDialogTimePicker);

                    // get the selected hour and minute
                    int hour = timePicker.getHour();
                    int minute = timePicker.getMinute();

                    // create a Date object with the selected time
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.HOUR_OF_DAY, hour);
                    calendar.set(Calendar.MINUTE, minute);
                    Date selectedTime = calendar.getTime();

                    // create a SimpleDateFormat object with the desired format string
                    SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");

                    // format the time and store it in a string
                    String formattedTime = dateFormat.format(selectedTime);

                    // display the selected time
                    binding.txtScheduledTime.setText(":"+formattedTime);

                    dialog.dismiss();
                }
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
}