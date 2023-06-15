package com.example.test.main_dashboard;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.test.api_manager.WebServices;
import com.example.test.helper_classes.Global;
import com.example.test.lead.adapter.LeadListAdapter;
import com.example.test.lead.model.LeadModel;
import com.example.test.login.LoginActivity;
import com.example.test.main_dashboard.adapter.MainDashBoardAdapter;
import com.example.test.main_dashboard.model.DashBoardModel;
import com.example.test.main_dashboard.model.DashBoardResponseModel;
import com.example.test.main_dashboard.model.DashBoardScheduleForTheDayModel;
import com.example.test.schedule_flow.schedule_for_the_day.ScheduleForTheDayAdapter;
import com.example.test.user.UserModel;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainDashBoardViewModel extends ViewModel {

    private Disposable subscribtion; //Disposable Interface used to prevent observer from receiving items from Observer before all items are loaded.


    //For Assigned in DashBoard
    private final MutableLiveData<List<DashBoardResponseModel>> mutDashBoardResponseApi = new MutableLiveData<>();
    private final MutableLiveData<String> mutErrorResponse = new MutableLiveData<>();

    public MutableLiveData<List<DashBoardResponseModel>> getMutDashBoardResponseApi() {
        return mutDashBoardResponseApi;
    }

    public MutableLiveData<String> getMutErrorResponse() {
        return mutErrorResponse;
    }

    //FOR Assigned
    public ArrayList<DashBoardResponseModel> arrListDashBoardData = new  ArrayList<>() ;
    public MainDashBoardAdapter mainDashBoardAdapter = new MainDashBoardAdapter(arrListDashBoardData);

    public void updateDashBoardData(){
        mainDashBoardAdapter.setData(arrListDashBoardData);
    }



    //for Schedule For the Day in DashBoard
    private final MutableLiveData<List<DashBoardScheduleForTheDayModel>> mutDashBoardScheduleForTheDayResponseApi = new MutableLiveData<>();

    public MutableLiveData<List<DashBoardScheduleForTheDayModel>> getMutDashBoardScheduleForTheDayResponseApi() {
        return mutDashBoardScheduleForTheDayResponseApi;
    }

    //FOR ScheduleForTheDay
    public ArrayList<DashBoardScheduleForTheDayModel> arrListScheduleForTheDayData = new ArrayList<>();
    public ScheduleForTheDayAdapter scheduleForTheDayAdapter = new ScheduleForTheDayAdapter(arrListScheduleForTheDayData);
    public void updateScheduleForTheDayData() {   scheduleForTheDayAdapter.setData(arrListScheduleForTheDayData);      }


    //Call to API with Required Function

    //DashBoard Api

    //for making @GET request
    // Changes Now Use userId and branchCode as Parameters( 28/04/2023) in UserModel to get DashBoard Data
    String userId_new = "CA_01_001";
    String userBranchCode = "001";
    String newBranchCode = "00048";



    //for making @GET request
    String userId = "admin"; //send User Id and password as Request Body in RestClient
    String password = "123456";

    //for making @POST request
    //UserModel userModel = new UserModel(userId,password);
    UserModel userModel = new UserModel(MainActivity3API.UserID,MainActivity3API.BranchCode); // Changes UserModel userModel = new UserModel(userId_new,userBranchCode);


    public void getDashBoardData()

    {  //WebServices.SmartCall_BaseURL2+WebServices.Dashboard_Data,
        subscribtion = (Disposable) Global.apiService().getDashBoardData(userModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(
                        this::onHomeApiSuccess, this::onApiError
                );

    }

    public void getScheduleForTheDayData(){
        subscribtion = (Disposable) Global.apiService().getDashBoardDataScheduleForTheDay(WebServices.schedule_for_the_day+"userId="+MainActivity3API.UserID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(
                        this::onHomeApiSuccess2, this::onApiError
                );

    }

    //For Assigned
    private void onHomeApiSuccess(List<DashBoardResponseModel> result) {
        mutDashBoardResponseApi.setValue(result);
    }

    //For Scheduled For The Day
    private void onHomeApiSuccess2(List<DashBoardScheduleForTheDayModel> result) {
        mutDashBoardScheduleForTheDayResponseApi.setValue(result);
    }

    private void onApiError(Throwable error) {
        mutErrorResponse.setValue(error.getLocalizedMessage());
    }


}
