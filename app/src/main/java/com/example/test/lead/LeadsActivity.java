package com.example.test.lead;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.test.R;
import com.example.test.databinding.ActivityLeadsBinding;
import com.example.test.helper_classes.Global;
import com.example.test.helper_classes.NetworkUtilities;
import com.example.test.lead.adapter.LeadListAdapter;
import com.example.test.lead.adapter.RawLeadListAdapter;
import com.example.test.lead.model.LeadModel;
import com.example.test.login.LoginActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class LeadsActivity extends AppCompatActivity {

    ActivityLeadsBinding binding;
    View view;
    LeadsViewModel leadsViewModel;
    List<Object> rawLeadListItem = new ArrayList<>(); // for Fetching data from Json file stored locally in raw folder

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initializeFields();
      //  initObserver();
        callAPi();
        callAPiFromRaw();
        onClickListener();
    }

    private void callAPiFromRaw() {
        setRawRecyclerView();

        try {
            String jsonString = readJsonFromRaw();
            JSONArray jsonArray = new JSONArray(jsonString);

            for(int i = 0 ; i<=jsonArray.length()-1 ; ++i){

                JSONObject itemJsonObject = jsonArray.getJSONObject(i);
                String firstName = itemJsonObject.getString("firstName");
                String phoneNumber = itemJsonObject.getString("phoneNumber");


                LeadModel leadModel = new LeadModel(firstName,phoneNumber);
                rawLeadListItem.add(leadModel);

            }


        }
        catch(Exception e){
            Global.showSnackBar(view,e.toString());
            System.out.println("Here :"+ e);
        }

    }

    private String readJsonFromRaw() throws IOException {
        InputStream inputStream = null;
        String jsonString = null ;
        StringBuilder stringBuilder = new StringBuilder();

        try{
            inputStream = getResources().openRawResource(R.raw.lead_list_response);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));

            while((jsonString = bufferedReader.readLine()) !=null){
                stringBuilder.append(jsonString);
            }

        }

        finally {
            if(inputStream!=null){ inputStream.close();  }
        }

        return new String(stringBuilder);
    }


    private void initializeFields() {

        binding = DataBindingUtil.setContentView(this, R.layout.activity_leads);
        view = binding.getRoot();
        leadsViewModel =  new ViewModelProvider(this).get(LeadsViewModel.class);
        binding.setLeadViewModel(leadsViewModel);
    }

    private void setUpRecyclerLeadListData(){

        leadsViewModel.updateLeadListData();
      RecyclerView recyclerView =  binding.rvLeadActivity;
      recyclerView.setAdapter(new LeadListAdapter(leadsViewModel.arrListLeadListData));
    }

    private void setRawRecyclerView(){
        RecyclerView recyclerView =  binding.rvLeadActivity;
        recyclerView.setAdapter(new RawLeadListAdapter(rawLeadListItem));
    }


    private void callAPi(){
        leadsViewModel.getLeads();
    }

    private void initObserver(){

        leadsViewModel.getMutLeadListResponseApi().observe(this,result->{

            if(NetworkUtilities.getConnectivityStatus(this)) {

               if(result.getAllLeadList()!=null){
                   leadsViewModel.arrListLeadListData.clear();
                   leadsViewModel.arrListLeadListData.addAll(result.getAllLeadList());

                   Global.showToast(this,"Size of Lead List:"+result.getAllLeadList().size());

                   setUpRecyclerLeadListData();
               }


            }
            else Global.showSnackBar(view,getResources().getString(R.string.check_internet_connection));

            });

        //handle  error response
        leadsViewModel.getMutErrorResponse().observe(this, error ->{

            if (error != null && !error.isEmpty()) {
                Global.showSnackBar(view, error);
                System.out.println("Here: " + error);
            } else {
                Global.showSnackBar(view, getResources().getString(R.string.check_internet_connection));
            }
        });

    }


    private void onClickListener() {
        binding.leadFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent newLeadIntent = new Intent(LeadsActivity.this,NewLeadDetailsActivity.class);
                startActivity(newLeadIntent);
            }
        });

        binding.txtAddNewLead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.leadFloatingActionButton.performClick();
            }
        });
    }
}