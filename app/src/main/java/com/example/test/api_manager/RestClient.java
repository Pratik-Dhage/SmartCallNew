package com.example.test.api_manager;


import com.example.test.lead.model.LeadListResponseModel;
import com.example.test.lead.model.LeadModel;
import com.example.test.login.model.LoginResponseModel;
import com.example.test.main_dashboard.model.DashBoardResponseModel;
import com.example.test.npa_flow.dpd.DPD_ResponseModel;
import com.example.test.user.UserModel;

import java.sql.Time;
import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

//Rest Interface will have GET(),POST(),PUT() etc. request methods
public interface RestClient {



    @GET
    Observable<List<LeadModel>> getLeadList(@Url String url);

    @POST("security/authenticateUser")
    Observable<LoginResponseModel> loginUserApi(
            @Body UserModel userModel
    );


    //Only @POST request can have request @BODY
 @POST("dashboard/getDashBoardForUser?")//@POST
 Observable<List<DashBoardResponseModel>> getDashBoardData(
         @Body UserModel userModel
 );

 @GET
    Observable<List<DPD_ResponseModel>> getDPD_QueueList(@Url String url);

}
