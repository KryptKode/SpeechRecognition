package com.text.speech.data;

import android.content.Context;

import com.text.speech.utils.SharedPrefsHelper;

public class AppRepository implements Repository{
    private SharedPrefsHelper prefsHelper;

    public AppRepository(Context context) {
        prefsHelper = new SharedPrefsHelper(context);
    }

    @Override
    public boolean isSetUpComplete(){
        return prefsHelper.setUpComplete();
    }

    @Override
    public void updateSetUp(boolean value){
        prefsHelper.updateSetUp(value);
    }


}
