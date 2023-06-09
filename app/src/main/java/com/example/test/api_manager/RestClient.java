package com.example.test.api_manager;


import com.example.test.lead.model.LeadModel;
import com.example.test.login.model.LoginResponseModel;
import com.example.test.main_dashboard.model.DashBoardResponseModel;
import com.example.test.main_dashboard.model.DashBoardScheduleForTheDayModel;
import com.example.test.npa_flow.call_details.CallDetails;
import com.example.test.npa_flow.details_of_customer.DetailsOfCustomerResponseModel;
import com.example.test.npa_flow.dpd.DPD_ResponseModel;
import com.example.test.npa_flow.loan_collection.LoanCollectionListResponseModel;
import com.example.test.npa_flow.status_of_customer.model.Activity;
import com.example.test.otp.model.OTPGenerateOTPResponseModel;
import com.example.test.otp.model.OTPValidateOTPResponseModel;
import com.example.test.schedule_flow.calls_for_the_day.model.CallsForTheDayResponseModel;
import com.example.test.schedule_flow.model.ScheduleVisitDetails;
import com.example.test.schedule_flow.visits_for_the_day.model.VisitsForTheDayResponseModel;
import com.example.test.user.UserModel;

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
            @Body UserModel userModel
    );

    //Forgot Password / Reset Pasword
    @POST("security/resetPassword")
    Observable<LoginResponseModel> resetPasswordUserApi(
            @Body UserModel userModel
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
    Observable<OTPValidateOTPResponseModel> otpValidateApi(
            @Body UserModel userModel
    );

    //DashBoard
    //Only @POST request can have request @BODY
    @POST("dashboard/getDashBoardForUser?")
//@POST
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
    //for Call Details
    @POST
    Observable<String> post_call_details(@Url String url, @Body List<CallDetails> callDetails);


    //DPD Queue
    @GET
    Observable<List<DPD_ResponseModel>> getDPD_QueueList(@Url String url);

    //LoanCollectionList
    @GET
    Observable<List<LoanCollectionListResponseModel>> getLoanCollectionList(@Url String url);

    //Details of Customer
    @GET
    Observable<List<DetailsOfCustomerResponseModel>> getDetailsOfCustomerList(@Url String url);

    //Schedule Details (Calendar icon in Dashboard)
    @GET
    Observable<List<ScheduleVisitDetails>> getScheduleDetails(@Url String url);

}
