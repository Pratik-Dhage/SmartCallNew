package com.example.test.npa_flow.dpd;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.test.api_manager.WebServices;
import com.example.test.helper_classes.Global;
import com.example.test.main_dashboard.model.DashBoardResponseModel;
import com.example.test.npa_flow.dpd.adapter.DPD_Adapter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class DPD_ViewModel extends ViewModel {


    private Disposable subscribtion; //Disposable Interface used to prevent observer from receiving items from Observer before all items are loaded.

    private final MutableLiveData<List<DPD_ResponseModel>> mutDPD_ResponseApi = new MutableLiveData<>();
    private final MutableLiveData<String> mutErrorResponse = new MutableLiveData<>();


    public MutableLiveData<List<DPD_ResponseModel>> getMutDPD_ResponseApi() {
        return mutDPD_ResponseApi;
    }

    public MutableLiveData<String> getMutErrorResponse() {
        return mutErrorResponse;
    }


    public ArrayList<DPD_ResponseModel> arrList_DPD_Data = new ArrayList<>();
    public DPD_Adapter dpd_adapter = new DPD_Adapter(arrList_DPD_Data);

    public void updateDPDData(){
        dpd_adapter.setData(arrList_DPD_Data);
    }

   //DPD Queue Api
    public void getDPD_Data(){

        subscribtion = (Disposable) Global.apiService().getDPD_QueueList(WebServices.SmartCall_BaseURL2+ WebServices.dpd_queue)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(
                        this::onHomeApiSuccess, this::onApiError
                );
    }

    private void onHomeApiSuccess(List<DPD_ResponseModel> result) {
        mutDPD_ResponseApi.setValue(result);
    }

    private void onApiError(Throwable error) {
        mutErrorResponse.setValue(error.getLocalizedMessage());
    }



}
