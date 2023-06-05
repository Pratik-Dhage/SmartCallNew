package com.example.test.schedule_flow.visits_for_the_day;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.test.api_manager.WebServices;
import com.example.test.helper_classes.Global;
import com.example.test.main_dashboard.model.DashBoardResponseModel;
import com.example.test.schedule_flow.visits_for_the_day.model.VisitsForTheDayResponseModel;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class VisitsForTheDayViewModel  extends ViewModel {

    private Disposable subscribtion; //Disposable Interface used to prevent observer from receiving items from Observer before all items are loaded.


    private final MutableLiveData<List<VisitsForTheDayResponseModel>> mutVisitsForTheDayResponseApi = new MutableLiveData<>();

    private final MutableLiveData<String> mutErrorResponse = new MutableLiveData<>();


    public MutableLiveData<List<VisitsForTheDayResponseModel>> getMutVisitsForTheDayResponseApi() {
        return mutVisitsForTheDayResponseApi;
    }

    public MutableLiveData<String> getMutErrorResponse() {
        return mutErrorResponse;
    }



    public void getVisitsForTheDayData(){

        subscribtion = (Disposable) Global.apiService().getVisitsForTheDay(WebServices.visits_for_the_day+"userId="+WebServices.userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(
                        this::onHomeApiSuccess, this::onApiError
                );
    }

    private void onHomeApiSuccess(List<VisitsForTheDayResponseModel> result) {
        mutVisitsForTheDayResponseApi.setValue(result);
    }

    private void onApiError(Throwable error) {
        mutErrorResponse.setValue(error.getLocalizedMessage());
    }

}
