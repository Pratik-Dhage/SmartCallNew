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

    Context context;
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


    public void postCallDetails(String dataSetId , String payment_type, String complete_action){

        //1)PARTIAL AMOUNT PAID
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

     //2)FULL AMOUNT PAID
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

    //3)WILL PAY LATER
    public void postScheduledDateTime(String dataSetId , String payment_type ,String scheduleVisitForCollection_dateTime){
        //WILL PAY LATER
        if(payment_type.contentEquals("will pay later") ){
            subscribtion = (Disposable) Global.apiService().post_call_details(WebServices.call_details_will_pay_later+"&dataSetId="+dataSetId+"&callingAgent="+userId+"&scheduledDateTime="+scheduleVisitForCollection_dateTime,callDetailsList)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .unsubscribeOn(Schedulers.io())
                    .subscribe(
                            this::onHomeApiSuccess, this::onApiError
                    );
        }
    }

    //4)PAYMENT MODE - (SVFC)SCHEDULE VISIT FOR COLLECTION BUTTON CLICK
    public void postScheduledDateTime_SVFC(String dataSetId , String scheduleVisitForCollection_dateTime ){
        subscribtion = (Disposable) Global.apiService().post_call_details(WebServices.call_details_send_visit_for_collection+"&dataSetId="+dataSetId+"&callingAgent="+userId+"&scheduledDateTime="+scheduleVisitForCollection_dateTime,callDetailsList)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .unsubscribeOn(Schedulers.io())
                    .subscribe(
                            this::onHomeApiSuccess, this::onApiError
                    );

    }

    //5)PAYMENT NOTIFICATION - ASKED TO CALL BACK LATER BUTTON CLICK(ATCL->Asked to Call Later)
    public void postScheduledDateTime_ATCL(String dataSetId , String scheduleVisitForCollection_dateTime ){
        subscribtion = (Disposable) Global.apiService().post_call_details(WebServices.call_details_asked_to_call_later+"&dataSetId="+dataSetId+"&callingAgent="+userId+"&scheduledDateTime="+scheduleVisitForCollection_dateTime,callDetailsList)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(
                        this::onHomeApiSuccess, this::onApiError
                );

    }

    // API Changes 29-05-2023
    //Addition Parameters added to API(Pass below 4 parameters as null in above API's)
    /* 1)dateOfVisitPromised – this is string value of date in dd-MM-yyyy format
    2)foName – String
    3)relativeName – String
    4)relativeContactNumber – String
*/

    //6)PAYMENT INFORMATION OF CUSTOMER ACTIVITY - FO NOT VISITED (FNV)BUTTON CLICK
    public void postScheduledDateTime_FNV(String dataSetId , String scheduleVisitForCollection_dateTime, String dateOfVisitPromised,String foName ,String relativeName, String relativeContactNumber){
        subscribtion = (Disposable) Global.apiService().post_call_details(WebServices.call_details_fo_not_visited+"&dataSetId="+dataSetId+"&callingAgent="+userId+"&scheduledDateTime="+scheduleVisitForCollection_dateTime+
                        "&dateOfVisitPromised="+dateOfVisitPromised+"&foName="+foName+"&relativeName="+relativeName+"&relativeContactNumber"+relativeContactNumber,callDetailsList)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(
                        this::onHomeApiSuccess, this::onApiError
                );

    }

    //7)PAYMENT INFORMATION OF CUSTOMER ACTIVITY - LOAN TAKEN BY RELATIVE (LTBR)BUTTON CLICK
    public void postScheduledDateTime_LTBR(String dataSetId , String scheduleVisitForCollection_dateTime, String dateOfVisitPromised,String foName ,String relativeName, String relativeContactNumber){
        subscribtion = (Disposable) Global.apiService().post_call_details(WebServices.call_details_loan_taken_by_relative+"&dataSetId="+dataSetId+"&callingAgent="+userId+"&scheduledDateTime="+scheduleVisitForCollection_dateTime+
                        "&dateOfVisitPromised="+dateOfVisitPromised+"&foName="+foName+"&relativeName="+relativeName+"&relativeContactNumber"+relativeContactNumber,callDetailsList)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(
                        this::onHomeApiSuccess, this::onApiError
                );

    }

    //8)PAYMENT INFORMATION OF CUSTOMER ACTIVITY - ALREADY PAID (AP)BUTTON CLICK
    public void postScheduledDateTime_AP(String dataSetId , String scheduleVisitForCollection_dateTime, String dateOfVisitPromised,String foName ,String relativeName, String relativeContactNumber){
        subscribtion = (Disposable) Global.apiService().post_call_details(WebServices.call_details_already_paid+"&dataSetId="+dataSetId+"&callingAgent="+userId+"&scheduledDateTime="+scheduleVisitForCollection_dateTime+
                        "&dateOfVisitPromised="+dateOfVisitPromised+"&foName="+foName+"&relativeName="+relativeName+"&relativeContactNumber"+relativeContactNumber,callDetailsList)
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
