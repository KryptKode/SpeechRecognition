package com.text.speech;

import com.text.speech.contacts.data.Contacts;

import androidx.multidex.MultiDexApplication;

public class SpeechApp extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        Contacts.initialize(this);
    }
}
