package com.example.test.api_manager;


import com.example.test.lead.model.LeadModel;
import com.example.test.login.model.LoginResponseModel;
import com.example.test.main_dashboard.model.DashBoardResponseModel;
import com.example.test.main_dashboard.model.DashBoardScheduleForTheDayModel;
import com.example.test.notes_history.NotesHistoryResponseModel;
import com.example.test.npa_flow.call_details.CallDetails;
import com.example.test.npa_flow.details_of_customer.DetailsOfCustomerResponseModel;
import com.example.test.npa_flow.dpd.DPD_ResponseModel;
import com.example.test.npa_flow.loan_collection.LoanCollectionListResponseModel;
//import com.example.test.npa_flow.save_location.SaveLocationOfCustomerModel;
import com.example.test.npa_flow.radio_buttons.RadioButtonReasons;
import com.example.test.npa_flow.status_of_customer.model.Activity;
import com.example.test.otp.model.OTPGenerateOTPResponseModel;
import com.example.test.otp.model.OTPValidateOTPResponseModel;
import com.example.test.roomDB.model.CallDetailsListRoomModel;
import com.example.test.schedule_flow.calls_for_the_day.model.CallsForTheDayResponseModel;
import com.example.test.schedule_flow.model.ScheduleVisitDetails;
import com.example.test.schedule_flow.visits_for_the_day.model.VisitsForTheDayResponseModel;
import com.example.test.user.UserModel;
import com.example.test.user.UserResponseModel;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

//Rest Interface will have GET(),POST(),PUT() etc. request methods
public interface RestClient {


    //LeadList
    @GET
    Observable<List<LeadModel>> getLeadList(@Url String url);

    //Login User
    @POST("security/authenticateUser")
    Observable<LoginResponseModel> loginUserApi(
            @Body LoginResponseModel loginResponseModel
    );


    //Register Password
    @POST("security/authenticateUser")
    Observable<UserResponseModel> registerPasswordUserApi(
            @Body UserResponseModel userResponseModel
    );

    //Forgot Password / Reset Password
    @POST("security/resetPassword")
    Observable<UserResponseModel> resetPasswordUserApi(
            @Body UserResponseModel userResponseModel
    );

    //Authenticate User(Login User)
    @POST
    Observable<UserModel> authenticateUser( @Url String url, @Body UserModel userModel);

    //Generate OTP
    @POST("security/generateOtp")
     Observable<OTPGenerateOTPResponseModel> otpGenerateApi(
             @Body UserModel userModel
    );

    //Validate OTP
    @POST("security/validateOtp")
   /* Observable<OTPValidateOTPResponseModel> otpValidateApi(
            @Body UserModel userModel
    );*/
    Observable<String> otpValidateApi(
            @Body UserModel userModel
    );

    //DashBoard
    //Only @POST request can have request @BODY
    @POST("dashboard/getDashBoardForUser?")
    //@POST("dashboardapi/postDashBoardForUser?")
    Observable<List<DashBoardResponseModel>> getDashBoardData(
            @Body UserModel userModel
    );

    //DashBoard(Schedule For The Day)
    @GET
    Observable<List<DashBoardScheduleForTheDayModel>> getDashBoardDataScheduleForTheDay(@Url String url);

    //DashBoard(Visits For The Day)
    @GET
    Observable<List<VisitsForTheDayResponseModel>> getVisitsForTheDay(@Url String url);

    //DashBoard(Calls For The Day)
    @GET
    Observable<List<CallsForTheDayResponseModel>> getCallsForTheDay(@Url String url);

    //Status Of Customer in Loan Collection List(Loan Collection Adapter)
    @GET
    Observable<List<Activity>> getStatusDetailsOfCustomer(@Url String url);

    //for Call Details
 /*@POST
 Observable<List<CallDetails>> post_call_details(@Url String url,
   @Body List<CallDetails> callDetails
 );*/
    //for Call Details (CALLS FOR THE DAY FLOW & NPA FLOW)
    @POST
    Observable<String> post_call_details(@Url String url, @Body List<CallDetails> callDetails);

    //for Call Details From ROOM DB for NotSpokeToCustomer - NumberIsBusy/SwitchedOff buttons  (CALLS FOR THE DAY FLOW & NPA FLOW)
    //At 5th attempt  navigate to ScheduleVisitForCollectionActivity(Means 4 attempts are completed with CallLogDetailsFromRoomDB sent to Backend)
    @POST
    Observable<String> post_call_details_from_roomDB(@Url String url, @Body List<CallDetailsListRoomModel> callDetailsFromRoomDB);

    //FOR VISITS FOR  THE DAY (NOT READY TO PAY FLOWS)
    @POST
    Observable<String> post_visits_call_details(@Url String url);

    //DPD Queue
    @GET
    Observable<List<DPD_ResponseModel>> getDPD_QueueList(@Url String url);

    //LoanCollectionList
    @GET
    Observable<List<LoanCollectionListResponseModel>> getLoanCollectionList(@Url String url);

    //NearByCustomerList , using LoanCollectionResponseModel
    @GET
    Observable<List<LoanCollectionListResponseModel>> getNearByCustomersList(@Url String url);

    //Details of Customer
    @GET
    Observable<List<DetailsOfCustomerResponseModel>> getDetailsOfCustomerList(@Url String url);

    //Schedule Details (Calendar icon in Dashboard)
    @GET
    Observable<List<ScheduleVisitDetails>> getScheduleDetails(@Url String url);

    //Notes-History
    @GET
    Observable<List<NotesHistoryResponseModel>> getNotesHistory(@Url String url);

    //SavedLocationOfCustomer(Passing dataSetId, Latitude ,Longitude , Distance)
    @GET
    //Observable<SaveLocationOfCustomerModel> getSavedLocationOfCustomer(@Url String url);
    Observable<String> getSavedLocationOfCustomer(@Url String url);

    //Radio Buttons Reason
    @GET
    Observable<List<RadioButtonReasons>> getRadioButtonReasons(@Url String url);

    //Save Alternate Number
    @GET
    Observable<String> saveAlternateNumber(@Url String url);

    //Get Distance
    @GET
    Observable<Double> getDistanceUsingApi(@Url String url);

    //Get ReceiptNumber
    @GET
    Observable<Long> getReceiptNumberUsingDataSetId(@Url String url);
}
