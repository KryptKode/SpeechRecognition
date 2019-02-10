package com.text.speech;

import com.text.speech.contacts.data.Contacts;
import com.text.speech.db.DbInitializer;

import androidx.multidex.MultiDexApplication;

public class SpeechApp extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        Contacts.initialize(this);
        DbInitializer.initialize(this);
    }
}
