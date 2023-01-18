package com.example.test.lead;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.test.api_manager.WebServices;
import com.example.test.helper_classes.Global;
import com.example.test.lead.adapter.LeadListAdapter;
import com.example.test.lead.model.LeadListResponseModel;
import com.example.test.lead.model.LeadModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LeadsViewModel extends ViewModel {


    private Disposable subscribtion; //Disposable Interface used to prevent observer from receiving items from Observer before all items are loaded.


    private final MutableLiveData<List<LeadModel>> mutLeadListResponseApi = new MutableLiveData<>();
    private final MutableLiveData<String> mutErrorResponse = new MutableLiveData<>();

    public MutableLiveData<List<LeadModel>> getMutLeadListResponseApi() {
        return mutLeadListResponseApi;
    }

    public MutableLiveData<String> getMutErrorResponse() {
        return mutErrorResponse;
    }

    public ArrayList<LeadModel> arrListLeadListData = new  ArrayList<LeadModel>() ;
    public LeadListAdapter leadListAdapter = new LeadListAdapter(arrListLeadListData);

    public void updateLeadListData(){
        leadListAdapter.setData(arrListLeadListData);
    }


    //Call to API with Required Function

    //Lead List Api

    public void getLeads()

    {
        subscribtion = (Disposable) Global.apiService().getLeadList(WebServices.SmartCall_BaseURL2 + WebServices.find_Leads)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(
                        this::onHomeApiSuccess, this::onApiError
                );

    }
    private void onHomeApiSuccess(List<LeadModel> result) {
        mutLeadListResponseApi.setValue(result);
    }

    private void onApiError(Throwable error) {
        mutErrorResponse.setValue(error.getLocalizedMessage());
    }

}
