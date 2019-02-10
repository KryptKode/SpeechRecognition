package com.text.speech.contacts;



import com.text.speech.contacts.data.Contact;
import com.text.speech.contacts.data.Contacts;
import com.text.speech.contacts.data.Query;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class ContactManager {

    public Observable<List<Contact>> getAllContacts(){
        return Observable.fromCallable(this::getContacts).subscribeOn(Schedulers.io());
    }

    private List<Contact> getContacts() {
        Query q = Contacts.getQuery();
        q.include(Contact.Field.ContactId, Contact.Field.DisplayName,
                Contact.Field.GivenName, Contact.Field.FamilyName,
                Contact.Field.PhoneNumber, Contact.Field.PhoneType,
                Contact.Field.PhoneLabel, Contact.Field.PhoneNormalizedNumber,
                Contact.Field.Email, Contact.Field.EmailLabel, Contact.Field.EmailType,
                Contact.Field.PhotoUri, Contact.Field.EventStartDate, Contact.Field.EventType,
                Contact.Field.EventLabel, Contact.Field.CompanyName, Contact.Field.CompanyTitle,
                Contact.Field.Website, Contact.Field.Note, Contact.Field.Address, Contact.Field.AddressType,
                Contact.Field.AddressType, Contact.Field.AddressStreet, Contact.Field.AddressCity,
                Contact.Field.AddressRegion, Contact.Field.AddressPostcode, Contact.Field.AddressCountry,
                Contact.Field.AddressLabel);
        return q.find();
    }

}
