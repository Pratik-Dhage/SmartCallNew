package com.example.test.lead;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Context;
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
import com.example.test.roomDB.dao.LeadDao;
import com.example.test.roomDB.database.LeadListDB;
import com.example.test.roomDB.model.LeadModelRoom;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
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
       initObserver();
        callAPi();
       // callAPiFromRaw();
        onClickListener();
    }

    private void callAPiFromRaw() {
        setRawRecyclerView();

        try {
            String jsonString = readJsonFromRaw();
            JSONArray jsonArray = new JSONArray(jsonString);

            for(int i = 0 ; i<jsonArray.length() ; ++i){

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

               if(result!=null){
                   leadsViewModel.arrListLeadListData.clear();

                   setUpRecyclerLeadListData();
                  leadsViewModel.arrListLeadListData.addAll(result);

                  /*
                  // iterate from result and store in local String
                     String first_name = result.iterator().next().getFirstName();
                     String phone_number = result.iterator().next().getPhoneNumber();

                   LeadModelRoom leadModelResponseForRoom = new LeadModelRoom(first_name,phone_number);

                      //store this Lead List Response in Room DataBase

                   storeInRoomDB_LeadListDB(this,leadModelResponseForRoom);
*/
                   for(int i = 1 ; i<= result.size() ; i++){

                       String first_name = result.iterator().next().getFirstName();
                       String phone_number = result.iterator().next().getPhoneNumber();

                       LeadModelRoom leadModelResponseForRoom = new LeadModelRoom(first_name,phone_number);

                       storeInRoomDB_LeadListDB(this,leadModelResponseForRoom);
                   }



                   Global.showToast(this,"Size of Lead List:"+result.size()); // size is getting 100


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

    private  void storeInRoomDB_LeadListDB(Context context , LeadModelRoom leadModelRoom){


       LeadDao lead_Dao = LeadListDB.getInstance(this).leadDao();

        lead_Dao.insert(leadModelRoom);

      //Global.showToast(this,""+lead_Dao.isExisting("1"));
      System.out.println("Lead ID:"+lead_Dao.getAllLeadListFromRoomDB().size());

    }
}