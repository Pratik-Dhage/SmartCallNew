package com.example.test.notes_history;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.test.api_manager.WebServices;
import com.example.test.helper_classes.Global;
import com.example.test.notes_history.adapter.NotesHistoryAdapter;
import com.example.test.npa_flow.details_of_customer.DetailsOfCustomerResponseModel;
import com.example.test.npa_flow.details_of_customer.adapter.DetailsOfCustomerAdapter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class NotesHistoryViewModel extends ViewModel {

    private Disposable subscribtion; //Disposable Interface used to prevent observer from receiving items from Observer before all items are loaded.


    private final MutableLiveData<List<NotesHistoryResponseModel>> mutNotesHistory_ResponseApi = new MutableLiveData<>();

    private final MutableLiveData<String> mutErrorResponse = new MutableLiveData<>();

    public MutableLiveData<List<NotesHistoryResponseModel>> getMutNotesHistory_ResponseApi() {
        return mutNotesHistory_ResponseApi;
    }

    public MutableLiveData<String> getMutErrorResponse() {
        return mutErrorResponse;
    }

    public ArrayList<NotesHistoryResponseModel> arrList_NotesHistory_Data = new ArrayList<>();
    public NotesHistoryAdapter notesHistoryAdapter = new NotesHistoryAdapter(arrList_NotesHistory_Data);
    public void updateNotesHistory_Data() {  notesHistoryAdapter.setData(arrList_NotesHistory_Data); }


    //Notes-History Api

    public void getNotesHistoryData(String dataSetId){

        subscribtion = (Disposable) Global.apiService().getNotesHistory( WebServices.notes_History+"dataSetId="+ dataSetId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(
                        this::onHomeApiSuccess, this::onApiError
                );

    }

    private void onHomeApiSuccess(List<NotesHistoryResponseModel> result) {
        mutNotesHistory_ResponseApi.setValue(result);
    }

    private void onApiError(Throwable error) {
        mutErrorResponse.setValue(error.getLocalizedMessage());
    }


}
