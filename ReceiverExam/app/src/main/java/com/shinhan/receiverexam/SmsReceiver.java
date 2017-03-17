package com.shinhan.receiverexam;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.ObbInfo;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class SmsReceiver extends BroadcastReceiver {

    public static final String TAG = "SmsReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "onReceive()---------------------------!!!!");

        Bundle bundle = intent.getExtras();
        SmsMessage[] messages = parseSmsMessage(bundle);
        if (messages != null && messages.length > 0) {      //수신된 메시지가 있으면
            String sender = messages[0].getOriginatingAddress();
            String contents = messages[0].getDisplayMessageBody().toString();
            Log.i(TAG, "sender:" + sender + ", contents:" + contents);
        }

        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        //throw new UnsupportedOperationException("Not yet implemented");
    }

    private SmsMessage[] parseSmsMessage(Bundle bundle) {
        Object[] objects = (Object[])bundle.get("pdus");
        SmsMessage[] messages1 = new SmsMessage[objects.length];

        for (int i = 0 ; i < objects.length ; i++) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                String format = bundle.getString("format");
                messages1[i] = SmsMessage.createFromPdu( (byte[]) objects[i], format);
            } else {
                messages1[i] = SmsMessage.createFromPdu( (byte[]) objects[i]);
            }
        }

        return messages1;
    }
}
