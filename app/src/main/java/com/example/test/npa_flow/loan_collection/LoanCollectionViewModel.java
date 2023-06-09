package com.example.test.npa_flow.loan_collection;

import android.content.Context;
import android.location.Location;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.test.api_manager.WebServices;
import com.example.test.fragments_activity.fragments.adapter.LoanCollectionFragmentAdapter;
import com.example.test.helper_classes.Global;
import com.example.test.main_dashboard.MainActivity3API;
import com.example.test.npa_flow.dpd.DPD_ResponseModel;
import com.example.test.npa_flow.loan_collection.adapter.LoanCollectionAdapter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LoanCollectionViewModel extends ViewModel {

    Context context;
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
    public Location currentLocation;
    // for LoanCollectionActivity
    public LoanCollectionAdapter loanCollectionAdapter = new LoanCollectionAdapter(arrList_LoanCollectionList,currentLocation);

    public void updateLoanCollectionData(){ loanCollectionAdapter.setData(arrList_LoanCollectionList);  }

    //for LoanCollectionFragment
    public LoanCollectionFragmentAdapter loanCollectionFragmentAdapter = new LoanCollectionFragmentAdapter(arrList_LoanCollectionList);

    public void updateLoanCollectionFragmentData(){ loanCollectionFragmentAdapter.setData(arrList_LoanCollectionList);  }




    //Loan Collection List Api
    public void getLoanCollectionList_Data(int DPD_row_position){


            //According to Row Position of DPD Queue in DPD Activity , the respective Api Response will be Called
        switch(DPD_row_position){
            case 0:
                subscribtion = (Disposable) Global.apiService().getLoanCollectionList( WebServices.loan_collection_list_30Days+"userId="+ MainActivity3API.UserID+"&branchCode=" + MainActivity3API.BranchCode+ "&queue=3738")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.io())
                        .subscribe(
                                this::onHomeApiSuccess, this::onApiError
                        );
                break;
            case 1:
                subscribtion = (Disposable) Global.apiService().getLoanCollectionList( WebServices.loan_collection_list_60Days+"userId="+ MainActivity3API.UserID+"&branchCode=" + MainActivity3API.BranchCode+ "&queue=3739")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.io())
                        .subscribe(
                                this::onHomeApiSuccess, this::onApiError
                        );
                break;
            case 2:
                subscribtion = (Disposable) Global.apiService().getLoanCollectionList( WebServices.loan_collection_list_90Days+"userId="+ MainActivity3API.UserID+"&branchCode=" + MainActivity3API.BranchCode+ "&queue=3740")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.io())
                        .subscribe(
                                this::onHomeApiSuccess, this::onApiError
                        );
                break;

            case 3:
                subscribtion = (Disposable) Global.apiService().getLoanCollectionList( WebServices.loan_collection_list_above90Days+"userId="+ MainActivity3API.UserID+"&branchCode=" + MainActivity3API.BranchCode+ "&queue=3750")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.io())
                        .subscribe(
                                this::onHomeApiSuccess, this::onApiError
                        );
                break;


            default:
               /* subscribtion = (Disposable) Global.apiService().getLoanCollectionList(WebServices.SmartCall_BaseURL2+ WebServices.loan_collection_list_30Days)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.io())
                        .subscribe(
                                this::onHomeApiSuccess, this::onApiError
                        );
*/
        }

    }


    private void onHomeApiSuccess(List<LoanCollectionListResponseModel> result) {
        mutLoanCollectionList_ResponseApi.setValue(result);
    }

    private void onApiError(Throwable error) {
        mutErrorResponse.setValue(error.getLocalizedMessage());
    }


}
