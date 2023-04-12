package com.example.test.npa_flow.loan_collection;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.test.api_manager.WebServices;
import com.example.test.helper_classes.Global;
import com.example.test.npa_flow.dpd.DPD_ResponseModel;
import com.example.test.npa_flow.loan_collection.adapter.LoanCollectionAdapter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LoanCollectionViewModel extends ViewModel {

    private Disposable subscribtion; //Disposable Interface used to prevent observer from receiving items from Observer before all items are loaded.

    private final MutableLiveData<List<LoanCollectionListResponseModel>> mutLoanCollectionList_ResponseApi = new MutableLiveData<>();
    private final MutableLiveData<String> mutErrorResponse = new MutableLiveData<>();


    public MutableLiveData<List<LoanCollectionListResponseModel>> getMutLoanCollectionList_ResponseApi() {
        return mutLoanCollectionList_ResponseApi;
    }

    public MutableLiveData<String> getMutErrorResponse() {
        return mutErrorResponse;
    }


    public ArrayList<LoanCollectionListResponseModel> arrList_LoanCollectionList = new ArrayList<>();
    public LoanCollectionAdapter loanCollectionAdapter = new LoanCollectionAdapter(arrList_LoanCollectionList);

    public void updateLoanCollectionData(){ loanCollectionAdapter.setData(arrList_LoanCollectionList);  }


    //Loan Collection List Api
    public void getLoanCollectionList_Data(){

        subscribtion = (Disposable) Global.apiService().getLoanCollectionList(WebServices.SmartCall_BaseURL2+ WebServices.loan_collection_list)
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
