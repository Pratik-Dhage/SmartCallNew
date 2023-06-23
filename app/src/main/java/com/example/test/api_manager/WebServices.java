package com.example.test.api_manager;

import android.content.Context;

import androidx.core.app.ComponentActivity;

import com.example.test.helper_classes.Global;
import com.example.test.lead.model.LeadModel;
import com.example.test.login.LoginActivity;
import com.example.test.main_dashboard.MainActivity3API;
import com.example.test.schedule_flow.ScheduleDetailsActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

//WebServices will store Domain(base url) and parameters required
public  class WebServices {

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

    //http://192.168.1.101:8081/transactionDataSet/getDpdQueues?branchCode=001  (DPD Queue - it will return a list of object of type DpdQueue in jSon format.)

    //Smart Call BaseURL
    public static String SmartCall_BaseURL = "https://192.168.1.101:8433/";

    public static String SmartCall_BaseURL2 = "http://43.239.52.151:8081/";

    public static String SmartCall_BaseURL3 = "http://192.168.1.100:8081/";

    public static String SmartCall_BaseURL4 = "http://45.117.0.12:8081/";

    //for Login Authentication
    public static String Login_Authentication = "security/authenticateUser";
    //you have to pass the User object as request body to the service
    //the return type is also User object

    //for Reset Password
    // 1.     It is a post method
   // 2.     You need to set the userId and password in the User object and set it as the request body for the method.
   // 3.     The method will return user object. In case of failure it will return null
    public static String reset_password = "security/resetPassword";


    //for Dashboard Data
    public static String Dashboard_Data = "dashboard/getDashBoardForUser?";
    //you have to send User as request body
    //the api will return DashBoard object (attached)

    //for Lead List
    public static String find_Leads = "lead/findLeads";

    // for Generating User
    public static String generate_User = "security/generateUser?userId=admin";

    //Authenticate User
    public static String authenticate_user = "security/authenticateUser";

    //for Generate OTP
    public static String generate_OTP = "security/generateOtp";

    //for Validate OTP
    public static String validate_OTP = "security/validateOtp";

    //for Notes-History
    //http://43.239.52.151:8081/activity/getNotesHistory?dataSetId=311180
    public static String notes_History = "activity/getNotesHistory?";

    //for Details of Customer
    //http://43.239.52.151:8081/transactionDataSet/getDetailView?dataSetId=311964&queue=3738
    public static String detail_of_customer = "transactionDataSet/getDetailView?dataSetId=311964&queue=3738";

    public static String detail_of_customer_common = "transactionDataSet/getDetailView?";


    //for DPD queue

    public static String UserID = MainActivity3API.UserID;
    public static String BranchCode = MainActivity3API.BranchCode;

   public static String userId = "CA_01_001";
    static String userBranchCode = "001";
   public static String newBranchCode = "00048"; // changed on 18/05/2023 (used in DPDQueue,LoanCollection)

    //  public static String dpd_queue = "transactionDataSet/getDpdQueues?branchCode=001";
//    public static String dpd_queue = "transactionDataSet/getDpdQueues?userId="+UserID+"&branchCode=" + BranchCode; //Changes 28/4/2023 use userId along with branchCode
    public static String dpd_queue = "transactionDataSet/getDpdQueues?";
    //for Loan Collection List that comes after DPD Activity
    //http://43.239.52.151:8081/transactionDataSet/getDpdQueueList?branchCode=001&queue=3738 //for 30 Days
    //http://43.239.52.151:8081/transactionDataSet/getDpdQueueList?branchCode=001&queue=3739 //for 60 Days
    //http://43.239.52.151:8081/transactionDataSet/getDpdQueueList?branchCode=001&queue=3740 //for 90 Days

    //  public static String loan_collection_list = dpd_queue+"&queue="+queue_30_days;
    public static String loan_collection_list_30Days = "transactionDataSet/getDpdQueueList?";
    public static String loan_collection_list_60Days = "transactionDataSet/getDpdQueueList?";
    public static String loan_collection_list_90Days = "transactionDataSet/getDpdQueueList?";
    public static String loan_collection_list_above90Days = "transactionDataSet/getDpdQueueList?";

    //for Call Details (Use Post Method)

    //Below is the API for call submit. It is a post method.
    //  http://43.239.52.151:8081/activity/submitcall?flow=<call flow as discussed>&dataSetId=<the records dataSetId>&callingAgent=<userId>
    // in addition to this you need to send the call details in the request body

    //FOR FULL AMOUNT PAID
    public static String call_details_complete_no_change_full_amt_paid = "activity/submitcall?flow=STTC-RTP-SLFOP-FAP-UR-P-CNC";
    public static String call_details_complete_need_to_update_details_full_amt_paid = "activity/submitcall?flow=STTC-RTP-SLFOP-FAP-UR-P-CNTUD";
    public static String call_details_complete_escalate_to_bm_full_amt_paid = "activity/submitcall?flow=STTC-RTP-SLFOP-FAP-UR-P-CETBM";

