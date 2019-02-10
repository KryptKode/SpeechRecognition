package com.text.speech.utils;

import android.content.Context;
import android.widget.Toast;

public class NotificationUtils {

    public static void notifyUser(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

}
