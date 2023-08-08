package com.example.test.main_dashboard;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.test.R;
import com.example.test.databinding.ActivityMainActivity3ApiBinding;
import com.example.test.fragments_activity.ActivityOfFragments;
import com.example.test.google_maps.GoogleMapsActivity;
import com.example.test.helper_classes.Global;
import com.example.test.helper_classes.NetworkUtilities;
import com.example.test.login.LoginActivity;
import com.example.test.login.LoginWithMPinActivity;
import com.example.test.main_dashboard.adapter.MainDashBoardAdapter;
import com.example.test.main_dashboard.model.DashBoardResponseModel;
import com.example.test.npa_flow.loan_collection.LoanCollectionActivity;
import com.example.test.npa_flow.nearby_customer.NearByCustomerListAdapter;
import com.example.test.roomDB.dao.MPinDao;
import com.example.test.roomDB.dao.UserNameDao;
import com.example.test.roomDB.database.LeadListDB;
import com.example.test.schedule_flow.ScheduleDetailsActivity;
import com.example.test.schedule_flow.calls_for_the_day.CallsForTheDayActivity;
import com.example.test.schedule_flow.calls_for_the_day.adapter.CallsForTheDayAdapter;
import com.example.test.schedule_flow.schedule_for_the_day.ScheduleForTheDayAdapter;
import com.example.test.schedule_flow.visits_for_the_day.VisitsForTheDayActivity;
import com.example.test.schedule_flow.visits_for_the_day.adapter.VisitsForTheDayAdapter;

public class MainActivity3API extends AppCompatActivity {

    ActivityMainActivity3ApiBinding binding;
    View view;
    MainDashBoardViewModel mainDashBoardViewModel;
    public static boolean showCallIcon = false; // for call icon to be visible in DetailsOfCustomerAdapter when coming from Visits For The Day Flow to be True Else False
    public static String UserID ;
    public static String BranchCode ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // UserID = getIntent().getStringExtra("UserID");
       // BranchCode = getIntent().getStringExtra("BranchCode");
       // setContentView(R.layout.activity_main_activity3_api);

        // Get UserID , BranchCode , UserName from RoomDB
        MPinDao mPinDao = LeadListDB.getInstance(this).mPinDao();
        System.out.println("Here UserID in MainActivity3API From RoomDB :"+mPinDao.getUserID());
        System.out.println("Here BranchCode in MainActivity3API From RoomDB :"+mPinDao.getBranchCode());

       /* if(getIntent().hasExtra("isFromLoginWithMPin")){

            UserID = mPinDao.getUserID(Global.getStringFromSharedPref(this,"MPin"));
            BranchCode = mPinDao.getBranchCode(Global.getStringFromSharedPref(this,"MPin"));

        }
*/

        UserID = mPinDao.getUserID();
        BranchCode = mPinDao.getBranchCode();

        initializeFields();
        onClickListener();

        if(NetworkUtilities.getConnectivityStatus(this)){
            callDashBoardApi();
             callScheduleForTheDayApi();
        }

       else{
            Global.showToast(this,getString(R.string.check_internet_connection));
        }

