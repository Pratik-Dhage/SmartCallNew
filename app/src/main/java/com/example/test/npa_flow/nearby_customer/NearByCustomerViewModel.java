package com.example.test.npa_flow.nearby_customer;

import android.location.Location;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.test.api_manager.WebServices;
import com.example.test.helper_classes.Global;
import com.example.test.main_dashboard.MainActivity3API;
import com.example.test.npa_flow.loan_collection.LoanCollectionListResponseModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class NearByCustomerViewModel extends ViewModel {

    private Disposable subscribtion; //Disposable Interface used to prevent observer from receiving items from Observer before all items are loaded.

    // Using LoanCollectionResponseModel for NearByCustomer List
    private final MutableLiveData<List<LoanCollectionListResponseModel>> mutLoanCollectionList_ResponseApi = new MutableLiveData<>();
    private final MutableLiveData<String> mutErrorResponse = new MutableLiveData<>();


    public MutableLiveData<List<LoanCollectionListResponseModel>> getMutLoanCollectionList_ResponseApi() {
        return mutLoanCollectionList_ResponseApi;
    }

    public MutableLiveData<String> getMutErrorResponse() {
        return mutErrorResponse;
    }

    public ArrayList<LoanCollectionListResponseModel> arrList_LoanCollectionList = new ArrayList<>();
    public Location currentLocation;
    public NearByCustomerListAdapter nearByCustomerListAdapter = new NearByCustomerListAdapter(arrList_LoanCollectionList,currentLocation);
    public void updateNearByCustomerData(){ nearByCustomerListAdapter.setData(arrList_LoanCollectionList);  }



    public void getNearByCustomerList_Data(double userLatitude , double userLongitude){

        subscribtion = (Disposable) Global.apiService().getNearByCustomersList( WebServices.visit_nearBy_customers+"userId="+ MainActivity3API.UserID+"&lat=" + userLatitude+ "&lon="+userLongitude)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(
                        this::onHomeApiSuccess, this::onApiError
                );
    }

    private void onHomeApiSuccess(List<LoanCollectionListResponseModel> result) {
        mutLoanCollectionList_ResponseApi.setValue(result);
    }

    private void onApiError(Throwable error) {
        mutErrorResponse.setValue(error.getLocalizedMessage());
    }

}
