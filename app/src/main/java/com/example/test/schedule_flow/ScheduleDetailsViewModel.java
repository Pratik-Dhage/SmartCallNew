package com.example.test.schedule_flow;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.test.api_manager.WebServices;
import com.example.test.helper_classes.Global;
import com.example.test.main_dashboard.MainActivity3API;
import com.example.test.schedule_flow.adapter.ScheduleDetailsAdapter;
import com.example.test.schedule_flow.model.ScheduleVisitDetails;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ScheduleDetailsViewModel extends ViewModel {

    private Disposable subscribtion; //Disposable Interface used to prevent observer from receiving items from Observer before all items are loaded.

    private final MutableLiveData<List<ScheduleVisitDetails>> mutActivity_ResponseApi = new MutableLiveData<>();

    private final MutableLiveData<String> mutErrorResponse = new MutableLiveData<>();

    public MutableLiveData<List<ScheduleVisitDetails>> getMutActivity_ResponseApi() {
        return mutActivity_ResponseApi;
    }

    public MutableLiveData<String> getMutErrorResponse() {
        return mutErrorResponse;
    }

    public ArrayList<ScheduleVisitDetails> arrList_scheduledVisitDetails_Data = new ArrayList<>();
    public ScheduleDetailsAdapter scheduleDetailsAdapter = new ScheduleDetailsAdapter(arrList_scheduledVisitDetails_Data);
    public void updateScheduledVisitDetails_Data() {  scheduleDetailsAdapter.setData(arrList_scheduledVisitDetails_Data); }




    //for ScheduleDetailsAdapter
    static String userId_new = MainActivity3API.UserID;

    public void get_ScheduleDetails_Data(String fromDate , String toDate){

        subscribtion = (Disposable) Global.apiService().getScheduleDetails( WebServices.schedule_details_in_dashboard+userId_new+"&fromDate="+fromDate+"&toDate="+toDate)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .unsubscribeOn(Schedulers.io())
                    .subscribe(
                            this::onHomeApiSuccess, this::onApiError
                    );
    }

    private void onHomeApiSuccess(List<ScheduleVisitDetails> result) {
        mutActivity_ResponseApi.setValue(result);
    }


    private void onApiError(Throwable error) {
        mutErrorResponse.setValue(error.getLocalizedMessage());
    }



}
