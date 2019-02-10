package com.text.speech.contacts.data;

import android.content.Context;

public final class Contacts {
    private static Context context;

    private Contacts() {}

    /**
     * Initialize the Contacts library
     *
     * @param context context
     */
    public static void initialize(Context context) {
        Contacts.context = context.getApplicationContext();
    }

    /**
     * Get a new Query object to find contacts.
     *
     * @return  A new Query object.
     */
    public static Query getQuery() {
        if (Contacts.context == null) {
            throw new IllegalStateException("Contacts library not initialized");
        }

        return new Query(context);
    }
}
