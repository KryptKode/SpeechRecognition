package com.text.speech.ui;

import android.Manifest;

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
import com.text.speech.utils.PhoneCallUtils;

import java.util.ArrayList;
import java.util.List;

@RuntimePermissions
public class CallActivity extends BaseActivity {
    private static final String TAG = "CallActivity";
    private Button btn1;
    private ContactManager contactManager = new ContactManager();
    private CompositeDisposable disposable = new CompositeDisposable();
    private List<Contact> contactList = new ArrayList<>();
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);
        btn1 = (Button) findViewById(R.id.button1);
        progressBar = findViewById(R.id.progress_bar);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(contactList.size() > 0){
                   CallActivityPermissionsDispatcher.callPersonWithPermissionCheck(CallActivity.this,
                           contactList.get(0).getPhoneNumbers().get(0).getNumber());
               }else{
                   NotificationUtils.notifyUser(CallActivity.this, "No number selected");
               }
            }
        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        CallActivityPermissionsDispatcher.getContactsWithPermissionCheck(this);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @NeedsPermission({Manifest.permission.READ_CONTACTS, Manifest.permission.CALL_PHONE})
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
     * Creates intent to call the phone number passed as a parameter
     *
     * @param number the phone number to call
     */
    @NeedsPermission({Manifest.permission.READ_CONTACTS, Manifest.permission.CALL_PHONE})
    public void callPerson(String number) {
        PhoneCallUtils.callPhone(this, number);
    }

    @OnShowRationale({Manifest.permission.READ_CONTACTS, Manifest.permission.CALL_PHONE})
    void showCallRationale(PermissionRequest request) {
        InfoConfirmDialog.showDialog(this, R.string.allow_permission, R.string.allow_call_permission_msg, R.drawable.ic_call_white, request::proceed);
    }

    @OnPermissionDenied({Manifest.permission.READ_CONTACTS, Manifest.permission.CALL_PHONE})
    void showDeniedForCall() {
        NotificationUtils.notifyUser(this, getString(R.string.call_permission_denied));
    }

    @OnNeverAskAgain({Manifest.permission.READ_CONTACTS, Manifest.permission.CALL_PHONE})
    void neverAskForCall() {
        NotificationUtils.notifyUser(this, getString(R.string.call_permission_never_ask));
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        CallActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
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





