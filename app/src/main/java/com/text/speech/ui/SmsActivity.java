package com.text.speech.ui;

import android.Manifest;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.text.speech.R;
import com.text.speech.contacts.ContactManager;
import com.text.speech.contacts.data.Contact;
import com.text.speech.media.Player;
import com.text.speech.ui.base.BaseActivity;
import com.text.speech.ui.dialogs.InfoConfirmDialog;
import com.text.speech.utils.NotificationUtils;
import com.text.speech.utils.PocketSphinxUtil;
import com.text.speech.utils.SmsUtils;
import com.text.speech.utils.WordUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class SmsActivity extends BaseActivity {
    private static final String TAG = "SmsActivity";
    private Button btn2;

    private ContactManager contactManager = new ContactManager();
    private CompositeDisposable disposable = new CompositeDisposable();
    private List<Contact> contactList = new ArrayList<>();
    private ProgressBar progressBar;
    private TextView logTextView;
    private boolean handled; //used to prevent the app from starting recognition after the right word has been identified

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);
        btn2 = (Button) findViewById(R.id.button2);
        progressBar = findViewById(R.id.progress_bar);
        logTextView = findViewById(R.id.tv_logs);

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick: " + contactList.size());
                if (contactList.size() > 0) {
                    for (Contact contact : contactList) {
                        if (contact != null && !TextUtils.isEmpty(contact.getDisplayName())) {

                            if (contact.getDisplayName().toLowerCase().contains(WordUtils.TWO)
                                    || contact.getDisplayName().toLowerCase().contains(WordUtils.HELLO)
                                    || contact.getDisplayName().toLowerCase().contains(WordUtils.ONE)
                                    || contact.getDisplayName().toLowerCase().contains(WordUtils.STOP)
                                    || contact.getDisplayName().toLowerCase().contains(WordUtils.THANK_YOU)) {
                                Log.i(TAG, "onClick: Match found " + contact);
                                SmsActivityPermissionsDispatcher.sendSmsToPersonWithPermissionCheck(SmsActivity.this,
                                        contact.getPhoneNumbers().get(0).getNumber(), "Abuo daalu");
                                NotificationUtils.notifyUser(SmsActivity.this, "Sms sending...");
                                break;
                            } else {
                                Log.i(TAG, "handleResult: No name match:  " + contact.getDisplayName());
                            }
                        } else {
                            Log.i(TAG, "handleResult: Contact name is null");
                        }
                    }

                } else {
                    NotificationUtils.notifyUser(SmsActivity.this, "No number selected");
                }
            }
        });


        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        populateTable(getString(R.string.kwusi_eng), getString(R.string.kwusi), getString(R.string.kwusi_phone));

    }

    @Override
    protected void onResume() {
        super.onResume();
        handled = false;
        playSound(Player.WHO_YOU_WANT_TO_MESSAGE);


        getPlayer().setListener(new Player.PlayerListener() {
            @Override
            public void onPlayEnd() {
                getPlayer().setListener(null);
                initRecognizerWithPermissionCheck(PocketSphinxUtil.STOP);
            }

            @Override
            public void onPlayStart() {

            }
        });

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
    protected void handleResult(String hypothesis) {
        Log.i(TAG, "handleResult: " + hypothesis);
        if (hypothesis.equals(WordUtils.STOP)) {
            handled = true;
            if (contactList.size() > 0) {
                for (Contact contact : contactList) {
                    if (contact != null && !TextUtils.isEmpty(contact.getDisplayName())) {

                        if (contact.getDisplayName().toLowerCase().contains(WordUtils.TWO)
                                || contact.getDisplayName().toLowerCase().contains(WordUtils.HELLO)
                                || contact.getDisplayName().toLowerCase().contains(WordUtils.ONE)
                                || contact.getDisplayName().toLowerCase().contains(WordUtils.STOP)
                                || contact.getDisplayName().toLowerCase().contains(WordUtils.THANK_YOU)) {

                            Log.i(TAG, "onClick: Match found " + contact);
                            SmsActivityPermissionsDispatcher.sendSmsToPersonWithPermissionCheck(SmsActivity.this,
                                    contact.getPhoneNumbers().get(0).getNumber(), "Abuo daalu");
                            NotificationUtils.notifyUser(SmsActivity.this, "Sms sending...");
                            break;
                        } else {
                            Log.i(TAG, "handleResult: No name match:  " + contact.getDisplayName());
                        }
                    } else {
                        Log.i(TAG, "handleResult: Contact name is null");
                    }
                }
            }

        } else {
            if (!getPlayer().isPlaying()) {
                playSound(Player.REPEAT);
            }

            startListening(PocketSphinxUtil.STOP);
        }
    }

    @Override
    protected void handleEndSpeech() {
        new Handler().postDelayed(() ->{
            if(!handled){
                startListeningWithTimeout(getPocketSphinxUtil().getRecognizer().getSearchName());
            }
        }, 1000);

        logTextView.setText(R.string.speech_ended);
        new Handler().postDelayed(()->{
            logTextView.setText("");
        }, 2000);
        Log.i(TAG, "handleEndSpeech: ");
    }

    @Override
    protected void handleStartSpeech() {
        logTextView.setText(R.string.speech_started);
    }

    @Override
    protected void handleTimeOut() {
        new Handler().postDelayed(() -> startListening(PocketSphinxUtil.STOP), 1000);
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
