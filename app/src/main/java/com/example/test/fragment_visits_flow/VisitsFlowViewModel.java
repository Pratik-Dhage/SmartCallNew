package com.example.test.fragment_visits_flow;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.test.api_manager.WebServices;
import com.example.test.helper_classes.Global;
import com.example.test.login.LoginActivity;
import com.example.test.main_dashboard.MainActivity3API;
import com.example.test.npa_flow.call_details.CallDetails;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class VisitsFlowViewModel extends ViewModel {

    private Disposable subscribtion; //Disposable Interface used to prevent observer from receiving items from Observer before all items are loaded.

    private final MutableLiveData<String> mutVisitsFlowCallDetailsResponseApi = new MutableLiveData<>();
    private final MutableLiveData<String> mutErrorResponse = new MutableLiveData<>();

    public MutableLiveData<String> getMutVisitsCallDetailsResponseApi() {
        return mutVisitsFlowCallDetailsResponseApi;
    }


    public MutableLiveData<String> getMutErrorResponse() {
        return mutErrorResponse;
    }
    String userId = MainActivity3API.UserID;

    VisitsFlowCallDetailsActivity visitsFlowCallDetailsActivity = new VisitsFlowCallDetailsActivity();
    List<CallDetails> callDetailsList = visitsFlowCallDetailsActivity.sendCallLogDetailsList_VisitsFlow();


    public void postVisitsFlowCallDateTime(String ApiType,String dataSetId, String scheduleVisitForCollection_dateTime, String dateOfVisitPromised, String foName, String relativeName, String relativeContactNumber) {
        subscribtion = (Disposable) Global.apiService().post_call_details( ApiType+ "&dataSetId=" + dataSetId + "&callingAgent=" + userId + "&scheduledDateTime=" + scheduleVisitForCollection_dateTime +
                        "&dateOfVisitPromised=" + dateOfVisitPromised + "&foName=" + foName + "&relativeName=" + relativeName + "&relativeContactNumber" + relativeContactNumber,callDetailsList)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(
                        this::onHomeApiSuccess, this::onApiError
                );

    }

    //FOR VISITS FLOW-> READY TO PAY -> CHEQUE PAYMENT & CASH PAYMENT
    public void postVisitsFlowCallDateTimeCheque_Cash(String ApiType,String dataSetId, String scheduleVisitForCollection_dateTime, String dateOfVisitPromised, String foName, String relativeName, String relativeContactNumber,
                                                 String amountCollected,String chequeDate ,String chequeNumber ,String chequeAmount, String bankName) {
        subscribtion = (Disposable) Global.apiService().post_call_details( ApiType+ "&dataSetId=" + dataSetId + "&callingAgent=" + userId + "&scheduledDateTime=" + scheduleVisitForCollection_dateTime +
                        "&dateOfVisitPromised=" + dateOfVisitPromised + "&foName=" + foName + "&relativeName=" + relativeName + "&relativeContactNumber" + relativeContactNumber
                        +"&amountCollected="+amountCollected + "&chequeDate="+chequeDate + "&chequeNumber="+chequeNumber + "&chequeAmount="+chequeAmount + "&bankName="+bankName,callDetailsList)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(
                        this::onHomeApiSuccess, this::onApiError
                );

    }

    private void onHomeApiSuccess(String result) {
        mutVisitsFlowCallDetailsResponseApi.setValue(result);
    }

    private void onApiError(Throwable error) {
        mutErrorResponse.setValue(error.getLocalizedMessage());
    }


}
