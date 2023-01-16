package com.example.test.lead;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.example.test.R;
import com.example.test.databinding.ActivityNewLeadDetailsBinding;
import com.example.test.helper_classes.Global;
import com.example.test.helper_classes.NetworkUtilities;

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

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void initializeFields() {
        binding = DataBindingUtil. setContentView(this,R.layout.activity_new_lead_details);
        view = binding.getRoot();


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
                        Global.showToast(NewLeadDetailsActivity.this,getResources().getString(R.string.ok));
                    }

                }
                else{
                    Global.showSnackBar(view, getResources().getString(R.string.check_internet_connection));
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

         if(binding.spinnerLeadSource.getSelectedItemPosition()==0){
             Global.showToast(this,getResources().getString(R.string.please_select_lead_source));
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
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        binding.edtLeadLastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.inputLayoutLastName.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        binding.edtLeadMobileNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.inputLayoutMobileNumber.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


    }
}