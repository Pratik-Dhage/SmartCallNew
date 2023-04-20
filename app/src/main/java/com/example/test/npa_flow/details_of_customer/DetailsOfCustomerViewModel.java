package com.example.test.npa_flow.details_of_customer;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.test.api_manager.WebServices;
import com.example.test.helper_classes.Global;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class DetailsOfCustomerViewModel extends ViewModel {

    private Disposable subscribtion; //Disposable Interface used to prevent observer from receiving items from Observer before all items are loaded.

    private final MutableLiveData<List<DetailsOfCustomer_ResponseModel>> mutDetailsOfCustomer_ResponseApi = new MutableLiveData<>();
    private final MutableLiveData<String> mutErrorResponse = new MutableLiveData<>();


    public MutableLiveData<List<DetailsOfCustomer_ResponseModel>> getMutDetailsOfCustomer_ResponseApi() {
        return mutDetailsOfCustomer_ResponseApi;
    }

    public MutableLiveData<String> getMutErrorResponse() {
        return mutErrorResponse;
    }


    public ArrayList<DetailsOfCustomer_ResponseModel> arrList_DetailsOfCustomer_Data = new ArrayList<>();


 public int dpd_row_position ; //this will set queue according to position

   // public String dataSetId;
    String queue_0 = "&queue=3738";
    String queue_1 = "&queue=3739";
    String queue_2 = "&queue=3740";

    //Details Of Customer Api
    public void getDetailsOfCustomer_Data(String dataSetId){

        switch (dpd_row_position){

            case 0:

                subscribtion = (Disposable) Global.apiService().getDetailsOfCustomerList(WebServices.SmartCall_BaseURL2+ WebServices.detail_of_customer_common+"dataSetId="+ dataSetId +queue_0)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.io())
                        .subscribe(
                                this::onHomeApiSuccess, this::onApiError
                        );

                break;
            case 1:

                subscribtion = (Disposable) Global.apiService().getDetailsOfCustomerList(WebServices.SmartCall_BaseURL2+ WebServices.detail_of_customer_common+ "dataSetId="+dataSetId +queue_1)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.io())
                        .subscribe(
                                this::onHomeApiSuccess, this::onApiError
                        );

                break;
            case 2:

                subscribtion = (Disposable) Global.apiService().getDetailsOfCustomerList(WebServices.SmartCall_BaseURL2+ WebServices.detail_of_customer_common+"dataSetId="+ dataSetId +queue_2)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.io())
                        .subscribe(
                                this::onHomeApiSuccess, this::onApiError
                        );


        }
    }


    private void onHomeApiSuccess(List<DetailsOfCustomer_ResponseModel> result) {
        mutDetailsOfCustomer_ResponseApi.setValue(result);
    }

    private void onApiError(Throwable error) {
        mutErrorResponse.setValue(error.getLocalizedMessage());
    }


}