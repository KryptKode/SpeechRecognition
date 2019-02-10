package com.text.speech.utils;

import android.telephony.SmsManager;
import android.util.Log;

public class SmsUtils {
    private static final String TAG = "SmsUtils";
    public static void sendSMS(String phoneNo, String msg) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, msg, null, null);
        } catch (Exception ex) {
            Log.e(TAG, "sendSMS: ",ex);
        }
    }
}
