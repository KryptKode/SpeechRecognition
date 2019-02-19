package com.text.speech.ui;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.disposables.CompositeDisposable;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.text.speech.R;
import com.text.speech.contacts.ContactManager;
import com.text.speech.contacts.data.Contact;
import com.text.speech.ui.base.BaseActivity;
import com.text.speech.ui.dialogs.InfoConfirmDialog;
import com.text.speech.utils.NotificationUtils;
import com.text.speech.utils.SmsUtils;

import java.util.ArrayList;
import java.util.List;

@RuntimePermissions
public class SmsActivity extends BaseActivity {
    private static final String TAG = "SmsActivity";
    private Button btn2;

    private ContactManager contactManager = new ContactManager();
    private CompositeDisposable disposable = new CompositeDisposable();
    private List<Contact> contactList = new ArrayList<>();
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);
        btn2 = (Button) findViewById(R.id.button2);
        progressBar = findViewById(R.id.progress_bar);

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(contactList.size() > 0){
                    SmsActivityPermissionsDispatcher
                            .sendSmsToPersonWithPermissionCheck(SmsActivity.this,
                                    contactList.get(0).getPhoneNumbers().get(0).getNumber(),
                                    "Hello world");
                }else {
                    NotificationUtils.notifyUser(SmsActivity.this, "No number selected");
                }
            }
        });


        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        SmsActivityPermissionsDispatcher.getContactsWithPermissionCheck(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @NeedsPermission({Manifest.permission.READ_CONTACTS, Manifest.permission.SEND_SMS})
    void getContacts() {
        disposable.add(contactManager.getAllContacts()
                .subscribe(contacts -> {
                    contactList.addAll(contacts);
                    Log.d(TAG, "getContacts: Contacts read=" + contacts.size());
                }, throwable -> {
                    Log.e(TAG, "getContacts: ", throwable);
                    NotificationUtils.notifyUser(this, getString(R.string.error_occurred));
                }));
    }


    /**
     * Creates intent to sms the phone number passed as a parameter
     *
     * @param number the phone number to sms
     */
    @NeedsPermission({Manifest.permission.READ_CONTACTS, Manifest.permission.SEND_SMS})
    public void sendSmsToPerson(String number, String message) {
        try {
            SmsUtils.sendSMS(number, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnShowRationale({Manifest.permission.READ_CONTACTS, Manifest.permission.SEND_SMS})
    void showCallRationale(PermissionRequest request) {
        InfoConfirmDialog.showDialog(this, R.string.allow_permission, R.string.allow_call_permission_msg, R.drawable.ic_sms_white, request::proceed);
    }

    @OnPermissionDenied({Manifest.permission.READ_CONTACTS, Manifest.permission.SEND_SMS})
    void showDeniedForCall() {
        NotificationUtils.notifyUser(this, getString(R.string.sms_permission_denied));
    }

    @OnNeverAskAgain({Manifest.permission.READ_CONTACTS, Manifest.permission.SEND_SMS})
    void neverAskForCall() {
        NotificationUtils.notifyUser(this, getString(R.string.sms_permission_never_ask));
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        SmsActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);

    }

    @Override
    protected void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    protected void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }
}
