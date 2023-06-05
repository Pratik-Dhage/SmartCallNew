package com.example.test.npa_flow.status_of_customer;

import android.view.View;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.test.api_manager.WebServices;
import com.example.test.helper_classes.Global;
import com.example.test.main_dashboard.model.DashBoardResponseModel;
import com.example.test.npa_flow.status_of_customer.adapter.StatusOfCustomerDetailsAdapter;
import com.example.test.npa_flow.status_of_customer.model.Activity;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class StatusOfCustomerViewModel extends ViewModel {

    private Disposable subscribtion; //Disposable Interface used to prevent observer from receiving items from Observer before all items are loaded.

    private final MutableLiveData<List<Activity>> mutActivityOfStatusResponseApi = new MutableLiveData<>();
    private final MutableLiveData<String> mutErrorResponse = new MutableLiveData<>();


    public MutableLiveData<List<Activity>> getMutActivityOfStatusResponseApi() {
        return mutActivityOfStatusResponseApi;
    }

    public MutableLiveData<String> getMutErrorResponse() {
        return mutErrorResponse;
    }

    public ArrayList<Activity> arrListActivityData = new ArrayList<>();
    public StatusOfCustomerDetailsAdapter statusOfCustomerDetailsAdapter = new StatusOfCustomerDetailsAdapter(arrListActivityData);
    public void updateStatusOfCustomerData(){  statusOfCustomerDetailsAdapter.setData(arrListActivityData);  }


    public void getDetailsOfStatusOfCustomerData(String dataSetId,String fromDate,String toDate){

        subscribtion = (Disposable) Global.apiService().getStatusDetailsOfCustomer(WebServices.details_of_status_of_customer+"dataSetId="+dataSetId+"&fromDate="+fromDate+"&toDate="+toDate)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(
                        this::onHomeApiSuccess, this::onApiError
                );

    }

    private void onHomeApiSuccess(List<Activity> result) {
        mutActivityOfStatusResponseApi.setValue(result);
    }


    private void onApiError(Throwable error) {
        mutErrorResponse.setValue(error.getLocalizedMessage());
    }

}
