package com.text.speech.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class CallHelper {

    public static void call(Context context, String number){
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse(String.format("tel:%s", number)));
        context.startActivity(intent);
    }
}
