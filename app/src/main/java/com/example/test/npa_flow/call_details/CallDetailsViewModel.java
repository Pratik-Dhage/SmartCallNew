package com.example.test.npa_flow.call_details;

import android.content.Context;

import androidx.core.app.ComponentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.test.api_manager.WebServices;
import com.example.test.helper_classes.Global;
import com.example.test.main_dashboard.model.DashBoardResponseModel;
import com.example.test.npa_flow.ScheduleVisitForCollectionActivity;
import com.example.test.npa_flow.details_of_customer.DetailsOfCustomerActivity;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CallDetailsViewModel extends ViewModel {

    private Disposable subscribtion; //Disposable Interface used to prevent observer from receiving items from Observer before all items are loaded.

    private final MutableLiveData<String> mutCallDetailsResponseApi = new MutableLiveData<>();
    private final MutableLiveData<String> mutErrorResponse = new MutableLiveData<>();

    public MutableLiveData<String> getMutCallDetailsResponseApi() {
        return mutCallDetailsResponseApi;
    }

    public MutableLiveData<String> getMutErrorResponse() {
        return mutErrorResponse;
    }


    String userId = "CA_01_001";

    DetailsOfCustomerActivity detailsOfCustomerActivity= new DetailsOfCustomerActivity();
    List<CallDetails> callDetailsList = detailsOfCustomerActivity.sendCallLogDetailsList_WillPayLater(); //for Will Pay Later
    List<CallDetails> callDetailsList_full_partial_amount = detailsOfCustomerActivity.sendCallLogDetailsList_FullPartialAmountPaid(); //for Full/Partial Amt. paid
    String send_callScheduledTime = DetailsOfCustomerActivity.send_callScheduledTime;
    //WebServices.call_details_complete_no_change_partial_amt_paid+"&dataSetId="+dataSetId+"&callingAgent="+userId

    public void postCallDetails(String dataSetId , String payment_type, String complete_action){

        //PARTIAL AMOUNT PAID
     if(payment_type.contentEquals("from_payment_status_partial_amt_paid")){

         // Post Call Data => Call_AttemptNo. , Call_Time , Call_Duration, Call_Notes , Call_Recording
         // for Every Attempt (0,1,2) there will be these Individual Call Data
         //3 Scenarios => 1) No Change 2) Need To Update Details 3) Escalate To BM (Branch Manager)

         if(complete_action.contentEquals("complete_no_change")){

             subscribtion = (Disposable) Global.apiService().post_call_details(WebServices.call_details_complete_no_change_partial_amt_paid+"&dataSetId="+dataSetId+"&callingAgent="+userId+"&scheduledDateTime="+"",callDetailsList_full_partial_amount)
                     .subscribeOn(Schedulers.io())
                     .observeOn(AndroidSchedulers.mainThread())
                     .unsubscribeOn(Schedulers.io())
                     .subscribe(
                             this::onHomeApiSuccess, this::onApiError
                     );
         }

         if(complete_action.contentEquals("complete_need_to_update_details")){

             subscribtion = (Disposable) Global.apiService().post_call_details(WebServices.call_details_complete_need_to_update_details_partial_amt_paid+"&dataSetId="+dataSetId+"&callingAgent="+userId+"&scheduledDateTime="+"",callDetailsList_full_partial_amount)
                     .subscribeOn(Schedulers.io())
                     .observeOn(AndroidSchedulers.mainThread())
                     .unsubscribeOn(Schedulers.io())
                     .subscribe(
                             this::onHomeApiSuccess, this::onApiError
                     );
         }

         if(complete_action.contentEquals("complete_escalate_to_bm")) {
             subscribtion = (Disposable) Global.apiService().post_call_details(WebServices.call_details_complete_escalate_to_bm_partial_amt_paid+"&dataSetId="+dataSetId+"&callingAgent="+userId+"&scheduledDateTime="+"",callDetailsList_full_partial_amount)
                     .subscribeOn(Schedulers.io())
                     .observeOn(AndroidSchedulers.mainThread())
                     .unsubscribeOn(Schedulers.io())
                     .subscribe(
                             this::onHomeApiSuccess, this::onApiError
                     );
         }
     }

     //FULL AMOUNT PAID
     if(payment_type.contentEquals("from_payment_status_full_amt_paid")){

         // Post Call Data => Call_AttemptNo. , Call_Time , Call_Duration, Call_Notes , Call_Recording
         // for Every Attempt (0,1,2) there will be these Individual Call Data

         if(complete_action.contentEquals("complete_no_change")){

             subscribtion = (Disposable) Global.apiService().post_call_details(WebServices.call_details_complete_no_change_full_amt_paid+"&dataSetId="+dataSetId+"&callingAgent="+userId+"&scheduledDateTime="+"",callDetailsList_full_partial_amount)
                     .subscribeOn(Schedulers.io())
                     .observeOn(AndroidSchedulers.mainThread())
                     .unsubscribeOn(Schedulers.io())
                     .subscribe(
                             this::onHomeApiSuccess, this::onApiError
                     );
         }

         if(complete_action.contentEquals("complete_need_to_update_details")){

             subscribtion = (Disposable) Global.apiService().post_call_details(WebServices.call_details_complete_need_to_update_details_full_amt_paid+"&dataSetId="+dataSetId+"&callingAgent="+userId+"&scheduledDateTime="+"",callDetailsList_full_partial_amount)
                     .subscribeOn(Schedulers.io())
                     .observeOn(AndroidSchedulers.mainThread())
                     .unsubscribeOn(Schedulers.io())
                     .subscribe(
                             this::onHomeApiSuccess, this::onApiError
                     );
         }

         if(complete_action.contentEquals("complete_escalate_to_bm")) {
             subscribtion = (Disposable) Global.apiService().post_call_details(WebServices.call_details_complete_escalate_to_bm_full_amt_paid+"&dataSetId="+dataSetId+"&callingAgent="+userId+"&scheduledDateTime="+"",callDetailsList_full_partial_amount)
                     .subscribeOn(Schedulers.io())
                     .observeOn(AndroidSchedulers.mainThread())
                     .unsubscribeOn(Schedulers.io())
                     .subscribe(
                             this::onHomeApiSuccess, this::onApiError
                     );
         }


     }


    }

    //WILL PAY LATER
    public void postScheduledDateTime(String dataSetId , String payment_type){
        //WILL PAY LATER
        if(payment_type.contentEquals("will pay later") ){
            subscribtion = (Disposable) Global.apiService().post_call_details(WebServices.call_details_will_pay_later+"&dataSetId="+dataSetId+"&callingAgent="+userId+"&scheduledDateTime="+send_callScheduledTime,callDetailsList)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .unsubscribeOn(Schedulers.io())
                    .subscribe(
                            this::onHomeApiSuccess, this::onApiError
                    );
        }
    }

    //PAYMENT MODE - (SVFC)SCHEDULE VISIT FOR COLLECTION BUTTON CLICK
    public void postScheduledDateTime_SVFC(String dataSetId ){
        subscribtion = (Disposable) Global.apiService().post_call_details(WebServices.call_details_send_visit_for_collection+"&dataSetId="+dataSetId+"&callingAgent="+userId+"&scheduledDateTime="+send_callScheduledTime,callDetailsList)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .unsubscribeOn(Schedulers.io())
                    .subscribe(
                            this::onHomeApiSuccess, this::onApiError
                    );

    }


    private void onHomeApiSuccess(String result) {
        mutCallDetailsResponseApi.setValue(result);
    }

    private void onApiError(Throwable error) {
        mutErrorResponse.setValue(error.getLocalizedMessage());
    }


}
