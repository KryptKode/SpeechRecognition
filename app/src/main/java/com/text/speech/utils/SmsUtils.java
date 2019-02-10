package com.text.speech.utils;

import android.telephony.SmsManager;
import android.util.Log;

public class SmsUtils {
    private static final String TAG = "SmsUtils";

    public static void sendSMS(String phoneNo, String msg) throws Exception {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNo, null, msg, null, null);

    }
}
