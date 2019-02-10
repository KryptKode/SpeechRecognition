package com.text.speech.data;

import android.content.Context;

import com.text.speech.utils.SharedPrefsHelper;

public class Repository {
    private SharedPrefsHelper prefsHelper;

    public Repository(Context context) {
        prefsHelper = new SharedPrefsHelper(context);
    }

    public boolean isSetUpComplete(){
        return prefsHelper.setUpComplete();
    }

    public void updateSetUp(boolean value){
        prefsHelper.updateSetUp(value);
    }


}
