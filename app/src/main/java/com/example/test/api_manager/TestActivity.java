package com.example.test.api_manager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;

import com.example.test.R;
import com.example.test.databinding.ActivityTestBinding;
import com.example.test.helper_classes.Global;
import com.example.test.helper_classes.NetworkUtilities;

public class TestActivity extends AppCompatActivity {

    ActivityTestBinding binding;
    View view;
    TestViewModel testViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_test);

        initializeFields();

        callLeadApi();
        callDashBoardApi();
        initObserver();
    }

    private void initObserver() {

        //for Lead Response
        TestViewModel.getMutLeadListResponseApi().observe(this, result->{

            if(NetworkUtilities.getConnectivityStatus(this)){

                //Global.showToast(TestActivity.this,"Size: "+result.size());
                binding.txtSizeOfLeadList.setText("Size of Lead List : "+result.size());
                System.out.println("Here Lead Size: "+result.size());

            }
        });

        //for DashBoard Response
        TestViewModel.getMutDashBoardResponseApi().observe(this,result->{

            if(NetworkUtilities.getConnectivityStatus(this)){

                 binding.txtSizeOfDashBoardList.setText("Size of DashBoard List : "+result.size());
                System.out.println("Here DashBoard Size: "+result.size());
            }

        });


        //handle error response
        testViewModel.getMutErrorResponse().observe(this,error->{
            if(error!=null){
                Global.showToast(this,"Error: "+error);
            }
        });
    }

    private void callLeadApi() {
        testViewModel.callApi(1);
    }

    private void callDashBoardApi(){  testViewModel.callApi(2);  }

    private void initializeFields() {

        binding = DataBindingUtil.setContentView(this,R.layout.activity_test);
        view = binding.getRoot();
        testViewModel = new ViewModelProvider(this).get(TestViewModel.class);
        binding.setTestViewModel(testViewModel);

    }
}