        initObserver();
       initObserverScheduleForTheDay();

    }

    private void initializeFields() {

        binding = DataBindingUtil.setContentView(this,R.layout.activity_main_activity3_api);
        view = binding.getRoot();
        mainDashBoardViewModel = new ViewModelProvider(this).get(MainDashBoardViewModel.class);
        binding.setViewModel(mainDashBoardViewModel);

        showCallIcon = false; //from Visits For The Day Flow to be True Else False
        VisitsForTheDayAdapter.showNearByCustomerButton = false ; //in NearByCustomerActivity - from Visits For The Day Flow to be True Else False
        NearByCustomerListAdapter.isFromNearByCustomerAdapter = false; //in NearByCustomerActivity - from NearByCustomerListActivity to be True Else False

        GoogleMapsActivity.isSaveButtonClicked = false; // only true when Save OR yes button is clicked in GoogleMaps Activity

       // Global.saveStringInSharedPref(this,"isFromCallsForTheDayAdapter",null); //to reset the Flows
       // CallsForTheDayAdapter.isFromCallsForTheDayAdapter=null ; // for Navigate Button to be Only visible in NPA/ VisitsForTheDayFlow
       // System.out.println("Here MainActivity3Api isFromCallsForTheDayAdapter "+ CallsForTheDayAdapter.isFromCallsForTheDayAdapter);

        System.out.println("Android Version:"+Global.getAndroidVersionAndApiLevel()); // to get User's Device Android Version


         if(LoginActivity.userName!=null){
            binding.txtWelcomeUser.setText("Welcome "+LoginActivity.userName);

            // Store UserName in SharedPreference and Use in StatusOfCustomerDetailsAdapter
            String userName = LoginActivity.userName;
            Global.saveStringInSharedPref(this,"userName",userName);
        }
         else{
            /* MPinDao mPinDao = LeadListDB.getInstance(this).mPinDao();
             UserNameDao userNameDao = LeadListDB.getInstance(this).userNameDao();
             String UserID = Global.getStringFromSharedPref(this,"UserID");
             String userName =  userNameDao.getUserNameUsingUserIDInUserNameRoomDB(UserID);
             binding.txtWelcomeUser.setText("Welcome "+userName);
             System.out.println("Here LoginWithMPin UserName:"+userName);
*/
             if(LoginWithMPinActivity.userName!=null){
                 String userName = LoginWithMPinActivity.userName;
                 binding.txtWelcomeUser.setText("Welcome "+userName);
                 System.out.println("Here LoginWithMPin UserName in MainActivity3:"+userName);
             }
             else {

                 //Get UserName from RoomDB
                 MPinDao mPinDao = LeadListDB.getInstance(this).mPinDao();
                 UserNameDao userNameDao = LeadListDB.getInstance(this).userNameDao();
                 String userName = userNameDao.getUserNameUsingUserIDInUserNameRoomDB(mPinDao.getUserID());
                 binding.txtWelcomeUser.setText("Welcome "+userName);
                 System.out.println("Here UserName from RoomDB in MainActivity3:"+userName);
             }

         }
    }

    private void callDashBoardApi() {
        mainDashBoardViewModel.getDashBoardData();
    }

    private void callScheduleForTheDayApi(){
        mainDashBoardViewModel.getScheduleForTheDayData();
    }

    private void setUpDashBoardRecyclerView() {
        mainDashBoardViewModel.updateDashBoardData();
        RecyclerView recyclerView = binding.rvDashBoardMain;
        recyclerView.setAdapter(new MainDashBoardAdapter(mainDashBoardViewModel.arrListDashBoardData));
    }

    private void setUpScheduleForTheDayRecyclerView(){
        mainDashBoardViewModel.updateScheduleForTheDayData();
        RecyclerView recyclerView = binding.rvScheduleForTheDay;
        recyclerView.setAdapter(new ScheduleForTheDayAdapter(mainDashBoardViewModel.arrListScheduleForTheDayData));
    }


    private void initObserver(){

        binding.loadingProgressBar.setVisibility(View.VISIBLE);
        mainDashBoardViewModel.getMutDashBoardResponseApi().observe(this, result -> {

            if(NetworkUtilities.getConnectivityStatus(this)) {
                if (result != null) {
                    //   Global.showToast(MainActivity.this, "Size:" + result.size());

                    mainDashBoardViewModel.arrListDashBoardData.clear();

                    setUpDashBoardRecyclerView();
                    mainDashBoardViewModel.arrListDashBoardData.addAll(result);
                    binding.loadingProgressBar.setVisibility(View.GONE);


                    //to get TotalMembers Assigned value
                    int totalCompletedCalls = 0;
                    int totalPendingCalls = 0;
                    int totalInProcessCalls = 0;
                    int TotalMembersAssigned = 0;


                    for(DashBoardResponseModel a : result){

                        totalCompletedCalls += a.getCompletedCalls();
                        totalPendingCalls += a.getPendingCalls();
                        totalInProcessCalls +=a.getInprocessCalls();

                         TotalMembersAssigned = totalCompletedCalls + totalPendingCalls;


                        binding.labelPendingMembersAssignedValue.setText(String.valueOf(totalPendingCalls)); //Pending Assigned
                        binding.labelInProcessMembersAssignedValue.setText(String.valueOf(totalInProcessCalls)); //InProcess Assigned
                        binding.labelCompletedMembersAssignedValue.setText(String.valueOf(totalCompletedCalls)); //Completed Assigned
                    }


                    // Total Members(Marketing+Collection(NPA)+Welcome Call+ Renewal)
                   // binding.labelTotalAssignedValue.setText(String.valueOf(TotalMembersAssigned));
                   // binding.labelPendingMembersAssignedValue.setText(String.valueOf(TotalMembersAssigned)); // same value for Pending because Completed = 0


                }


            }
            else{
                Global.showSnackBar(view,getResources().getString(R.string.check_internet_connection));
            }

        });

        //handle  error response
        mainDashBoardViewModel.getMutErrorResponse().observe(this, error -> {

            if (error != null && !error.isEmpty()) {
                Global.showSnackBar(view, error);
                System.out.println("Here AssignedDashBoardError: " + error);
            } else {
                Global.showSnackBar(view, getResources().getString(R.string.check_internet_connection));
            }
        });

    }

    private void initObserverScheduleForTheDay(){

        binding.loadingProgressBar2.setVisibility(View.VISIBLE);
        //for Scheduled For The Day
        mainDashBoardViewModel.getMutDashBoardScheduleForTheDayResponseApi().observe(this,result->{

            if(NetworkUtilities.getConnectivityStatus(this)){

                if(result!=null){

                    binding.loadingProgressBar2.setVisibility(View.INVISIBLE);
                    mainDashBoardViewModel.arrListScheduleForTheDayData.clear();
                    setUpScheduleForTheDayRecyclerView();
                    mainDashBoardViewModel.arrListScheduleForTheDayData.addAll(result);
                }

            }

            else{
                Global.showSnackBar(view,getResources().getString(R.string.check_internet_connection));
            }

        });

        //handle  error response
        mainDashBoardViewModel.getMutErrorResponse().observe(this, error -> {

            if (error != null && !error.isEmpty()) {
                Global.showSnackBar(view, error);
                System.out.println("Here ScheduleForTheDayError: " + error);
            } else {
                Global.showSnackBar(view, getResources().getString(R.string.check_internet_connection));
            }
        });


    }



    private void onClickListener(){

        //Scheduled for the day section
        binding.ivSchedule.setOnClickListener(v->{
            Intent i = new Intent(this, ScheduleDetailsActivity.class);
            startActivity(i);
        });

       /* binding.ivRightArrowVisits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showCallIcon = true;
                Intent i = new Intent(MainActivity3API.this, VisitsForTheDayActivity.class);
                i.putExtra("isFromVisitsForTheDay","isFromVisitsForTheDay");
                startActivity(i);
            }
        });

        binding.ivRightArrowCalls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showCallIcon = false;
                Intent i = new Intent(MainActivity3API.this, CallsForTheDayActivity.class);
                i.putExtra("isFromCallsForTheDay","isFromCallsForTheDay");
                startActivity(i);
            }
        });
*/

        //Assigned Section
        binding.ivRightArrowMembersAssigned.setOnClickListener(v->{

            if(binding.cardView2.getVisibility()==View.INVISIBLE){
                binding.ivRightArrowMembersAssigned.setImageResource(R.drawable.down_arrow);
                binding.cardView2.setVisibility(View.VISIBLE);
            }
            else{
                binding.ivRightArrowMembersAssigned.setImageResource(R.drawable.right_arrow);
                binding.cardView2.setVisibility(View.INVISIBLE);
            }

        });


       binding.ivLogout.setOnClickListener(v->{

           View customDialogLogout = LayoutInflater.from(this).inflate(R.layout.custom_dialog_logout, null);

           Button customButtonYes = customDialogLogout.findViewById(R.id.btnYes);
           Button customButtonNo = customDialogLogout.findViewById(R.id.btnNo);
           ImageView ivClose = customDialogLogout.findViewById(R.id.ivClose);

           AlertDialog.Builder builder = new AlertDialog.Builder(this);
           builder.setView(customDialogLogout);
           final AlertDialog dialog = builder.create();
           dialog.show();

           customButtonYes.setOnClickListener(v1->{

               Intent i = new Intent(MainActivity3API.this,LoginActivity.class);
               startActivity(i);
           });

           customButtonNo.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   dialog.dismiss();
               }
           });

           ivClose.setOnClickListener(v1->{
               dialog.dismiss();
           });

       });



    }

    @Override
    protected void onPause() {

        // Get UserName , UserID , BranchCode

        MPinDao mPinDao = LeadListDB.getInstance(this).mPinDao();
        UserNameDao userNameDao = LeadListDB.getInstance(this).userNameDao();

        String userName = userNameDao.getUserNameUsingUserIDInUserNameRoomDB(mPinDao.getUserID());

        binding.txtWelcomeUser.setText("Welcome "+userName);
        System.out.println("Here MainActivity3Api onPause() UserName:"+userName);

        // Store UserName in SharedPreference and Use in StatusOfCustomerDetailsAdapter
        Global.saveStringInSharedPref(this,"userName",userName);

        UserID = mPinDao.getUserID();
        BranchCode = mPinDao.getBranchCode();

        System.out.println("Here MainActivity3Api onPause() UserID:"+UserID);
        System.out.println("Here MainActivity3Api onPause() BranchCode:"+BranchCode);

        super.onPause();
    }

    @Override
    protected void onResume() {

        // Get UserName , UserID , BranchCode

        MPinDao mPinDao = LeadListDB.getInstance(this).mPinDao();
        UserNameDao userNameDao = LeadListDB.getInstance(this).userNameDao();

       String userName = userNameDao.getUserNameUsingUserIDInUserNameRoomDB(mPinDao.getUserID());

        binding.txtWelcomeUser.setText("Welcome "+userName);
        System.out.println("Here MainActivity3Api onResume() UserName:"+userName);

        // Store UserName in SharedPreference and Use in StatusOfCustomerDetailsAdapter
        Global.saveStringInSharedPref(this,"userName",userName);

        UserID = mPinDao.getUserID();
        BranchCode = mPinDao.getBranchCode();

        System.out.println("Here MainActivity3Api onResume() UserID:"+UserID);
        System.out.println("Here MainActivity3Api onResume() BranchCode:"+BranchCode);


        initializeFields();
        onClickListener();

        if(NetworkUtilities.getConnectivityStatus(this)){
            callDashBoardApi();
            callScheduleForTheDayApi();
        }

        else{
            Global.showToast(this,getString(R.string.check_internet_connection));
        }

        initObserver();
        initObserverScheduleForTheDay();


        super.onResume();
    }

    @Override
    protected void onDestroy() {

        MPinDao mPinDao = LeadListDB.getInstance(this).mPinDao();
        UserID = mPinDao.getUserID();
        BranchCode = mPinDao.getBranchCode();

        super.onDestroy();
    }
}