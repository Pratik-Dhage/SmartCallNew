package com.example.test.api_manager;


import com.example.test.lead.model.LeadListResponseModel;
import com.example.test.lead.model.LeadModel;
import com.example.test.login.model.LoginResponseModel;
import com.example.test.main_dashboard.model.DashBoardResponseModel;
import com.example.test.user.UserModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.Time;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

//Rest Interface will have GET(),POST(),PUT() etc. request methods
public interface RestClient {



   /* @GET
    Observable<LeadListResponseModel> getLeadList(@Url String url);*/
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

}
