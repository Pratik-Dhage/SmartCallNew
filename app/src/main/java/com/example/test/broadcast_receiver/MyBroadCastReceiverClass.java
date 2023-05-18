package com.example.test.broadcast_receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.test.call_status.CallStatusActivity;
import com.example.test.npa_flow.details_of_customer.DetailsOfCustomerActivity;

import java.io.IOException;

public class MyBroadCastReceiverClass extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        System.out.println("Here in BroadcastReceiver Class");
        CallStatusActivity callStatusActivity = new CallStatusActivity();
        try {
            callStatusActivity.getCallRecordingAndCallLogs();
        } catch (Exception e) {
            e.printStackTrace();
        }

        DetailsOfCustomerActivity detailsOfCustomerActivity = new DetailsOfCustomerActivity();
        try{
         detailsOfCustomerActivity.getCallRecordingAndCallLogs();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
