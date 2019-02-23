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
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.text.speech.R;
import com.text.speech.contacts.ContactManager;
import com.text.speech.contacts.data.Contact;
import com.text.speech.contacts.data.PhoneNumber;
import com.text.speech.media.Player;
import com.text.speech.ui.base.BaseActivity;
import com.text.speech.ui.dialogs.InfoConfirmDialog;
import com.text.speech.utils.NotificationUtils;
import com.text.speech.utils.PhoneCallUtils;
import com.text.speech.utils.WordUtils;

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

                if (contactList.size() > 0) {
                    for (Contact contact : contactList) {
                        if (contact != null && !TextUtils.isEmpty(contact.getDisplayName())) {

                            if (contact.getDisplayName().contains(WordUtils.TWO) || contact.getDisplayName().contains(WordUtils.HELLO)
                                    || contact.getDisplayName().contains(WordUtils.ONE) || contact.getDisplayName().contains(WordUtils.STOP)
                                    || contact.getDisplayName().contains(WordUtils.THANK_YOU)) {
                                CallActivityPermissionsDispatcher.callPersonWithPermissionCheck(CallActivity.this,
                                        contact.getPhoneNumbers().get(0).getNumber());
                                break;
                            }
                        }
                    }

                } else {
                    NotificationUtils.notifyUser(CallActivity.this, "No number selected");
                }
            }
        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }



    }

    @Override
    protected void onResume() {
        super.onResume();
        playSound(Player.WHO_YOU_WANT_TO_CALL);
        getPlayer().setListener(new Player.PlayerListener() {
            @Override
            public void onPlayEnd() {
                getPlayer().setListener(null);
                initRecognizerWithPermissionCheck();
            }
        });

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
    protected void handleResult(String hypothesis) {
        if (hypothesis.contains(WordUtils.HELLO)) {
            if (contactList.size() > 0) {
                for (Contact contact : contactList) {
                    if (contact != null && !TextUtils.isEmpty(contact.getDisplayName())) {

                        if (contact.getDisplayName().contains(WordUtils.TWO) || contact.getDisplayName().contains(WordUtils.HELLO)
                                || contact.getDisplayName().contains(WordUtils.ONE) || contact.getDisplayName().contains(WordUtils.STOP) || contact.getDisplayName().contains(WordUtils.THANK_YOU)) {
                            CallActivityPermissionsDispatcher.callPersonWithPermissionCheck(CallActivity.this,
                                    contact.getPhoneNumbers().get(0).getNumber());
                            break;
                        }
                    }
                }

            }
        }else{
            if(!getPlayer().isPlaying()){
                playSound(Player.REPEAT);

            }
        }
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





