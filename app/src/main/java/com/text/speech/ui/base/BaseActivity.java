package com.text.speech.ui.base;

import android.Manifest;
import android.util.Log;

import com.text.speech.R;
import com.text.speech.media.Player;
import com.text.speech.ui.dialogs.InfoConfirmDialog;
import com.text.speech.utils.NotificationUtils;
import com.text.speech.utils.PocketSphinxUtil;

import java.io.File;
import java.io.IOException;

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

@RuntimePermissions
public abstract class BaseActivity extends AppCompatActivity {
    private static final String TAG = "BaseActivity";
    private PocketSphinxUtil pocketSphinxUtil = new PocketSphinxUtil();
    private CompositeDisposable disposable = new CompositeDisposable();

    private Player player;

    private boolean isInitialized;


    public Player getPlayer() {
        return player;
    }

    public PocketSphinxUtil getPocketSphinxUtil() {
        return pocketSphinxUtil;
    }

    protected void initRecognizerWithPermissionCheck() {
        BaseActivityPermissionsDispatcher.initRecognizerWithPermissionCheck(this);
    }

    protected void initRecognizerWithPermissionCheck(String keyword) {
        BaseActivityPermissionsDispatcher.initRecognizerWithKeyWordWithPermissionCheck(this, keyword);
    }

    protected void initRecognizerWithPermissionCheckTimeout(String keyword) {
        BaseActivityPermissionsDispatcher.initRecognizerWithKeyWordAndTimeoutWithPermissionCheck(this, keyword);
    }

    @NeedsPermission({Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void initRecognizer() {
        Log.d(TAG, "onCreate: Initializing recognizer...");
        try {
            showProgress();
            Assets assets = new Assets(this);
            File assetDir = assets.syncAssets();
            disposable.addAll(pocketSphinxUtil.init(assetDir)
                    .subscribeOn(Schedulers.newThread())
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

    @NeedsPermission({Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void initRecognizerWithKeyWord(String keyword) {
        Log.d(TAG, "onCreate: Initializing recognizer...");
        try {
            showProgress();
            Assets assets = new Assets(this);
            File assetDir = assets.syncAssets();
            disposable.addAll(pocketSphinxUtil.init(assetDir)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> {
                        hideProgress();
                        isInitialized = true;
                        Log.d(TAG, "onCreate: Recognizer initialization complete");
                        startListening(keyword);
                    }, this::handleErrors));
        } catch (IOException e) {
            handleErrors(e);
        }
    }

    @NeedsPermission({Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void initRecognizerWithKeyWordAndTimeout(String keyword) {
        Log.d(TAG, "onCreate: Initializing recognizer...");
        try {
            showProgress();
            Assets assets = new Assets(this);
            File assetDir = assets.syncAssets();
            disposable.addAll(pocketSphinxUtil.init(assetDir)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> {
                        hideProgress();
                        isInitialized = true;
                        Log.d(TAG, "onCreate: Recognizer initialization complete");
                        startListeningWithTimeout(keyword);
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
    protected void onPause() {
        super.onPause();

        pocketSphinxUtil.shutDown();

        if (player != null) {
            player.destroy();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    protected void startListening() {
        pocketSphinxUtil.setListener(listener);
        if (isInitialized) {
            pocketSphinxUtil.startListening();
        } else {
            NotificationUtils.notifyUser(this, "Speech recognizer not initialized");
        }
    }

    protected void startListening(String keyword) {
        pocketSphinxUtil.setListener(listener);
        if (isInitialized) {
            pocketSphinxUtil.startListening(keyword);
        } else {
            NotificationUtils.notifyUser(this, "Speech recognizer not initialized");
        }
    }


    protected void startListeningWithTimeout(String keyword) {
        pocketSphinxUtil.setListener(listener);
        if (isInitialized) {
            pocketSphinxUtil.startListeningWithTimeout(keyword);
        } else {
            NotificationUtils.notifyUser(this, "Speech recognizer not initialized");
        }
    }


    protected void stopListening() {
       pocketSphinxUtil.stopListening();
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
            stopListening();
            handleTimeOut();
//            new Handler().postDelayed(() -> startListening(PocketSphinxUtil.THANK_YOU), 1000);
        }

        @Override
        public void onError(Exception e) {
            stopListening();
            handleEndSpeech();
//            new Handler().postDelayed(() -> startListening(PocketSphinxUtil.THANK_YOU), 1000);
            Log.e(TAG, "onError: ", e);
        }

        @Override
        public void onResult(String hypothesis) {
            handleResult(hypothesis);
//            NotificationUtils.notifyUser(BaseActivity.this, "Recognized: " + hypothesis);
        }

        @Override
        public void onStartSpeech() {
            handleStartSpeech();
        }

        @Override
        public void onEndSpeech() {
            handleEndSpeech();
        }
    };

    protected abstract void handleStartSpeech();

    protected abstract void handleTimeOut();

    protected abstract void handleEndSpeech();

    protected abstract void handleResult(String hypothesis);


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
            NotificationUtils.notifyUser(this, getString(R.string.error_occurred) );
        }
    }

    protected abstract void hideProgress();

    protected abstract void showProgress();

}
