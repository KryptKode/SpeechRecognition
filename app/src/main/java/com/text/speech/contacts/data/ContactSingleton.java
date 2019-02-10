package com.text.speech.contacts.data;

public class ContactSingleton {
    private static final ContactSingleton ourInstance = new ContactSingleton();
    private Contact contact;

   public static ContactSingleton getInstance() {
        return ourInstance;
    }

    private ContactSingleton() {
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public Contact getContact() {
        return contact;
    }
}
