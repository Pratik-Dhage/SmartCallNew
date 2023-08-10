package com.example.test.npa_flow.radio_buttons;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.test.api_manager.WebServices;
import com.example.test.helper_classes.Global;
import com.example.test.npa_flow.details_of_customer.DetailsOfCustomerResponseModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class RadioButtonsViewModel extends ViewModel {


    private Disposable subscribtion; //Disposable Interface used to prevent observer from receiving items from Observer before all items are loaded.

    private final MutableLiveData<List<RadioButtonReasons>> mutRadioButtonsReason_ResponseApi = new MutableLiveData<>();
    private final MutableLiveData<String> mutErrorResponse = new MutableLiveData<>();

    public MutableLiveData<List<RadioButtonReasons>> getMutRadioButtonsReason_ResponseApi() {
        return mutRadioButtonsReason_ResponseApi;
    }

    public MutableLiveData<String> getMutErrorResponse() {
        return mutErrorResponse;
    }


    public ArrayList<RadioButtonReasons> arrList_RadioButtonsReason_Data  =new ArrayList<>();
    RadioButtonsReasonAdapter radioButtonsReasonAdapter = new RadioButtonsReasonAdapter(arrList_RadioButtonsReason_Data);
    public void updateRadioButtonReasons_Data() { radioButtonsReasonAdapter.setData(arrList_RadioButtonsReason_Data); }


      //for NotSpokeToCustomerActivity -> Physical Visit Required
    public void getRadioButtonsReason_Data(){

        subscribtion = (Disposable) Global.apiService().getRadioButtonReasons( WebServices.radio_buttons_reason)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(
                        this::onHomeApiSuccess, this::onApiError
                );

    }

    private void onHomeApiSuccess(List<RadioButtonReasons> result) {
        mutRadioButtonsReason_ResponseApi.setValue(result);
    }

    private void onApiError(Throwable error) {
        mutErrorResponse.setValue(error.getLocalizedMessage());
    }
}