    //FOR PARTIAL AMOUNT PAID
    public static String call_details_complete_no_change_partial_amt_paid = "activity/submitcall?flow=STTC-RTP-SLFOP-PAP-UR-P-CNC";
    public static String call_details_complete_need_to_update_details_partial_amt_paid = "activity/submitcall?flow=STTC-RTP-SLFOP-PAP-UR-P-CNTUD";
    public static String call_details_complete_escalate_to_bm_partial_amt_paid = "activity/submitcall?flow=STTC-RTP-SLFOP-PAP-UR-P-CETBM";

    //FOR WILL PAY LATER
    public static String call_details_will_pay_later = "activity/submitcall?flow=STTC-RTP-SLFOP-WPL";

    //FOR SEND VISIT FOR COLLECTION
    public static String call_details_send_visit_for_collection = "activity/submitcall?flow=STTC-RTP-SVFC";

    //FOR ASKED TO CALL BACK LATER(PAYMENT NOTIFICATION OF CUSTOMER ACTIVITY)
    public static String call_details_asked_to_call_later ="activity/submitcall?flow=STTC-ATCL";

    //FOR FO NOT VISITED (PAYMENT INFORMATION OF CUSTOMER ACTIVITY (NOT READY TO PAY))
    public static String call_details_fo_not_visited ="activity/submitcall?flow=STTC-NRTP-FNV";

    //FOR LOAN TAKEN BY RELATIVE (PAYMENT INFORMATION OF CUSTOMER ACTIVITY (NOT READY TO PAY))
    public static String call_details_loan_taken_by_relative ="activity/submitcall?flow=STTC-NRTP-LTBR";

    //FOR ALREADY PAID (PAYMENT INFORMATION OF CUSTOMER ACTIVITY (NOT READY TO PAY))
    public static String call_details_already_paid ="activity/submitcall?flow=STTC-NRTP-AP";

  //FOR WILL PAY LUMPSUMP(PAYMENT INFORMATION OF CUSTOMER ACTIVITY - WILL PAY LATER -> WILL PAY LUMPSUMP)
  public static String call_details_will_pay_lump_sump ="activity/submitcall?flow=STTC-NRTP-WPL-WPLS";

    //FOR WILL PAY LATER - UPDATE(PAYMENT INFORMATION OF CUSTOMER ACTIVITY - WILL PAY LATER -> UPDATE)
  public static String call_details_will_pay_later_update = "activity/submitcall?flow=STTC-NRTP-WPL-UPDATE";

  //FOR OTHERS (PAYMENT INFORMATION OF CUSTOMER ACTIVITY - OTHERS)
    public static String call_details_payment_info_others = "activity/submitcall?flow=STTC-NRTP-OTHERS";


    //Visit For The Day - Visit-NPA Notification (Visits For The Day Not Ready To Pay Flow)
    public static String visit_not_ready_to_pay_lack_of_funds ="activity/submitcall?flow=NRTP-LOF";
    public static String visit_not_ready_to_pay_claims_payment_made ="activity/submitcall?flow=NRTP-CPM";
    public static String visit_not_ready_to_pay_not_taken_loan ="activity/submitcall?flow=NRTP-NTL";
    public static String visit_not_ready_to_pay_loan_taken_by_relative ="activity/submitcall?flow=NRTP-LTBR";
    public static String visit_not_ready_to_pay_will_pay_lumpsump ="activity/submitcall?flow=NRTP-WPLS";

    //Visit For The Day - Visit-NPA Payment Mode ((Visits For The Day Ready To Pay Flow))
    public static String visit_ready_to_pay_send_link_for_online_payment ="activity/submitcall?flow=RTP-SLFOP";
    public static String visit_ready_to_pay_cash_payment ="activity/submitcallCheque?flow=RTP-CAP";
    public static String visit_ready_to_pay_cheque_payment = "activity/submitcallCheque?flow=RTP-CHP";

    //FOR SCHEDULE DETAILS (Calender Icon In DashBoard)
    public static String schedule_details_in_dashboard = "activity/scheduledVisits?userId=";

    //FOR SCHEDULE FOR THE DAY In DASHBOARD
    public static String schedule_for_the_day="activity/scheduledForTheDay?";

    //FOR Visits for the Day (In DashBoard, on clicking right arrow)
    //http://43.239.52.151:8081/transactionDataSet/getVisitLists?userId=CA_01_001&branchCode=00048
    public static String visits_for_the_day = "transactionDataSet/getVisitLists?";

    //FOR Calls for the Day (In DashBoard, on clicking right arrow)
    //http://43.239.52.151:8081/transactionDataSet/getCallLists?userId=CA_01_001&branchCode=00048
    public static String calls_for_the_day = "transactionDataSet/getCallLists?";

    //FOR STATUS OF CUSTOMER(LOAN COLLECTION ADAPTER in Loan Collection List)
    public static String details_of_status_of_customer = "activity/activitiesForDataSet?";


    // For Saving LATITUDE , LONGITUDE , Distance of Member/Customer on Capture Button click in DetailsOfCustomerAdapter
    //http://43.239.52.151:8081/transactionDataSet/updateLocation?dataSetId=...&lat=....&lon=...&dist=...
    public static String save_location_of_customer = "transactionDataSet/updateLocation?";

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

        //Gson gson = new GsonBuilder().setLenient().registerTypeAdapter(Type.class,new TypeAdapter()).create();
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
