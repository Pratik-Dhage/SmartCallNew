package com.example.test.npa_flow;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.test.R;
import com.example.test.databinding.ActivityNearByCustomersBinding;
import com.example.test.helper_classes.Global;
import com.example.test.main_dashboard.MainActivity3API;
import com.example.test.npa_flow.details_of_customer.DetailsOfCustomerActivity;
import com.example.test.npa_flow.loan_collection.LoanCollectionActivity;

public class NearByCustomersActivity extends AppCompatActivity {

    ActivityNearByCustomersBinding binding;
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_by_customers);

        initializeFields();
        onClickListener();
    }

    private void initializeFields() {
        binding = DataBindingUtil.setContentView(this,R.layout.activity_near_by_customers);
        view = binding.getRoot();
    }

    private void onClickListener() {

        binding.clLoanCollectionData.setOnClickListener(v->{
            Intent i = new Intent(NearByCustomersActivity.this, DetailsOfCustomerActivity.class);
            startActivity(i);
        });

        binding.btnGotoDashBoard.setOnClickListener(v->{
            Intent i = new Intent(NearByCustomersActivity.this, MainActivity3API.class);
            startActivity(i);
        });

        binding.btnBackToMemberList.setOnClickListener(v->{
            //then redirect to LoanCollection List
            Intent i = new Intent(new Intent(this, LoanCollectionActivity.class));
            int DPD_row_position = Integer.parseInt(Global.getStringFromSharedPref(this,"DPD_row_position"));
            i.putExtra("DPD_row_position",DPD_row_position);
            i.putExtra("NearByCustomerActivity","NearByCustomerActivity");
            startActivity(i);
        });

        binding.btnVisitNearbyCustomers.setOnClickListener(v->{

            if(binding.clLoanCollectionData.getVisibility()==View.INVISIBLE){

                binding.btnGotoDashBoard.setVisibility(View.INVISIBLE);
                binding.clLoanCollectionData.setVisibility(View.VISIBLE);
            }

            else{
                binding.btnGotoDashBoard.setVisibility(View.VISIBLE);
                binding.clLoanCollectionData.setVisibility(View.INVISIBLE);
            }


        });


        //opens Google Maps
        binding.ivMap1.setOnClickListener(v -> {

            //Below commented code is working in my device but not in Other devices
           /* String location = "Navi Mumbai";
            Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + location);  //geo:latitude,longitude?q=query  //geo:0,0?q=my+street+address
          //  Uri gmmIntentUri = Uri.parse("geo:0,0?q=my+street+address"+ location);
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            if (mapIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(mapIntent);
            }*/

            Intent googleMapsIntent = new Intent(NearByCustomersActivity.this,WebViewActivity.class);
            startActivity(googleMapsIntent);
        });

        //opens Google Maps
        binding.ivMap2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.ivMap1.performClick();
            }
        });


    }


}