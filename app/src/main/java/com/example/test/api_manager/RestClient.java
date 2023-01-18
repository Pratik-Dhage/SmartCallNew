package com.example.test.api_manager;


import com.example.test.lead.model.LeadListResponseModel;
import com.example.test.lead.model.LeadModel;
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
import retrofit2.http.GET;
import retrofit2.http.Url;

//Rest Interface will have GET(),POST(),PUT() etc. api methods
public interface RestClient {

  /*  @GET//("/products")
    Observable<HomeResponse> getAllProducts(@Url String url);

    @GET
    Observable<NewsResponse> getNews(@Url String url);
*/

   /* @GET
    Observable<LeadListResponseModel> getLeadList(@Url String url);*/
    @GET
    Observable<List<LeadModel>> getLeadList(@Url String url);

}
