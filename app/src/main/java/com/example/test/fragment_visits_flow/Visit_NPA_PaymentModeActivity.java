package com.example.test.fragment_visits_flow;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
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
import com.example.test.npa_flow.VisitCompletionOfCustomerActivity;
import com.example.test.npa_flow.details_of_customer.DetailsOfCustomerResponseModel;
import com.example.test.npa_flow.details_of_customer.DetailsOfCustomerViewModel;
import com.example.test.npa_flow.details_of_customer.adapter.DetailsOfCustomerAdapter;

import java.util.ArrayList;
import java.util.Calendar;

public class Visit_NPA_PaymentModeActivity extends AppCompatActivity {

  ActivityVisitNpaPaymentModeBinding binding;
    View view;
    DetailsOfCustomerViewModel detailsOfCustomerViewModel;

    ArrayList<DetailsOfCustomerResponseModel> detailsList;
    public String navigateToPaymentModeStatusActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_npa_payment_mode);


        initializeFields();
        setUpDetailsOfCustomerRecyclerView();
        onClickListener();
    }

    private void initializeFields() {

        binding= DataBindingUtil.setContentView(this,R.layout.activity_visit_npa_payment_mode);
        view = binding.getRoot();
        detailsOfCustomerViewModel = new ViewModelProvider(this).get(DetailsOfCustomerViewModel.class);
        binding.setViewModel(detailsOfCustomerViewModel);

        //get detailsList
        detailsList = (ArrayList<DetailsOfCustomerResponseModel>) getIntent().getSerializableExtra("detailsList");

    }


    private void setUpDetailsOfCustomerRecyclerView(){

        detailsOfCustomerViewModel.updateDetailsOfCustomer_Data();
        RecyclerView recyclerView = binding.rvDetailsOfCustomer;
        recyclerView.setAdapter(new DetailsOfCustomerAdapter(detailsList));
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
            i.putExtra("isFromVisitsForTheDayFlow_Visit_NPA_PaymentModeActivity","isFromVisitsForTheDayFlow_Visit_NPA_PaymentModeActivity");
            i.putExtra("detailsList",detailsList);
            startActivity(i);
        });

        //for Cash Payment
        binding.btnCash.setOnClickListener(v->{

            //If Amount Paid is Null
            if(Global.getStringFromSharedPref(this,"Amount_Paid").isEmpty()){
                Global.showToast(this,getString(R.string.amount_paid_is_empty));
            }

            else{

                View customDialog = LayoutInflater.from(this).inflate(R.layout.custom_dialog_cash, null);
                ImageView ivCancel = customDialog.findViewById(R.id.ivCancel);
                Button btnGenerateReceipt = customDialog.findViewById(R.id.btnGenerateReceipt);

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setView(customDialog);
                final AlertDialog dialog = builder.create();
                dialog.setCancelable(true);
                // dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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
                    Uri uri = Uri.parse(generateReceiptUrl2);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    intent.setDataAndType(uri,"application/pdf");
                    startActivity(intent);

                    navigateToPaymentModeStatusActivity = "navigateToFullAmtPaid_PartialAmtPaid_WillPayLater_UI";

                    // for PDF view inside app
           /*     Intent i = new Intent(this,WebViewGenerateReceiptActivity.class);
                i.putExtra("dataSetId",dataSetId);
                startActivity(i);
*/
                });

                ivCancel.setOnClickListener(v1->{
                    dialog.dismiss();
                });

            }



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
             // dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
              dialog.show();


              //for Cheque Date
              edtPleaseEnterChequeDate.setOnFocusChangeListener((v1, hasFocus) -> {
                  showDatePickerDialogAndSetDate(edtPleaseEnterChequeDate);
              });

              btnProceed.setOnClickListener(v2->{
                  Intent i = new Intent(this,VisitCompletionOfCustomerActivity.class);
                  i.putExtra("dataSetId", getIntent().getStringExtra("dataSetId"));
                  i.putExtra("detailsList",detailsList);
                  i.putExtra("isFromVisitNPAPaymentModeActivity","isFromVisitNPAPaymentModeActivity");
                startActivity(i);
              });

              ivCancel.setOnClickListener(v1->{
                  dialog.dismiss();
              });
          });



        //for Notes
        binding.ivNotesIcon.setOnClickListener(v->{

            Global.showNotesEditDialogVisits(this);
        });

        //for History
        binding.ivHistory.setOnClickListener(v->{

            String dataSetId = getIntent().getStringExtra("dataSetId");
            Global.showNotesHistoryDialog(this,dataSetId);


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

        // After Receipt is generated and User view it in PdfViewer App , on back pressed in PdfViewer app navigate to PaymentModeStatusActivity
        if(navigateToPaymentModeStatusActivity!=null){
            Intent i = new Intent(this, PaymentModeStatusActivity.class);
            String dataSetId = getIntent().getStringExtra("dataSetId");
            i.putExtra("dataSetId", dataSetId);
            i.putExtra("detailsList", detailsList);
            i.putExtra("isFromVisitsForTheDayFlow_Visit_NPA_PaymentModeActivity","isFromVisitsForTheDayFlow_Visit_NPA_PaymentModeActivity");
            startActivity(i);

            navigateToPaymentModeStatusActivity = null ; // make it null to rest the flow
        }

        else{
            initializeFields();
            onClickListener();
            setUpDetailsOfCustomerRecyclerView();
        }

        super.onResume();
    }

}