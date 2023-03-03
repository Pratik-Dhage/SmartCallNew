package com.example.test.broadcast_receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.test.call_status.CallStatusActivity;

import java.io.IOException;

public class MyBroadCastReceiverClass extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        CallStatusActivity callStatusActivity = new CallStatusActivity();
        try {
            callStatusActivity.getCallRecordingAndCallLogs();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
