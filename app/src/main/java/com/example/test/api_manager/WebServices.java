package com.example.test.api_manager;

import com.example.test.lead.model.LeadModel;
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
    public static String SmartCall_BaseURL = "https://192.168.1.101:8433/";

    public static String SmartCall_BaseURL2 = "http://43.239.52.151:8081/";

    public static String SmartCall_BaseURL3 = "http://192.168.1.100:8081/";

    //for Login Authentication
    public static String Login_Authentication = "security/authenticateUser";
    //you have to pass the User object as request body to the service
    //the return type is also User object

    //for Dashboard Data
    public static String Dashboard_Data = "dashboard/getDashBoardForUser?";
    //you have to send User as request body
    //the api will return DashBoard object (attached)

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
        //okHttpBuilder.addInterceptor(new AuthInterceptor("your-auth-token")) // to authenticate credential before calling the api
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
