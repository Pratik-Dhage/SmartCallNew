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
import com.example.test.schedule_flow.calls_for_the_day.CallsForTheDayActivity;
import com.example.test.schedule_flow.calls_for_the_day.adapter.CallsForTheDayAdapter;
import com.example.test.schedule_flow.visits_for_the_day.VisitsForTheDayActivity;
import com.example.test.schedule_flow.visits_for_the_day.adapter.VisitsForTheDayAdapter;

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

        if(VisitsForTheDayAdapter.showNearByCustomerButton == true){
            binding.btnVisitNearbyCustomers.setVisibility(View.VISIBLE);
        }
        else{
            binding.btnVisitNearbyCustomers.setVisibility(View.INVISIBLE);
        }
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

            //Coming From VisitsForTheDay coz Call Icon Only visible in VisitsForTheDay flow
            if(MainActivity3API.showCallIcon){
                Intent i = new Intent(this, VisitsForTheDayActivity.class);
                startActivity(i);
            }

            //Coming from CallsForTheDay flow
         else if(CallsForTheDayAdapter.isFromCallsForTheDayAdapter!=null){
                Intent i = new Intent(this, CallsForTheDayActivity.class);
                startActivity(i);
            }

            //Coming from LoanCollection List (NPA flow)
         else if(CallsForTheDayAdapter.isFromCallsForTheDayAdapter==null) {
                Intent i = new Intent(new Intent(this, LoanCollectionActivity.class));
                int DPD_row_position = Integer.parseInt(Global.getStringFromSharedPref(this,"DPD_row_position"));
                i.putExtra("DPD_row_position",DPD_row_position);
                i.putExtra("NearByCustomerActivity","NearByCustomerActivity");
                startActivity(i);
            }



        });

        binding.btnVisitNearbyCustomers.setOnClickListener(v->{
            Intent i = new Intent(this,LoanCollectionActivity.class);
            i.putExtra("isFromNearByCustomerActivity","isFromNearByCustomerActivity");
            startActivity(i);
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

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}