package com.text.speech.ui;

import android.Manifest;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import edu.cmu.pocketsphinx.Assets;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.text.speech.R;
import com.text.speech.ui.dialogs.InfoConfirmDialog;
import com.text.speech.utils.NotificationUtils;
import com.text.speech.utils.PocketSphinxUtil;

import java.io.File;
import java.io.IOException;

@RuntimePermissions
public class ChooseActionActivity extends AppCompatActivity {
    private static final String TAG = "ChooseActionActivity";
    private PocketSphinxUtil pocketSphinxUtil = new PocketSphinxUtil();
    private CompositeDisposable disposable = new CompositeDisposable();
    private ProgressBar progressBar;
    private boolean isInitialized;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_action);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setTitle(getString(R.string.choose_action));
        }
        progressBar = findViewById(R.id.progress_bar);

        ChooseActionActivityPermissionsDispatcher.initRecognizerWithPermissionCheck(this);

    }


    @NeedsPermission({Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE})
     void initRecognizer() {
        Log.d(TAG, "onCreate: Initializing recognizer...");
        try {
            showProgress();
            Assets assets = new Assets(this);
            File assetDir = assets.syncAssets();
            disposable.add(pocketSphinxUtil.init(assetDir)
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> {
                        hideProgress();
                        isInitialized = true;
                        Log.d(TAG, "onCreate: Recognizer initialization complete");
                        startListening();
                    }, this::handleErrors));
        } catch (IOException e) {
            handleErrors(e);
        }
    }


    @OnShowRationale({Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void showInitRecognizerRationale(PermissionRequest request) {
        InfoConfirmDialog.showDialog(this, R.string.allow_permission, R.string.allow_recognizer_permission_msg, R.drawable.ic_voice_white, request::proceed);
    }

    @OnPermissionDenied({Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void showDeniedForInitRecognizer() {
        NotificationUtils.notifyUser(this, getString(R.string.recognizer_permission_denied));
    }

    @OnNeverAskAgain({Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void neverAskForInitRecognizer() {
        NotificationUtils.notifyUser(this, getString(R.string.recognizer_permission_never_ask));
    }



    private void handleErrors(Throwable e) {
        NotificationUtils.notifyUser(this, getString(R.string.error_occurred));
        Log.e(TAG, "onCreate: ",e );
        hideProgress();
        isInitialized = false;
    }

    private void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    private void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    public void onClickCall(View view){
        Intent intent = new Intent(this,CallActivity.class);
        startActivity(intent);
    }
    public void onClickSms(View view){
        Intent intent = new Intent(this,SmsActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!disposable.isDisposed()){
            disposable.dispose();
        }
        pocketSphinxUtil.shutDown();
    }

    void startListening(){
        pocketSphinxUtil.setListener(listener);
        if(isInitialized){
            pocketSphinxUtil.startListening();
        }else{
            NotificationUtils.notifyUser(this, "Speech recognizer not initialized");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ChooseActionActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }



    private PocketSphinxUtil.Listener listener = new PocketSphinxUtil.Listener() {
        @Override
        public void onTimeOut() {
            Log.d(TAG, "onTimeOut: ");
            startListening();
        }

        @Override
        public void onError(Exception e) {
            Log.e(TAG, "onError: ",e );
        }

        @Override
        public void onResult(String hypothesis) {
            NotificationUtils.notifyUser(ChooseActionActivity.this, "Recognized: " + hypothesis);
        }
    };
}
