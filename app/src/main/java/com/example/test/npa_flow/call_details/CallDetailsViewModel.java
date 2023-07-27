package com.example.test.npa_flow.call_details;

import android.content.Context;

import androidx.core.app.ComponentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.test.api_manager.WebServices;
import com.example.test.helper_classes.Global;
import com.example.test.login.LoginActivity;
import com.example.test.main_dashboard.MainActivity3API;
import com.example.test.main_dashboard.model.DashBoardResponseModel;
import com.example.test.npa_flow.ScheduleVisitForCollectionActivity;
import com.example.test.npa_flow.details_of_customer.DetailsOfCustomerActivity;
import com.example.test.roomDB.model.CallDetailsListRoomModel;

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


   public static String userId = MainActivity3API.UserID ;

    DetailsOfCustomerActivity detailsOfCustomerActivity = new DetailsOfCustomerActivity();
    List<CallDetails> callDetailsList = detailsOfCustomerActivity.sendCallLogDetailsList_WillPayLater(); //for Will Pay Later,SVFC,Asked To call Back,Already Paid
    List<CallDetails> callDetailsList_full_partial_amount = detailsOfCustomerActivity.sendCallLogDetailsList_FullPartialAmountPaid(); //for Full/Partial Amt. paid


    public void postCallDetails(String dataSetId, String payment_type, String complete_action) {

        System.out.println("CallDetailsViewModel CallingAgent:"+userId);

        //1)PARTIAL AMOUNT PAID
        if (payment_type.contentEquals("from_payment_status_partial_amt_paid")) {

            // Post Call Data => Call_AttemptNo. , Call_Time , Call_Duration, Call_Notes , Call_Recording
            // for Every Attempt (0,1,2) there will be these Individual Call Data
            //3 Scenarios => 1) No Change 2) Need To Update Details 3) Escalate To BM (Branch Manager)

            if (complete_action.contentEquals("complete_no_change")) {

                subscribtion = (Disposable) Global.apiService().post_call_details(WebServices.call_details_complete_no_change_partial_amt_paid + "&dataSetId=" + dataSetId + "&callingAgent=" + MainActivity3API.UserID + "&scheduledDateTime=" + "" +
                                "&dateOfVisitPromised=" + "&foName=" + "&relativeName=" + "&relativeContactNumber="+"&reason=", callDetailsList_full_partial_amount)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.io())
                        .subscribe(
                                this::onHomeApiSuccess, this::onApiError
                        );
            }

            if (complete_action.contentEquals("complete_need_to_update_details")) {

                subscribtion = (Disposable) Global.apiService().post_call_details(WebServices.call_details_complete_need_to_update_details_partial_amt_paid + "&dataSetId=" + dataSetId + "&callingAgent=" + MainActivity3API.UserID + "&scheduledDateTime=" + "" +
                                "&dateOfVisitPromised=" + "&foName=" + "&relativeName=" + "&relativeContactNumber="+"&reason=", callDetailsList_full_partial_amount)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.io())
                        .subscribe(
                                this::onHomeApiSuccess, this::onApiError
                        );
            }

            if (complete_action.contentEquals("complete_escalate_to_bm")) {
                subscribtion = (Disposable) Global.apiService().post_call_details(WebServices.call_details_complete_escalate_to_bm_partial_amt_paid + "&dataSetId=" + dataSetId + "&callingAgent=" + MainActivity3API.UserID + "&scheduledDateTime=" + "" +
                                "&dateOfVisitPromised=" + "&foName=" + "&relativeName=" + "&relativeContactNumber="+"&reason=", callDetailsList_full_partial_amount)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.io())
                        .subscribe(
                                this::onHomeApiSuccess, this::onApiError
                        );
            }
        }

        //2)FULL AMOUNT PAID
        if (payment_type.contentEquals("from_payment_status_full_amt_paid")) {

            // Post Call Data => Call_AttemptNo. , Call_Time , Call_Duration, Call_Notes , Call_Recording
            // for Every Attempt (0,1,2) there will be these Individual Call Data

            if (complete_action.contentEquals("complete_no_change")) {

                subscribtion = (Disposable) Global.apiService().post_call_details(WebServices.call_details_complete_no_change_full_amt_paid + "&dataSetId=" + dataSetId + "&callingAgent=" + MainActivity3API.UserID + "&scheduledDateTime=" + "" +
                                "&dateOfVisitPromised=" + "&foName=" + "&relativeName=" + "&relativeContactNumber="+"&reason=", callDetailsList_full_partial_amount)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.io())
                        .subscribe(
                                this::onHomeApiSuccess, this::onApiError
                        );
            }

            if (complete_action.contentEquals("complete_need_to_update_details")) {

                subscribtion = (Disposable) Global.apiService().post_call_details(WebServices.call_details_complete_need_to_update_details_full_amt_paid + "&dataSetId=" + dataSetId + "&callingAgent=" + MainActivity3API.UserID + "&scheduledDateTime=" + "" +
                                "&dateOfVisitPromised=" + "&foName=" + "&relativeName=" + "&relativeContactNumber="+"&reason=", callDetailsList_full_partial_amount)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.io())
                        .subscribe(
                                this::onHomeApiSuccess, this::onApiError
                        );
            }

            if (complete_action.contentEquals("complete_escalate_to_bm")) {
                subscribtion = (Disposable) Global.apiService().post_call_details(WebServices.call_details_complete_escalate_to_bm_full_amt_paid + "&dataSetId=" + dataSetId + "&callingAgent=" + MainActivity3API.UserID + "&scheduledDateTime=" + "" +
                                "&dateOfVisitPromised=" + "&foName=" + "&relativeName=" + "&relativeContactNumber="+"&reason=", callDetailsList_full_partial_amount)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.io())
                        .subscribe(
                                this::onHomeApiSuccess, this::onApiError
                        );
            }


        }


    }

    //3)WILL PAY LATER (STTC- RTP - SLFOP - WPL)
    public void postScheduledDateTime(String dataSetId, String payment_type, String scheduleVisitForCollection_dateTime,List<CallDetails> callDetailsList) {

        System.out.println("CallDetailsViewModel CallingAgent:"+userId);

        //WILL PAY LATER
        if (payment_type.contentEquals("will pay later")) {
            subscribtion = (Disposable) Global.apiService().post_call_details(WebServices.call_details_will_pay_later + "&dataSetId=" + dataSetId + "&callingAgent=" + MainActivity3API.UserID + "&scheduledDateTime=" + scheduleVisitForCollection_dateTime +
                            "&dateOfVisitPromised=" + "&foName=" + "&relativeName=" + "&relativeContactNumber="+"&reason=", callDetailsList)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .unsubscribeOn(Schedulers.io())
                    .subscribe(
                            this::onHomeApiSuccess, this::onApiError
                    );
        }

        //WILL PAY LATER -> UPDATE (STTC -NRTP -WPL -UPDATE)
        if(payment_type.contentEquals("will pay later update")){
            subscribtion = (Disposable) Global.apiService().post_call_details(WebServices.call_details_will_pay_later_update + "&dataSetId=" + dataSetId + "&callingAgent=" + MainActivity3API.UserID + "&scheduledDateTime=" + scheduleVisitForCollection_dateTime +
                            "&dateOfVisitPromised=" + "&foName=" + "&relativeName=" + "&relativeContactNumber="+"&reason=", callDetailsList)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .unsubscribeOn(Schedulers.io())
                    .subscribe(
                            this::onHomeApiSuccess, this::onApiError
                    );
        }
    }

    //4)PAYMENT MODE - (SVFC)SCHEDULE VISIT FOR COLLECTION BUTTON CLICK
    public void postScheduledDateTime_SVFC(String dataSetId, String scheduleVisitForCollection_dateTime, List<CallDetails> callDetailsList) {

        System.out.println("CallDetailsViewModel CallingAgent:"+userId);

        subscribtion = (Disposable) Global.apiService().post_call_details(WebServices.call_details_send_visit_for_collection + "&dataSetId=" + dataSetId + "&callingAgent=" + MainActivity3API.UserID + "&scheduledDateTime=" + scheduleVisitForCollection_dateTime +
                        "&dateOfVisitPromised=" + "&foName=" + "&relativeName=" + "&relativeContactNumber="+"&reason=", callDetailsList)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(
                        this::onHomeApiSuccess, this::onApiError
                );

    }

    //5)PAYMENT NOTIFICATION - ASKED TO CALL BACK LATER BUTTON CLICK(ATCL->Asked to Call Later)
    public void postScheduledDateTime_ATCL(String dataSetId, String scheduleVisitForCollection_dateTime, List<CallDetails> callDetailsList) {

        System.out.println("CallDetailsViewModel CallingAgent:"+userId);

        subscribtion = (Disposable) Global.apiService().post_call_details(WebServices.call_details_asked_to_call_later + "&dataSetId=" + dataSetId + "&callingAgent=" + MainActivity3API.UserID + "&scheduledDateTime=" + scheduleVisitForCollection_dateTime +
                        "&dateOfVisitPromised=" + "&foName=" + "&relativeName=" + "&relativeContactNumber="+"&reason=", callDetailsList)
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
    public void postScheduledDateTime_FNV(String dataSetId, String scheduleVisitForCollection_dateTime, String dateOfVisitPromised, String foName, String relativeName, String relativeContactNumber) {

        System.out.println("CallDetailsViewModel CallingAgent:"+userId);

        subscribtion = (Disposable) Global.apiService().post_call_details(WebServices.call_details_fo_not_visited + "&dataSetId=" + dataSetId + "&callingAgent=" + MainActivity3API.UserID + "&scheduledDateTime=" + scheduleVisitForCollection_dateTime +
                        "&dateOfVisitPromised=" + dateOfVisitPromised + "&foName=" + foName + "&relativeName=" + relativeName + "&relativeContactNumber=" + relativeContactNumber+"&reason=", detailsOfCustomerActivity.sendCallLogDetailsList_FNV_LTBR("FNV"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(
                        this::onHomeApiSuccess, this::onApiError
                );

    }

    //7)PAYMENT INFORMATION OF CUSTOMER ACTIVITY - LOAN TAKEN BY RELATIVE (LTBR)BUTTON CLICK
    public void postScheduledDateTime_LTBR(String dataSetId, String scheduleVisitForCollection_dateTime, String dateOfVisitPromised, String foName, String relativeName, String relativeContactNumber, String type) {

        System.out.println("CallDetailsViewModel CallingAgent:"+userId);

        subscribtion = (Disposable) Global.apiService().post_call_details(WebServices.call_details_loan_taken_by_relative + "&dataSetId=" + dataSetId + "&callingAgent=" + MainActivity3API.UserID + "&scheduledDateTime=" + scheduleVisitForCollection_dateTime +
                        "&dateOfVisitPromised=" + dateOfVisitPromised + "&foName=" + foName + "&relativeName=" + relativeName + "&relativeContactNumber=" + relativeContactNumber+"&reason=", detailsOfCustomerActivity.sendCallLogDetailsList_FNV_LTBR("LTBR"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(
                        this::onHomeApiSuccess, this::onApiError
                );

    }

    //8)PAYMENT INFORMATION OF CUSTOMER ACTIVITY - ALREADY PAID (AP)BUTTON CLICK
    public void postScheduledDateTime_AP(String dataSetId, String scheduleVisitForCollection_dateTime, String dateOfVisitPromised, String foName, String relativeName, String relativeContactNumber) {

        System.out.println("CallDetailsViewModel CallingAgent:"+userId);

        subscribtion = (Disposable) Global.apiService().post_call_details(WebServices.call_details_already_paid + "&dataSetId=" + dataSetId + "&callingAgent=" + MainActivity3API.UserID + "&scheduledDateTime=" + scheduleVisitForCollection_dateTime +
                        "&dateOfVisitPromised=" + dateOfVisitPromised + "&foName=" + foName + "&relativeName=" + relativeName + "&relativeContactNumber=" + relativeContactNumber+"&reason=", callDetailsList)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(
                        this::onHomeApiSuccess, this::onApiError
                );

    }

    //9)PAYMENT INFORMATION OF CUSTOMER ACTIVITY - WILL PAY LATER -> WILL PAY LUMPSUMP
    public void postScheduledDateTime_WPLS(String dataSetId, String scheduleVisitForCollection_dateTime, String dateOfVisitPromised, String foName, String relativeName, String relativeContactNumber) {

        System.out.println("CallDetailsViewModel CallingAgent:"+userId);

        subscribtion = (Disposable) Global.apiService().post_call_details(WebServices.call_details_will_pay_lump_sump + "&dataSetId=" + dataSetId + "&callingAgent=" + MainActivity3API.UserID + "&scheduledDateTime=" + scheduleVisitForCollection_dateTime +
                        "&dateOfVisitPromised=" + dateOfVisitPromised + "&foName=" + foName + "&relativeName=" + relativeName + "&relativeContactNumber=" + relativeContactNumber+"&reason=", callDetailsList)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(
                        this::onHomeApiSuccess, this::onApiError
                );

    }

    //10)PAYMENT INFORMATION OF CUSTOMER ACTIVITY -> OTHERS & PAYMENT INFORMATION OF CUSTOMER ACTIVITY -> NOT TAKEN LOAN
    // & PAYMENT NOTIFICATION OF CUSTOMER  -> STTC-OTH
    public void postScheduledDateTime_OTHERS(String apiType,String dataSetId, String scheduleVisitForCollection_dateTime, String dateOfVisitPromised, String foName, String relativeName, String relativeContactNumber,String reason) {

        System.out.println("CallDetailsViewModel CallingAgent:"+userId);

        subscribtion = (Disposable) Global.apiService().post_call_details(apiType + "&dataSetId=" + dataSetId + "&callingAgent=" + MainActivity3API.UserID + "&scheduledDateTime=" + scheduleVisitForCollection_dateTime +
                        "&dateOfVisitPromised=" + dateOfVisitPromised + "&foName=" + foName + "&relativeName=" + relativeName + "&relativeContactNumber=" + relativeContactNumber+"&reason="+reason, callDetailsList)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(
                        this::onHomeApiSuccess, this::onApiError
                );

    }

    //11) NotSpokeToCustomerActivity -Number is Invalid (Note: CallDetails List used of Full /Partial Amt. Paid from DetailsOfCustomerActivity)
    public void postCallDetailsNotSpokeToCustomer_NumberInvalid(String dataSetId){

        System.out.println("CallDetailsViewModel CallingAgent:"+userId);

        subscribtion = (Disposable) Global.apiService().post_call_details(WebServices.notSpokeToCustomer_numberIsInvalid + "&dataSetId=" + dataSetId + "&callingAgent=" + MainActivity3API.UserID + "&scheduledDateTime=" + "" +
                        "&dateOfVisitPromised=" + "&foName=" + "&relativeName=" + "&relativeContactNumber="+"&reason=", callDetailsList_full_partial_amount)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(
                        this::onHomeApiSuccess, this::onApiError
                );

    }

    //12) NotSpokeToCustomerActivity - Number is Busy / Switched Off Without ScheduleDateTime
    public void postCallDetailsNotSpokeToCustomer_NumberIsBusy_SwitchedOff(String apiType,String dataSetId,List<CallDetailsListRoomModel>callDetailsListFromRoomDB){

        System.out.println("CallDetailsViewModel CallingAgent:"+userId);

        subscribtion = (Disposable) Global.apiService().post_call_details_from_roomDB(apiType + "&dataSetId=" + dataSetId + "&callingAgent=" + MainActivity3API.UserID + "&scheduledDateTime=" + "" +
                        "&dateOfVisitPromised=" + "&foName=" + "&relativeName=" + "&relativeContactNumber="+"&reason=", callDetailsListFromRoomDB)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(
                        this::onHomeApiSuccess, this::onApiError
                );

    }

    //NotSpokeToCustomerActivity - Number is Busy / Switched Off With ScheduleDateTime
    public void postCallDetailsNotSpokeToCustomer_NumberIsBusy_SwitchedOff_WithScheduleDateTime(String apiType,String dataSetId,String scheduleVisitForCollection_dateTime,List<CallDetails> callDetailsList){

        System.out.println("CallDetailsViewModel CallingAgent:"+userId);

        subscribtion = (Disposable) Global.apiService().post_call_details(apiType + "&dataSetId=" + dataSetId + "&callingAgent=" + MainActivity3API.UserID + "&scheduledDateTime=" +scheduleVisitForCollection_dateTime +
                        "&dateOfVisitPromised=" + "&foName=" + "&relativeName=" + "&relativeContactNumber="+"&reason=", callDetailsList)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(
                        this::onHomeApiSuccess, this::onApiError
                );

    }


    //13) NotSpokeToCustomerActivity-(PVR)PHYSICAL VISIT REQUIRED
    public void postScheduledDateTime_PVR(String dataSetId, String scheduleVisitForCollection_dateTime, List<CallDetails> callDetailsList) {

        System.out.println("CallDetailsViewModel CallingAgent:"+userId);

        subscribtion = (Disposable) Global.apiService().post_call_details(WebServices.notSpokeToCustomer_physicalVisitRequired + "&dataSetId=" + dataSetId + "&callingAgent=" + MainActivity3API.UserID + "&scheduledDateTime=" + scheduleVisitForCollection_dateTime +
                        "&dateOfVisitPromised=" + "&foName=" + "&relativeName=" + "&relativeContactNumber="+"&reason=", callDetailsList)
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
