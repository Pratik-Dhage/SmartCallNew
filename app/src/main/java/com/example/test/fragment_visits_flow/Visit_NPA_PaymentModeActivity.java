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
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.test.R;
import com.example.test.databinding.ActivityPaymentInfoOfCustomerBinding;
import com.example.test.databinding.ActivityVisitNpaPaymentModeBinding;
import com.example.test.fragment_visits_flow.generate_receipt.WebViewGenerateReceiptActivity;
import com.example.test.fragments_activity.AlternateNumberApiCall;
import com.example.test.helper_classes.Global;
import com.example.test.helper_classes.NetworkUtilities;
import com.example.test.main_dashboard.MainActivity3API;
import com.example.test.npa_flow.PaymentModeStatusActivity;
import com.example.test.npa_flow.VisitCompletionOfCustomerActivity;
import com.example.test.npa_flow.details_of_customer.DetailsOfCustomerResponseModel;
import com.example.test.npa_flow.details_of_customer.DetailsOfCustomerViewModel;
import com.example.test.npa_flow.details_of_customer.adapter.DetailsOfCustomerAdapter;
import com.example.test.roomDB.dao.MPinDao;
import com.example.test.roomDB.dao.UserNameDao;
import com.example.test.roomDB.database.LeadListDB;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

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
        detailsList = (ArrayList<DetailsOfCustomerResponseModel>) Global.getUpdatedDetailsList(detailsList); //to get Updated List
    }


    private void setUpDetailsOfCustomerRecyclerView(){

        detailsOfCustomerViewModel.updateDetailsOfCustomer_Data();
        RecyclerView recyclerView = binding.rvDetailsOfCustomer;
        recyclerView.setAdapter(new DetailsOfCustomerAdapter(this,detailsList));
    }



    private void onClickListener(){

        //Call Save Alternate Number API
        if(null!= DetailsOfCustomerAdapter.alternateNumber && null!=DetailsOfCustomerAdapter.dataSetId){
            System.out.println("Save Alternate No. Api dataSetId:"+DetailsOfCustomerAdapter.dataSetId+" Alternate No. :"+DetailsOfCustomerAdapter.alternateNumber);
            AlternateNumberApiCall.saveAlternateNumber(this,DetailsOfCustomerAdapter.alternateNumber,DetailsOfCustomerAdapter.dataSetId);
        }

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

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Pending Requirement");
            builder.setMessage("This requirement is pending");
            builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();

            //Commented for Temporary Basis as Payment Gateway Requirement will come here later from Client

            /* Intent i = new Intent(this, PaymentModeStatusActivity.class);
            String dataSetId = getIntent().getStringExtra("dataSetId");
            i.putExtra("dataSetId",dataSetId);
            i.putExtra("isVisitsReadyToPaySendLinkForOnlinePayment","isVisitsReadyToPaySendLinkForOnlinePayment");
            i.putExtra("isFromVisitsForTheDayFlow_Visit_NPA_PaymentModeActivity","isFromVisitsForTheDayFlow_Visit_NPA_PaymentModeActivity");
            i.putExtra("detailsList",detailsList);
            startActivity(i);*/
        });

        //for Cash Payment
        binding.btnCash.setOnClickListener(v->{

            //If Amount Paid is Null
            if(Global.getStringFromSharedPref(this,"Amount_Paid").isEmpty()){
                Global.showToast(this,getString(R.string.please_enter_amount));
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

                //To Send To Server
                VisitsFlowCallDetailsActivity.send_amountCollected = Global.getStringFromSharedPref(this,"Amount_Paid");

                btnGenerateReceipt.setOnClickListener(v2->{

                    // Get UserName , UserID , BranchCode from RoomDB

                    MPinDao mPinDao = LeadListDB.getInstance(this).mPinDao();
                    UserNameDao userNameDao = LeadListDB.getInstance(this).userNameDao();

                    MainActivity3API.UserID = mPinDao.getUserID();
                    MainActivity3API.BranchCode = mPinDao.getBranchCode();

                    String dataSetId = getIntent().getStringExtra("dataSetId");
                    String userId =  MainActivity3API.UserID;   //"CA_01_001";
                    String username =  userNameDao.getUserNameUsingUserIDInUserNameRoomDB(mPinDao.getUserID()); // "CallingAgent1";
                    String amount_paid = Global.getStringFromSharedPref(this,"Amount_Paid");

                    //original URL
                    String generateReceiptUrl = "http://43.239.52.151:8081/report/Receipt?output=PDF&dataSetId=238624&amtPaid=1645&userId=CA_01_001&userName=CallingAgent1";

                    // Using SmartCall_BaseURL5 for fast response
                    String generateReceiptUrl2 = "http://45.113.189.27:8082/report/Receipt?output=PDF&dataSetId=" + dataSetId + "&amtPaid=" + amount_paid + "&userId=" + userId + "&userName=" + username;

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

              //If Amount Paid is Null
              if(Global.getStringFromSharedPref(this,"Amount_Paid").isEmpty()){
                  Global.showToast(this,getString(R.string.please_enter_amount));
              }

              else{

                  View customDialog = LayoutInflater.from(this).inflate(R.layout.custom_dialog_cheque, null);
                  ImageView ivCancel = customDialog.findViewById(R.id.ivCancel);
                  Button btnProceed = customDialog.findViewById(R.id.btnProceed);
                  EditText edtPleaseEnterChequeDate = customDialog.findViewById(R.id.edtPleaseEnterChequeDate);
                  TextInputLayout tilChequeDate = customDialog.findViewById(R.id.tilChequeDate);
                  EditText edtPleaseEnterChequeNumber = customDialog.findViewById(R.id.edtPleaseEnterChequeNumber);
                  TextInputLayout tilChequeNumber = customDialog.findViewById(R.id.tilChequeNumber);
                  EditText edtPleaseEnterBankName = customDialog.findViewById(R.id.edtPleaseEnterBankName);
                  TextInputLayout tilBankName = customDialog.findViewById(R.id.tilBankName);
                  EditText edtPleaseEnterIfscCode = customDialog.findViewById(R.id.edtPleaseEnterIfscCode);
                  TextInputLayout tilIFSCCode = customDialog.findViewById(R.id.tilIFSCCode);
                  EditText edtPleaseEnterAmount = customDialog.findViewById(R.id.edtPleaseEnterAmount);
                  TextInputLayout tilChequeAmount = customDialog.findViewById(R.id.tilChequeAmount);

                  AlertDialog.Builder builder = new AlertDialog.Builder(this);
                  builder.setView(customDialog);
                  final AlertDialog dialog = builder.create();
                  dialog.setCancelable(true);
                  // dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                  dialog.show();


                  //for Cheque Date
                  edtPleaseEnterChequeDate.setOnFocusChangeListener((v1, hasFocus) -> {
                      if(hasFocus){
                          showDatePickerDialogAndSetDate(edtPleaseEnterChequeDate);
                          tilChequeDate.setError(null);
                      }
                  });

                  edtPleaseEnterChequeDate.setOnClickListener(v1->{
                      showDatePickerDialogAndSetDate(edtPleaseEnterChequeDate);
                      tilChequeDate.setError(null);
                  });

                  //TextWatcher for Cheque
                  CustomTextWatcherForCheque(edtPleaseEnterChequeNumber,tilChequeNumber,edtPleaseEnterBankName,tilBankName,edtPleaseEnterIfscCode,tilIFSCCode,edtPleaseEnterAmount,tilChequeAmount);

                  btnProceed.setOnClickListener(v2->{

                      // Validations for Cheque Details

                      if(edtPleaseEnterChequeDate.getText().toString().isEmpty()){
                          tilChequeDate.setError(getResources().getString(R.string.please_enter_cheque_date));
                      }


                      //Cheque Number must be  6
                  else if(edtPleaseEnterChequeNumber.getText().toString().length() < 6 || edtPleaseEnterChequeNumber.getText().toString().isEmpty()){
                          tilChequeNumber.setError(getResources().getString(R.string.cheque_number_be_six_digit));
                  }

                      else  if(edtPleaseEnterBankName.getText().toString().isEmpty()){
                          tilBankName.setError(getResources().getString(R.string.please_enter_bank_name));
                      }


                      //IFSC code must be 11 digit
                   else if(edtPleaseEnterIfscCode.getText().toString().length() < 11 || edtPleaseEnterIfscCode.getText().toString().isEmpty()){
                          tilIFSCCode.setError(getResources().getString(R.string.ifsc_code_be_eleven_digit));
                   }

                      else  if(edtPleaseEnterAmount.getText().toString().isEmpty()){
                          tilChequeAmount.setError(getResources().getString(R.string.please_enter_amount));
                      }


                      else{

                          String ChequeDate = edtPleaseEnterChequeDate.getText().toString().trim();
                          String ChequeNumber = edtPleaseEnterChequeNumber.getText().toString().trim();
                          String BankName = edtPleaseEnterBankName.getText().toString().trim();
                          String ChequeAmount = edtPleaseEnterAmount.getText().toString().trim();

                          //to Post/Send  Cheque Details to Server
                          VisitsFlowCallDetailsActivity.send_amountCollected = Global.getStringFromSharedPref(this,"Amount_Paid");
                          VisitsFlowCallDetailsActivity.send_chequeDate = edtPleaseEnterChequeDate.getText().toString().trim();
                          VisitsFlowCallDetailsActivity.send_chequeNumber = edtPleaseEnterChequeNumber.getText().toString().trim();
                          VisitsFlowCallDetailsActivity.send_bankName = edtPleaseEnterBankName.getText().toString().trim();
                          VisitsFlowCallDetailsActivity.send_chequeAmount = edtPleaseEnterAmount.getText().toString().trim();

                          Intent i = new Intent(this,PaymentModeStatusActivity.class);
                          i.putExtra("dataSetId", getIntent().getStringExtra("dataSetId"));
                          i.putExtra("detailsList",detailsList);
                          i.putExtra("isVisitsReadyToPayChequePayment","isVisitsReadyToPayChequePayment");
                          i.putExtra("isFromVisitsForTheDayFlow_Visit_NPA_PaymentModeActivity","isFromVisitsForTheDayFlow_Visit_NPA_PaymentModeActivity");
                          i.putExtra("ChequeDate",ChequeDate);
                          i.putExtra("ChequeNumber",ChequeNumber);
                          i.putExtra("BankName",BankName);
                          i.putExtra("ChequeAmount",ChequeAmount);

                          startActivity(i);
                      }

                  });

                  ivCancel.setOnClickListener(v1->{
                      dialog.dismiss();
                  });


              }


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

    private void CustomTextWatcherForCheque(EditText edtPleaseEnterChequeNumber, TextInputLayout tilChequeNumber, EditText edtPleaseEnterBankName, TextInputLayout tilBankName, EditText edtPleaseEnterIfscCode, TextInputLayout tilIFSCCode, EditText edtPleaseEnterAmount, TextInputLayout tilChequeAmount) {

        //ChequeNumber
      edtPleaseEnterChequeNumber.addTextChangedListener(new TextWatcher() {
          @Override
          public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

          @Override
          public void onTextChanged(CharSequence s, int start, int before, int count) {
            tilChequeNumber.setError(null);
          }

          @Override
          public void afterTextChanged(Editable s) {}
      });

      //BankName
        edtPleaseEnterBankName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tilBankName.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        //IFSC Code
        edtPleaseEnterIfscCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tilIFSCCode.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        //ChequeAmount
        edtPleaseEnterAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tilChequeAmount.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {}
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

                        // Format the selected date with leading zeros
                        String formattedDayOfMonth = String.format(Locale.getDefault(), "%02d", dayOfMonth);
                        String formattedMonth = String.format(Locale.getDefault(), "%02d", month + 1);
                        String selectedDate = formattedDayOfMonth + "/" + formattedMonth + "/" + year;

                        // Set the selected date to the EditText
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
            i.putExtra("isVisitsReadyToPayCashPayment","isVisitsReadyToPayCashPayment");
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

    public ActivityVisitNpaPaymentModeBinding getBinding(){
        return binding;
    }
}