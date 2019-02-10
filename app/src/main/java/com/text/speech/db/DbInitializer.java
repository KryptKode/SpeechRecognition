package com.text.speech.db;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Room;

import static com.text.speech.db.AppDatabase.DATABASE_NAME;


public final class DbInitializer {

    private static volatile AppDatabase databaseInstance;

    private DbInitializer() {
        //no instance
    }

    private static AppDatabase getDatabase(Context context) {
        return Room
                .databaseBuilder(context.getApplicationContext(), AppDatabase.class, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build();
    }


    public static void initialize(@NonNull Context context) {
        AppDatabase result = databaseInstance;
        if (result == null) {
            synchronized (AppDatabase.class) {
                result = databaseInstance;
                if (result == null) {
                    databaseInstance = (result = getDatabase(context));
                }
            }
        }


    }

    @NonNull
    public static AppDatabase getDatabase() {
        if (databaseInstance == null) {
            throw new IllegalStateException("Database Not Initialized");
        }
        return databaseInstance;
    }

}
