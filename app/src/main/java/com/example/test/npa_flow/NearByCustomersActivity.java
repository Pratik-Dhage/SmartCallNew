package com.example.test.npa_flow;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import com.example.test.R;
import com.example.test.databinding.ActivityNearByCustomersBinding;
import com.example.test.fragment_visits_flow.VisitsFlowViewModel;
import com.example.test.helper_classes.Global;
import com.example.test.main_dashboard.MainActivity3API;
import com.example.test.npa_flow.details_of_customer.DetailsOfCustomerActivity;
import com.example.test.npa_flow.loan_collection.LoanCollectionActivity;
import com.example.test.npa_flow.nearby_customer.NearByCustomerListActivity;
import com.example.test.npa_flow.nearby_customer.NearByCustomerListAdapter;
import com.example.test.npa_flow.nearby_customer.NearByCustomerViewModel;
import com.example.test.schedule_flow.calls_for_the_day.CallsForTheDayActivity;
import com.example.test.schedule_flow.calls_for_the_day.adapter.CallsForTheDayAdapter;
import com.example.test.schedule_flow.visits_for_the_day.VisitsForTheDayActivity;
import com.example.test.schedule_flow.visits_for_the_day.VisitsForTheDayViewModel;
import com.example.test.schedule_flow.visits_for_the_day.adapter.VisitsForTheDayAdapter;

public class NearByCustomersActivity extends AppCompatActivity {

    ActivityNearByCustomersBinding binding;
    View view;
    NearByCustomerViewModel nearByCustomerViewModel;
    public static int backToMemberList = 0;

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
        nearByCustomerViewModel = new ViewModelProvider(this).get(NearByCustomerViewModel.class);

        //Button Visit NearBy Customers will only visible if coming from VisitsForTheDayFlow
        if(VisitsForTheDayAdapter.showNearByCustomerButton || Global.getStringFromSharedPref(this,"backToMemberList").equals("3")){
            binding.btnVisitNearbyCustomers.setVisibility(View.VISIBLE);
        }
        else{
            binding.btnVisitNearbyCustomers.setVisibility(View.INVISIBLE);
        }
    }

    private void onClickListener() {


        binding.btnGotoDashBoard.setOnClickListener(v->{
            Intent i = new Intent(NearByCustomersActivity.this, MainActivity3API.class);
            startActivity(i);
        });

        binding.btnBackToMemberList.setOnClickListener(v->{

            //Coming from LoanCollection List (NPA flow) //CallsForTheDayAdapter.isFromCallsForTheDayAdapter==null
          if( backToMemberList==1 || Global.getStringFromSharedPref(this,"backToMemberList").equals("1")) {
                Intent i = new Intent(new Intent(this, LoanCollectionActivity.class));
                int DPD_row_position = Integer.parseInt(Global.getStringFromSharedPref(this,"DPD_row_position"));
                i.putExtra("DPD_row_position",DPD_row_position);
                i.putExtra("NearByCustomerActivity","NearByCustomerActivity");
                startActivity(i);
            }

            //Coming from CallsForTheDay flow //CallsForTheDayAdapter.isFromCallsForTheDayAdapter!=null
            else if( backToMemberList==2 || Global.getStringFromSharedPref(this,"backToMemberList").equals("2")){
                Intent i = new Intent(this, CallsForTheDayActivity.class);
                startActivity(i);
            }

          //Coming From VisitsForTheDay coz Call Icon Only visible in VisitsForTheDay flow //MainActivity3API.showCallIcon
          else if( backToMemberList==3 || Global.getStringFromSharedPref(this,"backToMemberList").equals("3")){
              Intent i = new Intent(this, VisitsForTheDayActivity.class);
              startActivity(i);
          }

            //Coming from NearByCustomerListActivity //NearByCustomerListAdapter.isFromNearByCustomerAdapter
           else if(  backToMemberList==4 || Global.getStringFromSharedPref(this,"backToMemberList").equals("4")){
                Intent i = new Intent(this, NearByCustomerListActivity.class);
                startActivity(i);
            }

        });

        binding.btnVisitNearbyCustomers.setOnClickListener(v->{


            try {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
                } else if (!Global.isBackgroundLocationAccessEnabled(this)) {
                    //request BackGroundLocation Access
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, Global.REQUEST_BACKGROUND_LOCATION);
                    System.out.println("Here Requesting BackGroundLocation Permission");
                }

                else if(Global.isLocationEnabled(this) && Global.isBackgroundLocationAccessEnabled(this)){

                    NearByCustomersActivity.backToMemberList = 4;
                   Intent i = new Intent(this, NearByCustomerListActivity.class);
                   startActivity(i);
                }
            }
            catch (Exception e){
                System.out.println("NearByCustomer LocationException:"+e);
            }



        });





    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}