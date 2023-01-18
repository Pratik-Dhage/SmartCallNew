package com.example.test.api_manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

//WebServices will store Domain(base url) and parameters required
public class WebServices {

    //for News Api
    public static String Domain = "https://newsapi.org/v2/"; // Base Url
    public static String api_key = "&apiKey=0964afd15f5d4897b36e8541e1f9ab7e";
    public static String top_headlines = "top-headlines?";
    public static String country_india = "country=in";

    //for Fake Store Api
    public static String Domain2 = "https://fakestoreapi.com"; //Base Url
      public static  String allProducts = "/products";

      // SMART CALL APIs
/*
    The IP address and port has changed

    For internal (lan access) the IP address is 192.168.1.101 and the port is 8081

    For external (web access) the IP address is 43.239.52.151 and the port is 8081  // Access From Anywhere
    */

   // http://192.168.1.100:8080/lead/findLeads

    // http://192.168.1.101:8081/lead/findLeads

   // http://192.168.1.100:8080/security/generateUser?userId=admin

   // http://192.168.1.100:8080/security/generateOtp?(user object in the request body)

   // http://192.168.1.100:8080/security/validateOtp?(user object in the request body)

    //Smart Call BaseURL
    public static String SmartCall_BaseURL = "http://192.168.1.101:8081/";

    public static String SmartCall_BaseURL2 = "http://43.239.52.151:8081/";


    //for Lead List
    public static String find_Leads = "lead/findLeads";

    // for Generating User
    public static String generate_User = "security/generateUser?userId=admin";


    public static RestClient create() {
        OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder();

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        okHttpBuilder.connectTimeout(2, TimeUnit.MINUTES);
        okHttpBuilder.readTimeout(2, TimeUnit.MINUTES).build();
        okHttpBuilder.writeTimeout(2, TimeUnit.MINUTES);
        okHttpBuilder.addInterceptor(logging);
        OkHttpClient okHttpClient = okHttpBuilder.build();

        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                //.baseUrl(WebServices.Domain2)
                //.baseUrl(WebServices.Domain)
                .baseUrl(SmartCall_BaseURL2)
                .client(okHttpClient)
                .build();

        return retrofit.create(RestClient.class);
    }


}
