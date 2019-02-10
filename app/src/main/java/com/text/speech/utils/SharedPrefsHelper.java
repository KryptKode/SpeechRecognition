package com.text.speech.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefsHelper {

    public static final String PREFS = "prefs";
    public static final String SETUP_COMPLETE = "set_up";
    private Context context;

    public SharedPrefsHelper(Context context) {
        this.context = context;
    }

    public SharedPreferences getPrefs(){
        return context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    public boolean setUpComplete(){
        return getPrefs().getBoolean(SETUP_COMPLETE, false);
    }

    public void updateSetUp(boolean value){
        getPrefs().edit().putBoolean(SETUP_COMPLETE, value).apply();
    }
}
