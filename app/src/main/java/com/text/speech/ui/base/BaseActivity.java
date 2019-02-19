package com.text.speech.ui.base;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;

import com.text.speech.R;
import com.text.speech.media.Player;
import com.text.speech.ui.dialogs.InfoConfirmDialog;
import com.text.speech.utils.NotificationUtils;
import com.text.speech.utils.PocketSphinxUtil;

import java.io.File;
import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

@RuntimePermissions
public abstract class BaseActivity extends AppCompatActivity {
    private static final String TAG = "BaseActivity";
    private PocketSphinxUtil pocketSphinxUtil = new PocketSphinxUtil();
    private CompositeDisposable disposable = new CompositeDisposable();

    private Player player;

    private boolean isInitialized;


    protected void initRecognizerWithPermissionCheck() {
        BaseActivityPermissionsDispatcher.initRecognizerWithPermissionCheck(this);
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!disposable.isDisposed()) {
            disposable.dispose();
        }
        pocketSphinxUtil.shutDown();

        if (player != null) {
            player.destroy();
        }
    }

    void startListening() {
        pocketSphinxUtil.setListener(listener);
        if (isInitialized) {
            pocketSphinxUtil.startListening();
        } else {
            NotificationUtils.notifyUser(this, "Speech recognizer not initialized");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        BaseActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }


    private PocketSphinxUtil.Listener listener = new PocketSphinxUtil.Listener() {
        @Override
        public void onTimeOut() {
            Log.d(TAG, "onTimeOut: ");
            startListening();
        }

        @Override
        public void onError(Exception e) {
            Log.e(TAG, "onError: ", e);
        }

        @Override
        public void onResult(String hypothesis) {
            NotificationUtils.notifyUser(BaseActivity.this, "Recognized: " + hypothesis);
        }
    };


    private void handleErrors(Throwable e) {
        NotificationUtils.notifyUser(this, getString(R.string.error_occurred));
        Log.e(TAG, "onCreate: ", e);
        hideProgress();
        isInitialized = false;
    }


    protected void playSound(String fileName) {
        try {
            player = Player.getInstance(this, fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected abstract void hideProgress();

    protected abstract void showProgress();

}
