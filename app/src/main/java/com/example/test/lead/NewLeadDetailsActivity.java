package com.example.test.lead;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.example.test.R;
import com.example.test.databinding.ActivityNewLeadDetailsBinding;
import com.example.test.helper_classes.Global;
import com.example.test.helper_classes.NetworkUtilities;
import com.example.test.roomDB.dao.LeadDao;
import com.example.test.roomDB.database.LeadListDB;
import com.example.test.roomDB.model.LeadModelRoom;

import java.util.ArrayList;

public class NewLeadDetailsActivity extends AppCompatActivity {

    ActivityNewLeadDetailsBinding binding;
    View view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initializeFields();
        spinnerData();
        CustomTextWatcher();
        onClickListener();
    }

    private void spinnerData() {
        ArrayList<String> spinnerItems = new ArrayList<>();
        spinnerItems.add("Select Lead Source");
        spinnerItems.add("Lead Source 1");
        spinnerItems.add("Lead Source 2");
        spinnerItems.add("Lead Source 3");

        //attach data items to Array Adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerItems);

        //set DropDown to Array Adapter
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        binding.spinnerLeadSource.setAdapter(adapter);  // set Adapter to Spinner

        //Spinner
        binding.spinnerLeadSource.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                binding.txtCustomSpinner.setText(spinnerItems.get(position)); //for Custom Spinner

                if(position!=0 && validations())
                binding.btnSaveLead.setBackgroundColor(ContextCompat.getColor(NewLeadDetailsActivity.this,R.color.textBlue));
                else
                    binding.btnSaveLead.setBackgroundColor(ContextCompat.getColor(NewLeadDetailsActivity.this,R.color.borderColor));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                binding.btnSaveLead.setBackgroundColor(ContextCompat.getColor(NewLeadDetailsActivity.this,R.color.borderColor));

            }
        });


        //for Custom Spinner , because some devices wont display Spinner
        binding.clCustomSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.spinnerLeadSource.performClick();
            }
        });



    }

    private void initializeFields() {
        binding = DataBindingUtil. setContentView(this,R.layout.activity_new_lead_details);
        view = binding.getRoot();


    }

    private void enable_disable_Save_Button(){

        if(validations()){
            //if validations are true then enable Save Button
            binding.btnSaveLead.setEnabled(true);
            binding.btnSaveLead.setBackgroundColor(ContextCompat.getColor(NewLeadDetailsActivity.this,R.color.textBlue));

        }
        else{
            binding.btnSaveLead.setEnabled(false);
            binding.btnSaveLead.setBackgroundColor(ContextCompat.getColor(NewLeadDetailsActivity.this,R.color.borderColor));
        }
    }

    private void onClickListener() {

        //Back to Lead List
        binding.btnBackToLeadList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // go back to Lead List
            }
        });

         // Save Button
        binding.btnSaveLead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(NetworkUtilities.getConnectivityStatus(NewLeadDetailsActivity.this)){

                    if(validations()){


                        // for Test purpose Online Storage of Lead is also stored in RoomDB
                        enable_disable_Save_Button();
                        storageOfLeadOffline();

                    }

                }
                else{

                    // Store New Lead in Room Database for Offline Purpose

                   if(validations()){
                       enable_disable_Save_Button();
                       storageOfLeadOffline();

                   }


                }


            }
        });

    }

    private boolean validations(){

        if(binding.edtLeadFirstName.getText().toString().isEmpty()){
           binding.inputLayoutFirstName.setError(getResources().getString(R.string.lead_first_name_cannot_be_empty));
            return false;
        }

        if(binding.edtLeadLastName.getText().toString().isEmpty()){
            binding.inputLayoutLastName.setError(getResources().getString(R.string.lead_last_name_cannot_be_empty));
            return false;
        }

        if(binding.edtLeadMobileNumber.getText().toString().isEmpty()){
            binding.inputLayoutMobileNumber.setError(getResources().getString(R.string.lead_mobile_number_cannot_be_empty));
            return false;
        }

        if(binding.edtLeadMobileNumber.getText().toString().startsWith("0")){
            binding.inputLayoutMobileNumber.setError(getResources().getString(R.string.mobile_number_cannot_begin_with_zero));
            return false;
        }

        if(!binding.edtLeadMobileNumber.getText().toString().matches("^[1-9][0-9]{9}$")){
            binding.inputLayoutMobileNumber.setError(getResources().getString(R.string.please_enter_proper_lead_number));
            return false;
        }

         if(binding.spinnerLeadSource.getSelectedItemPosition()==0){
             Global.showToast(this,getResources().getString(R.string.please_select_lead_source));
             binding.btnSaveLead.setBackgroundColor(ContextCompat.getColor(NewLeadDetailsActivity.this,R.color.borderColor));;
             return false;
         }


        return true;
    }

    private void CustomTextWatcher(){

        binding.edtLeadFirstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                 binding.inputLayoutFirstName.setError(null);
               //  enable_disable_Save_Button();
            }

            @Override
            public void afterTextChanged(Editable s) {
                enable_disable_Save_Button();
            }
        });

        binding.edtLeadLastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.inputLayoutLastName.setError(null);
              //  enable_disable_Save_Button();
            }

            @Override
            public void afterTextChanged(Editable s) {
                enable_disable_Save_Button();
            }
        });

        binding.edtLeadMobileNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.inputLayoutMobileNumber.setError(null);
              //  enable_disable_Save_Button();
            }

            @Override
            public void afterTextChanged(Editable s) {
                enable_disable_Save_Button();
            }
        });


    }

    private  void storeInRoomDB_LeadListDB(Context context , LeadModelRoom leadModelRoom){
        LeadDao lead_Dao = LeadListDB.getInstance(this).leadDao();
        lead_Dao.insert(leadModelRoom);
    }

    private boolean checkIfDataExists(String phoneNumber) {
        LeadDao lead_Dao = LeadListDB.getInstance(this).leadDao();
        int count = lead_Dao.getCountByPhoneNumber(phoneNumber);
        return count > 0;
    }

    private void storageOfLeadOffline(){


        String first_name = binding.edtLeadFirstName.getText().toString().trim();
        String phone_number = binding.edtLeadMobileNumber.getText().toString().trim();

        LeadModelRoom leadModelResponseForRoom = new LeadModelRoom(first_name,phone_number);

        // to Check if Data(phoneNumber) already exists in the Table
        if(!checkIfDataExists(phone_number)) {
            storeInRoomDB_LeadListDB(NewLeadDetailsActivity.this, leadModelResponseForRoom);

            Global.showToast(NewLeadDetailsActivity.this,getResources().getString(R.string.saved_in_room_db));
            Global.hideKeyboard(NewLeadDetailsActivity.this);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    Global.hideKeyboard(NewLeadDetailsActivity.this);
                    binding.btnBackToLeadList.performClick();
                }
            }, 3000); // 3000 milliseconds = 3 seconds

        }
        // Else Lead Already exists
        else{
            Global.showToast(NewLeadDetailsActivity.this,getResources().getString(R.string.lead_number_already_exists));
            Global.hideKeyboard(NewLeadDetailsActivity.this);
        }


    }
}