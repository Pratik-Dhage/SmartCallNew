package com.example.test.npa_flow.save_location;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.test.api_manager.WebServices;
import com.example.test.helper_classes.Global;
import com.example.test.npa_flow.status_of_customer.model.Activity;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SaveLocationOfCustomerViewModel extends ViewModel {

    private Disposable subscribtion; //Disposable Interface used to prevent observer from receiving items from Observer before all items are loaded.

    private final MutableLiveData<String> mutSaveLocationOfCustomerResponseApi = new MutableLiveData<>();
    private final MutableLiveData<String> mutErrorResponse = new MutableLiveData<>();


    public MutableLiveData<String> getMutSaveLocationOfCustomerResponseApi() {
        return mutSaveLocationOfCustomerResponseApi;
    }

    public MutableLiveData<String> getMutErrorResponse() {
        return mutErrorResponse;
    }

    public void getSavedLocationOfCustomerData(String dataSetId , String lat , String lon , String dist) {

        subscribtion = (Disposable) Global.apiService().getSavedLocationOfCustomer(WebServices.save_location_of_customer+"dataSetId="+dataSetId+"&lat="+lat+"&lon="+lon+"&dist="+dist)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(
                        this::onHomeApiSuccess, this::onApiError
                );

    }

    private void onHomeApiSuccess(String result) {
        mutSaveLocationOfCustomerResponseApi.setValue(String.valueOf(result));
    }


    private void onApiError(Throwable error) {
        mutErrorResponse.setValue(error.getLocalizedMessage());
    }

}
