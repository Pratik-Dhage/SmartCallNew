package com.example.test.fragment_visits_flow;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.test.R;
import com.example.test.databinding.ActivityVisitNpaPaymentModeBinding;
import com.example.test.fragment_visits_flow.generate_receipt.WebViewGenerateReceiptActivity;
import com.example.test.helper_classes.Global;
import com.example.test.helper_classes.NetworkUtilities;
import com.example.test.main_dashboard.MainActivity3API;
import com.example.test.npa_flow.PaymentModeStatusActivity;
import com.example.test.npa_flow.details_of_customer.DetailsOfCustomerViewModel;
import com.example.test.npa_flow.details_of_customer.adapter.DetailsOfCustomerAdapter;

import java.util.Calendar;

public class Visit_NPA_PaymentModeActivity extends AppCompatActivity {

  ActivityVisitNpaPaymentModeBinding binding;
    View view;
    DetailsOfCustomerViewModel detailsOfCustomerViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_npa_payment_mode);


        initializeFields();
        initObserver();
        if(NetworkUtilities.getConnectivityStatus(this)){
            callDetailsOfCustomerApi();
        }
        else{
            Global.showToast(this,getString(R.string.check_internet_connection));
        }
        onClickListener();
    }

    private void initializeFields() {

        binding= DataBindingUtil.setContentView(this,R.layout.activity_visit_npa_payment_mode);
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

    private void onClickListener(){

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.ivHome.setOnClickListener(v->{
            startActivity(new Intent(this, MainActivity3API.class));
        });

        binding.btnSendLinkForOnlinePayment.setOnClickListener(v->{

            Intent i = new Intent(this, PaymentModeStatusActivity.class);
            String dataSetId = getIntent().getStringExtra("dataSetId");
            i.putExtra("dataSetId",dataSetId);
            startActivity(i);
        });

        //for Cash Payment
        binding.btnCash.setOnClickListener(v->{

            View customDialog = LayoutInflater.from(this).inflate(R.layout.custom_dialog_cash, null);
            ImageView ivCancel = customDialog.findViewById(R.id.ivCancel);
            Button btnGenerateReceipt = customDialog.findViewById(R.id.btnGenerateReceipt);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(customDialog);
            final AlertDialog dialog = builder.create();
            dialog.setCancelable(true);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();

            btnGenerateReceipt.setOnClickListener(v2->{

                String dataSetId = getIntent().getStringExtra("dataSetId");
                String userId = "CA_01_001";
                String username = "CallingAgent1";
                String amount_paid = Global.getStringFromSharedPref(this,"Amount_Paid");

                //original URL
                String generateReceiptUrl = "http://43.239.52.151:8081/report/Receipt?output=PDF&dataSetId=238624&amtPaid=1645&userId=CA_01_001&userName=CallingAgent1";

                String generateReceiptUrl2 = "http://43.239.52.151:8081/report/Receipt?output=PDF&dataSetId=" + dataSetId + "&amtPaid=" + amount_paid + "&userId=" + userId + "&userName=" + username;

                //for Navigating to PDF Viewer app installed in Device
              /*  Uri uri = Uri.parse(generateReceiptUrl2);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.setDataAndType(uri,"application/pdf");
                startActivity(intent);
*/
                Intent i = new Intent(this,WebViewGenerateReceiptActivity.class);
                i.putExtra("dataSetId",dataSetId);
                startActivity(i);

            });

            ivCancel.setOnClickListener(v1->{
                dialog.dismiss();
            });

        });


            //for Cheque Payment
          binding.btnCheque.setOnClickListener(v->{

              View customDialog = LayoutInflater.from(this).inflate(R.layout.custom_dialog_cheque, null);
              ImageView ivCancel = customDialog.findViewById(R.id.ivCancel);
              Button btnProceed = customDialog.findViewById(R.id.btnProceed);
              EditText edtPleaseEnterChequeDate = customDialog.findViewById(R.id.edtPleaseEnterChequeDate);
              EditText edtPleaseEnterChequeNumber = customDialog.findViewById(R.id.edtPleaseEnterChequeNumber);
              EditText edtPleaseEnterBankName = customDialog.findViewById(R.id.edtPleaseEnterBankName);
              EditText edtPleaseEnterIfscCode = customDialog.findViewById(R.id.edtPleaseEnterIfscCode);


              AlertDialog.Builder builder = new AlertDialog.Builder(this);
              builder.setView(customDialog);
              final AlertDialog dialog = builder.create();
              dialog.setCancelable(true);
              dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
              dialog.show();


              //for Cheque Date
              edtPleaseEnterChequeDate.setOnFocusChangeListener((v1, hasFocus) -> {
                  showDatePickerDialogAndSetDate(edtPleaseEnterChequeDate);
              });

              btnProceed.setOnClickListener(v2->{

              });

              ivCancel.setOnClickListener(v1->{
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

    private void showDatePickerDialogAndSetDate(EditText editText) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Set the selected date to the EditText
                       // String selectedDate = (month + 1) + "/" + dayOfMonth + "/" + year;
                        String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                        editText.setText(selectedDate);
                    }
                }, year, month, dayOfMonth);

        // Set the minimum and maximum dates allowed
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
      //  datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() + (1000*60*60*24*7));

        datePickerDialog.show();
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