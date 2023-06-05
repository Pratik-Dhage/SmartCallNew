package com.example.test.schedule_flow.calls_for_the_day;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.test.api_manager.WebServices;
import com.example.test.helper_classes.Global;
import com.example.test.schedule_flow.calls_for_the_day.model.CallsForTheDayResponseModel;
import com.example.test.schedule_flow.visits_for_the_day.model.VisitsForTheDayResponseModel;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CallsForTheDayViewModel extends ViewModel {

    private Disposable subscribtion; //Disposable Interface used to prevent observer from receiving items from Observer before all items are loaded.

    private final MutableLiveData<List<CallsForTheDayResponseModel>> mutCallsForTheDayResponseApi = new MutableLiveData<>();

    private final MutableLiveData<String> mutErrorResponse = new MutableLiveData<>();


    public MutableLiveData<List<CallsForTheDayResponseModel>> getMutCallsForTheDayResponseApi() {
        return mutCallsForTheDayResponseApi;
    }

    public MutableLiveData<String> getMutErrorResponse() {
        return mutErrorResponse;
    }


    public void getCallsForTheDayData(){

        subscribtion = (Disposable) Global.apiService().getCallsForTheDay(WebServices.calls_for_the_day+"userId="+WebServices.userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(
                        this::onHomeApiSuccess, this::onApiError
                );
    }

    private void onHomeApiSuccess(List<CallsForTheDayResponseModel> result) {
        mutCallsForTheDayResponseApi.setValue(result);
    }

    private void onApiError(Throwable error) {
        mutErrorResponse.setValue(error.getLocalizedMessage());
    }


}
