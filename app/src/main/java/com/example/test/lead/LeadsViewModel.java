package com.example.test.lead;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.test.api_manager.WebServices;
import com.example.test.helper_classes.Global;
import com.example.test.lead.adapter.LeadListAdapter;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LeadsViewModel extends ViewModel {


    private Disposable subscribtion; //Disposable Interface used to prevent observer from receiving items from Observer before all items are loaded.


   // private final MutableLiveData<LeadListResponse> mutLeadListResponseApi = new MutableLiveData<>();
    private final MutableLiveData<String> mutErrorResponse = new MutableLiveData<>();

    /*public MutableLiveData<LeadListResponse> getMutLeadListResponseApi() {
        return mutLeadListResponseApi;
    }*/

    public MutableLiveData<String> getMutErrorResponse() {
        return mutErrorResponse;
    }

   /* public  ArrayList<Products> arrListProductsData = new  ArrayList<Products>() ;
    public LeadListAdapter leadListAdapter = new LeadListAdapter(arrListProductsData);
*/

    public void updateLeadListData(){
     //   homeAdapter.setData(arrListProductsData);
    }


    //Call to API with Required Function

    //Lead List Api

    public void getLeads()

    {
       /* subscribtion = (Disposable) Global.apiService().getLeadList(WebServices.SmartCall_BaseURL + WebServices.find_Leads)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(
                     //   this::onHomeApiSuccess, this::onApiError
                );*/

    }
   /* private void onHomeApiSuccess(LeadListResponse result) {
        mutHomeResponseApi.setValue(result);
    }*/

    private void onApiError(Throwable error) {
        mutErrorResponse.setValue(error.getLocalizedMessage());
    }

}
