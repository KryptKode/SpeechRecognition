package com.text.speech;

import android.app.Application;

import com.text.speech.contacts.data.Contacts;

public class SpeechApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Contacts.initialize(this);
    }
}
